package com.example.eaclient.Controllers.AdminController;


import com.example.eaclient.Models.KindEm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class KindViewController {
    public Button deleteKind;
    public Button addNewKind;
    @FXML
    public ChoiceBox<String> charChoiceBox;
    public TextField newKindFiled;
    @FXML
    public TableView<KindEm> tableKind;
    @FXML
    public TableColumn<KindEm, Integer> id_kind;
    @FXML
    public TableColumn<KindEm, String> kind_name;
    @FXML
    public TableColumn<KindEm, String> char_name;
    @FXML
    public TextField chosenKind;

    private void showAlert(String title, String warning) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(warning);
        alert.showAndWait();
    }

    public void initData() {
    }

    public void deleteThisKind(ActionEvent actionEvent) {

    }

    public void addNewKind(ActionEvent actionEvent) {
    }

}
