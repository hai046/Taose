package com.buy.util;

import android.app.Dialog;
import android.content.Context;

import com.brunjoy.taose.R;

public class MyNoTitleDialog extends Dialog {

    public MyNoTitleDialog(Context context) {
        super( context,R.style.categoryTheme );
        
    }
    public MyNoTitleDialog(Context context,int Theme) {
        super( context,Theme);
        
    }

}
