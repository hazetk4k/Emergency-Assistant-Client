package com.example.eaclient.Controllers;

import com.example.eaclient.Network.HttpResponse;
import com.example.eaclient.Network.SimpleRequestManager;
import com.example.eaclient.Service.ServiceSingleton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AuthController {
    @FXML
    public TextField fieldLogin;
    @FXML
    public PasswordField fieldPassword;
    @FXML
    public Label labelProgress;
    @FXML
    public Button button;

    private final Gson gson = new Gson();
    public WindowManager manager = new WindowManager();

    public void authorizeUser() {
        try {
            labelProgress.setVisible(false);
            String login = fieldLogin.getText();
            String password = fieldPassword.getText();
            Map<String, String> requestData = new HashMap<>();
            requestData.put("login", login);
            requestData.put("password", password);
            String requestBody = gson.toJson(requestData);

            if (fieldPassword.getText() == null || Objects.equals(fieldPassword.getText(), "")) {
                fieldPassword.clear();
                fieldPassword.setStyle("-fx-prompt-text-fill: red");
                fieldPassword.setPromptText("Введите пароль");
                return;
            }

            if (fieldLogin.getText() == null || Objects.equals(fieldLogin.getText(), "")) {
                fieldPassword.clear();
                fieldPassword.setStyle("-fx-prompt-text-fill: red");
                fieldPassword.setPromptText("Введите логин");
                return;
            }

            HttpResponse response = SimpleRequestManager.sendPostRequest("/system-sign-in", requestBody);
            int responseCode = response.getResponseCode();

            if (responseCode == 401) {
                fieldPassword.clear();
                fieldPassword.setStyle("-fx-prompt-text-fill: red");
                fieldPassword.setPromptText("Неверный пароль");

            } else if (responseCode == 404) {
                fieldLogin.clear();
                fieldLogin.setStyle("-fx-prompt-text-fill: red");
                fieldLogin.setPromptText("Пользователь не найден");
                fieldPassword.clear();

            } else if (responseCode == -1) {
                labelProgress.setVisible(true);
                labelProgress.setText("Сервер не принимает запросы или не включен");

            } else if (responseCode == 200) {
                //получаем статус
                String responseBody = response.getResponseBody();
                JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
                String status = jsonObject.get("status").getAsString();
                //сверяем статус
                if (Objects.equals(status, "1")) {
                    ServiceSingleton.getInstance().setCurrentUser(login);
                    manager.openFxmlScene("/fxml/AdminSettingsWindows/AdminMenuWindow.fxml", "Меню настройки базы данных системы");
                    ServiceSingleton.getInstance().setIfClosed(true);
                    Stage stage = (Stage) button.getScene().getWindow();
                    stage.close();
                } else {
                    ServiceSingleton.getInstance().setCurrentUser(login);
                    manager.openFxmlScene("/fxml/DispatcherWindows/AllReportsWindow.fxml", "Отслеживание новых заявлений");
                    ServiceSingleton.getInstance().setIfClosed(true);
                    Stage stage = (Stage) button.getScene().getWindow();
                    stage.close();
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

}
