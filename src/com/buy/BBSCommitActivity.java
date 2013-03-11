package com.buy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.buy.bases.BaseActivity;
import com.buy.data.TaobaoDataCallBack;
import com.buy.data.TaobaokeDataManager;
import com.buy.stores.Settings;
import com.buy.util.MyToast;
import com.brunjoy.taose.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 需要出入评论articleID<BR>
 * 输入帖子评论
 * 
 * @author 邓海柱<br>
 *         E-mail:denghaizhu@brunjoy.com
 */
public class BBSCommitActivity extends BaseActivity {
    private EditText etUserName, etContent;
    private int articleID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.bbs_commit_layout );
        etUserName = (EditText) findViewById( R.id.teiba_reply_etUserName );
        etContent = (EditText) findViewById( R.id.teiba_reply_etContent );

        // 记录上次用户使用的用户名
        etUserName.setText( Settings.getInSettings( this ).getString( "bbsCommitName", null ) );

        articleID = getIntent( ).getIntExtra( "articleID", -1 );
        if (articleID < 0) {
            MyToast.showMsgShort( this, "传入参数错误" );
            finish( );
        }
        findViewById( R.id.tieba_reply_btn ).setOnClickListener( new OnClickListener( ) {

            @Override
            public void onClick(View v) {
                comment( );
            }
        } );
    }

    private void comment() {
        String content = etContent.getText( ).toString( );
        if (content == null || content.trim().length( ) < 6) {
            MyToast.showMsgLong( this, "输入的评论不应小于6位" );
            return;
        }
        Settings.getInSettings( this ).putString( "bbsCommitName", etUserName.getText( ).toString( ) );

        TaobaokeDataManager.getInstance( this ).doBBSComments( articleID, etUserName.getText( ).toString( ), null, etContent.getText( ).toString( ), 0, 0,
                new TaobaoDataCallBack<String>( ) {
                    public void success(String t) {
                        MyToast.showMsgShort( BBSCommitActivity.this, "评论成功" );
                        Intent intent = new Intent( );
//                        MyLog.e( "result", " getParent( )=" + getParent( ) );
                        intent.putExtra( "name", etUserName.getText( ).toString( ) );
                        intent.putExtra( "content", etContent.getText( ).toString( ) );
                        setResult( Activity.RESULT_OK, intent );
                        finish( );
                    }

                    @Override
                    public void failure(String msg) {
                        MyToast.showMsgShort( BBSCommitActivity.this, "评论失败" );
                    }
                } );

    }

    @Override
    public boolean onClickLeftButton() {
        return false;
    }

    @Override
    public void onClickRightButton() {

    }

    @Override
    public void setCurrentTitleString() {

        setTitle( "帖子内容" );
    }

    @Override
    public void onRefresh() {

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
