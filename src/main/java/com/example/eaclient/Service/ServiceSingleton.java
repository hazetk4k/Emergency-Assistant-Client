package com.example.eaclient.Service;

public class ServiceSingleton {
    static ServiceSingleton INSTANCE;
    Boolean ifClosed = true;

    public static ServiceSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServiceSingleton();
        }
        return INSTANCE;
    }

    public Boolean getIfClosed() {
        return ifClosed;
    }

    public void setIfClosed(Boolean ifClosed) {
        this.ifClosed = ifClosed;
    }
}
