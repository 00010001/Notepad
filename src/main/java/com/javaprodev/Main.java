package com.javaprodev;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private final String VERSION = "0.0.2";
    static FXMLLoader fxmlLoader;

    @Override
    public void start(Stage primaryStage) throws Exception{

        fxmlLoader = new FXMLLoader(getClass().getResource("/main.fxml"));
        Parent root = fxmlLoader.load();
        MainController controller = fxmlLoader.getController();
        controller.initialize(primaryStage);

        primaryStage.setTitle("Notepad ver. " + VERSION);
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.setResizable(true);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}