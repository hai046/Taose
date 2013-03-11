package com.buy.bases;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class IActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
    }

    // String TAG = "IActivity";

    // @Override
    // public View onCreateView(String name, Context context, AttributeSet attrs) {
    // int resId = -1;
    // // MyLog.e( "onCreateView", attrs );
    // for (int i = attrs.getAttributeCount( )-1; i >= 0; i--) {
    // if ("background".equalsIgnoreCase( attrs.getAttributeName( i ) )) {
    // resId = attrs.getAttributeResourceValue( i, 0 );
    //
    // }
    // Log.e( TAG, "getAttributeName=" + attrs.getAttributeName( i ) + "  getAttributeValue=" + attrs.getAttributeValue( i ) );
    // }
    // View view = super.onCreateView( name, context, attrs );
    // if (resId > 0) {
    // SkinManger.getInstance( this ).setBackgroundDrable( view, resId );
    // }
    // return view;
    // }

}
