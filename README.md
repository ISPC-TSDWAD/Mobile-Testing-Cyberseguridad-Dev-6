# EduTools Mobile 📱

Cliente Android de **EduTools**, la plataforma de gestión y sandbox pedagógico desarrollada por el equipo **DEV6** para optimizar el flujo de trabajo entre Asesoría Pedagógica y Maquetación en entornos de educación a distancia sobre Canvas LMS.

La aplicación móvil consume la misma API REST en Django construida durante el módulo Programador Web, de modo que la información queda sincronizada entre el cliente web y el móvil.

> **Estado actual: Sprint 2 (Evidencia 3).** La aplicación consume la API del backend por HTTPS: inicio de sesión real con JWT, roles leídos del token, CRUD de usuarios (solo admin), CRUD de recursos pedagógicos, Registro con contraseña robusta y token guardado de forma cifrada desde Android 6. En Android 5 la sesión se conserva solo en memoria. El detalle está en la [Wiki](../../wiki).

---

## Equipo DEV6

| Integrante | Rol | Responsabilidad en el Sprint 1 |
|:---|:---|:---|
| Gonzalo Velasco | Scrum Master · Developer | Módulo de recursos, modelos de datos y gestión de la Wiki |
| Jonathan Guillén | Líder Técnico · Developer | Configuración del proyecto, manifiesto, seguridad de red y code reviews |
| Ale Corva | Developer | Navegación base, pantalla de acceso y menú principal |
| Daniela Salvo | Technical Writer & Designer | Módulo de usuarios, design tokens y ANEXO II de capturas |
| Gerardo Romero | Developer | Multimedia, contacto, institucional e iconografía |
| Roni Duncan Gonzales Martínez | QA Tester | Plan de Pruebas, accesibilidad y compatibilidad con API 21 |

---

## Funcionalidades de la aplicación

| Módulo | Pantallas | Estado |
|:---|:---|:---|
| **Acceso** | Inicio de sesión · Registro | Login real con JWT · validación por campo · contraseña robusta · sesión cifrada que se restaura al abrir la app |
| **Home** | Menú principal con acceso a los cinco módulos | Navegable |
| **Recursos pedagógicos** | Listado · Detalle del recurso · Formulario de alta y edición | CRUD contra la API · catálogos reales · validación antes de guardar |
| **Usuarios** | Listado de usuarios · Perfil · Alta y edición | CRUD contra la API (solo admin; el asesor recibe 403) |
| **Guía multimedia** | Video de los estándares institucionales e imagen de la paleta | Video incluido · reproducción manual · imagen funcional |
| **Contacto** | Formulario de demostración | Validación por campo · no envía ni guarda consultas · canal de soporte pendiente |
| **Quiénes somos** | Identidad del proyecto y equipo | Funcional |

---

## Stack técnico

| Elemento | Valor |
|:---|:---|
| Lenguaje | Java 17 |
| `minSdk` | 21 — Android 5.0 Lollipop |
| `targetSdk` | 34 — Android 14 |
| Interfaz | Material Components · RecyclerView · CardView |
| Gradle Plugin | 8.1.4 |
| Backend | Django REST Framework · MySQL |
| Autenticación | JWT (SimpleJWT) · token en `EncryptedSharedPreferences` |

---

## Cómo ejecutar el proyecto

### Requisitos

- Android Studio Giraffe (2022.3.1) o superior
- JDK 17 (Eclipse Temurin)
- Un dispositivo con Android 5.0 o superior, o un emulador equivalente

### Pasos

```bash
git clone https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6.git
```

1. Abrir la carpeta en Android Studio y esperar la sincronización de Gradle.
2. El video de presentación ya está incluido en `app/src/main/res/raw/presentacion_edutools.mp4`.
3. Seleccionar el dispositivo o emulador y ejecutar **Run ▸ app**.

### Generar el APK de depuración

```
Build ▸ Build Bundle(s) / APK(s) ▸ Build APK(s)
```

El archivo queda en `app/build/outputs/apk/debug/app-debug.apk`.

---

## Estructura del proyecto

```
app/src/main/
├── AndroidManifest.xml          11 Activities; las hijas declaran parentActivityName
├── java/ar/edu/ispc/edutools/
│   ├── BaseActivity.java        Toolbar y navegación Up centralizadas
│   ├── *Activity.java           Las once pantallas de la aplicación
│   ├── adapter/                 RecursoAdapter · UsuarioAdapter
│   └── model/                   Recurso · UsuarioItem · DatosDemo
└── res/
    ├── layout/                  Layouts de pantallas e ítems de lista
    ├── values/                  strings · colors · themes (design tokens)
    ├── drawable/                Íconos vectoriales propios
    ├── xml/                     network_security_config.xml
    └── raw/                     presentacion_edutools.mp4
```

---

## Accesibilidad y usabilidad

La interfaz aplica criterios de WCAG 2.2 nivel AA:

- Descripción textual en todo ícono e imagen informativa.
- Área táctil mínima de 48 dp en controles y 72 dp en filas de listas.
- Relación de contraste de 15.1:1 en el texto principal y 10.4:1 en el texto sobre el color institucional.
- Tamaños de texto en `sp`, que respetan la escala de fuente configurada en el sistema.
- El estado de un usuario se comunica con texto, no únicamente con color.
- Cada fila de lista se anuncia en un solo foco con toda su información.

El resultado de Accessibility Scanner y las correcciones están en [docs/accesibilidad](docs/accesibilidad/INFORME_ACCESIBILIDAD.md); las pruebas en dispositivo, en [docs/pruebas/PRUEBAS_MANUALES.md](docs/pruebas/PRUEBAS_MANUALES.md).

---

## Seguridad

- Tráfico en texto plano prohibido por configuración; sin excepciones: se eliminó la del host de desarrollo local.
- Copia de seguridad de la aplicación deshabilitada (`allowBackup="false"`).
- Ninguna Activity interna se exporta.
- Claves de firma, keystores y archivos de entorno excluidos del control de versiones.
- Los pedidos protegidos con respuesta 401 borran la sesión y vuelven al login; un 403 informa falta de permisos sin cerrar la sesión.
- El refresh token se guarda, pero la renovación automática queda pendiente para Sprint 3. Al vencer el access, se solicita iniciar sesión nuevamente.

El detalle completo de políticas y la fundamentación de la elección de JWT están en la [página de Ciberseguridad de la Wiki](../../wiki/Ciberseguridad).

---

## Documentación

### Accesos rápidos para la defensa

| Qué | Dónde |
|:---|:---|
| Retrospectiva del Sprint 2 (IdeaBoardz) | [Abrir el tablero](https://ideaboardz.com/for/Retrospectiva%20Sprint%202%20-%20DEV6%20EduTools/5836322) |
| Backend desplegado (HTTPS) | [dev6.pythonanywhere.com](https://dev6.pythonanywhere.com) · el login es `POST /api/token/` |
| Decodificar el JWT (claims `rol` e `is_staff`) | [jwt.io](https://jwt.io) |
| Rutas, roles y errores de la API | [Endpoints-API](../../wiki/Endpoints-API) · [docs/api/ENDPOINTS.md](docs/api/ENDPOINTS.md) |
| Evidencia de seguridad (403, claims, HTTPS) | [Ciberseguridad](../../wiki/Ciberseguridad) |
| Ceremonias Scrum del Sprint 2 | [Scrum](../../wiki/Scrum) |
| Plan de pruebas y resultados | [Testing](../../wiki/Testing) · [Pruebas manuales](docs/pruebas/PRUEBAS_MANUALES.md) · [Pruebas unitarias](docs/pruebas/PRUEBAS_UNITARIAS.md) · [Espresso](docs/pruebas/EVIDENCIA_ESPRESSO.md) |
| Informe de accesibilidad | [docs/accesibilidad/INFORME_ACCESIBILIDAD.md](docs/accesibilidad/INFORME_ACCESIBILIDAD.md) |
| Guion del video de seguridad | [docs/seguridad/GUION_VIDEO_SEGURIDAD.md](docs/seguridad/GUION_VIDEO_SEGURIDAD.md) |
| Backend: despliegue y configuración | [docs/backend/BACKEND.md](docs/backend/BACKEND.md) |
| Issues, Milestone y Pull Requests | [Issues](../../issues) · [Milestones](../../milestones) · [Pull Requests](../../pulls) |
| Repositorio del backend | [ModPWeb--Dev6](https://github.com/ISPC-TSDWAD/ModPWeb--Dev6) |

### Wiki del proyecto

| Página | Contenido |
|:---|:---|
| [Home](../../wiki) | Presentación, Project Charter, stack y equipo |
| [Historias de Usuario](../../wiki/Historias-de-Usuario) | Product Backlog móvil, criterios de aceptación y tareas técnicas |
| [Requerimientos](../../wiki/Requerimientos) | Requisitos funcionales y no funcionales, mapa de Activities y endpoints |
| [Ceremonias Scrum](../../wiki/Scrum) | Sprint Planning, Dailies, Review, Retrospectiva y Definition of Done |
| [Testing](../../wiki/Testing) | Plan de Pruebas, casos de prueba y reporte de bugs |
| [Ciberseguridad](../../wiki/Ciberseguridad) | Políticas de seguridad y fundamentación de JWT |
| [Arquitectura](../../wiki/Arquitectura) | Mapa de navegación y mapeo contra la API |

### Documentos y evidencias

| Documento | Ubicación |
|:---|:---|
| Documentación formal del cliente móvil | [docs/EduTools_Documentacion_formal_Mobile_Ev2.md](docs/EduTools_Documentacion_formal_Mobile_Ev2.md) |
| ANEXO II — capturas de las Activities | [Ver anexo](docs/EduTools_Documentacion_formal_Mobile_Ev2.md#anexo-ii--capturas-de-las-activities) |
| Plan de seguridad (Evidencia 2) | [PDF](docs/Documentacion_Ciberseguridad_AppEduTools.pdf) · [documento colaborativo](https://docs.google.com/document/d/16HC2LHAWWaxBdfk89M1nlIFZkRZtYlht/edit) |
| Planilla de casos de prueba | [docs/testing/Planilla_Test_Cases.csv](docs/testing/Planilla_Test_Cases.csv) |
| Demo en video de la navegabilidad | [Ver en Drive](https://drive.google.com/file/d/193K4IP9BwwjeN6Mn-tMd8QOG3UiY6Efv/view?usp=drive_link) · [copia en el repositorio](docs/video) |
| Backend: cómo se conecta, roles, seguridad y despliegue | [docs/backend/BACKEND.md](docs/backend/BACKEND.md) |
| Lista de endpoints de la API | [docs/api/ENDPOINTS.md](docs/api/ENDPOINTS.md) |
| Correcciones de seguridad del backend | [docs/seguridad/CORRECCIONES_BACKEND.md](docs/seguridad/CORRECCIONES_BACKEND.md) |
| Pruebas unitarias de la app | [docs/pruebas/PRUEBAS_UNITARIAS.md](docs/pruebas/PRUEBAS_UNITARIAS.md) |
| Backend, modelo relacional y script SQL | [ModPWeb--Dev6](https://github.com/ISPC-TSDWAD/ModPWeb--Dev6) |
| Descarga de la aplicación (release) | Pendiente de publicación; el APK debug se genera en `app/build/outputs/apk/debug/app-debug.apk` |
| Revisión y pendientes de entrega Sprint 2 | [docs/pruebas/REVISION_SPRINT2.md](docs/pruebas/REVISION_SPRINT2.md) |

### Pruebas y reportes

Con JDK 17 y Android SDK 34 configurados en `local.properties` (archivo local, sin versionar):

```bash
./gradlew testDebugUnitTest jacocoTestReport lintDebug assembleDebug assembleDebugAndroidTest
```

En Windows usar `.\gradlew.bat`. La revisión local del 13/10/2026 ejecutó **87 tests JVM, con 0 fallas**. La verificación del 14/10/2026 ejecutó además 7 tests instrumentados en Moto g85 Android 16, con 0 fallas; la escucha de TalkBack y API 21 siguen pendientes (ver [evidencia Espresso](docs/pruebas/EVIDENCIA_ESPRESSO.md)).

Reportes generados:

- Tests: `app/build/reports/tests/testDebugUnitTest/index.html`.
- Cobertura JVM (JaCoCo): `app/build/reports/jacoco/jacocoTestReport/html/index.html` y XML en esa carpeta.
- Lint: `app/build/reports/lint-results-debug.html`.

El [workflow Android](.github/workflows/android-tests.yml) ejecuta esas comprobaciones en los PR a `develop` y `main`, y en los push a ambas ramas. Conserva los reportes y APK durante 14 días como artifacts. Su ejecución remota podrá verificarse en [GitHub Actions](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/actions) una vez publicado.

---

## Esquema de ramas

```
main       ← versión estable al cierre de cada Sprint (lo que se corrige)
 └ develop ← integración de las features del Sprint en curso
    ├ release/sprint-1
    └ feature/<nombre-del-desarrollador>
```

---

**ISPC** · Tecnicatura Superior en Desarrollo Web y Aplicaciones Digitales · Proyecto ABP — Aplicación Móvil
