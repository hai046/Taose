package com.brunjoy.Zxing.utils;

import android.util.Log;

public class ZxingDataManager {

    private static ZxingDataManager mZxingDataManager;
    private String connect_id = null;
    private ZxingCallBack mZxingCallBack;

    public String getConnect_id() {
        return connect_id;
    }

    /**
     * 清除连接，例如当用户关闭浏览器的时候
     */
    public void clearConnect() {
        connect_id = null;
    }

    public synchronized static ZxingDataManager getInsance() {
        if (mZxingDataManager == null)
            mZxingDataManager = new ZxingDataManager( );
        return mZxingDataManager;
    }

    public void setOnLinstener(ZxingCallBack mZxingCallBack) {
        this.mZxingCallBack = mZxingCallBack;
    }

    public void callBack(String msg) {
        Log.i( "qrInfo", "qrInfo="+msg );
        if (msg.startsWith( "connect_id:" )) {
            try {
                connect_id = msg.substring( msg.indexOf( ':' ) + 1 );
            } catch (Exception e) {
                connect_id = null;
                msg = null;
            }
        } else  if (msg.startsWith( "taose://" )) {
            TaoseScheme mScheme=new TaoseScheme( msg );
            connect_id=mScheme.getValue("connect_id");
        }else {
            connect_id = null;
            msg = null;
        }

        if (mZxingCallBack != null) {
            mZxingCallBack.onCallBack( msg );
        }
    }

    public void onDestory() {
        mZxingCallBack = null;
        mZxingDataManager = null;
        connect_id = null;
    }

    public interface ZxingCallBack {
        public void onCallBack(String msg);

    }

}
