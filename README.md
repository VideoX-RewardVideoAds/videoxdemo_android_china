# VideoXSDK Android

------

# 接入方式

- 通过运营获取架包的网络地址：com.videox.sdk:release_vc\*：\*
- 在目标项目对应 module 的 dependencies 中添加上述新加包的依赖
- 按照 build.gradle 文件内容添加相应的依赖
- 添加不同平台的warehouse地址，请参阅演示项目的build.gradle或dependencies文件

------

# Dependencies

##### 1.需要在工程的build.gradle文件中添加以下代码

```groovy
buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.3.2'//以开发环境的为准
        classpath 'com.google.gms:google-services:4.1.0'
    }
}
allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url "https://jitpack.io"
        }
        maven {
            url "https://dl.bintray.com/ironsource-mobile/android-sdk"
        }
        maven {
            url  "https://fyber.bintray.com/fairbid-maven"
        }
        maven {
            url  "https://adcolony.bintray.com/AdColony"
        }
        maven {
            url 'http://maven.getvideox.cn/repository/videox/'
        }
 }
}
```


##### 2.添加VideoX架包到对应项目APP的build.gradle文件中
```groovy
dependencies {
// 支持multidex功能
implementation 'com.android.support:multidex:1.0.3'
// baselib
implementation 'com.google.code.gson:gson:2.8.2'
implementation 'com.google.android.gms:play-services-basement:15.0.1'
implementation 'com.google.android.gms:play-services-ads-identifier:15.0.1'
    
// videoX Adbroad(国外默认版本，具体以运营提供为准)
//implementation 'com.videox.sdk:release_va2.0.1:11111111'
// videoX China(国内默认版本，具体以运营提供为准)
    implementation 'com.videox.sdk:release_vc2.0.5.01:1111111'
}

```

##### 3.在Mainfest.xml文件中添加相关权限

```xml
<uses-permission    android:name="android.permission.WRITE_EXTERNAL_STORAGE"    tools:remove="maxSdkVersion" />
```

##### 4.添加混淆文件

将Github中demo下面的proguard-rules.pro文件中内同添加到自己的混淆文件中



##### 注意事项

（1）对应VideoX架包从运营人员中获取，具体版本号以提供的为准。

（2）建议不要随便增加其他平台广告架包，如有需要告知运营，否则造成不兼容的问题。

（3）官方demo GitHub地址：https://github.com/VideoX-RewardVideoAds。

（4）target SDK大于等于23（6.0）时，则需要动态申请“android.permission.WRITE_EXTERNAL_STORAGE”权限

# 初始化

广告平台需要Activity对象，在长期存活的界面（如工程的主界面）中添加下列代码。

```java
ComponentHolder.setUnityActivity(this);
ComponentHolder.setUnity(true);
```

public static void initSdk(Context context,  String appId, String pubKey)

```java
VideoXSDK.initSdk(this,"appid","pubKey");
```

##### 注意事项

- appId:application id, 请在控制台app page中查找或由运营提供。
- pubKey: encrypted key, 请在控制台account page中查找或由运营提供。

- 若项目中没有集成AppsFlyer的SDK请忽略该点。若有，需实现AppsFlyer的`registerConversionListener`监听方法；并在`onInstallConversionDataLoaded`回调方法中调用`VideoXSDK.setOnInstallConversionDataLoaded`方法，如下所示：
   ```java
   AppsFlyerLib.getInstance().registerConversionListener(this, new AppsFlyerConversionListener() {
   @Override
   public void onInstallConversionDataLoaded(Map<String, String> conversionData) {
      VideoXSDK.setOnInstallConversionDataLoaded(conversionData);
   }
   @Override
   public void onInstallConversionFailure(String error) {
      	// do something...
   }});
   ```

------

# 基本功能

##### 1、激励视频广告

#####  请求加载广告
   ```java
   RewardAd.loadAd("adUnitId", new AdLoadListener() {
       @Override
       public void onLoad(String adUnitId) {
             // do something...
             // attention: don't showAd in this callback
         }

       @Override
       public void onError(String adUnitId, String errCode) {
             // do something...
             // attention: don't loadAd in this callback
       }
    });
   ```




#####  判断广告是否加载成功

​    `boolean isReady = RewardAd.isReady("adUnitId");`



##### 展示广告

   ```java
   RewardAd.show("adUnitId", new AdShowListener() {
        @Override
        public void onStart(String adUnitId) {
           // do something...
        }
        @Override
        public void onFinish(String adUnitId, boolean isReward) {
			//isReward表示当前是否有激励的返回
        }
        @Override
        public void onError(String adUnitId, String error) {
			//error提示错误的原因
        }
        @Override
        public void onClick(String adUnitId) {
			// do something...
       }
    });
   ```

##### 注意事项

- adUnitId: ad unit id（广告位的id）,  请在app page中查找
- 每次广告在show之前的逻辑场景需要先用load的方法进行广告加载，然后通过isReady判断是否缓存成功（激励视频需要缓存广告素材，需要一定的时间），如果isReady为true则可以调用show方法来展示。
- 禁止在 onLoad 回调中调用 RewardAd.show 方法
- 禁止在 onError 回调中直接调用 RewardAd.loadAd，可能会造成一直循环请求的bug.


##### 2、插屏广告

#####  请求加载广告
```java
   InterstitialAd.loadAd("adUnitId", new InterAdLoadListener() {
        @Override
        public void onError(String adUnitId, String error) {
              // do something...
        }
        @Override
        public void onAdLoaded(String adUnitId, InterstitialAdData nativeAdData) {
              // show ad or save ad and show it on right scence
              nativeAdData.show();
        }
        @Override
        public void onAdClosed(String adUnitId) {
              // do something...
        }
        @Override
        public void onAdClicked(String adUnitId) {
             // do something...
        }
     });
```
##### 判断广告是否准备完毕
  `InterstitialAd.isReady("adUnitId");`

##### 展示广告
  `InterstitialAd.show("adUnitId");`

##### 注意事项

- InterAdLoadListener接口中onAdLoaded 回调方法中的第二个参数对象，可以直接播放；也可以将数据保存在适当的时机展示。

##### 3、开屏广告（仅国内包支持）

#####  请求加载广告
```java
	/**
	*Activity:当前需要展示开屏的界面
	*ViewGroup:开屏广告返回的是View，需要容器加载展示
	*Unitid：开屏广告的广告为id
	*SplashListener：对开屏广告请求结果的回调
	*/
  SplashAd.loadAd(Activity, ViewGroup, "Unitid", SplashListener);
```
##### 开屏调用的时机

​		将初始化代码放到开屏界面，用`VideoXSDK.initSdkcontext,"appId","pubKey", InitListener)`来检测初始化成功与否。并在initSuccess的回调方法中调用插屏的请求.

##### 开屏界面中需要注意的逻辑

下面的是开屏广告的回调接口

```java
public interface SplashListener {    
    void onError(String unitId, String error);    
    void onAdLoadedAndShow();    
    void onAdDismiss();    
    void onAdClicked();}
```

开屏广告实在开屏展示的时候，进行广告请求，如果请求成功则返回广告素材，展示在开屏界面上。

（1）如果广告加载失败，调用onError方法，则直接跳转到主界面（关闭开屏界面，下同）。

（2）广告加载成功之后，在onAdLoadedAndShow方法中将ViewGroup容器显示出来。

（3）若用户不点击广告倒计时结束，和点击跳过一样都会调用onAdDismiss方法。则直接跳转到主界面。

（4）广告分为网页和下载2钟类型， 点击网页广告之后会造成onAdClicked和onAdDismiss的立即回调。这时处理则会导致广告界面被覆盖。此时在Activity的onPause中设置开关。如下

```java
boolean mIsPause = false;

@Overrideprotected 
void onPause() {    
    super.onPause();    
    mIsPause = true;
}
```

（5）mIsPause表示为是否网页广告唤醒了广告的落地页。则在onAdDismiss方法中做如下处理

```java
@Override
public void onAdDismiss() {
  	LocalLog.d("splash view close");
  	if (mIsPause) {
    	//如果为网页的回调则不进行处理
    	return;
	}
	//关闭开屏界面，跳转到主页逻辑（不能和上面的代码调换位置）
}
```

（6）广告点击之后，展示完毕会返回应用，则在这时需要对开屏进行关闭，跳转到主界面。

```java
@Override
protected void onResume() {    
    super.onResume();    
    if(mIsPause){        
        //关闭开屏界面，跳转到主界面    
    }
}
```

##### 注意事项

- （1）开屏广告需要获取当前应用的广告配置之后才可以展示，所以第一次启动，会无法展示。
- （2）在开屏界面和工程的主界面都需要添加下列代码。
```java
	ComponentHolder.setUnityActivity(this);
	ComponentHolder.setUnity(true);
```
- （3）建议启动有一个默认的启动图片，然后开屏广告加载成功之后，替换该图片。最终的跳转根据加载的结果做相应处理。
- （4）安装类型广告不会唤起落地页，所以和正常倒计时结束一样在onAdDismiss处理关闭跳转。

##### 4、原生广告

 (1)首先，生成NativeAdViewBinder对象，用来填充自定义展示Native广告的样式和对应的填充组件。

```java
new NativeAdViewBinder.Builder(getApplicationContext(), R.layout.ad_view)
                      .mediaId(R.id.ad_media)
                      .iconImageId(R.id.ad_icon_image)
                      .titleTextId(R.id.ad_title_text)
                      .subtitleTextId(R.id.ad_subtitle_text)
                      .callToActionTextId(R.id.ad_call_to_action_text)
                      .interactiveViewId(R.id.rl_view_container)
                      .build();
```

> - R.layout.ad_view: 当前自定的原生广告布局xml文件的layout。
> - R.id.ad_media: 多媒体控件，支持图片，gif等格式的展示，需采用Videox中提供的mobi.android.base.Vid      eoXNativeMediaView自定义控件。
> - R.id.ad_icon_image: 小图标图片，格式为VideoXNativeMediaView。
> - R.id.ad_title_text: 主标题，TextView。
> - R.id.ad_subtitle_text: 副标题，TextView。
> - R.id.ad_call_to_action_text：动作按钮，TextView。
> - R.id.rl_view_container：原生广告xml中第一层容器的id。

 (2)请求广告

```java
/**
*adUnitId:广告位的id
*adViewBinder:用来填充自定义展示Native广告的样式和对应的填充组件的对象
*nativeAdLoadListener：Native广告加载之后的回调接口
*/
NativeAd.loadAd(adUnitId, adViewBinder, nativeAdLoadListener);
```

将广告位的adUnitId,NativeAdViewBinder,以及回调的Listener填入loadAd方法，进行广告请求。

 (3)通过接口NativeAdLoadListener监听广告的回调结果。

```java
 public interface NativeAdLoadListener {
    //广告加载失败
    void onError(String var1, String var2);
    //广告加载成功
    void onAdLoaded(String var1, NativeAdData nativeAdData);
    //广告点击回调
    void onAdClicked(String var1);
    //广告展示回调（部分平台没有该回调）
    void onAdExpress(String var1);
}
```

（4）广告展示
广告加载成功之后回调onAdLoaded方法。通过NativeAdData对象，将广告的View渲染到需要添加的容器中。
`nativeAdData.render(adViewContainer);`

##### 5、横幅广告

(1) 请求横幅广告

```java
/**
*adUnitId:广告位的id
*bannerAdListener：Banner广告加载之后的回调接口
*/
BannerAd.loadAd(adUnitId, bannerAdListener); 
```

(2）Banner广告的监听回调

```java
public interface BannerAdListener {
    //广告加载成功
    void onAdLoaded(String var1, BannerAdData bannerAdData);

    void onAdClosed(String var1);
    //广告点击回调
    void onAdClicked(String var1);
    //广告加载失败
    void onError(String unitId, String error);
}
```

（3）展示Banner广告
广告加载成功，则会通过onAdLoaded接口进行回调。通过BannerAdData对象将广告View渲染到提供的父容器中，父容器的高度大小可以设置为80dp,根据返回的大小适当调整。。
`bannerAdData.addAdView(adViewContainer);`

##### 注意事项

> - 如果发现分包打包在 Android5.0 以下手机出现崩溃现象(not found class) 请让Application类继承自 MultiDexApplicaiton，如果没有Application类请手动添加。

> - 如果出现重复引入类的问题。在终端输入gradlew :'app':dependencies查看各个架包中以来的类，并判断重复的类在哪些架包中。通过exclude group: 或者 exclude module: 的方式剔除重复的类。命令中'app'为当前的module名称。


##### 广告位的测试UnitId,可以在测试的demo种填写以下UnitId，填充展示相应类型的广告。
- 插屏UnitI         :22227
- 激励视频UnitId    :22228
- 横幅广告UnitId    :22229
- 高级原生广告UnitId:22230
- 开屏广告UnitId    :22231
