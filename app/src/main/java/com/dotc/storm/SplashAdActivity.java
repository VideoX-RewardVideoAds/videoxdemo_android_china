package com.dotc.storm;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dotc.ll.LocalLog;

import java.util.ArrayList;
import java.util.List;

import mobi.android.SplashAd;
import mobi.android.VideoXSDK;
import mobi.android.base.SplashListener;


public class SplashAdActivity extends AppCompatActivity implements SplashListener {
    private static final String UNIT_ID = "21631";
    private static final String APP_ID = "3271";
    private static final String PUB_KEY = "f52275c7006fe85101637eedb1f73d5b";

    private RelativeLayout vParentViewGroup;
    private RelativeLayout vDefaultView;

    private boolean mIsPause;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash_ad_view);
        vParentViewGroup = findViewById(R.id.splash_view_container);
        vDefaultView = findViewById(R.id.splash_view_default);

        //  临时给权限
        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermission();
        }
        //开屏广告最好将初始化放到开屏界面
        VideoXSDK.initSdk(SplashAdActivity.this, APP_ID, PUB_KEY, new VideoXSDK.InitListener() {
            @Override
            public void initSuccess() {
                //为了防止广告请求的时间过长，可以在固定时间之后跳转到主界面。
                SplashAd.loadAd(SplashAdActivity.this, vParentViewGroup, UNIT_ID, SplashAdActivity.this);
            }

            @Override
            public void initFail() {

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsPause) {
            //关闭开屏界面，跳转到主界面
            SplashAdActivity.this.finish();
            Intent intent = new Intent();
            intent.setClass(SplashAdActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onError(String unitId, String error) {
        LocalLog.d("splash load error:" + error);
        SplashAdActivity.this.finish();
        Intent intent = new Intent();
        intent.setClass(SplashAdActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAdLoadedAndShow() {
        vDefaultView.setVisibility(View.GONE);
        LocalLog.d("splash load and show");
    }

    @Override
    public void onAdDismiss() {
        LocalLog.d("splash view close");
        if (mIsPause) {
            return;
        }
        SplashAdActivity.this.finish();
        Intent intent = new Intent();
        intent.setClass(SplashAdActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAdClicked() {
        LocalLog.d("splash view click");
    }


    /**
     * ----------非常重要----------
     * <p>
     * Android6.0以上的权限适配简单示例：
     * <p>
     * 如果targetSDKVersion >= 23，那么必须要申请到所需要的权限，再调用广点通SDK，否则广点通SDK不会工作。
     * <p>
     * Demo代码里是一个基本的权限申请示例，请开发者根据自己的场景合理地编写这部分代码来实现权限申请。
     * 注意：下面的`checkSelfPermission`和`requestPermissions`方法都是在Android6.0的SDK中增加的API，如果您的App还没有适配到Android6.0以上，则不需要调用这些方法，直接调用广点通SDK即可。
     */
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
