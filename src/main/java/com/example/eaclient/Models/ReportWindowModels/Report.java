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
    public String received_datetime = null;

    public  String recommendations;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAdditional_info() {
        return additional_info;
    }

    public String getPlace() {
        return place;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Boolean getAre_there_any_casualties() {
        return are_there_any_casualties;
    }

    public String getCasualties_amount() {
        return casualties_amount;
    }

    public Boolean getUser_in_danger() {
        return user_in_danger;
    }

    public String getReceived_datetime() {
        return received_datetime;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public Report(String type, String additional_info, String place, String timestamp, Boolean are_there_any_casualties, String casualties_amount, Boolean user_in_danger, Boolean was_seen, String received_datetime, String recommendations) {
        this.type = type;
        this.additional_info = additional_info;
        this.place = place;
        this.timestamp = timestamp;
        this.are_there_any_casualties = are_there_any_casualties;
        this.casualties_amount = casualties_amount;
        this.user_in_danger = user_in_danger;
        this.was_seen = was_seen;
        this.received_datetime = received_datetime;
        this.recommendations = recommendations;
    }
}
