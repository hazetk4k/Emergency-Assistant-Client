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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    @FXML
    public ChoiceBox<String> districtsChoiceBox;
    @FXML
    public Button buttonConfirmEndReacting;
    @FXML
    public Button buttonCallOtherServices;
    @FXML
    public TextArea otherChosenServicesArea;
    @FXML
    public Button btnCleanNewSevices;
    @FXML
    public ListView<CheckBox> otherServicesList;
    @FXML
    public ListView<String> recommendedServicesList2;
    @FXML
    public Button buttonFirstWindow;
    @FXML
    public TextField diedAmountField;
    @FXML
    public TextField peopleAmountField;
    @FXML
    public RadioButton radioDiedInDisaster;
    @FXML
    public RadioButton radioPeopleAmount;
    @FXML
    public TextArea additionalDataField2; //Сделано
    @FXML
    public TextField userStatusField2; //Сделано
    @FXML
    public TextField casualtiesField2; //Сделано
    @FXML
    public TextField datetimeField2; //Сделано
    @FXML
    public TextField placeField2; //Сделано
    @FXML
    public TextField typeField2;
    @FXML
    public TextField kindField2;
    @FXML
    public TextField charField2;
    @FXML
    public Button buttonConfirmReceivedData;
    //Дополнительные поля
    private AllReportsTable reportTableData;
    private Report report;
    private Applicant applicant;
    private DispChoice dispChoice;
    private List<String> listOfRecommendedServices;
    private List<String> chars;
    private List<String> services;
    //Gson
    private final Gson gson = new Gson();

    // Загрузка окна 1
    public void initData(AllReportsTable rowData) {
        this.reportTableData = rowData;
        loadReportApplicantData();
        loadCharsAndServices();
        setUpServices(allServicesList, chosenServicesArea);
        loadChoicesStage();

        districtsChoiceBox.getItems().addAll("Район 1", "Район 2", "Район 3");
        Tooltip.install(districtsChoiceBox, setToolTip("Выберите район"));

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

    public void loadChoicesStage() {
        try {
            HttpResponse httpResponse = SimpleRequestManager.sendGetRequest("/get-dispatcher-choice", "report_id=" + reportTableData.getId());
            int code = httpResponse.getResponseCode();
            if (code == 200) {
                String body = httpResponse.getResponseBody();
                dispChoice = gson.fromJson(body, DispChoice.class);
                stage1Load();
            } else if (code == 404) {
                System.out.println("Заявление еще не просмотрено!");
                loadTypeKindChar();
                vboxConfirmStartReaction.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
                buttonStartReacting.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
            } else {
                System.err.println("Ошибка загрузки!");
            }
        } catch (IOException e) {
            System.err.println("Ошибка загрузки результатов реагирования" + e.getMessage());
        }
    }

    public void stage1Load() {
        charChoiceBox.setValue(dispChoice.getName_char());
        kindComboBox.setValue(dispChoice.getName_kind());
        loadRecommendedServices(dispChoice.getName_kind());

        charChoiceBox.setDisable(true);
        kindComboBox.setDisable(true);
        buttonStartReacting.setDisable(true);
        if (dispChoice.getServices() == null || Objects.equals(dispChoice.getServices(), "")) {
            vboxConfirmChosenServices.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
            buttonConfirmServices.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
            buttonConfirmServices.setDisable(false);
        } else {
            stage2Load();
        }
    }

    public void stage2Load() {
        setUpChosenServices(allServicesList);
        btnCleanServices.setDisable(true);
        buttonNextWindow.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
        allServicesList.setDisable(true);
        buttonNextWindow.setDisable(false);
        stage3Load();
        chosenServicesArea.setText(dispChoice.getServices());
    }

    public void setUpChosenServices(ListView<CheckBox> list){
        String[] services = dispChoice.getServices().split("\n");
        for (CheckBox checkBox : list.getItems()) {
            String checkBoxText = checkBox.getText().trim();
            for (String service : services) {
                if (service.trim().equals(checkBoxText)) {
                    checkBox.setSelected(true);
                    break;
                }
            }
        }
    }

    public void loadReportApplicantData() { //загружаются заявление и данные о пользователе
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

    public void loadCharsAndServices() { //загружаются характеры и службы
        try {
            HttpResponse response = SimpleRequestManager.sendGetRequest("/set-up-emergency-data");
            int code = response.getResponseCode();
            if (code == 200) {
                String body = response.getResponseBody();
                Type type = new TypeToken<Map<String, List<String>>>() {
                }.getType();
                Map<String, List<String>> emergencyData = gson.fromJson(body, type);

                chars = emergencyData.get("chars");
                services = emergencyData.get("services");
            } else {
                System.err.println("Ошибка загрузки!");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void setUpServices(ListView<CheckBox> list, TextArea area) {
        List<CheckBox> checkBoxList = new ArrayList<>();
        for (String service : services) {
            CheckBox checkBox = new CheckBox(service);
            checkBoxList.add(checkBox);
        }
        list.getItems().addAll(checkBoxList);

        for (CheckBox checkBox : checkBoxList) {
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    area.appendText(checkBox.getText() + "\n");
                } else {
                    String text = area.getText().replace(checkBox.getText() + "\n", "");
                    area.setText(text);
                }
            });
        }
    }

    public void loadTypeKindChar() {
        String type = report.getType();
        try {
            HttpResponse httpResponse = SimpleRequestManager.sendGetRequest("/get-kind-char-by-type", "type_name=" + type);
            int code = httpResponse.getResponseCode();
            if (code == 200) {
                String response = httpResponse.getResponseBody();
                Map<String, String> map = gson.fromJson(response, Map.class);
                String kind_name = map.get("kind_name");
                String char_name = map.get("char_name");
                charChoiceBox.setValue(char_name);
                kindComboBox.setValue(kind_name);
                charChoiceBox.setDisable(true);
                kindComboBox.setDisable(true);
                loadRecommendedServices(kindComboBox.getValue());
            } else if (code == 404) {
                charChoiceBox.getItems().addAll(chars);
                System.out.println("Связь с типом не найдена!");
            } else {
                System.err.println("Ошибка при поиске связей с указанным типом!");
            }
        } catch (IOException e) {
            System.err.println("Ошибка при поиске связанных с типом данных: " + e.getMessage());
        }
    }

    // Загрузка окна 2
    public void stage3Load() {
        typeField2.setText(typeNameField.getText());
        kindField2.setText(kindComboBox.getValue());
        charField2.setText(charChoiceBox.getValue());
        casualtiesField2.setText(casualtiesAmount.getText());
        //TODO: Дополнить адрес районом
        placeField2.setText(placeNameField.getText());
        userStatusField2.setText(applicantStatusField.getText());
        datetimeField2.setText(dateTimeField.getText());
        additionalDataField2.setText(additionalInfoArea.getText());

        if (listOfRecommendedServices != null) {
            recommendedServicesList2.getItems().addAll(listOfRecommendedServices);
        } else recommendedServicesList2.getItems().add("Данный вид происшествия не определен");

        setUpServices(otherServicesList, otherChosenServicesArea);
        setUpChosenServices(otherServicesList);
    }

    public void stage4Load(){}

    public void stage5Load(){}

    // Дополнительные методы
    public Tooltip setToolTip(String text) {
        Tooltip tooltip = new Tooltip(text);
        tooltip.setShowDelay(Duration.millis(500));
        tooltip.setShowDuration(Duration.INDEFINITE);
        return tooltip;
    }

    public String makeFIOAndPhoneString(String fio, String phone) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(fio);
        stringBuilder.append(". Телефон: +375").append(phone);
        return stringBuilder.toString();
    }

    private void loadRecommendedServices(String kind) {
        recommendedServicesList.getItems().clear();
        try {
            HttpResponse response = SimpleRequestManager.sendGetRequest("/get-services-by-kind", "kind=" + kind);
            int code = response.getResponseCode();
            if (code == 200) {
                String body = response.getResponseBody();
                listOfRecommendedServices = gson.fromJson(body, List.class);
                if (listOfRecommendedServices != null) {
                    recommendedServicesList.getItems().addAll(listOfRecommendedServices);
                }
            } else if (code == 404) {
                recommendedServicesList.getItems().add("Данный вид происшествия не определен");
                System.out.println("К данному виду нет рекомендаций");
            } else {
                System.err.println("Рекомендации не загружены!");
            }
        } catch (IOException e) {
            System.err.println("Ошибка при выполнении HTTP-запроса: " + e.getMessage());
        }
    }

    // Кнопки взаимодействия
    public void openApplicantProfile(ActionEvent actionEvent) {
        try {
            buttonOpenApplicantProfile.setDisable(true);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DispatcherWindows/ApplicantProfileWindow.fxml"));
            Parent root = loader.load();

            ApplicantProfileController controller = loader.getController();
            controller.initData(applicant);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(event -> {
                // Включаем кнопку при закрытии окна
                buttonOpenApplicantProfile.setDisable(false);
            });
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cleanUpServices(ActionEvent actionEvent) {
        allServicesList.getItems().forEach(checkBox -> checkBox.setSelected(false));
        chosenServicesArea.clear();
    }

    public void cleanUpNewServices(ActionEvent actionEvent) {

    }

    // Кнопки переходов окно 1 и 2
    public void moveToNextWindow(ActionEvent actionEvent) {
        firstWindowSupport.setVisible(false);
        secondWindowSupport.setVisible(true);
    }

    public void openFirstWindow(ActionEvent actionEvent) {
        secondWindowSupport.setVisible(false);
        firstWindowSupport.setVisible(true);
    }

    //кнопки подтверждения выбора окно 1
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

    public void confirmChosenServices(ActionEvent actionEvent) {
        if (chosenServicesArea.getText().isEmpty()) {
            chosenServicesArea.setStyle("-fx-prompt-text-fill: red");
            chosenServicesArea.setPromptText("Выберите службы реагирования!");
        } else {
            JsonObject jsonObject = new JsonObject();
            LocalDateTime currentDateTime = LocalDateTime.now();
            jsonObject.addProperty("services", chosenServicesArea.getText());
            jsonObject.addProperty("report_id", reportTableData.getId());
            jsonObject.addProperty("confirm_services_time", currentDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            try {
                HttpResponse response = SimpleRequestManager.sendPostRequest("/confirm-chosen-services", gson.toJson(jsonObject));
                int code = response.getResponseCode();
                if (code == 200) {
                    buttonConfirmServices.setStyle("");
                    buttonNextWindow.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
                    buttonConfirmServices.setDisable(true);
                    buttonNextWindow.setDisable(false);
                    stage3Load();
                } else {
                    System.err.println("Ошибка загрузки!");
                }
            } catch (IOException e) {
                System.err.println("Ошибка при выполнении HTTP-запроса: " + e.getMessage());
            }
        }
    }

    //кнопки подтверждания выбора окна 2
    public void confirmReceivedData(ActionEvent actionEvent) {
        //TODO:Сделать
    }

    public void callOtherServices(ActionEvent actionEvent) {
        //TODO:Сделать
    }

    public void confirmEndReacting(ActionEvent actionEvent) {
        //TODO:Сделать
    }
}
