package com.example.eaclient.Models;

import java.util.List;

public class KindRelations {
    String kind_name;
    List<String> services;

    public String getKind_name() {
        return kind_name;
    }

    public void setKind_name(String kind_name) {
        this.kind_name = kind_name;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public KindRelations(String kind_name, List<String> services) {
        this.kind_name = kind_name;
        this.services = services;
    }
}
