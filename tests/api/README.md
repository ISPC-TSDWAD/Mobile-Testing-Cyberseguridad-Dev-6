# Pruebas de API (Postman + Newman)

Autor: Roni Duncan

## Archivos
- `EduTools.postman_collection.json`: colección v2.1 con carpetas Auth, Usuarios y Recursos
- `dev6.postman_environment.json`: entorno (token y credenciales vacíos)
- `reporte_newman.json`: último reporte (sanitizado: sin tokens ni claves)
- `generar_postman.js`: script que regenera la colección y el entorno

## Variables del entorno
| Variable | Uso |
|---|---|
| baseUrl | https://dev6.pythonanywhere.com |
| username / password | usuario admin (se piden al equipo por privado) |
| usernameAsesor / passwordAsesor | usuario asesor (para los casos de 403) |
| token, refresh, tokenAsesor | las completan los tests del login |
| userId, categoriaId, asignaturaId, recursoId | las completan los tests encadenados |

## Cómo ejecutar

    npm install -g newman
    newman run tests/api/EduTools.postman_collection.json \
      -e tests/api/dev6.postman_environment.json \
      --env-var "username=<ADMIN>" --env-var "password=<CLAVE>" \
      --env-var "usernameAsesor=<ASESOR>" --env-var "passwordAsesor=<CLAVE>" \
      --reporters cli,json \
      --reporter-json-export tests/api/reporte_newman.json

Las credenciales nunca se guardan en el repo. Antes de commitear un reporte, hay que sanitizarlo (JWT y claves).

## Qué valida cada request
Código de estado, Content-Type JSON, campos y tipos del cuerpo y tiempo de respuesta < 800 ms.
Negativos: 400 (payload inválido, login sin password, rol inexistente, contraseña actual incorrecta), 401 (sin token, login incorrecto), 403 (asesor en rutas de admin), 404 (id inexistente o recurso borrado).
