package com.buy.itemDetailsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.buy.bases.LoadingActivity;
import com.buy.data.CheckUpdataManager;
import com.buy.dialogs.LoadingDialog;
import com.buy.settings.SuggestActivity;
import com.buy.util.Util;
import com.buy.views.ItemCommentView;
import com.brunjoy.taose.R;

public class ItemCommentActivity extends LoadingActivity {
    ItemCommentView mItemCommentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );

        mItemCommentView = new ItemCommentView( this ) {
            @Override
            public void onRefresh() {

                super.onRefresh( );
            }

            @Override
            public void stopRefresh() {
                dismissLoadingDialog( );
                super.onStop( );
            }
            @Override
            public void onFailure(String msg) {
                setLoadingTxt( msg );
            }

            @Override
            public void onSuccess() {
                hideLoadingView( );
            }
        };
        
        mItemCommentView.getData( getIntent( ).getStringExtra( "numId" ), 0, 30 );
        setContentView( mItemCommentView.getView( ) );
        mLoadingDialog = new LoadingDialog( this );
        showLoadingDialog( );
        setLoadingTxt( "" );
        hideLoadingView( );
    }

    public void onRefresh() {
        mItemCommentView.getNewDate( );
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
