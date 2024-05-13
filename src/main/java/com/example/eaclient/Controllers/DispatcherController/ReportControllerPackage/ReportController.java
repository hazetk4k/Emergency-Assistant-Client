package com.example.eaclient.Controllers.DispatcherController.ReportControllerPackage;

import com.example.eaclient.Controllers.DispatcherController.ApplicantProfileController;
import com.example.eaclient.Controllers.WindowManager;
import com.example.eaclient.Models.ReportTableModels.AllReportsTable;
import com.example.eaclient.Models.ReportWindowModels.Applicant;
import com.example.eaclient.Models.ReportWindowModels.DispChoice;
import com.example.eaclient.Models.ReportWindowModels.Report;
import com.example.eaclient.Models.ReportWindowModels.ReportApplicant;
import com.example.eaclient.Models.ReportWindowModels.ServiceTransportPair;
import com.example.eaclient.Service.ServiceSingleton;
import com.example.eaclient.Service.WordReportGenerator;
import com.google.gson.JsonObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
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
    public TextArea additionalInfoArea;
    @FXML
    public Button buttonStartReacting;
    @FXML
    public ListView<String> recommendedServicesList;
    @FXML
    public Button buttonConfirmServices;
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
    public TextField casualtiesAmount;
    @FXML
    public VBox vboxConfirmStartReaction;
    @FXML
    public VBox vboxConfirmChosenServices;
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
    public TextArea additionalDataField2;
    @FXML
    public TextField userStatusField2;
    @FXML
    public TextField casualtiesField2;
    @FXML
    public TextField datetimeField2;
    @FXML
    public TextField placeField2;
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
    public Button buttonMakeReport;
    private final List<ServiceTransportPair> serviceTransportPairs = new ArrayList<>();
    private final List<ServiceTransportPair> otherServiceTransportPairs = new ArrayList<>();
    private AllReportsTable reportDataFormTable;
    private Report report;
    private Applicant applicant;
    private DispChoice dispChoice;
    private List<String> listOfRecommendedServices;
    private List<String> chars;
    private List<String> services;

    public final Request_ReportManager requestsManager = new Request_ReportManager();
    public final UI_ReportManager uiManager = new UI_ReportManager(this);

    private final Util_ReportManager utilManager = new Util_ReportManager(this);

    public void initData(AllReportsTable rowData) {
        this.reportDataFormTable = rowData;
        loadReportApplicantData();
        loadCharsServicesDistricts();
        loadChoicesStage();

        Label customPlaceholder = new Label("Службы не выбраны.");
        tableChosenServices.setPlaceholder(customPlaceholder);
        tableOtherChosenServices.setPlaceholder(customPlaceholder);
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
        jsonObject.addProperty("report_id", reportDataFormTable.getId());
        jsonObject.addProperty("disp_login", ServiceSingleton.getInstance().getCurrentUser());
        jsonObject.addProperty("char_name", charChoiceBox.getValue());
        jsonObject.addProperty("kind_name", kindComboBox.getValue());
        jsonObject.addProperty("district_name", districtsChoiceBox.getValue());

        int flag = requestsManager.confirmStage(jsonObject, "/start-action-time");

        if (flag == 1) {
            dispChoice = requestsManager.loadDispChoice(reportDataFormTable.getId());
            recommendedServicesList.getItems().clear();
            stage1Load(true, true);
        }

    }

    public void confirmChosenServices(ActionEvent actionEvent) {
        if (!isAnyRecordFilled()) {
            WindowManager.showAlert("Не выбрана запись!", "Необходимо выбрать службы реагирования на происшествие!", 2);
        } else {
            String services = utilManager.getAllServiceTransportRecordsAsString(tableChosenServices);
            JsonObject jsonObject = new JsonObject();
            LocalDateTime currentDateTime = LocalDateTime.now();
            jsonObject.addProperty("services", services);
            jsonObject.addProperty("report_id", reportDataFormTable.getId());
            jsonObject.addProperty("confirm_services_time", currentDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            int flag = requestsManager.confirmStage(jsonObject, "/confirm-chosen-services");

            if (flag == 1) {
                dispChoice = requestsManager.loadDispChoice(reportDataFormTable.getId());
                stage2Load();
            }
        }

    }

    public boolean isAnyRecordFilled() {
        for (ServiceTransportPair pair : tableChosenServices.getItems()) {
            if (!pair.getService().isEmpty() && !pair.getTransport().isEmpty()) {
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

        jsonObject.addProperty("report_id", reportDataFormTable.getId());
        jsonObject.addProperty("receivedDataDateTime", currentDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        int flag = requestsManager.confirmStage(jsonObject, "/confirm-received-data");
        if (flag == 1) {
            dispChoice = requestsManager.loadDispChoice(reportDataFormTable.getId());
            stage3Load(true, true);
        }

    }

    public void callOtherServices(ActionEvent actionEvent) {
        String services = utilManager.getAllServiceTransportRecordsAsString(tableOtherChosenServices);
        JsonObject jsonObject = new JsonObject();
        LocalDateTime currentDateTime = LocalDateTime.now();
        jsonObject.addProperty("additional_services", services);
        jsonObject.addProperty("report_id", reportDataFormTable.getId());
        jsonObject.addProperty("additional_services_time", currentDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        int flag = requestsManager.confirmStage(jsonObject, "/confirm-other-chosen-services");
        if (flag == 1) {
            dispChoice = requestsManager.loadDispChoice(reportDataFormTable.getId());
            stage4Load();
        }

    }

    public void confirmEndReacting(ActionEvent actionEvent) {
        vboxAdditionalServicesCalling.setStyle("");
        buttonConfirmEndReacting.setStyle("-fx-background-color: #1e2f56;");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("report_id", reportDataFormTable.getId());
        LocalDateTime currentDateTime = LocalDateTime.now();
        jsonObject.addProperty("end_actions_time", currentDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        int flag = requestsManager.confirmStage(jsonObject, "/end-action-time");
        if (flag == 1) {
            dispChoice = requestsManager.loadDispChoice(reportDataFormTable.getId());
            stage5Load();
        }

    }

    // Загрузка стадий:
    public void stage0Load() {
        loadTypeKindChar();
        uiManager.setTipsStageZero();
        uiManager.stageZeroBlockUnlock();
    }

    public void stage1Load(boolean shouldAddListeners, boolean shouldAddChoiceBox) {
        uiManager.removeTipsStageZero();
        uiManager.setTipsStageOne();
        uiManager.stageOneBlockUnlock();

        charChoiceBox.setValue(dispChoice.getName_char());
        kindComboBox.setValue(dispChoice.getName_kind());
        loadRecommendedServices(dispChoice.getName_kind());
        districtsChoiceBox.setValue(dispChoice.getDistrict_name());

        utilManager.setCVFForChosenServiceColumn(chosenServiceColumn, chosenServiceAutoColumn, shouldAddChoiceBox);
        // Загрузка интерфейса специфичная для стадии 1
        if (shouldAddListeners) {
            // Настраиваем слушателей для выбора услуг
            utilManager.setUpServicesListener(allServicesList, tableChosenServices, serviceTransportPairs, true);
        } else {
            // Загружаем данные в интерфейс без активации слушателей
            utilManager.loadServicesIntoUI(dispChoice.getServices(), tableChosenServices, allServicesList);
        }

    }

    public void stage2Load() {
        recommendedServicesList.getItems().clear();
        stage1Load(false, false);

        uiManager.removeTipsStageOne();
        uiManager.setTipsStageTwo();
        uiManager.stageTwoBlockUnlock();

        utilManager.setUpChosenServices(allServicesList, utilManager.extractServiceNames(dispChoice.getServices()));

        //установка данных для новой таблицы
        recommendedServicesList2.getItems().clear();
        if (listOfRecommendedServices != null) {
            recommendedServicesList2.getItems().addAll(listOfRecommendedServices);
        } else recommendedServicesList2.getItems().add("Данный вид происшествия не определен");

        String chosenServices = dispChoice.getServices();
        List<String> chosenServicesList = utilManager.extractServiceNames(chosenServices);

        List<CheckBox> checkBoxList = new ArrayList<>();
        for (String service : services) {
            if (!chosenServicesList.contains(service)) {
                CheckBox checkBox = new CheckBox(service);
                checkBoxList.add(checkBox);
            }
        }
        otherServicesList.getItems().clear();
        otherServicesList.getItems().addAll(checkBoxList);

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

    public void stage3Load(boolean shouldAddListener, boolean shouldAddChoiceBox) {
        stage2Load();

        uiManager.removeTipsStageTwo();
        uiManager.setTipsStageThree();
        uiManager.stageThreeBlockUnlock();

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

        utilManager.setCVFForChosenServiceColumn(otherChosenServiceColumn, otherChosenServiceAutoColumn, shouldAddChoiceBox);
        if (shouldAddListener) {
            // Настраиваем слушателей для выбора услуг
            utilManager.setUpServicesListener(otherServicesList, tableOtherChosenServices, otherServiceTransportPairs, true);
        } else {
            // Загружаем данные в интерфейс без активации слушателей
            utilManager.loadServicesIntoUI(dispChoice.getAdditional_services(), tableOtherChosenServices, otherServicesList);
        }
    }

    public void stage4Load() {
        stage3Load(false, false);

        uiManager.removeTipsStageThree();
        uiManager.setTipsStageFour();
        uiManager.stageFourBlockUnlock();

        utilManager.setUpChosenServices(allServicesList, utilManager.extractServiceNames(dispChoice.getServices()));

    }

    public void stage5Load() { //пользователь закончил реагирование на чс
        stage4Load();

        uiManager.removeTipsStageFour();
        uiManager.stageFiveBlockUnlock();
    }

    //Загрузка данных
    public void loadChoicesStage() {
        dispChoice = requestsManager.loadDispChoice(reportDataFormTable.getId());
        boolean shouldAddListeners = dispChoice == null || dispChoice.getStage().equals("1");
        boolean shouldAddListenersForStage4 = dispChoice != null && dispChoice.getStage().equals("3");
        // Настройка колонок для выбора транспорта с учетом текущей стадии
        boolean shouldAddChoiceBox = dispChoice == null || dispChoice.getStage().equals("1") || dispChoice.getStage().equals("3");

        utilManager.setCVFForChosenServiceColumn(chosenServiceColumn, chosenServiceAutoColumn, shouldAddChoiceBox);
        utilManager.setCVFForChosenServiceColumn(otherChosenServiceColumn, otherChosenServiceAutoColumn, shouldAddChoiceBox);

        switch (dispChoice == null ? "0" : dispChoice.getStage()) {
            case "0":
                stage0Load();
                break;
            case "1":
                stage1Load(shouldAddListeners, shouldAddChoiceBox);
                break;
            case "2":
                stage2Load();  // Слушатели не активны, данные уже загружены
                break;
            case "3":
                stage3Load(shouldAddListenersForStage4, shouldAddChoiceBox);
                break;
            case "4":
                stage4Load();  // Слушатели не активны, данные уже загружены
                break;
            case "5":
                stage5Load();       // Слушатели не активны, все данные установлены
                break;
        }
        if (dispChoice != null && dispChoice.getDispatcher_id() != ServiceSingleton.getInstance().getCurrentUserId()) {
            uiManager.blockAllButtons();
        }
    }

    public void loadReportApplicantData() { //загружаются заявление и данные о пользователе
        ReportApplicant reportApplicantData = requestsManager.loadReportApplicantData(reportDataFormTable.getId());
        report = reportApplicantData.getReport();
        applicant = reportApplicantData.getApplicant();

        String fio = ApplicantProfileController.makeFIO(applicant.getName(), applicant.getSurname(), applicant.getPatronymic());
        applicantNameAndPhone.setText(utilManager.makeFIOAndPhoneString(fio, applicant.getPhone_number()));
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
        Tooltip.install(districtsChoiceBox, uiManager.setToolTip("Выберите район"));

        List<CheckBox> checkBoxList = new ArrayList<>();
        for (String service : services) {
            CheckBox checkBox = new CheckBox(service);
            checkBoxList.add(checkBox);
        }
        allServicesList.getItems().addAll(checkBoxList);

//        utilManager.setCVFForChosenServiceColumn(chosenServiceColumn, chosenServiceAutoColumn);
//        utilManager.setUpServicesListener(allServicesList, tableChosenServices, serviceTransportPairs);
    }

    public void loadTypeKindChar() {
        String type = report.getType();
        Map<String, String> map = requestsManager.loadKindCharByType(type);
        if (map == null) {
            charChoiceBox.getItems().addAll(chars);
            Tooltip.install(charChoiceBox, uiManager.setToolTip("Выберите Характер происшествия"));
            charChoiceBox.setOnAction(event -> {
                kindComboBox.getItems().clear();
                List<String> listOfKinds;
                String selectedValue = charChoiceBox.getValue();
                if (selectedValue != null) {
                    listOfKinds = requestsManager.loadKindsOfChar(selectedValue);
                    assert listOfKinds != null;
                    kindComboBox.getItems().addAll(listOfKinds);
                    Tooltip.install(charChoiceBox, uiManager.setToolTip("Выберите вид происшествия"));
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

    public void loadRecommendedServices(String kind) {
        listOfRecommendedServices = requestsManager.loadRecommendedServices(kind);
        if (listOfRecommendedServices != null) {
            recommendedServicesList.getItems().addAll(listOfRecommendedServices);
        } else {
            recommendedServicesList.getItems().add("Данный вид происшествия не определен");
        }
    }

    // Кнопки взаимодействия
    public void openApplicantProfile(ActionEvent actionEvent) {
        utilManager.openApplicantProfile(applicant);
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
    public void openSecondWindow(ActionEvent actionEvent) {
        uiManager.openSecondWindow();
    }

    public void openFirstWindow(ActionEvent actionEvent) {
        uiManager.openFirstWindow();
    }

    //Работа с таблицами
    public void makeReport(ActionEvent actionEvent) {
        WordReportGenerator word = new WordReportGenerator();
        try {
            word.generateReport(reportDataFormTable.getId(), report, applicant, dispChoice);
            WindowManager.showAlert("Отчет создан!", "Добавлен отчет о заявлении " + reportDataFormTable.getId() + ", D:\\University\\CourseProject\\EAClient\\src\\main\\resources\\reports", 1);
        } catch (Exception e) {
            WindowManager.showAlert("Отчет не создан, закройте отчет!", "Файл с отчет о заявлении " + reportDataFormTable.getId() + " используется!", 2);
            System.out.println(e.getMessage());
        }
    }
}
