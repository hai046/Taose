package com.buy.holder;

import java.util.Date;

public class TItemRate {
    private int id;
    private long numIid;
    private int annoy;
    private String buyer;
    private long credit;
    private Date commentDate;
    private String deal;
    private long rateId;
    private String content;
    private int type;
    private Date updateTime;
    
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public long getNumIid() {
        return numIid;
    }
    public void setNumIid(long numIid) {
        this.numIid = numIid;
    }
    public int getAnnoy() {
        return annoy;
    }
    public void setAnnoy(int annoy) {
        this.annoy = annoy;
    }
    public String getBuyer() {
        return buyer;
    }
    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }
    public long getCredit() {
        return credit;
    }
    public void setCredit(long credit) {
        this.credit = credit;
    }
    public Date getCommentDate() {
        return commentDate;
    }
    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }
    public String getDeal() {
        return deal;
    }
    public void setDeal(String deal) {
        this.deal = deal;
    }
    public long getRateId() {
        return rateId;
    }
    public void setRateId(long rateId) {
        this.rateId = rateId;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
	@Override
	public String toString() {
		return "TItemRate [id=" + id + ", numIid=" + numIid + ", annoy="
				+ annoy + ", buyer=" + buyer + ", credit=" + credit
				+ ", commentDate=" + commentDate + ", deal=" + deal
				+ ", rateId=" + rateId + ", content=" + content + ", type="
				+ type + ", updateTime=" + updateTime + "]";
	}
    
   
}
