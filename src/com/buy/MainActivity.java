package com.buy;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.brunjoy.taose.R;
import com.buy.bases.BaseViewPagerActivity;
import com.buy.data.CheckUpdataManager;
import com.buy.data.TaobaokeDataManager;
import com.buy.dialogs.ExitDialog;
import com.buy.holder.Categroys;
import com.buy.holder.Holder;
import com.buy.settings.SuggestActivity;
import com.buy.util.Util;
import com.buy.views.CategoryView;
import com.buy.views.EditLikedView;
import com.buy.views.EnjoyView;
import com.buy.views.RecommendView;
import com.buy.views.SettingsView;
import com.umeng.analytics.MobclickAgent;

/**
 * 为本程序的主页面，显示5个子页面
 * 
 * @author 邓海柱<br>
 *         E-mail:denghaizhu@brunjoy.com
 */
public class MainActivity extends BaseViewPagerActivity implements OnItemClickListener {
    String TAG = "MainActivity";
    private RecommendView mRecommendView;
    private CategoryView mCategoryView;
    // LikedView mLikedView;
    private EditLikedView mEditLikedView;
    private SettingsView mSettingsView;
    private EnjoyView mEnjoyView;

    // private TiebaListView mTiebaListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TaobaokeDataManager.getInstance( this ).cancelAllTask( );
        super.onCreate( savedInstanceState );

        initBaseLayout( );
        int index = getIntent( ).getIntExtra( "index", 0 );
        switchView( index );
        onRightBtnRota( );
        MobclickAgent.onError( this );
        // HttpServer.getInstance( ).start( );
    }

    // @Override
    // public void onAttachedToWindow() {
    // MyLog.d( TAG, "===============" );
    // MyLog.d( TAG, "home keydown=onAttachedToWindow" );
    // getWindow( ).setType( WindowManager.LayoutParams.TYPE_KEYGUARD );
    // super.onAttachedToWindow( );
    // }
    private void initBaseLayout() {
        // mTiebaListView = new TiebaListView( this );
        mRecommendView = new RecommendView( this ) {
            @Override
            public void onRefresh() {
                super.onRefresh( );
                if (getCurrentItemId( ) == 0)
                    onRightBtnRota( );
            }

            @Override
            public void stopRefresh() {
                if (getCurrentItemId( ) == 0)
                    stopRinghtBtn( );
            }
        };
        mCategoryView = new CategoryView( this ) {
            @Override
            public void onRefresh() {
                super.onRefresh( );
                if (getCurrentItemId( ) == 1)
                    onRightBtnRota( );
            }

            @Override
            public void stopRefresh() {
                if (getCurrentItemId( ) == 1)
                    stopRinghtBtn( );
            }
        };
        // mLikedView = new LikedView( this );
        mEditLikedView = new EditLikedView( this ) {
            @Override
            public void onRefresh() {
                super.onRefresh( );
                if (getCurrentItemId( ) == 3)
                    onRightBtnRota( );
            }

            @Override
            public void stopRefresh() {
                if (getCurrentItemId( ) == 3)
                    stopRinghtBtn( );
            }
        };

        mSettingsView = new SettingsView( this ) {
            @Override
            public void onRefresh() {
                super.onRefresh( );
                if (getCurrentItemId( ) == 4)
                    onRightBtnRota( );
            }

            @Override
            public void stopRefresh() {
                if (getCurrentItemId( ) == 4)
                    stopRinghtBtn( );
            }
        };
        mEnjoyView = new EnjoyView( this ) {
            @Override
            public void onRefresh() {
                super.onRefresh( );
                if (getCurrentItemId( ) == 2)
                    onRightBtnRota( );
            }

            @Override
            public void stopRefresh() {
                if (getCurrentItemId( ) == 2)
                    stopRinghtBtn( );
            }
        };

    }

    /**
     * 退出确认dialog
     */
    private ExitDialog mExitDialog;

    /**
     * 最顶层捕获按键
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction( ) == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode( )) {
            case KeyEvent.KEYCODE_MENU:

                // MyLog.e( TAG, "DeviceToken==" + DeviceToken.getToken( this ) );
                break;
            case KeyEvent.KEYCODE_BACK:
                if (getCurrentItemId( ) != 0) {
                    setCurrentItem( 0 );
                    return true;
                }
                if (mExitDialog == null) {
                    mExitDialog = new ExitDialog( this ) {
                        @Override
                        public void onExit() {
                            finish( );

                        }

                        @Override
                        public void onCancel() {

                        }
                    };
                }
                mExitDialog.show( );
                // new AlertDialog.Builder( MainActivity.this ).setIcon( R.drawable.ic_launcher ).setTitle( R.string.prompt ).setMessage( R.string.areYouQuit )
                // .setPositiveButton( R.string.ok, new DialogInterface.OnClickListener( ) {
                //
                // @Override
                // public void onClick(DialogInterface dialog, int which) {
                // finish( );
                //
                // }
                // } ).setNegativeButton( R.string.cancel, null ).create( ).show( );
                return true;
            default:
                break;

            }
        }
        return super.dispatchKeyEvent( event );
    }

    /**
     * 回收资源
     */
    @Override
    protected void onDestroy() {
        TaobaokeDataManager.getInstance( this ).clear( );
        mRecommendView.onDestroy( );
        mCategoryView.onDestroy( );
        mEditLikedView.onDestroy( );
        mSettingsView.onDestroy( );
        mEnjoyView.onDestroy( );

        mRecommendView = null;
        mCategoryView = null;
        mEditLikedView = null;
        mSettingsView = null;
        mEnjoyView = null;
        deleteDatabase( "webviewCache.db" );
        // DianJinPlatform.destroy( );// 点金广告
        // HttpServer.getInstance( ).stop( );//关闭本地HttpServer
        super.onDestroy( );
    }

    /**
     * 当前的子页面选择情况
     */
    public void onPageSelected(int position) {
        setCurrentTitleString( getResources( ).getStringArray( R.array.btnLeftText )[position], getResources( ).getStringArray( R.array.centerText )[position], getResources( )
                .getStringArray( R.array.btnRightText )[position] );
        startLeftAnimation( );
        switch (position) {
        case 0:
            mRecommendView.onStart( );
            mCategoryView.onPause( );
            mEnjoyView.onPause( );
            mEditLikedView.onPause( );
            mSettingsView.onPause( );
            setNaviationBg( R.drawable.meinv, R.drawable.taoseshouye, R.drawable.selector_loading );
            MobclickAgent.onEvent( this, "start_recom" );
            break;
        case 1:
            mRecommendView.onPause( );
            mCategoryView.onStart( );
            mEnjoyView.onPause( );
            mEditLikedView.onPause( );
            mSettingsView.onPause( );
            setNaviationBg( R.drawable.meinv, -1, -1 );
            MobclickAgent.onEvent( this, "start_category" );
            break;
        case 2:
            mRecommendView.onPause( );
            mCategoryView.onPause( );
            mEnjoyView.onStart( );
            mEditLikedView.onPause( );
            mSettingsView.onPause( );
            setNaviationBg( R.drawable.meinv, -1, R.drawable.selector_loading );
            MobclickAgent.onEvent( this, "start_enjoy" );
            break;
        case 3:
            mRecommendView.onPause( );
            mEditLikedView.onResume( );
            mCategoryView.onPause( );
            mEnjoyView.onPause( );
            mSettingsView.onPause( );
            setNaviationBg( R.drawable.meinv, -1, R.drawable.selector_nav_like );
            MobclickAgent.onEvent( this, "start_stroe" );
            break;
        case 4:

            mRecommendView.onPause( );
            mCategoryView.onPause( );
            mEnjoyView.onPause( );
            mEditLikedView.onPause( );
            mSettingsView.onStart( );
            setNaviationBg( R.drawable.meinv, -1, -1 );
            MobclickAgent.onEvent( this, "start_setting" );
            break;
        default:
            break;
        }
    }

    /**
     * 得到子页面 update 有viewpage修改而成，故没没有改名
     */
    @Override
    public View onCreateDefView(int index/* , LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState */) {
        // stopRinghtBtn( );
        switch (index) {
        case 0:
            return mRecommendView.getView( );
        case 1:
            return mCategoryView.getView( );
        case 2:
            return mEnjoyView.getView( );
        case 3:
            stopRinghtBtn( );
            Util.addStoreToServer( this );
            return mEditLikedView.getView( );
        case 4:
            // return mTiebaListView.getView( );
            return mSettingsView.getView( );
        default:
            break;
        }
        return null;
    }

    /**
     * 动态获取当前的子页面数目
     */
    public int getCount() {

        return getResources( ).getStringArray( R.array.centerText ).length;
    }

    /**
     * 点击导航左边
     */
    public void onClickLeftButton(int position) {
        switch (position) {
        case 0:

            break;
        case 1:

        default:
            break;
        }
    }

    /**
     * 点击导航栏右键
     */
    public void onClickRightButton(int position) {
        stopRinghtBtn( );
        // MyLog.i( TAG, "onClickRightButton" );
        switch (position) {
        case 0:

            mRecommendView.onRefresh( );
            break;
        case 1:

            mCategoryView.onRefresh( );
            break;
        case 2:

            mEnjoyView.onRefresh( );
            break;
        case 3:
            // Intent intent = new Intent( );
            // intent.setClass( this, EditLikedActivity.class );
            // startActivity( intent );
            if (!mEditLikedView.isEdit( )) {
                setNaviationBg( -1, -1, R.drawable.selector_nav_like );
            } else {
                setNaviationBg( -1, -1, R.drawable.xihuan2 );
            }
            mEditLikedView.setAllEdit( );

            break;

        default:
            break;
        }
    }

    /**
     * 点击listview 处理情况
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Object obj = arg0.getItemAtPosition( arg2 );
        if (obj instanceof Holder) {
            Holder item = (Holder) obj;
            Util.gotoBuyNow( this, item.getClick_url( ), item.getNum_iid( ), item.getTitle( ) );
        } else if (obj instanceof Categroys) {
            Categroys item = (Categroys) obj;
            Toast.makeText( this, "" + item.getName( ) + " ," + item.getExplain( ), Toast.LENGTH_LONG ).show( );
        }
    }

    public void onClickLeftButton() {

    }

    public void onClickRightButton() {

    }

    public void setCurrentTitleString() {

    }

    @Override
    protected void onPause() {
        mRecommendView.onPause( );
        super.onPause( );
        MobclickAgent.onPause( this );
    }

    @Override
    protected void onResume() {
        mRecommendView.onResume( );
        mEditLikedView.onResume( );
        if (getCurrentItemId( ) == 3) {
            setNaviationBg( -1, -1, R.drawable.selector_nav_like );
        }

        super.onResume( );
        MobclickAgent.onResume( this );
    }

    public void onRefresh() {
        switch (getCurrentItemId( )) {
        case 0:
            mRecommendView.onRefresh( );
            break;
        case 1:
            mCategoryView.onRefresh( );
            break;
        case 2:
            mEnjoyView.onRefresh( );
            break;
        case 3:
            mEditLikedView.onRefresh( );
            break;

        default:
            break;
        }

    }

    /**
     * 创建menu
     */
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
            switchView( 4 );
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

}
