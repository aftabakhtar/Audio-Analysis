package pk.edu.nust.seecs.oop.project.controllers;

import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import pk.edu.nust.seecs.oop.project.helpers.FileDirectoryHandler;
import pk.edu.nust.seecs.oop.project.helpers.GraphHelper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GraphController implements Initializable {

    // Attributes.
    private GraphHelper graphHelper = new GraphHelper();
    private FileDirectoryHandler fileDirectoryHandler = new FileDirectoryHandler();
    private String currentFilePath;

    // FXML Attributes.
    @FXML
    private ImageView imageView = new ImageView();

    @FXML
    private JFXSpinner spinner;

    @FXML
    private StackPane stackPane;

    // Methods.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Generating waveform when window appears (whole process takes place in a thread)
        setGraphOnViewer(MainController.browseFieldText, "cache", true, MainController.waveform);
    }

    // Method to handle the back button press.
    @FXML
    private void backButtonPressed(ActionEvent event) throws IOException {
        MainController mainController = new MainController();
        String URL = "views/main.fxml";
        String title = "Audio Analysis";
        mainController.changeScene(URL, title, 492, 285, event);
    }

    // Method for modular approach for generating graph and displaying it on the image-view in thread.
    private void setGraphOnViewer(String pathOfFile, String savePath, boolean firstTime, boolean waveform) {
        // Implementing new thread and starting spinner.
        currentFilePath = pathOfFile;
        spinner.setVisible(true);
        new Thread(() -> {
            graphHelper.generateGraph(pathOfFile, savePath, firstTime, waveform);

            // Creating a file.
            File file = new File("cache/temp.jpg");

            // Loading the Image.
            Image image = new Image(file.toURI().toString());

            // Creating image view object.
            if (image.getWidth() > 760) {
                imageView.setPreserveRatio(false);
            }
            else {
                imageView.setPreserveRatio(true);
            }
            Platform.runLater(()-> {
                imageView.setImage(image);
                spinner.setVisible(false);
            });
        }).start();
    }

    // Method for handling the Open button press.
    @FXML
    private void openButtonPressed() {
        // It will prompt a file opener.
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open .wav File");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            boolean extension = fileDirectoryHandler.isWAVFile(file.getAbsolutePath());
            if (extension) {
                setGraphOnViewer(file.getAbsolutePath(), "cache", true, MainController.waveform);
            } else {
                String title = "Error";
                String message = "Please select a file of .wav format";
                fileDirectoryHandler.displayDialog(this.stackPane, title, message);
            }
        }
    }

    // Method to handle save button press.
    @FXML
    private void saveButtonPressed() {
        // It will prompt a file saver.
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select location");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG", "*.jpg"));
        fileChooser.setInitialFileName("Graph.jpg");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            if (file.getName().endsWith(".jpg")) {
                // Setting graph on the gui.
                setGraphOnViewer(currentFilePath, file.getAbsolutePath(), false, MainController.waveform);
            }
            else {
                String title = "Error";
                String message = "Please make sure the extension is .jpg";
                fileDirectoryHandler.displayDialog(this.stackPane, title, message);
            }
        }
    }
}
