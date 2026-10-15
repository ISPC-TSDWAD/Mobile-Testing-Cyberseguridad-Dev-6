# Resultados de API — cierre Sprint 2

47 requests, 208 assertions: **204 aprobadas y 4 fallas por tiempo**. No hubo fallas funcionales/contrato ni errores de limpieza.

| Request | Tiempo | Umbral |
|---|---|---|
| AUTH-01 Login valido (200) | 3073 ms | <800 ms |
| AUTH-06 Login asesor (200) | 1690 ms | <800 ms |
| AUTH-02 Login con credenciales invalidas (401) | 1575 ms | <800 ms |
| USR-06 Crear usuario temporal (201) | 1846 ms | <800 ms |

Incluye CRUD completo de usuario temporal, roles admin/asesor, perfiles ajenos, negativas por endpoints consumidos, PUT del formulario Android, copia de plantilla y eliminación de fixtures. Se mantiene el umbral y el resultado global con fallas. Hallazgo: [#53](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/issues/53).

Maquetador: **13/13** casos aprobados en servidor, con cuenta temporal propia y limpieza sin errores; [reporte sanitizado](../../tests/api/reporte_maquetador.json). Esta corrida complementa el caso omitido en la verificación inicial de Jonathan.

[Newman sanitizado](../../tests/api/reporte_newman.json). Ningún reporte incluye contraseñas, tokens, cuerpos privados ni headers.
