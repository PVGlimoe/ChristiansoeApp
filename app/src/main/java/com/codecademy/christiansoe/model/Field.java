package com.codecademy.christiansoe.model;

import java.io.Serializable;

public class Field implements Serializable {

    private int id;
    private String name;
    private String description;
    private String pictureUrl;
    private String soundUrl;
    private boolean isMarked;
    private int position;


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

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
