package com.codecademy.christiansoe.model;

import java.io.Serializable;

public class UserBingoBoard implements Serializable {

    private int id;
    private boolean done;
    private int bingoBoardId;
    private String userId;

    public UserBingoBoard(boolean done, int bingoBoardId, String userId) {
        this.done = done;
        this.bingoBoardId = bingoBoardId;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getBingoBoardId() {
        return bingoBoardId;
    }

    public void setBingoBoardId(int bingoBoardId) {
        this.bingoBoardId = bingoBoardId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
