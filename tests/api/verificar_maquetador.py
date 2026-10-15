"""Verifica el rol con fixtures propios; solo exporta estados y resultados."""
import json
import secrets
import urllib.request
import urllib.error
from pathlib import Path
from datetime import datetime, timezone

ROOT = Path(__file__).resolve().parents[2]
values = {v['key']: v['value'] for v in json.loads(
    (ROOT / 'tests/api/dev6.local.postman_environment.json').read_text(encoding='utf-8-sig'))['values']}
base = values['baseUrl']
if base != 'https://dev6.pythonanywhere.com':
    raise RuntimeError('Usar el servidor de pruebas HTTPS de EduTools')
results, fixtures = [], []
suffix = secrets.token_hex(6)
username, password = 'qa_maquetador_' + suffix, 'QA-' + secrets.token_urlsafe(24) + '-9!'
name = 'QA maquetador ' + suffix

def http(method, route, token=None, body=None):
    headers = {'Content-Type': 'application/json'}
    if token: headers['Authorization'] = 'Bearer ' + token
    request = urllib.request.Request(base + route, method=method, headers=headers,
        data=json.dumps(body).encode() if body is not None else None)
    try:
        with urllib.request.urlopen(request, timeout=20) as response:
            raw = response.read()
            return response.status, json.loads(raw) if raw else None
    except urllib.error.HTTPError as error:
        return error.code, None

def check(label, actual, expected, condition=True):
    results.append({'case': label, 'expected': expected, 'actual': actual,
                    'passed': actual == expected and bool(condition)})
    if not results[-1]['passed']: raise RuntimeError('Falló la verificación: ' + label)

def fixture(route, data, field, expected):
    fixtures.append((route + str(data['id']) + '/', field, expected))

admin = None
cleanup_errors = 0
started = datetime.now(timezone.utc).isoformat()
try:
    status, data = http('POST', '/api/token/', body={'username': values['username'], 'password': values['password']})
    check('Login admin', status, 200)
    admin = data['access']
    status, user = http('POST', '/api/users/', admin, {'username': username, 'password': password})
    check('Crear cuenta temporal', status, 201)
    fixture('/api/users/', user, 'username', username)
    status, data = http('POST', f"/api/users/{user['id']}/set-role/", admin, {'rol': 'maquetador'})
    check('Asignar rol maquetador', status, 200, data and data.get('rol') == 'maquetador')
    status, data = http('POST', '/api/token/', body={'username': username, 'password': password})
    check('Login maquetador y claim', status, 200, data and data.get('rol') == 'maquetador')
    token = data['access']
    status, data = http('GET', '/api/pedagogia/categorias/', token)
    check('Leer categorías', status, 200, isinstance(data, list))
    status, category = http('POST', '/api/pedagogia/categorias/', token, {'nombre': name})
    check('Crear categoría', status, 201)
    fixture('/api/pedagogia/categorias/', category, 'nombre', name)
    status, subject = http('POST', '/api/pedagogia/asignaturas/', token, {'nombre': name})
    check('Crear asignatura', status, 201)
    fixture('/api/pedagogia/asignaturas/', subject, 'nombre', name)
    status, resource = http('POST', '/api/pedagogia/recursos/', token,
        {'titulo': name, 'tipo': 'lectura', 'categoria': category['id'], 'asignatura': subject['id'], 'contenido': '<p>Prueba de rol</p>'})
    check('Crear plantilla como maquetador', status, 201, resource and resource.get('es_plantilla_base') is True)
    fixture('/api/pedagogia/recursos/', resource, 'titulo', name)
    status, data = http('POST', f"/api/pedagogia/recursos/{resource['id']}/set-plantilla/", token, {'es_plantilla_base': False})
    check('Desmarcar plantilla', status, 200, data and data.get('es_plantilla_base') is False)
    status, data = http('PATCH', f"/api/pedagogia/recursos/{resource['id']}/", token, {'descripcion': 'Edición de prueba'})
    check('Editar recurso', status, 200, data and data.get('descripcion') == 'Edición de prueba')
    status, _ = http('DELETE', f"/api/pedagogia/recursos/{resource['id']}/", token)
    check('Eliminar recurso propio', status, 204)
    status, _ = http('DELETE', f"/api/pedagogia/categorias/{category['id']}/", token)
    check('Eliminar categoría libre', status, 204)
    status, _ = http('DELETE', f"/api/pedagogia/asignaturas/{subject['id']}/", token)
    check('Eliminar asignatura libre', status, 204)
finally:
    if admin:
        for route, field, expected in reversed(fixtures):
            try:
                status, data = http('GET', route, admin)
                if status == 404: continue
                if status != 200 or not data or data.get(field) != expected:
                    cleanup_errors += 1
                    continue
                status, _ = http('DELETE', route, admin)
                if status != 204: cleanup_errors += 1
            except Exception: cleanup_errors += 1
    report = {'metadata': {'started': started, 'finished': datetime.now(timezone.utc).isoformat(),
        'baseUrl': base, 'sanitized': True, 'cleanupErrors': cleanup_errors}, 'results': results}
    (ROOT / 'tests/api/reporte_maquetador.json').write_text(json.dumps(report, ensure_ascii=False, indent=2) + '\n', encoding='utf-8')
    print(f"Maquetador: {sum(r['passed'] for r in results)}/{len(results)} casos; errores de limpieza: {cleanup_errors}")
if cleanup_errors: raise SystemExit(1)
