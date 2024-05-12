package com.example.eaclient.Controllers.AdminController;

import com.example.eaclient.Controllers.WindowManager;
import com.example.eaclient.Models.AdminModels.AutoTable;
import com.example.eaclient.Network.HttpRequests.HttpResponse;
import com.example.eaclient.Network.HttpRequests.SimpleRequestManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


import static com.example.eaclient.Controllers.WindowManager.showAlert;

public class AutoViewController {
    @FXML
    public TableView<AutoTable> autoTable;
    @FXML
    public TableColumn<AutoTable, Integer> auto_id;
    @FXML
    public TableColumn<AutoTable, String> mark;
    @FXML
    public TableColumn<AutoTable, String> number;
    @FXML
    public TableColumn<AutoTable, String> district;
    @FXML
    public TableColumn<AutoTable, String> service;
    @FXML
    public TextField markField;
    @FXML
    public TextField numberField;
    @FXML
    public ChoiceBox<String> serviceChoiceBox;
    @FXML
    public ChoiceBox<String> districtsChoiceBox;
    @FXML
    public Button btnAddAuto;
    @FXML
    public Button btnAddDistrict;
    @FXML
    public Button btnDeleteDistrict;
    @FXML
    public ChoiceBox<String> deleteDistrictChoiceBox;
    @FXML
    public Button btnDeleteAuto;
    @FXML
    public TextField autoIdField;
    @FXML
    public TextField districtField;

    Gson gson = new Gson();

    public void addAuto(ActionEvent actionEvent) {
        if (markField.getText().isEmpty() || Objects.equals(markField.getText(), "")) {
            WindowManager.showAlert("Не заполнено поле!", "Заполните поле марки транспорта!", 2);
            return;
        }
        if (numberField.getText().isEmpty() || Objects.equals(numberField.getText(), "")) {
            WindowManager.showAlert("Не заполнено поле!", "Заполните поле номера транспорта!", 2);
            return;
        }
        if (districtsChoiceBox.getValue() == null) {
            WindowManager.showAlert("Не заполнено поле!", "Выберите район, к которому относится транспорт!", 2);
            return;
        }
        if (serviceChoiceBox.getValue() == null) {
            WindowManager.showAlert("Не заполнено поле!", "Выберите службу, к которой относится транспорт!", 2);
            return;
        }
        Map<String, String> autoMap = new HashMap<>();
        autoMap.put("district_name", districtsChoiceBox.getValue());
        autoMap.put("service_name", serviceChoiceBox.getValue());
        autoMap.put("mark", markField.getText());
        autoMap.put("number", numberField.getText());

        try {
            HttpResponse httpResponse = SimpleRequestManager.sendPostRequest("/add-new-auto", gson.toJson(autoMap));
            int code = httpResponse.getResponseCode();
            if (code == 200) {
                showAlert("Успешно!", "Добавлен новый транспорт.", 1);
                initData();
            } else if (code == 409) {
                showAlert("Попытка повторного использования номера транспорта!", "Данный номер транспорта уже используется!", 2);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void deleteAuto(ActionEvent actionEvent) {
        if (autoIdField.getText().isEmpty() || Objects.equals(autoIdField.getText(), "")) {
            WindowManager.showAlert("Не заполнено поле!", "Выберите запись в таблице для удаления!", 2);
            return;
        }
        try {
            HttpResponse response = SimpleRequestManager.sendDeleteRequest("/delete-auto", "auto_id=" + autoIdField.getText());
            int code = response.getResponseCode();
            if (code == 200) {
                showAlert("Успешно!", "Транспорт с id " + autoIdField.getText() + " была удалена.", 1);
                autoIdField.clear();
                initData();
            } else if (code == 400) {
                showAlert("Не удалось провести удаление", "Что-то пошло не так.", 2);
            } else if (code == 404) {
                showAlert("Не удалось найти указанные данные", "Записи о транспорте с id " + autoIdField.getText() + " не найдены.", 2);
            } else if (code == 409) {
                showAlert("Не удалось провести удаление", "Данная связь используется в других записях.", 2);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteDistrict(ActionEvent actionEvent) {
        if (deleteDistrictChoiceBox.getValue() == null) {
            WindowManager.showAlert("Не заполнено поле!", "Выбберите район для удаления!", 2);
            return;
        }

        try {
            HttpResponse response = SimpleRequestManager.sendDeleteRequest("/delete-district", "district_name=" + deleteDistrictChoiceBox.getValue());
            int code = response.getResponseCode();
            if (code == 200) {
                showAlert("Успешно!", "Район " + deleteDistrictChoiceBox.getValue() + " был удален.", 1);
                initData();
            } else if (code == 400) {
                showAlert("Не удалось провести удаление", "Что-то пошло не так.", 2);
            } else if (code == 404) {
                showAlert("Не удалось найти указанные данные", "Записи о районе " + deleteDistrictChoiceBox.getValue() + " не найдены.", 2);
            } else if (code == 409) {
                showAlert("Не удалось провести удаление", "Данный район используется в других записях.", 2);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDistrict(ActionEvent actionEvent) {
        if (districtField.getText().isEmpty() || Objects.equals(districtField.getText(), "")) {
            WindowManager.showAlert("Не заполнено поле!", "Введите название нового района!", 2);
            return;
        }
        Map<String, String> districtMap = new HashMap<>();
        districtMap.put("district_name", districtField.getText());
        try {
            HttpResponse httpResponse = SimpleRequestManager.sendPostRequest("/add-new-district", gson.toJson(districtMap));
            int code = httpResponse.getResponseCode();
            if (code == 200) {
                showAlert("Успешно!", "Добавлен новый район район: " + districtField.getText() + ".", 1);
                initData();
            } else if (code == 409) {
                showAlert("Попытка повторного добавления района!", "Данный район уже существует!", 2);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void initData() {
        setUpDistricts();
        setUpServices();
        auto_id.setCellValueFactory(new PropertyValueFactory<>("auto_id"));
        mark.setCellValueFactory(new PropertyValueFactory<>("mark"));
        number.setCellValueFactory(new PropertyValueFactory<>("number"));
        district.setCellValueFactory(new PropertyValueFactory<>("district"));
        service.setCellValueFactory(new PropertyValueFactory<>("service"));
        autoTable.setItems(getAllAutos());

        autoTable.setOnMouseClicked(event -> {
            AutoTable selectedItem = autoTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                int selectedAutoId = selectedItem.getAuto_id();
                autoIdField.setText(Integer.toString(selectedAutoId));
            }
        });
    }

    private ObservableList<AutoTable> getAllAutos() {
        ObservableList<AutoTable> auto_list = FXCollections.observableArrayList();
        try {
            HttpResponse httpResponse = SimpleRequestManager.sendGetRequest("/get-all-autos");
            int code = httpResponse.getResponseCode();
            if (code == 200) {
                String body = httpResponse.getResponseBody();
                Type typeOfAuto = new TypeToken<ArrayList<AutoTable>>() {
                }.getType();
                List<AutoTable> list_auto = gson.fromJson(body, typeOfAuto);
                auto_list.addAll(list_auto);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return auto_list;
    }

    private void setUpDistricts() {
        districtsChoiceBox.getItems().clear();
        deleteDistrictChoiceBox.getItems().clear();
        List<String> listOfDistricts = null;
        try {
            HttpResponse httpResponse = SimpleRequestManager.sendGetRequest("/set-up-district");
            int code = httpResponse.getResponseCode();
            if (code == 200) {
                String body = httpResponse.getResponseBody();
                listOfDistricts = gson.fromJson(body, List.class);
            } else {
                System.out.println("Ошибка загрузки");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        assert listOfDistricts != null;
        districtsChoiceBox.getItems().addAll(listOfDistricts);
        deleteDistrictChoiceBox.getItems().addAll(listOfDistricts);
    }

    private void setUpServices() {
        serviceChoiceBox.getItems().clear();
        List<String> listOfServices = null;
        try {
            HttpResponse httpResponse = SimpleRequestManager.sendGetRequest("/set-up-services");
            int code = httpResponse.getResponseCode();
            if (code == 200) {
                String body = httpResponse.getResponseBody();
                listOfServices = gson.fromJson(body, List.class);
            } else {
                System.out.println("Ошибка загрузки");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        assert listOfServices != null;
        serviceChoiceBox.getItems().addAll(listOfServices);
    }
}
