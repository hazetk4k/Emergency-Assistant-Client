package com.example.eaclient.Models.Statistics;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.List;

public class FinishedReports {
    private final SimpleIntegerProperty reportId;
    private final SimpleStringProperty receiveTime;
    private final SimpleStringProperty endActionsTime;
    private final SimpleStringProperty addressDistrict;
    private final SimpleStringProperty typeName;
    private final SimpleListProperty<String> allServices;

    public FinishedReports(int reportId, String receiveTime, String endActionsTime, String addressDistrict, String typeName, List<String> allServices) {
        this.reportId = new SimpleIntegerProperty(reportId);
        this.receiveTime = new SimpleStringProperty(receiveTime);
        this.endActionsTime = new SimpleStringProperty(endActionsTime);
        this.addressDistrict = new SimpleStringProperty(addressDistrict);
        this.typeName = new SimpleStringProperty(typeName);
        this.allServices = new SimpleListProperty<>(FXCollections.observableArrayList(allServices));
    }

    public int getReportId() {
        return reportId.get();
    }

    public SimpleIntegerProperty reportIdProperty() {
        return reportId;
    }

    public String getReceiveTime() {
        return receiveTime.get();
    }

    public SimpleStringProperty receiveTimeProperty() {
        return receiveTime;
    }

    public String getEndActionsTime() {
        return endActionsTime.get();
    }

    public SimpleStringProperty endActionsTimeProperty() {
        return endActionsTime;
    }

    public String getAddressDistrict() {
        return addressDistrict.get();
    }

    public SimpleStringProperty addressDistrictProperty() {
        return addressDistrict;
    }

    public String getTypeName() {
        return typeName.get();
    }

    public SimpleStringProperty typeNameProperty() {
        return typeName;
    }

    public List<String> getAllServices() {
        return allServices.get();
    }

    public SimpleListProperty<String> allServicesProperty() {
        return allServices;
    }
}
