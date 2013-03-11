package com.buy.views;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.buy.bases.MyBaseAdapter;
import com.buy.data.TaobaoDataCallBack;
import com.buy.data.TaobaokeDataManager;
import com.buy.holder.Holder;
import com.buy.util.ImageDownloader;
import com.buy.util.MyLog;
import com.buy.util.Util;
import com.brunjoy.taose.R;

public class TopicDetailView extends BaseView {

    // private TextView tvTitle;
    private ListView mListView;

    public TopicDetailView(Context mContext) {
        super( mContext );
        View view = getLayoutInflater( ).inflate( R.layout.topic_layout, null );

        setContentView( R.layout.topic_layout );
        // tvTitle = (TextView) findViewById( R.id.topic_layout_tip );
        mListView = new ListView( mContext );// (ListView) findViewById( R.id.topic_layout_listview );
        mListView.addHeaderView( view );
        mListView.setAdapter( adapter );
        setContentView( mListView );
        adapter.clear( );

    }

    public void stopRefresh() {

    }

    private TaobaoDataCallBack<Holder> mTaobaoDataCallBack = new TaobaoDataCallBack<Holder>( ) {

        public void success(java.util.List<Holder> list) {
            adapter.addAll( list );
            //MyLog.e( "list", "list=" + list );
            adapter.notifyDataSetChanged( );
            // mListView.stopLoadMore( );
            // if (list == null || list.size( ) < PageNum)
            // mListView.setPullLoadEnable( false );
            // mListView.stopRefresh( );
//            MyLog.e( "test", "successsuccess" );
            stopRefresh( );
        }

        public void countPage(int page) {

            if (TopicDetailView.this.page == page) {
                // mListView.stopLoadMore( );
                // mListView.stopRefresh( );
                // mListView.setPullLoadEnable( false );
            }

        }

        @Override
        public void failure(String msg) {
            Toast.makeText( mContext, R.string.loadFailure, Toast.LENGTH_LONG ).show( );
            // mListView.stopLoadMore( );
            // mListView.stopRefresh( );
//            MyLog.e( "test", "sufailurefailurefailureccess" );
            stopRefresh( );
        }
    };

    private MyBaseAdapter<Holder> adapter = new MyBaseAdapter<Holder>( ) {

        @Override
        public View bindView(int position, Holder t, View view) {
            if (view == null)
                view = getLayoutInflater( ).inflate( R.layout.item_category_detail, null );
            TextView tvName = (TextView) view.findViewById( R.id.tv_name );
            TextView tvPrice = (TextView) view.findViewById( R.id.tv_price );
            TextView tvVolume = (TextView) view.findViewById( R.id.tv_volume );
            // TextView tvSellSend = (TextView) view.findViewById( R.id.tv_SellSend );// 是否是卖家包邮
            TextView tvLocation = (TextView) view.findViewById( R.id.tv_location );

            TextView tv_autoSend = (TextView) view.findViewById( R.id.tv_autoSend );
            if (t.getCash_ondelivery( ) == 1) {
                tv_autoSend.setVisibility( View.VISIBLE );
            } else {
                tv_autoSend.setVisibility( View.GONE );
            }

            tvName.setText( t.getName( ) );
            tvPrice.setText( "￥" + t.getPrice( ) );
            tvVolume.setText( getString( R.string.mounthVolume ) + t.getVolume( ) /* + getString( R.string.each ) */);

            ImageView image = (ImageView) view.findViewById( R.id.detail_image );
            image.setImageResource( R.drawable.zwt1 );
            ImageDownloader.getInstance( ).download( t.getPic_url( ), image );
            // if (t.getCash_ondelivery( ) == 1) {
            // tvSellSend.setText( "支持货到付款" );
            // } else {
            // tvSellSend.setVisibility( View.GONE );
            // }

            TextView tvCreditScore = (TextView) view.findViewById( R.id.tv_credit_score );
            tvCreditScore.setText( Util.getCreditSpanndText( mContext, t.getSeller_credit_score( ) ) );

            tvLocation.setText( t.getItem_location( ) );

            View v = view.findViewById( R.id.item_category_detail );
            v.setTag( position );
            v.setOnClickListener( mOnClickListener );
            return view;
        }
    };

    private String getString(int resStrId) {
        return mContext.getResources( ).getString( resStrId );
    }

    private OnClickListener mOnClickListener = new OnClickListener( ) {

        @Override
        public void onClick(View v) {
            switch (v.getId( )) {

            case R.id.item_category_detail:
                if (v.getTag( ) != null && v.getTag( ) instanceof Integer) {
                    Holder holder = adapter.getItem( (Integer) v.getTag( ) );
                    Util.goToDetialActivity( mContext, holder );
                }
                break;
            }
        }
    };

    public void setTopicId(int topicId, String pic) {
        ImageView topImage = (ImageView) findViewById( R.id.topic_topImage );
        ImageDownloader.getInstance( ).download( pic, topImage );
        this.topicId = topicId;
        initDate( );

    }

    int page = 1;
    int topicId = -1;

    private void initDate() {
        TaobaokeDataManager.getInstance( mContext ).getTopicItem( topicId, page, mTaobaoDataCallBack );

    }

    public void onRefresh() {
        
        page = 1;
        adapter.clear( );
        TaobaokeDataManager.getInstance( mContext ).getTopicItem( topicId, page, mTaobaoDataCallBack );
    }

}
