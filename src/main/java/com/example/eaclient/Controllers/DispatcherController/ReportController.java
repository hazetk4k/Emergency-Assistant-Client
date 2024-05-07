package com.example.eaclient.Controllers.DispatcherController;

import com.example.eaclient.Controllers.WindowManager;
import com.example.eaclient.Models.AllReportsTable;
import com.example.eaclient.Models.Applicant;
import com.example.eaclient.Models.DataReportApplicant;
import com.example.eaclient.Models.DispChoice;
import com.example.eaclient.Models.Report;
import com.example.eaclient.Network.HttpRequests.HttpResponse;
import com.example.eaclient.Network.HttpRequests.SimpleRequestManager;
import com.example.eaclient.Service.ServiceSingleton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
    public ListView<String> recommendedServicesList; //TODO: Проконсультироваться на счет рекомендаций
    @FXML
    public TextArea chosenServicesArea; // Сделано
    @FXML
    public Button buttonConfirmServices;
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
    public TextField casualtiesAmount; //Сделано
    @FXML
    public VBox vboxConfirmStartReaction; //Сделано
    @FXML
    public VBox vboxConfirmChosenServices; //Сделано
    @FXML
    public VBox secondWindowSupport;
    private AllReportsTable reportTableData; //Сделано

    private Report report;

    private Applicant applicant;

    private final Gson gson = new Gson();

    public void initData(AllReportsTable rowData) {
        this.reportTableData = rowData;

        loadChoicesData();
        loadReportApplicantData();
        loadEmergencyData();

        charChoiceBox.setOnAction(event -> {
            kindComboBox.getItems().clear();
            List<String> listOfKinds;
            String selectedValue = charChoiceBox.getValue();
            if (selectedValue != null) {
                try {
                    HttpResponse response = SimpleRequestManager.sendGetRequest("/get-kinds-of-char", "char=" + selectedValue);
                    int code = response.getResponseCode();
                    if (code == 200) {
                        String body = response.getResponseBody();
                        listOfKinds = gson.fromJson(body, List.class);
                        assert listOfKinds != null;
                        kindComboBox.getItems().addAll(listOfKinds);
                        kindComboBox.setDisable(false);
                    } else {
                        System.err.println("Ошибка загрузки!");
                    }
                } catch (IOException e) {
                    System.err.println("Ошибка при выполнении HTTP-запроса: " + e.getMessage());
                }
            }
        });

        kindComboBox.setOnAction(event -> {
            String selectedKind = kindComboBox.getValue();
            if (selectedKind != null) {
                loadRecommendedServices(selectedKind);
            }
        });

    }

    public void loadChoicesData() {
        try {
            HttpResponse httpResponse = SimpleRequestManager.sendGetRequest("/get-dispatcher-choice", "report_id=" + reportTableData.getId());
            int code = httpResponse.getResponseCode();
            if (code == 200) {
                String body = httpResponse.getResponseBody();
                DispChoice dispChoice = gson.fromJson(body, DispChoice.class);

                charChoiceBox.setValue(dispChoice.getName_char());
                kindComboBox.setValue(dispChoice.getName_kind());
                //TODO:Сделать проверку на кастомный вид
                loadRecommendedServices(dispChoice.getName_kind());
                charChoiceBox.setDisable(true);
                kindComboBox.setDisable(true);
                buttonStartReacting.setDisable(true);

                if (dispChoice.getServices() == null || Objects.equals(dispChoice.getServices(), "")) {
                    vboxConfirmChosenServices.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
                    buttonConfirmServices.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
                    buttonConfirmServices.setDisable(false);
                } else {
                    buttonNextWindow.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
                    //TODO: Добавить обработку выбора чекбоксов
                    chosenServicesArea.setText(dispChoice.getServices());
                }
            } else if (code == 404) {
                System.out.println("Заявление еще не просмотрено!");
                vboxConfirmStartReaction.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
                buttonStartReacting.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
            } else {
                System.err.println("Ошибка загрузки!");
            }
        } catch (IOException e) {
            System.err.println("Ошибка загрузки результатов реагирования" + e.getMessage());
        }
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

                String fio = ApplicantProfileController.makeFIO(
                        applicant.getName(),
                        applicant.getSurname(),
                        applicant.getPatronymic()
                );

                applicantNameAndPhone.setText(makeFIOAndPhoneString(
                        fio,
                        applicant.getPhone_number())
                );

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
                } else {
                    casualtiesAmount.setText("Отсутствуют");
                }

            } else {
                System.err.println("Ошибка загрузки");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public String makeFIOAndPhoneString(String fio, String phone) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(fio);
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

    private void loadRecommendedServices(String kind) {
        recommendedServicesList.getItems().clear();
        List<String> listOfServices;
        try {
            HttpResponse response = SimpleRequestManager.sendGetRequest("/get-services-by-kind", "kind=" + kind);
            int code = response.getResponseCode();
            if (code == 200) {
                String body = response.getResponseBody();
                listOfServices = gson.fromJson(body, List.class);
                if (listOfServices != null) {
                    recommendedServicesList.getItems().addAll(listOfServices);
                }
            } else {
                System.err.println("Рекомендации не загружены!");
            }
        } catch (IOException e) {
            System.err.println("Ошибка при выполнении HTTP-запроса: " + e.getMessage());
        }
    }

    public void openApplicantProfile(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DispatcherWindows/ApplicantProfileWindow.fxml"));
            Parent root = loader.load();

            ApplicantProfileController controller = loader.getController();
            controller.initData(applicant);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cleanUpServices(ActionEvent actionEvent) {
        allServicesList.getItems().forEach(checkBox -> checkBox.setSelected(false));
        chosenServicesArea.clear();
    }

    public void confirmStartReacting(ActionEvent actionEvent) {
        if (charChoiceBox.getValue() == null) {
            WindowManager.showAlert("Не заполнены данные", "Необходимо заполнить поле характра ЧС", 2);
            return;
        }

        if (kindComboBox.getValue() == null) {
            WindowManager.showAlert("Не заполнены данные", "Необходимо заполнить поле вида ЧС", 2);
            return;
        }

        try {
            JsonObject jsonObject = new JsonObject();
            LocalDateTime currentDateTime = LocalDateTime.now();

            jsonObject.addProperty("start_action_time", currentDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            jsonObject.addProperty("report_id", reportTableData.getId());
            jsonObject.addProperty("disp_login", ServiceSingleton.getInstance().getCurrentUser());
            jsonObject.addProperty("char_name", charChoiceBox.getValue());
            jsonObject.addProperty("kind_name", kindComboBox.getValue());

            HttpResponse response = SimpleRequestManager.sendPostRequest("/start-action-time", gson.toJson(jsonObject));
            int code = response.getResponseCode();
            if (code == 200) {
                vboxConfirmStartReaction.setStyle("");
                buttonStartReacting.setStyle("");
                vboxConfirmChosenServices.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
                buttonConfirmServices.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
                buttonStartReacting.setDisable(true);
                buttonConfirmServices.setDisable(false);
            } else {
                System.err.println("Ошибка загрузки!");
            }
        } catch (IOException e) {
            System.err.println("Ошибка при выполнении HTTP-запроса: " + e.getMessage());
        }
    }

    public void moveToNextWindow(ActionEvent actionEvent) {
        firstWindowSupport.setVisible(false);
        secondWindowSupport.setVisible(true);

    }

    public void confirmChosenServices(ActionEvent actionEvent) {
        //TODO:Доделать
        if (chosenServicesArea.getText().isEmpty()) {
            chosenServicesArea.setStyle("-fx-prompt-text-fill: red");
            chosenServicesArea.setPromptText("Выберите службы реагирования!");
        } else {
            buttonConfirmServices.setStyle("");
            buttonNextWindow.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
            buttonConfirmServices.setDisable(true);
            buttonNextWindow.setDisable(false);
        }
    }
}
