package com.buy.settings;

import android.os.Bundle;

import com.buy.bases.BaseActivity;
import com.buy.views.SettingsView;
import com.brunjoy.taose.R;
import com.umeng.analytics.MobclickAgent;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        setTitle( R.string.menu_settings );
        setContentView( new SettingsView( this ).getView( ) );
    }

    @Override
    public boolean onClickLeftButton() {
        
        return false;
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
    public void onClickRightButton() {
        // TODO Auto-generated method stub

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
