package com.buy.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brunjoy.taose.R;
import com.buy.PromotionsActivity;
import com.buy.bases.MyGallery;
import com.buy.data.TaobaoDataCallBack;
import com.buy.data.TaobaokeDataManager;
import com.buy.holder.Holder;
import com.buy.holder.TopicHolder;
import com.buy.util.ImageDownloader;
import com.buy.util.MyHandler;
import com.buy.util.MyHandler.HandlerCallBack;
import com.buy.util.RichText;
import com.buy.util.Util;

public class RecommendView extends BaseView /* implements OnPageChangeListener */{

    private BaseAdapter adapter;
    private ArrayList<Holder> dateSets = new ArrayList<Holder>( );
    private com.buy.widget.LoadMoreListView mListView;

    public RecommendView(final Context mContext) {
        super( mContext );
        View topView = getLayoutInflater( ).inflate( R.layout.recomment_layout, null );
        tvProgress = (TextView) topView.findViewById( R.id.recomment_tvprogress );
        mGallery = (MyGallery) topView.findViewById( R.id.recomment_gallery );
        mGallery.setOnItemSelectedListener( new OnItemSelectedListener( ) {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                MyHandler.getInstance( ).removeMessages( SCROLLER );
                MyHandler.getInstance( ).sendEmptyMessageDelayed( SCROLLER, 4000 );
                onPageSelected( arg2 );
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        } );
        mGallery.setOnItemClickListener( new OnItemClickListener( ) {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
                // if (index == 0)
                // return;
                index = index % list.size( );
                if (index < 0 || index >= list.size( ))
                    return;

                TopicHolder mTopicHolder = list.get( index );

                if (mTopicHolder.getType( ) == 0) {
                    Intent intent = new Intent( );
                    intent.setClass( mContext, PromotionsActivity.class );
                    intent.putExtra( PromotionsActivity.ID, mTopicHolder.getId( ) );
                    intent.putExtra( "icon", "" + mTopicHolder.getPic( ) );
                    intent.putExtra( "name", "" + mTopicHolder.getName( ) );
                    mContext.startActivity( intent );
                } else if (mTopicHolder.getType( ) == 1) {
                    Holder holder = new Holder( );
                    holder.setName( mTopicHolder.getName( ) );
                    holder.setClick_url( mTopicHolder.getUrl( ) );
                    holder.setPic_url( mTopicHolder.getPic( ) );
                    holder.setNum_iid( mTopicHolder.getNumiid( ) );
                    Util.goToDetialActivity( mContext, holder );
                } else if (mTopicHolder.getType( ) == 2) {
                    Util.goToWebActivity( mContext, mTopicHolder.getUrl( ) );
                }

            }
        } );

        adapter = new BaseAdapter( ) {

            @Override
            public int getCount() {

                return dateSets.size( ) % 2 == 0 ? dateSets.size( ) / 2 : (dateSets.size( ) / 2 + 1);
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );

            @Override
            public View getView(int position, View view, ViewGroup parent) {
                if (view == null) {
                    params.weight = 1;
                    LinearLayout layout = new LinearLayout( mContext );
                    layout.setOrientation( LinearLayout.HORIZONTAL );
                    params.leftMargin = 1;
                    params.rightMargin = 0;

                    View v1 = getLayoutItemView( null, dateSets.get( position * 2 ) );
                    v1.setTag( position * 2 );
                    layout.addView( v1, params );

                    params.leftMargin = 0;
                    params.rightMargin = 1;
                    if (position * 2 + 1 < dateSets.size( )) {
                        View v = getLayoutItemView( null, dateSets.get( position * 2 + 1 ) );
                        layout.addView( v, params );
                        v.setTag( position * 2 + 1 );
                    } else {
                        View v = getLayoutItemView( null, dateSets.get( position * 2 ) );
                        v.setVisibility( View.INVISIBLE );
                        layout.addView( v, params );
                        v.setTag( -1 );
                    }
                    return layout;
                } else {
                    LinearLayout layout = (LinearLayout) view;
                    View v1 = getLayoutItemView( layout.getChildAt( 0 ), dateSets.get( position * 2 ) );
                    v1.setTag( position * 2 );
                    if (position * 2 + 1 < dateSets.size( )) {
                        View v = getLayoutItemView( layout.getChildAt( 1 ), dateSets.get( position * 2 + 1 ) );
                        v.setVisibility( View.VISIBLE );
                        v.setTag( position * 2 + 1 );
                    } else {
                        View v = getLayoutItemView( layout.getChildAt( 1 ), dateSets.get( position * 2 ) );
                        v.setVisibility( View.INVISIBLE );
                        v.setTag( -1 );
                    }
                    return layout;
                }

            }

        };
        // com.buy.widget.LoadMoreListView
        mListView = new com.buy.widget.LoadMoreListView( mContext ) {
            @Override
            public void onLoading() {
                if (countPage > currentPage) {
                    getTaobaoKeItemData( currentPage + 1 );
                } else {
                    onStopLoading( );
                }
            }
        };
        mListView.setFastScrollEnabled( true );
        mListView.setDivider( mContext.getResources( ).getDrawable( R.drawable.hbfgx ) );
        mListView.setDividerHeight( 0 );
        mListView.setCacheColorHint( Color.TRANSPARENT );
        mListView.addHeaderView( topView );
        mListView.setAdapter( adapter );
        setContentView( mListView );
        onRefresh( );
        MyHandler.getInstance( ).addListener( mHandlerCallBack );
    }

    public void onRefresh() {

        getTaobaoKeItemData( 1 );
        initTop( );
    }

    private static int currentPage = 1;
    private static int countPage = 1;
    private byte refreshFlag = 0;

    private void initTop() {
        if (galleryAdapter != null) {
            isGalleryClear = true;
        }
        TaobaokeDataManager.getInstance( mContext ).getTopics( 0, new TaobaoDataCallBack<TopicHolder>( ) {

            public void success(java.util.List<TopicHolder> list) {
                if (list != null) {
                    setDatas( list );
                    refreshFlag++;
                    if (refreshFlag % 2 == 0) {
                        refreshFlag = 0;
                        stopRefresh( );
                    }
                }
                // MyLog.e( TAG, "topic items  list=" + list );
            }

            @Override
            public void failure(String msg) {
//                MyToast.showMsgShort( mContext, msg );
            }
        } );

    }

    private OnClickListener mOnClickListener = new OnClickListener( ) {

        @Override
        public void onClick(View v) {
            switch (v.getId( )) {
            case R.id.topic_itemLayout:
                if (v.getTag( ) instanceof Integer) {
                    int index = (Integer) v.getTag( );
                    if (index < 0 || index >= dateSets.size( ))
                        return;
                    Holder holder = dateSets.get( index );
                    Util.goToDetialActivity( mContext, holder );

                }
                break;

            default:
                break;
            }

        }
    };
    private boolean isClear = false;

    public void getTaobaoKeItemData(int page) {
        if (page == 0 || page == 1)
            isClear = true;
        else
            isClear = false;
        currentPage = page;
        TaobaokeDataManager.getInstance( mContext ).getTopicItem( 0, page, mTaobaoDataCallBack );
    }

    private TaobaoDataCallBack<Holder> mTaobaoDataCallBack = new TaobaoDataCallBack<Holder>( ) {

        public void success(java.util.List<Holder> list) {
            refreshFlag++;
            if (refreshFlag % 2 == 0) {
                refreshFlag = 0;
                stopRefresh( );
            }
            if (list == null)
                return;
            if (isClear)
                dateSets.clear( );
            dateSets.addAll( list );
            // MyLog.e( "list", "list=" + list );
            adapter.notifyDataSetChanged( );

        }

        public void countPage(int page) {
            countPage = page;
            if (currentPage == page)
                mListView.onStopLoading( );
        }

        @Override
        public void failure(String msg) {
            Toast.makeText( mContext, msg, Toast.LENGTH_LONG ).show( );
            mListView.onStopLoading( );
        }
    };

    private View getLayoutItemView(View convertView, Holder item) {
        ImageView img = null;
        if (convertView == null) {
            convertView = getLayoutInflater( ).inflate( R.layout.topic_item, null );
        }
        img = (ImageView) convertView.findViewById( R.id.topic_img );
        TextView tvTitle = (TextView) convertView.findViewById( R.id.topic_title );
        TextView tvPrice = (TextView) convertView.findViewById( R.id.topic_price );
        TextView tvVolumn = (TextView) convertView.findViewById( R.id.topic_volumn );

        tvVolumn.setText( "销量：" + item.getVolume( ) );
        tvTitle.setText( item.getName( ) );
        tvPrice.setText( item.getPrice( ) );
        img.setImageResource( R.drawable.zwt2 );
        ImageDownloader.getInstance( ).download( item.getPic_url( ), img );
        convertView.setOnClickListener( mOnClickListener );
        return convertView;
    }

    @Override
    public void onResume() {

        if (list.size( ) > 0) {
            MyHandler.getInstance( ).removeMessages( SCROLLER );
            MyHandler.getInstance( ).sendEmptyMessageDelayed( SCROLLER, 4000 );
        }
        super.onResume( );
    }

    @Override
    public void onPause() {
        MyHandler.getInstance( ).removeMessages( SCROLLER );
        super.onPause( );
    }

    private TextView tvProgress;
    private MyGallery mGallery;
    private BaseAdapter galleryAdapter;
    private boolean isGalleryClear;
    private List<TopicHolder> list = new ArrayList<TopicHolder>( );

    public void setDatas(List<TopicHolder> list) {
        if (list != null) {
            if (isGalleryClear) {
                this.list.clear( );
                isGalleryClear = false;
            }
            this.list.addAll( list );
            if (galleryAdapter == null) {
                galleryAdapter = new BaseAdapter( ) {

                    private Gallery.LayoutParams params = new Gallery.LayoutParams( Gallery.LayoutParams.FILL_PARENT, Gallery.LayoutParams.FILL_PARENT );

                    public View bindView(int position, String t, View view) {
                        ImageView image;
                        if (view == null) {
                            image = new ImageView( mContext );
                            image.setScaleType( ImageView.ScaleType.CENTER_CROP );
                            image.setLayoutParams( params );
                        } else {
                            image = (ImageView) view;
                        }
                        ImageDownloader.getInstance( ).download( t, image );
                        // MyLog.e( TAG, "Gallery getView" + position );
                        return image;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        if (RecommendView.this.list.size( ) > 0) {
                            position = position % RecommendView.this.list.size( );
                            return bindView( position, RecommendView.this.list.get( position ).getPic( ), convertView );
                        } else {
                            return null;
                        }

                    }

                    public int getCount() {
                        // TODO　　内网测试去掉循环
                        return RecommendView.this.list.size( );
                    }

                    @Override
                    public Object getItem(int position) {
                        return null;
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }
                };
                mGallery.setAdapter( galleryAdapter );
            } else {
                galleryAdapter.notifyDataSetChanged( );
            }
            mGallery.setFadingEdgeLength( 0 );
            mGallery.setSelection( 0/* this.list.size( ) * 500 */);
            onPageSelected( 0 );
            MyHandler.getInstance( ).removeMessages( SCROLLER );
            MyHandler.getInstance( ).sendEmptyMessageDelayed( SCROLLER, 4000 );
        } else {
            return;
        }

    }

    private static final int SCROLLER = 1215;

    private HandlerCallBack mHandlerCallBack = new HandlerCallBack( ) {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

            case SCROLLER:
                int size = list.size( );
                if (size > 0) {
                    int current = mGallery.getSelectedItemPosition( ) + 1;
                    onPageSelected( (current) % size );
                    mGallery.setSelection( (current) % size );
                }
                break;
            default:
                break;
            }

        }
    };

    @Override
    public void onDestroy() {
        MyHandler.getInstance( ).removeMessages( SCROLLER );
        MyHandler.getInstance( ).removeListener( mHandlerCallBack );
        super.onDestroy( );
    }

    RichText rt = new RichText( mContext );

    public void onPageSelected(int position) {
        rt.clear( );
        if (list.size( ) == 0)
            return;
        position = position % list.size( );
        for (int i = 0; i < list.size( ); i++) {
            rt.addText( " " );
            if (position % list.size( ) == i) {
                // rt.addText( " . ", Color.WHITE, Typeface.BOLD );
                rt.addImage( BitmapFactory.decodeResource( mContext.getResources( ), R.drawable.point2 ), 14, 14 );

            } else {
                // rt.addText( " . ", Color.GRAY, Typeface.BOLD );
                rt.addImage( BitmapFactory.decodeResource( mContext.getResources( ), R.drawable.point1 ), 14, 14 );
            }
            rt.addText( " " );
        }

        this.tvProgress.setText( rt );
        MyHandler.getInstance( ).removeMessages( SCROLLER );
        MyHandler.getInstance( ).sendEmptyMessageDelayed( SCROLLER, 4000 );
    }

}
