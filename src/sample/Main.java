package sample;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        HBox root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Pathfinding");
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
