module com.example.eaclient {

    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires jakarta.websocket.client;
    requires org.apache.poi.ooxml;
    requires org.apache.xmlbeans;

    opens com.example.eaclient.Controllers to javafx.fxml;
    opens com.example.eaclient.Controllers.AdminController to javafx.fxml;
    opens com.example.eaclient.Controllers.DispatcherController to javafx.fxml;

    opens fxml.AdminSettingsWindows to javafx.fxml;
    opens fxml.DispatcherWindows to javafx.fxml;
    opens fxml to javafx.fxml;
    opens css to javafx.fxml;
    opens images to javafx.fxml;

    exports com.example.eaclient;
    exports com.example.eaclient.Controllers;
    exports com.example.eaclient.Controllers.AdminController;
    exports com.example.eaclient.Controllers.DispatcherController;
    exports com.example.eaclient.Network.HttpRequests;
    exports com.example.eaclient.Network.WebSocket;
    exports com.example.eaclient.Models.ReportTableModels;
    exports com.example.eaclient.Models.ReportWindowModels;
    exports com.example.eaclient.Models.AdminModels;
    exports com.example.eaclient.Models.Statistics;
    exports com.example.eaclient.Controllers.DispatcherController.ReportControllerPackage;
    opens com.example.eaclient.Controllers.DispatcherController.ReportControllerPackage to javafx.fxml;
    exports com.example.eaclient.Controllers.DispatcherController.StatisticsControllerPackage;
    opens com.example.eaclient.Controllers.DispatcherController.StatisticsControllerPackage to javafx.fxml;


}