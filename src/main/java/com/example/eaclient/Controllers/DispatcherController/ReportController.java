package com.example.eaclient.Controllers.DispatcherController;

import com.example.eaclient.Models.AllReportsTable;
import com.example.eaclient.Models.Applicant;
import com.example.eaclient.Models.DataReportApplicant;
import com.example.eaclient.Models.Report;
import com.example.eaclient.Network.HttpRequests.HttpResponse;
import com.example.eaclient.Network.HttpRequests.SimpleRequestManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    public TextField typeNameField; //Сделано
    @FXML
    public ChoiceBox<String> charChoiceBox;
    @FXML
    public ComboBox<String> kindComboBox;
    @FXML
    public TextField placeNameField; //Сделано
    @FXML
    public TextField dateTimeField; //Сделано
    @FXML
    public TextField applicantStatusField; //Сделано
    @FXML
    public TextArea additionalInfoArea; //Сделано
    @FXML
    public Button buttonStartReacting;
    @FXML
    public ListView<String> recommendedServicesList;
    @FXML
    public TextArea chosenServicesArea; // Сделано
    @FXML
    public Button buttonCallingServices;
    @FXML
    public Button buttonNextWindow;
    @FXML
    public Button btnCleanServices; //Сделано
    @FXML
    public ListView<CheckBox> allServicesList; //Сделано
    @FXML
    public Button buttonOpenApplicantProfile;
    @FXML
    public TextField applicantNameAndPhone; //Сделано
    @FXML
    public TextField casualtiesAmount;
    private AllReportsTable reportTableData; //Сделано

    private Report report;

    private Applicant applicant;

    private final Gson gson = new Gson();


    public void initData(AllReportsTable rowData) {
        this.reportTableData = rowData;
        loadReportApplicantData();
        loadEmergencyData();
    }

    public void loadReportApplicantData() {
        try {
            HttpResponse response = SimpleRequestManager.sendGetRequest("/get-report-applicant-data", "report_id=" + reportTableData.getId());
            int code = response.getResponseCode();
            if (code == 200) {
                String body = response.getResponseBody();
                DataReportApplicant data = gson.fromJson(body, DataReportApplicant.class);
                report = data.getReport();
                applicant = data.getApplicant();

                applicantNameAndPhone.setText(makeFIOAndPhoneString(applicant.getName(),
                        applicant.getSurname(),
                        applicant.getPatronymic(),
                        applicant.getPhone_number()));
                placeNameField.setText(report.getPlace());
                dateTimeField.setText(report.getTimestamp());
                typeNameField.setText(report.getType());

                String user_in_danger;
                if (report.getUser_in_danger()) {
                    user_in_danger = "В опасности";
                } else {
                    user_in_danger = "В безопасности";
                }
                applicantStatusField.setText(user_in_danger);

                additionalInfoArea.setText(report.getAdditional_info());
                if (report.getAre_there_any_casualties()) {
                    casualtiesAmount.setText(report.getCasualties_amount());
                } else{
                    casualtiesAmount.setText("Отсутствуют");
                }

            } else {
                System.err.println("Ошибка загрузки");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public String makeFIOAndPhoneString(String name, String surname, String patronymic, String phone) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(surname.substring(0, 1).toUpperCase()).append(surname.substring(1).toLowerCase()).append(" ")
                .append(name.substring(0, 1).toUpperCase()).append(name.substring(1).toLowerCase());

        if (patronymic != null) {
            stringBuilder.append(" ").append(patronymic.substring(0, 1).toUpperCase()).append(patronymic.substring(1).toLowerCase());
        }

        stringBuilder.append(". Телефон: +375").append(phone);
        return stringBuilder.toString();
    }

    public void loadEmergencyData() {
        try {
            HttpResponse response = SimpleRequestManager.sendGetRequest("/set-up-emergency-data");
            int code = response.getResponseCode();
            if (code == 200) {
                String body = response.getResponseBody();
                Type type = new TypeToken<Map<String, List<String>>>() {
                }.getType();
                Map<String, List<String>> emergencyData = gson.fromJson(body, type);

                List<String> chars = emergencyData.get("chars");
                List<String> services = emergencyData.get("services");
                List<CheckBox> checkBoxList = new ArrayList<>();
                for (String service : services) {
                    CheckBox checkBox = new CheckBox(service);
                    checkBoxList.add(checkBox);
                }

                charChoiceBox.getItems().addAll(chars);
                allServicesList.getItems().addAll(checkBoxList);

                for (CheckBox checkBox : checkBoxList) {
                    checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            chosenServicesArea.appendText(checkBox.getText() + "\n");
                        } else {
                            String text = chosenServicesArea.getText().replace(checkBox.getText() + "\n", "");
                            chosenServicesArea.setText(text);
                        }
                    });
                }

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

    public void callServices(ActionEvent actionEvent) {
        //TODO:Доделать
    }

    public void openNextWindow(ActionEvent actionEvent) {
        firstWindowSupport.setVisible(false);
    }

    public void finishReacting(ActionEvent actionEvent) {
    }

    public void openApplicantProfile(ActionEvent actionEvent) {
        //TODO:Доделать (интерфейс + функционал)
    }

    public void cleanUpServices(ActionEvent actionEvent) {
        allServicesList.getItems().forEach(checkBox -> checkBox.setSelected(false));
        chosenServicesArea.clear();
    }
}
