# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /opt/google/android-sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#Remove Log
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
    public static int w(...);
    public static int e(...);
    public static int wtf(...);
}

#Parcelable
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

#Remove LoaderManager Log
-assumenosideeffects class android.support.v4.app.LoaderManager {
    public static void enableDebugLogging(...);
}

#Remove FragmentManager Log
-assumenosideeffects class android.support.v4.app.FragmentManager {
    public static void enableDebugLogging(...);
}

#Remove StrictMode
-assumenosideeffects class com.pepper.apps.android.debug.StrictModeUtils {
    public static void enableStrictMode();
}

#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.GeneratedAppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

#Maps
-keep class com.google.android.gms.maps.** { *; }
-keep interface com.google.android.gms.maps.** { *; }

#Firebase
-keep class com.google.android.gms.internal.** { *; }
-keep interface com.google.android.gms.internal.** { *; }

#Behavior
-keep public class * extends android.support.design.widget.CoordinatorLayout$Behavior { *; }

#support-v4 //TODO Meh.
-dontwarn android.support.v4.**
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v4.** { *; }

#support-v7 //TODO Meh.
-dontwarn android.support.v7.**
-keep class android.support.v7.internal.** { *; }
-keep interface android.support.v7.internal.** { *; }
-keep class android.support.v7.** { *; }

#Retrofit
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

#Okio
-dontwarn okio.**

#Moshi
-keep class org.lagonette.app.api.adapter.MoshiNullable** { *; }
-keepclassmembers class ** {
    @com.squareup.moshi.FromJson *;
    @com.squareup.moshi.ToJson *;
}