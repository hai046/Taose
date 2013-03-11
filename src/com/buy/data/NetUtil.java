package com.buy.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.telephony.TelephonyManager;

import com.buy.util.MyLog;

public class NetUtil {
    public NetWorkCallback mNetWorkCallback;

    public void setNetWorkCallback(NetWorkCallback mNetWorkCallback) {
        this.mNetWorkCallback = mNetWorkCallback;
    }

    private void checkNetWork(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo( ConnectivityManager.TYPE_MOBILE );

        // wifi
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo( ConnectivityManager.TYPE_WIFI )/* .getState( ) */;
        
//        MyLog.i( "network", "  wifiInfo=" + wifiInfo ); 
//        MyLog.i( "network", "  mobileInfo=" + mobileInfo ); 
        // 如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接
        if (mobileInfo != null && mobileInfo.getState( ) == State.CONNECTED /* || mobile == State.CONNECTING */) {
            TelephonyManager manager = (TelephonyManager) mContext.getSystemService( Context.TELEPHONY_SERVICE );
            boolean isFastConn = isConnectionFast( manager.getNetworkType( ) );
            if (mNetWorkCallback != null) {
                mNetWorkCallback.netChanger( ConnectivityManager.TYPE_MOBILE, mobileInfo.getState( ), isFastConn );
            }
        }
        if (wifiInfo != null && wifiInfo.getState( ) == State.CONNECTED /* || wifi == State.CONNECTING */) {
//          
            if (mNetWorkCallback != null) {
                mNetWorkCallback.netChanger( ConnectivityManager.TYPE_WIFI, wifiInfo.getState( ), true );
            }
        }

    }

    public boolean isConnectionFast(int subType) {
//        MyLog.e( "network", "subType  =" + subType );
        switch (subType) {
        case TelephonyManager.NETWORK_TYPE_1xRTT:// 7
        case TelephonyManager.NETWORK_TYPE_GPRS:// 1
        case TelephonyManager.NETWORK_TYPE_EDGE:// 2
        case TelephonyManager.NETWORK_TYPE_CDMA:// 3电信2G是CDMA
        case TelephonyManager.NETWORK_TYPE_UNKNOWN:// 0

            // ~ 50-100 kbps
        case TelephonyManager.NETWORK_TYPE_IDEN:
            return false; // ~25 kbps

        case 12:// TelephonyManager.NETWORK_TYPE_EVDO_B:
        case 13:// TelephonyManager.NETWORK_TYPE_LTE:
            // ~ 5 Mbps
        case TelephonyManager.NETWORK_TYPE_EVDO_0:// 5
        case TelephonyManager.NETWORK_TYPE_EVDO_A:// 是电信3G
        case TelephonyManager.NETWORK_TYPE_HSDPA:// 8
            // return true; // ~ 2-14 Mbps
        case TelephonyManager.NETWORK_TYPE_HSPA:// 10
            // return true; // ~ 700-1700 kbps
        case TelephonyManager.NETWORK_TYPE_HSUPA:// 9

            // return true; // ~ 1-23 Mbps
        case TelephonyManager.NETWORK_TYPE_UMTS:// 3
            return true; // ~ 400-7000 kbps
            // NOT AVAILABLE YET IN API LEVEL 7
        default:
            return false;
        }

    }

    public NetUtil(Context mContext) {
        initListener( mContext );
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver( ) {

        @Override
        public void onReceive(Context context, Intent intent) {

            checkNetWork( context );
        }
    };
    private final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    private void initListener(Context mContext) {
        IntentFilter filter = new IntentFilter( );
        filter.addAction( CONNECTIVITY_CHANGE_ACTION );
        filter.setPriority( 1000 );
        mContext.registerReceiver( mBroadcastReceiver, filter );

    }

    public void clear(Context mContext) {
        try {
            mContext.unregisterReceiver( mBroadcastReceiver );
        } catch (Exception e) {
        }

    }
}
