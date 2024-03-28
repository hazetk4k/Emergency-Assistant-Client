package com.example.eaclient.Controllers;

import com.example.eaclient.Service.ServiceSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AuthController {
    @FXML
    public TextField fieldLogin;
    @FXML
    public PasswordField fieldPassword;
    @FXML
    public Label labelProgress;
    @FXML
    public Button button;
    public WindowManager manager = new WindowManager();

    public void authorizeUser() throws IOException {
        manager.openFxmlScene("/fxml/DispatcherWindows/AllReportsWindow.fxml", "Отслеживание новых заявлений");
        ServiceSingleton.getInstance().setIfClosed(true);
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();

    }
}
