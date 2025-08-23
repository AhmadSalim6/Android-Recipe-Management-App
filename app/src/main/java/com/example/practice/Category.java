package com.example.practice;

public class Category {
    private String name;
    private int photoId; // resource id for the image

    public Category(String name, int photoId) {
        this.name = name;
        this.photoId = photoId;
    }

    public String getName() {
        return name;
    }

    public int getPhotoId() {
        return photoId;
    }
}
