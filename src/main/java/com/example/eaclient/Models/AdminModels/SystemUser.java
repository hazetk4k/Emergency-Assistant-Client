package com.example.eaclient.Models.AdminModels;

public class SystemUser {
    public  Integer id_syst;
    public String login_syst;
    public String status_syst;

    public Integer getId_syst() {
        return id_syst;
    }

    public void setId_syst(Integer id_syst) {
        this.id_syst = id_syst;
    }

    public String getLogin_syst() {
        return login_syst;
    }

    public void setLogin_syst(String login_syst) {
        this.login_syst = login_syst;
    }

    public String getStatus_syst() {
        return status_syst;
    }

    public void setStatus_syst(String status_syst) {
        this.status_syst = status_syst;
    }

    public SystemUser(Integer id_syst, String login_syst, String status_syst) {
        this.id_syst = id_syst;
        this.login_syst = login_syst;
        this.status_syst = status_syst;
    }
}
