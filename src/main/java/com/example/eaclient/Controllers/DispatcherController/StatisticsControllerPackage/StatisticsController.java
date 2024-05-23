package com.example.eaclient.Controllers.DispatcherController.StatisticsControllerPackage;

import com.example.eaclient.Models.Statistics.FinishedReports;
import com.example.eaclient.Network.HttpRequests.HttpResponse;
import com.example.eaclient.Network.HttpRequests.SimpleRequestManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class StatisticsController {
    @FXML
    public TableView<FinishedReports> finishedReportsTable;
    @FXML
    public TableColumn<FinishedReports, Integer> report_id;
    @FXML
    public TableColumn<FinishedReports, String> receive_time;
    @FXML
    public TableColumn<FinishedReports, String> end_actions_time;
    @FXML
    public TableColumn<FinishedReports, String> address_district;
    @FXML
    public TableColumn<FinishedReports, String> type_name;
    @FXML
    public TableColumn<FinishedReports, List<String>> all_services;
    public Menu btnMakeStatisticsWord;
    public Menu btnOpensStatistics;

    public void initData() {
        // Инициализация столбцов таблицы
        report_id.setCellValueFactory(new PropertyValueFactory<>("reportId"));
        receive_time.setCellValueFactory(new PropertyValueFactory<>("receiveTime"));
        end_actions_time.setCellValueFactory(new PropertyValueFactory<>("endActionsTime"));
        address_district.setCellValueFactory(new PropertyValueFactory<>("addressDistrict"));
        type_name.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        all_services.setCellValueFactory(new PropertyValueFactory<>("allServices"));

        all_services.setCellFactory(column -> new TableCell<FinishedReports, List<String>>() {
            private final ListView<String> listView = new ListView<>();

            {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            }

            @Override
            protected void updateItem(List<String> services, boolean empty) {
                super.updateItem(services, empty);
                if (empty || services == null) {
                    setGraphic(null);
                } else {
                    listView.setItems(FXCollections.observableArrayList(services));
                    setGraphic(listView);
                }
            }
        });

        ObservableList<FinishedReports> reportsList = getAllTodayChoices();
        finishedReportsTable.setItems(reportsList);

        finishedReportsTable.setRowFactory(tv -> new TableRow<FinishedReports>() {
            @Override
            public void updateItem(FinishedReports item, boolean empty) {
                super.updateItem(item, empty);
                // Проверяем, есть ли данные в строке
                if (item == null || empty) {
                    setStyle(""); // Если нет данных, сбрасываем стиль
                } else {
                    // Устанавливаем высоту строки
                    setMinHeight(100);
                    setPrefHeight(100);
                    setMaxHeight(2000);
                }
            }
        });
    }

    public ObservableList<FinishedReports> getAllTodayChoices() {
        ObservableList<FinishedReports> finishedReports = FXCollections.observableArrayList();
        try {
            HttpResponse httpResponse = SimpleRequestManager.sendGetRequest("/get-today-reports");
            int response_code = httpResponse.getResponseCode();
            if (response_code == 200) {
                String jsonResponse = httpResponse.getResponseBody(); // Получаем JSON ответ
                Gson gson = new Gson();
                JsonArray jsonArray = gson.fromJson(jsonResponse, JsonArray.class); // Преобразуем JSON в массив

                for (JsonElement jsonElement : jsonArray) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    int reportId = jsonObject.get("reportId").getAsInt();
                    String receiveTime = jsonObject.get("receiveTime").getAsString();
                    String endActionsTime = jsonObject.get("endActionsTime").getAsString();
                    String addressDistrict = jsonObject.get("addressDistrict").getAsString();
                    String typeName = jsonObject.get("typeName").getAsString();

                    Type listType = new TypeToken<List<String>>() {}.getType();
                    List<String> services = gson.fromJson(jsonObject.get("services"), listType);

                    FinishedReports report = new FinishedReports(reportId, receiveTime, endActionsTime, addressDistrict, typeName, services);
                    finishedReports.add(report);
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return finishedReports;
    }


    public void makeStaticsReport(ActionEvent actionEvent) {
    }

    public void openStatics(ActionEvent actionEvent) {
    }
}
