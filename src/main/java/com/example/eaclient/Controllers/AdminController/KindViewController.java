package com.example.eaclient.Controllers.AdminController;

import com.example.eaclient.Models.AllReportsTable;
import com.example.eaclient.Models.KindEm;
import com.example.eaclient.Models.SystemUser;
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
import java.util.*;

public class KindViewController {
    @FXML
    public Button deleteKind;
    @FXML
    public Button addNewKind;
    @FXML
    public ChoiceBox<String> charChoiceBox;
    public TextField newKindFiled;
    @FXML
    public TableView<KindEm> tableKind;
    @FXML
    public TableColumn<KindEm, Integer> id_kind;
    @FXML
    public TableColumn<KindEm, String> kind_name;
    @FXML
    public TableColumn<KindEm, String> char_name;
    @FXML
    public TextField chosenKind;

    public Integer selectedId;

    private final Gson gson = new Gson();
//TODO: SHOW ALERT должны быть разными стилистически (типа успех и неудача)
    private void showAlert(String title, String warning) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(warning);
        alert.showAndWait();
    }

    ObservableList<KindEm> initialTableData() {
        ObservableList<KindEm> kindEmList = FXCollections.observableArrayList();
        try {
            HttpResponse httpResponse = SimpleRequestManager.sendGetRequest("/get-all-kinds");
            System.out.println("Запрос отправлен");
            int response_code = httpResponse.getResponseCode();
            if (response_code == 200) {
                String responseBody = httpResponse.getResponseBody();
                Type typeOfKinds = new TypeToken<ArrayList<KindEm>>() {
                }.getType();
                List<KindEm> tempKindsList = gson.fromJson(responseBody, typeOfKinds);

                kindEmList.addAll(tempKindsList);
                System.out.println(tempKindsList);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return kindEmList;
    }

    void charSetUp() {
        charChoiceBox.getItems().clear();
        List<String> listOfChars = null;
        try {
            HttpResponse httpResponse = SimpleRequestManager.sendGetRequest("/set-up-chars");
            int code = httpResponse.getResponseCode();
            if (code == 200) {
                String body = httpResponse.getResponseBody();
                listOfChars = gson.fromJson(body, List.class);
            } else {
                System.out.println("Ошибка загрузки");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        assert listOfChars != null;
        charChoiceBox.getItems().addAll(listOfChars);
    }

    public void initData() {
        charSetUp();
        id_kind.setCellValueFactory(new PropertyValueFactory<KindEm, Integer>("kind_id"));
        kind_name.setCellValueFactory(new PropertyValueFactory<KindEm, String>("kind_name"));
        char_name.setCellValueFactory(new PropertyValueFactory<KindEm, String>("char_name"));
        tableKind.setItems(initialTableData());

        tableKind.setOnMouseClicked(event -> {
            KindEm selectedItem = tableKind.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String selectedKind = selectedItem.getKind_name();
                selectedId = selectedItem.getKind_id();
                chosenKind.setText(selectedKind);
            }
        });
    }

    public void deleteThisKind(ActionEvent actionEvent) {
        if (chosenKind.getText() == null || Objects.equals(chosenKind.getText(), "")) {
            showAlert("Не выбрана запись!", "Выберите запись для удаления!");
        } else {
            String kind_name = chosenKind.getText();
            try {
                HttpResponse response = SimpleRequestManager.sendDeleteRequest("/delete-kind", "kind_id=" + selectedId);
                int code = response.getResponseCode();
                if (code == 200) {
                    showAlert("Успешно!", "Вид ЧС с именем " + kind_name + " был удален.");
                    initData();
                } else if (code == 400) {
                    showAlert("Не удалось получить имя вида ЧС", "Что-то пошло не так.");
                } else if (code == 404) {
                    showAlert("Не удалось найти вид ЧС", "Такой пользователь не найден.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void addNewKind(ActionEvent actionEvent) {
        if (charChoiceBox.getSelectionModel().getSelectedItem() == null) {
            showAlert("Не выбран характер ЧС!", "Выберите характер ЧС, к которому будет относиться вид!");
            return;
        }

        String kind_name = newKindFiled.getText();
        if (kind_name == null || kind_name.isEmpty()) {
            showAlert("Не введено название!", "Введите название вида!");
            return;
        }

        String char_name = charChoiceBox.getSelectionModel().getSelectedItem();
        Map<String, Object> newKindMap = new HashMap<>();
        newKindMap.put("kind_name", kind_name);
        newKindMap.put("char_name", char_name);
        try {
            HttpResponse httpResponse = SimpleRequestManager.sendPostRequest("/add-new-kind", gson.toJson(newKindMap));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        newKindFiled.clear();
        showAlert("Успешно!", "Добавлен новый вид ЧС. Вид: " + kind_name + ", Характер:" + char_name);
        initData();
    }

}
