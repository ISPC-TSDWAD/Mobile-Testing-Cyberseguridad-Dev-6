# Pruebas de API — Postman y Newman

Colección de 47 requests: Auth (6), Usuarios (18) y Recursos (23). Utiliza cuentas de prueba admin y asesor; crea fixtures propios y comprueba su identidad antes de eliminarlos.

## Ejecución

Requisitos: Node.js y Newman 6. Desde la raíz:

```powershell
npm install --prefix app/build/newman-runtime --no-save --package-lock=false --ignore-scripts newman@6
node tests/api/ejecutar_newman.cjs
```

Copiar el entorno público a `tests/api/dev6.local.postman_environment.json` y completar `username`, `password`, `usernameAsesor` y `passwordAsesor`. El archivo privado está ignorado por Git. No publicar credenciales, tokens ni exportaciones completas de Newman.

El ejecutor publica únicamente resultados sanitizados en `reporte_newman.json`. La última corrida registró 47 requests y 208 comprobaciones: 204 aprobadas y 4 fallas de latencia; ninguna falla funcional o de contrato. Limpieza sin fixtures pendientes ni errores.

Para verificar el rol maquetador:

```powershell
python tests/api/verificar_maquetador.py
```

La verificación del servidor aprobó 13 comprobaciones y eliminó sus fixtures. Detalle y límites: [Resultados API](../../docs/testing/RESULTADOS_API.md).
