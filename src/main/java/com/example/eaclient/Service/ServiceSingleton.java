package com.example.eaclient.Service;

import com.example.eaclient.Controllers.DispatcherController.AllReportsController;
import com.example.eaclient.Models.AllReportsTable;

public class ServiceSingleton {
    static ServiceSingleton INSTANCE;
    private Boolean ifClosed = true;
    private String CurrentUser;

    private int CurrentUserId;

    private AllReportsController allReportsController;

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

    public int getCurrentUserId() {
        return CurrentUserId;
    }

    public void setCurrentUserId(int currentUserId) {
        CurrentUserId = currentUserId;
    }

    public void setAllReportsController(AllReportsController allReportsController) {
        this.allReportsController = allReportsController;
    }

    public void deliverDataToController(AllReportsTable newData) {
        if (allReportsController != null) {
            allReportsController.updateTableWithData(newData);
        }
    }


}
