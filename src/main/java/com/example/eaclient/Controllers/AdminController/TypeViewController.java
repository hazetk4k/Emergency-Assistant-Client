package com.example.eaclient.Controllers.AdminController;

import com.example.eaclient.Models.TypeEm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class TypeViewController {
    @FXML
    public TableView<TypeEm> tableTypes;
    @FXML
    public ChoiceBox<String> kindChoiceBox;
    @FXML
    public Button btnDeleteType;
    @FXML
    public TextField fieldTypeName;
    @FXML
    public TableColumn<TypeEm, String> type;
    @FXML
    public TableColumn<TypeEm, String> recommendations;
    @FXML
    public TableColumn<TypeEm, String> kind;
    @FXML
    public TextArea recoms;
    @FXML
    public Button btnAddNewType;
    @FXML
    public TextField chosenType;

    public void initData() {
    }

    public void deleteType(ActionEvent actionEvent) {
    }

    private void showAlert(String title, String warning) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(warning);
        alert.showAndWait();
    }

    public void addNewType(ActionEvent actionEvent) {
    }
}
