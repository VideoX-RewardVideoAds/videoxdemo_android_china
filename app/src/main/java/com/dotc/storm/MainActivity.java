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

import com.dotc.ll.LocalLog;

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
    private EditText vNativeSlotIdEdit;
    private EditText editTextReward;
    private Button loadNativeButton;
    private Button showNativeButton;
    private Button loadReward;
    private Button readyReward;
    private Button showReward;
    private Button initSDK;

    private EditText editTextInter;
    private Button loadInter;
    private Button readyInter;
    private Button showInter;
    private TextView describeText;
    private FrameLayout mAdViewContainer;
    private StringBuilder mDescribleAction;
    private View mLoadBanner;
    private EditText mEtBanner;

    private EditText mSplashUnitIdEdit;
    private Button mSplashLoadButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDescribleAction = new StringBuilder();

        ComponentHolder.setUnityActivity(this);
        ComponentHolder.setUnity(true);

        initSDK = findViewById(R.id.init_button);

        vNativeSlotIdEdit = findViewById(R.id.native_slotid_Edit);
        loadNativeButton = findViewById(R.id.load_native_button);
        showNativeButton = findViewById(R.id.show_native_button);

        editTextReward = findViewById(R.id.editText);
        loadReward = findViewById(R.id.loadReward);
        readyReward = findViewById(R.id.readyReward);
        showReward = findViewById(R.id.playReward);

        editTextInter = findViewById(R.id.interEdit);
        loadInter = findViewById(R.id.loadInter);
        readyInter = findViewById(R.id.readyInter);
        showInter = findViewById(R.id.playInter);

        mLoadBanner = findViewById(R.id.loadBanner);
        mEtBanner = findViewById(R.id.bannerrEdit);

        describeText = findViewById(R.id.describe_text);
        mAdViewContainer = findViewById(R.id.ad_container_layout);

        mSplashUnitIdEdit = findViewById(R.id.splash_edit);
        mSplashLoadButton = findViewById(R.id.splash_button);

        initSDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoXSDK.initSdk(MainActivity.this, mAppId, mPubKey);
            }
        });
        mSplashLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String splashUnitid = mSplashUnitIdEdit.getText().toString().trim();
                if(TextUtils.isEmpty(splashUnitid)){
                    Toast.makeText(MainActivity.this,"please fill unitid.",Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent();
                intent.setClass(MainActivity.this,SplashAdActivity.class);
                intent.putExtra("unitId",splashUnitid);
                startActivity(intent);
            }
        });

        loadNativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        String slotId = vNativeSlotIdEdit.getText().toString().trim();
                        if (TextUtils.isEmpty(slotId)) {
                            addActionToDescribe("native slotId is empty,please add!");
                        }

                        NativeAdViewBinder adViewBinder = new NativeAdViewBinder.Builder(getApplicationContext(), R.layout.layout_native_hot_ad_view)
                                .mediaId(R.id.nad_native_ad_media)
                                .adChoicesImageId(R.id.nad_native_ad_choices_image)
//                                .iconImageId(R.id.nad_native_ad_icon_image)
                                .titleTextId(R.id.nad_native_ad_title_text)
                                .subtitleTextId(R.id.nad_native_ad_subtitle_text)
                                .callToActionTextId(R.id.nad_native_ad_call_to_action_text)
                                .interactiveViewId(R.id.rl_view_container)
                                .build();

                        NativeAd.loadAd(slotId, adViewBinder, new NativeAdLoadListener() {
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

        showNativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        loadReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        RewardAd.loadAd(editTextReward.getText().toString(), adLoadListener);
                        addActionToDescribe("load reward " + editTextReward.getText());
            }
        });

        readyReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = RewardAd.isReady(editTextReward.getText().toString());
                addActionToDescribe(editTextReward.getText() + "rewardAd isReady? " + flag);
            }
        });

        showReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RewardAd.show(editTextReward.getText().toString(), adShowListener);
                addActionToDescribe("rewardAd show " + editTextReward.getText());
            }
        });


        loadInter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        InterstitialAd.loadAd(editTextInter.getText().toString(), interAdLoadListener);
                        addActionToDescribe("load interstitialAd " + editTextInter.getText());
            }
        });

        readyInter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean flag = InterstitialAd.isReady(editTextInter.getText().toString());
                addActionToDescribe(editTextInter.getText() + "interstitialAd isReady?    " + flag);
            }

        });

        showInter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InterstitialAd.show(editTextInter.getText().toString());
                addActionToDescribe("interstitialAd show    " + editTextInter.getText());
            }
        });

        mLoadBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String slotId = mEtBanner.getText().toString();
                if (TextUtils.isEmpty(slotId)) return;
                BannerAd.loadAd(slotId, new BannerAdListener() {
                    @Override
                    public void onAdLoaded(String adUnitId, BannerAdData bannerAdData) {
                        bannerAdData.addAdView(mAdViewContainer);
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
//        if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
//            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
//        }
        //为了精准定向最好添加该权限
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
