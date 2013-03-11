package com.buy.settings;

import android.os.Bundle;
import android.view.WindowManager;

import com.buy.bases.IActivity;
import com.buy.views.BuyHelpPageView;
import com.brunjoy.taose.R;
import com.umeng.analytics.MobclickAgent;

public class HelpYouActivity extends IActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        
        getWindow( ).setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_BLUR_BEHIND );

        setTitle( R.string.menu_usehelp );
        setContentView( new BuyHelpPageView( this ) {
            public void finish() {
                HelpYouActivity.this.finish( );
            }
        }.getView( ) );
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

}
