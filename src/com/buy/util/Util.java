package com.buy.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;

import com.brunjoy.Zxing.activity.CaptureActivity;
import com.brunjoy.Zxing.utils.ZxingDataManager;
import com.buy.ItemDetailActivity;
import com.buy.data.TaobaoDataCallBack;
import com.buy.data.TaobaokeDataManager;
import com.buy.holder.Holder;
import com.buy.settings.AboutMeActivity;
import com.buy.settings.DisclaimerActivity;
import com.buy.settings.HelpYouActivity;
import com.buy.settings.SettingsActivity;
import com.buy.settings.SuggestActivity;
import com.buy.stores.MySqlLiteDataBase;
import com.buy.stores.Settings;
import com.buy.web.BuyWebActivity;
import com.buy.web.WebActivity;
import com.brunjoy.taose.R;
import com.umeng.analytics.MobclickAgent;

public class Util {

    public static CharSequence getCreditSpanndText(Context context, int credit) {
        RichText rt = new RichText( context );
        TaoboCredit mTaoboCredit = getCreditLevel( credit );
        int resId = R.drawable.tmall_icon_user_level_1;
        if (mTaoboCredit == TaoboCredit.ONE_LEVEL) {
            resId = R.drawable.level1;
        } else if (mTaoboCredit == TaoboCredit.TWO_LEVEL) {
            resId = R.drawable.level2;
        } else if (mTaoboCredit == TaoboCredit.THREE_LEVEL) {
            resId = R.drawable.level3;
        } else if (mTaoboCredit == TaoboCredit.FOUR_LEVEL) {
            resId = R.drawable.level4;
        }
        Bitmap bitmap = BitmapFactory.decodeResource( context.getResources( ), resId );
        for (int i = 0; i < mTaoboCredit.getNum( ); i++) {
            rt.addImage( bitmap );
        }
        return rt;
    }

    public static TaoboCredit getCreditLevel(int credit) {
        TaoboCredit mTaoboCredit = null;
        if (credit < 1) {
            mTaoboCredit = TaoboCredit.ONE_LEVEL;
            mTaoboCredit.setNum( 0 );
        }// //12
        switch ((credit + 4) / 5) {

        case 1:
            mTaoboCredit = TaoboCredit.ONE_LEVEL;
            break;
        case 2:
            mTaoboCredit = TaoboCredit.TWO_LEVEL;
            break;
        case 3:
            mTaoboCredit = TaoboCredit.THREE_LEVEL;
            break;
        case 4:
            mTaoboCredit = TaoboCredit.FOUR_LEVEL;
            break;

        default:
            mTaoboCredit = TaoboCredit.ONE_LEVEL;
            mTaoboCredit.setNum( 0 );
            break;
        }
        mTaoboCredit.setNum( (credit + 4) % 5 + 1 );

        return mTaoboCredit;
    }

    public static CharSequence getUserCreditSpanndText(Context context, long credit) {
        RichText rt = new RichText( context );
        TaoboCredit mTaoboCredit = getUserCreditLevel( credit );
        int resId = R.drawable.tmall_icon_user_level_1;
        if (mTaoboCredit == TaoboCredit.ONE_LEVEL) {
            resId = R.drawable.tmall_icon_user_level_1;
        } else if (mTaoboCredit == TaoboCredit.TWO_LEVEL) {
            resId = R.drawable.tmall_icon_user_level_2;
        } else if (mTaoboCredit == TaoboCredit.THREE_LEVEL) {
            resId = R.drawable.tmall_icon_user_level_3;
        } else if (mTaoboCredit == TaoboCredit.FOUR_LEVEL) {
            resId = R.drawable.tmall_icon_user_level_4;
        }
        Bitmap bitmap = BitmapFactory.decodeResource( context.getResources( ), resId );
        for (int i = 0; i < mTaoboCredit.getNum( ); i++) {
            rt.addImage( bitmap );
            // rt.addText( " " );
        }
        return rt;
    }

    public static TaoboCredit getUserCreditLevel(long credit) {
        TaoboCredit mTaoboCredit = null;
        if (credit < 4) {
            mTaoboCredit = TaoboCredit.ONE_LEVEL;
            mTaoboCredit.setNum( 0 );
        } else if (credit > 10000000) {
            mTaoboCredit = TaoboCredit.FOUR_LEVEL;
            mTaoboCredit.setNum( 5 );
        } else {
            int[] credits = { 10, 40, 90, 150, 250, 500, 1000, 2000, 5000, 10000, 20000, 50000, 100000, 200000, 500000, 1000000, 2000000, 5000000, 10000000, -1 };
            int i = 0;
            do {
                if (credit <= credits[i]) {

                    break;
                }
                i++;
            } while (i < credits.length - 1);
            switch (i / 5 + 1) {
            case 1:
                mTaoboCredit = TaoboCredit.ONE_LEVEL;
                break;
            case 2:
                mTaoboCredit = TaoboCredit.TWO_LEVEL;
                break;
            case 3:
                mTaoboCredit = TaoboCredit.THREE_LEVEL;
                break;
            case 4:
                mTaoboCredit = TaoboCredit.FOUR_LEVEL;
                break;

            default:
                break;
            }
            mTaoboCredit.setNum( i % 5 + 1 );

        }
        return mTaoboCredit;
    }

    public static String saveTempFile(String content, Context mContext) {
        if (content == null || content.length( ) < 5) {
            try {
                mContext.getFileStreamPath( "temp_web" ).delete( );
            } catch (Exception e) {
                e.printStackTrace( );
            }
            // MyLog.e( "util", "content=" + content );
            return null;

        }
        try {
            FileOutputStream output = mContext.openFileOutput( "temp_web", Context.MODE_PRIVATE );
            BufferedOutputStream bos = new BufferedOutputStream( output );
            ByteArrayInputStream is = new ByteArrayInputStream( content.getBytes( ) );
            byte buffer[] = new byte[1024];
            int flag = -1;
            while ((flag = is.read( buffer )) != -1) {
                bos.write( buffer, 0, flag );
            }
            bos.flush( );
            bos.close( );
            is.close( );
            output.close( );
            // output.write( content.getBytes( ) );
            content = mContext.getFileStreamPath( "temp_web" ).getPath( );
            // MyLog.d( "Util", "path=" + content );
            return content;
        } catch (FileNotFoundException e) {
            e.printStackTrace( );
        } catch (IOException e) {
            e.printStackTrace( );
        }

        return null;
    }

    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance( "MD5" );

            messageDigest.reset( );

            messageDigest.update( str.getBytes( "UTF-8" ) );
        } catch (NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException e) {
        }

        byte[] byteArray = messageDigest.digest( );

        StringBuffer md5StrBuff = new StringBuffer( );

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString( 0xFF & byteArray[i] ).length( ) == 1)
                md5StrBuff.append( "0" ).append( Integer.toHexString( 0xFF & byteArray[i] ) );
            else
                md5StrBuff.append( Integer.toHexString( 0xFF & byteArray[i] ) );
        }
        return md5StrBuff.substring( 8, 24 ).toString( ).toUpperCase( Locale.getDefault( ) );
    }

    /**
     * 跳转到具体详情页面
     * 
     * @param mContext
     * @param holder
     */
    public static void goToDetialActivity(Context mContext, Holder holder) {
        Intent intent = new Intent( );
        // intent.putExtra( ItemDetailActivity.PATH, holder.getNum_iid( ) );
        // intent.putExtra( ItemDetailActivity.CLICK_URL, holder.getClick_url( ) );
        ItemDetailActivity.currentHolder = holder;
        // MyLog.i( "util", "holder=" + holder );
        intent.setClass( mContext, ItemDetailActivity.class );
        mContext.startActivity( intent );
    }

    public static void gotoBuyHelp(Context mContext) {
        Intent intent = new Intent( );
        intent.setClass( mContext, HelpYouActivity.class );
        mContext.startActivity( intent );
    }

    /**
     * 免责申明
     */
    public static void gotoDisclaimer(Context mContext) {

        Intent intent = new Intent( );
        intent.setClass( mContext, DisclaimerActivity.class );
        mContext.startActivity( intent );
    }

    public static void gotoAboutMe(Context mContext) {
        Intent intent = new Intent( );
        intent.setClass( mContext, AboutMeActivity.class );
        mContext.startActivity( intent );

    }

    public static void gotoSettings(Context mContext) {

        Intent intent = new Intent( );
        intent.setClass( mContext, SettingsActivity.class );
        mContext.startActivity( intent );
    }

    public static void gotoSuggest(Context mContext) {
        Intent intent = new Intent( );
        intent.setClass( mContext, SuggestActivity.class );
        mContext.startActivity( intent );

    }

    /**
     * ==========================================<BR>
     * 功能： 传递的二维码的地址 <BR>
     * 时间：2013-1-31 下午5:26:54 <BR>
     * ========================================== <BR>
     * 参数：
     * 
     * @param numiid
     * @param connect_id
     * @param mContext
     * @return
     */
    public static String getQRUrl(String numiid, String connect_id, Context mContext) {

        return "http://www.taose69.com/sendMessage?method=sendMsg&connect_id=" + connect_id + "&numIid=" + numiid + "&device_no=" + DeviceToken.getToken( mContext );
        // return "http://192.168.1.125:8080/sendMessage?method=sendMsg&connect_id=" + connect_id + "&numIid=" + numiid+ "&device_no=" + DeviceToken.getToken( mContext );
    }

    private static String getIpAddress(Context mContext) {
        WifiManager wifiManager = (WifiManager) mContext.getSystemService( Context.WIFI_SERVICE );
        if (wifiManager != null && wifiManager.getConnectionInfo( ) != null) {

            int ip = wifiManager.getConnectionInfo( ).getIpAddress( );
            return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + (ip >> 24 & 0xFF) + ":9000";
        }
        return null;
    }

    /**
     * ==========================================<BR>
     * 功能： <BR>
     * 时间：2013-1-30 下午6:13:53 <BR>
     * ========================================== <BR>
     * 参数：
     * 
     * @param mContext
     *            必须
     * @param url
     *            必须
     * @param numiid
     *            可选
     * @param title
     *            可选
     */
    public static void gotoBuyNow(final Context mContext, final String url, final String numiid, String title) {
        Intent intent = new Intent( );
        intent.setClass( mContext, BuyWebActivity.class );
        intent.putExtra( BuyWebActivity.PATH, url );
        mContext.startActivity( intent );
        MobclickAgent.onEvent( mContext, "buy_numid", numiid );
        MobclickAgent.onEvent( mContext, "buy_numidName", title );
    }

    /**
     * 
     * 功能：实现二维码扫描 <BR>
     * 时间：2013-1-24 下午5:40:08 <BR>
     * 参数：
     * 
     * @param mContext
     * @param numiid
     */
    public static void goToScanQR(final Context mContext, final String numiid) {
        MobclickAgent.onEvent( mContext, "buy_byZxing", numiid );
        MyLog.e( "onCallBack", "getIpAddress(mContext)=" + getIpAddress( mContext ) );
        Intent intent = new Intent( );
        ZxingDataManager.getInsance( ).setOnLinstener( new com.brunjoy.Zxing.utils.ZxingDataManager.ZxingCallBack( ) {
            @Override
            public void onCallBack(String msg) {
                MyLog.e( "onCallBack", "onCallBack  msg=" + msg );
                if (msg == null) {
                    MyToast.showMsgLong( mContext, R.string.qr_tip );
                    return;
                }
                TaobaokeDataManager.getInstance( mContext ).doQRGet( getQRUrl( numiid, com.brunjoy.Zxing.utils.ZxingDataManager.getInsance( ).getConnect_id( ), mContext ),
                        new TaobaoDataCallBack<String>( ) {
                            @Override
                            public void failure(String msg) {
                                // MyLog.e( "onSelected", "failure  msg=" + msg );
                                MyToast.showMsgLong( mContext, msg );
                            }

                            public void success(String t) {
                                MyToast.showMsgLong( mContext, "宝贝已在浏览器中打开，请放心在电脑端购买" );
                                // MyLog.e( "onSelected", "success  msg=" + t );
                            }
                        } );

            }
        } );
        intent.setClass( mContext, CaptureActivity.class );
        mContext.startActivity( intent );

        addStoreToServer( mContext );
    }

    /*************************************
     * 功能 ：
     * 
     * @param mContext
     *************************************/
    public static void addStoreToServer(final Context mContext) {

        MyHandler.getInstance( ).postDelayed( new Runnable( ) {

            @Override
            public void run() {
                boolean collectChange = Settings.getInSettings( mContext ).getBoolean( "collectChange", false );
                if (!collectChange)
                    return;
                ArrayList<Holder> list = MySqlLiteDataBase.getInstance( mContext ).getItems( );
                if (list != null) {
                    ArrayList<String> nums = new ArrayList<String>( );
                    for (Holder holder : list) {
                        nums.add( holder.getNum_iid( ) );
                    }
                    TaobaokeDataManager.getInstance( mContext ).doStoreCollects( nums );
                } else {
                    TaobaokeDataManager.getInstance( mContext ).doStoreCollects( null );
                }
                Settings.getInSettings( mContext ).putBoolean( "collectChange", false );
            }
        }, 1000 );

    }

    /**
     * ==========================================<BR>
     * 功能：收藏是否变法 <BR>
     * 时间：2013-1-31 下午5:27:43 <BR>
     * ========================================== <BR>
     * 参数：
     * 
     * @param mContext
     */
    public static void collectChange(Context mContext) {
        Settings.getInSettings( mContext ).putBoolean( "collectChange", true );

    }

    /**
     * 去web端
     * 
     * @param mContext
     * @param url
     */
    public static void goToWebActivity(Context mContext, String url) {
        Intent intent = new Intent( );
        intent.setClass( mContext, WebActivity.class );
        intent.putExtra( BuyWebActivity.PATH, url );
        mContext.startActivity( intent );
    }

    /**
     * 转化显示帖子的内容 让其美端空2格
     * 
     * @param str
     * @return
     */
    public static String transStrToParagraph(String str) {
        if (str == null)
            return null;
        return "\t" + str.replaceAll( "\n", "\n\t" );
    }

    /**
     * ==========================================<BR>
     * 功能：检查网络 <BR>
     * 时间：2013-2-5 下午2:38:10 <BR>
     * ========================================== <BR>
     * 参数：
     * 
     * @param mContext
     * @return
     */
    public static boolean isConnectedNetWork(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo( ConnectivityManager.TYPE_MOBILE );
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo( ConnectivityManager.TYPE_WIFI )/* .getState( ) */;
        if (mobileInfo != null && (mobileInfo.getState( ) == State.CONNECTED || mobileInfo.getState( ) == State.CONNECTING)) {
            return true;
        }
        if (wifiInfo != null && (wifiInfo.getState( ) == State.CONNECTED || wifiInfo.getState( ) == State.CONNECTING)) {
            return true;
        }
        return false;
    }

}
