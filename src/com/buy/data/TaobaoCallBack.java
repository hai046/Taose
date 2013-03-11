package com.buy.data;

public interface TaobaoCallBack {

    public <T> void success(T t);
    public void failure(String msg);
}
