package test.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class JavaFxTest extends Application {

    public static void main(String...args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MenuButton choiceBox = new MenuButton();
        for (int i = 0; i < 100; i++) {
            choiceBox.getItems().add(new MenuItem("AAA: " + i));
        }

        primaryStage.setScene(new Scene(new StackPane(choiceBox), 640, 480));
        primaryStage.show();
    }

}
