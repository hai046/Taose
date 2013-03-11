package com.buy.views;

import java.text.SimpleDateFormat;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.buy.bases.MyBaseAdapter;
import com.buy.data.TaobaoDataCallBack;
import com.buy.data.TaobaokeDataManager;
import com.buy.holder.TItemRate;
import com.buy.util.Util;
import com.buy.widget.RefreshListView;
import com.buy.widget.RefreshListView.IXListViewListener;
import com.brunjoy.taose.R;

public class ItemCommentView extends BaseView {

    public static final int PageNum = 30;
    private RefreshListView mListView;

    public ItemCommentView(Context mContext) {
        super( mContext );
        mListView = new RefreshListView( mContext );
        mListView.startLoadMore( );
        mListView.stopRefresh( );
        mListView.setAdapter( adapter );
        setContentView( mListView );
        mListView.setOnListViewListener( new IXListViewListener( ) {

            @Override
            public void onRefresh() {
                adapter.clear( );
                getData( 0, PageNum );
                mListView.setPullLoadEnable( true );
                mListView.setPullRefreshEnable( true );
                mListView.stopLoadMore( );
                mListView.stopRefresh( );

            }

            @Override
            public void onLoadMore() {
                getData( adapter.getCount( ), PageNum );
            }
        } );
        // mListView.startLoadMore( );
    }

    private SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
    private MyBaseAdapter<TItemRate> adapter = new MyBaseAdapter<TItemRate>( ) {

        @Override
        public View bindView(int position, TItemRate t, View convertView) {

            if (convertView == null)
                convertView = getLayoutInflater( ).inflate( R.layout.item_comment, null );
            TextView tvContent = (TextView) convertView.findViewById( R.id.comment_content );
            TextView tvName = (TextView) convertView.findViewById( R.id.comment_name );
            TextView tvDate = (TextView) convertView.findViewById( R.id.comment_date );
            TextView tvCredit = (TextView) convertView.findViewById( R.id.comment_credit );
            tvContent.setText( t.getContent( ) );
            tvName.setText( t.getBuyer( ) );
            tvDate.setText( sdf.format( t.getCommentDate( ) ) );
            tvCredit.setText( Util.getUserCreditSpanndText( mContext, t.getCredit( ) ) );
            return convertView;
        }
    };
    private TaobaoDataCallBack<TItemRate> taobaoDataCallBack = new TaobaoDataCallBack<TItemRate>( ) {

        public void success(java.util.List<TItemRate> list) {
            stopRefresh( );
            adapter.addAll( list );
            adapter.notifyDataSetChanged( );
            if (list != null) {
                if (list.size( ) < PageNum) {
                    mListView.setPullLoadEnable( false );
                }
            } else {
                if (adapter.getCount( ) > 0) {
                    mListView.setPullLoadEnable( false );
                }
            }

            if (adapter.getCount( ) <= 0) {
                onFailure( "没有新的评论" );
            } else {
                onSuccess( );
            }
        }

        @Override
        public void failure(String msg) {
            stopRefresh( );
            onFailure( TextUtils.isEmpty( msg ) ? "宝贝暂时没有评价" : msg );
//            ItemCommentView.this.onFailure( mContext.getString( R.string.loadFailure ) );
            mListView.stopLoadMore( );
            mListView.stopRefresh( );
//            Toast.makeText( mContext, TextUtils.isEmpty( msg ) ? mContext.getString( R.string.loadFailure ) : msg, 1 ).show( );
        }
    };

    public void getData(int offset, int limit) {
        if (numIid != null) {
            onRefresh( );
            TaobaokeDataManager.getInstance( mContext ).getComments( numIid, offset, limit, taobaoDataCallBack );
        }
    }

    public void getNewDate() {
        adapter.clear( );
        getData( 0, PageNum );
    }

    private String numIid = null;

    public void getData(String numIid, int offset, int limit) {
        this.numIid = numIid;
        TaobaokeDataManager.getInstance( mContext ).getComments( numIid, offset, limit, taobaoDataCallBack );

    }
}
