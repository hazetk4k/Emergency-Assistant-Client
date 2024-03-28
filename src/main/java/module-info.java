module com.example.eaclient {

    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.eaclient.Controllers to javafx.fxml;
    opens com.example.eaclient.Controllers.AdminController to javafx.fxml;
    opens com.example.eaclient.Controllers.DispatcherController to javafx.fxml;

    opens fxml.AdminSettingsWindows to javafx.fxml;
    opens fxml.DispatcherWindows to javafx.fxml;
    opens fxml to javafx.fxml;

    exports com.example.eaclient;
    exports com.example.eaclient.Controllers;
    exports com.example.eaclient.Models;
    exports com.example.eaclient.Controllers.AdminController;
    exports com.example.eaclient.Controllers.DispatcherController;

    opens css to javafx.fxml;
    opens images to javafx.fxml;
}