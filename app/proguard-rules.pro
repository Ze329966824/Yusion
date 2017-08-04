# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/ice/Library/Android/sdk/tools/proguard/proguard-android.txt
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

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#retrofit
-dontwarn okio.**
-dontwarn javax.annotation.**

#混淆后的有a.a予pgyersdk中的a.a重复！！！
#pgy
#-dontwarn com.pgyersdk.**
#-keep class com.pgyersdk.** { *; }
#-keep class com.a.a.** { *; }

#lambda
-dontwarn java.lang.invoke.*

#eventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }


#如果你使用是okhttp和rxAndroid，retrofit2。并且在打release包是报找不到okio，rx，retrofit。你只需要在app下的proguard-rules.pro混淆文件中加入
#-dontwarn org.codehaus.**
#-dontwarn java.nio.**
#-dontwarn java.lang.invoke.**
-dontwarn rx.**
-dontwarn retrofit2.**


#oss
-keep class com.alibaba.sdk.android.oss.** { *; }
-dontwarn okio.**
-dontwarn org.apache.commons.codec.binary.**