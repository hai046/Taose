package com.buy;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.buy.bases.IActivityGroup;
import com.buy.holder.Holder;
import com.buy.itemDetailsActivity.ItemCommentActivity;
import com.buy.itemDetailsActivity.ItemDetailMiddleActivity;
import com.buy.itemDetailsActivity.ShowDescWebActivity;
import com.brunjoy.taose.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 需要先传入 PATH ,CLICK_UR 缺一不可<BR>
 * 类型 String
 * 
 * @author 邓海柱<br>
 *         E-mail:denghaizhu@brunjoy.com
 */
public class ItemDetailActivity extends IActivityGroup implements OnClickListener {
    final String TAG = "ItemDetailActivity";
    // private MyViewPager myViewPager;
    // public String numid, click_url;
    public static Holder currentHolder;
    // public static String PATH = "path";
    // public static String CLICK_URL = "click_url";

    // private ItemDetailView mItemDetailView;
    // private ItemCommentView mItemCommentView;
    private Button btnLeft, btnCenter, btnRight;
    private RelativeLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.item_detail_layout );
        // numid = getIntent( ).getStringExtra( PATH );
        // click_url = getIntent( ).getStringExtra( CLICK_URL );
        if (currentHolder == null) {
            finish( );
            return;
        }
        btnLeft = (Button) findViewById( R.id.item_detailBtnLeft );
        btnCenter = (Button) findViewById( R.id.item_detailBtnCenter );
        btnRight = (Button) findViewById( R.id.item_detailBtnRight );
        btnLeft.setOnClickListener( this );
        btnCenter.setOnClickListener( this );
        btnRight.setOnClickListener( this );
        mLayout = (RelativeLayout) findViewById( R.id.item_detail_content_layout );
        switchView( 1 );

    }
  
    void switchIntent(int i) {
        Intent intent = new Intent( );
        mLayout.removeAllViews( );
        switch (i) {
        case 0:
            intent.putExtra( "numId", currentHolder.getNum_iid( ) );
            intent.setClass( this, ItemCommentActivity.class );
            MobclickAgent.onEvent( this,"seeItem_comment"  );
            break;
        case 1:
            intent.setClass( this, ItemDetailMiddleActivity.class );
            MobclickAgent.onEvent( this,"seeItem_detail"  );
            break;
        case 2:
            intent.putExtra( "numId", currentHolder.getNum_iid( ) );
            intent.setClass( this, ShowDescWebActivity.class );
            MobclickAgent.onEvent( this,"seeItem_web"  );
            break;
        default:
            break;
        }
        Window subActivity = getLocalActivityManager( ).startActivity( "subActivity" + i, intent );
        mLayout.addView( subActivity.getDecorView( ), LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT );
    }
    
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_MENU:
            getLocalActivityManager( ).getCurrentActivity( ).openOptionsMenu( );
            break;

        default:
            break;
        }
        return super.onKeyUp( keyCode, event );
    }

    // private WebView webView;

    private void switchView(int position) {
        switchIntent( position );
        btnLeft.setBackgroundResource( position == 0 ? R.drawable.bbpj_on : R.drawable.bbpj );
        btnCenter.setBackgroundResource( position == 1 ? R.drawable.jbxx_on : R.drawable.jbxx );
        btnRight.setBackgroundResource( position == 2 ? R.drawable.xxxx_on : R.drawable.xxxx );

    }

    @Override
    public void onClick(View v) {

        // super.onClick( v );
        switch (v.getId( )) {
        case R.id.item_detailBtnLeft:
            switchView( 0 );
            break;
        case R.id.item_detailBtnCenter:
            switchView( 1 );
            break;
        case R.id.item_detailBtnRight:
            switchView( 2 );
            break;

        default:
            break;
        }
    }

    @Override
    public void finish() {

        // for (int i = 0; i < 3; i++) {
        // getLocalActivityManager( ).getActivity( "subActivity" + i ).finish( );
        // }
        // getLocalActivityManager( ).getCurrentActivity( ).finish( );
        //
        super.finish( );
    }

    @Override
    protected void onPause() {
        // mItemDetailView.onPause( );
        super.onPause( );
        // finish( );
        MobclickAgent.onPause( this );
    }

    public void onClickLeftButton() {
        finish( );

        // getLocalActivityManager( ).destroyActivity( getLocalActivityManager( ).getCurrentId( ), true );

        // getLocalActivityManager( )
    }

    public void onClickRightButton() {

    }

    public void setCurrentTitleString() {
        setTitle( R.string.item_detailInfo );
        setTopBtnBackground( R.drawable.selector_back, -1 );
    }

    @Override
    protected void onResume() {
        // mItemDetailView.onResume( );
        super.onResume( );
        MobclickAgent.onResume( this );
    }

    // @Override
    // public boolean onMenuOpened(int featureId, Menu menu) {
    // MenuItem item = menu.add( 0, 1, 0, R.string.menu_buynow );
    //
    // item = menu.add( 0, 2, 0, R.string.menu_addStroe );
    //
    // item = menu.add( 0, 3, 0, R.string.menu_refresh );
    //
    // item = menu.add( 1, 4, 0, R.string.menu_chenckupdata );
    //
    // item = menu.add( 2, 5, 0, R.string.menu_usehelp );
    //
    // item = menu.add( 3, 6, 0, R.string.menu_aboutme );
    // return super.onMenuOpened( featureId, menu );
    // }
    //
    // @Override
    // public boolean onMenuItemSelected(int featureId, MenuItem item) {
    // switch (item.getItemId( )) {
    // case 1:
    //
    // break;
    // case 2:
    //
    // break;
    // case 3:
    //
    // break;
    // case 4:
    //
    // break;
    // case 5:
    //
    // break;
    // case 6:
    //
    // break;
    //
    // default:
    // break;
    // }
    // return super.onMenuItemSelected( featureId, item );
    // }

}
