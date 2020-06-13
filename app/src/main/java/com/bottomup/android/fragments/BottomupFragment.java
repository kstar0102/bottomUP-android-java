package com.bottomup.android.fragments;


import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.bottomup.android.R;
import com.bottomup.android.common.Config;

import static com.bottomup.android.common.Common.setUpWebViewDefaults;

/**
 * A simple {@link Fragment} subclass.
 */
public class BottomupFragment extends Fragment {

    private View root_view;
    private WebView webView;
    private RelativeLayout loading;
    private ChangeBottomupWebviewUrlListner listner;

    public BottomupFragment(ChangeBottomupWebviewUrlListner listner) {
        this.listner = listner;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root_view = inflater.inflate(R.layout.fragment_bottomup, container, false);
        loading = root_view.findViewById(R.id.loadingLayout);

        webView = (WebView) root_view.findViewById(R.id.webView2);
        setUpWebViewDefaults(webView);
        webView.setWebChromeClient(new WebChromeClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                request.grant(request.getResources());
            }
            @Override
            public void onProgressChanged(WebView view, int newProgress){
                if(newProgress == 100){
                    loading.setVisibility(View.GONE);
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(final WebView view, final SslErrorHandler handler, final SslError error) {
            }

            @Override
            public boolean shouldOverrideUrlLoading(final WebView _view, final String _url) {
                if (!_url.equals(Config.SERVER_URL+Config.BOOKS_URL)) {
                    if (listner != null) {
                        if (_url.equals(Config.SERVER_URL+Config.LECTURE_URL) ||
                                _url.equals(Config.FACEBOOK_URL) ||
                                _url.equals(Config.SERVER_URL+Config.REPORT_URL) ||
                                _url.equals(Config.SERVER_URL+Config.PROFILE_URL)) {
                            listner.changedWebviewUrl(_url);
                            return true;
                        }
                    }
                }
                return super.shouldOverrideUrlLoading(_view, _url);
            }
        });
        webView.loadUrl(Config.SERVER_URL+Config.BOOKS_URL);

        return root_view;
    }

    public void refreshWebview(){
        webView.loadUrl(Config.SERVER_URL+Config.BOOKS_URL);
        loading.setVisibility(View.VISIBLE);
    }

    public interface ChangeBottomupWebviewUrlListner {
        void changedWebviewUrl(String url);
    }
}
