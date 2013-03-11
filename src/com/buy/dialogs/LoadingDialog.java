package com.buy.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Message;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.buy.util.MyHandler;
import com.buy.util.MyHandler.HandlerCallBack;
import com.buy.util.Rotate3dAnimation;
import com.brunjoy.taose.R;

public class LoadingDialog extends Dialog {

    private LoadingDialog(Context context, int theme) {
        super( context, theme );
    }

    private ImageView image;
    private TextView tvTitle;
//    private View layoutView;

    public LoadingDialog(Context context) {
        this( context, R.style.loadingDialogTheme );
        setContentView( getLayoutInflater( ).inflate( R.layout.loading_dialog, null ) );
//        layoutView = findViewById( R.id.loadingLayout );
        image = (ImageView) findViewById( R.id.loadingImage );
        tvTitle = (TextView) findViewById( R.id.loadingTitle );
        // show( false );
        
    }

    @Override
    public void setTitle(CharSequence title) {
        tvTitle.setText( title );
    }

    @Override
    public void setTitle(int titleId) {
        tvTitle.setText( titleId );
    }

    Rotate3dAnimation mRotate3dAnimation;

    private void showinit() {

        // MyLog.e( "Loading", "image.getWidth( )=" + (int) (image.getWidth( ) * 0.5) );// 120

        if (mRotate3dAnimation == null) {
            mRotate3dAnimation = new Rotate3dAnimation( 0, 360, (int) (image.getWidth( ) * 0.5), (int) (image.getHeight( ) * 0.5), 0, false );
        } else {
            mRotate3dAnimation.setCenterSize( (int) (image.getWidth( ) * 0.5), (int) (image.getHeight( ) * 0.5) );
        }

        if (mRotate3dAnimation.getCenterX( ) > 0) {
            image.startAnimation( mRotate3dAnimation );
            mRotate3dAnimation.setDuration( 1000 );
            mRotate3dAnimation.setInterpolator( new BounceInterpolator( ) );
            mRotate3dAnimation.setRepeatCount( Integer.MAX_VALUE );
        } else {

            // MyLog.e( "Loading", "----------重新获取数据再次显示" );
            MyHandler.getInstance( ).removeMessages( SHOW );
            MyHandler.getInstance( ).removeListener( mHandlerCallBack );
            MyHandler.getInstance( ).addListener( mHandlerCallBack );
            MyHandler.getInstance( ).sendEmptyMessageDelayed( SHOW, 200 );
            image.clearAnimation( );
        }

    }

    public void show() {
//        WindowManager.LayoutParams lp = getWindow( ).getAttributes( );
//        lp.width = getWindow( ).getWindowManager( ).getDefaultDisplay( ).getWidth( );
//        lp.height = getWindow( ).getWindowManager( ).getDefaultDisplay( ).getHeight( );

        super.show( );
        showinit( );

    }

    private HandlerCallBack mHandlerCallBack = new HandlerCallBack( ) {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case SHOW:
                showinit( );
                break;

            default:
                break;
            }

        }

    };
    private static final int SHOW = 1022;

    @Override
    public void dismiss() {
        MyHandler.getInstance( ).removeMessages( SHOW );
        MyHandler.getInstance( ).removeListener( mHandlerCallBack );
        super.dismiss( );
    }

}
