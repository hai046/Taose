package com.buy.settings;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.buy.bases.BaseActivity;
import com.buy.data.TaobaoDataCallBack;
import com.buy.data.TaobaokeDataManager;
import com.brunjoy.taose.R;
import com.umeng.analytics.MobclickAgent;

public class SuggestActivity extends BaseActivity {

    private Button btnSend;
    private TextView tvMobileInfo;
    private EditText etContact, etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        View view = getLayoutInflater( ).inflate( R.layout.setting_suggest, null );
        setContentView( view, new ViewGroup.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT ) );
        btnSend = (Button) findViewById( R.id.suggest_send_btn );
        btnSend.setOnClickListener( mOnClickListener );
        tvMobileInfo = (TextView) findViewById( R.id.suggest_mobile_tv );
        etContact = (EditText) findViewById( R.id.suggest_contact_et );
        etContent = (EditText) findViewById( R.id.suggest_content_et );
        initView( );
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

    private void initView() {

        tvMobileInfo.setText( Build.MANUFACTURER + " " + Build.MODEL + ",\tandroid:" + Build.VERSION.RELEASE + ",\t" + getWindowManager( ).getDefaultDisplay( ).getWidth( ) + "*"
                + getWindowManager( ).getDefaultDisplay( ).getHeight( ) );

    }

    private OnClickListener mOnClickListener = new OnClickListener( ) {

        @Override
        public void onClick(View v) {
            if (etContent.getText( ).toString( ).length( ) >= 6) {
                TaobaokeDataManager.getInstance( SuggestActivity.this ).commitContent( etContent.getText( ).toString( ), tvMobileInfo.getText( ).toString( ),
                        etContact.getText( ).toString( ), new TaobaoDataCallBack<String>( ) {

                            public void success(String T) {
                                Toast.makeText( SuggestActivity.this, T, 1 ).show( );
                                finish( );
                            }

                            @Override
                            public void failure(String msg) {
                                Toast.makeText( SuggestActivity.this, msg, 1 ).show( );
                            }
                        } );
            } else {
                Toast.makeText( SuggestActivity.this, "亲，建议不能少于6为字符哦！", 1 ).show( );
            }

        }
    };

    @Override
    public boolean onClickLeftButton() {
        return false;
    }

    @Override
    public void onClickRightButton() {

    }

    @Override
    public void setCurrentTitleString() {
        setTitle( "意见反馈" );
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub

    }

}
