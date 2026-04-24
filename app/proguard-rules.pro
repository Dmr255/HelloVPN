# ProGuard rules

# Keep rules for VPN service classes and proxy servers
-keep class com.slipkprojects.ultrasshservice.** { *; }
-keep class com.slipkprojects.sockshttp.** { *; }
-keep class me.dawson.proxyserver.** { *; }
-keep class jomar.dev.** { *; }
-keep class cn.pedant.SweetAlert.** { *; }
-keep class android.app.Service { *; }
-keep class org.conscrypt.** { *; }

# GSON serialized name fields
-keepnames class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Existing obfuscation rule
-obfuscate