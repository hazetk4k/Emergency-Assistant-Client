package com.example.eaclient.Controllers.DispatcherController;

import com.example.eaclient.Controllers.WindowManager;
import com.example.eaclient.Models.AllReportsTable;
import com.example.eaclient.Service.ServiceSingleton;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;


public class AllReportsController {
    public Button toggleMenuButton;
    @FXML
    public VBox burgerMenu;
    @FXML
    public TableView<AllReportsTable> tableView;
    @FXML
    public TableColumn<AllReportsTable, Integer> id;
    @FXML
    public TableColumn<AllReportsTable, String> type;
    @FXML
    public TableColumn<AllReportsTable, String> timestamp;
    @FXML
    public TableColumn<AllReportsTable, String> place;
    @FXML
    public TableColumn<AllReportsTable, String> fio;
    @FXML
    public TableColumn<AllReportsTable, Boolean> wasSeen;
    @FXML
    public Button toReportsListButton;
    @FXML
    public Button openSaveDirectoryButton;
    @FXML
    public Button toAuthWindowButton;

    WindowManager manager = new WindowManager();

    public void toggleMenu(ActionEvent actionEvent) {
        TranslateTransition menuAnimation = new TranslateTransition(Duration.millis(300), burgerMenu);

        if (!burgerMenu.isVisible()) {
            menuAnimation.setToY(0);
            burgerMenu.setVisible(true);
            toggleMenuButton.setText("✖");
            menuAnimation.play();
        } else {
            menuAnimation.setToY(-burgerMenu.getWidth());
            burgerMenu.setVisible(false);
            toggleMenuButton.setText("☰");
            menuAnimation.play();
        }
    }

    public void toReportsList(ActionEvent actionEvent) {
    }

    public void openSaveDirectory(ActionEvent actionEvent) {
    }

    public void toAuthWindow() throws IOException {
        manager.openFxmlScene("/fxml/AuthWindow.fxml", "Авторизация");
        ServiceSingleton.getInstance().setIfClosed(true);
        Stage stage = (Stage) toAuthWindowButton.getScene().getWindow();
        stage.close();
    }
}
