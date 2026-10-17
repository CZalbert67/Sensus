# Sensus - Proguard / R8 Optimization & Obfuscation Rules

# ====================================================================
# Kotlinx Serialization
# ====================================================================
-keepattributes *Annotation*,InnerClasses,Signature
-dontnote kotlinx.serialization.SerializationKt

# Keep companion objects for serializable classes
-keepclassmembers class * {
    *** Companion;
}

# Keep the serializer method on companion objects
-keepclasseswithmembers class * {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep annotated @Serializable classes and their properties
-keep,allowobfuscation,allowoptimization class * {
    @kotlinx.serialization.Serializable class *;
}

-keepclassmembers class * {
    @kotlinx.serialization.SerialName <fields>;
}

# Navigation 3 keys
-keep class com.example.sensus.NavigationKeysKt { *; }
-keep class com.example.sensus.Main { *; }

# ====================================================================
# Data Models
# ====================================================================
-keep class com.example.sensus.data.** { *; }

# ====================================================================
# Jetpack Compose & Navigation
# ====================================================================
-keepclassmembers class * {
    @androidx.compose.runtime.Composable *;
}

-dontwarn androidx.compose.**

# ====================================================================
# ViewModels & Architecture Components
# ====================================================================
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# ====================================================================
# Coroutines
# ====================================================================
-dontwarn kotlinx.coroutines.**
