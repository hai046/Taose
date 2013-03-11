package com.buy.views;

import android.content.Context;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MyWebView extends BaseView {

    public MyWebView(Context mContext, String url) {
        super( mContext );
        WebView mWebView = new WebView( mContext );
      
       
        setContentView( mWebView );
        WebSettings mWebSettings = mWebView.getSettings( );
        mWebSettings.setJavaScriptEnabled( true );
        mWebSettings.setBuiltInZoomControls( true );
        mWebView.loadUrl( url );

    }

}
