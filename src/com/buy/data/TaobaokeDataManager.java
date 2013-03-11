package com.buy.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.buy.holder.APKHolder;
import com.buy.holder.Article;
import com.buy.holder.Categroys;
import com.buy.holder.Comment;
import com.buy.holder.ExtraItem;
import com.buy.holder.Holder;
import com.buy.holder.ItemDetail;
import com.buy.holder.JokeHolder;
import com.buy.holder.ParentsCategory;
import com.buy.holder.TItemRate;
import com.buy.holder.TopicHolder;
import com.buy.stores.Settings;
import com.buy.util.DeviceToken;
import com.buy.util.MyLog;
import com.buy.util.Util;
import com.buy.webUtil.WebDataManager;

public class TaobaokeDataManager {
    String TAG = "TaobaokeDataManager";
    private static TaobaokeDataManager mTaobaokeDataManager = null;
    private Context mContext;

    private TaobaokeDataManager(Context mContext) {
        this.mContext = mContext.getApplicationContext( );
    }

    /**
     * ****************************************<BR>
     * 功能：上传收藏信息到服务器端 <BR>
     * 时间：2013-1-24 下午5:44:48 <BR>
     * 参数：
     * 
     * @param token
     * @param numiids
     * <BR>
     * 
     */
    public void doStoreCollects(final ArrayList<String> numiids) {
        // MyLog.e( TAG, "上传收藏信息到服务器端" );
        GetDataTask task = new GetDataTask( "store/collects" ) {

            @Override
            public String doInBground(String url) {
                String resultMSG = null;
                try {
                    Map<String, String> params = WebDataManager.getInsance( ).generateApiParams( );
                    params.put( "token", "" + DeviceToken.getToken( mContext ) );
                    if (numiids != null) {
                        StringBuilder sb = new StringBuilder( );
                        for (String id : numiids) {
                            sb.append( id + "," );
                            // params.put( "numiids", id );
                        }
                        params.put( "numiids", sb.substring( 0, sb.length( ) - 1 ) );
                    }
                    resultMSG = WebDataManager.getInsance( ).doPost( url, params );
                } catch (Exception e) {
                    e.printStackTrace( );
                }
                return resultMSG;
            }

            @Override
            public void onPost(String result) {

            }

            @Override
            public void cancel(String msg) {

            }
        };
        addNewTask( task );
    }

    /**
     * Path bbs/articleList<BR>
     * 另外可以设置页面大小 pgSize 不填默认为20
     * 
     * @param limit
     * @param offset
     * @param taobaoDataCallBack
     */
    public void getBBSArticleList(final int limit, final int offset, final TaobaoDataCallBack<Article> taobaoDataCallBack) {
        GetDataTask task = new GetDataTask( "bbs/articleList" ) {

            @Override
            public String doInBground(String url) {
                String resultMSG = null;
                try {
                    Map<String, String> params = WebDataManager.getInsance( ).generateApiParams( );
                    params.put( "limit", "" + limit );
                    params.put( "offset", "" + offset );
                    resultMSG = WebDataManager.getInsance( ).doGet( url, params );
                } catch (IOException e) {
                    e.printStackTrace( );
                }
                return resultMSG;
            }

            @Override
            public void onPost(String result) {
                DataStatus mDataStatus = getDataType( result );
                if (mDataStatus == DataStatus.NG) {
                    taobaoDataCallBack.failure( mDataStatus.getValue( ) );
                    return;
                }
                JSONObject obj = JSONObject.parseObject( mDataStatus.getValue( ) );
                taobaoDataCallBack.success( JSON.parseArray( obj.getString( "articleList" ), Article.class ) );
                taobaoDataCallBack.countPage( obj.getIntValue( "count" ) );
            }

            @Override
            public void cancel(String msg) {
                taobaoDataCallBack.failure( "" );
            }

        };
        addNewTask( task );
    }

    /**
     * @param articleID
     *            帖子的id
     * 
     * @param containArticle
     *            是否包含帖子内容
     * @param limit
     * @param offset
     * @param taobaoDataCallBack
     */

    public void getBBSComments(final int articleID, final int limit, final int offset, final boolean begin_count, final boolean containArticle,
            final TaobaoDataCallBack<Comment> taobaoDataCallBack) {
        GetDataTask task = new GetDataTask( "bbs/getComments" ) {

            @Override
            public String doInBground(String url) {
                String resultMSG = null;
                try {
                    Map<String, String> params = WebDataManager.getInsance( ).generateApiParams( );
                    params.put( "articleID", "" + articleID );
                    params.put( "limit", "" + limit );
                    params.put( "offset", "" + offset );
                    params.put( "isCounter", "" + begin_count );
                    params.put( "isContainArticle", "" + containArticle );
                    resultMSG = WebDataManager.getInsance( ).doGet( url, params );
                } catch (IOException e) {
                    e.printStackTrace( );
                }
                return resultMSG;
            }

            @Override
            public void onPost(String result) {
                DataStatus mDataStatus = getDataType( result );
                if (mDataStatus == DataStatus.NG) {
                    taobaoDataCallBack.failure( mDataStatus.getValue( ) );
                    return;
                }
                JSONObject obj = JSONObject.parseObject( mDataStatus.getValue( ) );
                taobaoDataCallBack.success( JSON.parseArray( obj.getString( "commentList" ), Comment.class ) );
                taobaoDataCallBack.countPage( obj.getIntValue( "count" ) );
                taobaoDataCallBack.extraObject( JSON.parseObject( obj.getString( "article" ), Article.class ), obj.getString( "switch_comment" ) );

            }

            @Override
            public void cancel(String msg) {
                taobaoDataCallBack.failure( "" );
            }

        };
        addNewTask( task );
    }

    /**
     * bbs/comment<br>
     * 评论帖子
     * 
     * @param articleID
     *            帖子的id号
     * @param commentorName
     *            评论者的姓名
     * @param replieeName
     *            被评论者姓名 暂时可以不填 为二级评论预留
     * @param msg
     *            评论内容
     * @param repliedCommentId
     *            一级评论的id ，为二级评论预留
     * @param parentId
     *            回复一级评论里面回复的id
     * @param taobaoDataCallBack
     */
    public void doBBSComments(final int articleID, final String commentorName, final String replieeName, final String msg, final int repliedCommentId, final int parentId,
            final TaobaoDataCallBack<String> taobaoDataCallBack) {
        GetDataTask task = new GetDataTask( "bbs/comment" ) {

            @Override
            public String doInBground(String url) {
                String resultMSG = null;
                try {
                    Map<String, String> params = WebDataManager.getInsance( ).generateApiParams( );
                    params.put( "articleID", "" + articleID );
                    params.put( "commentorName", commentorName );
                    params.put( "replieeName", replieeName );
                    params.put( "msg", msg );
                    params.put( "repliedCommentId", repliedCommentId + "" );
                    params.put( "parentId", parentId + "" );

                    resultMSG = WebDataManager.getInsance( ).doPost( url, params );
                } catch (Exception e) {
                    e.printStackTrace( );
                }
                return resultMSG;
            }

            @Override
            public void onPost(String result) {
                DataStatus mDataStatus = getDataType( result );
                if (mDataStatus == DataStatus.NG) {
                    taobaoDataCallBack.failure( mDataStatus.getValue( ) );
                    return;
                }
                taobaoDataCallBack.success( mDataStatus.getValue( ) );
            }

            @Override
            public void cancel(String msg) {
                taobaoDataCallBack.failure( "cancel" );
            }

        };
        addNewTask( task );
    }

    public void doBBSCommentDele(final long commentId, final TaobaoDataCallBack<String> taobaoDataCallBack) {
        GetDataTask task = new GetDataTask( "bbs/commentDel" ) {

            @Override
            public String doInBground(String url) {
                String resultMSG = null;
                try {
                    Map<String, String> params = WebDataManager.getInsance( ).generateApiParams( );
                    params.put( "commentId", "" + commentId );
                    resultMSG = WebDataManager.getInsance( ).doPost( url, params );
                } catch (Exception e) {
                    e.printStackTrace( );
                }
                return resultMSG;
            }

            @Override
            public void onPost(String result) {
                DataStatus mDataStatus = getDataType( result );
                if (mDataStatus == DataStatus.NG) {
                    taobaoDataCallBack.failure( mDataStatus.getValue( ) );
                    return;
                }
                taobaoDataCallBack.success( mDataStatus.getValue( ) );
            }

            @Override
            public void cancel(String msg) {
                taobaoDataCallBack.failure( msg );
            }

        };
        addNewTask( task );
    }

    public void doGet(String Url, final TaobaoDataCallBack<String> taobaoDataCallBack) {
        // MyLog.e( TAG, "content" + content );
        GetDataTask task = new GetDataTask( Url ) {

            @Override
            public void onPost(String result) {
                taobaoDataCallBack.success( result );
            }

            @Override
            public String doInBground(String url) {
                String resultMSG = null;
                try {
                    Map<String, String> params = WebDataManager.getInsance( ).generateApiParams( );
                    resultMSG = WebDataManager.getInsance( ).doPost( url, params );
                } catch (Exception e) {
                    e.printStackTrace( );
                }
                return resultMSG;
            }

            @Override
            public void cancel(String msg) {
                taobaoDataCallBack.failure( null );
            }
        };
        addNewTask( task );
    }

    /**
     * 二维码下 相关的请求 超时时间为2s,如果4s内没有返回则重新扫描
     * 
     * @param Url
     * @param taobaoDataCallBack
     */
    public void doQRGet(String Url, final TaobaoDataCallBack<String> taobaoDataCallBack) {
        cancelAllTask( );
        GetDataTask task = new GetDataTask( Url ) {

            @Override
            public void onPost(String result) {
                if (result == null) {
                    taobaoDataCallBack.failure( "可能是网络的原因，暂时没有链接到服务器，请重新试试" );
                    return;
                }
                DataStatus mDataStatus = getDataType( result );
                if (mDataStatus == DataStatus.NG) {
                    taobaoDataCallBack.failure( mDataStatus.getValue( ) );
                    return;
                }
                taobaoDataCallBack.success( mDataStatus.getValue( ) );
                // } else {
                // taobaoDataCallBack.failure( "" );
                // }
            }

            @Override
            public String doInBground(String url) {
                String resultMSG = null;
                try {
                    Map<String, String> params = WebDataManager.getInsance( ).generateApiParams( );
                    resultMSG = WebDataManager.getInsance( ).doPost( url, params, 6000 );
                } catch (Exception e) {
                    e.printStackTrace( );
                    return null;
                }
                return resultMSG;
            }

            @Override
            public void cancel(String msg) {
                taobaoDataCallBack.failure( "可能是网络的原因，暂时没有链接到服务器，请重新试试" );
            }
        };
        addNewTask( task );
    }

    /**
     * 反馈 feedBack/commit
     * 
     * @param content
     * @param mobile_model
     * @param email
     * @param taobaoDataCallBack
     */
    public void commitContent(final String content, final String mobile_model, final String mailOrOther, final TaobaoDataCallBack<String> taobaoDataCallBack) {
        // MyLog.e( TAG, "content" + content );
        GetDataTask task = new GetDataTask( "feedBack/commit" ) {

            @Override
            public void onPost(String result) {

                if (getStatuse( result )) {
                    taobaoDataCallBack.success( "提交成功,非常感谢您的建议" );
                } else {
                    taobaoDataCallBack.failure( "提交失败，请稍后再试" );
                }
            }

            @Override
            public String doInBground(String url) {
                String resultMSG = null;
                try {
                    Map<String, String> params = WebDataManager.getInsance( ).generateApiParams( );
                    params.put( "content", content );
                    params.put( "mobile_model", mobile_model );
                    params.put( "email", mailOrOther );
                    // MyLog.e( TAG, "params=" + params );
                    resultMSG = WebDataManager.getInsance( ).doPost( url, params );
                } catch (Exception e) {
                    e.printStackTrace( );
                }
                return resultMSG;
            }

            @Override
            public void cancel(String msg) {
                taobaoDataCallBack.failure( "提交失败，请稍后再试" );
            }
        };
        addNewTask( task );
    }

    public void downFile(final String path, final String fileName, final TaobaoDataCallBack<File> taobaoDataCallBack) {
        GetDataTask task = new GetDataTask( path ) {

            @Override
            public String doInBground(String url) {

                try {
                    return WebDataManager.getInsance( ).downFile( fileName, url, null );
                } catch (IOException e) {

                    e.printStackTrace( );
                    return null;
                }
            }

            @Override
            public void onPost(String result) {
                if (result == null) {
                    taobaoDataCallBack.failure( "文件为空" );
                }
                try {
                    if (TextUtils.isEmpty( result ))
                        return;
                    File file = new File( result );
                    if (!file.exists( )) {
                        taobaoDataCallBack.failure( "文件不存在" );
                    } else {
                        taobaoDataCallBack.success( file );
                    }
                } catch (Exception e) {
                    e.printStackTrace( );
                }

            }

            @Override
            public void cancel(String msg) {
                taobaoDataCallBack.failure( "文件下载失败" );

            }

        };
        addNewTask( task );
    }

    public void getLoadingPic(final int width, final int height, final TaobaoDataCallBack<ExtraItem> taobaoDataCallBack) {
        // MyLog.e( TAG, "content" + content );
        GetDataTask task = new GetDataTask( "system/getLoadingPic" ) {

            @Override
            public void onPost(String result) {

                DataStatus mDataStatus = getDataType( result );
                if (mDataStatus == DataStatus.NG) {
                    taobaoDataCallBack.failure( mDataStatus.getValue( ) );
                    return;
                }
                // result = getData( result );
                // if (result == null) {
                // taobaoDataCallBack.failure( null );
                // return;
                // }
                ExtraItem item = JSON.parseObject( mDataStatus.getValue( ), ExtraItem.class );
                taobaoDataCallBack.success( item );
            }

            @Override
            public String doInBground(String url) {
                String resultMSG = null;
                try {
                    Map<String, String> params = WebDataManager.getInsance( ).generateApiParams( );
                    params.put( "height", height + "" );
                    params.put( "width", width + "" );
                    // MyLog.e( TAG, "params=" + params );
                    resultMSG = WebDataManager.getInsance( ).doGet( url, params );
                } catch (IOException e) {
                    e.printStackTrace( );
                }
                return resultMSG;
            }

            @Override
            public void cancel(String msg) {
                taobaoDataCallBack.failure( null );
            }
        };
        addNewTask( task );
    }

    public void getSystemProperties(final String key, final TaobaoDataCallBack<ExtraItem> taobaoDataCallBack) {
        // MyLog.e( TAG, "content" + content );
        GetDataTask task = new GetDataTask( "system/getValueByKey" ) {

            @Override
            public void onPost(String result) {
                DataStatus mDataStatus = getDataType( result );
                if (mDataStatus == DataStatus.NG) {
                    taobaoDataCallBack.failure( mDataStatus.getValue( ) );
                    return;
                }
                ExtraItem item = JSON.parseObject( mDataStatus.getValue( ), ExtraItem.class );
                taobaoDataCallBack.success( item );
            }

            @Override
            public String doInBground(String url) {
                String resultMSG = null;
                try {
                    Map<String, String> params = WebDataManager.getInsance( ).generateApiParams( );
                    params.put( "key", key );
                    resultMSG = WebDataManager.getInsance( ).doGet( url, params );
                } catch (IOException e) {
                    e.printStackTrace( );
                }
                return resultMSG;
            }

            @Override
            public void cancel(String msg) {
                taobaoDataCallBack.failure( null );
            }
        };
        addNewTask( task );
    }

    public static synchronized TaobaokeDataManager getInstance(Context mContext) {
        if (mTaobaokeDataManager == null) {
            mTaobaokeDataManager = new TaobaokeDataManager( mContext );
        }
        return mTaobaokeDataManager;
    }

    public void getServerApkVersion(final TaobaoDataCallBack<APKHolder> taobaoDataCallBack) {
        GetDataTask task = new GetDataTask( "version/get" ) {

            @Override
            public String doInBground(String url) {
                String resultMSG = null;
                try {
                    resultMSG = WebDataManager.getInsance( ).doGet( url, WebDataManager.getInsance( ).generateApiParams( ) );
                } catch (IOException e) {
                    e.printStackTrace( );
                }
                return resultMSG;
            }

            @Override
            public void onPost(String result) {
                DataStatus mDataStatus = getDataType( result );
                if (mDataStatus == DataStatus.NG) {
                    taobaoDataCallBack.failure( mDataStatus.getValue( ) );
                    return;
                }
                APKHolder mAPKHolder = com.alibaba.fastjson.JSONObject.parseObject( mDataStatus.getValue( ), APKHolder.class );
                taobaoDataCallBack.success( mAPKHolder );

            }

            @Override
            public void cancel(String msg) {
                taobaoDataCallBack.failure( "" );
            }

        };
        addNewTask( task );
    }

    /**
     * 推荐 topic/itemList
     * 
     * @param id
     * @param page
     * @param taobaoDataCallBack
     */
    public void getTopicItem(final int id, final int page, final TaobaoDataCallBack<Holder> taobaoDataCallBack) {

        GetDataTask task = new GetDataTask( "topic/itemList" ) {

            @Override
            public String doInBground(String url) {
                String resultMSG = null;
                try {
                    Map<String, String> params = WebDataManager.getInsance( ).generateApiParams( );
                    params.put( "page", page + "" );
                    params.put( "id", id + "" );
                    params.put( "pgSize", "20" );

                    resultMSG = WebDataManager.getInsance( ).doGet( url, params );
                } catch (IOException e) {
                    resultMSG = e.getMessage( );
                }
                return resultMSG;
            }

            @Override
            public void onPost(String result) {
                DataStatus mDataStatus = getDataType( result );
                if (mDataStatus == DataStatus.NG) {
                    taobaoDataCallBack.failure( mDataStatus.getValue( ) );
                    return;
                }
                result = mDataStatus.getValue( );
                String data = getStrDataByFiled( result, "list" );
                if (data == null) {
                    taobaoDataCallBack.failure( "没有新的内容" );
                    return;
                }
                List<Holder> list = com.alibaba.fastjson.JSONArray.parseArray( data, Holder.class );
                taobaoDataCallBack.success( list );
                taobaoDataCallBack.countPage( getIntDataByFiled( result, "pageCount" ) );
            }

            @Override
            public void cancel(String msg) {

            }

        };
        addNewTask( task );
    }

    /**
     * 推荐 Gallery 轮换 <BR>
     * topic/topicList
     * 
     * @param page
     * @param taobaoDataCallBack
     */
    public void getTopics(final int page, final TaobaoDataCallBack<TopicHolder> taobaoDataCallBack) {
        GetDataTask task = new GetDataTask( "topic/topicList" ) {
            @Override
            public String doInBground(String url) {
                String resultMSG = null;
                try {
                    Map<String, String> params = WebDataManager.getInsance( ).generateApiParams( );
                    params.put( "page", page + "" );
                    params.put( "pgSize", "20" );
                    resultMSG = WebDataManager.getInsance( ).doGet( url, params );
                } catch (IOException e) {
                    // resultMSG = e.getMessage( );
                }

                return resultMSG;
            }

            @Override
            public void onPost(String result) {
                result = getData( result );
                String data = getStrDataByFiled( result, "list" );
                if (result == null || data == null) {
                    taobaoDataCallBack.failure( "没有新的内容" );
                    return;
                }
                List<TopicHolder> list = com.alibaba.fastjson.JSONArray.parseArray( data, TopicHolder.class );
                list = topicFilter( list );
                taobaoDataCallBack.success( list );
                taobaoDataCallBack.countPage( getIntDataByFiled( result, "pageCount" ) );
            }

            @Override
            public void cancel(String msg) {

            }

        };
        addNewTask( task );
    }

    /**
     * 过滤主题 类型 0 代码主题，1 代表详情，2,代表网页
     * 
     * @param list
     * @return
     */
    protected List<TopicHolder> topicFilter(List<TopicHolder> list) {
        if (list != null) {
            for (int i = list.size( ) - 1; i >= 0; i--) {
                int type = list.get( i ).getType( );
                if (type < 0 || type > 2) {
                    list.remove( i );
                }
            }
        }
        return list;
    }

    /**
     * joke/get 段子
     * 
     * @param date
     * @param offset
     * @param limit
     * @param taobaoDataCallBack
     */
    public void getJoke(final Date date, final int offset, final int limit, final TaobaoDataCallBack<JokeHolder> taobaoDataCallBack) {

        GetDataTask task = new GetDataTask( "joke/get" ) {

            @Override
            public void onPost(String result) {
                DataStatus mDataStatus = getDataType( result );
                if (mDataStatus == DataStatus.NG) {
                    taobaoDataCallBack.failure( mDataStatus.getValue( ) );
                    return;
                }
                List<JokeHolder> list = com.alibaba.fastjson.JSONArray.parseArray( mDataStatus.getValue( ), JokeHolder.class );
                taobaoDataCallBack.success( list );
            }

            @Override
            public String doInBground(String url) {
                String resultMSG = null;
                try {
                    Map<String, String> params = WebDataManager.getInsance( ).generateApiParams( );
                    params.put( "offset", offset + "" );
                    if (date != null)
                        params.put( "date", date + "" );
                    params.put( "limit", limit + "" );
                    resultMSG = WebDataManager.getInsance( ).doGet( url, params );
                } catch (IOException e) {
                    e.printStackTrace( );
                }
                return resultMSG;
            }

            @Override
            public void cancel(String msg) {
                taobaoDataCallBack.failure( msg );
            }
        };
        addNewTask( task );
    }

    /**
     * 商品评论 items/comments/{numIid:{0-9}+}
     * 
     * @param numIid
     * @param offset
     * @param limit
     * @param taobaoDataCallBack
     */
    public void getComments(final String numIid, final int offset, final int limit, final TaobaoDataCallBack<TItemRate> taobaoDataCallBack) {
        GetDataTask mGetDataTask = new GetDataTask( "items/comments/" + numIid ) {
            @Override
            public void onPost(String result) {

                DataStatus mDataStatus = getDataType( result );
                if (mDataStatus == DataStatus.NG) {
                    taobaoDataCallBack.failure( mDataStatus.getValue( ) );
                    return;
                } else if (mDataStatus == DataStatus.OK) {
                    List<TItemRate> list = com.alibaba.fastjson.JSONArray.parseArray( mDataStatus.getValue( ), TItemRate.class );
                    taobaoDataCallBack.success( list );
                }

            }

            @Override
            public String doInBground(String url) {
                String resultMSG = null;
                try {
                    Map<String, String> params = WebDataManager.getInsance( ).generateApiParams( );
                    params.put( "offset", offset + "" );
                    params.put( "limit", limit + "" );
                    resultMSG = WebDataManager.getInsance( ).doGet( url, params );
                } catch (IOException e) {
                    e.printStackTrace( );
                }
                return resultMSG;
            }

            @Override
            public void cancel(String msg) {
                taobaoDataCallBack.failure( msg );

            }
        };
        addNewTask( mGetDataTask );
        // addTask( "items/comments/" + numIid, mGetDataTask );
    }

    /**
     * items/getAllParentsCategorys<BR>
     * 获取所有的父分类
     * 
     * @param offset
     * @param limit
     * @param taobaoDataCallBack
     */
    public void getParentsCategroys(final int offset, final int limit, final TaobaoDataCallBack<Categroys> taobaoDataCallBack) {
        final String storeCategory = "STORECATEGORY";
        // 先读取缓存，然后在冲服务器重新读取并存储
        String data = getData( Settings.getInSettings( mContext ).getString( storeCategory, null ) );

        if (data != null) {
            try {
                List<Categroys> list = com.alibaba.fastjson.JSONArray.parseArray( data, Categroys.class );
                taobaoDataCallBack.success( list );
            } catch (Exception e) {
                Settings.getInSettings( mContext ).putString( storeCategory, null );
            }
        }
        // end
        GetDataTask task = new GetDataTask( "items/getAllParentsCategorys" ) {

            @Override
            public void onPost(String result) {
                DataStatus mDataStatus = getDataType( result );
                if (mDataStatus == DataStatus.NG) {
                    taobaoDataCallBack.failure( mDataStatus.getValue( ) );
                    return;
                }
                List<Categroys> list = com.alibaba.fastjson.JSONArray.parseArray( mDataStatus.getValue( ), Categroys.class );
                taobaoDataCallBack.success( list );
            }

            @Override
            public String doInBground(String url) {
                String resultMSG = null;
                try {
                    Map<String, String> params = WebDataManager.getInsance( ).generateApiParams( );
                    params.put( "offset", offset + "" );
                    params.put( "limit", limit + "" );
                    resultMSG = WebDataManager.getInsance( ).doGet( url, params );
                } catch (IOException e) {
                    resultMSG = null;
                    e.printStackTrace( );
                }
                // MyLog.i( TAG, "resultMSG=" + resultMSG );
                // MyLog.i( TAG, "getItemDetail耗时=" + (System.currentTimeMillis( ) - time) );
                if (resultMSG != null)
                    Settings.getInSettings( mContext ).putString( storeCategory, resultMSG );

                return resultMSG;
            }

            @Override
            public void cancel(String msg) {
                taobaoDataCallBack.failure( msg );

            }
        };
        addNewTask( task );

    }

    /**
     * 得到某个子分类的数据<BR>
     * items/getItemsById
     * 
     * @param id也就是
     *            tcId
     * @param isContainCategory
     *            true包含该分类信息 ,false 只包含相关分类的数据信息
     * @param orderBy
     *            例如按照commission，seller_credit_score，volume，price 等排序
     * @param isAsc
     *            是否是升序排列
     * @param offset
     * @param limit
     * @param taobaoDataCallBack
     */
    public void getItemsById(final int id, final String orderBy, final boolean isAsc, final int offset, final int limit,
            final TaobaoDataCallBack<ParentsCategory> taobaoDataCallBack) {

        GetDataTask task = new GetDataTask( "items/getItemsById" ) {
            public void onPost(String result) {
                String data = getData( result );
                if (data == null) {
                    taobaoDataCallBack.failure( "没有新的内容" );
                    return;
                }
                ParentsCategory date = com.alibaba.fastjson.JSONArray.parseObject( data, ParentsCategory.class );
                taobaoDataCallBack.success( date );

            }

            @Override
            public String doInBground(String url) {
                String resultMSG = null;
                try {
                    Map<String, String> params = WebDataManager.getInsance( ).generateApiParams( );
                    params.put( "offset", offset + "" );
                    params.put( "limit", limit + "" );
                    params.put( "id", id + "" );

                    params.put( "orderBy", orderBy );
                    params.put( "asc", isAsc + "" );

                    // resultMSG = WebDataManager.getInsance( ).doPost( mContext, "items/getItemsBytcId", params, null, 10000, 10000, true );
                    resultMSG = WebDataManager.getInsance( ).doGet( url, params );
                } catch (IOException e) {
                    resultMSG = e.getMessage( );
                }
                return resultMSG;
            }

            @Override
            public void cancel(String msg) {
                taobaoDataCallBack.failure( msg );

            }
        };
        addNewTask( task );
    }

    /**
     * 得到父分类数据<br>
     * items/getItemsByParentsId
     * 
     * @param id
     * @param orderBy
     * @param isAsc
     * @param offset
     * @param limit
     * @param taobaoDataCallBack
     */
    public void getItemsByParentsId(final int id, final String orderBy, final boolean isAsc, final int offset, final int limit,
            final TaobaoDataCallBack<ParentsCategory> taobaoDataCallBack) {

        GetDataTask task = new GetDataTask( "items/getItemsByParentsId" ) {
            @Override
            public void onPost(String result) {
                DataStatus mDataStatus = getDataType( result );
                if (mDataStatus == DataStatus.NG) {
                    taobaoDataCallBack.failure( mDataStatus.getValue( ) );
                    return;
                }
                ParentsCategory date = com.alibaba.fastjson.JSONArray.parseObject( mDataStatus.getValue( ), ParentsCategory.class );
                taobaoDataCallBack.success( date );

            }

            @Override
            public String doInBground(String url) {
                String resultMSG = null;
                try {
                    Map<String, String> params = WebDataManager.getInsance( ).generateApiParams( );
                    params.put( "offset", offset + "" );
                    params.put( "limit", limit + "" );
                    params.put( "id", id + "" );
                    params.put( "orderBy", orderBy );
                    params.put( "asc", isAsc + "" );
                    resultMSG = WebDataManager.getInsance( ).doGet( url, params );
                } catch (IOException e) {
                    resultMSG = e.getMessage( );
                }
                return resultMSG;
            }

            @Override
            public void cancel(String msg) {
                taobaoDataCallBack.failure( msg );

            }
        };
        addNewTask( task );
    }

    /**
     * items/{numid:{0-9}+} 商品详情
     * 
     * @param numid
     * @param taobaoDataCallBack
     */
    public void getItemDetail(final String numid, final TaobaoDataCallBack<ItemDetail> taobaoDataCallBack) {
        cancelAllTask( );
        GetDataTask task = new GetDataTask( "items/" + numid ) {
            @Override
            public void onPost(String result) {
                DataStatus mDataStatus = getDataType( result );
                if (mDataStatus == DataStatus.NG) {
                    taobaoDataCallBack.failure( mDataStatus.getValue( ) );
                    return;
                }
                ItemDetail item;
                item = com.alibaba.fastjson.JSONObject.parseObject( mDataStatus.getValue( ), ItemDetail.class );
                taobaoDataCallBack.success( item );

            }

            @Override
            public String doInBground(String url) {
                String resultMSG = null;
                try {
                    resultMSG = WebDataManager.getInsance( ).doGet( url, null );
                } catch (IOException e) {
                    e.printStackTrace( );
                }
                // MyLog.i( TAG, "resultMSG=" + resultMSG );
                // MyLog.i( TAG, "getItemDetail耗时=" + (System.currentTimeMillis( ) - time) );
                return resultMSG;
            }

            @Override
            public void cancel(String msg) {
                taobaoDataCallBack.failure( msg );

            }

        };
        addNewTask( task );
    }

    public String getData(String text) {
        do {
            if (text == null)
                break;
            try {
                JSONObject obj = JSONObject.parseObject( text );
                return obj.getString( "data" );
            } catch (Exception e) {
            }
        } while (false);
        return null;
    }

    public String getStrDataByFiled(String text, String filed) {
        do {
            if (text == null)
                break;
            JSONObject obj = JSONObject.parseObject( text );
            return obj.getString( filed );
        } while (false);
        return null;
    }

    public int getIntDataByFiled(String text, String filed) {
        do {
            if (text == null)
                break;
            JSONObject obj = JSONObject.parseObject( text );
            return obj.getInteger( filed );
        } while (false);
        return 0;
    }

    public boolean getStatuse(String text) {
        try {
            do {
                if (text == null)
                    break;

                JSONObject obj = JSONObject.parseObject( text );
                String status = obj.getString( "op_status" );

                return (status != null && status.equalsIgnoreCase( "ok" ));
            } while (false);
        } catch (Exception e) {

        }
        return false;
    }

    private static ArrayList<Task> taskUrl = new ArrayList<Task>( );

    private class Task {
        public String url;
        public GetDataTask mGetDataTask;

        public Task(String url, GetDataTask mGetDataTask) {
            this.url = url;
            this.mGetDataTask = mGetDataTask;
        }

    }

    private synchronized void addNewTask(GetDataTask task) {

        if (taskUrl == null)
            return;

        if (currentRunningTask != null) {
            synchronized (currentRunningTask) {
                if (System.currentTimeMillis( ) - startTime > 3 * 1000) {
                    currentRunningTask.cancel( true );
                    // MyLog.e( TAG, "------cancel---------siz=" + currentRunningTask.getUrl( ) );
                    currentRunningTask = null;
                }
            }

        }
        MyLog.e( TAG, "------addNewTask---------siz=" + taskUrl.size( ) );
        for (int i = taskUrl.size( ) - 1; i >= 0; i--) {
            synchronized (taskUrl) {
                if (task.getUrl( ).equals( taskUrl.get( i ).url )) {
                    taskUrl.remove( i );
                    taskUrl.add( i, new Task( task.getUrl( ), task ) );
                    MyLog.e( TAG, "-----------------" );
                    MyLog.e( TAG, "---------已经包含了替换以前的任务任务，不能重复添加-----" );
                    MyLog.e( TAG, "-----------------" );
                    return;
                }
            }

        }
        if (currentRunningTask != null/* currentRuningTaskNum >= MAXTASKNUM */) {
            taskUrl.add( 0, new Task( task.getUrl( ), task ) );
            MyLog.e( TAG, "-asfdas---有正在执行的任务  故先添加到执行队列中--addNewTask---------siz=" + taskUrl.size( ) );
        } else {
            MyLog.e( TAG, "-asfdas-----开始执行execute---------siz=" + taskUrl.size( ) );
            task.execute( );
        }

    }

    private static long startTime = 0;
    private GetDataTask currentRunningTask = null;

    private synchronized void checkTask() {
        if (currentRunningTask != null/* currentRuningTaskNum >= MAXTASKNUM */) {
            // MyLog.e( TAG, "---------正在执行的zhong--------currentRunningTask" + currentRunningTask.getUrl( ) );
        } else {
            if (taskUrl.size( ) > 0) {
                Task task = taskUrl.remove( taskUrl.size( ) - 1 );
                task.mGetDataTask.execute( );
                // MyLog.e( TAG, "---------缓存有需要执行的任务，现在去执行--------url=" + task.url );
            } else {
                // MyLog.e( TAG, "---------任务队列已经没有任务需要执行了--------" );
            }
        }

    }

    // private byte currentRuningTaskNum = 0;

    public abstract class GetDataTask extends AsyncTask<Void, Void, String> {
        private String url;

        public String getUrl() {
            return url;
        }

        public abstract String doInBground(String url);

        public abstract void onPost(String result);

        public abstract void cancel(String msg);

        public GetDataTask(String url) {
            this.url = url;

        }

        @Override
        protected synchronized void onCancelled() {
            // currentRuningTaskNum--;
            currentRunningTask = null;
            checkTask( );
            // MyLog.e( TAG, "---------因为有新的任务，并且此任务耗时超过最大UI时间，故onCancelled-------url=" + url );
        }

        @Override
        protected void onPreExecute() {
            // currentRuningTaskNum++;
            startTime = System.currentTimeMillis( );
            currentRunningTask = this;
            // MyLog.e( TAG, "---------准备执行onPreExecute-------url=" + url );
        }

        @Override
        protected String doInBackground(Void... arg0) {
            // MyLog.e( TAG, "---------后台执行doInBackground-------url=" + url );
            return doInBground( url );
        }

        @Override
        protected synchronized void onPostExecute(String result) {
            onPost( result );
            currentRunningTask = null;
            // MyLog.e( TAG, "---------执行完成onPostExecute-------url=" + url );
            // currentRuningTaskNum--;
            checkTask( );
        }

    }

    public synchronized void cancelAllTask() {
        // MyLog.e( TAG, "----取消正在执行的任务-----cancelAllTask-------" );
        taskUrl.clear( );
        if (currentRunningTask != null) {
            currentRunningTask.cancel( false );
        }
        currentRunningTask = null;
    }

    public void clear() {
        taskUrl.clear( );
        currentRunningTask = null;
        mTaobaokeDataManager = null;
    }

    public enum DataStatus {
        OK, NG;
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    public DataStatus getDataType(String result) {
        DataStatus mDataStatus = DataStatus.NG;
        // MyLog.e( TAG, "result=" + result );
        if (TextUtils.isEmpty( result )) {
            if (!Util.isConnectedNetWork( mContext )) {
                mDataStatus.setValue( "亲，您的网络好像有问题，请先连接到互联网" );
            } else {
                mDataStatus.setValue( "获取数据失败" );
            }
        }
        try {
            JSONObject obj = JSONObject.parseObject( result );
            String op_status = obj.getString( "op_status" );
            if ("ok".equalsIgnoreCase( op_status )) {
                mDataStatus = DataStatus.OK;
                mDataStatus.setValue( obj.getString( "data" ) );

            } else if ("NG".equalsIgnoreCase( op_status )) {
                mDataStatus = DataStatus.NG;
                mDataStatus.setValue( obj.getString( "err_msg" ) );
            }
        } catch (Exception e) {
            if (!Util.isConnectedNetWork( mContext )) {
                mDataStatus.setValue( "您的网络没连接，请先连接到互联网  然后刷新试试" );
            } else {
                mDataStatus.setValue( "数据加载失败,请稍后重新刷新试一试" );
            }

        }

        return mDataStatus;
    }

}
