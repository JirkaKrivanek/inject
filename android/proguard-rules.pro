# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /AndroidStudioSdk/tools/proguard/proguard-android.txt
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

-dontwarn android.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.CheckReturnValue
-dontwarn javax.annotation.CheckForNull
-dontwarn javax.annotation.ParametersAreNonnullByDefault
-dontwarn javax.annotation.concurrent.GuardedBy
-dontwarn javax.annotation.concurrent.Immutable
-dontwarn javax.annotation.concurrent.NotThreadSafe
-dontwarn javax.annotation.concurrent.ThreadSafe
-dontwarn sun.misc.Unsafe

-dontnote android.**
-dontnote com.google.common.**
-dontnote com.google.vending.**
-dontnote com.google.inject.**
-dontnote org.apache.http.**

-keepattributes InnerClasses
