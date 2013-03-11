package com.buy.itemDetailsActivity;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.buy.bases.LoadingActivity;
import com.buy.data.CheckUpdataManager;
import com.buy.dialogs.LoadingDialog;
import com.buy.settings.SuggestActivity;
import com.buy.util.MyToast;
import com.buy.util.Util;
import com.brunjoy.taose.R;

public class ShowDescWebActivity extends LoadingActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        webView = new WebView( this );
        webView.getSettings( ).setLayoutAlgorithm( LayoutAlgorithm.SINGLE_COLUMN );
        mLoadingDialog = new LoadingDialog( this );
        File file = getFileStreamPath( "temp_web" );
       
        if (!file.exists( )) {
            setLoadingTxt( "无该宝贝相关信息" );
            MyToast.showMsgLong( this, R.string.prompt_noDetail_desc );
            return;
        } else {
            setContentView( webView );
            hideLoadingView( );
            webView.loadUrl( "file://" + file.getPath( ) );
            webView.setWebViewClient( new WebViewClient( ) {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return true;// 禁止跳转
                }

                @Override
                public void onPageFinished(WebView view, String url) {

                    super.onPageFinished( view, url );
                }

            } );
            webView.setWebChromeClient( new WebChromeClient( ) {

                @Override
                public void onProgressChanged(WebView view, int newProgress) {

                    if (newProgress > 45) {
                        if (mLoadingDialog.isShowing( )) {
                            mLoadingDialog.dismiss( );
                        }
                    }

                }

            } );

        }
        showLoadingDialog( );

    }

    @Override
    protected void onDestroy() {

        super.onDestroy( );
        File file = getFileStreamPath( "temp_web" );
        if (file.exists( ))
            file.delete( );
    }

    public void onRefresh() {
        webView.reload( );
        showLoadingDialog( );
    }

    private LoadingDialog mLoadingDialog;

    public void showLoadingDialog() {
        mLoadingDialog.show( );
    }

    public void dismissLoadingDialog() {
        mLoadingDialog.dismiss( );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add( 0, 1, 0, R.string.menu_settings );
        item.setIcon( R.drawable.menu_szan );
        item = menu.add( 0, 2, 0, R.string.menu_suggest );
        Intent intent = new Intent( );
        intent.setClass( this, SuggestActivity.class );
        item.setIntent( intent );
        item.setIcon( R.drawable.menu_yjfk );

        item = menu.add( 0, 3, 0, R.string.menu_refresh );
        item.setIcon( R.drawable.menu_sxan );

        item = menu.add( 1, 4, 0, R.string.menu_chenckupdata );
        item.setIcon( R.drawable.menu_jcgx );

        item = menu.add( 2, 5, 0, R.string.menu_usehelp );
        item.setIcon( R.drawable.menu_gmbz );

        item = menu.add( 3, 6, 0, R.string.menu_aboutme );
        item.setIcon( R.drawable.menu_gywm );

        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId( )) {
        case 1:
            Util.gotoSettings( this );
            break;
        case 2:
            Util.gotoSuggest( this );
            break;
        case 3:
            onRefresh( );
            break;
        case 4:
            new CheckUpdataManager( this ).checkAndUpdata( true );
            break;
        case 5:
            Util.gotoBuyHelp( this );
            break;
        case 6:
            Util.gotoAboutMe( this );
            break;

        default:
            break;
        }
        return super.onMenuItemSelected( featureId, item );
    }

    @Override
    public void PCGou() {
        Util.goToScanQR( this, getIntent( ).getStringExtra( "numId" ) );
        
    }

}
