package com.example.eaclient.Models.ReportWindowModels;

public class Report {

    public String type;
    public String additional_info;
    public String place;
    public String timestamp;
    public Boolean are_there_any_casualties;
    public String casualties_amount;
    public Boolean user_in_danger;
    public Boolean was_seen;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getAre_there_any_casualties() {
        return are_there_any_casualties;
    }

    public void setAre_there_any_casualties(Boolean are_there_any_casualties) {
        this.are_there_any_casualties = are_there_any_casualties;
    }

    public String getCasualties_amount() {
        return casualties_amount;
    }

    public void setCasualties_amount(String casualties_amount) {
        this.casualties_amount = casualties_amount;
    }

    public Boolean getUser_in_danger() {
        return user_in_danger;
    }

    public void setUser_in_danger(Boolean user_in_danger) {
        this.user_in_danger = user_in_danger;
    }

    public Boolean getWas_seen() {
        return was_seen;
    }

    public void setWas_seen(Boolean was_seen) {
        this.was_seen = was_seen;
    }

    public Report(String type, String additional_info, String place, String timestamp, Boolean are_there_any_casualties, String casualties_amount, Boolean user_in_danger, Boolean was_seen) {
        this.type = type;
        this.additional_info = additional_info;
        this.place = place;
        this.timestamp = timestamp;
        this.are_there_any_casualties = are_there_any_casualties;
        this.casualties_amount = casualties_amount;
        this.user_in_danger = user_in_danger;
        this.was_seen = was_seen;
    }
}
