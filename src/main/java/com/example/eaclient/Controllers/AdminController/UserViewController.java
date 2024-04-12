package com.example.eaclient.Controllers.AdminController;

import com.example.eaclient.Models.SystemUser;
import com.example.eaclient.Network.HttpResponse;
import com.example.eaclient.Network.SimpleRequestManager;
import com.example.eaclient.Service.ServiceSingleton;
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

public class UserViewController {
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public RadioButton radIsAdmin;
    @FXML
    public Button btnAddUser;
    @FXML
    public Button btnDelete;
    @FXML
    public ChoiceBox<String> choiceBoxStatus;
    @FXML
    public Button btnChangeStatus;
    @FXML
    public TableView<SystemUser> systUserTable;
    @FXML
    public TableColumn<SystemUser, Integer> id_syst;
    @FXML
    public TableColumn<SystemUser, String> login_syst;
    @FXML
    public TableColumn<SystemUser, String> status_syst;
    @FXML
    public TextField chosenProfile;
    public int userId;

    private final Gson gson = new Gson();

    public void initData() {
        choiceBoxStatus.getItems().clear();
        choiceBoxStatus.getItems().addAll("Администратор", "Диспетчер");
        id_syst.setCellValueFactory(new PropertyValueFactory<SystemUser, Integer>("id_syst"));
        login_syst.setCellValueFactory(new PropertyValueFactory<SystemUser, String>("login_syst"));
        status_syst.setCellValueFactory(new PropertyValueFactory<SystemUser, String>("status_syst"));
        systUserTable.setItems(initialData());

        systUserTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                String value = newSelection.getLogin_syst();
                userId = newSelection.getId_syst();
                chosenProfile.setText(value);
            }
        });
    }

    ObservableList<SystemUser> initialData() {
        ObservableList<SystemUser> usersList = FXCollections.observableArrayList();
        try {
            HttpResponse httpResponse = SimpleRequestManager.sendGetRequest("/get-all-system-users");
            int response_code = httpResponse.getResponseCode();
            if (response_code == 200) {
                String responseBody = httpResponse.getResponseBody();
                Type typeOfUsers = new TypeToken<ArrayList<SystemUser>>() {
                }.getType();
                List<SystemUser> tempUsersList = gson.fromJson(responseBody, typeOfUsers);

                usersList.addAll(tempUsersList);
                System.out.println(tempUsersList);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return usersList;
    }

    public void addNewUser(ActionEvent actionEvent) {
        String login = loginField.getText();
        String password = passwordField.getText();

        if (login.isEmpty() || password.isEmpty()) {
            showAlert("Ошибка", "Введите логин и пароль");
            return;
        }

        byte status;
        if (radIsAdmin.isSelected()) {
            status = 1;
        } else {
            status = 0;
        }
        Map<String, Object> newUserData = new HashMap<>();
        newUserData.put("login", login);
        newUserData.put("password", password);
        newUserData.put("status", status);
        try {
            SimpleRequestManager.sendPostRequest("/add-new-user", gson.toJson(newUserData));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        loginField.clear();
        passwordField.clear();
        showAlert("Успешно!", "Добавлен новый пользователь. Логин: " + login + ", Пароль:" + password);
        initData();
    }

    public void deleteUser(ActionEvent actionEvent) {
        if (chosenProfile.getText() != null && !Objects.equals(chosenProfile.getText(), "")) {
            if (Objects.equals(ServiceSingleton.getInstance().getCurrentUser(), chosenProfile.getText())) {
                showAlert("Попытка удаления активного профиля!", "Остановлена попытка удаления собственного профиля!");
            } else {
                if (systUserTable.getSelectionModel().isEmpty()) {
                    showAlert("Не выбран элемент таблицы!", "Выберите поле таблицы, которое хотите удалить.");
                } else {
                    try {
                        HttpResponse response = SimpleRequestManager.sendDeleteRequest("/delete-user", "user_id=" + userId);
                        int code = response.getResponseCode();
                        if (code == 200) {
                            showAlert("Успешно!", "Пользователь с id " + userId + " был удален.");
                            initData();
                        } else if (code == 400) {
                            showAlert("Не удалось получить id", "Что-то пошло не так.");
                        } else if (code == 404) {
                            showAlert("Не удалось найти пользователя", "Такой пользователь не найден.");
                        } else if (code == 409) {
                            showAlert("Не удалось провести удаление", "Данный пользователь .");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    initData();
                }
            }
        } else {
            showAlert("Пустой выбор!", "Выберите поле из таблицы, которые хотите удалить!");
        }
    }

    //TODO:Запретить пользователю менять статус на тот же что и так стоит
    public void changeUserStatus(ActionEvent actionEvent) {
        if (choiceBoxStatus.getSelectionModel().getSelectedItem() == null) {
            showAlert("Не выбран статус!", "Выберите статус, который будет применен к профилю!");
        } else {
            if (systUserTable.getSelectionModel().isEmpty()) {
                showAlert("Не выбран элемент таблицы!", "Выберите поле, в котором хотите изменить статус.");
            } else {
                Map<String, Object> userStatusMap = new HashMap<>();
                userStatusMap.put("id", userId);
                byte n = 0;
                if (Objects.equals(choiceBoxStatus.getSelectionModel().getSelectedItem(), "Администратор")) {
                    n = 1;
                }
                userStatusMap.put("status", n);
                userStatusMap.put("login", ServiceSingleton.getInstance().getCurrentUser());
                try {
                    HttpResponse response = SimpleRequestManager.sendPutRequest("/update-user-status", gson.toJson(userStatusMap));
                    int responseCode = response.getResponseCode();
                    if (responseCode == 200) {
                        showAlert("Успешно!", "Изменен статус пользователя с id " + userId + " на '" + choiceBoxStatus.getSelectionModel().getSelectedItem() + "'.");

                        initData();
                    } else if (responseCode == 403) {
                        showAlert("Попытка изменения активного профиля!", "Остановлена попытка изменения статуса собственного профиля!");
                    }
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    private void showAlert(String title, String warning) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(warning);
        alert.showAndWait();
    }
}
