//package com.buy.data;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.buy.bases.BaseApplication;
//import com.buy.holder.Holder;
//import com.buy.holder.TaobaokeItem;
//import com.buy.util.//MyLog;
//import com.taobao.top.android.TopAndroidClient;
//import com.taobao.top.android.TopParameters;
//import com.taobao.top.android.api.ApiError;
//import com.taobao.top.android.api.TopApiListener;
//
//public class TaobaoApiManager {
//    static final String TAG = "TaobaoApiManager";
//    static String TaobaoApi = "TaobaoApi";
//    private TopAndroidClient client;
//
//    private static TaobaoApiManager mTaobaoApiManager;
//
//    public static TaobaoApiManager getInstance() {
//        if (mTaobaoApiManager == null) {
//            mTaobaoApiManager = new TaobaoApiManager( );
//
//        }
//        return mTaobaoApiManager;
//    }
//
//    private TaobaoApiManager() {
//        client = TopAndroidClient.getAndroidClientByAppKey( BaseApplication.appKey );
//    }
//
//    public void getTaobaokeItemByKeyWord(String KeyWord, final TaobaoDataCallBack<Holder> mTaobaoDataCallBack) {
//        TopParameters params = getTopParameters( );
//        params.addFields( "num_iid,title,nick,pic_url,price,click_url,commission,commission_rate,commission_num,commission_volume,shop_click_url,seller_credit_score,item_location,volume" );
//        params.addParam( "keyword", KeyWord );
//        params.setMethod( "taobao.taobaoke.items.get" );
//
//        client.api( params, null, new TopApiListener( ) {
//
//            @Override
//            public void onException(Exception e) {
//                //MyLog.d( TAG, "onException=" + e );
//                runOnUiThread( mTaobaoDataCallBack, e.getMessage( ) );
//            }
//
//            @Override
//            public void onError(ApiError error) {
//                runOnUiThread( mTaobaoDataCallBack, error.getMsg( ) );
//                //MyLog.d( TAG, "onError=" + error );
//            }
//
//            @Override
//            public void onComplete(JSONObject json) {
//
//                Object items = getObjectJson( "taobaoke_item", json );
//                List<Holder> list = null;
//                if (items instanceof JSONArray) {
//                    list = com.alibaba.fastjson.JSONArray.parseArray( items.toString( ), Holder.class );
//                }
//                runOnUiThread( mTaobaoDataCallBack, list );
//                // mTaobaoDataCallBack.success( list );
//            }
//        }, true );
//
//    }
//
//    protected <T> void runOnUiThread(TaobaoDataCallBack<T> mTaobaoDataCallBack, String message) {
//        mTaobaoDataCallBack.failure( message );
//
//    }
//
//    protected <T> void runOnUiThread(TaobaoDataCallBack<T> mTaobaoDataCallBack, List<T> list) {
//        mTaobaoDataCallBack.success( list );
//
//    }
//
//    protected ArrayList<TaobaokeItem> converToList(JSONArray items) {
//
//        return null;
//    }
//
//    public Object getObjectJson(String key, JSONObject json) {
//        try {
//            Iterator<String> it = json.keys( );
//            while (it.hasNext( )) {
//                String k = it.next( );
//                Object v = json.get( k );
//                if (key.equals( k )) {
//
//                    return v;
//                }
//                if (v instanceof JSONArray) {
//                    // getObjArrays( (JSONArray) v );
//
//                } else if (v instanceof JSONObject) {
//                    v = getObjectJson( key, (JSONObject) v );
//                    if (v != null)
//                        return v;
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace( );
//        }
//
//        return null;
//    }
//
//    private void getObjArrays(JSONArray v) {
//        try {
//            for (int i = 0; i < v.length( ); i++) {
//                //MyLog.d( TAG, v.get( i ) );
//            }
//        } catch (JSONException e) {
//            e.printStackTrace( );
//        }
//    }
//
//    public void getTaobaokeItemByCid(String cid, final TaobaoDataCallBack<Holder> mTaobaoDataCallBack) {
//        TopParameters params = getTopParameters( );
//        params.addFields( "num_iid,title,nick,pic_url,price,click_url,commission,commission_rate,commission_num,commission_volume,shop_click_url,seller_credit_score,item_location,volume" );
//        params.addParam( "cid", cid );
//        params.setMethod( "taobao.taobaoke.items.get" );
//        client.api( params, null, new TopApiListener( ) {
//
//            @Override
//            public void onException(Exception e) {
//                //MyLog.d( TAG, "onException=" + e );
//                runOnUiThread( mTaobaoDataCallBack, e.getMessage( ) );
//            }
//
//            @Override
//            public void onError(ApiError error) {
//                runOnUiThread( mTaobaoDataCallBack, error.getMsg( ) );
//                //MyLog.d( TAG, "onError=" + error );
//            }
//
//            @Override
//            public void onComplete(JSONObject json) {
//
//                Object items = getObjectJson( "taobaoke_item", json );
//                List<Holder> list = null;
//                if (items instanceof JSONArray) {
//                    list = com.alibaba.fastjson.JSONArray.parseArray( items.toString( ), Holder.class );
//                }
//                runOnUiThread( mTaobaoDataCallBack, list );
//            }
//        }, true );
//
//    }
//
//    // 8684137645
//    public void getTaobaoItemByNumId(String num_iid) {
//        TopParameters params = getTopParameters( );
//        params.addFields( "detail_url,num_iid,title,nick,type,cid,wap_desc,sell_promise,item_imgs,prop_imgs,freight_payer,location,price,post_fee,express_fee,ems_fee,is_lightning_consignment" );
//        params.addParam( "num_iid", num_iid );
//        params.setMethod( "taobao.item.get" );
//
//        client.api( params, null, new TopApiListener( ) {
//
//            @Override
//            public void onException(Exception e) {
//                //MyLog.d( TAG, "onException=" + e );
//
//            }
//
//            @Override
//            public void onError(ApiError error) {
//
//                //MyLog.d( TAG, "onError=" + error );
//            }
//
//            @Override
//            public void onComplete(JSONObject json) {
//                //MyLog.d( TAG, "onComplete=" + json );
//
//            }
//        }, true );
//
//    }
//
//    public void getTaobaoItemByTrack_iid(String track_iid) {
//        TopParameters params = getTopParameters( );
//        params.addFields( "detail_url,num_iid,title,nick,type,cid,seller_cids,props,input_pids,input_str,desc,pic_url,num,valid_thru,list_time,delist_time,stuff_status,location,price,post_fee,express_fee,ems_fee,has_discount,freight_payer,has_invoice,has_warranty,has_showcase,modified,increment,approve_status,postage_id,product_id,auction_point,property_alias,item_img,prop_img,sku,video,outer_id" );
//        params.addParam( "track_iid", track_iid );
//        params.setMethod( "taobao.item.get" );
//        client.api( params, null, new TopApiListener( ) {
//
//            @Override
//            public void onException(Exception e) {
//                //MyLog.d( TAG, "onException=" + e );
//
//            }
//
//            @Override
//            public void onError(ApiError error) {
//
//                //MyLog.d( TAG, "onError=" + error );
//            }
//
//            @Override
//            public void onComplete(JSONObject json) {
//                //MyLog.d( TAG, "onComplete=" + json );
//
//            }
//        }, true );
//
//    }
//
//    private TopParameters getTopParameters() {
//        TopParameters params = new TopParameters( );
//        params.addParam( "is_mobile", "true" );
//        return params;
//
//    }
//    public interface TaobokeCIDs {
//        /**
//         * 计生用品
//         */
//        public static final String JISHENGYONGPIN = "50012829";
//        /**
//         * 男用器具
//         */
//        public static final String NANYONGQIJU = "50019617";
//        /**
//         * 女用器具
//         */
//        public static final String NVYONGQIJU = "50019630";
//        /**
//         * 情趣用品
//         */
//        public static final String QINGQUYONGPING = "50019641";
//        /**
//         * 情趣内衣
//         */
//        public static final String QINGQUNEIYI = "50019651";
//        /**
//         * 情趣家具
//         */
//        public static final String QINGQUJIAJU = "50020206";
//
//    }
//}
