# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/android-sdk/tools/proguard/proguard-android.txt
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
-dontwarn com.android.volley.**
-dontwarn com.squareup.okhttp.**
-dontwarn okio.**
-dontwarn com.umeng.**

#指定代码的压缩级别
-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
 #优化  不优化输入的类文件
-dontoptimize
 #预校验
-dontpreverify
 #混淆时是否记录日志
-verbose
 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#保护注解
-keepattributes *Annotation*
# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.jin.domain.Results { *;}
-keep class com.jin.domain.Results$BiaoBaiItem{
    public <fields>;
    public <methods>;
}

#-keep class com.jin.domain.BiaoBaiItem { *; public <fields>;
#                                               public <methods>;}
##---------------End: proguard configuration for Gson  ----------
-keep class com.google.gson.** { *;}
#如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment
#忽略警告
-ignorewarning
#记录生成的日志数据,gradle build时在本项目根目录输出
#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt
#记录生成的日志数据，gradle build时 在本项目根目录输出-end
#混淆保护自己项目的部分代码以及引用的第三方jar包library
#- libraryjars libs/easemobchat_2.2.2.jar
#- libraryjars libs/ksoap2-android-assembly-2.6.5-jar-with-dependencies.jar
#三星应用市场需要添加:sdk-v1.0.0.jar,look-v1.0.1.jar
#-libraryjars libs/sdk-v1.0.0.jar
#-libraryjars libs/look-v1.0.1.jar
#我是以libaray的形式引用了一个图片加载框架,如果不想混淆 keep 掉
-keep class com.nostra13.universalimageloader.** { *; }
#友盟
-keep class com.umeng.**{*;}
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
#支付宝
-keep class com.alipay.android.app.IAliPay{*;}
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.lib.ResourceMap{*;}
#信鸽推送
-keep class com.tencent.android.tpush.**  {* ;}
-keep class com.tencent.mid.**  {* ;}
#自己项目特殊处理代码

#忽略警告
-dontwarn com.veidy.mobile.common.**
#保留一个完整的包
-keep class com.getbase.floatingactionbutton.**{
    *;
}
-keep class  com.veidy.activity.login.WebLoginActivity{*;}
-keep class  com.veidy.activity.UserInfoFragment{*;}
-keep class  com.veidy.activity.HomeFragmentActivity{*;}
-keep class  com.veidy.activity.CityActivity{*;}
-keep class  com.veidy.activity.ClinikActivity{*;}
#如果引用了v4或者v7包
-dontwarn android.support.**
-keep class android.support.**{*;}
#混淆保护自己项目的部分代码以及引用的第三方jar包library-end#
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}
#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable
#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#保持枚举 enum 类不被混淆 如果混淆报错，建议直接使用上面的 -keepclassmembers class * implements java.io.Serializable即可
-keepclassmembers enum * {
  public static **[] values();
  public static ** valueOf(java.lang.String);
}
-keepclassmembers class * {
    public void *ButtonClicked(android.view.View);
}
#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}
#避免混淆泛型 如果混淆报错建议关掉
#–keepattributes Signature

#环信有关类com.easemob.chat  com/easemob/media
-keep class vi.com.** {*;}
-keep class com.hyphenate.chat.** {*;}
-keep class com.hyphenate.media.** {*;}

-keep class armeabi.** {*;}
-keep class com.baidu.** {*;}

-keep class org.**{*;}

-keep class com.baidu.** {*;}
-keep class lib.** {*;}

-keep class com.parse.**{*;}
-keep class com.nostra13.**{*;}
-keep class com.google.**{*;}

-keep class com.hyphenate.** {*;}
-keep class org.jivesoftware.** {*;}
-keep class org.apache.** {*;}
-dontwarn  com.hyphenate.**
#2.0.9后的不需要加下面这个keep
#-keep class org.xbill.DNS.** {*;}
#另外，demo中发送表情的时候使用到反射，需要keep SmileUtils,注意前面的包名，
#不要SmileUtils复制到自己的项目下keep的时候还是写的demo里的包名
-keep class **.utils.SmileUtils {*;}

#2.0.9后加入语音通话功能，如需使用此功能的api，加入以下keep
-dontwarn ch.imvs.**
-dontwarn org.slf4j.**
-keep class org.ice4j.** {*;}
-keep class net.java.sip.** {*;}
-keep class org.webrtc.voiceengine.** {*;}
-keep class org.bitlet.** {*;}
-keep class org.slf4j.** {*;}
-keep class ch.imvs.** {*;}

#webview的打包混淆
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*
-keepclassmembers class **.widget.view.SwipeWebview$* {
  *;
}
-keep  class com.boyuyun.applib.boyuyunmanager.CommonJsToJava {
  public <methods>;
}
-keep class android.webkit.JavascriptInterface {*;}

-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* {*;}
-keep public class javax.**


-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}

-keep class com.tencent.** {*;}
-dontwarn com.tencent.**

-keep public class com.umeng.soexample.R$*{
    public static final int *;
}
-keep public class com.umeng.soexample.R$*{
    public static final int *;
}
-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}
-keep class  com.alipay.share.sdk.** {
   *;
}
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**

-keep class org.kobjects.** { *; }

-keep class org.ksoap2.** { *; }

-keep class org.kxml2.** { *; }

-keep class org.xmlpull.** { *; }

-keep public class **.R$*{
    public static final int *;
}

#极光推送
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
#==================gson==========================
-dontwarn com.google.**
-keep class com.google.gson.** {*;}

#==================protobuf======================
-dontwarn com.google.**
-keep class com.google.protobuf.** {*;}

#-libraryjars libs/armeabi/libhyphenate_av.so
#-libraryjars libs/armeabi-v7a/libhyphenate_av.so

#-libraryjars libs/arm64-v8a/libhyphenate_av.so
#-libraryjars libs/x86/libhyphenate_av.so



#-keep class com.hyphenate.* {*;}
#-keep class com.hyphenate.chat.** {*;}
# # -------------------------------------------
# #  ######## greenDao混淆  ##########
# # -------------------------------------------
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties
