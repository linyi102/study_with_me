package com.example.bean;

public class RoomState {
    private String action;
    private int roomId;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "RoomState [action=" + action + ", roomId=" + roomId + "]";
    }

}
