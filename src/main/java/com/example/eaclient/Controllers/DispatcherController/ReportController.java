package com.example.eaclient.Controllers.DispatcherController;

import com.example.eaclient.Models.AllReportsTable;
import com.example.eaclient.Network.HttpRequests.HttpResponse;
import com.example.eaclient.Network.HttpRequests.SimpleRequestManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ReportController {
    @FXML
    public VBox firstWindowSupport;
    @FXML
    public TextField typeNameField;
    @FXML
    public ChoiceBox<String> charChoiceBox;
    @FXML
    public ComboBox<String> kindComboBox;
    @FXML
    public TextField placeNameField;
    @FXML
    public TextField dateTimeField;
    @FXML
    public TextField applicantStatusField;
    @FXML
    public TextField applicantProfileStatus;
    @FXML
    public TextArea additionalInfoArea;
    @FXML
    public Button buttonStartReacting;
    @FXML
    public ListView<String> recommendedServicesList;
    @FXML
    public TextArea chosenServicesArea;
    @FXML
    public Button buttonCallingServices;
    @FXML
    public Button buttonNextWindow;
    @FXML
    public Button btnCleanServices;
    @FXML
    public ListView<CheckBox> allServicesList;
    @FXML
    public Button buttonOpenApplicantProfile;
    @FXML
    public TextField applicantNameAndPhone;
    @FXML
    private AllReportsTable reportTableData;

    private final Gson gson = new Gson();


    public void initData(AllReportsTable rowData) {
        this.reportTableData = rowData;
        //loadReportApplicantData();
        loadOtherData();
    }

    public void loadReportApplicantData() {
        try {
            HttpResponse response = SimpleRequestManager.sendGetRequest("/get-report-applicant-data", "report_id=" + reportTableData.getId());
            int code = response.getResponseCode();
            if (code == 200) {
                String body = response.getResponseBody();

            } else {
                System.err.println("Ошибка загрузки");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void loadOtherData() {
        try {
            HttpResponse response = SimpleRequestManager.sendGetRequest("/set-up-emergency-data");
            int code = response.getResponseCode();
            if (code == 200) {
                String body = response.getResponseBody();
                Type type = new TypeToken<Map<String, List<String>>>() {
                }.getType();
                Map<String, List<String>> emergencyData = gson.fromJson(body, type);

                List<String> chars = emergencyData.get("chars");
                List<String> kinds = emergencyData.get("kinds");
                List<String> services = emergencyData.get("services");
                List<CheckBox> checkBoxList = new ArrayList<>();
                for (String service : services) {
                    CheckBox checkBox = new CheckBox(service);
                    checkBoxList.add(checkBox);
                }

                charChoiceBox.getItems().addAll(chars);
                kindComboBox.getItems().addAll(kinds);
                allServicesList.getItems().addAll(checkBoxList);
            } else {
                System.err.println("Ошибка загрузки!");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void startReacting(ActionEvent actionEvent) {
        //TODO:Доделать
    }

    public void openNextWindow(ActionEvent actionEvent) {
        firstWindowSupport.setVisible(false);
    }

    public void callServices(ActionEvent actionEvent) {
        //TODO:Доделать
    }

    public void openApplicantProfile(ActionEvent actionEvent) {
        //TODO:Доделать (интерфейс + функционал)
    }

    public void cleanUpServices(ActionEvent actionEvent) {
    }
}
