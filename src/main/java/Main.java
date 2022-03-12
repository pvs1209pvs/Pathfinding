
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import fx.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        HBox root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fx/sample.fxml")));
        primaryStage.setTitle("Pathfinding");
        Scene scene = new Scene(root, 100 + Controller.CANVAS_SIZE, Controller.CANVAS_SIZE);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("fx/style.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
