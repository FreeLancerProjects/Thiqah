package com.creative.share.apps.thiqah.models;

import java.io.Serializable;

public class CategoryModel implements Serializable {
    private int img;
    private String title;

    public CategoryModel(int img, String title) {
        this.img = img;
        this.title = title;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
