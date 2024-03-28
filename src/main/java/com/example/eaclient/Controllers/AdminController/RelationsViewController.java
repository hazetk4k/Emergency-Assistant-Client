package com.example.eaclient.Controllers.AdminController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;

public class RelationsViewController {
    @FXML
    public TreeView<String> relationsTreeView;
    @FXML
    public ChoiceBox<String> choiceBoxKinds;
    @FXML
    public ChoiceBox<String> choiceBoxServices;
    @FXML
    public TextField chosenKind;
    @FXML
    public TextField chosenService;
    @FXML
    public String kindOfService;
    @FXML
    public TextField textKindOfService;

    public void initData() {
    }

    private void initAdditional() {
    }

    public void addNewRelation(ActionEvent actionEvent) {
    }

    private void showAlert(String title, String warning) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(warning);
        alert.showAndWait();
    }

    public void deleteServiceRel(ActionEvent actionEvent) {
    }

    public void deleteAllRels(ActionEvent actionEvent) {
    }
}
