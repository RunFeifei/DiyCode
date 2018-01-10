# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/yangcheng/dev/android-sdk-macosx/tools/proguard/proguard-android.txt
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

# !!! 请在proguard-local.txt中写入以下两行注释中的配置，并将</Users/yangcheng/dev/android-sdk-macosx>部分改为你的Adnroid SDK根路径
#-include /Users/yangcheng/dev/android-sdk-macosx/tools/proguard/proguard-android.txt
#-libraryjars /Users/yangcheng/dev/android-sdk-macosx/sources/android-19/android.jar
#-include proguard-local.txt
#-injars      build/intermediates/classes
#-injars      libs
#-outjars     build/intermediates//classes-processed.jar

-repackageclasses ''
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable

-keep class com.fei.android.common.viewholder.Res{
    public int value();
}

-keepclassmembers class **.**{
    @com.fei.android.common.viewholder.Res <fields>;
}

-keep class com.fei.android.deeplink.DeepLinkMapping{
    public int value();
}

-keepclassmembers class **.**{
    @com.fei.android.deeplink.DeepLinkMapping <methods>;
}

-keep class com.fei.android.deeplink.DeepLinkResult{
    public int value();
}

-keepclassmembers class **.**{
    @com.fei.android.deeplink.DeepLinkResult <methods>;
}

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.support.v4.app.Fragment

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public <init>(android.content.Context, android.util.AttributeSet, int, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}

-keep class * implements android.os.Parcelable {*;}

-keep class **.R$* {
    public static <fields>;
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-dontwarn java.lang.invoke.*
-dontwarn android.support.**
-dontwarn com.fasterxml.**
-dontwarn com.admaster.**
-dontwarn org.apache.http.**
-dontwarn okio.**
-dontwarn com.tencent.**
-dontwarn com.squareup.**
-dontwarn okhttp3.**
-dontwarn com.squareup.okhttp.**
-keep class com.tencent.** {*;}
-keep class com.umeng.** {*;}
-keep class u.aly.** {*;}
-keep class com.fei.android.network.volley.** {*;}
-keep class org.apache.http.** {*;}

# Okio
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**
# retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keep class com.fei.android.foxtalk.**{*;}
-keepattributes Signature
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
#Crnetwork
-keep class com.fei.crnetwork.** {*;}
-keep class com.fei.processor.** {*;}
# Otto
#-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# rxjava
-dontwarn rx.**
# RxJava
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    long producerNode;
    long consumerNode;
}

# 转化大师
-keep class com.admaster.** {
    *;
}

-keep class com.fei.credit.net.credit.** {
 *;
}

-keep class com.fei.credit.net.credit.**.* {
 *;
}

-keep class com.fei.credit.net.credit.pbcc.** {
 *;
}

-keep class com.fei.credit.net.credit.pbcc.**.* {
 *;
}

-keepclassmembers,allowoptimization enum * {*;}

-keepclassmembernames class * {
    native <methods>;
}

# 忽略所有支持序列化的class
-keep class * implements java.io.Serializable {
    *;
}

# Umeng start==============
-keep,allowshrinking class org.android.agoo.service.* {
    public <fields>;
    public <methods>;
}

-keep,allowshrinking class com.umeng.message.* {
    public <fields>;
    public <methods>;
}

-keepclassmembers class * {
    public <init>(org.json.JSONObject);
}
-keep public class com.umeng.fb.ui.ThreadView {}

# Umeng Push--------------
-dontwarn com.ut.mini.**
-dontwarn okio.**
-dontwarn com.xiaomi.**
-dontwarn com.squareup.wire.**
-dontwarn android.support.v4.**
-dontwarn com.umeng.message.**

-keepattributes *Annotation*
-keepattributes Signature,*Annotation*

-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }

-keep class okio.** {*;}
-keep class com.squareup.wire.** {*;}

-keep class com.umeng.message.protobuffer.* {
	 public <fields>;
         public <methods>;
}

-keep class com.umeng.message.* {
	 public <fields>;
         public <methods>;
}

-keep class org.android.agoo.impl.* {
	 public <fields>;
         public <methods>;
}

-keep class org.android.agoo.service.* {*;}

-keep class org.android.spdy.**{*;}

-keep public class [应用包名].R$*{
    public static final int *;
}
-dontwarn org.apache.http.**
-dontwarn android.webkit.**
-keep class org.apache.http.** { *; }
-keep class org.apache.commons.codec.** { *; }
-keep class org.apache.commons.logging.** { *; }
-keep class android.net.compatibility.** { *; }
-keep class android.net.http.** { *; }
# Umeng end===============

# TD
-keep public class com.tendcloud.tenddata.** { public protected *;}

-keep class com.github.mikephil.charting.** {
  public *;
}
-dontwarn com.github.mikephil.charting.**


-assumenosideeffects class java.io.PrintStream {
     public void println(...);
     public void print(...);
 }
-assumenosideeffects class * extends java.lang.Throwable{
    public void printStackTrace();
}
-assumenosideeffects class com.fei.android.common.utils.Log{
    public static *** d(...);
    public static *** i(...);
    public static *** v(...);
    public static *** w(...);
    public static *** wtf(...);
    public static *** logStackTrace(...);
}
-assumenosideeffects class android.util.Log{
    public static *** d(...);
    public static *** i(...);
    public static *** v(...);
    public static *** w(...);
    public static *** wtf(...);
}
# OkHttp3
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
# RxJava RxAndroid
-dontwarn sun.misc.**
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# Gson
-keep class com.google.gson.stream.** { *; }
-keepattributes EnclosingMethod

-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }

# for GrowingIO
-keep class com.growingio.android.sdk.** {
 *;
}
-dontwarn com.growingio.android.sdk.**
-keepnames class * extends android.view.View

-keep class * extends android.app.Fragment {
 public void setUserVisibleHint(boolean);
 public void onHiddenChanged(boolean);
 public void onResume();
 public void onPause();
}
-keep class android.support.v4.app.Fragment {
 public void setUserVisibleHint(boolean);
 public void onHiddenChanged(boolean);
 public void onResume();
 public void onPause();
}
-keep class * extends android.support.v4.app.Fragment {
 public void setUserVisibleHint(boolean);
 public void onHiddenChanged(boolean);
 public void onResume();
 public void onPause();
}
#butterKnife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
  @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
  @butterknife.* <methods>;
}
#com.franmontiel.persistentcookiejar
-keep class com.franmontiel.persistentcookiejar.**
-keep interface com.franmontiel.persistentcookiejar.**