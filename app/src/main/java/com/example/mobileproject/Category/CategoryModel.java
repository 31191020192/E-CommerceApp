package com.example.mobileproject.Category;

import java.io.Serializable;

public class CategoryModel implements Serializable {
    private String CateID;
    private String CateName;
    private String Icon;

    public CategoryModel(String cateID, String cateName, String icon) {
        CateID = cateID;
        CateName = cateName;
        Icon = icon;
    }

    public CategoryModel(){

    }

    public String getCateID() {
        return CateID;
    }

    public void setCateID(String cateID) {
        CateID = cateID;
    }

    public String getCateName() {
        return CateName;
    }

    public void setCateName(String cateName) {
        CateName = cateName;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }
}
