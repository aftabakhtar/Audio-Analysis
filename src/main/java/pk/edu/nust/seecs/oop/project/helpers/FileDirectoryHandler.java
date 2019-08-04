package pk.edu.nust.seecs.oop.project.helpers;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class FileDirectoryHandler {
    // Attributes.

    // Methods.
    public boolean isWAVFile(String pathOfFile) {
        if (pathOfFile.isEmpty()) {
            return false;
        }
        else {
            String extension = pathOfFile.substring(pathOfFile.length() - 4);
            return extension.equalsIgnoreCase(".wav");
        }
    }

    // Method to handle directory format error.
    public void displayDialog(StackPane stackPane, String title, String message) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.setHeading(new Text(title));
        dialogLayout.setBody(new Text(message));
        JFXDialog dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
        JFXButton button = new JFXButton("OK");
        button.setOnAction(e -> dialog.close());
        dialogLayout.setActions(button);
        dialog.show();
    }
}
