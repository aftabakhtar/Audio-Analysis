package pk.edu.nust.seecs.oop.project;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        URL location = getClass().getClassLoader().getResource("views/main.fxml");
        loader.setLocation(location);
        Parent root = loader.load();
        primaryStage.setTitle("Audio Analysis");
        primaryStage.setScene(new Scene(root, 492,285));
        primaryStage.setResizable(false);
        primaryStage.show();

        // New code below: disable the focus on the first radio button
        root.requestFocus();
    }
}
