package com.buy;

import java.io.File;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.ImageView;

import com.brunjoy.taose.R;
import com.buy.bases.IActivity;
import com.buy.data.TaobaoDataCallBack;
import com.buy.data.TaobaokeDataManager;
import com.buy.dialogs.InputPassWordDialog;
import com.buy.dialogs.InputPassWordDialog.CallBack;
import com.buy.holder.ExtraItem;
import com.buy.stores.Settings;
import com.buy.util.BitmapUtil;
import com.buy.util.MyLog;
import com.buy.util.MyToast;
import com.buy.util.Util;
import com.umeng.analytics.MobclickAgent;

public class LoadingActivity extends IActivity {
    private ImageView imgView;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        getWindow( ).setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView( R.layout.loading_layout );
        imgView = (ImageView) findViewById( R.id.loading_background );
        path = Settings.getInSettings( this ).getString( fileName, null );
        if (path != null) {
            File file = new File( BitmapUtil.rootCacheAppSavaPath + "/" + fileName );
            if (file.exists( )) {
                MyLog.e( "loading", "exists  System.currentTimeMillis( )=" + System.currentTimeMillis( ) );
                MyLog.e( "loading", "exists file.lastModified( )=" + file.lastModified( ) );
                if (System.currentTimeMillis( ) - file.lastModified( ) > 24 * 60 * 60 * 1000) {// 当longPic图片存放时间超过2天 删除
                    file.delete( );
                }
                try {
                    MyLog.e( "loading", "exists  file=" + file );
                    imgView.setImageBitmap( BitmapFactory.decodeFile( file.getPath( ) ) );
                } catch (OutOfMemoryError e) {
                    MyLog.e( "loading", "OutOfMemoryError pic t=" );
                }
            }
        }

        downPic( );
        pleasInputPassword( );
        com.umeng.common.Log.LOG = false;
        MobclickAgent.setDebugMode( false );
        if (MyLog.isDEBUG) {
            MyToast.showMsgLong( this, "此为 admin 版本 " );

        }
    }

    private final String fileName = "loadingPic";

    private void downPic() {

        TaobaokeDataManager.getInstance( LoadingActivity.this ).getLoadingPic( getWindowManager( ).getDefaultDisplay( ).getWidth( ),
                getWindowManager( ).getDefaultDisplay( ).getHeight( ), new TaobaoDataCallBack<ExtraItem>( ) {

                    public void success(final ExtraItem t) {
                        // MyLog.e( "loading", "long pic t=" + t + "  path=" + path );

                        if (t != null && t.getValue( ) != null) {
                            if (path != null && path.equals( t.getValue( ) )) {
                                MyLog.e( "loading", "一样不用下载 t=" + t );
                                return;
                            } else {
                                MyLog.e( "loading", "不一样 下载 t=" + t );
                                Settings.getInSettings( LoadingActivity.this ).putString( fileName, t.getValue( ) );
                                if (path != null) {
                                    boolean flat = new File( path ).exists( );

                                    MyLog.e( "loading", "删除上次的保存图片  =" + flat );
                                }
                                TaobaokeDataManager.getInstance( LoadingActivity.this ).downFile( t.getValue( ), fileName, new TaobaoDataCallBack<File>( ) {
                                    public void success(File t) {
                                        MyLog.e( "loading", "s下载图片完成" + t );
                                        if (t.exists( )) {
                                            try {
                                                if (imgView != null)
                                                    imgView.setImageBitmap( BitmapFactory.decodeFile( t.getPath( ) ) );
                                            } catch (OutOfMemoryError e) {
                                            }
                                        }
                                    }

                                    @Override
                                    public void failure(String msg) {
                                        MyLog.e( "loading", "s下载图片失败" + t );
                                    }
                                } );
                            }
                        }
                    }

                    @Override
                    public void failure(String msg) {

                    }
                } );

    }

    InputPassWordDialog dialog;

    protected void pleasInputPassword() {

        if (!TextUtils.isEmpty( Settings.getInSettings( this ).getPassWord( ) )) {
            if (dialog == null) {
                dialog = new InputPassWordDialog( this );
                dialog.setColseEnable( false );
                dialog.setTitle( R.string.prompt_input_openPwd );
                dialog.setOnCallBack( new CallBack( ) {

                    @Override
                    public void cancle() {
                        finish( );
                    }

                    @Override
                    public void bakeResult(String result) {
                        if (Settings.getInSettings( LoadingActivity.this ).getPassWord( ).equals( Util.getMD5Str( result ) )) {
                            gotoMain( );
                            dialog = null;
                        } else {
                            pleasInputPassword( );
                        }

                    }
                } );
            } else {
                dialog.setTitle( "输入密码错误，请重新输入" );
            }
            dialog.show( );
        } else {
            new Thread( new Runnable( ) {

                @Override
                public void run() {
                    try {
                        Thread.sleep( 1000 );
                        if (!Settings.getInSettings( LoadingActivity.this ).contains( "firstLoading" )) {
                            Settings.getInSettings( LoadingActivity.this ).putBoolean( "firstLoading", true );
                            Thread.sleep( 1000 );
                            gotoGuide( );
                        } else {

                            Thread.sleep( 3000 );
                            gotoMain( );
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace( );
                    }

                }
            } ).start( );

        }
    }

    private void gotoMain() {
        Intent intent = new Intent( );
        intent.setClass( LoadingActivity.this, MainActivity.class );
        startActivity( intent );
        finish( );

    }

    private void gotoGuide() {
        Intent intent = new Intent( );
        intent.setClass( LoadingActivity.this, GuideActivity.class );
        startActivity( intent );
        finish( );

    }

    @Override
    protected void onResume() {
        MobclickAgent.onResume( this );
        // MobclickAgent.setDebugMode( false );

        super.onResume( );
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPause( this );
        super.onPause( );
    }
}
