package com.buy.holder;

import java.util.Date;

public class Comment {
    private long id;
    private String commentorName;
    private String replieeName;// repliee_name
    private int repliedCommentId;
    private int parentId;
    private int articleID;
    private int type;
    private String msg;
    private Date createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCommentorName() {
        return commentorName;
    }

    public void setCommentorName(String commentorName) {
        this.commentorName = commentorName;
    }

    public String getReplieeName() {
        return replieeName;
    }

    public void setReplieeName(String replieeName) {
        this.replieeName = replieeName;
    }

    public int getRepliedCommentId() {
        return repliedCommentId;
    }

    public void setRepliedCommentId(int repliedCommentId) {
        this.repliedCommentId = repliedCommentId;
    }

    public int getArticleID() {
        return articleID;
    }

    public void setArticleID(int articleID) {
        this.articleID = articleID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

}
