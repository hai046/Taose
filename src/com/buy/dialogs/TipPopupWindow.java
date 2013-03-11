package com.buy.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

import com.buy.util.MyLog;
import com.brunjoy.taose.R;

public class TipPopupWindow extends Dialog {

    private ImageView image;

    public TipPopupWindow(Context context, int resId) {
        super( context, R.style.loadingDialogTheme );
        // getWindow( ).setGravity( Gravity.LEFT | Gravity.TOP );
        image = new ImageView( context );
        image.setImageResource( resId );
        setContentView( image );
        setCanceledOnTouchOutside( true );

    }

    public void setImage(int resId) {
        image.setImageResource( resId );
    }

    //
    // private HandlerCallBack mHandlerCallBack = new HandlerCallBack( ) {
    //
    // @Override
    // public void handleMessage(Message msg) {
    // switch (msg.what) {
    // case DISSMISSS:
    // dismiss( );
    // break;
    // case SHOW:
    //
    // MyLog.e( "pop", "SHOWSHOWSHOWSHOWSHOW  showAsDropDown   x=" + msg.arg1 + "   y=" + msg.arg2 );
    //
    // // TipPopupWindow.super.showAsDropDown( ((View) msg.obj), msg.arg1, msg.arg2 );
    // default:
    // break;
    // }
    //
    // }
    //
    // };
    // private static final int DISSMISSS = 30220;
    // private static final int SHOW = 30221;

    // @Override
    // public void dismiss() {
    // MyHandler.getInstance( ).removeMessages( DISSMISSS );
    // MyHandler.getInstance( ).removeListener( mHandlerCallBack );
    // super.dismiss( );
    // }
    /**
     * ==========================================<BR>
     * 功能：显示的位置 <BR>
     * 时间：2013-1-31 下午1:11:25 <BR>
     * ========================================== <BR>
     * 参数：
     * 
     * @param x
     *            显示的x坐标
     * @param y
     *            显示的y坐标
     * @param screenHeight
     *            屏幕的高度
     */
    public void show(int x, int y, int screenHeight) {
        LayoutParams params = getWindow( ).getAttributes( );

//        MyLog.e( "pop", "show  x=" + x + "   y=" + y + " screenHeight=" + screenHeight );

        y = y - (int) ((246f * (screenHeight / 800f) + screenHeight) * 0.5);

        if (screenHeight > 0) {
            params.y = y - 30;
        }
        MyLog.e( "pop", "show  x=" + x + "   y=" + y + "  params.height=" + image.getHeight( ) );
        MyLog.e( "pop", "show  x=" + x + "   y=" + y );
        super.show( );
    }

}
