package com.buy;

import com.buy.views.LoaingPageView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class GuideActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        getWindow( ).setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView( new LoaingPageView( this ) {
            public void finish() {
                gotoMain( );
            }
        }.getView( ) );
    }

    private void gotoMain() {
        Intent intent = new Intent( );
        intent.setClass( this, MainActivity.class );
        startActivity( intent );
        finish( );

    }
    
}
