package com.codecademy.christiansoe.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BingoBoard implements Serializable {

    private int id;
    @SerializedName("map")
    private RouteMap routeMap;
    private String name;

    public BingoBoard(int id, RouteMap routeMap, String name) {
        this.id = id;
        this.routeMap = routeMap;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RouteMap getRouteMap() {
        return routeMap;
    }

    public void setRouteMap(RouteMap routeMap) {
        this.routeMap = routeMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
