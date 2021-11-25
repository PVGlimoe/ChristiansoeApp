package com.codecademy.christiansoe.model;

import java.io.Serializable;
import java.util.List;

public class Theme implements Serializable {

    private int id;
    private String name;
    private List<Route> routes;

    public Theme(int id, String name, List<Route> routes) {
        this.id = id;
        this.name = name;
        this.routes = routes;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
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

    @Override
    public String toString() {
        return id + name + routes;
    }
}
