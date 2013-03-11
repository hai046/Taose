package com.buy.settings;

import android.os.Bundle;

import com.buy.bases.BaseActivity;
import com.buy.views.MyWebView;
import com.buy.webUtil.WebDataManager;
import com.brunjoy.taose.R;
import com.umeng.analytics.MobclickAgent;

public class DisclaimerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setTitle( R.string.menu_disclaimer );
        setContentView( new MyWebView( this, WebDataManager.ROOTPATH + "static/disclaimer.html" ).getView( ) );

    }

    @Override
    public boolean onClickLeftButton() {

        return false;
    }

    @Override
    public void onClickRightButton() {

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
    public void setCurrentTitleString() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub

    }

}
