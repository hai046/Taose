package com.buy.views;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.buy.TiebaActivity;
import com.buy.bases.MyBaseAdapter;
import com.buy.data.TaobaoDataCallBack;
import com.buy.data.TaobaokeDataManager;
import com.buy.holder.Article;
import com.buy.widget.RefreshListView;
import com.buy.widget.RefreshListView.IXListViewListener;
import com.brunjoy.taose.R;

/**
 * 贴吧列表
 * 
 * @author haizhu
 * 
 */
public class TiebaListView extends BaseView {

    private RefreshListView mListView;

    public TiebaListView(Context mContext) {
        super( mContext );
        mListView = new RefreshListView( mContext );
        mListView.setVisibility( View.GONE );
        mListView.setDivider( mContext.getResources( ).getDrawable( R.drawable.transparent ) );
        mListView.setAdapter( adapter );
        // int padding=mContext.getResources( ).getDimensionPixelSize( R.dimen.margin6 );
        // mListView.setPadding(padding , padding, padding, padding );
        // mListView.setScrollBarStyle( ListView.SCROLLBARS_INSIDE_INSET);
        setContentView( mListView );
        mListView.setOnItemClickListener( new OnItemClickListener( ) {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                if (arg2 - 1 >= 0 && arg2 - 1 < adapter.getCount( )) {
                    Intent intent = new Intent( );
                    Article mArticle = adapter.getItem( arg2 - 1 );
                    intent.putExtra( "articleID", mArticle.getId( ) );
                    intent.putExtra( "replyNum", mArticle.getReplyNum( ) );
                    intent.putExtra( "name", mArticle.getArticleName( ) );
                    intent.putExtra( "content", mArticle.getArticleBody( ) );
                    intent.putExtra( "has_see", mArticle.getHas_see( ) );
                    intent.setClass( TiebaListView.this.mContext, TiebaActivity.class );
                    TiebaListView.this.mContext.startActivity( intent );
                }

            }
        } );
        mListView.setOnListViewListener( new IXListViewListener( ) {

            @Override
            public void onRefresh() {
                TiebaListView.this.onRefresh( );

            }

            @Override
            public void onLoadMore() {
                getNextPage( );

            }
        } );
        onRefresh( );
    }

    private boolean isFirst = true;
    
    public void onCompile() {
        if (isFirst) {
            mListView.setVisibility( View.VISIBLE );
            isFirst = false;
        }
    }

    @Override
    public void onRefresh() {
        adapter.clear( );
        mListView.setPullLoadEnable( true );
        mListView.setPullRefreshEnable( true );
        TaobaokeDataManager.getInstance( mContext ).getBBSArticleList( 15, 0, taobaoDataCallBack );
    }

    private void getNextPage() {
        TaobaokeDataManager.getInstance( mContext ).getBBSArticleList( 15, adapter.getCount( ), taobaoDataCallBack );
    }

    private TaobaoDataCallBack<Article> taobaoDataCallBack = new TaobaoDataCallBack<Article>( ) {
        public void success(java.util.List<Article> list) {
            if (list != null && list.size( ) > 0) {
                adapter.addAll( list );
                adapter.notifyDataSetChanged( );

            }
            onCompile( );
        }

        @Override
        public void countPage(int page) {
            if (page <= adapter.getCount( )) {
                mListView.setPullLoadEnable( false );
                // mListView.setPullRefreshEnable( false );
            }
            mListView.stopLoadMore( );
            mListView.stopRefresh( );

        }

        @Override
        public void failure(String msg) {

        }
    };

    private MyBaseAdapter<Article> adapter = new MyBaseAdapter<Article>( ) {

        @Override
        public View bindView(int position, Article t, View view) {
            if (view == null) {
                view = getLayoutInflater( ).inflate( R.layout.bbslist_item, null );
            }
            TextView tvName = (TextView) view.findViewById( R.id.tieba_title );
            TextView tvContent = (TextView) view.findViewById( R.id.tieba_content );
            TextView tvSee = (TextView) view.findViewById( R.id.tieba_see );
            tvSee.setText( "" + t.getHas_see( ) );
            tvContent.setMaxLines( 6 );// 再次设置最大6行
            TextView tvReplyNum = (TextView) view.findViewById( R.id.tieba_num );
            tvName.setText( t.getArticleName( ) );
            tvContent.setText( t.getArticleBody( ) );
            tvReplyNum.setText( "" + t.getReplyNum( ) );
            return view;
        }
    };

}
