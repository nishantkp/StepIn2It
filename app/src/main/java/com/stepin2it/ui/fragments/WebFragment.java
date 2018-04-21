package com.stepin2it.ui.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.stepin2it.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebFragment extends Fragment {
    private static final String KEY_URL_BUNDLE = "key_url_bundle";
    private String mUrl;
    @BindView(R.id.wbv_product_url)
    WebView wbvProductUrl;

    public WebFragment() {
        // Required empty public constructor
    }

    public static WebFragment newInstance(String url) {
        WebFragment webFragment = new WebFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_URL_BUNDLE, url);
        webFragment.setArguments(bundle);
        return webFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrl = getArguments().getString(KEY_URL_BUNDLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_web, container, false);
        ButterKnife.bind(this, view);
        initWebView();
        return view;
    }

    // Load the web page into web-tab
    @SuppressLint("ClickableViewAccessibility")
    private void initWebView() {
        WebSettings webSettings = wbvProductUrl.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wbvProductUrl.setWebChromeClient(new MyWebViewClient());
        wbvProductUrl.setWebViewClient(new WebViewClient());
        wbvProductUrl.loadUrl(mUrl);
        wbvProductUrl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    private class MyWebViewClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            Timber.i("Url : %s", view.getUrl());
        }
    }
}

