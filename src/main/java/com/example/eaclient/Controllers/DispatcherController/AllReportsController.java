package com.example.eaclient.Controllers.DispatcherController;

import com.example.eaclient.Controllers.WindowManager;
import com.example.eaclient.Models.AllReportsTable;
import com.example.eaclient.Network.HttpResponse;
import com.example.eaclient.Network.SimpleRequestManager;
import com.example.eaclient.Service.ServiceSingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class AllReportsController implements Initializable {
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

    private final Gson gson = new Gson();

    public void toggleMenu() {
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

    // Доделать
    public void toReportsList(ActionEvent actionEvent) {

    }

    // Доделать
    public void openSaveDirectory(ActionEvent actionEvent) {

    }

    // Сделано
    public void toAuthWindow() throws IOException {
        manager.openFxmlScene("/fxml/AuthWindow.fxml", "Авторизация");
        ServiceSingleton.getInstance().setIfClosed(true);
        Stage stage = (Stage) toAuthWindowButton.getScene().getWindow();
        stage.close();
    }

    //TODO: Добавить замену timestamp на "словянский тип"
    ObservableList<AllReportsTable> initialData() {
        ObservableList<AllReportsTable> reportsTable = FXCollections.observableArrayList();
        try {
            HttpResponse httpResponse = SimpleRequestManager.sendGetRequest("/get-all-reports");
            System.out.println("Запрос отправлен");
            int response_code = httpResponse.getResponseCode();
            if (response_code == 200) {
                String responseBody = httpResponse.getResponseBody();
                Type typeOfReports = new TypeToken<ArrayList<AllReportsTable>>() {
                }.getType();
                List<AllReportsTable> tempReportList = gson.fromJson(responseBody, typeOfReports);

                reportsTable.addAll(tempReportList);
                System.out.println(tempReportList);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return reportsTable;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        id.setCellValueFactory(new PropertyValueFactory<AllReportsTable, Integer>("id"));
        type.setCellValueFactory(new PropertyValueFactory<AllReportsTable, String>("type"));
        timestamp.setCellValueFactory(new PropertyValueFactory<AllReportsTable, String>("timestamp"));
        place.setCellValueFactory(new PropertyValueFactory<AllReportsTable, String>("place"));
        fio.setCellValueFactory(new PropertyValueFactory<AllReportsTable, String>("fio"));
        wasSeen.setCellValueFactory(new PropertyValueFactory<AllReportsTable, Boolean>("wasSeen"));

        tableView.setItems(initialData());
    }
}
