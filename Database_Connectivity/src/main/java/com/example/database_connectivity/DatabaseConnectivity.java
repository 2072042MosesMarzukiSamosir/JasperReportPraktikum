package com.example.database_connectivity;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DatabaseConnectivity extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DatabaseConnectivity.class.getResource("first_page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Praktikum Minggu#5 DATABASE_CONNECTIVITY");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}