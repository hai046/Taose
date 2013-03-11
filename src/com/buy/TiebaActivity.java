package com.buy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.buy.bases.BaseActivity;
import com.buy.bases.MyBaseAdapter;
import com.buy.data.TaobaoDataCallBack;
import com.buy.data.TaobaokeDataManager;
import com.buy.holder.Article;
import com.buy.holder.Comment;
import com.buy.util.MyLog;
import com.buy.util.MyToast;
import com.buy.widget.LoadMoreListView;
import com.buy.widget.LoadMoreListView.LoadCallBack;
import com.brunjoy.taose.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 帖子具体内容 一定需要传参数 articleID 整形
 * 
 * @author 邓海柱<br>
 *         E-mail:denghaizhu@brunjoy.com
 */
public class TiebaActivity extends /* SherlockActivity */BaseActivity {

    private LoadMoreListView mListView;

    private int articleID;
    private Button btnComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        articleID = getIntent( ).getIntExtra( "articleID", -1 );
        if (articleID < 0) {
            MyToast.showMsgShort( this, "传递参数错误" );
            finish( );
            return;
        }
        setContentView( R.layout.tieba_item_layout );
        mListView = (LoadMoreListView) findViewById( R.id.teiba_layout_listview );
        mListView.addHeaderView( getHeaderView( ) );
        mListView.setAdapter( adapter );
        mListView.setDividerHeight( 0 );
        mListView.setSelector( getResources( ).getDrawable( R.drawable.selector_transparent ) );
        mListView.showFooterView( );
        mListView.setFastScrollEnabled( true );
        btnComment = (Button) findViewById( R.id.tieba_reply_btn );
        btnComment.setOnClickListener( this );
        mListView.setOnLoadCallBack( new LoadCallBack( ) {

            @Override
            public void onLoading() {
                getArticlesNext( );
            }
        } );
        onRefresh( true );
        setTopBtnBackground( R.drawable.selector_back, R.drawable.selector_loading );

        // 为admin做的输出评论
        if (MyLog.isDEBUG) {
            mDialog = new AlertDialog.Builder( this ).setNegativeButton( "取消", null ).setPositiveButton( "删除", mDelClickListener ).create( );

            mListView.setOnItemLongClickListener( new OnItemLongClickListener( ) {

                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    MyLog.e( "tag", "position=" + position );
                    if (position > 0 && position <= adapter.getCount( )) {
                        Comment mComment = adapter.getItem( position - 1 );
                        MyLog.e( "tag", "mComment=" + mComment.getArticleID( ) + "  msg= " + mComment.getMsg( ) + "  id=" + mComment.getId( ) );
                        mDelClickListener.setPosition( mComment.getId( ) );
                        mDialog.setTitle( "是否删除 " + mComment.getCommentorName( ) + " 的评论？" );
                        mDialog.show( );

                    }
                    return true;
                }
            } );
        }
        // end admin
    }

    // Admin
    private Dialog mDialog;
    private DelOnClickListener mDelClickListener = new DelOnClickListener( ) {

        @Override
        public void onMyClick(DialogInterface dialog, long commentId, int which) {
            TaobaokeDataManager.getInstance( TiebaActivity.this ).doBBSCommentDele( commentId, mDelTaobaoDataCallBack );
            MyLog.e( "tag", "which=" + which + "  commentId=" + commentId );
        }
    };

    private TaobaoDataCallBack<String> mDelTaobaoDataCallBack = new TaobaoDataCallBack<String>( ) {

        public void success(String t) {
            MyToast.showMsgShort( TiebaActivity.this, t );
            onRefresh( );
        }

        @Override
        public void failure(String msg) {
            MyToast.showMsgShort( TiebaActivity.this, "操作失败" );

        }
    };

    private abstract class DelOnClickListener implements OnClickListener {

        private long id;

        public void setPosition(long id) {
            this.id = id;

        }

        public abstract void onMyClick(DialogInterface dialog, long commentId, int which);

        @Override
        public void onClick(DialogInterface dialog, int which) {
            onMyClick( dialog, id, which );

        }

    }

    // end Admin
    @Override
    public void onClick(View v) {
        switch (v.getId( )) {
        case R.id.tieba_addcomment:
        case R.id.tieba_reply_btn:
            Intent intent = new Intent( );
            intent.setClass( this, BBSCommitActivity.class );
            intent.putExtra( "articleID", articleID );
            startActivityForResult( intent, 124 );
            break;

        default:
            break;
        }
        super.onClick( v );
    }

    // private Comment resultComment;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        MyLog.e( "result", "requestCode=" + requestCode + "  resultCode= " + resultCode );
        if (requestCode == 124 && resultCode == Activity.RESULT_OK) {
            // Bundle mBundle = data.getExtras( );
            String name = data.getStringExtra( "name" );
            String mContent = data.getStringExtra( "content" );
            MyLog.e( "result", "name=" + name );
            MyLog.e( "result", "mContent=" + mContent );
            Comment resultComment = new Comment( );
            resultComment.setCommentorName( name );
            resultComment.setMsg( mContent );
            resultComment.setId( -1 );
            adapter.add( resultComment );
            adapter.notifyDataSetChanged( );
        }
        // super.onActivityResult( requestCode, resultCode, data ); 
    }

    // private int currentPage = 0;
    // private int pageCount = 0;
    @Override
    public void onRefresh() {
        onRefresh( false );
    }

    public void onRefresh(boolean begin_count) {
        mListView.setMyEnabel( true );
        adapter.clear( );
        onRightBtnRota( );
        TaobaokeDataManager.getInstance( this ).getBBSComments( articleID, 20, 0, begin_count, true, mTaobaoDataCallBack );
    }

    private void getArticlesNext() {
        onRightBtnRota( );
        TaobaokeDataManager.getInstance( this ).getBBSComments( articleID, 20, adapter.getCount( ), false, true, mTaobaoDataCallBack );
    }

    private TaobaoDataCallBack<Comment> mTaobaoDataCallBack = new TaobaoDataCallBack<Comment>( ) {
        public void success(java.util.List<Comment> list) {
            if (adapter.getCount( ) > 0) {
                Comment mComment = adapter.getItem( adapter.getCount( ) - 1 );
                if (mComment.getId( ) < 0) {
                    boolean flag = adapter.removeAt( adapter.getCount( ) - 1 );
                    MyLog.e( "result", "flag=" + flag );
                }
            }
            if (list != null && list.size( ) > 0) {
                adapter.addAll( list );
                adapter.notifyDataSetChanged( );
            }
            stopRinghtBtn( );
        }

        public void extraObject(Object... obj) {
            if (obj.length >= 1) {
                if (obj[0] != null && obj[0] instanceof Article) {
                    Article mArticle = (Article) obj[0];
                    tvName.setText( mArticle.getArticleName( ) );
                    tvContent.setText( mArticle.getArticleBody( ) );
                    tvNum.setText( mArticle.getReplyNum( ) + "" );
                    tvSee.setText( mArticle.getHas_see( ) + "" );
                    if (mArticle.getReplyNum( ) > 0) {
                        tiebaCommentLayout.setVisibility( View.VISIBLE );
                    } else {
                        tiebaCommentLayout.setVisibility( View.GONE );
                    }
                }
            }
            if (obj.length >= 2) {
                if (obj[1] != null && obj[1] instanceof Boolean) {
                    Boolean bool = (Boolean) obj[1];
                    if (bool) {
                        findViewById( R.id.tieba_addcomment ).setVisibility( View.VISIBLE );
                        findViewById( R.id.tieba_reply_btn ).setVisibility( View.VISIBLE );
                    } else {
                        findViewById( R.id.tieba_addcomment ).setVisibility( View.GONE );
                        findViewById( R.id.tieba_reply_btn ).setVisibility( View.GONE );
                    }
                }
            }
        }

        @Override
        public void countPage(int page) {
            MyLog.e( "tiebaActivity", "page= " + page );
            if (adapter.getCount( ) >= page) {
                mListView.setMyEnabel( false );
            }
            mListView.onStopLoading( );
        }

        @Override
        public void failure(String msg) {
            stopRinghtBtn( );
        }
    };
    TextView tvName, tvContent, tvNum, tvSee;
    private View tiebaCommentLayout;

    private View getHeaderView() {
        View view = getLayoutInflater( ).inflate( R.layout.exchange_item, null );
        tvNum = (TextView) view.findViewById( R.id.tieba_num );
        tvSee = (TextView) view.findViewById( R.id.tieba_see );
        tiebaCommentLayout = view.findViewById( R.id.tieba_commentLayout );
        tvName = (TextView) view.findViewById( R.id.tieba_title );
        tvContent = (TextView) view.findViewById( R.id.tieba_content );
        // tvReplyNum = (TextView) view.findViewById( R.id.tieba_num );
        tvSee.setText( getIntent( ).getIntExtra( "has_see", 0 ) + "" );
        int commitCount = getIntent( ).getIntExtra( "replyNum", 0 );
        if (commitCount > 0) {
            tiebaCommentLayout.setVisibility( View.VISIBLE );
        }
        tvNum.setText( commitCount + "" );
        tvName.setText( getIntent( ).getStringExtra( "name" ) );
        view.findViewById( R.id.tieba_addcomment ).setOnClickListener( this );
        tvContent.setText( getIntent( ).getStringExtra( "content" ) );
        return view;
    }

    private MyBaseAdapter<Comment> adapter = new MyBaseAdapter<Comment>( ) {

        @Override
        public View bindView(int position, Comment t, View view) {
            if (view == null) {
                view = getLayoutInflater( ).inflate( R.layout.tieba_item, null );
            }
            TextView name = (TextView) view.findViewById( R.id.tieba_item_userName );
            // TextView floorNum = (TextView) view.findViewById( R.id.tieba_item_floorNum );
            TextView content = (TextView) view.findViewById( R.id.tieba_item_content );
            name.setText( TextUtils.isEmpty( t.getCommentorName( ) ) ? "匿名用户" : t.getCommentorName( ) );
            // floorNum.setText( (position + 1) + " 楼" );
            content.setText( t.getMsg( ) );
            view.findViewById( R.id.tiebaContent_layout ).setBackgroundResource( (position == getCount( ) - 1) ? R.drawable.bbs_list_bottom : R.drawable.bbs_list_middle );
            return view;
        }
    };

    public boolean onClickLeftButton() {
        return false;
    }

    public void onClickRightButton() {
        onRefresh( );
    }

    public void setCurrentTitleString() {
        setTitle( "帖子内容" );

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
