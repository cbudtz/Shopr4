package com.nexb.shopr4.dataModel.View;

/**
 * @author Christian
 */
public abstract class ShopListViewContent {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public enum contentType {CATEGORY,ITEM,HEADER,FOOTER};
    public abstract contentType getType();
}
