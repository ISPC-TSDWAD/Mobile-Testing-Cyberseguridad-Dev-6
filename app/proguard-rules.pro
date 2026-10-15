# Reglas de ofuscacion para el build de release.
-keepattributes Signature,*Annotation*,InnerClasses,EnclosingMethod

# Gson deserializa estos modelos y DTO por reflexion.
-keep class ar.edu.ispc.edutools.model.** { *; }
-keep class ar.edu.ispc.edutools.net.**Dto { *; }
-keep class ar.edu.ispc.edutools.net.**Request { *; }
-keep class ar.edu.ispc.edutools.net.**Response { *; }
-keep class ar.edu.ispc.edutools.net.ResultadoLogin { *; }
-keep interface ar.edu.ispc.edutools.net.**Api { *; }
