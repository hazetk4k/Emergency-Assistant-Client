package com.example.eaclient.Models;

public class AllReportsTable {
    private int id;
    private String type;
    private String timestamp;
    private String place;
    private String fio;
    private Boolean wasSeen;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Boolean getWasSeen() {
        return wasSeen;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public void setWasSeen(Boolean wasSeen) {
        this.wasSeen = wasSeen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AllReportsTable(int id, String type, String timestamp, String place, String fio, Boolean wasSeen) {
        this.id = id;
        this.type = type;
        this.timestamp = timestamp;
        this.place = place;
        this.fio = fio;
        this.wasSeen = wasSeen;
    }

    @Override
    public String toString() {
        return "id: " + id + ", type: " + type + ", timestamp: " + timestamp + ", place: " + place + ", FIO: " + fio + ", Просмотрено: " + wasSeen;
    }
}