package com.bottomup.android.common;

import android.content.SharedPreferences;

import com.bottomup.android.R;

/**
 *
 * アプリ内のデータの保存を管理するクラス
 *
 */

public class LocalStorageManager {

    public static void saveAccountIndex(String ind) {
        SharedPreferences sharedPreferences = Common.myApp.getSharedPreferences(String.valueOf(R.string.app_name), Common.myApp.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("account", ind);
        editor.commit();
    }

    public static String getAccountIndex() {
        SharedPreferences sharedPreferences = Common.myApp.getSharedPreferences(String.valueOf(R.string.app_name), Common.myApp.MODE_PRIVATE);
        String syncState = sharedPreferences.getString("account", null);
        return syncState;
    }

    public static void saveAgreeInfo(String ind) {
        SharedPreferences sharedPreferences = Common.myApp.getSharedPreferences(String.valueOf(R.string.app_name), Common.myApp.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("agree", ind);
        editor.commit();
    }

    public static String getAgreeInfo() {
        SharedPreferences sharedPreferences = Common.myApp.getSharedPreferences(String.valueOf(R.string.app_name), Common.myApp.MODE_PRIVATE);
        String syncState = sharedPreferences.getString("agree", null);
        return syncState;
    }
}

