package com.example.eaclient.Models.AdminModels;

public class AutoTable {
    public int auto_id;
    public String mark;
    public String number;
    public String district;
    public String service;

    public int getAuto_id() {
        return auto_id;
    }

    public void setAuto_id(int auto_id) {
        this.auto_id = auto_id;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public AutoTable(int auto_id, String mark, String number, String district, String service) {
        this.auto_id = auto_id;
        this.mark = mark;
        this.number = number;
        this.district = district;
        this.service = service;
    }
}
