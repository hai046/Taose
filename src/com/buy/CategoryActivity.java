package com.buy;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buy.bases.BaseActivity;
import com.buy.bases.MyBaseAdapter;
import com.buy.data.TaobaoDataCallBack;
import com.buy.data.TaobaokeDataManager;
import com.buy.dialogs.LoadingDialog;
import com.buy.holder.Category;
import com.buy.holder.Holder;
import com.buy.holder.ParentsCategory;
import com.buy.util.ImageDownloader;
import com.buy.util.MyNoTitleDialog;
import com.buy.util.MyToast;
import com.buy.util.Util;
import com.buy.widget.RefreshListView;
import com.buy.widget.RefreshListView.IXListViewListener;
import com.brunjoy.taose.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * 显示具体小分类内容 <br>
 * 需要输入title<br>
 * 必须传入int类型的参数 cid
 * 
 * @author 邓海柱<br>
 *         E-mail:denghaizhu@brunjoy.com
 */
public class CategoryActivity extends BaseActivity implements OnClickListener {
    String TAG = "CategoryActivity";
    private int id;
    private Category parentCategory;
    private RefreshListView mListView;
    private Button btnLeft, btnCenter, btnRight;
    private String name;
    private boolean isParents = true;
    private boolean isGridView = true;
    private MyAdapter adapter = new MyAdapter( );

    class MyAdapter extends MyBaseAdapter<Holder> {

        @Override
        public View bindView(int position, Holder t, View view) {
            if (view == null || !(view instanceof RelativeLayout))
                view = getLayoutInflater( ).inflate( R.layout.item_category_detail, null );
            TextView tvName = (TextView) view.findViewById( R.id.tv_name );
            TextView tvPrice = (TextView) view.findViewById( R.id.tv_price );
            TextView tvVolume = (TextView) view.findViewById( R.id.tv_volume );

            TextView tvLocation = (TextView) view.findViewById( R.id.tv_location );
            tvName.setText( t.getTitle( ) );
            tvPrice.setText( "￥" + t.getPrice( ) );
            tvVolume.setText( getString( R.string.mounthVolume ) + t.getVolume( )/* + getString( R.string.each ) */);

            ImageView image = (ImageView) view.findViewById( R.id.detail_image );
            image.setImageResource( R.drawable.zwt2 );
            ImageDownloader.getInstance( ).download( t.getPic_url( ), image );
            TextView tv_autoSend = (TextView) view.findViewById( R.id.tv_autoSend );
            if (t.getCash_ondelivery( ) == 1) {
                tv_autoSend.setVisibility( View.VISIBLE );
            } else {
                tv_autoSend.setVisibility( View.GONE );
            }
            TextView tvCreditScore = (TextView) view.findViewById( R.id.tv_credit_score );
            tvCreditScore.setText( Util.getCreditSpanndText( CategoryActivity.this, t.getSeller_credit_score( ) ) );

            tvLocation.setText( t.getItem_location( ) );
            View v = view.findViewById( R.id.item_category_detail );
            v.setTag( position );
            v.setOnClickListener( CategoryActivity.this );
            return view;
        }

        @Override
        public int getCount() {

            return isGridView ? (size( ) % 2 == 0 ? size( ) / 2 : ((size( ) / 2 + 1))) : size( );
        }

        public int size() {
            return super.getCount( );
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // //MyLog.i( TAG, "getView    position=" + position );
            return isGridView ? (getGridItemView( position, convertView )) : super.getView( position, convertView, parent );
        }
    }

    protected View getGridItemView(int position, View view) {
        if (view == null || !(view instanceof LinearLayout)) {
            params.weight = 1;
            LinearLayout layout = new LinearLayout( CategoryActivity.this );
            layout.setOrientation( LinearLayout.HORIZONTAL );
            params.leftMargin = 1;
            params.rightMargin = 0;
            adapter.getItem( position * 2 );
            View v1 = getLayoutItemView( null, adapter.getItem( position * 2 ), position * 2 );
            v1.setTag( position * 2 );
            layout.addView( v1, params );
            params.leftMargin = 0;
            params.rightMargin = 1;
            if (position * 2 + 1 < adapter.size( )) {
                View v = getLayoutItemView( null, adapter.getItem( position * 2 + 1 ), position * 2 + 1 );
                layout.addView( v, params );
                // v.setTag( position * 2 + 1 );
            } else {
                View v = getLayoutItemView( null, adapter.getItem( position * 2 ), -1 );
                v.setVisibility( View.INVISIBLE );
                layout.addView( v, params );
                // v.setTag( -1 );
            }
            return layout;
        } else {
            LinearLayout layout = (LinearLayout) view;
            // View v1 = layout.getChildAt( 0 );
            getLayoutItemView( layout.getChildAt( 0 ), adapter.getItem( position * 2 ), position * 2 );

            if (position * 2 + 1 < adapter.size( )) {
                View v = getLayoutItemView( layout.getChildAt( 1 ), adapter.getItem( position * 2 + 1 ), position * 2 + 1 );
                v.setVisibility( View.VISIBLE );
            } else {
                View v = getLayoutItemView( layout.getChildAt( 1 ), adapter.getItem( position * 2 ), -1 );
                v.setVisibility( View.INVISIBLE );
            }
            return layout;
        }

    }

    private View getLayoutItemView(View convertView, Holder item, int position) {
        ImageView img = null;
        if (convertView == null) {
            convertView = getLayoutInflater( ).inflate( R.layout.category_grid_item, null );
        }
        img = (ImageView) convertView.findViewById( R.id.category_grid_img );
        TextView tvScreditScore = (TextView) convertView.findViewById( R.id.category_grid_screditscore );
        TextView tvPrice = (TextView) convertView.findViewById( R.id.category_grid_price );
        TextView tvVolumn = (TextView) convertView.findViewById( R.id.category_grid_volumn );

        if ("seller_credit_score".equals( orderBy )) {
            tvScreditScore.setText( Util.getCreditSpanndText( this, item.getSeller_credit_score( ) ) );
        } else {
            tvScreditScore.setText( null );

        }

        tvVolumn.setText( "销量：" + item.getVolume( ) );

        // tvTitle.setText( item.getTitle( ) );
        tvPrice.setText( item.getPrice( ) );
        img.setImageResource( R.drawable.zwt4 );
        ImageDownloader.getInstance( ).download( item.getPic_url( ), img );
        View v = convertView.findViewById( R.id.category_grid_itemLayout );
        v.setTag( position );
        v.setOnClickListener( CategoryActivity.this );
        return convertView;
    }

    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        id = getIntent( ).getIntExtra( "id", -1 );
        if (id == -1) {
            finish( );
            return;
        }
        setContentView( R.layout.category_detail_layout );
        runOnUiThread( new Runnable( ) {

            @Override
            public void run() {
                // TaobaokeDataManager.getInstance( CategoryActivity.this ).cancelAllTask( );
                initCreate( );

            }
        } );

    }

    private void initCreate() {

        parentCategory = new Category( );
        parentCategory.setId( id );
        parentCategory.setName( getIntent( ).getStringExtra( "title" ) );
        parentCategory.setIconUrl( getIntent( ).getStringExtra( "icon" ) );
        name = parentCategory.getName( );

        isParents = getIntent( ).getBooleanExtra( "showCategory", false );

        setTitle( name );
        btnLeft = (Button) findViewById( R.id.topBtn_1 );
        btnCenter = (Button) findViewById( R.id.topBtn_2 );
        btnRight = (Button) findViewById( R.id.topBtn_3 );
        btnLeft.setOnClickListener( this );
        btnCenter.setOnClickListener( this );
        btnRight.setOnClickListener( this );

        mListView = (RefreshListView) findViewById( R.id.detail_listviewCategory );
        mListView.setAdapter( adapter );

        mListView.setPullLoadEnable( true );
        mListView.startLoadMore( );
        mListView.setOnListViewListener( new IXListViewListener( ) {

            @Override
            public void onRefresh() {
                adapter.clear( );
                getData( PageNum, 0 );
                mListView.setPullLoadEnable( true );
                mListView.setPullRefreshEnable( true );
            }

            @Override
            public void onLoadMore() {
                getData( PageNum, adapter.getCount( ) );

            }
        } );
        // setContentView( mListView, new ViewGroup.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT ) );
        getData( PageNum, 0 );
        switicBtn( 0 );
        setRight2Background( R.drawable.btnbg );
        initDialog( false );
        showLoadingDialog( );
        setRight2Background( R.drawable.selector_category_list );
    }

    @Override
    public void onClickRight2Btn() {

        isGridView = !isGridView;
        mListView.setSelection( 0 );
        if (isGridView) {
            MyToast.showMsgShort( this, "已切换到视图模式" );
            setRight2Background( R.drawable.selector_category_list );
        } else {
            MyToast.showMsgShort( this, "已切换到列表模式" );
            setRight2Background( R.drawable.selector_category_grid );
        }
        runOnUiThread( new Runnable( ) {

            @Override
            public void run() {
                try {
                    Thread.sleep( 200 );
                } catch (InterruptedException e) {
                    // TODO Auto-generated cadtch block
                    e.printStackTrace( );
                }

                adapter.notifyDataSetChanged( );

            }
        } );
    }

    private LoadingDialog mLoadingDialog;

    protected void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog( this );
        }
        mLoadingDialog.show( );
    }

    private void switicBtn(int position) {

        btnLeft.setBackgroundResource( position == 0 ? R.drawable.rx_on : R.drawable.rx );
        btnCenter.setBackgroundResource( position == 1 ? (isAsc ? R.drawable.jgup_on : R.drawable.jgdown_on) : (isAsc ? R.drawable.jgup : R.drawable.jgdown) );
        btnRight.setBackgroundResource( position == 2 ? R.drawable.xy_on : R.drawable.xy );
        MobclickAgent.onEvent( CategoryActivity.this, "see_category", "orderBy " + orderBy + (isAsc ? " asc" : "desc") );
    }

    private final static int PageNum = 30;

    @Override
    public void onClick(View v) {
        switch (v.getId( )) {
        case R.id.popup_btnHome:
            if (!isParents) {
                getParentItemId( parentCategory.getId( ) );
                mDialog.dismiss( );
            } else {
                Toast.makeText( this, "亲，已经是该分类了", Toast.LENGTH_SHORT ).show( );
            }
            break;
        case R.id.topBtn_1:
            showLoadingDialog( );
            isAsc = false;
            orderBy = "volume";
            adapter.clear( );
            mListView.setSelection( 0 );
            getData( PageNum, 0 );
            switicBtn( 0 );

            break;
        case R.id.topBtn_2:
            showLoadingDialog( );
            if ("price".equals( orderBy )) {
                isAsc = !isAsc;
            } else {
                orderBy = "price";
            }
            adapter.clear( );
            mListView.setSelection( 0 );
            getData( PageNum, 0 );
            switicBtn( 1 );
            break;
        case R.id.topBtn_3:
            showLoadingDialog( );
            isAsc = false;
            orderBy = "seller_credit_score";
            adapter.clear( );
            mListView.setSelection( 0 );
            getData( PageNum, 0 );

            switicBtn( 2 );
            break;
        case R.id.category_grid_itemLayout:
        case R.id.item_category_detail:
            if (v.getTag( ) != null && v.getTag( ) instanceof Integer) {
                int index = (Integer) v.getTag( );
                if (index >= 0 && index < adapter.size( )) {
                    Holder holder = adapter.getItem( index );
                    Util.goToDetialActivity( CategoryActivity.this, holder );
                }
            }
            break;
        case R.id.popup_btnClose:
            mDialog.dismiss( );
            break;

        default:
            break;
        }

        super.onClick( v );
    }

    private String orderBy = "volume";
    private boolean isAsc = false;

    private void getData(int limit, int offset) {
        // TaobaokeDataManager.getInstance( this ).cancelAllTask( );
        if (isParents) {
            TaobaokeDataManager.getInstance( this ).getItemsByParentsId( id, orderBy, isAsc, offset, limit, mTaobaoDataCallBack );

        } else {
            TaobaokeDataManager.getInstance( this ).getItemsById( id, orderBy, isAsc, offset, limit, mTaobaoDataCallBack );
        }

    }

    private void getParentItemId(int id) {
        showLoadingDialog( );
        this.id = id;
        // orderBy = "volume";
        isAsc = false;
        isParents = true;
        adapter.clear( );
        // setTopBtnBackground( R.drawable.selector_back, R.drawable.selector_category_lookfor );
        getData( PageNum, 0 );

        setTitle( parentCategory.getName( ) );

        MobclickAgent.onEvent( this, "start_pCategoryItemAgain", "再次查看父分类 tic=" + id );

    }

    private void getItemId(int id) {
        showLoadingDialog( );
        this.id = id;
        // orderBy = "volume";
        isAsc = false;
        adapter.clear( );
        // setTopBtnBackground( R.drawable.selector_back, -1 );
        isParents = false;
        getData( PageNum, 0 );
    }

    private TaobaoDataCallBack<ParentsCategory> mTaobaoDataCallBack = new TaobaoDataCallBack<ParentsCategory>( ) {

        public void success(ParentsCategory t) {
            if (mLoadingDialog != null && mLoadingDialog.isShowing( ))// add code " mLoadingDialog.isShowing( )  " fix bug
                                                                      // "java.lang.IllegalArgumentException: View not attached to window manager"
                mLoadingDialog.dismiss( );
            if (t != null) {
                adapter.addAll( t.getList( ) );
                adapter.notifyDataSetChanged( );

            }

            mListView.stopLoadMore( );
            mListView.stopRefresh( );
            if (t.getList( ) == null || t.getList( ).size( ) < PageNum)
                mListView.setPullLoadEnable( false );

            if (t.isHasCategory( )) {
                popupAdapter.clear( );
                // popupAdapter.add( parentCategory );
                popupAdapter.addAll( t.getCategorys( ).getItems( ) );
                popupAdapter.notifyDataSetChanged( );
                name = t.getCategorys( ).getName( );
            }
            setTopBtnBackground( R.drawable.selector_back, R.drawable.selector_category_lookfor );
        }

        @Override
        public void failure(String msg) {
            Toast.makeText( CategoryActivity.this, msg, Toast.LENGTH_LONG ).show( );
            mListView.stopLoadMore( );
            mListView.stopRefresh( );
            if (mLoadingDialog != null)
                mLoadingDialog.dismiss( );
        }
    };

    @Override
    public boolean onClickLeftButton() {
        return false;
    }

    @Override
    public void onClickRightButton() {
        initDialog( true );

    }

    @Override
    protected void onDestroy() {
        if (mDialog != null) {
            mDialog.dismiss( );
            mDialog = null;
        }
        super.onDestroy( );
    }

    @Override
    public void setCurrentTitleString() {
        // setTopBtnBackground( R.drawable.selector_back, -1 );

    }

    OnItemClickListener mOnItemClickListener = new OnItemClickListener( ) {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Object obj = arg0.getItemAtPosition( arg2 );
            mDialog.dismiss( );
            /*
             * if (obj instanceof Categroys) { Categroys categroys = (Categroys) arg0.getItemAtPosition( arg2 ); initDialog( categroys ); } else
             */if (obj instanceof Category) {
                Category mCategory = (Category) arg0.getItemAtPosition( arg2 );

                // if (arg2 == 0) {
                // getParentItemId( mCategory.getId( ) );
                // } else {
                getItemId( mCategory.getId( ) );
                // }
                MobclickAgent.onEvent( CategoryActivity.this, "start_categroyItem", mCategory.getName( ) );
                setTitle( mCategory.getName( ) );

            }

        }
    };

    // boolean isFirst = true;

    @Override
    public void onStart() {
        // if (isFirst)
        // getData( 0, 40 );
        // isFirst = false;
        super.onStart( );
    }

    private MyBaseAdapter<Category> popupAdapter = new MyBaseAdapter<Category>( ) {

        @Override
        public View bindView(int position, Category t, View convertView) {
            if (convertView == null) {
                convertView = getLayoutInflater( ).inflate( R.layout.popup_category_item, null );
            }
            TextView tv = (TextView) convertView.findViewById( R.id.popup_item_name );
            ImageView img = (ImageView) convertView.findViewById( R.id.popup_item_image );
            tv.setText( t.getName( ) );

            ImageDownloader.getInstance( ).download( t.getIconUrl( ), img );
            return convertView;
        }
    };
    private MyNoTitleDialog mDialog;

    private void initDialog(boolean show) {
        if (mDialog == null) {
            mDialog = new MyNoTitleDialog( this );
            mDialog.setCanceledOnTouchOutside( true );
            View popupView = getLayoutInflater( ).inflate( R.layout.popup_category, null );
            mDialog.setContentView( popupView );
            ListView mListView = (ListView) popupView.findViewById( R.id.popup_listView );

            popupView.findViewById( R.id.popup_btnClose ).setOnClickListener( this );
            popupView.findViewById( R.id.popup_btnHome ).setOnClickListener( this );

            mListView.setOnItemClickListener( mOnItemClickListener );
            mListView.setAdapter( popupAdapter );
        }
        if (!show)
            return;
        if (popupAdapter.getCount( ) == 0) {
            Toast.makeText( this, "没有子分类，请稍后再试", Toast.LENGTH_SHORT ).show( );
            return;
        }
        ((ListView) mDialog.findViewById( R.id.popup_listView )).setSelection( 0 );
        TextView title = (TextView) mDialog.findViewById( R.id.popup_title );
        title.setText( name );
        WindowManager.LayoutParams params = mDialog.getWindow( ).getAttributes( );
        if (isParents) {
            mDialog.findViewById( R.id.popup_btnHome ).setVisibility( View.GONE );
        } else {
            mDialog.findViewById( R.id.popup_btnHome ).setVisibility( View.VISIBLE );
        }

        // 480*800

        int w = Math.min( getWindow( ).getWindowManager( ).getDefaultDisplay( ).getWidth( ), getWindow( ).getWindowManager( ).getDefaultDisplay( ).getHeight( ) );
        w = Math.max( 220, w - 50 );
        params.width = w;
        params.height = w + 100;
        mDialog.show( );
        MobclickAgent.onEvent( this, "category_dialog" );
    }

    @Override
    public void onRefresh() {
        showLoadingDialog( );
        adapter.clear( );
        getData( PageNum, 0 );
        mListView.setSelection( 0 );

    }

    @Override
    protected void onResume() {
        super.onResume( );
        MobclickAgent.onResume( this );
    }

    @Override
    protected void onPause() {
        super.onPause( );
        MobclickAgent.onPause( this );
    }
}
