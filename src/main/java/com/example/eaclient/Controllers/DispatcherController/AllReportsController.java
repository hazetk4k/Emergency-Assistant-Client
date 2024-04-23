package com.example.eaclient.Controllers.DispatcherController;

import com.example.eaclient.Controllers.WindowManager;
import com.example.eaclient.Models.AllReportsTable;
import com.example.eaclient.Network.HttpResponse;
import com.example.eaclient.Network.SimpleRequestManager;
import com.example.eaclient.Network.WebSocketClientEndpoint;
import com.example.eaclient.Service.ServiceSingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.WebSocketContainer;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    Thread webSocketThread;

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

    ObservableList<AllReportsTable> initialData() {
        ObservableList<AllReportsTable> reportsTable = FXCollections.observableArrayList();
        try {
            HttpResponse httpResponse = SimpleRequestManager.sendGetRequest("/get-all-reports");
            int response_code = httpResponse.getResponseCode();
            if (response_code == 200) {
                String responseBody = httpResponse.getResponseBody();
                if (Objects.equals(responseBody, "[]")) {
                    Label label_placeholder = new Label("Заявления отсутствуют");
                    label_placeholder.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
                    tableView.setPlaceholder(label_placeholder);
                } else {
                    Type typeOfReports = new TypeToken<ArrayList<AllReportsTable>>() {
                    }.getType();
                    List<AllReportsTable> tempReportList = gson.fromJson(responseBody, typeOfReports);

                    reportsTable.addAll(tempReportList);
                    System.out.println(tempReportList);
                }
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

        webSocketThread = new Thread(() -> {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            WebSocketClientEndpoint client = new WebSocketClientEndpoint();
            try {
                container.connectToServer(client, new URI("ws://localhost:8085/"));
            } catch (DeploymentException | IOException | URISyntaxException e) {
                System.out.println(e.getMessage());
            }
        });

        webSocketThread.start();
    }
}
