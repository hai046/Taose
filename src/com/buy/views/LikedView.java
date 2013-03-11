package com.buy.views;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.buy.bases.MyBaseAdapter;
import com.buy.holder.Holder;
import com.buy.stores.MySqlLiteDataBase;
import com.buy.util.ImageDownloader;
import com.buy.util.Util;
import com.brunjoy.taose.R;

public class LikedView extends BaseView {

    private ListView listview;

    private MyBaseAdapter<Holder> adapter = new MyBaseAdapter<Holder>( ) {

        @Override
        public View bindView(int position, Holder t, View view) {
            if (view == null)
                view = getLayoutInflater( ).inflate( R.layout.item_liked_detail, null );
            ImageView image = (ImageView) view.findViewById( R.id.detail_image );
            TextView tvName = (TextView) view.findViewById( R.id.tv_name );
            TextView tvPrice = (TextView) view.findViewById( R.id.tv_price );
            TextView tvLikedNum = (TextView) view.findViewById( R.id.tv_likedNum );
            TextView tvVolumn = (TextView) view.findViewById( R.id.tv_volume );

            Button btnBuy = (Button) view.findViewById( R.id.btn_buy );
            image.setImageResource( R.drawable.zwt1 );
            ImageDownloader.getInstance( ).download( t.getPic_url( ), image );
            tvName.setText( t.getTitle( ) );
            tvPrice.setText( t.getPrice( ) );
            tvLikedNum.setText( "12" );
            tvVolumn.setText( t.getVolume( ) );

            btnBuy.setTag( position );
            View v = view.findViewById( R.id.item_liked_detail );
            v.setTag( position );
            v.setOnClickListener( mOnClickListener );
            btnBuy.setOnClickListener( mOnClickListener );
            return view;
        }
    };

    private View getHeardView() {
        View view = getLayoutInflater( ).inflate( R.layout.liked_header, null );
        // TextView tvTitle = (TextView) view.findViewById( R.id.headerTitle );
        // ImageView image = (ImageView) view.findViewById( R.id.headerImage );
        return view;
    }

    public void onStart() {

        new AsyncTask<Void, Void, ArrayList<Holder>>( ) {

            @Override
            protected ArrayList<Holder> doInBackground(Void... params) {

                return MySqlLiteDataBase.getInstance( mContext ).getItems( );
            }

            protected void onPostExecute(java.util.ArrayList<Holder> result) {
                if (result == null) {
                    adapter.clearNow( );
                } else {
                    adapter.clear( );
                    adapter.addAll( result );
                }
                adapter.notifyDataSetChanged( );

            }
        }.execute( );

    }

    public LikedView(Context mContext) {
        super( mContext );
        listview = new ListView( mContext );
        listview.setDivider( mContext.getResources( ).getDrawable( R.drawable.hbfgx ) );

        listview.setBackgroundColor( mContext.getResources( ).getColor( R.color.contentbg ) );
        listview.addHeaderView( getHeardView( ) );
        listview.setCacheColorHint( Color.TRANSPARENT );
        setContentView( listview, new ViewGroup.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT ) );
        listview.setAdapter( adapter );
    }

    private OnClickListener mOnClickListener = new OnClickListener( ) {

        @Override
        public void onClick(View v) {
            Object obj = v.getTag( );
            if (obj != null && obj instanceof Integer) {
                int index = (Integer) obj;
                Holder holder = adapter.getItem( index );
                switch (v.getId( )) {
                case R.id.item_liked_detail:
                    Util.goToDetialActivity( mContext, holder );
                    break;
                case R.id.btn_buy:
                    Util.gotoBuyNow( mContext, holder.getClick_url( ), holder.getNum_iid( ), holder.getTitle( ) );
                    break;

                default:
                    break;
                }
            }
        }
    };
}
