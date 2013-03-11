package com.buy.holder;

import java.util.Date;

import com.buy.util.Util;

public class Article {
    private int id;
    private String articleName;
    private String articleBody;
    private int categoryId;
    private int status;
    private Date createTime;
    private int replyNum;
    
    private int has_see;
    

    public int getHas_see() {
        return has_see;
    }

    public void setHas_see(int has_see) {
        this.has_see = has_see;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getArticleBody() {
        return Util.transStrToParagraph( articleBody );
    }

    public void setArticleBody(String articleBody) {
        this.articleBody = articleBody;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
