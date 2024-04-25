package com.example.eaclient.Controllers.DispatcherController;

import java.time.LocalDateTime;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TypeReportController {
    @FXML
    public TextArea recommendationsTextArea;//сделано
    @FXML
    public ListView<String> listOfServices; //сделано
    @FXML
    public TextArea EmergencyField; //сделано
    @FXML
    public TextField areThereAnyCasualties; //сделано
    @FXML
    public TextField amountOfCasualities; //сделано
    @FXML
    public TextField dateAndTime; //сделано
    @FXML
    public TextField isUserInDanger; // переделать
    @FXML
    public TextArea additionalData; //сделано
    @FXML
    public TextField fio; //сделано
    @FXML

    public TextField email; //сделано
    @FXML
    public TextField homeAddress; //сделано
    @FXML
    public Button resolveButton; //сделано
    @FXML
    public Button reportButton;


    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Выполнено");
        alert.setHeaderText(null);
        LocalDateTime currentDateTime = LocalDateTime.now();
        alert.setContentText("Ситуация разрешена.");
        alert.showAndWait();
    }

    public void markResolved(ActionEvent actionEvent) {

    }

    public void generateReport(ActionEvent actionEvent) {

    }
}
