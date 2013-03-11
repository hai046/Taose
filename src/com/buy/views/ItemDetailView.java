package com.buy.views;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buy.bases.MyBaseAdapter;
import com.buy.data.TaobaoDataCallBack;
import com.buy.data.TaobaokeDataManager;
import com.buy.dialogs.TipPopupWindow;
import com.buy.holder.Holder;
import com.buy.holder.ItemDetail;
import com.buy.stores.MySqlLiteDataBase;
import com.buy.util.ImageDownloader;
import com.buy.util.MyHandler;
import com.buy.util.MyHandler.HandlerCallBack;
import com.buy.util.MyLog;
import com.buy.util.Util;
import com.brunjoy.taose.R;
import com.umeng.analytics.MobclickAgent;

public class ItemDetailView extends BaseView implements OnClickListener {
    private String numid, click_url;
    private Button btnBuy;

    public void setNumid(String numid) {
        this.numid = numid;
    }

    public void setClick_url(String click_url) {
        this.click_url = click_url;
    }

    // private LinearLayout leftPics;
    private TextView tvLocation, tvVolume, tvPriceDetail, tvDescDetail, tvTitle, tvCreditScore, item_detail_cashOndelivery;
    // private RelativeLayout mRelativeLayout;
    // private ScrollGalleryView mScrollGalleryView;
    private ImageView image;
    String TAG = "ItemDetailView";

    // private WebView mWebView;
    public ItemDetailView(Context mContext) {
        super( mContext );
        init( );

    }

    public void checkAddStroeStatus() {
        if (isStore( )) {
            MyLog.e( TAG, "checkAddStroeStatus=true" );
            addStronBtn.setImageResource( R.drawable.selector_itemdeail_unaddstore );
            image.setImageResource( R.drawable.sc_on );
        } else {
            MyLog.e( TAG, "checkAddStroeStatus=false" );
            addStronBtn.setImageResource( R.drawable.selector_itemdeail_addstore );
            image.setImageResource( R.drawable.sc );
        }
    }

    public void getData() {
        TaobaokeDataManager.getInstance( mContext ).getItemDetail( numid, mCallBack );
    }

    public void onRefresh() {
        getData( );
    }

    private ListView mListView;
    private ImageView mitem_detail_image;
    private int MixWH = 300;// 顶部最新的图片尺寸
    private View menuView;
    private ImageButton addStronBtn;

    public void init() {
        setContentView( R.layout.item_detail_main );
        mListView = (ListView) findViewById( R.id.item_detail_Listview );
        menuView = findViewById( R.id.item_detail_menu );
        View view = getLayoutInflater( ).inflate( R.layout.item_detail_list_layout, null );
        addStronBtn = (ImageButton) findViewById( R.id.popup_detail_addstore );
        addStronBtn.setOnClickListener( this );
        findViewById( R.id.popup_detail_buyNow ).setOnClickListener( this );
        findViewById( R.id.popup_detail_pcGou ).setOnClickListener( this );
        view.findViewById( R.id.item_detail_PCbuy ).setOnClickListener( this );
        mListView.addHeaderView( view );
        mListView.setCacheColorHint( Color.TRANSPARENT );
        mListView.setOnScrollListener( new OnScrollListener( ) {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // MyLog.i( TAG, "onScrollStateChanged  scrollState=" + scrollState );
                if (scrollState == SCROLL_STATE_IDLE) {
                    if (view.getFirstVisiblePosition( ) > 0) {
                        showPopupMenu( );
                    } else {
                        hidePopupMenu( );
                    }
                }
                // MyLog.i( TAG, "view.getFirstVisiblePosition( )=" + view.getFirstVisiblePosition( ) );

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // MyLog.i( TAG, "onScroll  firstVisibleItem=" + firstVisibleItem + "  visibleItemCount=" + visibleItemCount + " totalItemCount= " + totalItemCount );
            }
        } );
        btnBuy = (Button) view.findViewById( R.id.item_detail_buyBtn );
        // leftPics = (LinearLayout) findViewById( R.id.item_detial_view_Layout );
        // tvSener = (TextView) findViewById( R.id.item_detail_viewSenderTv );
        tvCreditScore = (TextView) view.findViewById( R.id.item_detail_viewCreditScore );
        tvVolume = (TextView) view.findViewById( R.id.item_detail_viewVolume );
        tvPriceDetail = (TextView) view.findViewById( R.id.item_detail_viewPriceDetailTv );
        tvTitle = (TextView) view.findViewById( R.id.item_detail_viewName );
        tvDescDetail = (TextView) view.findViewById( R.id.item_detail_viewaddrAndOthers );
        tvLocation = (TextView) view.findViewById( R.id.item_detail_viewLocation );
        item_detail_cashOndelivery = (TextView) view.findViewById( R.id.item_detail_cashOndelivery );
        btnBuy.setOnClickListener( this );
        image = (ImageView) view.findViewById( R.id.item_detail_liked );
        image.setOnClickListener( this );
        // mScrollGalleryView = new ScrollGalleryView( mContext );
        // mListView = (ListView) findViewById( R.id.item_detail_listViewBottom );
        mitem_detail_image = (ImageView) view.findViewById( R.id.item_detail_image );
        Display mDisplay = ((Activity) mContext).getWindowManager( ).getDefaultDisplay( );
        MixWH = Math.min( mDisplay.getWidth( ), mDisplay.getHeight( ) );
        MixWH = Math.max( MixWH, 300 );
        MyLog.e( TAG, "MixWH====" + MixWH );
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.FILL_PARENT, MixWH - 54 );
        mitem_detail_image.setLayoutParams( params );
        // this.params = new ViewGroup.LayoutParams( MixWH, MixWH );
    }

    ItemDetail item;
    private TaobaoDataCallBack<ItemDetail> mCallBack = new TaobaoDataCallBack<ItemDetail>( ) {
        public void success(ItemDetail T) {
            // MyLog.d( TAG, "success=" + T );
            item = T;
            inintValue( );
        }

        @Override
        public void failure(String msg) {
            // MyLog.d( TAG, "failure=" + msg );
        }
    };

    public void inintValue() {
        // MyLog.d( TAG, "  item.getImgs( ) =" + item.getImgs( ) );
        setPicListView( item.getImgs( ) );
        tvPriceDetail.setText( "￥" + item.getPrice( ) );
        tvTitle.setText( item.getTitle( ) );
        tvDescDetail.setText( item.getNick( ) );
        tvCreditScore.setText( Util.getCreditSpanndText( mContext, item.getSellerCreditScore( ) ) );
        tvVolume.setText( mContext.getString( R.string.mounthVolume ) + item.getVolume( ) );
        tvLocation.setText( item.getLocation( ) );
        if (item.getCashOndelivery( ) == 1) {
            item_detail_cashOndelivery.setVisibility( View.VISIBLE );
        } else {
            item_detail_cashOndelivery.setVisibility( View.GONE );
        }
        if (MySqlLiteDataBase.getInstance( mContext ).getItem( numid ) != null) {
            image.setImageResource( R.drawable.sc_on );
            addStronBtn.setImageResource( R.drawable.selector_itemdeail_unaddstore );
        }
        if (item != null) {
            new AsyncTask<Void, Void, Void>( ) {

                @Override
                protected Void doInBackground(Void... params) {
                    Util.saveTempFile( "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head><body>" + item.getDesc( ) + "</body></html>",
                            mContext );
                    return null;
                }
            }.execute( );
        }

        // mScrollGalleryView.onResume( );
    }

    // private ViewGroup.LayoutParams params;
    private MyBaseAdapter<String> picAdapter = new MyBaseAdapter<String>( ) {

        @Override
        public View bindView(int position, String t, View view) {
            if (view == null) {
                view = getLayoutInflater( ).inflate( R.layout.item_detail_list_item, null );
            }
            ImageView image = (ImageView) view;
            image.setImageResource( R.drawable.zwt1 );
            ImageDownloader.getInstance( ).download( t + ImageDownloader.getInstance( ).getQuality( MixWH ).getValue( ), image );
            return image;
        }
    };

    private void setPicListView(List<String> imgs) {
        if (imgs == null || imgs.size( ) < 1)
            return;
        MyLog.e( TAG, "MixWH====" + MixWH );
        MyLog.e( TAG, "ImageDownloader.getInstance( ).getQuality( MixWH )====" + ImageDownloader.getInstance( ).getQuality( MixWH ) );
        if (imgs.size( ) == 1) {
            ImageDownloader.getInstance( ).download( imgs.get( 0 ) + ImageDownloader.getInstance( ).getQuality( MixWH ).getValue( ), this.mitem_detail_image );
        } else {
            ImageDownloader.getInstance( ).download( imgs.get( 0 ) + ImageDownloader.getInstance( ).getQuality( MixWH ).getValue( ), this.mitem_detail_image );
            picAdapter.addAll( imgs.subList( 1, imgs.size( ) ) );
            picAdapter.notifyDataSetChanged( );
        }
    }

    @Override
    public void onPause() {
        super.onPause( );
    }

    private MyHandler.HandlerCallBack mBack;

    @Override
    public void onResume() {

        mBack = new HandlerCallBack( ) {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                case 9000:
                    MyHandler.getInstance( ).removeMessages( 9000 );
                    View view = findViewById( R.id.item_detail_PCbuy );
                    final int[] location = new int[2];
                    view.getLocationOnScreen( location );
                    if (location[1] <= 0) {
                        MyHandler.getInstance( ).sendEmptyMessageDelayed( 9000, 1000 );
                        return;
                    }
                    TipPopupWindow popupWindow = new TipPopupWindow( mContext, R.drawable.tip_pcgyd );
                    popupWindow.show( 0, location[1], mContext.getResources( ).getDisplayMetrics( ).heightPixels );
                    MyHandler.getInstance( ).removeListener( mBack );
                    break;

                default:
                    break;
                }

            }
        };
        MyHandler.getInstance( ).addListener( mBack );
        MyHandler.getInstance( ).sendEmptyMessageDelayed( 9000, 1000 );
        super.onResume( );
    }

    /**
     * 是否是已经收藏
     * 
     * @return
     */
    public boolean isStore() {
        return MySqlLiteDataBase.getInstance( mContext ).getItem( numid ) != null;
    }

    /**
     * 添加收藏或者删除收藏
     */
    public void collectOrNot() {
        if (isStore( )) {
            image.setImageResource( R.drawable.sc );
            addStronBtn.setImageResource( R.drawable.selector_itemdeail_addstore );
            if (!MySqlLiteDataBase.getInstance( mContext ).open( )) {
                return;
            }
            MySqlLiteDataBase.getInstance( mContext ).deleteDataById( numid );
            MySqlLiteDataBase.getInstance( mContext ).close( );
            Toast.makeText( mContext, "取消收藏成功", Toast.LENGTH_LONG ).show( );
            return;
        }
        if(item==null)
        {
            Toast.makeText( mContext, "正在请求数据，请稍后再试", Toast.LENGTH_SHORT ).show( );
            return;
        }
        Holder holder = new Holder( );
        holder.setPic_url( item.getPicUrl( ) );
        holder.setNum_iid( item.getNumIid( ) + "" );
        holder.setTitle( item.getTitle( ) );
        holder.setClick_url( click_url );
        holder.setItem_location( item.getLocation( ) );
        holder.setPrice( item.getPrice( ) );
        holder.setShop_click_url( item.getDetailUrl( ) );
        holder.setVolume( item.getVolume( ) + "" );
        holder.setSeller_credit_score( item.getSellerCreditScore( ) );
        if (MySqlLiteDataBase.getInstance( mContext ).insertLiked( numid, holder )) {
            Toast.makeText( mContext, "收藏成功", Toast.LENGTH_LONG ).show( );
            image.setImageResource( R.drawable.sc_on );
            addStronBtn.setImageResource( R.drawable.selector_itemdeail_unaddstore );
            MobclickAgent.onEvent( mContext, "addstore", numid );
        } else {
            // MyLog.e( "Error ", "添加收藏失败" );
        }
    }

    public void gotoBuy() {
        Util.gotoBuyNow( mContext, click_url, numid, item != null ? item.getTitle( ) : "" );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId( )) {
        case R.id.popup_detail_addstore:
        case R.id.item_detail_liked:
            if (item == null)
                break;
            collectOrNot( );
            break;
        case R.id.popup_detail_buyNow:
        case R.id.item_detail_buyBtn:
            gotoBuy( );
            break;
        case R.id.item_detail_PCbuy:
        case R.id.popup_detail_pcGou:
            Util.goToScanQR( mContext, numid );
            break;
        }
    }

    public void setHolder(Holder holder) {

        setClick_url( holder.getClick_url( ) );
        setNumid( holder.getNum_iid( ) );
        getData( );

        ImageDownloader.getInstance( ).download( holder.getPic_url( ), this.mitem_detail_image );
        // mScrollGalleryView.addData( holder.getPic_url( ) );
        tvPriceDetail.setText( "￥" + holder.getPrice( ) );
        tvTitle.setText( holder.getTitle( ) );
        tvDescDetail.setText( holder.getNick( ) );
        tvCreditScore.setText( Util.getCreditSpanndText( mContext, holder.getSeller_credit_score( ) ) );
        tvVolume.setText( mContext.getString( R.string.mounthVolume ) + holder.getVolume( ) );
        tvLocation.setText( holder.getItem_location( ) );
        if (holder.getCash_ondelivery( ) == 1) {
            item_detail_cashOndelivery.setVisibility( View.VISIBLE );
        } else {
            item_detail_cashOndelivery.setVisibility( View.GONE );
        }
        if (MySqlLiteDataBase.getInstance( mContext ).getItem( numid ) != null) {
            image.setImageResource( R.drawable.sc_on );
            addStronBtn.setImageResource( R.drawable.selector_itemdeail_unaddstore );
        }
        mListView.setAdapter( picAdapter );
    }

    // private PopupWindow mPopupWindow;

    private Animation mAnimationOut, mAnimationIn;
    private boolean isShowMenu = false;

    public void showPopupMenu() {
        if (mAnimationOut == null) {
            mAnimationOut = AnimationUtils.loadAnimation( mContext, R.anim.menu_out );
            mAnimationIn = AnimationUtils.loadAnimation( mContext, R.anim.menu_in );
        }
        if (isShowMenu)
            return;
        this.menuView.clearAnimation( );
        mAnimationIn.setFillAfter( true );
        menuView.startAnimation( mAnimationIn );
        isShowMenu = true;

    }

    protected void hidePopupMenu() {
        if (mAnimationOut == null || !isShowMenu) {
            return;
        }

        this.menuView.clearAnimation( );
        // mAnimationOut.setFillAfter( true );

        menuView.startAnimation( mAnimationOut );
        isShowMenu = false;
        menuView.postDelayed( new Runnable( ) {

            @Override
            public void run() {
                menuView.setVisibility( View.GONE );

            }
        }, 600 );

    }
}
