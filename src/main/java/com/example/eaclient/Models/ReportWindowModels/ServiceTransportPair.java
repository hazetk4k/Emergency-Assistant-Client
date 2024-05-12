package com.example.eaclient.Models.ReportWindowModels;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ServiceTransportPair {
    private String service;
    private String transport;

    public String getService() {
        return service;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public StringProperty serviceProperty() {
        return new SimpleStringProperty(service);
    }

    public ServiceTransportPair(String service, String transport) {
        this.service = service;
        this.transport = transport;
    }
}
