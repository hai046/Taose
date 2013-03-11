package com.buy.views;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.buy.CategoryActivity;
import com.buy.bases.MyBaseAdapter;
import com.buy.data.TaobaoDataCallBack;
import com.buy.data.TaobaokeDataManager;
import com.buy.holder.Category;
import com.buy.holder.Categroys;
import com.buy.util.ImageDownloader;
import com.brunjoy.taose.R;
import com.umeng.analytics.MobclickAgent;

public class CategoryView extends BaseView implements OnItemClickListener {

    private ListView mListView;
    private MyBaseAdapter<Categroys> adapter;

    public CategoryView(Context mContext) {
        super( mContext );
        setContentView( R.layout.category_layout );
        mListView = (ListView) findViewById( R.id.listviewCategory );
        adapter = new MyBaseAdapter<Categroys>( ) {

            @Override
            public View bindView(int position, Categroys t, View convertView) {
                if (convertView == null) {
                    convertView = getLayoutInflater( ).inflate( R.layout.categoryitem, null );
                }
                ImageView img = (ImageView) convertView.findViewById( R.id.img_categoryImage );
                TextView name = (TextView) convertView.findViewById( R.id.tv_categoryName );
                TextView explain = (TextView) convertView.findViewById( R.id.tv_categoryTitle );

                explain.setText( t.getExplain( ) );
                img.setImageResource( R.drawable.zwt2 );
                ImageDownloader.getInstance( ).download( t.getIconUrl( ), img );
                name.setText( t.getName( ) );
                return convertView;
            }
        };
        mListView.setAdapter( adapter );
        mListView.setOnItemClickListener( this );
        getData( 0, 40 );
    }

    @Override
    public void onRefresh() {
        adapter.clear( );
        getData( 0, 40 );
    }

    private TaobaoDataCallBack<Categroys> mTaobaoDataCallBack = new TaobaoDataCallBack<Categroys>( ) {

        @Override
        public void success(List<Categroys> list) {
            adapter.clear( );
            adapter.addAll( list );
            adapter.notifyDataSetChanged( );
            stopRefresh( );
        }

        @Override
        public void failure(String msg) {

        }
    };

    public void getData(int offset, int limit) {
        TaobaokeDataManager.getInstance( mContext ).getParentsCategroys( offset, limit, mTaobaoDataCallBack );
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Object obj = arg0.getItemAtPosition( arg2 );
        /*
         * if (obj instanceof Categroys) { Categroys categroys = (Categroys) arg0.getItemAtPosition( arg2 ); initDialog( categroys ); } else
         */if (obj instanceof Category) {
            Category mCategory = (Category) arg0.getItemAtPosition( arg2 );
            Intent intent = new Intent( );
            intent.setClass( mContext, CategoryActivity.class );
            intent.putExtra( "id", mCategory.getId( ) );
            intent.putExtra( "title", mCategory.getName( ) );
            intent.putExtra( "icon", mCategory.getIconUrl( ) );
            intent.putExtra( "showCategory", true );
            mContext.startActivity( intent );
            MobclickAgent.onEvent( mContext, "start_categroyPItem", mCategory.getName( ) );
        }

    }

    // boolean isFirst = true;

    @Override
    public void onStart() {
       
        super.onStart( );
      
    }
    
   

}
