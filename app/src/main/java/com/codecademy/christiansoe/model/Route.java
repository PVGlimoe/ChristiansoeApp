package com.codecademy.christiansoe.model;

import java.io.Serializable;

public class Route implements Serializable {

    private int id;
    private String name;
    private double length;
    private int hikingTime;
    private String description;
    private int bingoBoardId;



    public Route(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Route() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public int getHikingTime() {
        return hikingTime;
    }

    public void setHikingTime(int hikingTime) {
        this.hikingTime = hikingTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBingoBoardId() {
        return bingoBoardId;
    }

    public void setBingoBoardId(int bingoBoardId) {
        this.bingoBoardId = bingoBoardId;
    }

    @Override
    public String toString() {
        return id + name;
    }
}
