package com.javaprodev;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

class AlertsUtil {

    static AlertsAction showSaveAlert(String filename) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Notepad");
        alert.setHeaderText("Do you want to save changes to " + filename);

        ButtonType buttonTypeOne = new ButtonType("Save");
        ButtonType buttonTypeTwo = new ButtonType("Don't Save");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {
            return AlertsAction.SAVE;
        } else if (result.get() == buttonTypeTwo) {
            return AlertsAction.DONTSAVE;
        } else {
            return AlertsAction.CANCEL;
        }
    }

    static AlertsAction showSaveAlert2(String filename) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Notepad");
        alert.setHeaderText("Do you really want to save " + filename);

        ButtonType buttonTypeOne = new ButtonType("Save");
        ButtonType buttonTypeTwo = new ButtonType("Don't Save");

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {
            return AlertsAction.SAVE;
        } else {
            return AlertsAction.DONTSAVE;
        }
    }

    static void showFileIsAlreadyOpened() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notepad");
        alert.setHeaderText("File is already open");

        ButtonType buttonTypeOne = new ButtonType("Ok");

        alert.getButtonTypes().setAll(buttonTypeOne);

        Optional<ButtonType> result = alert.showAndWait();
    }
}
