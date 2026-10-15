# APK de entrega 0.2.0

La compilación release activa R8 y reducción de recursos. Las reglas conservan modelos, DTO y firmas de Retrofit/Gson usados por reflexión. La firma se carga desde `keystore.properties`, ignorado por Git; la clave está en `.signing/edutools-release.jks`, también ignorada. Responsable de empaquetado: Gonzalo Velasco.

No borrar estos archivos privados al limpiar el proyecto; respaldarlos fuera del repositorio. Sin ellos Gradle permite compilar release sin firma para CI, pero ese APK no es instalable hasta firmarlo. La descarga de la entrega debe usar el APK firmado, no el artifact unsigned del pipeline.

Un APK con firma de release no puede actualizar directamente una instalación debug con otra firma. Para comprobar el mismo código sin perder datos del celular, se puede firmar una copia local de verificación con la firma debug habitual; esa copia no es el archivo público de entrega. Documentar la comprobación efectivamente realizada.
