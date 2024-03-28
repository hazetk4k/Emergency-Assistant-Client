package com.example.eaclient.Controllers;

import com.example.eaclient.Main;
import com.example.eaclient.Service.ServiceSingleton;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class WindowManager {

    public void openFxmlScene(String name, String title) throws IOException {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (ServiceSingleton.getInstance().getIfClosed()) {
                    System.out.println("Работа завершена");
                    System.exit(0);
                }
                ServiceSingleton.getInstance().setIfClosed(true);
            }
        });

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(name));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

}