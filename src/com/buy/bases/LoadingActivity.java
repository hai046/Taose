package com.buy.bases;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buy.ItemDetailActivity;
import com.buy.stores.MySqlLiteDataBase;
import com.buy.util.MyToast;
import com.buy.util.Util;
import com.brunjoy.taose.R;
import com.umeng.analytics.MobclickAgent;

public abstract class LoadingActivity extends Activity implements OnClickListener {
    private ImageButton addStronBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.layout_loading );
        addStronBtn = (ImageButton) findViewById( R.id.popup_detail_addstore );
        addStronBtn.setOnClickListener( this );
        findViewById( R.id.popup_detail_buyNow ).setOnClickListener( this );
        findViewById( R.id.popup_detail_pcGou ).setOnClickListener( this );
    }

    @Override
    protected void onResume() {
        checkAddStroeStatus( );
        super.onResume( );
    }

    @Override
    public void setContentView(View view) {

        RelativeLayout mRelativeLayout = (RelativeLayout) findViewById( R.id.main_layout_loaing );
        mRelativeLayout.removeAllViews( );
        mRelativeLayout.addView( view );
    }

    // start loading
    public void setLoadingTxt(String text) {
        TextView loadingTxt = (TextView) findViewById( R.id.loadingText );
        loadingTxt.setText( text );
        findViewById( R.id.loading_layout ).setVisibility( View.VISIBLE );
    }

    // public void setLoadingTxt(int text) {
    // TextView loadingTxt = (TextView) findViewById( R.id.loadingText );
    // loadingTxt.setText( text );
    //
    // }

    public void hideLoadingView() {
        findViewById( R.id.loading_layout ).setVisibility( View.GONE );
    }

    public boolean isStore() {
        if (ItemDetailActivity.currentHolder == null) {
            return false;
        }
        return MySqlLiteDataBase.getInstance( this ).getItem( ItemDetailActivity.currentHolder.getNum_iid( ) ) != null;
    }

    private void checkAddStroeStatus() {
        if (isStore( )) {
            addStronBtn.setImageResource( R.drawable.selector_itemdeail_unaddstore );

        } else {
            addStronBtn.setImageResource( R.drawable.selector_itemdeail_addstore );
        }
    }

    public void collectOrNot() {
        if (ItemDetailActivity.currentHolder == null) {
            return;
        }
        if (isStore( )) {
            addStronBtn.setImageResource( R.drawable.selector_itemdeail_addstore );
            if (!MySqlLiteDataBase.getInstance( this ).open( )) {
                MyToast.showMsgShort( this, "收藏失败" );
                return;
            }
            MySqlLiteDataBase.getInstance( this ).deleteDataById( ItemDetailActivity.currentHolder.getNum_iid( ) );
            MySqlLiteDataBase.getInstance( this ).close( );
            Toast.makeText( this, "取消收藏成功", Toast.LENGTH_LONG ).show( );
            return;
        }

        if (MySqlLiteDataBase.getInstance( this ).insertLiked( ItemDetailActivity.currentHolder.getNum_iid( ), ItemDetailActivity.currentHolder )) {
            Toast.makeText( this, "收藏成功", Toast.LENGTH_LONG ).show( );
            addStronBtn.setImageResource( R.drawable.selector_itemdeail_unaddstore );
            MobclickAgent.onEvent( this, "addstore", ItemDetailActivity.currentHolder.getNum_iid( ) );
        } else {
            // MyLog.e( "Error ", "添加收藏失败" );
        }
    }

    public abstract void PCGou();

    @Override
    public void onClick(View v) {
        if (ItemDetailActivity.currentHolder == null) {
            return;
        }
        switch (v.getId( )) {
        case R.id.popup_detail_pcGou:
            PCGou( );

            break;
        case R.id.popup_detail_addstore:
            collectOrNot( );
            break;
        case R.id.popup_detail_buyNow:
            Util.gotoBuyNow( this, ItemDetailActivity.currentHolder.getClick_url( ), ItemDetailActivity.currentHolder.getNum_iid( ),ItemDetailActivity.currentHolder.getTitle( ) );
        }
    }
}
