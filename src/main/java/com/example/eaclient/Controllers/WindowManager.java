package com.example.eaclient.Controllers;

import com.example.eaclient.Main;
import com.example.eaclient.Network.WebSocket.WebSocketClient;
import com.example.eaclient.Service.ServiceSingleton;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class WindowManager {

    public void openFxmlScene(String name, String title) throws IOException {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setOnCloseRequest(event -> {
            if (ServiceSingleton.getInstance().getIfClosed()) {
                System.out.println("WindowManager; openFXMLScene: Работа завершена");
                WebSocketClient.getInstance().disconnect();
                System.exit(0);
            }
            ServiceSingleton.getInstance().setIfClosed(true);
        });

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(name));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public static void showAlert(String title, String warning, int code) {
        Alert alert = null;
        if (code == 1){
            alert = new Alert(Alert.AlertType.INFORMATION);
        } else if(code == 2){
            alert = new Alert(Alert.AlertType.WARNING);
        }
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(warning);
        alert.showAndWait();
    }

}