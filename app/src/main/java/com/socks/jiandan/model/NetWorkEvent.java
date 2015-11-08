package com.socks.jiandan.model;

public class NetWorkEvent {

    public static final int AVAILABLE = 1;
    public static final int UNAVAILABLE = -1;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public NetWorkEvent(int type) {
        this.type = type;
    }

}
