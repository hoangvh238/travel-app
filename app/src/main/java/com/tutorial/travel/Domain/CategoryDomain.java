package com.tutorial.travel.Domain;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "popularCategories")
public class CategoryDomain {
    @PrimaryKey(autoGenerate = true)
    private int categoryId;
    private String title;
    private String picPath;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public CategoryDomain(String title, String picPath) {
        this.title = title;
        this.picPath = picPath;
    }
}
