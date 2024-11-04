package com.tutorial.travel.Domain;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "populars",
        foreignKeys = @ForeignKey(
                entity = CategoryDomain.class,
                parentColumns = "categoryId",
                childColumns = "categoryId",
                onDelete = ForeignKey.NO_ACTION
        ))
public class PopularDomain implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String placeName;
    private String location;
    private String shortDescription;
    private String detailDescription;
    private String imageUrl;
    private int categoryId;

    public PopularDomain(String placeName, String location, String shortDescription, String detailDescription, String imageUrl, int categoryId) {
        this.placeName = placeName;
        this.location = location;
        this.shortDescription = shortDescription;
        this.detailDescription = detailDescription;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDetailDescription() {
        return detailDescription;
    }

    public void setDetailDescription(String detailDescription) {
        this.detailDescription = detailDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
