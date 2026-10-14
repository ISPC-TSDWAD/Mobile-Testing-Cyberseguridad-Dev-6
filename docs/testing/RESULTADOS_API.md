# Resultados de API — Sprint 2

Fecha: 13/10/2026. Ejecutó: Gonzalo Velasco. Herramienta: Newman 6; servidor de pruebas HTTPS de EduTools; roles admin y asesor.

La colección de Roni se consolidó con token manipulado, acceso administrativo como asesor y protección de plantillas base. No se atribuyen nuevas ejecuciones a integrantes que no las realizaron.

24 requests, 114 assertions, 0 errores de transporte y 3 fallas de tiempo. Las restantes 111 assertions pasaron. El CRUD y los negativos 400/401/403/404 pasaron.

Fallos de login: 1902 ms (admin), 1431 ms (asesor) y 1377 ms (credenciales inválidas), frente al criterio menor a 800 ms. Hallazgo de rendimiento, prioridad Media: revisar latencia y costo de login y acordar el SLA con el equipo. No se afirma que el backend sea la única causa sin separar red y servidor.

Evidencia: [reporte sanitizado](../../tests/api/reporte_newman.json). Reproducir con el entorno privado según [README de API](../../tests/api/README.md) y `node tests/api/ejecutar_newman.cjs`. Las credenciales quedan fuera del reporte y de Git.

Resultado global: **con fallas de SLA**, pendiente de gestión como Issue. El recurso de la corrida fue eliminado y el GET posterior confirmó 404.
