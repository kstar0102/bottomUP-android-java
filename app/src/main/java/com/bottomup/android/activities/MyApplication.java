package com.bottomup.android.activities;

import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.bottomup.android.common.Common;
import com.coreasp.CoreAspManager;

public class MyApplication extends MultiDexApplication {
    private static final String TAG = "FirebaseService";

    public void onCreate(){
        super.onCreate();

        Log.d(TAG, "Myapplication!!!" + "");
        Common.myApp = this;
        CoreAspManager.initialize(this);
    }

}
