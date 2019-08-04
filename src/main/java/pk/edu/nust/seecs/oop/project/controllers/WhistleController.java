package pk.edu.nust.seecs.oop.project.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import pk.edu.nust.seecs.oop.project.helpers.FileDirectoryHandler;
import pk.edu.nust.seecs.oop.project.helpers.GraphHelper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class WhistleController implements Initializable {
    // Attributes.
    private GraphHelper graphHelper = new GraphHelper();
    private MainController mainController = new MainController();
    private FileDirectoryHandler fileDirectoryHandler = new FileDirectoryHandler();
    private String currentFilePath;

    // FXML Attributes.
    @FXML
    private StackedBarChart<String, Number> stackedBarChart;

    @FXML
    private ToggleGroup menuItems;

    @FXML
    private Label whistleLabel;

    @FXML
    private PieChart pieChart;

    @FXML
    private StackPane stackPane;

    // Methods.
    // Method that will be executed at window's appearance.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stackedBarChart.setVisible(false);
        whistleLabel.setText("");
        currentFilePath = MainController.browseFieldText;
        createPieChart(currentFilePath);
    }

    // Method for handling pie chart on the gui.
    @FXML
    private void pieChartSelected() {
        stackedBarChart.setVisible(false);
        pieChart.setVisible(true);
        createPieChart(currentFilePath);
    }

    // Method handling stacked bat chart on the gui.
    @FXML
    private void stackedBarChartSelected() {
        pieChart.setVisible(false);
        stackedBarChart.setVisible(true);
        createStackedBarChart(currentFilePath);
    }

    // Method for creating pie-chart for whistle probability.
    private void createPieChart(String pathOfFile) {
        // Preparing ObservableList object.
        double probability = graphHelper.getWhistleProbability(pathOfFile);

        if (probability != -1) {
            probability *= 100;
            double leftOut = 100 - probability;
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("Is Whistle", probability),
                    new PieChart.Data("Not a Whistle", leftOut)
            );
            DecimalFormat decimalFormat = new DecimalFormat("##.00");
            pieChart.setData(pieChartData);
            pieChart.setClockwise(true);
            pieChart.setLabelsVisible(true);
            whistleLabel.setText("Whistle Similarity: " + decimalFormat.format(probability) + "%");
        }
        else {
            String title = "Error";
            String message = "The program support only mono wave files. Make sure you have selected a " +
                    "mono wave file.";
            fileDirectoryHandler.displayDialog(stackPane, title, message);
        }
    }

    // Method for creating stacked bar chart on gui.
    private void createStackedBarChart(String pathOfFile) {
        // Resetting the stacked bar chart.
        stackedBarChart.getData().clear();
        stackedBarChart.getYAxis().setAutoRanging(false);

        // Preparing data for the stacked bar chart.
        double probability = graphHelper.getWhistleProbability(pathOfFile);

        if (probability != -1) {
            // Multiplying probability by 100.
            probability *= 100;
            double leftOut = 100 - probability;

            // Data for Is Whistle.
            XYChart.Series<String, Number> series1 = new XYChart.Series<>();
            series1.setName("Is Whistle");
            series1.getData().add(new XYChart.Data<>("Whistle Similarity", probability));

            // Data for Not a Whistle.
            XYChart.Series<String, Number> series2 = new XYChart.Series<>();
            series2.setName("Not a Whistle");
            series2.getData().add(new XYChart.Data<>("Whistle Similarity", leftOut));

            // Setting data.
            stackedBarChart.getData().add(series1);
            stackedBarChart.getData().add(series2);

            // Setting label on the GUI.
            DecimalFormat decimalFormat = new DecimalFormat("##.00");
            whistleLabel.setText("Whistle Similarity: " + decimalFormat.format(probability) + "%");
        }
        else {
            String title = "Error";
            String message = "The program support only mono wave files. Make sure you have selected a " +
                    "mono wave file.";
            fileDirectoryHandler.displayDialog(stackPane, title, message);
        }
    }

    // Method for handling the back button press on GUI.
    @FXML
    void backButtonPressed(ActionEvent event) throws IOException {
        String URL = "views/main.fxml";
        String title = "Audio Analysis";
        mainController.changeScene(URL, title, 492, 285, event);
    }

    // Method for handling the open button press.
    @FXML
    void openButtonPressed() {
        // Creating file-chooser.
        String path;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select audio file");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            path = file.getAbsolutePath();
            if (fileDirectoryHandler.isWAVFile(path)) {
                // Getting the selected menu.
                RadioMenuItem radioMenuItem = (RadioMenuItem) menuItems.getSelectedToggle();
                String selectedType = radioMenuItem.getText();

                //Setting the current path to selected one.
                currentFilePath = path;

                if (selectedType.equals("Pie Chart")) {
                    createPieChart(currentFilePath);
                }
                else {
                    createStackedBarChart(currentFilePath);
                }
            }
            else {
                String title = "Error";
                String message = "Please select a file of .wav format";
                fileDirectoryHandler.displayDialog(this.stackPane, title, message);
            }
        }
    }
}
