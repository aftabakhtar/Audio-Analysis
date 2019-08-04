package pk.edu.nust.seecs.oop.project.controllers;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import pk.edu.nust.seecs.oop.project.helpers.AudioFingerPrintingHelper;
import pk.edu.nust.seecs.oop.project.helpers.FileDirectoryHandler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AudioFingerPrintingController implements Initializable {
    // Attributes.
    private FileDirectoryHandler fileDirectoryHandler = new FileDirectoryHandler();
    private AudioFingerPrintingHelper audioFingerPrintingHelper = new AudioFingerPrintingHelper();

    // FXML Attributes.
    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private JFXButton addToGraph;

    @FXML
    private JFXTreeView<Object> treeView = new JFXTreeView<>();

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXButton refreshButton;

    @FXML
    private JFXSpinner spinner;

    // Methods handling different operations.

    // Method to create Tree-view.
    private void createTreeView(String path) throws IOException {
        // changing the path.substring to path
        TreeItem<Object> treeItem = new TreeItem<>(path);
        List<TreeItem<Object>> directories = new ArrayList<>();
        List<TreeItem<Object>> files = new ArrayList<>();
        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(path));
        for (Path filePath : directoryStream) {
            if (Files.isDirectory(filePath)) {
                // Changed filePath to its substring so that only name of folder is added to tree.
                TreeItem<Object> subDirectory = new TreeItem<>(filePath.toString().substring(1 + filePath.toString().lastIndexOf(File.separator)));
                getSubDirectories(filePath, subDirectory);
                directories.add(subDirectory);
            }
            else {
                files.add(getFileDirectory(filePath));
            }
        }
        treeItem.getChildren().addAll(directories);
        treeItem.getChildren().addAll(files);
        treeItem.setExpanded(true);
        treeView.setRoot(treeItem);
        treeView.setShowRoot(true);
    }

    // Method to get sub-directories
    private void getSubDirectories(Path subDirectoryPath, TreeItem<Object> parent) throws IOException {
        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(subDirectoryPath.toString()));
        for (Path subDirectory : directoryStream) {
            if (!Files.isDirectory(subDirectory)) {
                String subDirectoryTreeString = subDirectory.toString();
                TreeItem<Object> subDirectoryTree = new TreeItem<>(subDirectoryTreeString.substring(1 + subDirectoryTreeString.lastIndexOf(File.separator)));
                parent.getChildren().add(subDirectoryTree);
            }
        }
    }

    // Method to get files (address).
    private TreeItem<Object> getFileDirectory(Path filePath) {
        String filePathString = filePath.toString();
        return new TreeItem<>(filePathString.substring(1 + filePathString.lastIndexOf(File.separator)));
    }

    // Method to build address of items in tree. ***Still to be checked****
    private String buildDirectoryAddress() {
        StringBuilder pathBuilder = new StringBuilder();
        for (TreeItem<Object> item = treeView.getSelectionModel().getSelectedItem(); item != null; item = item.getParent()) {
            pathBuilder.insert(0, item.getValue().toString());
            pathBuilder.insert(0, "/");
        }

        String path = pathBuilder.toString();

        // Enforcing condition so that user can't add without selecting any audio file.
        if (!path.equals("")) {
            return pathBuilder.toString().substring(1);
        }
        else {
            return "empty";
        }
    }

    // Method to handle Add-to-Graph button on the UI. (FXML Method)
    @FXML
    private void addToGraphHandler() {
        // New code for testing out bar chart
        String pathOfFile = buildDirectoryAddress();
        if (fileDirectoryHandler.isWAVFile(pathOfFile)) {
            // New changes: comparing by using object of AudioHelper class. Adding spinner.setVisible to true
            // Adding the whole code in a non-fx thread.
            spinner.setVisible(true);
            new Thread(() -> {
                String name = buildDirectoryAddress().substring(1 + buildDirectoryAddress().lastIndexOf(File.separator));
                double similarity = audioFingerPrintingHelper.audioSimilarity(MainController.browseFieldText, pathOfFile);
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName(name);
                series.getData().add(new XYChart.Data<>("Similarity", similarity));
                Platform.runLater(() -> {
                    barChart.getData().add(series);
                    spinner.setVisible(false);
                });
            }).start();
        }
        else {
            String title = "Error";
            String message = "Please select a file of .wav format";
            fileDirectoryHandler.displayDialog(this.stackPane, title, message);
        }
    }

    // Method to handle refresh button.
    @FXML
    private void refreshButtonHandler() throws IOException {
        createTreeView(MainController.directoryPath);
    }

    // Method to handle back button press.
    @FXML
    private void backButtonPressed(ActionEvent event) throws IOException {
        MainController mainController = new MainController();
        String URL = "views/main.fxml";
        String title = "Audio Analysis";
        mainController.changeScene(URL, title, 492,285, event);
    }

    // Method that will be called when window will appear.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            createTreeView(MainController.directoryPath);
            spinner.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
