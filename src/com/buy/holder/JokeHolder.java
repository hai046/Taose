package com.buy.holder;

import java.util.Date;

public class JokeHolder {

    private String content;
    private Date date;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    @Override
    public String toString() {
        return "JokeHolder [content=" + content + ", date=" + date + "]";
    }
    
    
}
