package com.buy.settings;

import android.os.Bundle;

import com.buy.bases.BaseActivity;
import com.umeng.analytics.MobclickAgent;

public class CreditSecurityActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
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

        setTitle( "信誉保证" );
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        
    }

}
