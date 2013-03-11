package com.buy.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.buy.bases.MyBaseAdapter;
import com.buy.bases.MyGallery;
import com.buy.util.ImageDownloader;
import com.buy.util.RichText;
import com.brunjoy.taose.R;

public class ScrollGalleryView extends BaseView {

  

    public ScrollGalleryView(final Context mContext) {
        super( mContext );
        setContentView( R.layout.scroll_gallery_layout );
        tvProgress = (TextView) findViewById( R.id.scollGallery_tvprogress );
        mGallery = (MyGallery) findViewById( R.id.scollGallery_gallery );
        mGallery.setAdapter( galleryAdapter );
        mGallery.setOnItemSelectedListener( new OnItemSelectedListener( ) {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                MyHandler.getInstance( ).removeMessages( SCROLLER );
//                MyHandler.getInstance( ).sendEmptyMessageDelayed( SCROLLER, 4000 );
                onPageSelected( arg2 );

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        } );

    }
    private TextView tvProgress;
    private MyGallery mGallery;
    private MyBaseAdapter<String> galleryAdapter = new MyBaseAdapter<String>( ) {

        private Gallery.LayoutParams params = new Gallery.LayoutParams( Gallery.LayoutParams.FILL_PARENT, Gallery.LayoutParams.FILL_PARENT );

        @Override
        public View bindView(int position, String t, View view) {
            ImageView image;
            if (view == null) {
                image = new ImageView( mContext );
                image.setScaleType( ImageView.ScaleType.CENTER_CROP );
//                image.setPadding( 15, 0, 15, 0 );
                image.setLayoutParams( params );
                
            } else {
                image = (ImageView) view;
            }
            ImageDownloader.getInstance( ).download( t, image );
            return image;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (list.size( ) > 0) {
                position = position % list.size( );
                return bindView( position, list.get( position ), convertView );
            } else {
                return null;
            }

        }

        public int getCount() {
            return list.size( );
        }
    };

    private List<String> list = new ArrayList<String>( );

    public void addData(String item)
    {
        list.add( item );
        galleryAdapter.notifyDataSetChanged( );
    }
    public void setDatas(List<String> list) {
        this.list.clear( );
        if (list != null) {
            this.list.addAll( list );
            onPageSelected( 0 );
            galleryAdapter.notifyDataSetChanged( );
        } else {
            return;
        }
        
//        MyHandler.getInstance( ).addListener( mHandlerCallBack );
//        MyHandler.getInstance( ).removeMessages( SCROLLER );
//        mGallery.setSelection( this.list.size( ) * 50000 );
//        MyHandler.getInstance( ).sendEmptyMessageDelayed( SCROLLER, 4000 );
    }

//    private static final int SCROLLER = 1215;

//    private HandlerCallBack mHandlerCallBack = new HandlerCallBack( ) {
//
//        @Override
//        public void handleMessage(Message msg) {
//
//            switch (msg.what) {
//
//            case SCROLLER:
//                int size = list.size( );
//                if (size > 0) {
//                    int current = (mGallery.getSelectedItemPosition( ) + 1) % size;
//                    onPageSelected( current );
//                    mGallery.setSelection( current );
//                }
//                break;
//            default:
//                break;
//            }
//
//        }
//    };

    @Override
    public void onPause() {
//        MyHandler.getInstance( ).removeMessages( SCROLLER );
        super.onPause( );
    }

    @Override
    public void onDestroy() {
//        MyHandler.getInstance( ).removeMessages( SCROLLER );
//        MyHandler.getInstance( ).removeListener( mHandlerCallBack );
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
//        MyHandler.getInstance( ).removeMessages( SCROLLER );
//        MyHandler.getInstance( ).sendEmptyMessageDelayed( SCROLLER, 4000 );
    }

}
