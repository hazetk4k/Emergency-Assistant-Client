package com.example.eaclient.Controllers.DispatcherController;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class NoTypeReportController {
    @FXML
    public TextField typeField;
    @FXML
    public TextArea listOfServices;
    @FXML
    public ListView<String> listChsServices;
    @FXML
    public Button btnCleanServices;
    @FXML
    public TextField areThereAnyCasualties;
    @FXML
    public TextField dateAndTime;
    @FXML
    public TextField isUserInDanger;
    @FXML
    public TextField fio;
    @FXML
    public TextField email;
    @FXML
    public TextField homeAddress;
    @FXML
    public TextArea additionalData;
    @FXML
    public Button resolveButton;
    @FXML
    public Button reportButton;
    @FXML
    public ComboBox<String> cmbBoxKinds;
    @FXML
    public TextArea recommendationsTextArea;
    @FXML
    public TextField place;
    @FXML
    public TextField chosenServices;
    @FXML
    public TextField amountOfCasualties;
    @FXML
    public ChoiceBox<String> choiceBoxChar;


    private void selectionChanged(ObservableValue<? extends String> Observable, String oldVal, String newVal) {
    }

    public void generateReport(ActionEvent actionEvent) {
    }

    private boolean areFieldsFilled() {
        return false;
    }

    public void markResolved(ActionEvent actionEvent) {

    }

    private void showWarning() {
    }

    private void showAlert() {
    }

    public void cleanUpServices(ActionEvent actionEvent) {
    }
}
