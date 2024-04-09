package com.example.eaclient.Controllers.AdminController;

import com.example.eaclient.Models.SystemUser;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class UserViewController {
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public RadioButton radIsAdmin;
    @FXML
    public Button btnAddUser;
    @FXML
    public Button btnDelete;
    @FXML
    public ChoiceBox<String> choiceBoxStatus;
    @FXML
    public Button btnChangeStatus;
    @FXML
    public TableView<SystemUser> systUserTable;
    @FXML
    public TableColumn<SystemUser, Integer> id_syst;
    @FXML
    public TableColumn<SystemUser, String> login_syst;
    @FXML
    public TableColumn<SystemUser, String> status_syst;
    @FXML
    public TextField chosenProfile;
    public int userId;

    public void initData() {
        choiceBoxStatus.getItems().clear();
        choiceBoxStatus.getItems().addAll("Администратор", "Диспетчер");
        id_syst.setCellValueFactory(new PropertyValueFactory<SystemUser, Integer>("id_syst"));
        login_syst.setCellValueFactory(new PropertyValueFactory<SystemUser, String>("login_syst"));
        status_syst.setCellValueFactory(new PropertyValueFactory<SystemUser, String>("status_syst"));
        systUserTable.setItems(initialData());
    }

    ObservableList<SystemUser> initialData() {
        return null;
    }

    public void addNewUser(ActionEvent actionEvent) {

    }

    public void deleteUser(ActionEvent actionEvent) {

    }

    private void showAlert(String title, String warning) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(warning);
        alert.showAndWait();
    }

    public void changeUserStatus(ActionEvent actionEvent) {

    }
}
