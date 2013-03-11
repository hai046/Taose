package com.buy.web;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.buy.bases.BaseActivity;
import com.buy.util.MyLog;
import com.brunjoy.taose.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 设置传递 value 的key
 */
public class WebActivity extends BaseActivity {

    /**
     * 设置传递 value 的key
     */
    public static final String PATH = "path";
    String TAG = "WebActivity";
    private WebView mWebView;
    private ProgressBar mProgressBar;

    // private TextView tvUrl;
    private int firstStep = 0;
    private boolean isFrist = true;
    private String firstUrl = "####";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.web_buy );
        mWebView = (WebView) findViewById( R.id.webViewBuy );
        // tvUrl = (TextView) findViewById( R.id.webUrl );
        mProgressBar = (ProgressBar) findViewById( R.id.progressWebLoad );
        String path = getIntent( ).getStringExtra( PATH );
        // MyLog.i( TAG, "get web path=" + path );
        if (path == null) {
            finish( );
            return;
        }
        WebSettings settings = mWebView.getSettings( );
        settings.setPluginsEnabled( true );
        settings.setSupportZoom( true );
        settings.setJavaScriptEnabled( true );
        settings.setBuiltInZoomControls( true );
        // settings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);

        mWebView.setWebViewClient( new WebViewClient( ) {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl( url );
                WebBackForwardList mWebBackForwardList = mWebView.copyBackForwardList( );
                MyLog.e( TAG, " url =" + url );
                MyLog.d( TAG, " getCurrentIndex =" + mWebBackForwardList.getCurrentIndex( ) );
                for (int i = 0; i < mWebBackForwardList.getSize( ); i++) {
                    MyLog.d( TAG, i + "=index, item=" + mWebBackForwardList.getItemAtIndex( i ).getUrl( ) );
                }
                mProgressBar.setVisibility( View.VISIBLE );
                // tvUrl.setText( url );
                MyLog.d( TAG, "shouldOverrideUrlLoading=" + url );
                if (isFrist) {
                    firstStep++;
                }
                MyLog.i( TAG, " firstStep =" + firstStep );
                if (url.contains( "http://a.m.taobao.com" )) {
                    isFrist = false;
                    MyLog.e( TAG, " firstStep =" + firstStep );
                    firstUrl = url;
                }
                return true;
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                MyLog.e( TAG, "failingurl = " + failingUrl );
                if (failingUrl.contains( "#" )) {
                    String[] temp;
                    temp = failingUrl.split( "#" );
                    view.loadUrl( temp[0] ); // load page without internal
                    try {
                        Thread.sleep( 400 );
                    } catch (InterruptedException e) {

                        e.printStackTrace( );
                    }
                    view.goBack( );
                    view.goBack( );
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mProgressBar.setVisibility( View.GONE );

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mProgressBar.setVisibility( View.VISIBLE );

            }

        } );
        mWebView.loadUrl( path );
        mWebView.setWebChromeClient( new WebChromeClient( ) {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress( newProgress );
                if (newProgress > 90) {

                }
                // MyLog.d( TAG, "newProgress=" + newProgress );
            }

            public void onReceivedTitle(WebView view, String title) {
                setTitle( title );
            }

        } );
        MobclickAgent.onEvent( this, "buy_item", path );
    }

    @Override
    protected void onResume() {
        MobclickAgent.onResume( this );
        super.onResume( );
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPause( this );
        super.onPause( );
    }

    @Override
    protected void onDestroy() {
        mWebView.clearHistory( );
        mWebView.clearFormData( );
        mWebView.destroyDrawingCache( );
        mWebView.destroy( );

        super.onDestroy( );
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction( ) == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode( )) {
            case KeyEvent.KEYCODE_BACK:
                if (mWebView.canGoBack( )) {
                    if (!isFrist && mWebView.getUrl( ).contains( firstUrl )) {
                        finish( );
                        return true;
                    }
                    MyLog.d( TAG, "mWebView=" + mWebView.getUrl( ) );
                    mWebView.goBack( );
                    return true;
                }
                break;

            default:
                break;
            }
        }
        return super.dispatchKeyEvent( event );
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown( keyCode, event );
    }

    @Override
    public boolean onClickLeftButton() {
        if (mWebView.canGoBack( )) {
            if (!isFrist && mWebView.getUrl( ).contains( firstUrl )) {
                finish( );
                return true;
            }
            mWebView.goBack( );
            return true;
        } else {
            return false;

        }

    }

    @Override
    public void onClickRightButton() {
        // Util.gotoBuyHelp( this );

    }

    @Override
    public void setCurrentTitleString() {
        // setRightTitleString( getString( R.string.help ) );
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub

    }
}
