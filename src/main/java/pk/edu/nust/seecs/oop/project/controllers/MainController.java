package pk.edu.nust.seecs.oop.project.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pk.edu.nust.seecs.oop.project.helpers.FileDirectoryHandler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

// Implementing Initializable interface
public class MainController implements Initializable {
    // Attributes.
    private FileDirectoryHandler fileDirectoryHandler = new FileDirectoryHandler();
    static String directoryPath;
    static String browseFieldText = "";
    static boolean waveform;

    // FXML Attributes.
    @FXML
    private StackPane dialogStackPane;

    @FXML
    private JFXButton proceed;

    @FXML
    private JFXTextField directoryPathField;

    @FXML
    private ToggleGroup operationSelected;

    @FXML
    private JFXButton browse;

    // Methods handling the operations.

    // Method to handle the browse button press. (FXML Method)
    @FXML
    public void browseButtonPressed() {
        String path;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select audio file");
        File file = fileChooser.showOpenDialog(null);
        if (!(file == null)) {
            path = file.getAbsolutePath();
            browseFieldText = path;
            directoryPathField.setText(path);
        }
    }

    // Method to handle proceed button press. (FXML Method)
    @FXML
    public void proceedButtonPressed(ActionEvent event) throws IOException {
        // New changing checking extension on proceed button press.
        boolean extension = fileDirectoryHandler.isWAVFile(directoryPathField.getText());

        // Changing the error to occur through a function call instead of defining it here.
        if (directoryPathField.getText().isEmpty() || !(extension)) {
            String title = "Error";
            String message = null;
            if (directoryPathField.getText().isEmpty()) {
                message = "Please specify the path of the file to perform the required operation";
            }
            else {
                message = "Please select a file of .wav format";
            }
            fileDirectoryHandler.displayDialog(this.dialogStackPane, title, message);
        }
        else {
            RadioButton selectedRadioButton = (RadioButton) operationSelected.getSelectedToggle();
            String selectedRadioButtonText = selectedRadioButton.getText();
            if (selectedRadioButtonText.equals("Audio Fingerprinting")) {
                // Changing scene to Audio Fingerprinting window.
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Select Library");
                File file = directoryChooser.showDialog(null);
                if (!(file == null)) {
                    String URL = "views/fingerprinting.fxml";
                    String title = "Audio Fingerprinting";
                    directoryPath = file.getAbsolutePath();
                    changeScene(URL, title, 1000, 600, event);
                }
            }
            else if (selectedRadioButtonText.equals("Generate Waveform")) {
                String URL = "views/graph.fxml";
                String title = "Waveform";
                waveform = true;
                changeScene(URL, title,  800, 520, event);
            }
            else if (selectedRadioButtonText.equals("Generate Spectrogram")) {
                String URL = "views/graph.fxml";
                String title = "Spectrogram";
                waveform = false;
                changeScene(URL, title, 800, 520, event);
            }
            else {
                String URL = "views/whistle.fxml";
                String title = "Whistle Probability";
                changeScene(URL, title, 800, 500, event);
            }
        }
    }

    // Method to set the next scene.
    void changeScene(String URL, String title, int width, int height, ActionEvent event) throws IOException {
        // Opening the Audio Fingerprinting window
        FXMLLoader loader = new FXMLLoader();
        URL location = getClass().getClassLoader().getResource(URL);
        loader.setLocation(location);
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root, width,height));
        stage.setResizable(false);
        stage.show();

        // Hiding this window.
        ((Node)(event.getSource())).getScene().getWindow().hide();

        // New Code: Setting focus to root.
        root.requestFocus();
    }

    // Method which will be called when this window appears. Using it to set browse field text.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        directoryPathField.setText(browseFieldText);
    }
}
