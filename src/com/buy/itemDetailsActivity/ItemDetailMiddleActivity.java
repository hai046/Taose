package com.buy.itemDetailsActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.buy.ItemDetailActivity;
import com.buy.stores.Settings;
import com.buy.util.MyLog;
import com.buy.util.Util;
import com.buy.views.ItemDetailView;
import com.brunjoy.taose.R;

public class ItemDetailMiddleActivity extends Activity {
    ItemDetailView mItemDetailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        mItemDetailView = new ItemDetailView( this );
        // mItemDetailView.setClick_url( getIntent( ).getStringExtra( "CLICK_URL" ) );
        // mItemDetailView.setNumid( getIntent( ).getStringExtra( "NUMID" ) );
        // mItemDetailView.getData( );
        setContentView( mItemDetailView.getView( ) );
        mItemDetailView.setHolder( ItemDetailActivity.currentHolder );
        // MyLog.d( "adfasf", "create" );
    }

    @Override
    protected void onDestroy() {
//        MyLog.d( "ItemDetailMiddleActivity", "onDestroy" );
        super.onDestroy( );
    }

    @Override
    protected void onResume() {
//        MyLog.d( "ItemDetailMiddleActivity", "onResume" );
        mItemDetailView.checkAddStroeStatus( );

        super.onResume( );
        if (!Settings.getInSettings( this ).getBoolean( "hadSee_seenItem", false ))// 是否加载PC够引导图
        {
            mItemDetailView.onResume( );
            Settings.getInSettings( this ).putBoolean( "hadSee_seenItem", true );
        }

    }

    @Override
    protected void onPause() {
        // MyLog.d( "adfasf", "onPause" );
        super.onPause( );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add( 0, 1, 0, R.string.menu_buynow );
        item.setIcon( R.drawable.menu_ljgm );
        item = menu.add( 0, 2, 0, mItemDetailView.isStore( ) ? R.string.menu_deleteStroe : R.string.menu_addStroe );
        item.setIcon( R.drawable.menu_tjsc );

        item = menu.add( 0, 3, 0, R.string.menu_refresh );
        item.setIcon( R.drawable.menu_sxan );

        item = menu.add( 1, 4, 0, R.string.menu_buyhelp );
        item.setIcon( R.drawable.menu_gmbz );

        item = menu.add( 2, 5, 0, R.string.menu_disclaimer );
        item.setIcon( R.drawable.menu_gmbz );

        item = menu.add( 3, 6, 0, R.string.menu_aboutme );
        item.setIcon( R.drawable.menu_gywm );

        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId( )) {
        case 1:
            mItemDetailView.gotoBuy( );
            break;
        case 2:
            mItemDetailView.collectOrNot( );
            break;
        case 3:
            mItemDetailView.onRefresh( );
            break;
        case 4:
            Util.gotoBuyHelp( this );
            break;
        case 5:
            Util.gotoDisclaimer( this );
            break;
        case 6:
            Util.gotoAboutMe( this );
            break;

        default:
            break;
        }
        return super.onMenuItemSelected( featureId, item );
    }
}
