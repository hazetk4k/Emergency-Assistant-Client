package com.example.eaclient.Controllers.AdminController;

import com.example.eaclient.Models.KindEm;
import com.example.eaclient.Models.TypeEm;
import com.example.eaclient.Network.HttpResponse;
import com.example.eaclient.Network.SimpleRequestManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TypeViewController {
    @FXML
    public TableView<TypeEm> tableTypes;
    @FXML
    public ChoiceBox<String> kindChoiceBox;
    @FXML
    public Button btnDeleteType;
    @FXML
    public TextField fieldTypeName;
    @FXML
    public TableColumn<TypeEm, String> type;
    @FXML
    public TableColumn<TypeEm, String> recommendations;
    @FXML
    public TableColumn<TypeEm, String> kind;
    @FXML
    public TextArea recoms;
    @FXML
    public Button btnAddNewType;
    @FXML
    public TextField chosenType;
    private final Gson gson = new Gson();

    private void showAlert(String title, String warning) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(warning);
        alert.showAndWait();
    }

    public ObservableList<TypeEm> initialTableData() {
        ObservableList<TypeEm> typeEmList = FXCollections.observableArrayList();
        try {
            HttpResponse httpResponse = SimpleRequestManager.sendGetRequest("/get-all-types");
            System.out.println("Запрос отправлен");
            int response_code = httpResponse.getResponseCode();
            if (response_code == 200) {
                String responseBody = httpResponse.getResponseBody();
                Type typeOfTypes = new TypeToken<ArrayList<TypeEm>>() {
                }.getType();
                List<TypeEm> tempTypesList = gson.fromJson(responseBody, typeOfTypes);

                typeEmList.addAll(tempTypesList);
                System.out.println(tempTypesList);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return typeEmList;
    }

    public void setUpKind() {
        kindChoiceBox.getItems().clear();
        List<String> listOfKinds = null;
        try {
            HttpResponse httpResponse = SimpleRequestManager.sendGetRequest("/set-up-kinds");
            int code = httpResponse.getResponseCode();
            if (code == 200) {
                String body = httpResponse.getResponseBody();
                listOfKinds = gson.fromJson(body, List.class);
            } else {
                System.out.println("Ошибка загрузки");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        assert listOfKinds != null;
        kindChoiceBox.getItems().addAll(listOfKinds);
    }

    public void initData() {
        setUpKind();
        type.setCellValueFactory(new PropertyValueFactory<TypeEm, String>("type"));
        recommendations.setCellValueFactory(new PropertyValueFactory<TypeEm, String>("recommendations"));
        kind.setCellValueFactory(new PropertyValueFactory<TypeEm, String>("kind"));
        tableTypes.setItems(initialTableData());

        tableTypes.setOnMouseClicked(event -> {
            TypeEm selectedItem = tableTypes.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String selectedType = selectedItem.getType();
                chosenType.setText(selectedType);
            }
        });
    }

    public void deleteType(ActionEvent actionEvent) {
        if (chosenType.getText() == null || Objects.equals(chosenType.getText(), "")) {
            showAlert("Не выбрана запись!", "Выберите запись для удаления!");
        } else {
            String type_name = chosenType.getText();
            System.out.println(type_name);
            try {
                String encodedWord = URLEncoder.encode(type_name, StandardCharsets.UTF_8);
                HttpResponse response = SimpleRequestManager.sendDeleteRequest("/delete-type", "type_name=" + encodedWord);
                int code = response.getResponseCode();
                if (code == 200) {
                    showAlert("Успешно!", "Тип ЧС с именем " + type_name + " был удален.");
                    initData();
                } else if (code == 400) {
                    showAlert("Не удалось получить имя вида ЧС", "Что-то пошло не так.");
                } else if (code == 404) {
                    showAlert("Не удалось найти вид ЧС", "Запись о типе " + type_name + " не найдена.");
                } else if (code == 409) {
                    showAlert("Не удалось провести удаление", "Данный тип используется в других записях.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void addNewType(ActionEvent actionEvent) {
        if (kindChoiceBox.getSelectionModel().getSelectedItem() == null) {
            showAlert("Не выбран вид ЧС!", "Выберите вид, к которому будет относится тип!");
            return;
        }

        if (fieldTypeName.getText() == null || fieldTypeName.getText().isEmpty()) {
            showAlert("Не введено название!", "Введите название типа");
            return;
        }

        if (recoms.getText() == null || recoms.getText().isEmpty()) {
            showAlert("Не дана рекомендация!", "Введите рекомендацию для пользователя!");
            return;
        }

        String kind_name = kindChoiceBox.getSelectionModel().getSelectedItem();
        String type_name = fieldTypeName.getText();
        String recommendations = recoms.getText();
        Map<String, Object> newTypeMap = new HashMap<>();
        newTypeMap.put("kind_name", kind_name);
        newTypeMap.put("type_name", type_name);
        newTypeMap.put("recommendations", recommendations);
        try {
            HttpResponse httpResponse = SimpleRequestManager.sendPostRequest("/add-new-type", gson.toJson(newTypeMap));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        fieldTypeName.clear();
        recoms.clear();

        initData();

    }
}
