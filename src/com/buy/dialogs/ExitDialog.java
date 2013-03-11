package com.buy.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.brunjoy.taose.R;

public abstract class ExitDialog extends Dialog implements android.view.View.OnClickListener {

    /**
     * 确定取消id分别为 R.id.exit_OK R.id.exit_cance，
     * 
     * @param context
     * @param mOnClickListener
     */
    public ExitDialog(Context context) {
        super( context, R.style.inputPWDTheme );
        setContentView( getLayoutInflater( ).inflate( R.layout.popup_exit_app, null ) );
        findViewById( R.id.exit_cancel ).setOnClickListener( this );
        findViewById( R.id.exit_OK ).setOnClickListener( this );
    }

    public abstract void onCancel();

    public abstract void onExit();

    @Override
    public void onClick(View v) {
        dismiss( );
        switch (v.getId( )) {
        case R.id.exit_cancel:
            onCancel( );
            break;
        case R.id.exit_OK:
            onExit( );
            break;
        default:
            break;
        }
       
    }

}
