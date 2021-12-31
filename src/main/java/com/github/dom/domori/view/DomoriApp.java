package com.github.dom.domori.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DomoriApp extends Application {

    public static void start(String...args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainViewController controller = new MainViewController();

        Scene scene = new Scene(controller.root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
