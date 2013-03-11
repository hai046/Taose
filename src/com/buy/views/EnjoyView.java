package com.buy.views;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import com.buy.BBSActivity;
import com.buy.data.TaobaoDataCallBack;
import com.buy.data.TaobaokeDataManager;
import com.buy.games.compass.CompassGameActivity;
import com.buy.games.dice.DiceActvity;
import com.buy.holder.JokeHolder;
import com.buy.util.MyHandler;
import com.buy.util.MyHandler.HandlerCallBack;
import com.buy.util.RichText;
import com.brunjoy.taose.R;

public class EnjoyView extends BaseView implements ViewFactory, OnClickListener {

    // private ListView mListView;
    private LinearLayout mLayout;

    public EnjoyView(Context mContext) {
        super( mContext );
        setContentView( R.layout.enjoy_layout );
        mLayout = (LinearLayout) findViewById( R.id.enjoy_layout );
        // mLayout.addView( getHeardView( ) );
        // initData( );
        mLayout.addView( getJokeView( ) );
    }

    @Override
    public void onRefresh() {
        index = 0;
        if (list != null)
            list.clear( );
        getText( index );
        viewDown.setImageResource( R.drawable.down_on );
        super.onRefresh( );
    }

    private List<JokeHolder> list;
    private TaobaoDataCallBack<JokeHolder> taobaoDataCallBack = new TaobaoDataCallBack<JokeHolder>( ) {
        @Override
        public void success(List<JokeHolder> list) {
            // MyLog.e( "enjoy", "success=" + list );
            stopRefresh( );
            if (EnjoyView.this.list != null) {
                EnjoyView.this.list.addAll( list );
            } else {
                EnjoyView.this.list = list;
                getText( index );
            }
            if (list == null || list.size( ) == 0)// 如果为null,证明没有新的数据了，也就是最后一条笑话
            {
                MAXOFFSET = EnjoyView.this.list.size( );
                // MyLog.e( "enjoy", "MAXOFFSETMAXOFFSETMAXOFFSETMAXOFFSET=" + MAXOFFSET );
            }
            if (index == 0)
                getText( index );
        }

        @Override
        public void failure(String msg) {
            Toast.makeText( mContext, msg, Toast.LENGTH_SHORT ).show( );
        }
    };
    private int MAXOFFSET = Integer.MAX_VALUE;

    public void getdate() {
        TaobaokeDataManager.getInstance( mContext ).getJoke( null, (list == null ? index : list.size( )), 10, taobaoDataCallBack );
    }

    /**
     * 初始化header
     * 
     * @return
     */
    // private View getHeardView() {
    // View view = getLayoutInflater( ).inflate( R.layout.liked_header, null );
    // TextView tvTitle = (TextView) view.findViewById( R.id.headerTitle );
    // ImageView image = (ImageView) view.findViewById( R.id.headerImage );
    // tvTitle.setText( "情感秘笈，惊喜不断！" );
    // return view;
    // }

    private View JokeView;
    private TextSwitcher mTextSwitcher;
    private EditText tvJokeContent;
    // private float lastX = 0;
    private ImageButton viewDown;

    private View getJokeView() {
        JokeView = getLayoutInflater( ).inflate( R.layout.enjoy_joke_item, null );

        viewDown = (ImageButton) JokeView.findViewById( R.id.joke_down_btn );
        viewDown.setOnClickListener( this );
        JokeView.findViewById( R.id.joke_up_btn ).setOnClickListener( this );
        JokeView.findViewById( R.id.enjoy_joke_compass ).setOnClickListener( this );
        JokeView.findViewById( R.id.enjoy_joke_dic ).setOnClickListener( this );
        JokeView.findViewById( R.id.enjoy_joke_bbs ).setOnClickListener( this );

        tvJokeContent = (EditText) JokeView.findViewById( R.id.joke_content_tv );
        MyHandler.getInstance( ).addListener( mHandlerCallBack );
        // /* tvJokeContent.setOnTouchListener( new OnTouchListener( ) {
        //
        // @Override
        // public boolean onTouch(View v, MotionEvent event) {
        // GestureDetector mGestureDetector = new GestureDetector( mContext, new SimpleOnGestureListener( ) {
        //
        // @Override
        // public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // // MyLog.i( "onFling", " velocityX=" + velocityX );
        // return super.onFling( e1, e2, velocityX, velocityY );
        // }
        //
        // @Override
        // public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        // // MyLog.i( "onFling", " onScroll=" + distanceX );
        // if (lastX != 0) {
        //
        // if ((distanceX - lastX) > 50) {
        // MyHandler.getInstance( ).removeMessages( SCROOLL );
        // // MyLog.i( "onFling", " left=" + distanceX );
        // Message msg = Message.obtain( );
        // msg.what = SCROOLL;
        // msg.arg1 = -1;
        // MyHandler.getInstance( ).sendMessageDelayed( msg, 100 );
        // } else if ((distanceX - lastX) < -50) {
        // MyHandler.getInstance( ).removeMessages( SCROOLL );
        // // MyLog.i( "onFling", " right=" + distanceX );
        // Message msg = Message.obtain( );
        // msg.what = SCROOLL;
        // msg.arg1 = 1;
        // MyHandler.getInstance( ).sendMessageDelayed( msg, 100 );
        // }
        // }
        //
        // lastX = distanceX;
        //
        // return super.onScroll( e1, e2, distanceX, distanceY );
        // }
        //
        // } );
        // mGestureDetector.onTouchEvent( event );
        // return false;
        // }
        // } );*/

        mTextSwitcher = (TextSwitcher) JokeView.findViewById( R.id.joke_stchTv );
        mTextSwitcher.setFactory( this );
        Animation in = AnimationUtils.loadAnimation( mContext, android.R.anim.fade_in );
        Animation out = AnimationUtils.loadAnimation( mContext, android.R.anim.fade_out );
        mTextSwitcher.setInAnimation( in );
        mTextSwitcher.setOutAnimation( out );

        index = 0;
        getText( index );
        return JokeView;

    }

    @Override
    public void onDestroy() {
        MyHandler.getInstance( ).removeListener( mHandlerCallBack );
        super.onDestroy( );
    }

    private final int SCROOLL = 534;
    private MyHandler.HandlerCallBack mHandlerCallBack = new HandlerCallBack( ) {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case SCROOLL:
                // MyLog.e( "onFling", "ddsfa msg.arg1=" + msg.arg1 );
                // lastX = 0;
                if (msg.arg1 == 1) {
                    getText( index + 1 );
                    if (index >= 0) {
                        viewDown.setImageResource( R.drawable.selector_right );
                    }
                } else if (msg.arg1 == -1) {
                    if (index <= 0) {
                        viewDown.setImageResource( R.drawable.down_on );
                    }
                    getText( index - 1 );
                }
                break;

            default:
                break;
            }

        }
    };

    // private class MyHoder {
    // private String name, content;
    // private int imageResId, imgTip;
    //
    // public String getContent() {
    // return content;
    // }
    //
    // public int getImgTip() {
    // return imgTip;
    // }
    //
    // public MyHoder(String name, String content, int imageResId, int imgTip) {
    // this.name = name;
    // this.content = content;
    // this.imageResId = imageResId;
    // this.imgTip = imgTip;
    // }
    //
    // public String getName() {
    // return name;
    // }
    //
    // public int getImageResId() {
    // return imageResId;
    // }
    //
    // }

    @Override
    public View makeView() {
        TextView t = new TextView( mContext );
        t.setGravity( Gravity.CENTER_HORIZONTAL );
        t.setTextSize( 18 );
        return t;
    }

    public Date getDate(int offset) {
        Calendar cal = Calendar.getInstance( );
        cal.add( Calendar.DAY_OF_MONTH, -offset );
        cal.set( cal.get( Calendar.YEAR ), cal.get( Calendar.MONTH ), cal.get( Calendar.DAY_OF_MONTH ), 0, 0, 0 );
        return cal.getTime( );
    }

    public RichText getRichText(Date date) {
        RichText rt = new RichText( mContext );
        // Date date = getDate( offset );
        rt.addTextln( sdf.format( date ), mContext.getResources( ).getColor( R.color.red ), Typeface.BOLD );
        // rt.addText( sdfDate.format( date ), Color.RED, Typeface.NORMAL, 0.4f );
        return rt;
    }

    // private SimpleDateFormat sdfDay = new SimpleDateFormat( "dd" );
    private SimpleDateFormat sdf = new SimpleDateFormat( "yyyy.MM.dd  EEEE" );

    private void getText(int offset) {
        if (offset < 0) {
            Toast.makeText( mContext, "已经为最新内容了", Toast.LENGTH_SHORT ).show( );
            index = 0;

            return;
        }

        // MyLog.i( "enjoy", "index=" + index + "   MAXOFFSET=" + MAXOFFSET + "offset=" + offset );
        if (list == null || list.size( ) == 0) {
            getdate( );
            return;
        } else if (MAXOFFSET == offset) {
            Toast.makeText( mContext, "亲，你太厉害，笑话都让你看完了", Toast.LENGTH_SHORT ).show( );
            return;
        } else if (offset == list.size( ) - 1) {
            getdate( );

        }

        if (offset >= 0 && offset < list.size( )) {
            JokeHolder holder = list.get( offset );
            // MyLog.i( "enjoy", "holder=" + holder );
            mTextSwitcher.setText( getRichText( holder.getDate( ) ) );
            tvJokeContent.setText( holder.getContent( ) );
            index = offset;

        }

    }

    private static int index = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId( )) {
        case R.id.joke_down_btn:// click right
            if (index <= 0) {

                viewDown.setImageResource( R.drawable.down_on );
            }
            getText( index - 1 );
            break;
        case R.id.joke_up_btn:// left
            if (index >= 0) {
                viewDown.setVisibility( View.VISIBLE );
                viewDown.setImageResource( R.drawable.selector_right );
            }
            getText( index + 1 );
            break;
        case R.id.enjoy_joke_dic:
            Intent intent = new Intent( );
            intent.setClass( mContext, DiceActvity.class );
            mContext.startActivity( intent );

            break;
        case R.id.enjoy_joke_compass:
            intent = new Intent( );
            intent.setClass( mContext, CompassGameActivity.class );
            mContext.startActivity( intent );
            break;
        case R.id.enjoy_joke_bbs:
            intent = new Intent( );
            intent.setClass( mContext, BBSActivity.class );
            mContext.startActivity( intent );
            break;
        default:
            break;
        }

    }
}
