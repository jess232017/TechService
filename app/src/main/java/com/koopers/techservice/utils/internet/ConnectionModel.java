package com.koopers.techservice.utils.internet;

public class ConnectionModel {
    private int type;
    private boolean isConnected;

    ConnectionModel(int type, boolean isConnected) {
        this.type = type;
        this.isConnected = isConnected;
    }

    public int getType() {
        return type;
    }

    public boolean getIsConnected() {
        return isConnected;
    }
}
