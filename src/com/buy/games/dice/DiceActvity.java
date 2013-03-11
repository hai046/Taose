/**
 * Copyright 2010 Per-Erik Bergman (per-erik.bergman@jayway.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.buy.games.dice;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.brunjoy.buy.sound.SoundManager;
import com.buy.games.dice.Mesh.CallBack;
import com.buy.games.dice.MySensorManager.AccelerometerListener;
import com.buy.stores.Settings;
import com.buy.util.BitmapUtil;
import com.buy.util.MyLog;
import com.brunjoy.taose.R;
import com.umeng.analytics.MobclickAgent;

/**
 * This class is the setup for the Tutorial part VI located at: http://blog.jayway.com/
 * 
 * @author Per-Erik Bergman (per-erik.bergman@jayway.com)
 * 
 */
public class DiceActvity extends Activity {
    // private String TAG = "DiceActvity";
    private SimplePlane plane;
    // ImageView leftImg, rightImg;
    private View contentLayout;
    private ImageView diceLeft, diceRight;
    private ImageButton btnClose, btnAgain, btnVolumn;
    private Animation mAnimationIn, mAnimationDiceIn;
    private Vibrator vib;
    private OpenGLDiceRenderer renderer;
    private boolean isMute;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        getWindow( ).setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView( R.layout.dice_main );

        btnClose = (ImageButton) findViewById( R.id.diceCloseBtnImg );
        btnAgain = (ImageButton) findViewById( R.id.diceAgainBtnImg );
        btnVolumn = (ImageButton) findViewById( R.id.diceVolumn );
        isMute = Settings.getInSettings( this ).getBoolean( "dice_volumn", false );
        notifyVolumnChange( );
        btnVolumn.setOnClickListener( mOnClickListener );
        btnClose.setOnClickListener( mOnClickListener );
        btnAgain.setOnClickListener( mOnClickListener );
        contentLayout = findViewById( R.id.dic_content );
        diceLeft = (ImageView) findViewById( R.id.dic_leftImage );
        diceRight = (ImageView) findViewById( R.id.dic_RightImage );
        GLSurfaceView view = (GLSurfaceView) findViewById( R.id.dic_glSurfaceView );
        view.setZOrderOnTop( true );
        view.setEGLConfigChooser( 8, 8, 8, 8, 16, 0 );
        view.getHolder( ).setFormat( PixelFormat.TRANSPARENT );
        renderer = new OpenGLDiceRenderer( );
        view.setRenderer( renderer );
        plane = new SimplePlane( 1, 1 );

        int height = getWindowManager( ).getDefaultDisplay( ).getHeight( );
        if (height > 320) {
            plane.loadBitmap( BitmapUtil.formatScaleBitmap( BitmapFactory.decodeResource( getResources( ), R.drawable.sezizhong ), 512, 512 ) );
        } else {
            plane.loadBitmap( BitmapUtil.formatScaleBitmap( BitmapFactory.decodeResource( getResources( ), R.drawable.sezizhong ), 256, 256 ) );
        }
        renderer.addMesh( plane );
        mAnimationIn = AnimationUtils.loadAnimation( this, R.anim.fade_in );
        mAnimationDiceIn = AnimationUtils.loadAnimation( this, R.anim.fade_dicein );
        MySensorManager.getInstance( getApplicationContext( ) ).setAccelerometerListener( mAccelerometerListener );
        vib = (Vibrator) getSystemService( VIBRATOR_SERVICE );

        initView( );
        plane.setOnCallBack( new CallBack( ) {

            @Override
            public void isScroolToLeft(boolean flag) {
                // MyLog.e( TAG, "Vibrator" );
                vib.vibrate( 30 );
            }
        } );
        maxV = 400;
        isRunning = false;
    }

    private void notifyVolumnChange() {
        if (!isMute) {
            btnVolumn.setImageResource( R.drawable.selector_volumn );

        } else {
            btnVolumn.setImageResource( R.drawable.selector_volumn_mute );
            SoundManager.getInstance( ).pause( );
        }

    }

    private AccelerometerListener mAccelerometerListener = new AccelerometerListener( ) {

        public void callBack(float x, float y, float z) {
            float v = x * y * z;
            v = Math.abs( v );
            if (v > maxV) {
                // maxV = v;
                if (!isRunning) {
                    mHandler.sendEmptyMessage( START );
                } else {
                }

            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause( );
        MobclickAgent.onPause( this );
        finish( );
    }

    private float maxV = 500;
    private ImageView imagePlayTip;

    private void initView() {

        imagePlayTip = new ImageView( this );
        imagePlayTip.setImageResource( R.drawable.yaosezi );
        WindowManager.LayoutParams params = new WindowManager.LayoutParams( WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_BASE_APPLICATION, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT );
        params.gravity = Gravity.CENTER;

        // getWindow( ).addContentView( imagePlayTip, params );
        imagePlayTip.setPadding( 0, 124, 0, 0 );
        getWindowManager( ).addView( imagePlayTip, params );
    }

    private final OnClickListener mOnClickListener = new OnClickListener( ) {

        public void onClick(View v) {
            switch (v.getId( )) {
            case R.id.diceVolumn:

                isMute = !isMute;
                notifyVolumnChange( );
                break;
            case R.id.diceCloseBtnImg:
                finish( );
                break;
            case R.id.diceAgainBtnImg:
                if (v.getVisibility( ) == View.VISIBLE) {
                    plane.reset( );
                    btnAgain.setVisibility( View.GONE );
                    imagePlayTip.setVisibility( View.VISIBLE );
                    contentLayout.setVisibility( View.GONE );
                }
                break;
            default:
                break;
            }

        }
    };
    private final static int START = 1;
    private final static int STOP = 2;
    private final static int STOP_RUNNING = 3;
    private boolean isRunning = false;
    private Handler mHandler = new Handler( ) {
        @Override
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
            case START:
                removeMessages( START );
                if (!renderer.isCreated)
                    return;
                isRunning = true;
                sendEmptyMessageDelayed( STOP, 1700 );
                start( );

                break;
            case STOP:
                removeMessages( STOP );
                start( );
                break;
            case STOP_RUNNING:
                isRunning = false;
                break;
            default:
                break;
            }
        }
    };

    private void start() {
        plane.start( );
        if (!plane.start) {
            setBitmapBg( diceLeft, true );
            setBitmapBg( diceRight, false );
            contentLayout.setVisibility( View.VISIBLE );
            contentLayout.startAnimation( mAnimationDiceIn );
            btnAgain.setVisibility( View.VISIBLE );
            mAnimationIn.setDuration( 500 );
            btnAgain.startAnimation( mAnimationIn );
            mHandler.sendEmptyMessageDelayed( STOP_RUNNING, 500 );
            SoundManager.getInstance( ).pause( );
        } else {
            btnAgain.setVisibility( View.GONE );
            imagePlayTip.setVisibility( View.GONE );
            contentLayout.setVisibility( View.GONE );
            try {
                if (!isMute)
                    SoundManager.getInstance( ).start( getAssets( ).openFd( "sounds/dicing.wav" ) );
            } catch (IOException e) {
                e.printStackTrace( );
//                MyLog.e( "dic", "IOException=" + e );
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_MENU:
            mHandler.sendEmptyMessage( START );
            break;

        default:
            break;
        }
        return super.onKeyDown( keyCode, event );
    }

    private void setBitmapBg(ImageView view, boolean isLeft) {
        Random rand = new Random( );
        int leftIndex = rand.nextInt( 6 );
        try {
            InputStream is = getAssets( ).open( "dices/sd" + (isLeft ? "1" : "2") + "_" + (leftIndex + 1) + ".png", AssetManager.ACCESS_STREAMING );

            view.setImageBitmap( BitmapFactory.decodeStream( is ) );
            if (is != null)
                is.close( );
        } catch (IOException e) {

            e.printStackTrace( );
        }

    }

    @Override
    protected void onDestroy() {
        MySensorManager.getInstance( getApplicationContext( ) ).removeListener( );
        if (imagePlayTip != null) {
            getWindowManager( ).removeView( imagePlayTip );
        }
        mHandler.removeMessages( START );
        mHandler.removeMessages( STOP );
        mHandler.removeMessages( STOP_RUNNING );
        SoundManager.getInstance( ).stop( );
        Settings.getInSettings( DiceActvity.this ).putBoolean( "dice_volumn", isMute );
        super.onDestroy( );
    }

    @Override
    protected void onResume() {
        super.onResume( );
        MobclickAgent.onResume( this );
        super.onResume( );
    }
}