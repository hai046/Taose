package com.buy.holder;

import java.util.List;

public class ParentsCategory {
    private ParentsCategoryItem categorys;
    private boolean hasCategory;
    private List<Holder>list;
    public ParentsCategoryItem getCategorys() {
        return categorys;
    }
    public void setCategorys(ParentsCategoryItem categorys) {
        this.categorys = categorys;
    }
    public boolean isHasCategory() {
        return hasCategory;
    }
    public void setHasCategory(boolean hasCategory) {
        this.hasCategory = hasCategory;
    }
    public List<Holder> getList() {
        return list;
    }
    public void setList(List<Holder> list) {
        this.list = list;
    }
    
}
