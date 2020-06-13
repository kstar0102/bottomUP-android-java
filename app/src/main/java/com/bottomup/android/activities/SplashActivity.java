package com.bottomup.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.bottomup.android.R;
import com.bottomup.android.common.Common;
import com.bottomup.android.common.LocalStorageManager;
import com.bottomup.android.models.UserModel;
import com.coreasp.CorePushAppLaunchAnalyticsManager;
import com.coreasp.CorePushManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // intentオブジェクトから通知IDを取得する
        String pushId = CorePushManager.getInstance().getPushId(getIntent());

        if (pushId != null) {
            CorePushAppLaunchAnalyticsManager appLaunchAnalyticsManager = new CorePushAppLaunchAnalyticsManager(this);
            appLaunchAnalyticsManager.requestAppLaunchAnalytics(pushId, "0", "0", new CorePushAppLaunchAnalyticsManager.CompletionHandler() {
                @Override
                public void appLaunchAnalyticsManagerSuccess() {
                    Toast.makeText(SplashActivity.this, "起動数送信成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void appLaunchAnalyticsManagerFail() {
                    Toast.makeText(SplashActivity.this, "起動数送信失敗", Toast.LENGTH_SHORT).show();

                }
            });
        }
        // 分散配信機能
//        if (pushId != null) {
//            CorePushAppLaunchAnalyticsManager appLaunchAnalyticsManager = new CorePushAppLaunchAnalyticsManager(this);
//            appLaunchAnalyticsManager.requestAppLaunchAnalytics(pushId, "101", "0", "0", new CorePushAppLaunchAnalyticsManager.CompletionHandler() {
//                @Override
//                public void appLaunchAnalyticsManagerSuccess() {
//                    Toast.makeText(SplashActivity.this, "送信成功", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void appLaunchAnalyticsManagerFail() {
//                    Toast.makeText(SplashActivity.this, "送信失敗", Toast.LENGTH_SHORT).show();
//
//                }
//            });
//        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (LocalStorageManager.getAccountIndex() == null) {
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    intent.putExtra("fromAc", 1);
                    startActivity(intent);
                }else {
                    String userInfo = LocalStorageManager.getAccountIndex();
                    Common.me = new UserModel(Common.cm.convertToHashMapFromString(userInfo));
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, 2000);
    }
}
