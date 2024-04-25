package com.example.eaclient.Controllers.AdminController;

import com.example.eaclient.Models.SystemUser;
import com.example.eaclient.Network.HttpRequests.HttpResponse;
import com.example.eaclient.Network.HttpRequests.SimpleRequestManager;
import com.example.eaclient.Service.ServiceSingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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

    public String current_status;

    private final Gson gson = new Gson();

    public void initData() {
        choiceBoxStatus.getItems().clear();
        choiceBoxStatus.getItems().addAll("Администратор", "Диспетчер");
        id_syst.setCellValueFactory(new PropertyValueFactory<SystemUser, Integer>("id_syst"));
        login_syst.setCellValueFactory(new PropertyValueFactory<SystemUser, String>("login_syst"));
        status_syst.setCellValueFactory(cellData -> {
            String status = cellData.getValue().status_syst;
            String statusText = (Objects.equals(status, "0")) ? "Диспетчер" : "Администратор";
            return new SimpleStringProperty(statusText);
        });
        systUserTable.setItems(initialData());

        systUserTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                String value = newSelection.getLogin_syst();
                current_status = newSelection.status_syst;
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
            showAlert("Ошибка", "Введите логин и пароль", 2);
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
            HttpResponse httpResponse = SimpleRequestManager.sendPostRequest("/add-new-user", gson.toJson(newUserData));
            int code = httpResponse.getResponseCode();
            if (code == 409) {
                loginField.clear();
                passwordField.clear();
                showAlert("Ошибка!", "Такой пользователь уже существует", 2);
            } else if (code == 200) {
                loginField.clear();
                passwordField.clear();
                showAlert("Успешно!", "Добавлен новый пользователь. Логин: " + login + ", Пароль:" + password, 1);
                initData();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void deleteUser(ActionEvent actionEvent) {
        if (chosenProfile.getText() != null && !Objects.equals(chosenProfile.getText(), "")) {
            if (Objects.equals(ServiceSingleton.getInstance().getCurrentUser(), chosenProfile.getText())) {
                showAlert("Попытка удаления активного профиля!", "Остановлена попытка удаления собственного профиля!", 2);
            } else {
                if (systUserTable.getSelectionModel().isEmpty()) {
                    showAlert("Не выбран элемент таблицы!", "Выберите поле таблицы, которое хотите удалить.", 2);
                } else {
                    try {
                        HttpResponse response = SimpleRequestManager.sendDeleteRequest("/delete-user", "user_id=" + userId);
                        int code = response.getResponseCode();
                        if (code == 200) {
                            showAlert("Успешно!", "Пользователь с id " + userId + " был удален.", 1);
                            initData();
                        } else if (code == 400) {
                            showAlert("Не удалось получить id", "Что-то пошло не так.", 2);
                        } else if (code == 404) {
                            showAlert("Не удалось найти пользователя", "Такой пользователь не найден.", 2);
                        } else if (code == 409) {
                            showAlert("Не удалось провести удаление", "Данный пользователь .", 2);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    initData();
                }
            }
        } else {
            showAlert("Не выбрана запись!", "Выберите поле из таблицы, которые хотите удалить!", 2);
        }
    }

    public void changeUserStatus(ActionEvent actionEvent) {
        if (choiceBoxStatus.getSelectionModel().getSelectedItem() == null) {
            showAlert("Не выбран статус!", "Выберите статус, который будет применен к профилю!", 2);
            return;
        }

        String choice_status;
        if (Objects.equals(choiceBoxStatus.getSelectionModel().getSelectedItem(), "Администратор")) {
            choice_status = "1";
        } else {
            choice_status = "0";
        }
        if (Objects.equals(current_status, choice_status)) {
            showAlert("Попытка ненужного обновления статуса", "Статус выбранного пользователя уже определен как " + choiceBoxStatus.getSelectionModel().getSelectedItem(), 2);
            return;
        }

        if (systUserTable.getSelectionModel().isEmpty()) {
            showAlert("Не выбран элемент таблицы!", "Выберите поле, в котором хотите изменить статус.", 2);
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
                    showAlert("Успешно!", "Изменен статус пользователя с id " + userId + " на '" + choiceBoxStatus.getSelectionModel().getSelectedItem() + "'.", 1);

                    initData();
                } else if (responseCode == 403) {
                    showAlert("Попытка изменения активного профиля!", "Остановлена попытка изменения статуса собственного профиля!", 2);
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }


    private void showAlert(String title, String warning, int code) {
        Alert alert = null;
        if (code == 1) {
            alert = new Alert(Alert.AlertType.INFORMATION);
        } else if (code == 2) {
            alert = new Alert(Alert.AlertType.WARNING);
        }
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(warning);
        alert.showAndWait();
    }
}
