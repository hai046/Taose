package com.brunjoy.Zxing.utils;

import java.net.URI;
import java.util.HashMap;

public class TaoseScheme {

    private String taoseUrl;
    private HashMap<String, String> params;

    public TaoseScheme(String taoseUrl) {
        this.taoseUrl = taoseUrl;
        if (taoseUrl != null && taoseUrl.toLowerCase( ).startsWith( "taose://" )) {
            URI mURI = URI.create( taoseUrl );
            if (taoseUrl.indexOf( '?' ) < 0) {
                this.taoseUrl = mURI.getAuthority( );
            } else {
                this.taoseUrl = mURI.getQuery( );
            }
            params = new HashMap<String, String>( );
            starParser( );
        }
        System.out.println( this.taoseUrl );

    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public String toString() {
        if (params == null)
            return "params=null";
        String string = "";
        for (String key : params.keySet( )) {
            string += "[key=" + key + ",value=" + params.get( key ) + "]\t";
        }
        return string;

    }

    private void starParser() {
        if (taoseUrl.contains( "&" )) {
            String[] pars = taoseUrl.split( "&" );
            for (String par : pars) {
                if (par.contains( "=" )) {
                    String[] kv = par.split( "=" );
                    if (kv.length == 2) {
                        params.put( kv[0], kv[1] );
                    }
                }
            }
        }else if(taoseUrl.contains( "=" ))
        {
            String[] kv = taoseUrl.split( "=" );
            if (kv.length == 2) {
                params.put( kv[0], kv[1] );
            }
        }
    }

    public String getValue(String key) {
        if (params != null && params.containsKey( key )) {
            return params.get( key );
        }
        return null;
    }
}
