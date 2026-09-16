# EduTools Mobile 📱

Cliente Android de **EduTools**, la plataforma de gestión y sandbox pedagógico desarrollada por el equipo **DEV6** para optimizar el flujo de trabajo entre Asesoría Pedagógica y Maquetación en entornos de educación a distancia sobre Canvas LMS.

La aplicación móvil consume la misma API REST en Django construida durante el módulo Programador Web, de modo que la información queda sincronizada entre el cliente web y el móvil.

> **Estado actual: Sprint 1 (Evidencia 2).** Esta entrega presenta la estructura de navegación completa de la aplicación, **sin funcionalidad integrada**. El consumo de la API, el CRUD y la autenticación se desarrollan en el Sprint 2.

---

## Equipo DEV6

| Integrante | Rol | Responsabilidad en el Sprint 1 |
|:---|:---|:---|
| Gonzalo Velasco | Scrum Master · Developer | Módulo de recursos, modelos de datos y gestión de la Wiki |
| Jonathan Guillén | Developer | Configuración del proyecto, acceso y seguridad de red |
| Ale Corva | Developer | Navegación principal y jerarquía de Activities |
| Daniela Salvo | Developer | Módulo de usuarios y design tokens |
| Gerardo Romero | Developer | Multimedia, contacto e institucional |
| Roni Duncan Gonzales Martínez | Developer | Accesibilidad, compatibilidad y plan de pruebas |

---

## Funcionalidades de la aplicación

| Módulo | Pantallas | Estado |
|:---|:---|:---|
| **Acceso** | Pantalla de inicio de sesión | Maquetada · validación en Sprint 2 |
| **Home** | Menú principal con acceso a los cinco módulos | Navegable |
| **Recursos pedagógicos** | Listado · Detalle del recurso · Formulario de alta y edición | Navegable · CRUD en Sprint 2 |
| **Usuarios** | Listado de usuarios · Perfil | Navegable · CRUD en Sprint 2 |
| **Guía multimedia** | Video de los estándares institucionales e imagen de la paleta | Imagen funcional · agregar el archivo de video |
| **Contacto** | Formulario de consulta a la mesa de ayuda | Maquetado · envío en Sprint 2 |
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
| Autenticación | JWT (SimpleJWT) — Sprint 2 |

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
2. Agregar el video de presentación en `app/src/main/res/raw/presentacion_edutools.mp4`. Sin este archivo el proyecto compila igual: la pantalla multimedia informa que el video no está incorporado.
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
├── AndroidManifest.xml          10 Activities con su parentActivityName declarado
├── java/ar/edu/ispc/edutools/
│   ├── BaseActivity.java        Toolbar y navegación Up centralizadas
│   ├── *Activity.java           Las diez pantallas de la aplicación
│   ├── adapter/                 RecursoAdapter · UsuarioAdapter
│   └── model/                   Recurso · UsuarioItem · DatosDemo
└── res/
    ├── layout/                  Layouts de pantallas e ítems de lista
    ├── values/                  strings · colors · themes (design tokens)
    ├── drawable/                Íconos vectoriales propios
    ├── xml/                     network_security_config.xml
    └── raw/                     presentacion_edutools.mp4  ← agregar
```

---

## Accesibilidad y usabilidad

La interfaz aplica criterios de WCAG 2.1 nivel AA:

- Descripción textual en todo ícono e imagen informativa.
- Área táctil mínima de 48 dp en controles y 72 dp en filas de listas.
- Relación de contraste de 15.1:1 en el texto principal y 10.4:1 en el texto sobre el color institucional.
- Tamaños de texto en `sp`, que respetan la escala de fuente configurada en el sistema.
- El estado de un usuario se comunica con texto, no únicamente con color.
- Cada fila de lista se anuncia en un solo foco con toda su información.

---

## Seguridad

- Tráfico en texto plano prohibido por configuración; la única excepción documentada es el host de desarrollo local, que debe eliminarse antes del release.
- Copia de seguridad de la aplicación deshabilitada (`allowBackup="false"`).
- Ninguna Activity interna se exporta.
- Claves de firma, keystores y archivos de entorno excluidos del control de versiones.

El detalle completo de políticas y la fundamentación de la elección de JWT están en la [página de Ciberseguridad de la Wiki](../../wiki/Ciberseguridad).

---

## Documentación

| Documento | Ubicación |
|:---|:---|
| Wiki del proyecto | [Ir a la Wiki](../../wiki) |
| Sprint 1 y ceremonias Scrum | [Sprint 1](../../wiki/Sprint-1) |
| Historias de Usuario y tareas | [Historias de Usuario](../../wiki/Historias-de-Usuario) |
| Plan de Pruebas y casos de prueba | [Testing](../../wiki/Testing) |
| Políticas de seguridad y JWT | [Ciberseguridad](../../wiki/Ciberseguridad) |
| Arquitectura y mapeo con la API | [Arquitectura](../../wiki/Arquitectura) |
| Backend, modelo relacional y script SQL | [ModPWeb--Dev6](https://github.com/ISPC-TSDWAD/ModPWeb--Dev6) |
| Documento IEEE 830 | *(pegar enlace)* |
| Demo en video de la navegabilidad | *(pegar enlace)* |
| Descarga de la aplicación (release) | *(pegar enlace — Sprint 2)* |

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
