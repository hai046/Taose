package com.buy.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.brunjoy.taose.R;

public class InputPassWordDialog extends Dialog implements android.view.View.OnClickListener {

    private InputPassWordDialog(Context context, int theme) {
        super( context, theme );

    }

    private ImageButton btnClose;
    private View Line;
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btnDel;
    private TextView tvTitel;
    private TextView tvPwd1, tvPwd2, tvPwd3, tvPwd4;

    @Override
    public void setTitle(CharSequence title) {
        tvTitel.setText( title );
    }

    @Override
    public void setTitle(int titleId) {
        tvTitel.setText( titleId );
    }

    public void setColseEnable(boolean enable) {
        if (!enable) {
            btnClose.setVisibility( View.GONE );
            Line.setVisibility( View.GONE );
        } else {
            btnClose.setVisibility( View.VISIBLE );
            Line.setVisibility( View.VISIBLE );
        }
    }

    public InputPassWordDialog(Context context) {
        this( context, R.style.inputPWDTheme );
//        int height = getWindow( ).getWindowManager( ).getDefaultDisplay( ).getHeight( );
//        int width = getWindow( ).getWindowManager( ).getDefaultDisplay( ).getWidth( );
//        width = Math.min( height, width );
//        if (width < 320)
//            width = ViewGroup.LayoutParams.WRAP_CONTENT;
        setContentView( getLayoutInflater( ).inflate( R.layout.setting_setpwd_layout, null )/*, new ViewGroup.LayoutParams( width, ViewGroup.LayoutParams.WRAP_CONTENT )*/ );
        tvTitel = (TextView) findViewById( R.id.setPwd_title );
        Line = findViewById( R.id.setPwd_line );
        btnClose = (ImageButton) findViewById( R.id.setPwd_close );
        tvPwd1 = (TextView) findViewById( R.id.setPwd_tv1 );
        tvPwd2 = (TextView) findViewById( R.id.setPwd_tv2 );
        tvPwd3 = (TextView) findViewById( R.id.setPwd_tv3 );
        tvPwd4 = (TextView) findViewById( R.id.setPwd_tv4 );
        btn0 = (Button) findViewById( R.id.setPwd_btn0 );
        btn1 = (Button) findViewById( R.id.setPwd_btn1 );
        btn2 = (Button) findViewById( R.id.setPwd_btn2 );
        btn3 = (Button) findViewById( R.id.setPwd_btn3 );
        btn4 = (Button) findViewById( R.id.setPwd_btn4 );
        btn5 = (Button) findViewById( R.id.setPwd_btn5 );
        btn6 = (Button) findViewById( R.id.setPwd_btn6 );
        btn7 = (Button) findViewById( R.id.setPwd_btn7 );
        btn8 = (Button) findViewById( R.id.setPwd_btn8 );
        btn9 = (Button) findViewById( R.id.setPwd_btn9 );

        btnDel = (Button) findViewById( R.id.setPwd_btnDelete );

        btnClose.setOnClickListener( this );
        btn0.setOnClickListener( this );
        btn1.setOnClickListener( this );
        btn2.setOnClickListener( this );
        btn3.setOnClickListener( this );
        btn4.setOnClickListener( this );
        btn5.setOnClickListener( this );
        btn6.setOnClickListener( this );
        btn7.setOnClickListener( this );
        btn8.setOnClickListener( this );
        btn9.setOnClickListener( this );
        btnDel.setOnClickListener( this );

        setOnDismissListener( new OnDismissListener( ) {

            @Override
            public void onDismiss(DialogInterface dialog) {
                sb.delete( 0, sb.length( ) );
                // sb = new StringBuffer( );
                printText( );
                if (mCallBack != null && isCancle)
                    mCallBack.cancle( );

            }
        } );
    }

    private StringBuffer sb = new StringBuffer( );

    public String getResult() {
        return sb.toString( );
    }

    private void printText() {

        switch (sb.length( )) {
        case 0:
            tvPwd1.setText( null );
            tvPwd2.setText( null );
            tvPwd3.setText( null );
            tvPwd4.setText( null );
            break;
        case 1:
            tvPwd1.setText( "*" );
            tvPwd2.setText( null );
            tvPwd3.setText( null );
            tvPwd4.setText( null );
            break;
        case 2:
            tvPwd1.setText( "*" );
            tvPwd2.setText( "*" );
            tvPwd3.setText( null );
            tvPwd4.setText( null );
            break;
        case 3:
            tvPwd1.setText( "*" );
            tvPwd2.setText( "*" );
            tvPwd3.setText( "*" );
            tvPwd4.setText( null );
            break;
        case 4:
            tvPwd1.setText( "*" );
            tvPwd2.setText( "*" );
            tvPwd3.setText( "*" );
            tvPwd4.setText( "*" );
            break;

        default:
            break;
        }
    }

    public boolean isCancle = false;

    @Override
    public void onClick(View v) {

        switch (v.getId( )) {
        case R.id.setPwd_btnDelete:
            if (sb.length( ) > 0) {
                sb.deleteCharAt( sb.length( ) - 1 );
            }

            break;
        // case R.id.setPwd_btnOk:
        //
        // break;
        case R.id.setPwd_close:
            isCancle = true;
            dismiss( );
            sb.delete( 0, sb.length( ) );
            break;

        default:
            if (v instanceof Button) {
                Button btn = (Button) v;
                sb.append( btn.getText( ) );
            }
            break;
        }
        printText( );
        if (sb.length( ) == 4) {
            isCancle = false;
            dismiss( );
            if (mCallBack != null) {
                mCallBack.bakeResult( getResult( ) );
            }

            sb.delete( 0, sb.length( ) );
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_BACK:
            isCancle = true;
            dismiss( );
            sb.delete( 0, sb.length( ) );
            break;

        default:
            break;
        }
        return super.onKeyDown( keyCode, event );
    }

    private CallBack mCallBack;

    public void setOnCallBack(CallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    public interface CallBack {
        void bakeResult(String result);

        void cancle();

    }

}
