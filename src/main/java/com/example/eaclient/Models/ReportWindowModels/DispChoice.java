package com.example.eaclient.Models.ReportWindowModels;

public class DispChoice {
    public String name_char;
    public String name_kind;
    public String services;
    public int dead_amount;
    public int people_amount;
    public int dispatcher_id;
    public String additional_services;
    public String stage;

    public String district_name;

    public String getName_char() {
        return name_char;
    }

    public String getName_kind() {
        return name_kind;
    }

    public String getServices() {
        return services;
    }

    public int getDead_amount() {
        return dead_amount;
    }

    public int getPeople_amount() {
        return people_amount;
    }

    public int getDispatcher_id() {
        return dispatcher_id;
    }

    public String getAdditional_services() {
        return additional_services;
    }

    public String getStage() {
        return stage;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public DispChoice(String name_char, String name_kind, String services, int dead_amount, int people_amount, int dispatcher_id, String additional_services, String stage, String district_name) {
        this.name_char = name_char;
        this.name_kind = name_kind;
        this.services = services;
        this.dead_amount = dead_amount;
        this.people_amount = people_amount;
        this.dispatcher_id = dispatcher_id;
        this.additional_services = additional_services;
        this.stage = stage;
        this.district_name = district_name;
    }
}
