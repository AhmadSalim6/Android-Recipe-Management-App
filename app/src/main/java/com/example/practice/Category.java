package com.example.practice;

public class Category {
    private String name;
    private String photoId; // resource id for the image

    public Category(String name, String photoId) {
        this.name = name;
        this.photoId = photoId;
    }

    public String getName() {
        return name;
    }

    public String getPhotoId() {
        return photoId;
    }
}
