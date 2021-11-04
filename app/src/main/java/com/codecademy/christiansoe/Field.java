package com.codecademy.christiansoe;

import java.io.Serializable;

public class Field implements Serializable {

    private int id;
    private String name;
    private String description;
    private String pictureUrl;
    private String soundUrl;


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getSoundUrl() {
        return soundUrl;
    }

}
