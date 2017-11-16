#-------------------------------------------定制化区域----------------------------------------------
#---------------------------------1.实体类---------------------------------

#---------------------------------2.第三方包-------------------------------

#---------------------------------3.与js互相调用的类------------------------

#---------------------------------4.反射相关的类和方法-----------------------

#---------------------------------5.提供给外部的接口----------------------------------------
#-keep class com.dsm.guardsdk.base.GuardSDKLibrary { *; }
#-keep class com.dsm.guardsdk.base.OnOpenListener { *; }
#-keep class com.dsm.guardsdk.base.DeviceGuard { *; }
#-keep class com.dsm.guardsdk.common.dao.* { *; }
#-keep class com.dsm.guardsdk.common.impl.CommonGuardDeviceImpl {
#    public static com.dsm.guardsdk.common.dao.CommonGuardDevice getInstance();
#}
#-keep class com.dsm.guardsdk.common.impl.CommonGuardServerImpl {
#    public static com.dsm.guardsdk.common.dao.CommonGuardServer getInstance();
#}
#
#-keep class com.dsm.guardsdk.hainan.dao.* { *; }
#-keep class com.dsm.guardsdk.hainan.impl.HainanGuardDeviceImpl {
#    public static com.dsm.guardsdk.hainan.dao.HainanGuardDevice getInstance();
#}
#-keep class com.dsm.guardsdk.hainan.impl.HainanGuardServerImpl {
#    public static com.dsm.guardsdk.hainan.dao.HainanGuardServer getInstance();
#}

#-------------------------------------------基本不用动区域--------------------------------------------
#---------------------------------基本指令区----------------------------------
-optimizationpasses 5
-dontskipnonpubliclibraryclassmembers
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}

-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}
-keepclassmembers class * {
    void *(**On*Event);
}
#----------------------------------------------------------------------------

#---------------------------------webview------------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}
#----------------------------------------------------------------------------
#---------------------------------------------------------------------------------------------------