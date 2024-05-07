package com.example.eaclient.Models;

public class DispChoice {
    public String name_char;
    public String name_kind;
    public String services;

    public String getName_char() {
        return name_char;
    }

    public void setName_char(String name_char) {
        this.name_char = name_char;
    }

    public String getName_kind() {
        return name_kind;
    }

    public void setName_kind(String name_kind) {
        this.name_kind = name_kind;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public DispChoice(String name_char, String name_kind, String services) {
        this.name_char = name_char;
        this.name_kind = name_kind;
        this.services = services;
    }
}
