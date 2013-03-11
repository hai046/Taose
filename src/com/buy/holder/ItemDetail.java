package com.buy.holder;

import java.util.List;

public class ItemDetail {

    private long volume;
    private int id, cashOndelivery, sellerCreditScore;
    private String detailUrl;
    private long numIid;
    private String title;
    private String nick;
    private String type;
    private long cId;
    private String picUrl;
    private long num;
    // private int validThru;
    // private Date listTime;
    // private Date delistTime;
    private String stuffStatus;
    private String location;
    private String price;
    private float postFee;
    private float expressFee;
    private float emsFee;
    private int hasDiscount;
    private String freightPayer;
    private int hasInvoice;
    private int hasWarranty;
    private int hasShowcase;
    // private Date modified;
    private String approveStatus;
    private long productId;
    private String outerId;
    private int isVirtual;
    // private int isTaobao;
    // private int isEx;
    // private int isTiming;
    // private Date createTime;
    // private Date updateTime;

    private String desc;
    private List<String> imgs;

    @Override
    public String toString() {
        return "ItemDetail [id=" + id + ", detailUrl=" + detailUrl + ", numIid=" + numIid + ", title=" + title + ", nick=" + nick + ", type=" + type + ", cId=" + cId + ", picUrl="
                + picUrl + ", num=" + num + ", stuffStatus=" + stuffStatus + ", location=" + location + ", price=" + price + ", postFee=" + postFee + ", expressFee=" + expressFee
                + ", emsFee=" + emsFee + ", hasDiscount=" + hasDiscount + ", freightPayer=" + freightPayer + ", hasInvoice=" + hasInvoice + ", hasWarranty=" + hasWarranty
                + ", hasShowcase=" + hasShowcase + ", approveStatus=" + approveStatus + ", productId=" + productId + ", outerId=" + outerId + ", isVirtual=" + isVirtual
                + ", desc=" + desc + ", imgs=" + imgs + "]";
    }

    public int getCashOndelivery() {
        return cashOndelivery;
    }

    public void setCashOndelivery(int cashOndelivery) {
        this.cashOndelivery = cashOndelivery;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public int getSellerCreditScore() {
        return sellerCreditScore;
    }

    public void setSellerCreditScore(int sellerCreditScore) {
        this.sellerCreditScore = sellerCreditScore;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public long getNumIid() {
        return numIid;
    }

    public void setNumIid(long numIid) {
        this.numIid = numIid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getcId() {
        return cId;
    }

    public void setcId(long cId) {
        this.cId = cId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public String getStuffStatus() {
        return stuffStatus;
    }

    public void setStuffStatus(String stuffStatus) {
        this.stuffStatus = stuffStatus;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public float getPostFee() {
        return postFee;
    }

    public void setPostFee(float postFee) {
        this.postFee = postFee;
    }

    public float getExpressFee() {
        return expressFee;
    }

    public void setExpressFee(float expressFee) {
        this.expressFee = expressFee;
    }

    public float getEmsFee() {
        return emsFee;
    }

    public void setEmsFee(float emsFee) {
        this.emsFee = emsFee;
    }

    public int getHasDiscount() {
        return hasDiscount;
    }

    public void setHasDiscount(int hasDiscount) {
        this.hasDiscount = hasDiscount;
    }

    public String getFreightPayer() {
        return freightPayer;
    }

    public void setFreightPayer(String freightPayer) {
        this.freightPayer = freightPayer;
    }

    public int getHasInvoice() {
        return hasInvoice;
    }

    public void setHasInvoice(int hasInvoice) {
        this.hasInvoice = hasInvoice;
    }

    public int getHasWarranty() {
        return hasWarranty;
    }

    public void setHasWarranty(int hasWarranty) {
        this.hasWarranty = hasWarranty;
    }

    public int getHasShowcase() {
        return hasShowcase;
    }

    public void setHasShowcase(int hasShowcase) {
        this.hasShowcase = hasShowcase;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getOuterId() {
        return outerId;
    }

    public void setOuterId(String outerId) {
        this.outerId = outerId;
    }

    public int getIsVirtual() {
        return isVirtual;
    }

    public void setIsVirtual(int isVirtual) {
        this.isVirtual = isVirtual;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

}
