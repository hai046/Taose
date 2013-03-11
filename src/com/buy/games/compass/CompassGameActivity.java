package com.buy.games.compass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buy.bases.IActivity;
import com.buy.games.opengl.compass.model.Mesh.CallBack;
import com.buy.games.opengl.compass.model.SimplePlane;
import com.buy.util.BitmapUtil;
import com.buy.util.MyNoTitleDialog;
import com.brunjoy.taose.R;
import com.umeng.analytics.MobclickAgent;

public class CompassGameActivity extends IActivity {
    protected static final int GAME_DICE_BTNSTATUS = 102;
    SimplePlane plane;
    Button btnStatus;
    GLSurfaceView view;
    View agaginView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        getWindow( ).setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_BLUR_BEHIND );
        setContentView( R.layout.game_compass_layout );
        agaginView = findViewById( R.id.game_dice_again );
        agaginView.setOnClickListener( mClickListener );
        findViewById( R.id.game_dice_back ).setOnClickListener( mClickListener );
        plane = new SimplePlane( 1, 1 );
        plane.setOnCallBack( new CallBack( ) {

            @Override
            public void scroolTo(final int index) {
                runOnUiThread( new Runnable( ) {

                    @Override
                    public void run() {
                        inintDialog( index, true );
                    }
                } );

            }
        } );

        runOnUiThread( new Runnable( ) {

            @Override
            public void run() {
                OpenGLRenderer renderer = new OpenGLRenderer( );
                view = (GLSurfaceView) findViewById( R.id.game_dice_surfaceView );
                view.setZOrderOnTop( true );
                view.getHolder( ).setFormat( PixelFormat.TRANSPARENT );
                view.setEGLConfigChooser( 8, 8, 8, 8, 16, 0 );
                view.setRenderer( renderer );
                renderer.addMesh( plane );
                initCenterButton( );
                inintDialog( 0, false );
                plane.z = 1.2f;
                int height = getWindowManager( ).getDefaultDisplay( ).getHeight( );
                if (height > 320) {

                    if (!plane.loadBitmap( BitmapUtil.formatScaleBitmap( BitmapFactory.decodeResource( getResources( ), R.drawable.dipan ), 512, 512 ) )) {
                        plane.loadBitmap( BitmapUtil.formatScaleBitmap( BitmapFactory.decodeResource( getResources( ), R.drawable.dipan ), 256, 256 ) );
                    }
                } else {
                    plane.loadBitmap( BitmapUtil.formatScaleBitmap( BitmapFactory.decodeResource( getResources( ), R.drawable.dipan ), 256, 256 ) );
                }

            }
        } );

    }

    // private PopupWindow popupWindowDetail = null;
    private MyNoTitleDialog mMyNoTitleDialog;

    private void inintDialog(int index, boolean show) {
        // int offsetW =0;// (int) (btnStatus.getWidth( ) * 0.25);
        if (mMyNoTitleDialog == null) {
            View popupView = getLayoutInflater( ).inflate( R.layout.game_compass_dialog, null );
            mMyNoTitleDialog = new MyNoTitleDialog( this, R.style.DialogtransparentTheme );
            mMyNoTitleDialog.setContentView( popupView/*
                                                       * , new ViewGroup.LayoutParams( (int) (getWindowManager( ).getDefaultDisplay( ).getWidth( ) * 0.5) - offsetW,
                                                       * LinearLayout.LayoutParams.WRAP_CONTENT )
                                                       */);
            // mMyNoTitleDialog.setCanceledOnTouchOutside( true );
            mMyNoTitleDialog.setCancelable( true );
            mMyNoTitleDialog.setOnDismissListener( new OnDismissListener( ) {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    Animation mAnimation = AnimationUtils.loadAnimation( CompassGameActivity.this, R.anim.fade_in );
                    // agaginView.setAnimation( mAnimation );
                    agaginView.setVisibility( View.VISIBLE );
                    mAnimation.setFillAfter( true );
                    // agaginView.setAnimation( mAnimation );
                    agaginView.startAnimation( mAnimation );
                    // mAnimation.start( );

                }
            } );
            WindowManager.LayoutParams params = mMyNoTitleDialog.getWindow( ).getAttributes( );
            int centerX = (int) (getWindowManager( ).getDefaultDisplay( ).getWidth( ) * 0.5);
            params.width = centerX;
            params.x = centerX;
        }
        if (show == false)
            return;
        ImageButton btn = (ImageButton) mMyNoTitleDialog.findViewById( R.id.btnImg_close );
        TextView tvContent = (TextView) mMyNoTitleDialog.findViewById( R.id.content );
        TextView tvName = (TextView) mMyNoTitleDialog.findViewById( R.id.name );
        ImageView image = (ImageView) mMyNoTitleDialog.findViewById( R.id.image );
        try {
            InputStream is = getAssets( ).open( "luopan/zs" + (index + 1) + ".png", AssetManager.ACCESS_STREAMING );
            image.setVisibility( View.VISIBLE );
            if (is != null) {
                image.setImageBitmap( BitmapFactory.decodeStream( is ) );
            }
            is.close( );

            tvContent.setText( null );
            is = getAssets( ).open( "luopan/" + (index + 1) + ".txt", AssetManager.ACCESS_STREAMING );
            StringBuilder sb = new StringBuilder( );
            BufferedReader br = new BufferedReader( new InputStreamReader( is ) );
            String temp = null;
            tvName.setText( br.readLine( ) );
            while ((temp = br.readLine( )) != null) {
                sb.append( "\t" + temp );
            }
            tvContent.setText( sb );
            br.close( );
            is.close( );
        } catch (IOException e) {
            image.setVisibility( View.GONE );
            e.printStackTrace( );
            // MyLog.e( "err", e.getMessage( ) + "" );

        }

        btn.setOnClickListener( mClickListener );
        mMyNoTitleDialog.show( );
        // popupWindowDetail.showAtLocation( view, Gravity.TOP | Gravity.RIGHT, -offsetW, 0 );
    }

    private void initCenterButton() {

        btnStatus = new Button( this );
        btnStatus.setOnClickListener( mClickListener );
        btnStatus.setId( GAME_DICE_BTNSTATUS );
        btnStatus.setBackgroundResource( R.drawable.selector_dice_start );

        WindowManager.LayoutParams params = new WindowManager.LayoutParams( WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_BASE_APPLICATION, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT );
        WindowManager m = getWindowManager( );

        m.addView( btnStatus, params );

    }

    @Override
    protected void onDestroy() {
        if (btnStatus != null)
            getWindowManager( ).removeView( btnStatus );
        if (mMyNoTitleDialog != null)
            mMyNoTitleDialog.dismiss( );
        super.onDestroy( );
    }

    private boolean isStart = false;
    private OnClickListener mClickListener = new OnClickListener( ) {

        @Override
        public void onClick(View v) {
            switch (v.getId( )) {
            case R.id.game_dice_back:
                finish( );
                return;
            case R.id.btnImg_close:
                mMyNoTitleDialog.dismiss( );

                return;
            case R.id.game_dice_again:
                if (isStart) {
                    return;
                }
            case GAME_DICE_BTNSTATUS:
                if (!isStart && plane.rzRate > 0) {
                    Toast.makeText( CompassGameActivity.this, "亲，只有停止后才能开始", Toast.LENGTH_SHORT ).show( );
                    return;
                }
                isStart = !isStart;
                break;
            default:
                break;
            }
            if (isStart) {
                plane.isResult = true;
                plane.rzRate = 10;
                plane.isStart = true;
                btnStatus.setBackgroundResource( R.drawable.selector_dice_stop );

                Animation mAnimation = AnimationUtils.loadAnimation( CompassGameActivity.this, R.anim.fade_out );
                mAnimation.setFillAfter( true );
                mAnimation.start( );
                agaginView.startAnimation( mAnimation );
                // agaginView.setVisibility( View.GONE );
                if (mMyNoTitleDialog != null) {
                    mMyNoTitleDialog.dismiss( );
                }

                // TRY {
                // // IF (!ISMUTE)
                // SOUNDMANAGER.GETINSTANCE( ).START( GETASSETS( ).OPENFD( "SOUNDS/1170.MP3" ) );
                // } CATCH (IOEXCEPTION E) {
                // E.PRINTSTACKTRACE( );
                // }

            } else {
                btnStatus.setBackgroundResource( R.drawable.selector_dice_start );

                plane.isStart = false;
            }

        }
    };

    @Override
    protected void onResume() {
        DisplayMetrics dm = new DisplayMetrics( );
        getWindowManager( ).getDefaultDisplay( ).getMetrics( dm );

        super.onResume( );
        MobclickAgent.onResume( this );
    }

    @Override
    protected void onPause() {
        super.onPause( );
        view.onPause( );

        finish( );
        MobclickAgent.onPause( this );
    }

}
