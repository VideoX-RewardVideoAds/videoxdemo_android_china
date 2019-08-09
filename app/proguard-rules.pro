

-repackageclasses 'com.o0o'
-allowaccessmodification

# keep
-keepattributes Exceptions, InnerClasses
-keep class com.dotc.ll.**{*;}
-keep interface com.dotc.ll.**{*;}

-keep class mobi.android.**{*;}

-keep enum mobi.android.base.DspType{*;}
-keep interface mobi.android.base.AdLoadListener{*;}
-keep interface mobi.android.base.AdShowListener{*;}
-keep interface mobi.android.base.BannerAdListener{*;}
-keep interface mobi.android.base.InterAdLoadListener{*;}
-keep interface mobi.android.base.SplashListener{*;}
# appProtect
-keep class com.guard.appprotect.** {*;}


# keep classes - analytics
-keep class mobi.anasutil.anay.lite.** {*;}

# AppsFlyer
-dontwarn com.appsflyer.**
-keep class com.appsflyer.** {*;}

# Bugly
-dontwarn com.tencent.bugly.**
-keep class com.tencent.bugly.** { *; }
-keep public class com.tencent.bugly.crashreport.crash.jni.NativeCrashHandler{public *; native <methods>;}
-keep public interface com.tencent.bugly.crashreport.crash.jni.NativeExceptionHandler{*;}

-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {
    public static *;
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {
    public *;
}

# google
-dontwarn com.google.**
-keep class com.google.** { *; }

# Vungle
-keep class com.vungle.warren.** { *; }
-dontwarn com.vungle.warren.error.VungleError$ErrorCode

# Okio
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Retrofit
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8


# toutiao
-keep class com.bytedance.sdk.openadsdk.** {*;}
-keep class com.bytedance.sdk.openadsdk.service.TTDownloadProvider
-keep class com.bytedance.article.** {*;}
-keep class com.ss.android.** {*;}
-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}
-keep class com.ss.sys.ces.* {*;}

# android-query
-dontwarn com.androidquery.callback.**
-keep class com.androidquery.** {*;}
-dontwarn oauth.signpost.**

# gdt
-keep class com.qq.e.** {
    public protected *;
}
-keep class android.support.v4.**{
    public *;
}
-keep class android.support.v7.**{
    public *;
}

# ironSource
-keepclassmembers class com.ironsource.sdk.controller.IronSourceWebView$JSInterface {
    public *;
}
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
-keep public class com.google.android.gms.ads.** {
   public *;
}
-keep class com.ironsource.adapters.** {*;}
-keep class com.ironsource.mediationsdk.** {*;}
-dontwarn com.ironsource.mediationsdk.**
-dontwarn com.ironsource.adapters.**
-dontwarn com.moat.**
-keep class com.moat.** { public protected private *; }

# applovin
-dontwarn com.applovin.**
-keep class com.applovin.** {*;}

# adcolony
# For communication with AdColony's WebView
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-keep class com.adcolony.** {*;}
-keep class com.integralads.avid.library.adcolony.** {*;}

-keep class pl.droidsonroids.gif.GifImageView{*;}
-keep class com.bytedance.article.common.nativecrash.NativeCrashInit{*;}
-keep class com.unity3d.player.UnityPlayer{*;}

-keep class nativesdk.ad.common.modules.activityad.imageloader.ImageLoader {*;}
-keep class nativesdk.ad.common.modules.activityad.imageloader.widget.BasicLazyLoadImageView {
    public <fields>;
    public <methods>;
}

#baidu
-keepclassmembers class * extends android.app.Activity {
 public void *(android.view.View);
}
-keepclassmembers enum * {
 public static **[] values();
 public static ** valueOf(java.lang.String);
}
-keep class com.baidu.mobads.*.** { *; }

-dontwarn com.baidu.mobads.**
-dontwarn com.baidu.mobad.feeds.**
-dontwarn com.qq.e.ads.**
-dontwarn com.bytedance.sdk.openadsdk.**
-dontwarn com.facebook.ads.**
-dontwarn com.qq.e.comm.util.**

-ignorewarnings
