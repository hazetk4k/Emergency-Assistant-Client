package com.example.eaclient;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml/AuthWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());


        stage.setOnCloseRequest(event -> {
            System.out.println("Работа завершена");
            System.exit(0);
        });

        stage.setTitle("Авторизация");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}