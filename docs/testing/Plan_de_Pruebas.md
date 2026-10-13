# Plan de Pruebas - EduTools Mobile (IEEE 829 mínimo)

Autor: Roni Duncan (Testing)

## 1. Objetivo
Verificar que la app EduTools Mobile cumple los requisitos funcionales, de seguridad y de accesibilidad al consumir el backend Django REST.

## 2. Alcance
Incluye: login, registro, usuarios, recursos (CRUD), contacto, accesibilidad y consumo de API.
No incluye: pruebas de carga ni publicación en Play Store.

## 3. Funcionalidades a probar
- Autenticación (login y registro, validaciones)
- Gestión de usuarios
- CRUD de Recursos (api/pedagogia/recursos/)
- Formulario de contacto
- Accesibilidad de las pantallas principales
- Seguridad (token, roles, permisos)

## 4. Enfoque
- Manual: exploratorio y accesibilidad (TalkBack, Accessibility Scanner)
- Automatizado: JUnit (unitarias), MockWebServer (red), Espresso (UI), Postman + Newman (API)

## 5. Criterios de aprobación
- 100% de los tests automatizados pasan
- 0 bugs críticos abiertos
- Casos de prueba ejecutados al 100%
- Mínimo 3 bugs de accesibilidad documentados o justificados

## 6. Entorno
- Android 5.0+ (minSdk 21), dispositivo físico y emulador
- API: https://dev6.pythonanywhere.com (HTTPS)
- Android Studio, Gradle 8.1, Postman, Newman

## 7. Responsables
Roni Duncan (Testing), Gonzalo (Espresso y release), Jonathan (unitarios y seguridad), Gerardo (Recursos), Daniela (seguridad y Wiki).

## 8. Cronograma
| Actividad | Responsable | Fecha |
|---|---|---|
| Plan y planilla | Roni Duncan | COMPLETAR |
| Postman y Newman | Roni Duncan | COMPLETAR |
| Accesibilidad | Roni Duncan + equipo | COMPLETAR |
| Matriz y Wiki | Roni Duncan | COMPLETAR |
