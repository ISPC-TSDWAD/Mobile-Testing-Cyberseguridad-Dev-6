# Pruebas unitarias de la app (JUnit)

Son pruebas de lógica pura, sin emulador ni dispositivo. Corren en la JVM con JUnit 4.13.2.

## Cómo correrlas

Desde Android Studio: click derecho sobre `app/src/test` > *Run Tests*.

Por consola (desde la raíz del repo):

```
./gradlew testDebugUnitTest
```

El reporte queda en `app/build/reports/tests/testDebugUnitTest/index.html`.

## Qué se prueba

| Clase | Qué se verifica | Tests |
|---|---|---|
| `Validador` (`util/`) | email, usuario del login, título y URL del recurso | 14 |
| `UsuarioItem` (`model/`) | etiquetas legibles de rol y estado, getters | 6 |
| `Recurso` (`model/`) | etiquetas legibles del tipo de recurso, getters | 6 |

Total: 26 tests.

## Validador

| Método | Regla | Casos probados |
|---|---|---|
| `esEmailValido` | formato `algo@dominio.ext` | correcto, sin arroba, sin dominio, vacío, null, con espacios |
| `normalizarUsuario` | saca espacios; si queda vacío devuelve `invitado` | vacío, solo espacios, null, con espacios |
| `esTituloValido` | no vacío y hasta 200 caracteres (igual que el backend) | normal, vacío, null, 200 y 201 caracteres |
| `esUrlValida` | vacía vale (es opcional); si hay valor debe empezar con `https://` | https, http, vacía, null, sin dominio, con espacios, `javascript:` |

La regla de HTTPS viene de que la app tiene el tráfico sin cifrar deshabilitado, así que una URL `http://` no se podría abrir.

## Roles

Los únicos roles que existen en el backend son `admin`, `asesor` y `maquetador`. El rol "docente" no existe: `UsuarioItem.getRolLegible()` lo devuelve tal cual, sin etiqueta propia, y hay un test que lo deja asentado.

## Nota

`LoginActivity` usa `Validador.normalizarUsuario` para el nombre ingresado (antes la lógica estaba escrita adentro del botón y no se podía probar).
