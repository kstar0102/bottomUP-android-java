package com.bottomup.android.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bottomup.android.R;
import com.bottomup.android.common.Common;
import com.bottomup.android.common.Config;
import com.bottomup.android.common.LocalStorageManager;
import com.bottomup.android.models.UserModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static com.bottomup.android.common.Common.setUpWebViewDefaults;

public class TermsAndUseActivity extends AppCompatActivity {

    private CheckBox checkBox;
    private LinearLayout btnLayout;

    private Boolean checked = false;
    private int fromAc = 0;
    private HashMap<String, String> userMap = null;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_use);
        Common.currentActivity = this;

        WebView contentTxt = findViewById(R.id.contentTxt);
        contentTxt.loadUrl("file:///android_asset/bottomUP.html");

        btnLayout = findViewById(R.id.btnLayout);
        btnLayout.setVisibility(View.INVISIBLE);

        checkBox = findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checked = isChecked;
                if (checked) {
                    btnLayout.setVisibility(View.VISIBLE);
                }else {
                    btnLayout.setVisibility(View.INVISIBLE);
                }
            }
        });

        Button agreeBtn = findViewById(R.id.agreeBtn);
        agreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userMap != null) {
                    UserModel userModel = new UserModel(userMap);
                    Common.me = userModel;
                    LocalStorageManager.saveAccountIndex(Common.cm.convertToStringFromHashMap(userMap));
                }
                if (fromAc == 1) {
                    Intent intent = new Intent(Common.currentActivity, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    finish();
                }
            }
        });
        RelativeLayout closeBtn = findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fromAc = getIntent().getIntExtra("fromAc", 0);
        if (fromAc == 0) {
            checkBox.setVisibility(View.GONE);
            agreeBtn.setVisibility(View.GONE);
            closeBtn.setVisibility(View.VISIBLE);
        }else {
            closeBtn.setVisibility(View.GONE);
            userMap = (HashMap<String, String>) getIntent().getSerializableExtra("userInfo");
        }
    }
}
