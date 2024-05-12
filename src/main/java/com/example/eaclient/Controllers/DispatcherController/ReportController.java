package com.example.eaclient.Controllers.DispatcherController;

import com.example.eaclient.Controllers.WindowManager;
import com.example.eaclient.Models.ReportTableModels.AllReportsTable;
import com.example.eaclient.Models.ReportWindowModels.Applicant;
import com.example.eaclient.Models.ReportWindowModels.DispChoice;
import com.example.eaclient.Models.ReportWindowModels.Report;
import com.example.eaclient.Models.ReportWindowModels.ReportApplicant;
import com.example.eaclient.Models.ReportWindowModels.ServiceTransportPair;
import com.example.eaclient.Service.ServiceSingleton;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    public ListView<String> recommendedServicesList;
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
    @FXML
    public CheckBox checkPeopleAmount;
    @FXML
    public CheckBox checkDiedInDisaster;
    @FXML
    public TextField districtField;
    @FXML
    public TableView<ServiceTransportPair> tableChosenServices;
    @FXML
    public TableColumn<ServiceTransportPair, String> chosenServiceColumn;
    @FXML
    public TableColumn<ServiceTransportPair, String> chosenServiceAutoColumn;
    @FXML
    public TableView<ServiceTransportPair> tableOtherChosenServices;
    @FXML
    public TableColumn<ServiceTransportPair, String> otherChosenServiceColumn;
    @FXML
    public TableColumn<ServiceTransportPair, String> otherChosenServiceAutoColumn;
    private List<ServiceTransportPair> serviceTransportPairs;

    private List<ServiceTransportPair> otherServiceTransportPairs;
    private AllReportsTable reportTableData;
    private Report report;
    private Applicant applicant;
    private DispChoice dispChoice;
    private List<String> listOfRecommendedServices;
    private List<String> chars;
    private List<String> services;

    private boolean allowAdding = true;

    private final ReportControllerRequests requestsManager = new ReportControllerRequests();

    public void initData(AllReportsTable rowData) {
        this.reportTableData = rowData;
        loadReportApplicantData();
        loadCharsServicesDistricts();
        loadChoicesStage();
    }


    //кнопки подтверждения выбора
    public void confirmStartReacting(ActionEvent actionEvent) {
        if (charChoiceBox.getValue() == null) {
            WindowManager.showAlert("Не заполнены данные", "Необходимо выбрать характер происшествия", 2);
            return;
        }

        if (kindComboBox.getValue() == null) {
            WindowManager.showAlert("Не заполнены данные", "Необходимо выбрать вид происшествия или ввести новый", 2);
            return;
        }

        if (districtsChoiceBox.getValue() == null) {
            WindowManager.showAlert("Не заполнены данные", "Необходимо выбрать район, к которому относится место происшествия", 2);
            return;
        }

        JsonObject jsonObject = new JsonObject();
        LocalDateTime currentDateTime = LocalDateTime.now();

        jsonObject.addProperty("start_action_time", currentDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        jsonObject.addProperty("report_id", reportTableData.getId());
        jsonObject.addProperty("disp_login", ServiceSingleton.getInstance().getCurrentUser());
        jsonObject.addProperty("char_name", charChoiceBox.getValue());
        jsonObject.addProperty("kind_name", kindComboBox.getValue());
        jsonObject.addProperty("district_name", districtsChoiceBox.getValue());

        int flag = requestsManager.confirmStage(jsonObject, "/start-action-time");

        if (flag == 1) {
            dispChoice = requestsManager.loadDispChoice(reportTableData.getId());
            recommendedServicesList.getItems().clear();
            stage1Load();
            preLoadStage2();
        }

    }

    public void confirmChosenServices(ActionEvent actionEvent) {
        if (!isAnyRecordFilled()) {
            WindowManager.showAlert("Не выбрана запись!", "Необходимо выбрать службы реагирования на происшествие!", 2);
        } else {
            String services = getAllRecordsAsString(tableChosenServices);
            JsonObject jsonObject = new JsonObject();
            LocalDateTime currentDateTime = LocalDateTime.now();
            jsonObject.addProperty("services", services);
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

    public boolean isAnyRecordFilled() {
        for (ServiceTransportPair pair : tableChosenServices.getItems()) {
            if (!pair.getService().isEmpty() && !pair.getTransport().isEmpty()) {
                System.out.println(pair.getService());
                System.out.println(pair.getTransport());
                return true;
            }
        }
        return false;
    }

    public void confirmReceivedData(ActionEvent actionEvent) {
        if (checkDiedInDisaster.isSelected() && diedAmountField.getText().isEmpty()) {
            WindowManager.showAlert("Не введены данные!", "Введите количество погибших", 2);
            return;
        }
        if (checkPeopleAmount.isSelected() && peopleAmountField.getText().isEmpty()) {
            WindowManager.showAlert("Не введены данные!", "Введите количество людей, находящихся на месте происшествия", 2);
            return;
        }

        JsonObject jsonObject = new JsonObject();
        LocalDateTime currentDateTime = LocalDateTime.now();

        String died_string;
        if (checkDiedInDisaster.isSelected()) {
            died_string = diedAmountField.getText();
        } else {
            died_string = "0";
        }
        jsonObject.addProperty("died_in_disaster_amount", Integer.parseInt(died_string));

        String people_string;
        if (checkPeopleAmount.isSelected()) {
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
        String services = getAllRecordsAsString(tableOtherChosenServices);
        JsonObject jsonObject = new JsonObject();
        LocalDateTime currentDateTime = LocalDateTime.now();
        jsonObject.addProperty("additional_services", services);
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
        vboxAdditionalServicesCalling.setStyle("");
        buttonConfirmEndReacting.setStyle("-fx-background-color: #1e2f56;");
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
        allServicesList.setDisable(false);
        charChoiceBox.setValue(dispChoice.getName_char());
        kindComboBox.setValue(dispChoice.getName_kind());
        loadRecommendedServices(dispChoice.getName_kind());
        districtsChoiceBox.setValue(dispChoice.getDistrict_name());

        // отключить старые элементы
        charChoiceBox.setDisable(true);
        buttonStartReacting.setDisable(true);
        kindComboBox.setDisable(true);
        districtsChoiceBox.setDisable(true);

        //убрать старые подсказки
        vboxConfirmStartReaction.setStyle("-fx-background-color: #aed2ca;");
        buttonStartReacting.setStyle("-fx-background-color: #1e2f56;");

    }

    public void preLoadStage2() {
        //установка подказок
        vboxConfirmChosenServices.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
        buttonConfirmServices.setStyle("-fx-background-color: #1e2f56; -fx-border-color: #0cc715; -fx-border-width: 3;");
        buttonConfirmServices.setDisable(false);
    }

    public void stage2Load() {
        allowAdding = false;
        recommendedServicesList.getItems().clear();
        stage1Load();
        buttonNextWindow.setDisable(false);
        //убрать подсказки
        vboxConfirmChosenServices.setStyle("");
        buttonConfirmServices.setStyle("-fx-background-color: #1e2f56;");
        buttonConfirmServices.setDisable(true);
        //установка уже выбранных значений
        recommendedServicesList2.getItems().clear();
        if (listOfRecommendedServices != null) {
            recommendedServicesList2.getItems().addAll(listOfRecommendedServices);
        } else recommendedServicesList2.getItems().add("Данный вид происшествия не определен");

        String chosenServices = dispChoice.getServices();

        List<String> chosenServicesList = extractServiceNames(chosenServices);
        List<CheckBox> checkBoxList = new ArrayList<>();
        for (String service : services) {
            if (!chosenServicesList.contains(service)) {
                CheckBox checkBox = new CheckBox(service);
                checkBoxList.add(checkBox);
            }
        }

        otherServicesList.getItems().clear();
        otherServicesList.getItems().addAll(checkBoxList);
        //установка таблицы 1 + чекбоксы
        setUpChosenServices(allServicesList, chosenServicesList);
        tableChosenServices.getItems().clear();
        addRecordsFromString(tableChosenServices, chosenServices);
        allowAdding = true;
        //установка слушателей второй таблицы
        otherServiceTransportPairs = new ArrayList<>();
        setCVFForChosenServiceColumn(otherChosenServiceColumn, otherChosenServiceAutoColumn);
        setUpServicesListener(otherServicesList, tableOtherChosenServices, otherServiceTransportPairs);

        //блокировать предыдущие действия
        btnCleanServices.setDisable(true);
        buttonConfirmServices.setDisable(true);
        allServicesList.setDisable(true);

        //перенос выборов на вторую страницу
        typeField2.setText(report.getType());
        kindField2.setText(dispChoice.getName_kind());
        charField2.setText(dispChoice.getName_char());
        if (report.getAre_there_any_casualties()) {
            casualtiesField2.setText(report.getCasualties_amount());
        } else {
            casualtiesField2.setText("Отсутствуют");
        }
        districtField.setText(dispChoice.getDistrict_name());
        placeField2.setText(report.getPlace());
        String user_in_danger;
        districtField.setText(dispChoice.getStage());
        if (report.getUser_in_danger()) {
            user_in_danger = "В опасности";
        } else {
            user_in_danger = "В безопасности";
        }
        userStatusField2.setText(user_in_danger);
        datetimeField2.setText(report.getTimestamp());
        additionalDataField2.setText(report.getAdditional_info());

        //установка слушателей
        checkDiedInDisaster.setOnAction(event -> {
            if (checkDiedInDisaster.isSelected()) {
                diedAmountField.setDisable(false);
            } else {
                diedAmountField.clear();
                diedAmountField.setDisable(true);
            }
        });

        checkPeopleAmount.setOnAction(event -> {
            if (checkPeopleAmount.isSelected()) {
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
        buttonNextWindow.setStyle("-fx-background-color: #1e2f56; -fx-border-color: #0cc715; -fx-border-width: 3;");
        vboxRecievedData.setStyle("-fx-background-color: #aed2ca; -fx-border-color: #0cc715; -fx-border-width: 3;");
        buttonConfirmReceivedData.setStyle("-fx-background-color: #1e2f56; -fx-border-color: #0cc715; -fx-border-width: 3;");
    }

    public void stage3Load() {
        stage2Load();

        //Убрать подсказки
        buttonConfirmReceivedData.setDisable(true);
        buttonNextWindow.setStyle("-fx-background-color: #1e2f56;");
        vboxRecievedData.setStyle("-fx-background-color: #aed2ca;");
        buttonConfirmReceivedData.setStyle("-fx-background-color: #1e2f56;");

        //установить значения
        if (dispChoice.getDead_amount() == 0) {
            checkDiedInDisaster.setSelected(false);
        } else {
            checkDiedInDisaster.setSelected(true);
            diedAmountField.setText(Integer.toString(dispChoice.getDead_amount()));
        }


        if (dispChoice.getPeople_amount() == 0) {
            checkPeopleAmount.setSelected(false);
        } else {
            checkPeopleAmount.setSelected(true);
            peopleAmountField.setText(Integer.toString(dispChoice.getPeople_amount()));
        }
        //заблокировать старый функционал
        peopleAmountField.setEditable(false);
        diedAmountField.setEditable(false);
        checkDiedInDisaster.setDisable(true);
        checkPeopleAmount.setDisable(true);
    }

    public void preLoadStage4() {
        vboxAdditionalServicesCalling.setStyle("-fx-border-color: #0cc715; -fx-border-width: 3;");
        buttonCallOtherServices.setStyle("-fx-background-color: #1e2f56; -fx-border-color: #0cc715; -fx-border-width: 3;");
        buttonCallOtherServices.setDisable(false);
    }

    public void stage4Load() {
        allowAdding = false;
        stage3Load();
        //убрать подсказки
        buttonCallOtherServices.setStyle("-fx-background-color: #1e2f56;");

        // загрузка данных
        String chosenServices = dispChoice.getAdditional_services();
        setUpChosenServices(otherServicesList, extractServiceNames(chosenServices));
        tableOtherChosenServices.getItems().clear();
        addRecordsFromString(tableOtherChosenServices, chosenServices);

        //заблокировать старый функционал
        otherServicesList.setDisable(true);
        btnCleanNewServices.setDisable(true);
        buttonCallOtherServices.setDisable(true);

    }

    public void preLoadStage5() {
        buttonConfirmEndReacting.setDisable(false);
        buttonConfirmEndReacting.setStyle("-fx-background-color: #1e2f56; -fx-border-color: #0cc715; -fx-border-width: 3;");
    }

    public void stage5Load() { //пользователь закончил реагирование на чс
        stage4Load();
        //TODO:Получение отчета
        buttonConfirmEndReacting.setStyle("-fx-background-color: #1e2f56;");
        buttonConfirmEndReacting.setDisable(true);
    }


    //Загрузка данных
    public void loadChoicesStage() {
        dispChoice = requestsManager.loadDispChoice(reportTableData.getId());
        if (dispChoice == null || Objects.equals(dispChoice.getStage(), "0")) {
            allServicesList.setDisable(true);
            loadTypeKindChar();
            vboxConfirmStartReaction.setStyle("-fx-background-color: #aed2ca; -fx-border-color: #0cc715; -fx-border-width: 3;");
            buttonStartReacting.setStyle("-fx-background-color: #1e2f56; -fx-border-color: #0cc715; -fx-border-width: 3;");
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
            if (dispChoice.getDispatcher_id() != ServiceSingleton.getInstance().getCurrentUserId()) {
                blockAllButtons();
            }
        }
    }

    public void loadReportApplicantData() { //загружаются заявление и данные о пользователе
        ReportApplicant reportApplicantData = requestsManager.loadReportApplicantData(reportTableData.getId());
        report = reportApplicantData.getReport();
        applicant = reportApplicantData.getApplicant();

        String fio = ApplicantProfileController.makeFIO(applicant.getName(), applicant.getSurname(), applicant.getPatronymic());
        applicantNameAndPhone.setText(requestsManager.makeFIOAndPhoneString(fio, applicant.getPhone_number()));
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

    public void loadCharsServicesDistricts() {
        Map<String, List<String>> emergencyData = requestsManager.loadCharsServicesDistrictsData();
        chars = emergencyData.get("chars");
        services = emergencyData.get("services");
        List<String> districts = emergencyData.get("districts");

        districtsChoiceBox.getItems().clear();
        districtsChoiceBox.getItems().addAll(districts);
        Tooltip.install(districtsChoiceBox, setToolTip("Выберите район"));

        List<CheckBox> checkBoxList = new ArrayList<>();
        for (String service : services) {
            CheckBox checkBox = new CheckBox(service);
            checkBoxList.add(checkBox);
        }
        allServicesList.getItems().addAll(checkBoxList);
        serviceTransportPairs = new ArrayList<>();
        setCVFForChosenServiceColumn(chosenServiceColumn, chosenServiceAutoColumn);
        setUpServicesListener(allServicesList, tableChosenServices, serviceTransportPairs);
    }

    public void loadTypeKindChar() {
        String type = report.getType();
        Map<String, String> map = requestsManager.loadKindCharByType(type);
        if (map == null) {
            charChoiceBox.getItems().addAll(chars);
            Tooltip.install(charChoiceBox, setToolTip("Выберите Характер происшествия"));
            charChoiceBox.setOnAction(event -> {
                kindComboBox.getItems().clear();
                List<String> listOfKinds;
                String selectedValue = charChoiceBox.getValue();
                if (selectedValue != null) {
                    listOfKinds = requestsManager.loadKindsOfChar(selectedValue);
                    assert listOfKinds != null;
                    kindComboBox.getItems().addAll(listOfKinds);
                    Tooltip.install(charChoiceBox, setToolTip("Выберите вид происшествия"));
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
    public void addRecordsFromString(TableView<ServiceTransportPair> table, String data) {
        String[] records = data.split("\n");
        for (String record : records) {

            String[] parts = record.split("//");
            if (parts.length == 2) {
                ServiceTransportPair pair = new ServiceTransportPair(parts[0], parts[1]);
                table.getItems().add(pair);
            } else {
                System.err.println("Неправильный формат записи: " + record);
            }
        }
    }

    public List<String> extractServiceNames(String data) {
        List<String> serviceNames = new ArrayList<>();
        String[] records = data.split("\n");
        for (String record : records) {
            String[] parts = record.split("//");
            if (parts.length >= 1) {
                serviceNames.add(parts[0]);
            } else {
                System.err.println("Неправильный формат записи: " + record);
            }
        }
        return serviceNames;
    }

    public String getAllRecordsAsString(TableView<ServiceTransportPair> table) {
        StringBuilder sb = new StringBuilder();
        for (ServiceTransportPair pair : table.getItems()) {
            sb.append(pair.getService()).append("//").append(pair.getTransport()).append("\n");
        }
        return sb.toString();
    }

    public void setUpChosenServices(ListView<CheckBox> list, List<String> chosenServices) {
        for (CheckBox checkBox : list.getItems()) {
            String checkBoxText = checkBox.getText().trim();
            for (String service : chosenServices) {
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
        tableChosenServices.getItems().clear();
    }

    public void cleanUpNewServices(ActionEvent actionEvent) {
        otherServicesList.getItems().forEach(checkBox -> checkBox.setSelected(false));
        tableChosenServices.getItems().clear();
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


    // Сделать весь функционал недоступным
    public void blockAllButtons() {
        //window 1
        charChoiceBox.setDisable(true);
        kindComboBox.setDisable(true);
        districtsChoiceBox.setDisable(true);
        allServicesList.setDisable(true);
        btnCleanServices.setDisable(true);
        //window2
        checkDiedInDisaster.setDisable(true);
        checkPeopleAmount.setDisable(true);
        peopleAmountField.setDisable(true);
        diedAmountField.setDisable(true);
        btnCleanNewServices.setDisable(true);
        otherServicesList.setDisable(true);
        //отключить все кнопки
        buttonConfirmServices.setDisable(true);
        buttonCallOtherServices.setDisable(true);
        buttonConfirmReceivedData.setDisable(true);
        buttonConfirmEndReacting.setDisable(true);
    }


    //Работа с таблицами
    public void setCVFForChosenServiceColumn(TableColumn<ServiceTransportPair, String> serviceColumn, TableColumn<ServiceTransportPair, String> autoColumn) {
        serviceColumn.setCellValueFactory(data -> data.getValue().serviceProperty());
        autoColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTransport()));
        autoColumn.setCellFactory(column -> {
            return new TableCell<>() {
                private final ChoiceBox<String> choiceBox = new ChoiceBox<>();

                {
                    // Устанавливаем слушатель на изменение значения в ChoiceBox
                    choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue != null && !newValue.isEmpty()) {
                            // Получаем текущую выбранную строку таблицы и устанавливаем для нее выбранный транспорт
                            ServiceTransportPair pair = getTableView().getItems().get(getIndex());
                            pair.setTransport(newValue);
                        }
                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setGraphic(null);
                    } else {
                        // Получаем текущую строку таблицы и загружаем в ChoiceBox список транспортов для выбранной службы
                        ServiceTransportPair pair = getTableView().getItems().get(getIndex());
                        List<String> autos = requestsManager.getServicesAutoByName(pair.getService(), districtsChoiceBox.getValue());
                        if (autos.isEmpty()) {
                            choiceBox.getItems().clear();
                            choiceBox.getItems().add("Транспорт службы отсутствует");
                        } else {
                            choiceBox.getItems().setAll(autos);
                            choiceBox.getSelectionModel().select(item);
                        }
                        setGraphic(choiceBox);
                    }
                }
            };
        });
    }

    public void setUpServicesListener(ListView<CheckBox> servicesList, TableView<ServiceTransportPair> chosenServicesTable, List<ServiceTransportPair> pairs) {
        // Настройка слушателей для чекбоксов
        for (CheckBox checkBox : servicesList.getItems()) {
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    // Добавляем выбранную службу и пустой транспорт в таблицу
                    if (allowAdding) {
                        ServiceTransportPair pair = new ServiceTransportPair(checkBox.getText(), "");
                        pairs.add(pair);
                        chosenServicesTable.getItems().add(pair);
                    }
                } else {
                    // Удаляем запись о выбранной службе из таблицы
                    ServiceTransportPair pairToRemove = null;
                    for (ServiceTransportPair pair : pairs) {
                        if (pair.getService().equals(checkBox.getText())) {
                            pairToRemove = pair;
                            break;
                        }
                    }
                    if (pairToRemove != null) {
                        pairs.remove(pairToRemove);
                        chosenServicesTable.getItems().remove(pairToRemove);
                    }
                }
            });
        }
    }
}
