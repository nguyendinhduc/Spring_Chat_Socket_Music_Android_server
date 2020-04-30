package com.t3h.demofragment.viewpager;

public class ItemData {
    private String title;
    private String content;
    private String image;
    private String linkDetail;

    public ItemData(String title, String content, String image, String linkDetail) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.linkDetail = linkDetail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLinkDetail() {
        return linkDetail;
    }

    public void setLinkDetail(String linkDetail) {
        this.linkDetail = linkDetail;
    }
}
