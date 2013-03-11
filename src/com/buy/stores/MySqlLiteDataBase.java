package com.buy.stores;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.MediaStore;

import com.alibaba.fastjson.JSON;
import com.buy.holder.Holder;
import com.buy.util.Util;

public class MySqlLiteDataBase {

    String TAG = "MySqlLiteDataBase";
    // 数据库名称为data
    private static final String DB_NAME = "items_liked.db";

    // 数据库帐号表名字
    private static final String DB_TABLE_NAME = "TABLE_NAME";
    public static final String TABLE_MAINID = "_id";
    public static final String TABLE_ID = "id";
    public static final String TABLE_TYPE = "type";
    public static final String TABLE_CONTENT = "content";

    // 类型
    public static final int LIKED = 1;

    // 数据库版本
    private static final int DB_VERSION = 1;

    // 本地Context对象
    private Context mContext = null;

    // 创建一个表
    private final String DB_CREATE_TABCOUNT = "CREATE TABLE " + DB_TABLE_NAME + " (" + TABLE_MAINID + " INTEGER PRIMARY KEY," + TABLE_ID + " text," + TABLE_TYPE + " INTEGER,"
            + TABLE_CONTENT + " text)";

    // 执行open（）打开数据库时，保存返回的数据库对象
    private SQLiteDatabase mSQLiteDatabase = null;

    // 由SQLiteOpenHelper继承过来
    private DatabaseHelper mDatabaseHelper = null;

    private static MySqlLiteDataBase mySqlLiteDataBase;

    public static synchronized MySqlLiteDataBase getInstance(Context context) {
        if (mySqlLiteDataBase == null) {
            mySqlLiteDataBase = new MySqlLiteDataBase( context );
        }

        return mySqlLiteDataBase;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super( context, DB_NAME, null, DB_VERSION );

        }

        /* 创建一个表 */
        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL( DB_CREATE_TABCOUNT );
        }

        /* 升级数据库 */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL( "DROP TABLE IF EXISTS " + DB_TABLE_NAME );
            onCreate( db );
        }
    }

    /* 构造函数-取得Context */
    private MySqlLiteDataBase(Context context) {
        mContext = context;
    }

    // 打开数据库，返回数据库对象
    public synchronized boolean open() {
        try {
            if (mDatabaseHelper == null) {
                mDatabaseHelper = new DatabaseHelper( mContext );
            }
            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase( );
            mDatabaseHelper.onOpen( mSQLiteDatabase );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 关闭数据库
    public void close() {
        mSQLiteDatabase.close( );
        mDatabaseHelper.close( );
    }

    /**
     * 
     * @param id
     *            覆盖存储
     * @param type
     * @param content
     * @return
     */
    public boolean insert(String id, int type, String content) {
        deleteDataById( id );
        ContentValues initialValues = new ContentValues( );
        initialValues.put( TABLE_ID, id );
        initialValues.put( TABLE_TYPE, type );
        initialValues.put( TABLE_CONTENT, content );
        // MyLog.I( TAG, initialValues.toString( ) );
        return mSQLiteDatabase.insert( DB_TABLE_NAME, null, initialValues ) > 0 ? true : false;
    }

    /**
     * 添加喜欢,not need use db open and close
     * 
     * @param id
     * @param type
     * @param content
     * @return
     */
    public boolean insertLiked(String id, Holder holder) {
        if (!open( )) {
            return false;
        }
        deleteDataById( id );
        ContentValues initialValues = new ContentValues( );
        String content = JSON.toJSONString( holder );
        initialValues.put( TABLE_ID, id );
        initialValues.put( TABLE_TYPE, LIKED );
        initialValues.put( TABLE_CONTENT, content );
        // MyLog.I( TAG, content );
        // MyLog.I( TAG, initialValues.toString( ) );

        boolean flag = mSQLiteDatabase.insert( DB_TABLE_NAME, null, initialValues ) > 0 ? true : false;
        close( );
        Util.collectChange( mContext );
        return flag;
    }

    public Holder getItem(String id) {
        if (!open( ))
            return null;
        if (getCount( id ) == 1) {
            Cursor mCursor = mSQLiteDatabase.query( DB_TABLE_NAME, null, TABLE_ID + "=?", new String[] { id + "" }, null, null, null );
            if (mCursor != null) {
                mCursor.moveToFirst( );
                String content = mCursor.getString( mCursor.getColumnIndexOrThrow( TABLE_CONTENT ) );
                mCursor.close( );
                close( );
                return JSON.parseObject( content, Holder.class );
            }

        }
        close( );
        return null;
    }

    public synchronized ArrayList<Holder> getItems() {

        if (!open( )) {
            return null;
        }
        Cursor mCursor = mSQLiteDatabase.query( DB_TABLE_NAME, null, TABLE_TYPE + "=?", new String[] { LIKED + "" }, null, null, TABLE_ID + "  desc" );
        if (mCursor == null || mCursor.getCount( ) < 1)
            return null;
        mCursor.moveToFirst( );
        ArrayList<Holder> list = new ArrayList<Holder>( );
        do {
            String content = mCursor.getString( mCursor.getColumnIndexOrThrow( TABLE_CONTENT ) );
            try {
                // MyLog.i( TAG, "content=" + content );
                Holder holder = JSON.parseObject( content, Holder.class );
                list.add( holder );
            } catch (Exception e) {
                break;
            }
        } while (mCursor.moveToNext( ));
        close( );
        return list;
    }

    public int getCount(String id) {
        int count = 0;

        Cursor mCursor = mSQLiteDatabase.query( DB_TABLE_NAME, new String[] { TABLE_ID }, TABLE_ID + "=?", new String[] { id + "" }, null, null, null );
        count = mCursor.getCount( );
        mCursor.close( );

        return count;
    }

    public boolean hasContain(String id) {

        if (getCount( id ) > 0)
            return true;
        else
            return false;
    }

    public boolean deleteDataById(String id) {
        if (id == null)
            return false;
        Util.collectChange( mContext );
        return mSQLiteDatabase.delete( DB_TABLE_NAME, TABLE_ID + "=" + id, null ) > 0;

    }

    // /**
    // * 删除一条数据
    // */
    // public boolean deleteData(String type) {
    // return mSQLiteDatabase.delete( DB_TABLE_NAME, TABLE_TYPE + "=" + type, null ) > 0;
    // }

    // public Cursor fetchAllData() {
    // return mSQLiteDatabase.query( DB_TABLE_NAME, null, null, null, null, null, null );
    // }
    //
    // public synchronized Cursor getTABLE() {
    // return mSQLiteDatabase.query( DB_TABLE_NAME, null, null, null, null, null, null );
    // }

    // /**
    // * 查询指定数据 选择不同的类型
    // */
    // public Cursor selectDataByType(String type) throws SQLException {
    // Cursor mCursor = mSQLiteDatabase.query( true, DB_TABLE_NAME, null, TABLE_TYPE + "=" + type, null, null, null, null, null );
    //
    // if (mCursor != null) {
    // mCursor.moveToFirst( );
    // }
    // return mCursor;
    //
    // }

    // /**
    // * // * 更新一条数据 //
    // */
    // public boolean updateData(long rowId, int type, String data) {
    // ContentValues args = new ContentValues( );
    //
    // return mSQLiteDatabase.update( DB_TABLE_NAME, args, TABLE_UID + "=" + rowId, null ) > 0;
    // }

    private void deleteDataAll() {
        mSQLiteDatabase.delete( DB_TABLE_NAME, null, null );
        Util.collectChange( mContext );
    }

    public Cursor getAllSongs() {
        ContentResolver resolver = mContext.getContentResolver( );
        if (resolver == null) {
            return null;
        }
        return resolver.query( MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER );
    }

    public void clear() {

        deleteDataAll( );
    }

    public void deleteAll(int liked) {
        Util.collectChange( mContext );
        if (!open( )) {
            return;
        }
        mSQLiteDatabase.delete( DB_TABLE_NAME, TABLE_TYPE + "=" + liked, null );
        close( );

    }

}
