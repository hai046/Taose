package com.buy.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.buy.bases.MyBaseAdapter;
import com.brunjoy.taose.R;

public abstract class SelectDialog extends Dialog {
    
    private TextView popup_title;
    /**
     * 确定取消id分别为 R.id.exit_OK R.id.exit_cance，
     * 
     * @param context
     * @param mOnClickListener
     */
    public SelectDialog(Context context) {
        super( context, R.style.inputPWDTheme );
        // setContentView( getLayoutInflater( ).inflate( R.layout.popup_select, null ) );
        // findViewById( R.id.exit_cancel ).setOnClickListener( this );
        // findViewById( R.id.exit_OK ).setOnClickListener( this );
        setContentView( R.layout.popup_select );
        popup_title=(TextView) findViewById( R.id.popup_title );
        ListView mListView = (ListView) findViewById( R.id.select_listview );
        findViewById( R.id.select_close ).setOnClickListener( new View.OnClickListener( ) {
            
            @Override
            public void onClick(View v) {
                dismiss( );
            }
        } );
        mListView.setAdapter( adapter );
        mListView.setOnItemClickListener( new OnItemClickListener( ) {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                onSelected( arg2, adapter.getItem( arg2 ) );
                dismiss( );
            }
        } );
    }
    
    @Override
    public void setTitle(CharSequence title) {
        popup_title.setText( title );
    }

    @Override
    public void setTitle(int titleId) {
        popup_title.setText( titleId );
    }

    private MyBaseAdapter<String> adapter = new MyBaseAdapter<String>( ) {

        @Override
        public View bindView(int position, String t, View view) {
            if (view == null) {
                view = getLayoutInflater( ).inflate( R.layout.text_item, null );
            }
            TextView tv = (TextView) view;
            tv.setText( t );
            return tv;
        }
    };

    public void addItem(String item) {
        adapter.add( item );
        adapter.notifyDataSetChanged( );
    }

    public abstract void onSelected(int position, String msg);

}
