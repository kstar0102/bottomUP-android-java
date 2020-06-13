package com.bottomup.android.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;

import com.bottomup.android.activities.MyApplication;
import com.bottomup.android.R;
import com.bottomup.android.models.UserModel;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class Common {
    //　共通

    /** Commonクラスのオブジェクト */
    public static Common cm = new Common();

    /** 現在のActivityを保存するオブジェクト */
    public static Activity currentActivity = null;

    /** MyApplicationを保存するオブジェクト */
    public static MyApplication myApp;

    public static UserModel me = null;

    /**
     *
     * アラートを表示する。
     * */
    public void showAlertDlg(String title, String msg, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder alert = new AlertDialog.Builder(currentActivity);
        alert.setTitle(title);
        alert.setMessage(msg);
        alert.setPositiveButton(currentActivity.getResources().getString(R.string.OK), positiveListener);
        if (negativeListener != null) alert.setNegativeButton(currentActivity.getResources().getString(R.string.Cancel), negativeListener);
        alert.create();
        alert.show();
    }

    /**
     *
     * 必要な許可を確認する
     *
     * @param permissionsToRequest 必要な許可のリスト
     *
     * @return ArrayList 可能な許容のリスト
     * */
    public ArrayList checkPermissions(ArrayList<String> permissionsToRequest) {
        ArrayList<String> permissionsRejected = new ArrayList();
        for (String perms : permissionsToRequest) {
            if (!hasPermission(perms)) {
                permissionsRejected.add(perms);
            }
        }
        return permissionsRejected;
    }

    /**
     *
     * 許可の状態をを判断する
     *
     * @return boolean 許可の状態 true / false
     * */
    public boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (currentActivity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    public boolean hasInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) currentActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    /**
     *
     * Androidのバージョンを判断する
     *
     * @return boolean Android6.0より低いバージョンの場合 false / true
     * */
    public boolean canAskPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) return true;
        else return false;
    }

    /**
     * Convenience method to set some generic defaults for a
     * given WebView
     *
     * @param webView
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setUpWebViewDefaults(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setSaveFormData(true);
        settings.setSupportZoom(false);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        // Enable Javascript
        settings.setJavaScriptEnabled(true);

        // Use WideViewport and Zoom out if there is no viewport defined
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        // Enable pinch to zoom without the zoom buttons
        settings.setBuiltInZoomControls(true);

        // Allow use of Local Storage
        settings.setDomStorageEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Hide the zoom controls for HONEYCOMB+
            settings.setDisplayZoomControls(false);
        }

        // Enable remote debugging via chrome://inspect
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        webView.setWebViewClient(new WebViewClient());

        // AppRTC requires third party cookies to work
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptThirdPartyCookies(webView, true);
    }

    /**
     * 文字列配列から文字列を取得する
     *
     * @param arr 文字列配列
     * @return String 文字列
     * */
    public String stringFromStringArray(ArrayList<String> arr) {
        String str = "";
        int ii = 0;
        for (String item : arr) {
            if (ii == 0) {
                str = item;
            }else {
                str = str + ", " + item;
            }
            ii ++;
        }
        return str;
    }

    /**
     * HashMapから文字列を取得する
     *
     * @param jsonObj HashMap data
     * @return String 文字列
     * */
    public String convertToStringFromHashMap(HashMap<String, String> jsonObj) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String key : jsonObj.keySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            String value = jsonObj.get(key);
            try {
                try {
                    stringBuilder.append((key != null ? URLEncoder.encode(key, "UTF-8") : ""));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                stringBuilder.append("=");
                stringBuilder.append(value != null ? URLEncoder.encode(value, "UTF-8") : "");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("error", e);
            }
        }

        return stringBuilder.toString();
    }

    /**
     * 文字列からHashMapを取得する
     *
     * @param str String 文字列
     * @return HashMap data
     * */
    public HashMap convertToHashMapFromString(String str) {
        HashMap<String, String> map = new HashMap<String, String>();

        String[] nameValuePairs = str.split("&");
        for (String nameValuePair : nameValuePairs) {
            String[] nameValue = nameValuePair.split("=");
            try {
                map.put(URLDecoder.decode(nameValue[0], "UTF-8"), nameValue.length > 1 ? URLDecoder.decode(
                        nameValue[1], "UTF-8") : "");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("This method requires UTF-8 encoding support", e);
            }
        }

        return map;
    }

    /**
     * 文字列からArrayListを取得する
     *
     * @param str String 文字列
     * @return ArrayList data
     * */
    public ArrayList convertToArrayListFromString(String str) {
        ArrayList<String> arr = new ArrayList<>();
        if (str == null || str.equals("")) return arr;
        String[] strArr = str.split(", ");
        for (String item : strArr) {
            arr.add(item);
        }
        return arr;
    }

}
