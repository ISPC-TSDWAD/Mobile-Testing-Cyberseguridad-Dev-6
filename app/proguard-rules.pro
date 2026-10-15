# Reglas de ofuscacion para el build de release.
-keepattributes Signature,*Annotation*,InnerClasses,EnclosingMethod

# Gson deserializa estos modelos y DTO por reflexion.
-keep class ar.edu.ispc.edutools.model.** { *; }
-keep class ar.edu.ispc.edutools.net.**Dto { *; }
-keep class ar.edu.ispc.edutools.net.**Request { *; }
-keep class ar.edu.ispc.edutools.net.**Response { *; }
-keep class ar.edu.ispc.edutools.net.ResultadoLogin { *; }
-keep interface ar.edu.ispc.edutools.net.**Api { *; }

# Retrofit inspecciona Call<T> y Response<T> por reflexion en release.
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
# Gson conserva el tipo generico de sus adaptadores.
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken
