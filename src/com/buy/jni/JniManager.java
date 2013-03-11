//package com.buy.jni;
//
//import java.util.Map;
//
//public class JniManager {
//
//    private static JniManager mJniManager;
//
//    public static synchronized  JniManager getInstance() {
//        mJniManager = new JniManager( );
//        return mJniManager;
//    }
//
//    static {
//        System.loadLibrary( "brunjoy" );
//    }
//
//    public native void setParam(String arg);
//
//    public native String setParams(String... arg);
//
//    public native String getDateByUrl(String url);
//
//    public native String getPostDateByUrl(String url, Map<String, String> params);
//
//}
