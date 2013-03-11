package com.buy.views;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.buy.bases.MyBaseAdapter;
import com.buy.holder.Holder;
import com.buy.stores.MySqlLiteDataBase;
import com.buy.util.ImageDownloader;
import com.buy.util.MyLog;
import com.buy.util.MyToast;
import com.buy.util.Util;
import com.brunjoy.taose.R;

public class EditLikedView extends BaseView {

    private ListView listview;

    private MyBaseAdapter<Holder> adapter = new MyBaseAdapter<Holder>( ) {

        @Override
        public View bindView(int position, Holder t, View view) {
            if (view == null)
                view = getLayoutInflater( ).inflate( R.layout.item_edit_liked_detail, null );
            ImageView image = (ImageView) view.findViewById( R.id.detail_image );
            TextView tvName = (TextView) view.findViewById( R.id.tv_name );
            TextView tvPrice = (TextView) view.findViewById( R.id.tv_price );
            // TextView tvLikedNum = (TextView) view.findViewById( R.id.tv_likedNum );
            TextView tvVolumn = (TextView) view.findViewById( R.id.tv_volume );
            TextView tvCreditScore = (TextView) view.findViewById( R.id.tv_creditScore );

            tvCreditScore.setText( Util.getCreditSpanndText( mContext, t.getSeller_credit_score( ) ) );
            tvVolumn.setText( mContext.getString( R.string.mounthVolume ) + t.getVolume( ) );
            ImageView imgStatu = (ImageView) view.findViewById( R.id.img_selectOk );
            if (selectedList.contains( t.getNum_iid( ) )) {
                imgStatu.setVisibility( View.VISIBLE );
            } else {
                imgStatu.setVisibility( View.GONE );
            }
            imgStatu.setTag( t.getNum_iid( ) );
            imgStatu.setOnClickListener( mOnClickListener );

            ImageButton btnBuy = (ImageButton) view.findViewById( R.id.btnBuy );
            btnBuy.setTag( position );
            btnBuy.setOnClickListener( mOnClickListener );

            image.setImageResource( R.drawable.zwt2 );
            ImageDownloader.getInstance( ).download( t.getPic_url( ), image );
            tvName.setText( t.getTitle( ) );
            tvPrice.setText( "￥" + t.getPrice( ) );
            tvVolumn.setText( mContext.getString( R.string.mounthVolume ) + t.getVolume( ) );

            view.setOnClickListener( mOnClickListener );
            view.setTag( position );
            // view.setTag( t.getNum_iid( ) );
            // view.setOnClickListener( mOnClickListener );
            return view;
        }
    };
    private boolean isEdit = true;

    public boolean isEdit() {
        return isEdit;
    }

    public void setAllEdit() {
        selectedList.clear( );
        if (isEdit) {
            for (Holder holder : adapter.getDateSet( )) {
                selectedList.add( holder.getNum_iid( ) );
            }
        }
        isEdit = !isEdit;
        adapter.notifyDataSetChanged( );
    }

    private ArrayList<String> selectedList = new ArrayList<String>( );

    // private View getHeardView() {
    // View view = getLayoutInflater( ).inflate( R.layout.liked_header, null );
    // TextView tvTitle = (TextView) view.findViewById( R.id.headerTitle );
    // ImageView image = (ImageView) view.findViewById( R.id.headerImage );
    // return view;
    // }
    @Override
    public void onResume() {
        selectedList.clear( );
        onStart( );
        super.onResume( );
    }

    public void onStart() {

        isEdit = true;
        new AsyncTask<Void, Void, ArrayList<Holder>>( ) {

            @Override
            protected ArrayList<Holder> doInBackground(Void... params) {

                return MySqlLiteDataBase.getInstance( mContext ).getItems( );
            }

            protected void onPostExecute(java.util.ArrayList<Holder> result) {
                if (result == null) {
                    adapter.clearNow( );
                    setLoadingTxt( R.string.store_nothad );
                    listview.setVisibility( View.GONE );
                } else {
                    adapter.clear( );
                    adapter.addAll( result );
                    hideLoadingView( );
                    listview.setVisibility( View.VISIBLE );
                }
                adapter.notifyDataSetChanged( );
            }
        }.execute( );

    }

    // Animation animation;

    public EditLikedView(Context mContext) {
        super( mContext );
        setContentView( R.layout.edit_like_layout );

        listview = (ListView) findViewById( R.id.edit_listView );
        listview.addHeaderView( getHeardView( ) );
        listview.setCacheColorHint( Color.TRANSPARENT );
        listview.setOnItemClickListener( new OnItemClickListener( ) {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {

                if (arg2 < 0 && arg2 >= adapter.getCount( ))
                    return;
                Holder holder = (Holder) arg0.getItemAtPosition( arg2 );
                if (holder == null)
                    return;
//                View image = view.findViewById( R.id.img_selectOk );
                // if (image != null)
                // image.startAnimation( animation );
                // MyLog.e( "edit", "image != null=====" + (image != null) );
                if (selectedList.contains( holder.getNum_iid( ) )) {
                    selectedList.remove( holder.getNum_iid( ) );
                } else {
                    selectedList.add( holder.getNum_iid( ) );
                }
                adapter.notifyDataSetChanged( );
            }
        } );
        // findViewById( R.id.select_all ).setOnClickListener( mOnClickListener );
        // findViewById( R.id.delete_all ).setOnClickListener( mOnClickListener );
        // findViewById( R.id.delete ).setOnClickListener( mOnClickListener );
        listview.setAdapter( adapter );
        // animation = AnimationUtils.loadAnimation( mContext, R.anim.rota_delete );
        // onStart( );
    }

    // start loading
//    private void setLoadingTxt(String text) {
//        TextView loadingTxt = (TextView) findViewById( R.id.loadingText );
//        loadingTxt.setText( text );
//
//    }

    private void setLoadingTxt(int text) {
        TextView loadingTxt = (TextView) findViewById( R.id.loadingText );
        loadingTxt.setText( text );
        findViewById( R.id.loading_layout ).setVisibility( View.VISIBLE );
    }

    private void hideLoadingView() {
        findViewById( R.id.loading_layout ).setVisibility( View.GONE );
    }

    // hide loading
    
    
    private View getHeardView() {
        View viewTop = getLayoutInflater( ).inflate( R.layout.liked_header, null );
        // TextView tvTitle = (TextView) view.findViewById( R.id.headerTitle );
        // 没有收藏的宝贝，快去看看挑几个吧
        // ImageView image = (ImageView) view.findViewById( R.id.headerImage );
        return viewTop;
    }

    private OnClickListener mOnClickListener = new OnClickListener( ) {

        @Override
        public void onClick(View v) {

            switch (v.getId( )) {
            case R.id.btnBuy:
                if (v.getTag( ) instanceof Integer) {
                    int index = (Integer) v.getTag( );
                    Holder holder = adapter.getItem( index );
                    Util.gotoBuyNow( mContext, holder.getClick_url( ), holder.getNum_iid( ) ,holder.getTitle( ));
                }
                break;
            case R.id.item_liked_edit:
                MyLog.e( "edit", "isEdit=" + isEdit );

                if (v.getTag( ) instanceof Integer) {
                    int index = (Integer) v.getTag( );
                    Holder holder = adapter.getItem( index );
                    Util.goToDetialActivity( mContext, holder );
                }

                break;
            case R.id.img_selectOk:
                // MyLog.e( "edit", "img_selectOk=" );
                if (v.getTag( ) instanceof String) {
                    String nummid = (String) v.getTag( );
                    // MyLog.e( "edit", "nummid=" + nummid );
                    if(MySqlLiteDataBase.getInstance( mContext ).open( )==false)
                    {
                        MyToast.showMsgShort( mContext, "操作失败" );
                    }
                    
                    MySqlLiteDataBase.getInstance( mContext ).deleteDataById( nummid );
                    MySqlLiteDataBase.getInstance( mContext ).close( );
                    selectedList.remove( nummid );
                }

                break;
            /*
             * case R.id.select_all: selectedList.clear( ); for (Holder holder : adapter.getDateSet( )) { selectedList.add( holder.getNum_iid( ) ); } break; case R.id.delete_all:
             * MySqlLiteDataBase.getInstance( mContext ).deleteAll( MySqlLiteDataBase.LIKED ); Toast.makeText( mContext, "已经为您清空所有数据", 1 ).show( ); selectedList.clear( ); break;
             * case R.id.delete: MySqlLiteDataBase.getInstance( mContext ).open( ); for (String id : selectedList) { MySqlLiteDataBase.getInstance( mContext ).deleteDataById( id );
             * } selectedList.clear( ); MySqlLiteDataBase.getInstance( mContext ).close( ); Toast.makeText( mContext, "删除成功", 1 ).show( ); break;
             */
            default:
                break;
            }

            onStart( );

        }
    };
}
