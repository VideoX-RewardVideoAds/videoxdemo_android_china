package com.dotc.storm;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mobi.android.BannerAd;
import mobi.android.InterstitialAd;
import mobi.android.NativeAd;
import mobi.android.RewardAd;
import mobi.android.VideoXSDK;
import mobi.android.base.AdLoadListener;
import mobi.android.base.AdShowListener;
import mobi.android.base.BannerAdData;
import mobi.android.base.BannerAdListener;
import mobi.android.base.ComponentHolder;
import mobi.android.base.InterAdLoadListener;
import mobi.android.base.InterstitialAdData;
import mobi.android.base.NativeAdData;
import mobi.android.base.NativeAdLoadListener;
import mobi.android.base.NativeAdViewBinder;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    public static String mAppId = "3552";
    public static String mPubKey = "f52275c7006fe85101637eedb1f73d5b";

    public static String mFullScreenUnitId = "22227";
    public static String mRewardUnitId = "22228";
    public static String mBannerUnitId = "22229";
    public static String mNativeUnitId = "22230";
    public static String mSplashUnitId = "22231";

    private Button loadNativeButton;
    private Button loadReward;
    private Button readyReward;
    private Button showReward;
    private Button initSDK;

    private Button loadInter;
    private Button readyInter;
    private Button showInter;
    private TextView describeText;
    private FrameLayout mAdViewContainer;
    private FrameLayout vBannerAdViewContainer;
    private StringBuilder mDescribleAction;
    private View mLoadBanner;

    private Button mSplashLoadButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDescribleAction = new StringBuilder();

        ComponentHolder.setUnityActivity(this);
        ComponentHolder.setUnity(true);

        initSDK = findViewById(R.id.init_button);

        loadNativeButton = findViewById(R.id.load_native_button);

        loadReward = findViewById(R.id.loadReward);
        readyReward = findViewById(R.id.readyReward);
        showReward = findViewById(R.id.playReward);

        loadInter = findViewById(R.id.loadInter);
        readyInter = findViewById(R.id.readyInter);
        showInter = findViewById(R.id.playInter);

        mLoadBanner = findViewById(R.id.loadBanner);

        describeText = findViewById(R.id.describe_text);
        mAdViewContainer = findViewById(R.id.ad_container_layout);
        vBannerAdViewContainer = findViewById(R.id.banner_container_layout);

        mSplashLoadButton = findViewById(R.id.splash_button);

        initSDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//不需要初始化回调的用这个方法
//              VideoXSDK.initSdk(MainActivity.this, mAppId, mPubKey);

                VideoXSDK.initSdk(MainActivity.this, mAppId, mPubKey,new VideoXSDK.InitListener(){

                    @Override
                    public void initSuccess() {
                        addActionToDescribe("sdk init success");
                    }

                    @Override
                    public void initFail() {
                        addActionToDescribe("sdk init fail");
                    }
                });
            }
        });
        mSplashLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(MainActivity.this,SplashAdActivity.class);
                intent.putExtra("unitId",mSplashUnitId);
                startActivity(intent);
            }
        });

        loadNativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                        NativeAdViewBinder adViewBinder = new NativeAdViewBinder.Builder(getApplicationContext(), R.layout.layout_native_hot_ad_view)
                                .mediaId(R.id.nad_native_ad_media)
                                .adChoicesImageId(R.id.nad_native_ad_choices_image)
//                                .iconImageId(R.id.nad_native_ad_icon_image)
                                .titleTextId(R.id.nad_native_ad_title_text)
                                .subtitleTextId(R.id.nad_native_ad_subtitle_text)
                                .callToActionTextId(R.id.nad_native_ad_call_to_action_text)
                                .interactiveViewId(R.id.rl_view_container)
                                .build();

                        NativeAd.loadAd(mNativeUnitId, adViewBinder, new NativeAdLoadListener() {
                            @Override
                            public void onError(String adUnitId, String error) {
                                addActionToDescribe("native slotId onError");
                            }

                            @Override
                            public void onAdLoaded(String adUnitId, NativeAdData nativeAdData) {
                                addActionToDescribe("native slotId onAdLoaded");

                                nativeAdData.render(mAdViewContainer);
                            }

                            @Override
                            public void onAdClicked(String adUnitId) {
                                addActionToDescribe("native slotId onAdClicked!");
                            }

                            @Override
                            public void onAdExpress(String adUnitId) {
                                addActionToDescribe("native slotId onAdClicked");
                            }
                        });

            }
        });


        loadReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        RewardAd.loadAd(mRewardUnitId, adLoadListener);
                        addActionToDescribe("load reward " + mRewardUnitId);
            }
        });

        readyReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = RewardAd.isReady(mRewardUnitId);
                addActionToDescribe(mRewardUnitId + " rewardAd isReady? " + flag);
            }
        });

        showReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RewardAd.show(mRewardUnitId, adShowListener);
                addActionToDescribe("rewardAd show " + mRewardUnitId);
            }
        });


        loadInter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        InterstitialAd.loadAd(mFullScreenUnitId, interAdLoadListener);
                        addActionToDescribe("load interstitialAd " + mFullScreenUnitId);
            }
        });

        readyInter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean flag = InterstitialAd.isReady(mFullScreenUnitId);
                addActionToDescribe(mFullScreenUnitId + " interstitialAd isReady?    " + flag);
            }

        });

        showInter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InterstitialAd.show(mFullScreenUnitId);
                addActionToDescribe("interstitialAd show    " + mFullScreenUnitId);
            }
        });

        mLoadBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BannerAd.loadAd(mBannerUnitId, new BannerAdListener() {
                    @Override
                    public void onAdLoaded(String adUnitId, BannerAdData bannerAdData) {
                        bannerAdData.addAdView(vBannerAdViewContainer);
                        addActionToDescribe("banner slotId onAdLoaded!");
                    }

                    @Override
                    public void onAdClosed(String adUnitId) {
                        addActionToDescribe("banner slotId onAdClosed!");
                    }

                    @Override
                    public void onAdClicked(String adUnitId) {
                        addActionToDescribe("banner slotId onAdClicked!");
                    }

                    @Override
                    public void onError(String adUnitId, String error) {
                        addActionToDescribe("banner slotId onError! " + error);
                    }
                });
            }
        });

        //  临时给权限
        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermission();
        }
    }


    private AdLoadListener adLoadListener = new AdLoadListener() {
        @Override
        public void onLoad(String slotId) {
            addActionToDescribe("reward onLoad success   " + slotId);
        }

        @Override
        public void onError(String slotId, String errCode) {
            addActionToDescribe(slotId + "    reward onLoad error   " + errCode);
        }
    };

    private AdShowListener adShowListener = new AdShowListener() {
        @Override
        public void onStart(String slotId) {
            addActionToDescribe("reward onStart   " + slotId);
        }

        @Override
        public void onFinish(String slotId, boolean isReward) {
            addActionToDescribe("reward onFinish   " + slotId + "reward:"+isReward);
        }

        @Override
        public void onError(String slotId, String error) {
            addActionToDescribe(slotId + "    reward onError   " + error);
        }

        @Override
        public void onClick(String slotId) {
            addActionToDescribe("reward onClick   " + slotId);
        }
    };

    private InterAdLoadListener interAdLoadListener = new InterAdLoadListener() {
        @Override
        public void onError(String slotId, String error) {
            addActionToDescribe(slotId + "    Inter onError   " + error);

        }

        @Override
        public void onAdLoaded(String slotId, InterstitialAdData interstitialAdData) {
            addActionToDescribe("Inter onAdLoaded   " + slotId);
        }

        @Override
        public void onAdClosed(String slotId) {
            addActionToDescribe("Inter onAdClosed   " + slotId);
        }

        @Override
        public void onAdClicked(String slotId) {
            addActionToDescribe("Inter onAdClicked   " + slotId);
        }
    };


    @Override
    public void onClick(View v) {
    }


    public void addActionToDescribe(String describe) {
        Log.d("Videox",describe);
        try{
            mDescribleAction.append(describe);
            mDescribleAction.append("\n");
            describeText.setText(mDescribleAction.toString());

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();
//        //为了精准定向最好添加该权限
//        if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
//            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
//        }
//        if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
//            lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
//        }

        if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        // 权限都已经有了，那么直接调用SDK
        if (lackedPermission.size() == 0) {

        } else {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {

        } else {
            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
            Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            finish();
        }
    }

}
