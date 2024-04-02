package com.example.eaclient.Service;

public class ServiceSingleton {
    static ServiceSingleton INSTANCE;
    private Boolean ifClosed = true;

    private String CurrentUser;

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

    public String getCurrentUser() {
        return CurrentUser;
    }

    public void setCurrentUser(String currentUser) {
        CurrentUser = currentUser;
    }
}
