package com.example.eaclient.Controllers.DispatcherController.ReportControllerPackage;

import com.example.eaclient.Controllers.DispatcherController.ApplicantProfileController;
import com.example.eaclient.Models.ReportWindowModels.Applicant;
import com.example.eaclient.Models.ReportWindowModels.ServiceTransportPair;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class Util_ReportManager {

    private final ReportController c;

    public Util_ReportManager(ReportController c){
        this.c = c;
    }

    //доп методы

    public String makeFIOAndPhoneString(String fio, String phone) {
        return fio + ". Телефон: +375" + phone;
    }

    //Дополнительные методы работы со службами
    public List<String> extractServiceNames(String data) {
        List<String> serviceNames = new ArrayList<>();
        String[] records = data.split("\n");
        for (String record : records) {
            String[] parts = record.split("//");
            if (parts.length >= 1) {
                serviceNames.add(parts[0]);
            } else {
                System.err.println("Неправильный формат записи: " + record);
            }
        }
        return serviceNames;
    }

    public String getAllServiceTransportRecordsAsString(TableView<ServiceTransportPair> table) {
        StringBuilder sb = new StringBuilder();
        for (ServiceTransportPair pair : table.getItems()) {
            sb.append(pair.getService()).append("//").append(pair.getTransport()).append("\n");
        }
        return sb.toString();
    }

    //работа с таблицами и чекбоксами
    public void addRecordsFromString(TableView<ServiceTransportPair> table, String data) {
        String[] records = data.split("\n");
        for (String record : records) {

            String[] parts = record.split("//");
            if (parts.length == 2) {
                ServiceTransportPair pair = new ServiceTransportPair(parts[0], parts[1]);
                table.getItems().add(pair);
            } else {
                System.err.println("Неправильный формат записи: " + record);
            }
        }
    }

    public void setUpChosenServices(ListView<CheckBox> list, List<String> chosenServices) {
        for (CheckBox checkBox : list.getItems()) {
            String checkBoxText = checkBox.getText().trim();
            for (String service : chosenServices) {
                if (service.trim().equals(checkBoxText)) {
                    checkBox.setSelected(true);
                    break;
                }
            }
        }
    }

    public void setCVFForChosenServiceColumn(TableColumn<ServiceTransportPair, String> serviceColumn, TableColumn<ServiceTransportPair, String> autoColumn, boolean shouldAddChoiceBox) {
        serviceColumn.setCellValueFactory(data -> data.getValue().serviceProperty());
        autoColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTransport()));
        if (shouldAddChoiceBox) {
            autoColumn.setCellFactory(column -> new TableCell<>() {
                private final ChoiceBox<String> choiceBox = new ChoiceBox<>();

                {
                    // Устанавливаем слушатель на изменение значения в ChoiceBox
                    choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue != null && !newValue.isEmpty()) {
                            // Получаем текущую выбранную строку таблицы и устанавливаем для нее выбранный транспорт
                            ServiceTransportPair pair = getTableView().getItems().get(getIndex());
                            pair.setTransport(newValue);
                        }
                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(choiceBox);
                        // Загружаем возможные варианты транспорта
                        ServiceTransportPair pair = getTableView().getItems().get(getIndex());
                        List<String> autos = c.requestsManager.getServicesAutoByName(pair.getService(), c.districtsChoiceBox.getValue());
                        choiceBox.getItems().setAll(autos);
                        choiceBox.getSelectionModel().select(item);
                    }
                }
            });
        } else {
            autoColumn.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                }
            });
        }
    }


    public void setUpServicesListener(ListView<CheckBox> servicesList, TableView<ServiceTransportPair> chosenServicesTable, List<ServiceTransportPair> pairs, boolean shouldAddListeners) {
        // Очищаем список служб перед добавлением новых
        pairs.clear();

        if (shouldAddListeners) {
            for (CheckBox checkBox : servicesList.getItems()) {
                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    manageServiceSelection(newValue, checkBox.getText(), chosenServicesTable, pairs);
                });
            }
        }
    }

    private void manageServiceSelection(boolean isSelected, String serviceName, TableView<ServiceTransportPair> table, List<ServiceTransportPair> pairs) {
        if (isSelected) {
            // Добавляем службу
            ServiceTransportPair newPair = new ServiceTransportPair(serviceName, "");
            table.getItems().add(newPair);
            pairs.add(newPair);
        } else {
            // Удаляем службу
            ServiceTransportPair pairToRemove = pairs.stream()
                    .filter(p -> p.getService().equals(serviceName))
                    .findFirst()
                    .orElse(null);
            if (pairToRemove != null) {
                pairs.remove(pairToRemove);
                table.getItems().remove(pairToRemove);
            }
        }
        // Установка кастомного placeholder
        if (table.getItems().isEmpty()) {
            Label customPlaceholder = new Label("Службы не выбраны.");
            table.setPlaceholder(customPlaceholder);
        } else {
            table.setPlaceholder(null); // Убираем placeholder, если в таблице есть записи
        }
    }


    //Методы работы с отдельными функциями
    public void openApplicantProfile(Applicant applicant){
        try {
            c.buttonOpenApplicantProfile.setDisable(true);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DispatcherWindows/ApplicantProfileWindow.fxml"));
            Parent root = loader.load();

            ApplicantProfileController controller = loader.getController();
            controller.initData(applicant);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(event -> {
                // Включаем кнопку при закрытии окна
                c.buttonOpenApplicantProfile.setDisable(false);
            });
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void loadServicesIntoUI(String data, TableView<ServiceTransportPair> table, ListView<CheckBox> listView) {
        List<String> chosenServices = extractServiceNames(data);

        // Загрузка данных в таблицу
        table.getItems().clear();
        addRecordsFromString(table, data);

        // Установка чекбоксов
        for (CheckBox checkBox : listView.getItems()) {
            checkBox.setSelected(chosenServices.contains(checkBox.getText()));
        }
    }
}
