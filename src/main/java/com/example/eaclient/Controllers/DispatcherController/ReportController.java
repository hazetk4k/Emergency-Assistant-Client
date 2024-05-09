package com.example.eaclient.Controllers.DispatcherController;

import com.example.eaclient.Controllers.WindowManager;
import com.example.eaclient.Models.AllReportsTable;
import com.example.eaclient.Models.Applicant;
import com.example.eaclient.Models.DispChoice;
import com.example.eaclient.Models.Report;
import com.example.eaclient.Models.ReportApplicant;
import com.example.eaclient.Service.ServiceSingleton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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
    public Button btnCleanNewServices;
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
    @FXML
    public VBox vboxAdditionalServicesCalling;
    @FXML
    public VBox vboxRecievedData;
    private AllReportsTable reportTableData;
    private Report report;
    private Applicant applicant;
    private DispChoice dispChoice;
    private List<String> listOfRecommendedServices;
    private List<String> chars;
    private List<String> services;

    private final ReportControllerRequests requestsManager = new ReportControllerRequests();

    public void initData(AllReportsTable rowData) {
        this.reportTableData = rowData;
        loadReportApplicantData();
        loadCharsAndServices();
        loadChoicesStage();
        //TODO:ДОДЕЛАТЬ ЭТИ РАЙОНЫ
//        districtsChoiceBox.getItems().addAll("Район 1", "Район 2", "Район 3");
//        Tooltip.install(districtsChoiceBox, setToolTip("Выберите район"));
    }

    //кнопки подтверждения выбора
    public void confirmStartReacting(ActionEvent actionEvent) {
        if (charChoiceBox.getValue() == null) {
            WindowManager.showAlert("Не заполнены данные", "Необходимо заполнить поле характра ЧС", 2);
            return;
        }

        if (kindComboBox.getValue() == null) {
            WindowManager.showAlert("Не заполнены данные", "Необходимо заполнить поле вида ЧС", 2);
            return;
        }

        JsonObject jsonObject = new JsonObject();
        LocalDateTime currentDateTime = LocalDateTime.now();

        jsonObject.addProperty("start_action_time", currentDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        jsonObject.addProperty("report_id", reportTableData.getId());
        jsonObject.addProperty("disp_login", ServiceSingleton.getInstance().getCurrentUser());
        jsonObject.addProperty("char_name", charChoiceBox.getValue());
        jsonObject.addProperty("kind_name", kindComboBox.getValue());

        int flag = requestsManager.confirmStage(jsonObject, "/start-action-time");

        if (flag == 1) {
            dispChoice = requestsManager.loadDispChoice(reportTableData.getId());
            stage1Load();
            preLoadStage2();
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
            int flag = requestsManager.confirmStage(jsonObject, "/confirm-chosen-services");

            if (flag == 1) {
                dispChoice = requestsManager.loadDispChoice(reportTableData.getId());
                stage2Load();
                preLoadStage3();
            }
        }

    }

    public void confirmReceivedData(ActionEvent actionEvent) {
        if (radioDiedInDisaster.isSelected() && diedAmountField.getText().isEmpty()) {
            WindowManager.showAlert("Не введены данные!", "Введите количество погибших", 2);
            return;
        }
        if (radioPeopleAmount.isSelected() && peopleAmountField.getText().isEmpty()) {
            WindowManager.showAlert("Не введены данные!", "Введите количество людей, находящихся на месте происшествия", 2);
            return;
        }

        JsonObject jsonObject = new JsonObject();
        LocalDateTime currentDateTime = LocalDateTime.now();

        String died_string;
        if (radioDiedInDisaster.isSelected()) {
            died_string = diedAmountField.getText();
        } else {
            died_string = "0";
        }
        jsonObject.addProperty("died_in_disaster_amount", Integer.parseInt(died_string));

        String people_string;
        if (radioPeopleAmount.isSelected()) {
            people_string = peopleAmountField.getText();
        } else {
            people_string = "0";
        }
        jsonObject.addProperty("people_in_area_amount", Integer.parseInt(people_string));

        jsonObject.addProperty("report_id", reportTableData.getId());
        jsonObject.addProperty("receivedDataDateTime", currentDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        int flag = requestsManager.confirmStage(jsonObject, "/confirm-received-data");
        if (flag == 1) {
            dispChoice = requestsManager.loadDispChoice(reportTableData.getId());
            stage3Load();
            preLoadStage4();
        }

    }

    public void callOtherServices(ActionEvent actionEvent) {
        JsonObject jsonObject = new JsonObject();
        LocalDateTime currentDateTime = LocalDateTime.now();
        jsonObject.addProperty("additional_services", otherChosenServicesArea.getText());
        jsonObject.addProperty("report_id", reportTableData.getId());
        jsonObject.addProperty("additional_services_time", currentDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        int flag = requestsManager.confirmStage(jsonObject, "/confirm-other-chosen-services");
        if (flag == 1) {
            dispChoice = requestsManager.loadDispChoice(reportTableData.getId());
            stage4Load();
            preLoadStage5();
        }

    }

    public void confirmEndReacting(ActionEvent actionEvent) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("report_id", reportTableData.getId());
        LocalDateTime currentDateTime = LocalDateTime.now();
        jsonObject.addProperty("end_actions_time", currentDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        int flag = requestsManager.confirmStage(jsonObject, "/end-action-time");
        if (flag == 1) {
            dispChoice = requestsManager.loadDispChoice(reportTableData.getId());
            stage5Load();
            //TODO: Документация
        }

    }

    // Загрузка стадий:
    public void stage1Load() { //определен
        // установка выбранных значений
        charChoiceBox.setValue(dispChoice.getName_char());
        kindComboBox.setValue(dispChoice.getName_kind());
        loadRecommendedServices(dispChoice.getName_kind());

        // отключить старые элементы
        charChoiceBox.setDisable(true);
        buttonStartReacting.setDisable(true);
        kindComboBox.setDisable(true);

        //убрать старые подсказки
        vboxConfirmStartReaction.setStyle("");
        buttonStartReacting.setStyle("");

    }

    public void preLoadStage2() {
        //установка подказок
        vboxConfirmChosenServices.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
        buttonConfirmServices.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
        buttonConfirmServices.setDisable(false);
    }

    public void stage2Load() {
        recommendedServicesList.getItems().clear();
        stage1Load();
        buttonNextWindow.setDisable(false);
        //убрать подсказки
        vboxConfirmChosenServices.setStyle("");
        buttonConfirmServices.setStyle("");
        buttonConfirmServices.setDisable(true);
        //установка уже выбранных значений
        recommendedServicesList2.getItems().clear();
        if (listOfRecommendedServices != null) {
            recommendedServicesList2.getItems().addAll(listOfRecommendedServices);
        } else recommendedServicesList2.getItems().add("Данный вид происшествия не определен");

        String chosenServices = dispChoice.getServices();
        List<String> chosenServicesList = Arrays.asList(chosenServices.split("\n"));
        List<CheckBox> checkBoxList = new ArrayList<>();
        for (String service : services) {
            if (!chosenServicesList.contains(service)) {
                CheckBox checkBox = new CheckBox(service);
                checkBoxList.add(checkBox);
            }
        }
        otherServicesList.getItems().clear();
        otherServicesList.getItems().addAll(checkBoxList);
        System.out.println("Установка выбранных служб, dispchoice:" + dispChoice.getServices());
//        chosenServicesArea.setText(dispChoice.getServices());

        //установка слушателей
        setUpServices(otherChosenServicesArea, checkBoxList);
        setUpChosenServices(allServicesList, dispChoice.getServices());
        //блокировать предыдущие действия
        btnCleanServices.setDisable(true);
        buttonConfirmServices.setDisable(true);
        allServicesList.setDisable(true);

        //перенос выборов на вторую страницу
        typeField2.setText(report.getType());
        kindField2.setText(dispChoice.getName_kind());
        charField2.setText(dispChoice.getName_char());
        casualtiesField2.setText(report.getCasualties_amount());
        //TODO: Дополнить адрес районом
        placeField2.setText(report.getPlace());
        String user_in_danger;
        if (report.getUser_in_danger()) {
            user_in_danger = "В опасности";
        } else {
            user_in_danger = "В безопасности";
        }
        userStatusField2.setText(user_in_danger);
        datetimeField2.setText(report.getTimestamp());
        additionalDataField2.setText(report.getAdditional_info());

        //установка слушателей
        radioDiedInDisaster.setOnAction(event -> {
            if (radioDiedInDisaster.isSelected()) {
                diedAmountField.setDisable(false);
            } else {
                diedAmountField.clear();
                diedAmountField.setDisable(true);
            }
        });

        radioPeopleAmount.setOnAction(event -> {
            if (radioPeopleAmount.isSelected()) {
                peopleAmountField.setDisable(false);
            } else {
                peopleAmountField.clear();
                peopleAmountField.setDisable(true);
            }
        });

        diedAmountField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                diedAmountField.setText(oldValue);
            }
        });

        peopleAmountField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                peopleAmountField.setText(oldValue);
            }
        });
    }

    public void preLoadStage3() {
        //установка подсказок
        buttonConfirmReceivedData.setDisable(false);
        buttonNextWindow.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
        vboxRecievedData.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
        buttonConfirmReceivedData.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
    }

    public void stage3Load() {
        stage2Load();

        //Убрать подсказки
        buttonConfirmReceivedData.setDisable(true);
        buttonNextWindow.setStyle("");
        vboxRecievedData.setStyle("");
        buttonConfirmReceivedData.setStyle("");

        //установить значения
        if (dispChoice.getDead_amount() == 0) {
            radioDiedInDisaster.setSelected(false);
        } else {
            radioDiedInDisaster.setSelected(true);
            diedAmountField.setText(Integer.toString(dispChoice.getDead_amount()));
        }


        if (dispChoice.getPeople_amount() == 0) {
            radioPeopleAmount.setSelected(false);
        } else {
            radioPeopleAmount.setSelected(true);
            peopleAmountField.setText(Integer.toString(dispChoice.getPeople_amount()));
        }
        //заблокировать старый функционал
        peopleAmountField.setEditable(false);
        diedAmountField.setEditable(false);
        radioDiedInDisaster.setDisable(true);
        radioPeopleAmount.setDisable(true);
    }

    public void preLoadStage4() {
        vboxAdditionalServicesCalling.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
        buttonCallOtherServices.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
        buttonCallOtherServices.setDisable(false);
    }

    public void stage4Load() {
        stage3Load();
        //убрать подсказки
        buttonCallOtherServices.setStyle("");
        // загрузка данных
        setUpChosenServices(otherServicesList, dispChoice.getAdditional_services());
        otherChosenServicesArea.setText(dispChoice.getAdditional_services());
        //заблокировать старый функционал
        otherServicesList.setDisable(true);
        btnCleanNewServices.setDisable(true);
        buttonCallOtherServices.setDisable(true);

    }

    public void preLoadStage5() {
        buttonConfirmEndReacting.setDisable(false);
        buttonConfirmEndReacting.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
    }

    public void stage5Load() { //пользователь закончил реагирование на чс
        stage4Load();
        //TODO:Получение отчета
        buttonConfirmEndReacting.setStyle("");
        buttonConfirmEndReacting.setDisable(true);
    }

    //Загрузка данных
    public void loadChoicesStage() {
        dispChoice = requestsManager.loadDispChoice(reportTableData.getId());
        if (dispChoice == null || Objects.equals(dispChoice.getStage(), "0")) {
            loadTypeKindChar();
            vboxConfirmStartReaction.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
            buttonStartReacting.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
        } else {
            switch (dispChoice.getStage()) {
                case "1":
                    //определен вид и характре чс
                    stage1Load();
                    preLoadStage2();
                    break;
                case "2":
                    //вызваны службы
                    stage2Load();
                    preLoadStage3();
                    break;
                case "3":
                    // собрана информация от служб
                    stage3Load();
                    preLoadStage4();
                    break;
                case "4":
                    //вызваны дополнительные службы
                    stage4Load();
                    preLoadStage5();
                    break;
                case "5":
                    //реагирование окончено
                    stage5Load();
                    break;
            }
        }
    }

    public void loadReportApplicantData() { //загружаются заявление и данные о пользователе
        ReportApplicant reportApplicantData = requestsManager.loadReportApplicantData(reportTableData.getId());
        report = reportApplicantData.getReport();
        applicant = reportApplicantData.getApplicant();

        String fio = ApplicantProfileController.makeFIO(applicant.getName(), applicant.getSurname(), applicant.getPatronymic());
        applicantNameAndPhone.setText(makeFIOAndPhoneString(fio, applicant.getPhone_number()));
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
    }

    public void loadCharsAndServices() {
        Map<String, List<String>> emergencyData = requestsManager.loadCharsAndServicesData();
        chars = emergencyData.get("chars");
        services = emergencyData.get("services");

        List<CheckBox> checkBoxList = new ArrayList<>();
        for (String service : services) {
            CheckBox checkBox = new CheckBox(service);
            checkBoxList.add(checkBox);
        }
        allServicesList.getItems().addAll(checkBoxList);
        chosenServicesArea.clear();
        setUpServices(chosenServicesArea, checkBoxList);
    }

    public void loadTypeKindChar() {
        String type = report.getType();
        Map<String, String> map = requestsManager.loadKindCharByType(type);
        if (map == null) {
            charChoiceBox.getItems().addAll(chars);
            charChoiceBox.setOnAction(event -> {
                kindComboBox.getItems().clear();
                List<String> listOfKinds;
                String selectedValue = charChoiceBox.getValue();
                if (selectedValue != null) {
                    listOfKinds = requestsManager.loadKindsOfChar(selectedValue);
                    assert listOfKinds != null;
                    kindComboBox.getItems().addAll(listOfKinds);
                    kindComboBox.setDisable(false);
                }
            });

            kindComboBox.setOnAction(event -> {
                String selectedKind = kindComboBox.getValue();
                if (selectedKind != null) {
                    loadRecommendedServices(selectedKind);
                }
            });
        } else {
            String kind_name = map.get("kind_name");
            String char_name = map.get("char_name");
            charChoiceBox.setValue(char_name);
            kindComboBox.setValue(kind_name);
            charChoiceBox.setDisable(true);
            kindComboBox.setDisable(true);
            recommendedServicesList.getItems().clear();
            loadRecommendedServices(kindComboBox.getValue());
        }
    }

    private void loadRecommendedServices(String kind) {
        listOfRecommendedServices = requestsManager.loadRecommendedServices(kind);
        if (listOfRecommendedServices != null) {
            recommendedServicesList.getItems().addAll(listOfRecommendedServices);
        } else {
            recommendedServicesList.getItems().add("Данный вид происшествия не определен");
        }
    }


    // Дополнительные методы
    public void setUpServices(TextArea area, List<CheckBox> checkBoxList) {
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

    public void setUpChosenServices(ListView<CheckBox> list, String chosenServices) {
        String[] services = chosenServices.split("\n");
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
        otherServicesList.getItems().forEach(checkBox -> checkBox.setSelected(false));
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


}
