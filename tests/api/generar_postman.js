// Genera la coleccion Postman v2.1 y el entorno de EduTools (autor: Roni Duncan).
// Uso: node tests/api/generar_postman.js
console.error('Generador anterior deshabilitado: sobrescribe la coleccion de entrega. Usar la coleccion actual y ejecutar_newman.cjs; ver README.md.');
process.exit(2);
const fs = require('fs');

const U = (p) => ({ raw: '{{baseUrl}}' + p, host: ['{{baseUrl}}'], path: p.split('/').slice(1) });
const bearer = (v) => ({ type: 'bearer', bearer: [{ key: 'token', value: v, type: 'string' }] });
const sinToken = { type: 'noauth' };
const comoAsesor = bearer('{{tokenAsesor}}');

const basicos = (status, json = true) => {
  const t = [`pm.test("Código de estado ${status}", () => pm.response.to.have.status(${status}));`];
  if (json) t.push('pm.test("Content-Type es JSON", () => pm.expect(pm.response.headers.get("Content-Type") || "").to.include("application/json"));');
  t.push('pm.test("Tiempo de respuesta < 800 ms", () => pm.expect(pm.response.responseTime).to.be.below(800));');
  return t;
};

const campos = (spec, fuente = 'pm.response.json()') => [
  'pm.test("Campos y tipos del cuerpo", () => {',
  `  const j = ${fuente};`,
  ...Object.entries(spec).map(([k, t]) => `  pm.expect(j).to.have.property("${k}"); pm.expect(j.${k}).to.be.a("${t}");`),
  '});',
];

const detail = ['pm.test("Trae mensaje detail (string)", () => pm.expect(pm.response.json().detail).to.be.a("string"));'];
const noEncontrado = ['pm.test("detail = No encontrado.", () => pm.expect(pm.response.json().detail).to.eql("No encontrado."));'];
const lista = 'const lista = () => { const d = pm.response.json(); return Array.isArray(d) ? d : d.results; };';
const tieneCampo = (c) => [`pm.test("El error indica el campo ${c}", () => pm.expect(pm.response.json()).to.have.property("${c}"));`];

const item = (name, method, ruta, { body, auth, tests = [] } = {}) => ({
  name,
  event: [{ listen: 'test', script: { type: 'text/javascript', exec: tests } }],
  request: {
    method,
    ...(auth ? { auth } : {}),
    header: body ? [{ key: 'Content-Type', value: 'application/json' }] : [],
    ...(body ? { body: { mode: 'raw', raw: typeof body === 'string' ? body : JSON.stringify(body, null, 2), options: { raw: { language: 'json' } } } } : {}),
    url: U(ruta),
  },
});

const USER = { id: 'number', username: 'string', email: 'string', first_name: 'string', last_name: 'string', is_staff: 'boolean', is_active: 'boolean', rol: 'string' };
const RECURSO = { id: 'number', titulo: 'string', descripcion: 'string', tipo: 'string', categoria: 'number', asignatura: 'number', es_plantilla_base: 'boolean', activo: 'boolean', creado_en: 'string' };
const ROLES = '["admin","asesor","maquetador"]';

const loginOk = (varToken, varRefresh) => [
  ...basicos(200),
  ...campos({ access: 'string', refresh: 'string', is_staff: 'boolean', rol: 'string' }),
  `pm.test("Rol válido", () => pm.expect(${ROLES}).to.include(pm.response.json().rol));`,
  `pm.environment.set("${varToken}", pm.response.json().access);`,
  ...(varRefresh ? [`pm.environment.set("${varRefresh}", pm.response.json().refresh);`] : []),
];

const cuerpoRecurso = (titulo) => `{
  "titulo": "${titulo} {{$timestamp}}",
  "descripcion": "Creado por Newman",
  "tipo": "lectura",
  "categoria": {{categoriaId}},
  "asignatura": {{asignaturaId}}
}`;

const auth = {
  name: 'Auth',
  item: [
    item('Login admin (200)', 'POST', '/api/token/', { auth: sinToken, body: { username: '{{username}}', password: '{{password}}' }, tests: loginOk('token', 'refresh') }),
    item('Login asesor (200)', 'POST', '/api/token/', { auth: sinToken, body: { username: '{{usernameAsesor}}', password: '{{passwordAsesor}}' }, tests: loginOk('tokenAsesor') }),
    item('Login credenciales incorrectas (401)', 'POST', '/api/token/', { auth: sinToken, body: { username: '{{username}}', password: '{{$randomPassword}}' }, tests: [...basicos(401), ...detail] }),
    item('Login sin password (400)', 'POST', '/api/token/', { auth: sinToken, body: { username: '{{username}}' }, tests: [...basicos(400), ...tieneCampo('password')] }),
    item('Refresh token (200)', 'POST', '/api/token/refresh/', { auth: sinToken, body: { refresh: '{{refresh}}' }, tests: [...basicos(200), ...campos({ access: 'string' })] }),
  ],
};

const usuarios = {
  name: 'Usuarios',
  item: [
    item('Mi usuario - GET me (200)', 'GET', '/api/users/me/', { tests: [
      ...basicos(200), ...campos(USER),
      'pm.test("Nunca devuelve la contraseña", () => pm.expect(pm.response.json()).to.not.have.property("password"));',
      'pm.environment.set("userId", pm.response.json().id);',
    ] }),
    item('Mi usuario sin token (401)', 'GET', '/api/users/me/', { auth: sinToken, tests: [...basicos(401), ...detail] }),
    item('Listar usuarios como admin (200)', 'GET', '/api/users/', { tests: [
      ...basicos(200), lista,
      'pm.test("Devuelve una lista no vacía", () => { pm.expect(lista()).to.be.an("array").that.is.not.empty; });',
    ] }),
    item('Listar usuarios como asesor (403)', 'GET', '/api/users/', { auth: comoAsesor, tests: [...basicos(403), ...detail] }),
    item('Usuario inexistente (404)', 'GET', '/api/users/99999999/', { tests: [...basicos(404), ...noEncontrado] }),
    item('Cambiar contraseña con la actual incorrecta (400)', 'POST', '/api/users/change-password/', { body: { actual: '{{$randomPassword}}', nueva: '{{$randomPassword}}' }, tests: [...basicos(400), ...detail] }),
    item('Set-role con rol inexistente (400)', 'POST', '/api/users/{{userId}}/set-role/', { body: { rol: 'docente' }, tests: [...basicos(400), ...tieneCampo('rol')] }),
    item('Set-role como asesor (403)', 'POST', '/api/users/{{userId}}/set-role/', { auth: comoAsesor, body: { rol: 'admin' }, tests: [...basicos(403), ...detail] }),
  ],
};

const recursos = {
  name: 'Recursos',
  item: [
    item('Categorías - GET lista (guarda categoriaId)', 'GET', '/api/pedagogia/categorias/', { tests: [
      ...basicos(200), lista,
      'pm.test("Hay al menos una categoría", () => pm.expect(lista()).to.be.an("array").that.is.not.empty);',
      'pm.environment.set("categoriaId", lista()[0].id);',
    ] }),
    item('Asignaturas - GET lista (guarda asignaturaId)', 'GET', '/api/pedagogia/asignaturas/', { tests: [
      ...basicos(200), lista,
      'pm.test("Hay al menos una asignatura", () => pm.expect(lista()).to.be.an("array").that.is.not.empty);',
      'pm.environment.set("asignaturaId", lista()[0].id);',
    ] }),
    item('1. POST crear recurso (201, guarda recursoId)', 'POST', '/api/pedagogia/recursos/', { body: cuerpoRecurso('Recurso Newman'), tests: [
      ...basicos(201), ...campos(RECURSO),
      'pm.test("tipo = lectura", () => pm.expect(pm.response.json().tipo).to.eql("lectura"));',
      'pm.environment.set("recursoId", pm.response.json().id);',
    ] }),
    item('2. GET lista de recursos (200)', 'GET', '/api/pedagogia/recursos/', { tests: [
      ...basicos(200), lista,
      'pm.test("Es una lista", () => pm.expect(lista()).to.be.an("array"));',
      'pm.test("Incluye el recurso creado", () => pm.expect(lista().map(r => r.id)).to.include(Number(pm.environment.get("recursoId"))));',
    ] }),
    item('3. GET recurso por id (200)', 'GET', '/api/pedagogia/recursos/{{recursoId}}/', { tests: [
      ...basicos(200), ...campos(RECURSO),
      'pm.test("Es el recurso pedido", () => pm.expect(pm.response.json().id).to.eql(Number(pm.environment.get("recursoId"))));',
    ] }),
    item('4. PUT editar recurso (200)', 'PUT', '/api/pedagogia/recursos/{{recursoId}}/', { body: cuerpoRecurso('Recurso Newman editado'), tests: [
      ...basicos(200), ...campos(RECURSO),
      'pm.test("El título cambió", () => pm.expect(pm.response.json().titulo).to.include("editado"));',
    ] }),
    item('5. DELETE recurso (204)', 'DELETE', '/api/pedagogia/recursos/{{recursoId}}/', { tests: basicos(204, false) }),
    item('6. GET recurso borrado (404)', 'GET', '/api/pedagogia/recursos/{{recursoId}}/', { tests: [...basicos(404), ...noEncontrado] }),
    item('Negativo: POST payload inválido (400)', 'POST', '/api/pedagogia/recursos/', { body: { descripcion: 'sin titulo', tipo: 'no_existe' }, tests: [...basicos(400), ...tieneCampo('titulo')] }),
    item('Negativo: GET recursos sin token (401)', 'GET', '/api/pedagogia/recursos/', { auth: sinToken, tests: [...basicos(401), ...detail] }),
    item('Negativo: GET recurso inexistente (404)', 'GET', '/api/pedagogia/recursos/99999999/', { tests: [...basicos(404), ...noEncontrado] }),
  ],
};

// "lista" (la función) se inyecta como línea de script donde se usa
const aplanar = (nodo) => {
  if (nodo.item && nodo.request === undefined) { nodo.item.forEach(aplanar); return; }
  const exec = nodo.event[0].script.exec;
  nodo.event[0].script.exec = exec.map((l) => (l === lista ? lista : l));
};
[auth, usuarios, recursos].forEach(aplanar);

const coleccion = {
  info: { name: 'EduTools API', schema: 'https://schema.getpostman.com/json/collection/v2.1.0/collection.json' },
  auth: bearer('{{token}}'),
  item: [auth, usuarios, recursos],
};

const secretas = ['token', 'refresh', 'tokenAsesor', 'password', 'passwordAsesor'];
const entorno = {
  name: 'dev6',
  values: [
    { key: 'baseUrl', value: 'https://dev6.pythonanywhere.com', type: 'default', enabled: true },
    ...['token', 'refresh', 'tokenAsesor', 'username', 'password', 'usernameAsesor', 'passwordAsesor', 'userId', 'categoriaId', 'asignaturaId', 'recursoId']
      .map((k) => ({ key: k, value: '', type: secretas.includes(k) ? 'secret' : 'default', enabled: true })),
  ],
  _postman_variable_scope: 'environment',
};

fs.writeFileSync('tests/api/EduTools.postman_collection.json', JSON.stringify(coleccion, null, 2));
fs.writeFileSync('tests/api/dev6.postman_environment.json', JSON.stringify(entorno, null, 2));
console.log('Listo: coleccion y entorno generados en tests/api/');
