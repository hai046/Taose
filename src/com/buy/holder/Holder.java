package com.buy.holder;

public class Holder {
    @Override
    public String toString() {
        return "Holder [id=" + id + ", cid=" + cid + ", num_iid=" + num_iid + ", title=" + title + ", nick=" + nick + ", pic_url=" + pic_url + ", price=" + price + ", click_url="
                + click_url + ", shop_click_url=" + shop_click_url + ", seller_credit_score=" + seller_credit_score + ", item_location=" + item_location + ", volume=" + volume
                + ", cash_ondelivery=" + cash_ondelivery + "]";
    }

    private String id, cid, num_iid, title, nick, pic_url, price, click_url, shop_click_url, item_location, volume, name;
    private int cash_ondelivery;

   

    private int seller_credit_score;

    public int getSeller_credit_score() {
        return seller_credit_score;
    }

    public void setSeller_credit_score(int seller_credit_score) {
        this.seller_credit_score = seller_credit_score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCash_ondelivery() {
        return cash_ondelivery;
    }

    public void setCash_ondelivery(int cash_ondelivery) {
        this.cash_ondelivery = cash_ondelivery;
    }

    public String getNum_iid() {
        return num_iid;
    }

    public void setNum_iid(String num_iid) {
        this.num_iid = num_iid;
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

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getClick_url() {
        return click_url;
    }

    public void setClick_url(String click_url) {
        this.click_url = click_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getShop_click_url() {
        return shop_click_url;
    }

    public void setShop_click_url(String shop_click_url) {
        this.shop_click_url = shop_click_url;
    }

    public String getItem_location() {
        return item_location;
    }

    public void setItem_location(String item_location) {
        this.item_location = item_location;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

}
