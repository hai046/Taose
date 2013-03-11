package com.buy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.buy.bases.IActivity;
import com.buy.util.MyToast;
import com.brunjoy.taose.R;
import com.umeng.analytics.MobclickAgent;

public class ActionFilterActivity extends IActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.action_filter_layout );
//        Intent it = getIntent( );
        MyToast.showMsgLong( this, "我藏的这么深 你竟然能通过这种途径发现了我们，你太厉害了 ，这个功能是个隐藏功能，后期将会开放"  );
        gotoMain();
//        findViewById( R.id.enableOpenWeb ).setOnClickListener( this );
        MobclickAgent.onEvent( this, "findByOtherQR" );
    }
    
    public void gotoMain() {
        Intent intent = new Intent( );
        intent.setClass( this, MainActivity.class );
        startActivity( intent );
        setResult( Activity.RESULT_OK );
        finish( );
    }
    
//    private LoadingDialog mDialog;
//
//    public void gotoPcBuy() {
//        String data = getIntent( ).getDataString( );
//        
//        //taose://connect_id:192122
//        String nummid=null;
//        if(data!=null)
//        {
//            String []values=data.split( ":" );
//            if(values.length>2)
//            {
//                nummid=values[2];
//            }
//        }
//        if(nummid==null)
//        {
//            MyToast.showMsgLong( this, "没有检索到数据，去应用里面看看找找吧！" );
//            gotoMain( );
//            return;
//        }
       
//        mDialog = new LoadingDialog( this );
//        mDialog.show( );
//        TaobaokeDataManager.getInstance( this ).doQRGet( Util.getQRUrl( "", com.brunjoy.Zxing.utils.ZxingDataManager.getInsance( ).getConnect_id( ) ),
//                new TaobaoDataCallBack<String>( ) {
//                    @Override
//                    public void failure(String msg) {
//
//                        MyLog.e( "onSelected", "failure  msg=" + msg );
//                        MyToast.showMsgLong( ActionFilterActivity.this, msg );
//                        mDialog.dismiss( );
//                        gotoMain( );
//
//                    }
//
//                    public void success(String t) {
//
//                        MyToast.showMsgLong( ActionFilterActivity.this, "已经连接到了PC购" );
//                        MyLog.e( "onSelected", "success  msg=" + t );
//                        mDialog.dismiss( );
//                        gotoMain( );
//                    }
//                } );
//    }

//    @Override
//    public void onClick(View arg0) {
//        switch (arg0.getId( )) {
//        case R.id.enableOpenWeb:
//            gotoPcBuy( );
//            break;
//
//        default:
//            break;
//        }
//
//    }

}
