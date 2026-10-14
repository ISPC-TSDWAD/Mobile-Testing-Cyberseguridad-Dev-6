# Pruebas de API — Postman y Newman

Colección v2.1 contra `https://dev6.pythonanywhere.com`, con cuentas de prueba admin y asesor. **24 requests**: Auth (6), Usuarios (5), Recursos (13). Rutas y contrato: [ENDPOINTS.md](../../docs/api/ENDPOINTS.md).

El flujo crea, lee, edita y elimina un recurso de la corrida. Comprueba también que el asesor no pueda eliminar esa plantilla y que siga existiendo tras el 403. No modifica usuarios existentes.

## Preparación y ejecución

Requisitos: Node.js y Newman 6. Desde la raíz del repo:

```powershell
npm install --prefix app/build/newman-runtime --no-save --package-lock=false --ignore-scripts newman@6
node tests/api/ejecutar_newman.cjs
```

Crear `dev6.local.postman_environment.json` como copia del entorno público. Completar `username`, `password`, `usernameAsesor` y `passwordAsesor`. Ese archivo privado está ignorado por Git; no incluir credenciales en mensajes, capturas ni archivos públicos. El entorno público mantiene vacíos usuarios, contraseñas y tokens.

El ejecutor usa Newman como biblioteca y guarda únicamente nombre de request, estado, duración, comprobaciones y resultado en `reporte_newman.json`. No exporta cuerpos, headers ni variables privadas. No publicar el JSON completo de Newman sin sanitizar.

También se puede importar la colección y el entorno privado en Postman y usar Collection Runner, manteniendo el orden.

## Resultado del 13/10/2026

| Requests | Assertions | Fallas funcionales/contrato | Fallas de tiempo |
|---|---|---|---|
| 24 | 114 | 0 | 3 |

Sin errores de transporte. Pasaron CRUD, negativos 400/401/403/404, token manipulado y protección de plantillas base. El recurso creado fue eliminado; el GET posterior respondió 404.

El criterio de 800 ms falló en AUTH-01 (login admin, 1902 ms), AUTH-06 (login asesor, 1431 ms) y AUTH-02 (credenciales inválidas, 1377 ms). **Resultado global: con fallas de SLA.** Se mantiene el límite y se conservan las fallas en el [reporte sanitizado](reporte_newman.json).

## Mantenimiento

Modificar la colección exportada y verificar los scripts. El antiguo `generar_postman.js` está deshabilitado porque sobrescribiría los casos y IDs de trazabilidad actuales.
