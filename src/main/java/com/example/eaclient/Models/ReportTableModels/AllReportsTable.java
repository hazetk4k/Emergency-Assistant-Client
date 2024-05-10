package com.example.eaclient.Models.ReportTableModels;

public class AllReportsTable {
    public int id;
    public String type;
    public String timestamp;
    public String place;
    public String fio;
    public String stage_name;

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

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getStage_name() {
        return stage_name;
    }

    public void setStage_name(String stage_name) {
        this.stage_name = stage_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AllReportsTable(int id, String type, String timestamp, String place, String fio, String stage_name) {
        this.id = id;
        this.type = type;
        this.timestamp = timestamp;
        this.place = place;
        this.fio = fio;
        this.stage_name = stage_name;
    }

    @Override
    public String toString() {
        return "id: " + id + ", type: " + type + ", timestamp: " + timestamp + ", place: " + place + ", FIO: " + fio + ", Просмотрено: " + stage_name;
    }
}