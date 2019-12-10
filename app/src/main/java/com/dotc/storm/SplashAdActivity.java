package com.dotc.storm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.dotc.ll.LocalLog;

import mobi.android.SplashAd;
import mobi.android.VideoXSDK;
import mobi.android.base.ComponentHolder;
import mobi.android.base.SplashListener;


public class SplashAdActivity extends AppCompatActivity implements SplashListener {
    private String mUnitid;
    private RelativeLayout vParentViewGroup;
    private RelativeLayout vDefaultView;

    private boolean mIsClick;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash_ad_view);
        vParentViewGroup = findViewById(R.id.splash_view_container);
        vDefaultView = findViewById(R.id.splash_view_default);
        ComponentHolder.setUnityActivity(this);
        ComponentHolder.setUnity(true);
        mUnitid = getIntent().getStringExtra("unitId");

        if (TextUtils.isEmpty(mUnitid)) {
            VideoXSDK.initSdk(SplashAdActivity.this, MainActivity.mAppId, MainActivity.mPubKey, new VideoXSDK.InitListener() {
                @Override
                public void initSuccess() {
                    LocalLog.w("VideoXSDK init Success ");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SplashAd.loadAd(SplashAdActivity.this, vParentViewGroup, "22231", SplashAdActivity.this);
                        }
                    }, 2000);
                }

                @Override
                public void initFail() {
                    LocalLog.w("VideoXSDK init Fail ");
                }
            });

        } else {
            SplashAd.loadAd(SplashAdActivity.this, vParentViewGroup, mUnitid, SplashAdActivity.this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("VC-Mediation", "splash onResume");
        if (mIsClick) {
            SplashAdActivity.this.finish();
            Intent intent = new Intent();
            intent.setClass(SplashAdActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onError(String unitId, String error) {
        Log.d("VC-Mediation", "splash load error:" + error);
        Intent intent = new Intent();
        intent.setClass(SplashAdActivity.this, MainActivity.class);
        startActivity(intent);
        SplashAdActivity.this.finish();
    }

    @Override
    public void onAdLoadedAndShow() {
        Log.d("VC-Mediation", "onAdLoadedAndShow splash load and show");
        vDefaultView.setVisibility(View.GONE);
    }

    @Override
    public void onAdDismiss() {
        Log.d("VC-Mediation", "onAdDismiss splash view close");
        if (TextUtils.isEmpty(mUnitid)) {
            SplashAdActivity.this.finish();
            Intent intent = new Intent();
            intent.setClass(SplashAdActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            SplashAdActivity.this.finish();
        }
    }

    @Override
    public void onAdClicked() {
        Log.d("VC-Mediation", "onAdClicked");
        mIsClick = true;
    }

    /**
     * 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
