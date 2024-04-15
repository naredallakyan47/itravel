package com.example.itravel;

public class Place {
    private String name;
    private String imageUrl;

    public Place() {

    }

    public Place(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }



    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
