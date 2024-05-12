package com.example.eaclient.Models.ReportWindowModels;

public class StageTimings {
    public String start_time;
    public String call_services_time;

    public String received_data_time;
    public String call_add_services_time;

    public String end_time;

    public String getStart_time() {
        return start_time;
    }

    public String getCall_services_time() {
        return call_services_time;
    }

    public String getReceived_data_time() {
        return received_data_time;
    }

    public String getCall_add_services_time() {
        return call_add_services_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public StageTimings(String start_time, String call_services_time, String received_data_time, String call_add_services_time, String end_time) {
        this.start_time = start_time;
        this.call_services_time = call_services_time;
        this.received_data_time = received_data_time;
        this.call_add_services_time = call_add_services_time;
        this.end_time = end_time;
    }
}
