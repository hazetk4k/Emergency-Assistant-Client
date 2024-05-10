package com.example.eaclient.Controllers.AdminController;

import com.example.eaclient.Controllers.WindowManager;
import com.example.eaclient.Service.ServiceSingleton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
    public Button btnOpenAuto;
    public Button btnOpenServiceAutoRels;
    WindowManager manager = new WindowManager();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void backToAuth() throws IOException {
        manager.openFxmlScene("/fxml/AuthWindow.fxml", "Авторизация");
        ServiceSingleton.getInstance().setIfClosed(true);
        Stage stage = (Stage) btnBackToAuth.getScene().getWindow();
        stage.close();
        ServiceSingleton.getInstance().setCurrentUser(null);
        ServiceSingleton.getInstance().setCurrentUserId(-1);
    }

    public void openUserOptions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminSettingsWindows/UserViewWindow.fxml"));
            Parent root = loader.load();

            UserViewController controller = loader.getController();
            controller.initData();

            Stage stage = new Stage();
            stage.setTitle("Управление пользователями");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void openTypeOptions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminSettingsWindows/TypeViewWindow.fxml"));
            Parent root = loader.load();

            TypeViewController controller = loader.getController();
            controller.initData();

            Stage stage = new Stage();
            stage.setTitle("Управление типами ЧС");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void openKindOptions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminSettingsWindows/KindViewWindow.fxml"));
            Parent root = loader.load();

            KindViewController controller = loader.getController();
            controller.initData();

            Stage stage = new Stage();
            stage.setTitle("Управление видами ЧС");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void openRelationsOptions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminSettingsWindows/RelationsViewWindow.fxml"));
            Parent root = loader.load();

            RelationsViewController controller = loader.getController();
            controller.initData();

            Stage stage = new Stage();
            stage.setTitle("Управление связями между видом ЧС и службами реагирования");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void openAutoOptions(ActionEvent actionEvent) {
    }

    public void openServiceAutoRels(ActionEvent actionEvent) {
    }
}
