package com.example.eaclient.Controllers.AdminController;

import com.example.eaclient.Controllers.WindowManager;
import com.example.eaclient.Service.ServiceSingleton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {
    @FXML
    public VBox burgerMenu;
    @FXML
    public Button btnOpenKinds;
    @FXML
    public Button btnOpenTypes;
    @FXML
    public Button btnOpenUsers;
    @FXML
    public Button btnBackToAuth;
    @FXML
    public Button btnOpenRels;
    WindowManager manager = new WindowManager();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void backToAuth() throws IOException {
        manager.openFxmlScene("/fxml/AuthWindow.fxml", "Авторизация");
        ServiceSingleton.getInstance().setIfClosed(true);
        Stage stage = (Stage) btnBackToAuth.getScene().getWindow();
        stage.close();
    }

    public void openUserOptions() {
    }

    public void openTypeOptions() {
    }

    public void openKindOptions() {
    }

    public void openRelationsOptions() {
    }
}
