package com.buy.views;

import java.util.ArrayList;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.brunjoy.taose.R;

public class BuyHelpPageView extends BaseView implements OnClickListener {

    private ArrayList<View> list = new ArrayList<View>( );
    private ArrayList<Integer> listId = new ArrayList<Integer>( );

    public BuyHelpPageView(Context mContext) {
        super( mContext );
        ViewPager mViewPager = new ViewPager( mContext );

        setContentView( mViewPager );
        initDates( );

        mViewPager.setAdapter( new PagerAdapter( ) {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {

                return arg0 == arg1;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = list.get( position );
                ((ViewPager) container).addView( view, 0 );
                return view;
            }

            public void destroyItem(ViewGroup container, int position, Object object) {
                ((ViewPager) container).removeView( (View) object );
            }

            @Override
            public Parcelable saveState() {
                return null;
            }

            @Override
            public int getCount() {

                return list.size( );
            }
        } );

    }

    protected View getItemView(int position) {
        View view = getLayoutInflater( ).inflate( R.layout.loading_helpbuy_item, null );
        ImageView image = (ImageView) view.findViewById( R.id.loading_help_image );
        image.setImageResource( position );
        view.findViewById( R.id.loading_help_close ).setOnClickListener( this );
        return view;
    }

    private void initDates() {
        listId.add( R.drawable.gmbz1 );
        listId.add( R.drawable.gmbz2 );
        listId.add( R.drawable.gmbz3 );
        listId.add( R.drawable.gmbz4 );
        listId.add( R.drawable.gmbz5 );
        listId.add( R.drawable.gmbz6 );
        listId.add( R.drawable.gmbz7 );
        for (int id : listId) {

            list.add( getItemView( id ) );
        }
    }

    @Override
    public void onClick(View v) {
        finish( );

    }

}
