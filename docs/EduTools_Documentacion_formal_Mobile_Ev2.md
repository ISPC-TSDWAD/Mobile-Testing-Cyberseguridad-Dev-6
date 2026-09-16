# DOCUMENTACIÓN OFICIAL DE INICIO DE PROYECTO DE SOFTWARE
## Proyecto: EduTools Mobile — Cliente Android del Gestor y Sandbox Pedagógico
**Institución:** Instituto Superior Politécnico Córdoba (ISPC)
**Módulo:** Aplicaciones Móviles · Testeador de Software · Ciberseguridad · Proyecto Integrador II - TSDWAD
**Equipo de Desarrollo:** DEV6
**Scrum Master:** Gonzalo Velasco
**Líder Técnico:** Jonathan Guillén
**Fecha de Presentación:** 15/09/2026

> **Documento base.** Esta especificación extiende la [Documentación Oficial de EduTools (Evidencia 2 — Programador Web)](https://github.com/ISPC-TSDWAD/ModPWeb--Dev6/blob/main/docs/EduTools_Documentacion_formal_Ev2.md). El contexto del problema, los destinatarios y la justificación PMI se heredan de aquel documento y no se vuelven a fundamentar aquí. Lo que sigue especifica el **cliente móvil Android** de esa misma plataforma. La correspondencia entre unos y otros requisitos está en el **ANEXO I**.

---

## ÍNDICE
1. [Definición y Contexto del Proyecto](#1-definición-y-contexto-del-proyecto)
2. [Viabilidad Técnica y Económica](#2-viabilidad-técnica-y-económica)
3. [Alcance del Proyecto](#3-alcance-del-proyecto)
4. [Objetivos SMART del Proyecto](#4-objetivos-smart-del-proyecto)
5. [Requisitos Funcionales y No Funcionales](#5-requisitos-funcionales-y-no-funcionales)
6. [Identificación de Stakeholders y Roles del Equipo](#6-identificación-de-stakeholders-y-roles-del-equipo)
7. [Historias de Usuario](#7-historias-de-usuario)
8. [Enlaces y Herramientas del Proyecto](#8-enlaces-y-herramientas-del-proyecto)
9. [Seguimiento del Proyecto y Ceremonias](#9-seguimiento-del-proyecto-y-ceremonias)
10. [Bibliografía](#10-bibliografía)
- [ANEXO I — Trazabilidad entre el cliente web y el cliente móvil](#anexo-i--trazabilidad-entre-el-cliente-web-y-el-cliente-móvil)
- [ANEXO II — Capturas de las Activities](#anexo-ii--capturas-de-las-activities)

---

## 1. DEFINICIÓN Y CONTEXTO DEL PROYECTO

### 1.1. Nombre del Proyecto
**EduTools Mobile — Cliente Android del Sistema Integral de Gestión y Sandbox Pedagógico**

### 1.2. Problema u Oportunidad que Resuelve
El cliente web de EduTools resolvió la creación y curaduría de recursos pedagógicos desde el escritorio. El relevamiento posterior con los equipos de Asesoría Pedagógica y Maquetación de la UCC expuso una limitación que aquel producto no cubre:

* **Dependencia del puesto de trabajo:** la consulta del repositorio institucional solo es posible frente a una computadora. Los asesores pedagógicos necesitan verificar qué plantillas existen, con qué asignatura están asociadas y quién las cargó durante reuniones de cátedra, recorridas o trabajo fuera de la oficina.
* **Latencia en la verificación de accesos:** el control de altas, bajas y roles del equipo queda postergado hasta el regreso al escritorio, lo que demora la habilitación de nuevos maquetadores sobre aulas en producción.
* **Difusión desigual de los estándares institucionales:** la guía de componentes y la paleta de identidad se transmiten por documentos adjuntos que rara vez se leen completos. Un soporte audiovisual accesible desde el teléfono aumenta la probabilidad de consulta efectiva.

### 1.3. Descripción Breve (Elevator Pitch)
**EduTools Mobile** es el cliente Android nativo de la plataforma EduTools. Lleva al teléfono el repositorio de recursos pedagógicos, el padrón de usuarios del equipo y la guía multimedia de estándares institucionales, consumiendo la misma API REST en Django que alimenta al cliente web. De este modo, el asesor pedagógico consulta, verifica y da de alta contenidos desde cualquier lugar, con la información sincronizada contra la misma base de datos y sin duplicar la lógica de negocio.

### 1.4. Organización o Cliente Destinatario
**Destinatario Principal:** Universidad Católica de Córdoba (UCC), equipos de Asesoría Pedagógica y Maquetación Web.

*Aclaración de Contexto:* se mantiene el destinatario del proyecto web. El cliente móvil no incorpora usuarios nuevos: atiende a los mismos perfiles en un contexto de uso distinto.

### 1.5. Justificación del Proyecto (Referencia PMI)
Este documento opera como extensión del **Acta de Constitución del Proyecto (Project Charter)** aprobada en la Evidencia 2 del módulo Programador Web.

* **Principio de Entrega de Valor (PMBOK 7 — Principio 2):** la incorporación de un canal móvil amplía la ventana de uso del producto sin alterar su propuesta de valor ni fragmentar la fuente de datos.
* **Principio de Adaptabilidad (PMBOK 7 — Principio 11):** el cliente móvil se construye sobre los activos ya validados en el Sprint 0 (modelo relacional, API REST y design tokens institucionales), lo que reduce el riesgo técnico y concentra el esfuerzo del Sprint en la capa de presentación nativa.

---

## 2. VIABILIDAD TÉCNICA Y ECONÓMICA

### 2.1. Viabilidad Técnica

#### Stack Tecnológico Propuesto y Justificación
* **Plataforma:** **Android nativo en Java**, con `minSdk 21` (Android 5.0 Lollipop) y `targetSdk 34`. El desarrollo nativo se adopta por requerimiento de la cátedra y porque permite exhibir de forma explícita el ciclo de vida de las Activities y la jerarquía de navegación, que constituyen el objeto de evaluación del módulo.
* **Interfaz:** **Material Components**, `RecyclerView`, `CardView` y `ConstraintLayout`. La paleta y la tipografía se declaran como *design tokens* en `colors.xml` y `themes.xml`, replicando los mismos valores institucionales definidos para el cliente web (`primary: #003761`, `secondary: #006972`).
* **Navegación:** Activities con jerarquía declarada mediante `android:parentActivityName` en el manifiesto, en lugar de Navigation Component. La decisión deja la relación padre-hijo declarada de forma verificable y permite que el sistema reconstruya la pila de retorno aun cuando la Activity se invoque desde fuera de la aplicación.
* **Traspaso de datos:** objetos que implementan `Serializable`, transportados dentro del `Intent`. No se emplean variables estáticas ni singletons como vehículo de estado entre pantallas.
* **Origen de datos (Sprint 1):** clase `DatosDemo` con colecciones estáticas que reproducen campo por campo la respuesta de los serializers de Django. El Sprint 2 sustituye únicamente el origen, sin modificar adapters ni layouts.
* **Backend (reutilizado):** **Django REST Framework** con **SimpleJWT** sobre **MySQL**, sin alteraciones respecto del cliente web.

#### Infraestructura Requerida
* **Entorno de Desarrollo:** Android Studio con Gradle Wrapper, emuladores de API 21 y API 34, y al menos un dispositivo físico para validación de accesibilidad con TalkBack.
* **Entorno de Producción (Proyectado):** distribución interna del APK firmado a los equipos de la UCC. El backend permanece en la misma instancia que sirve al cliente web.

#### Conocimientos Disponibles en el Equipo
El equipo DEV6 aporta la experiencia de haber construido el cliente web y su backend, lo que garantiza el conocimiento del contrato de la API y del modelo de datos. La capa nativa Android se incorpora como aprendizaje del módulo, con cobertura de roles en configuración de proyecto, navegación, adapters, accesibilidad y QA.

#### Dependencias Externas
* Bibliotecas de Material Components y AndroidX (RecyclerView, CardView, AppCompat).
* Ninguna dependencia de licenciamiento privativo. La iconografía y la imagen institucional son vectores de autoría propia del equipo, sin descargas de bancos de terceros.

#### Riesgos Técnicos Iniciales Identificados
* **Riesgo:** la API no implementa paginación en sus listados, de modo que el `RecyclerView` recibiría la colección completa al integrar el Sprint 2.
  **Mitigación:** incorporar paginación en el backend o carga incremental en el cliente antes de la integración.
* **Riesgo:** el backend devuelve errores en dos formatos distintos según el origen de la excepción, lo que complica un manejo uniforme en el cliente.
  **Mitigación:** unificar el formato de error en el backend, o normalizarlo en una capa de traducción del cliente móvil.
* **Riesgo:** el permiso `IsAdminOrOwner` sobre `/api/users/` puede devolver un único registro en lugar del listado completo según el rol del solicitante.
  **Mitigación:** contemplar ambas respuestas en el adapter y documentar el comportamiento esperado por rol.
* **Riesgo:** `minSdk 21` excluye APIs introducidas en versiones posteriores.
  **Mitigación:** verificación de nivel de API previa al uso de cualquier componente y validación del recorrido completo en emulador de API 21.

### 2.2. Viabilidad Económica

#### Estimación de Esfuerzo
El esfuerzo del Sprint 1 del cliente móvil se estima en **36 Story Points**, equivalentes a aproximadamente **90 horas/persona** distribuidas entre los seis integrantes del equipo.

#### Costos Principales
* **Licencias de Software:** $0. Android Studio, el SDK de Android y la totalidad de las bibliotecas empleadas son de uso libre.
* **Horas de Desarrollo (Equipo DEV6):** costo interno absorbido como proyecto académico y práctica profesionalizante.
* **Infraestructura:** sin costo incremental. El cliente móvil consume la infraestructura ya prevista para el cliente web.

#### ROI o Beneficio Esperado
* **Cualitativo:** disponibilidad del repositorio institucional fuera del puesto de trabajo y mayor alcance efectivo de la guía de estándares.
* **Cuantitativo:** reducción estimada del tiempo de verificación de un recurso o de un alta de usuario, que pasa de requerir el regreso al escritorio a resolverse en el momento.
* **Naturaleza del Presupuesto:** estimación con fines de planificación del Proyecto Integrador.

---

## 3. ALCANCE DEL PROYECTO

### 3.1. Qué Incluye (In Scope)

#### Módulos y Funcionalidades Principales
1. **Pantalla de acceso:** punto de entrada de la aplicación, con campos de usuario y contraseña, ocultamiento de caracteres y control de revelado.
2. **Menú principal:** cinco accesos directos a los módulos de la aplicación, saludo personalizado con el usuario recibido y cierre de sesión que limpia la pila de navegación.
3. **Repositorio de recursos pedagógicos:** listado con título, tipo y asignatura, total de registros en el encabezado, ficha de detalle por recurso y formulario de alta con selección de tipo, categoría y asignatura.
4. **Padrón de usuarios:** listado con nombre, rol y estado, y pantalla de perfil individual.
5. **Guía multimedia:** reproductor de video con controles nativos e imagen de la paleta y tipografía institucional con su descripción textual.
6. **Contacto:** formulario de consulta a la mesa de ayuda con nombre, correo, asunto y mensaje.
7. **Pantalla institucional:** identidad del proyecto, integrantes del equipo DEV6 y versión.

#### Tipos de Usuarios Soportados
Se mantienen los tres perfiles del cliente web: **Asesor Pedagógico**, **Maquetador Web** y **Administrador**.

#### Plataformas Soportadas
Dispositivos Android desde la versión 5.0 (API 21) en adelante, en orientación vertical, con soporte de escalado de fuente del sistema y lectores de pantalla.

### 3.2. Qué No Incluye (Out of Scope)

#### Excluido de la Evidencia 2 y planificado para el Sprint 2
* **Validación de credenciales contra el backend:** el acceso del Sprint 1 no verifica usuario ni contraseña contra la API. La obtención del token JWT se planifica para el Sprint 2.
* **Operaciones CRUD contra la API REST:** los listados y el formulario operan sobre datos estáticos. La persistencia real queda fuera de esta entrega.
* **Persistencia local y manejo de sesión expirada.**

#### Excluido del proyecto móvil en su totalidad
* **Sandbox de edición enriquecida (RTE):** el editor visual y la vista de HTML crudo permanecen como funcionalidad exclusiva del cliente web, por inadecuación de la interacción a una pantalla táctil de teléfono.
* **Exportación de compendios en `.doc` y `.html`:** se mantiene en el cliente web, donde el archivo resultante se integra al flujo de trabajo de escritorio.
* **Automatización de pruebas con Appium:** planificada como evolución del Plan de Pruebas, fuera del alcance del Sprint 1.

---

## 4. OBJETIVOS SMART DEL PROYECTO

1. **Objetivo de Navegabilidad Completa:** lograr que las **diez Activities** de la aplicación sean alcanzables desde el menú principal y que el **100 %** de las pantallas hijas retorne a su Activity padre, tanto por la flecha de la Toolbar como por el control de retroceso del sistema, verificado mediante recorrido exhaustivo antes del cierre del **Sprint 1 (Evidencia 2)** el 15/09/2026.
2. **Objetivo de Traspaso de Información:** demostrar el traslado de datos entre Activities mediante `Intent` en **dos recorridos completos** (acceso hacia menú principal con el usuario, y listado de recursos hacia ficha de detalle con el objeto completo), sin recurrir a variables estáticas, con evidencia documental en el ANEXO II al cierre del Sprint 1.
3. **Objetivo de Accesibilidad Verificable:** alcanzar en la totalidad de las pantallas una relación de contraste mínima de **4.5:1**, áreas táctiles de al menos **48 dp** y descripción textual en todo elemento gráfico informativo, validado con Accessibility Scanner y TalkBack antes de la defensa de la Evidencia 2.
4. **Objetivo de Compatibilidad:** garantizar la instalación y el recorrido completo del flujo de navegación en un dispositivo con **Android 5.0 (API 21)**, cubriendo el parque de equipos más antiguo de la institución, verificado en emulador durante el Sprint 1.

---

## 5. REQUISITOS FUNCIONALES Y NO FUNCIONALES

### 5.1. Requisitos Funcionales (Prioridad MoSCoW)

* **RFM01: Pantalla de Acceso**
  * *Prioridad:* **Must** | *Actor:* Todos los usuarios.
  * *Descripción:* la aplicación debe abrir en una pantalla de acceso con campos de usuario y contraseña, ocultando los caracteres de la clave y ofreciendo un control para revelarlos.
  * *Criterio de Aceptación:* al presionar Ingresar, la aplicación abre el menú principal trasladando el usuario indicado; al presionar retroceso desde el menú, la aplicación se cierra sin regresar al formulario.

* **RFM02: Menú Principal de Módulos**
  * *Prioridad:* **Must** | *Actor:* Todos los usuarios.
  * *Descripción:* la aplicación debe presentar un menú con acceso a Recursos pedagógicos, Usuarios, Guía multimedia, Contacto y Quiénes somos.
  * *Criterio de Aceptación:* cada tarjeta abre su Activity correspondiente en un solo toque, y el menú saluda al usuario con el nombre recibido de la pantalla de acceso.

* **RFM03: Retorno a la Activity Padre**
  * *Prioridad:* **Must** | *Actor:* Todos los usuarios.
  * *Descripción:* toda Activity distinta del acceso y del menú principal debe permitir el retorno a su pantalla padre.
  * *Criterio de Aceptación:* la flecha de la Toolbar y el control de retroceso del sistema producen el mismo resultado; las pantallas de detalle regresan a su listado y los listados al menú principal.

* **RFM04: Traspaso de Información entre Activities**
  * *Prioridad:* **Must** | *Actor:* Todos los usuarios.
  * *Descripción:* la aplicación debe trasladar información entre Activities mediante `Intent.putExtra()` sobre objetos serializables.
  * *Criterio de Aceptación:* la ficha de detalle muestra exactamente los mismos datos de la fila seleccionada, y el título de la Toolbar refleja el elemento elegido.

* **RFM05: Repositorio de Recursos Pedagógicos**
  * *Prioridad:* **Must** | *Actor:* Asesor Pedagógico / Maquetador.
  * *Descripción:* el sistema debe listar los recursos disponibles con su título, tipo legible y asignatura, indicando el total en el encabezado.
  * *Criterio de Aceptación:* el listado muestra la totalidad de los recursos y ofrece un botón de acción flotante que abre el formulario de alta.

* **RFM06: Padrón de Usuarios**
  * *Prioridad:* **Must** | *Actor:* Administrador.
  * *Descripción:* el sistema debe listar los usuarios con nombre completo, rol legible y estado, y abrir el perfil individual al seleccionarlos.
  * *Criterio de Aceptación:* el estado se comunica mediante texto y no únicamente por color, y el encabezado indica la cantidad de usuarios registrados.

* **RFM07: Guía Multimedia de Estándares**
  * *Prioridad:* **Must** | *Actor:* Todos los usuarios.
  * *Descripción:* la aplicación debe incorporar una Activity con recurso multimedia de video e imagen institucional.
  * *Criterio de Aceptación:* el reproductor ofrece controles nativos de reproducción, pausa y barra de progreso; si el archivo de video no está incorporado al proyecto, la pantalla informa la situación en lugar de interrumpirse.

* **RFM08: Pantalla de Contacto**
  * *Prioridad:* **Must** | *Actor:* Todos los usuarios.
  * *Descripción:* el sistema debe ofrecer un formulario de consulta con nombre, correo electrónico, asunto y mensaje.
  * *Criterio de Aceptación:* cada campo abre el teclado adecuado a su tipo de dato y la pantalla exhibe el correo de la mesa de ayuda.

* **RFM09: Formulario de Alta de Recursos**
  * *Prioridad:* **Should** | *Actor:* Asesor Pedagógico / Administrador.
  * *Descripción:* el sistema debe ofrecer un formulario de carga con selección de tipo, categoría y asignatura.
  * *Criterio de Aceptación:* los tres selectores se despliegan con las opciones disponibles y cada uno cuenta con etiqueta vinculada mediante `labelFor`.

* **RFM10: Pantalla Institucional**
  * *Prioridad:* **Should** | *Actor:* Todos los usuarios.
  * *Descripción:* la aplicación debe exhibir la identidad del proyecto, la nómina del equipo DEV6 y la versión.
  * *Criterio de Aceptación:* la pantalla enumera a la totalidad de los integrantes del equipo.

* **RFM11: Autenticación contra el Backend**
  * *Prioridad:* **Must** *(Sprint 2)* | *Actor:* Todos los usuarios.
  * *Descripción:* el acceso debe validarse contra `POST /api/token/` obteniendo un par de tokens JWT.
  * *Criterio de Aceptación:* con campos vacíos la acción se bloquea; con credenciales inválidas se exhibe un error claro; ante una respuesta 401 posterior, la aplicación redirige al acceso.

### 5.2. Requisitos No Funcionales

* **RNFM01: Compatibilidad de Plataforma**
  La aplicación instala y ejecuta el flujo completo de navegación en Android 5.0 (API 21), sin emplear APIs de versiones posteriores sin verificación previa del nivel disponible.

* **RNFM02: Accesibilidad Perceptiva**
  Los pares de texto y fondo alcanzan una relación de contraste mínima de 4.5:1 (WCAG 2.2 nivel AA), los controles interactivos miden al menos 48 dp de alto y los tamaños de texto se declaran en `sp`, respetando la escala de fuente configurada en el sistema.

* **RNFM03: Accesibilidad Operativa**
  Todo elemento gráfico informativo posee `contentDescription`; los decorativos se marcan como no relevantes para accesibilidad. Cada fila de listado se anuncia en un único foco con la totalidad de su información, y cada selector cuenta con etiqueta vinculada mediante `labelFor`.

* **RNFM04: Arquitectura de Navegación**
  La jerarquía de pantallas se declara mediante `android:parentActivityName`, de modo que el sistema reconstruye correctamente la pila de retorno aun cuando la Activity se invoque desde fuera de la aplicación.

* **RNFM05: Desacoplamiento del Origen de Datos**
  Los objetos que viajan entre Activities implementan `Serializable` y reproducen campo por campo los serializers de Django, de modo que la integración del Sprint 2 sustituya exclusivamente el origen de los datos.

* **RNFM06: Seguridad del Canal**
  La aplicación prohíbe el tráfico en texto plano mediante `network_security_config.xml`, obligando al uso de HTTPS en toda comunicación con el backend.

* **RNFM07: Tratamiento de Credenciales**
  La aplicación no persiste la contraseña del usuario en el dispositivo. Las contraseñas se almacenan hasheadas en el backend mediante los algoritmos criptográficos de Django (PBKDF2).

* **RNFM08: Autoría de los Activos Gráficos**
  La iconografía y las imágenes institucionales son vectores de autoría propia del equipo, sin dependencias de licenciamiento de terceros.

---

## 6. IDENTIFICACIÓN DE STAKEHOLDERS Y ROLES DEL EQUIPO

### 6.1. Mapa de Stakeholders

| Stakeholder | Rol / Puesto | Tipo | Interés | Influencia | Expectativas Principales | Canal de Comunicación |
| :--- | :--- | :---: | :---: | :---: | :--- | :--- |
| **Prof. Titular / Tribunal** | Evaluador Académico | Externo | Alto | Alta | Navegabilidad completa demostrable, confirmaciones propias de cada integrante y documentación de pruebas y seguridad. | Entregables GitHub / Defensa |
| **Asesores Pedagógicos** | Usuarios Finales | Interno | Alto | Media | Consulta del repositorio fuera del escritorio, con la misma información que el cliente web. | Pruebas de Usabilidad / Feedback |
| **Maquetadores Web** | Usuarios Finales | Interno | Medio | Media | Acceso a la guía de estándares institucionales desde el teléfono. | Repositorio / Issues |
| **Usuarios con discapacidad visual** | Usuarios Finales | Interno | Alto | Baja | Operación completa de la aplicación con lector de pantalla y texto ampliado. | Pruebas con TalkBack |

### 6.2. Roles del Equipo Scrum (DEV6)

Los roles rotan por módulo. La conformación para el módulo de Aplicaciones Móviles es la siguiente:

* **Scrum Master / Developer: Gonzalo Velasco**
  *Responsabilidades:* facilitación de las ceremonias, gestión de la Wiki, módulo de recursos (listado, detalle y formulario), modelos serializables y consolidación de ramas.
* **Líder Técnico / Developer: Jonathan Guillén**
  *Responsabilidades:* configuración del proyecto Gradle, manifiesto y jerarquía de Activities, política de seguridad de red y revisión de código.
* **Developer: Ale Corva**
  *Responsabilidades:* `BaseActivity` con Toolbar y navegación Up centralizadas, pantalla de acceso y menú principal.
* **Technical Writer & Designer: Daniela Salvo**
  *Responsabilidades:* design tokens institucionales, módulo de usuarios y elaboración del ANEXO II de capturas.
* **Developer: Gerardo Romero**
  *Responsabilidades:* guía multimedia, contacto, pantalla institucional e iconografía vectorial.
* **QA Tester: Roni Duncan Gonzales Martínez**
  *Responsabilidades:* Plan de Pruebas, planificación de los casos de prueba, verificación de accesibilidad y compatibilidad con API 21.

> **Nota de conformación.** Roni Duncan Gonzales Martínez se incorpora al equipo DEV6 en este módulo y no figura en la documentación de la Evidencia 2 del módulo Programador Web.

---

## 7. HISTORIAS DE USUARIO

Historias correspondientes a los requisitos de prioridad **Must** del Sprint 1, redactadas bajo criterios Gherkin. El backlog completo, con las doce historias y sus tareas técnicas, está publicado en la [Wiki del proyecto](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/wiki/Historias-de-Usuario).

### HUM-01: Acceso desde el Dispositivo Móvil
* **Como** usuario del sistema,
* **Quiero** contar con una pantalla de acceso en la aplicación móvil,
* **Para** iniciar sesión desde mi teléfono con las mismas credenciales que uso en la web.
* **Criterios de Aceptación (Gherkin):**
  * **Dado que** abro la aplicación por primera vez,
  * **Cuando** se completa la carga inicial,
  * **Entonces** se presenta la pantalla de acceso con los campos de usuario y contraseña y el control de revelado de la clave.
  * **Dado que** ingresé al menú principal,
  * **Cuando** presiono el control de retroceso del sistema,
  * **Entonces** la aplicación se cierra y no regresa al formulario de acceso.
* *Prioridad:* Alta (Must) | *Estimación:* 3 Story Points | *Trazabilidad:* US-01 (web).

### HUM-02: Menú Principal de Módulos
* **Como** asesor pedagógico,
* **Quiero** un menú principal con los módulos de la aplicación,
* **Para** llegar a cualquier sección en un solo toque.
* **Criterios de Aceptación (Gherkin):**
  * **Dado que** ingresé con mi usuario,
  * **Cuando** se abre el menú principal,
  * **Entonces** el encabezado me saluda con el nombre que ingresé y se presentan los cinco accesos disponibles.
  * **Dado que** deseo abandonar la sesión,
  * **Cuando** presiono Cerrar sesión,
  * **Entonces** regreso a la pantalla de acceso con la pila de navegación limpia.
* *Prioridad:* Alta (Must) | *Estimación:* 3 Story Points.

### HUM-03: Retorno a la Pantalla Anterior
* **Como** usuario,
* **Quiero** poder volver siempre a la pantalla anterior,
* **Para** no quedar atrapado en una sección de la aplicación.
* **Criterios de Aceptación (Gherkin):**
  * **Dado que** me encuentro en cualquier Activity distinta del acceso y del menú principal,
  * **Cuando** presiono la flecha de la Toolbar o el control de retroceso del sistema,
  * **Entonces** regreso a la Activity padre declarada, obteniendo idéntico resultado por ambas vías.
* *Prioridad:* Alta (Must) | *Estimación:* 2 Story Points.

### HUM-04: Traspaso de Información entre Pantallas
* **Como** asesor pedagógico,
* **Quiero** que al tocar un elemento de una lista se abra su detalle con los mismos datos,
* **Para** consultar la ficha completa sin volver a buscarlo.
* **Criterios de Aceptación (Gherkin):**
  * **Dado que** visualizo el listado de recursos,
  * **Cuando** selecciono una fila,
  * **Entonces** la ficha de detalle exhibe el mismo título, tipo, categoría, asignatura, descripción y autor de la fila seleccionada, y el título de la Toolbar refleja el elemento elegido.
  * **Dado que** visualizo el padrón de usuarios,
  * **Cuando** selecciono una fila,
  * **Entonces** el perfil exhibe el mismo nombre, usuario, correo, rol y estado.
* *Prioridad:* Alta (Must) | *Estimación:* 5 Story Points | *Trazabilidad:* US-02 (web).

### HUM-05: Catálogo de Recursos en el Teléfono
* **Como** asesor pedagógico,
* **Quiero** ver el catálogo de recursos pedagógicos en mi teléfono,
* **Para** consultar las plantillas institucionales fuera del escritorio.
* **Criterios de Aceptación (Gherkin):**
  * **Dado que** ingreso al módulo de recursos,
  * **Cuando** se despliega el listado,
  * **Entonces** cada fila exhibe título, tipo legible y asignatura, y el encabezado indica la cantidad total disponible.
* *Prioridad:* Alta (Must) | *Estimación:* 5 Story Points | *Trazabilidad:* US-03 (web).

### HUM-09: Accesibilidad de la Interfaz
* **Como** usuario con baja visión,
* **Quiero** que la aplicación sea utilizable con lector de pantalla y texto ampliado,
* **Para** poder trabajar en igualdad de condiciones.
* **Criterios de Aceptación (Gherkin):**
  * **Dado que** tengo TalkBack activado,
  * **Cuando** recorro un listado,
  * **Entonces** cada fila se anuncia en un único foco con la totalidad de su información.
  * **Dado que** amplío el tamaño de fuente del sistema,
  * **Cuando** recorro las pantallas,
  * **Entonces** los textos escalan sin recortes ni superposiciones.
* *Prioridad:* Alta (Must) | *Estimación:* 5 Story Points | *Trazabilidad:* RNF02 (web).

---

## 8. ENLACES Y HERRAMIENTAS DEL PROYECTO

* 🔗 **Repositorio del Cliente Móvil:** [ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6)
* 🌐 **Repositorio del Cliente Web y Backend:** [ISPC-TSDWAD/ModPWeb--Dev6](https://github.com/ISPC-TSDWAD/ModPWeb--Dev6)
* 📚 **Wiki del Proyecto Móvil:** [Home](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/wiki) · [Historias de Usuario](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/wiki/Historias-de-Usuario) · [Requerimientos](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/wiki/Requerimientos) · [Ceremonias Scrum](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/wiki/Scrum) · [Testing](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/wiki/Testing) · [Ciberseguridad](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/wiki/Ciberseguridad) · [Arquitectura](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/wiki/Arquitectura)
* 📄 **Documentación Formal del Cliente Web (Ev2):** [EduTools_Documentacion_formal_Ev2.md](https://github.com/ISPC-TSDWAD/ModPWeb--Dev6/blob/main/docs/EduTools_Documentacion_formal_Ev2.md)
* 🗂️ **Modelo Relacional y DER (Sprint 0):** [modelo_relacional.md](https://github.com/ISPC-TSDWAD/ModPWeb--Dev6/blob/main/docs/modelo_relacional.md) · [der_chen.md](https://github.com/ISPC-TSDWAD/ModPWeb--Dev6/blob/main/docs/der_chen.md)
* 🧪 **Planilla de Casos de Prueba:** [docs/Test_Cases_Sprint1.csv](Test_Cases_Sprint1.csv)
* 📋 **Tablero de Gestión y Backlog:** *(pendiente de enlace definitivo del Project del repositorio móvil)*
* 🌿 **Esquema de Ramas:** cada integrante trabaja en su rama `feature/<nombre>`, integra en `develop`, y el Scrum Master consolida en `release/sprint-1` y `main` al cierre del Sprint.

---

## 9. SEGUIMIENTO DEL PROYECTO Y CEREMONIAS

El registro completo de las ceremonias del Sprint 1, con el detalle de la Sprint Planning, las Dailies, la Review y la Retrospectiva, está publicado en la página [Ceremonias Scrum](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/wiki/Scrum) de la Wiki del proyecto, conforme a lo solicitado por la cátedra.

### 9.1. Compromiso del Sprint 1

| Historia | Descripción | SP | Estado |
|:---:|:---|:---:|:---:|
| HUM-01 | Pantalla de acceso | 3 | Done |
| HUM-02 | Menú principal con cinco módulos | 3 | Done |
| HUM-03 | Retorno a la Activity padre | 2 | Done |
| HUM-04 | Traspaso de información entre Activities | 5 | Done |
| HUM-05 | Catálogo de recursos pedagógicos | 5 | Done |
| HUM-06 | Listado de usuarios y perfil | 5 | Done |
| HUM-07 | Guía multimedia con video e imagen | 3 | Done |
| HUM-08 | Pantalla de contacto | 3 | Done |
| HUM-09 | Accesibilidad de la interfaz | 5 | Done |
| HUM-10 | Compatibilidad con Android 5.0 | 2 | Done |

**Velocidad comprometida:** 36 Story Points.

### 9.2. Impedimentos y Resolución

* **Conflictos de integración sobre el manifiesto.** El `AndroidManifest.xml` concentra la declaración de las diez Activities y su jerarquía, de modo que constituye un punto de contacto para la totalidad del equipo. Se acordó que su edición quede a cargo de un único integrante, y que el resto solicite las declaraciones necesarias. La medida evitó la repetición de los conflictos de fusión registrados en el módulo Programador Web.
* **Ramas que no compilan de forma aislada.** Las ramas `feature/` se crearon a partir del commit inicial del repositorio, que no incluye la configuración de Gradle. Ninguna compila por separado: el proyecto queda armado recién tras la integración. La acción correctiva acordada para el Sprint 2 consiste en crear las ramas a partir de `develop` ya configurada.
* **Ausencia del archivo de video.** El recurso `presentacion_edutools.mp4` no se incorporó dentro del Sprint. La Activity multimedia contempla la situación y muestra un aviso en lugar de interrumpirse, según consta en el ANEXO II.

---

## 10. BIBLIOGRAFÍA

1. **Project Management Institute (PMI).** (2021). *Guía de los Fundamentos para la Dirección de Proyectos (Guía del PMBOK®) – Séptima Edición*. Project Management Institute.
2. **Schwaber, K., & Sutherland, J.** (2020). *La Guía de Scrum*. Scrum.org.
3. **W3C Web Accessibility Initiative (WAI).** (2023). *Web Content Accessibility Guidelines (WCAG) 2.2*.
4. **Google Developers.** (2024). *Guide to app architecture y Tasks and the back stack*. Android Developers.
5. **Google Developers.** (2024). *Make apps more accessible*. Android Developers.
6. **Internet Engineering Task Force (IETF).** (2015). *RFC 7519: JSON Web Token (JWT)*.

---

## ANEXO I — Trazabilidad entre el cliente web y el cliente móvil

Correspondencia entre los requisitos y las historias de la Evidencia 2 del módulo Programador Web y los del cliente móvil.

| Cliente web | Cliente móvil | Observación |
|:---|:---|:---|
| RF01 · US-01 — Gestión de sesión y autenticación | RFM01 · HUM-01 (Sprint 1) → RFM11 (Sprint 2) | El Sprint 1 entrega la pantalla; la validación contra la API se planifica para el Sprint 2. |
| RF02 · US-02 — Alta de recursos con clasificación | RFM09 · HUM-04 | El formulario móvil conserva los tres selectores de tipo, categoría y asignatura. |
| RF03 · US-03 — Filtrado dinámico de biblioteca | RFM05 · HUM-05 | El cliente móvil entrega el listado completo con su total. El filtrado se evalúa para el Sprint 2. |
| RF04 · US-04 — Sandbox de edición enriquecida | *Sin equivalente* | Fuera de alcance del cliente móvil por inadecuación a la interacción táctil. |
| RF05 · US-05 — Exportación de compendios | *Sin equivalente* | Se mantiene como funcionalidad exclusiva del cliente web. |
| RF06 — Visualización de HTML crudo | *Sin equivalente* | Fuera de alcance del cliente móvil. |
| RF07 — Catálogo de estándares visuales y H5P | RFM07 · HUM-07 | El cliente móvil lo resuelve como guía multimedia con video e imagen institucional. |
| *Sin equivalente* | RFM06 · HUM-06 | El padrón de usuarios se incorpora como módulo propio del cliente móvil. |
| RNF02 — Usabilidad y adaptabilidad | RNFM02 · RNFM03 · HUM-09 | El cliente móvil amplía el requisito con criterios de accesibilidad WCAG 2.2 AA. |
| RNF03 — Persistencia e integridad referencial | RNFM05 | El cliente móvil espeja los modelos sin alterar el esquema relacional. |
| RNF04 — Compatibilidad y estándares | RNFM01 · HUM-10 | Se traduce en la compatibilidad con Android 5.0 (API 21). |

---

## ANEXO II — Capturas de las Activities

Registro visual del recorrido de navegabilidad de EduTools Mobile, capturado sobre dispositivo físico con Android 11. Cada captura evidencia el cumplimiento de los requisitos funcionales indicados.

| # | Activity | Captura | Requisitos que evidencia |
|:--:|:---|:---|:---|
| 1 | `LoginActivity` | [01-login.jpeg](anexo-ii/01-login.jpeg) | RFM01 · HUM-01 |
| 2 | `MainActivity` | [02-menu-principal.jpeg](anexo-ii/02-menu-principal.jpeg) | RFM02 · HUM-02 · HUM-04 |
| 3 | `RecursosActivity` | [03-recursos.jpeg](anexo-ii/03-recursos.jpeg) | RFM05 · RFM03 · HUM-05 |
| 4 | `RecursoDetalleActivity` | *Pendiente de captura* | RFM04 · HUM-04 |
| 5 | `RecursoFormActivity` | *Pendiente de captura* | RFM09 · RNFM03 |
| 6 | `UsuariosActivity` | [06-usuarios.jpeg](anexo-ii/06-usuarios.jpeg) | RFM06 · HUM-06 |
| 7 | `PerfilUsuarioActivity` | *Pendiente de captura* | RFM04 · HUM-04 |
| 8 | `MultimediaActivity` | [08-multimedia.jpeg](anexo-ii/08-multimedia.jpeg) | RFM07 · HUM-07 |
| 9 | `ContactoActivity` | [09-contacto.jpeg](anexo-ii/09-contacto.jpeg) | RFM08 · HUM-08 |
| 10 | `AcercaDeActivity` | [10-acerca-de.jpeg](anexo-ii/10-acerca-de.jpeg) | RFM10 |

### Observaciones sobre el estado del Anexo

1. **Capturas pendientes.** Restan las tres Activities de detalle y formulario (`RecursoDetalleActivity`, `RecursoFormActivity` y `PerfilUsuarioActivity`). Son precisamente las que evidencian el traspaso de información entre Activities (RFM04 · HUM-04), de modo que su incorporación es prioritaria antes de la defensa.

2. **Capturas a rehacer.** Las correspondientes a `UsuariosActivity` y `AcercaDeActivity` se tomaron sobre una compilación anterior a la incorporación del sexto integrante del equipo. La primera indica *"5 usuarios registrados"* y la segunda enumera cinco nombres, mientras que la versión vigente en la rama de entrega registra seis en ambos casos. Deben capturarse nuevamente sobre la compilación actual.

3. **Aviso de video ausente.** La captura de `MultimediaActivity` exhibe el mensaje *"El video no está disponible en este momento"*. Corresponde al comportamiento previsto en el criterio de aceptación de RFM07 ante la ausencia del archivo `presentacion_edutools.mp4`, que no alcanzó a incorporarse dentro del Sprint.

---
**FINAL DE LA DOCUMENTACIÓN OFICIAL DE SOFTWARE — EDUTOOLS MOBILE**
