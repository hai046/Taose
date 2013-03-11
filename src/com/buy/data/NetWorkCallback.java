package com.buy.data;

import android.net.NetworkInfo.State;

public interface NetWorkCallback {
    public void netChanger(int typeMobile, State mobile,boolean isFastNetwork);
}
