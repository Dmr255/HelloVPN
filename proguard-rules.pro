# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\tools\adt-bundle-windows-x86_64-20131030\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-obfuscate

# Keep all classes in specified packages
-keep class com.slipkprojects.ultrasshservice.** { *; }
-keep class com.slipkprojects.sockshttp.** { *; }
-keep class me.dawson.proxyserver.** { *; }
-keep class jomar.dev.** { *; }

# Keep SweetAlert library
-keep class cn.pedant.SweetAlert.** { *; }

# Keep all Service subclasses
-keep class * extends android.app.Service

# Keep Conscrypt classes
-keep class org.conscrypt.** { *; }

# Keep GSON SerializedName annotations and fields
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Additional recommended rules for Android apps

# Keep Android framework classes
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.view.View

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep view constructors for inflation from XML
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

# Keep custom Application class
-keep public class * extends android.app.Application

# Keep BuildConfig and R classes
-keep class **.BuildConfig { *; }
-keep class **.R$* { *; }

# Preserve line numbers for debugging
-keepattributes SourceFile,LineNumberTable

# Preserve method signatures and other metadata
-keepattributes Signature
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes EnclosingMethod
-keepattributes Deprecated
-keepattributes *Annotation*

# Optimization settings
-optimizationpasses 5
-allowaccessmodification
-dontpreverify