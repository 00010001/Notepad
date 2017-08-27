package com.javaprodev;

import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

class FileChooserUtil {

    private FileChooser fileChooser;

    FileChooserUtil() {
        this.fileChooser = new FileChooser();
        fileChooser = new javafx.stage.FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new javafx.stage.FileChooser.ExtensionFilter("All Files", "*.*"),
                new javafx.stage.FileChooser.ExtensionFilter("TXT", "*.txt"));
    }

    File open() {
        fileChooser.setTitle("Open File");
        return fileChooser.showOpenDialog(null);
    }
    List<File> openMultiple() {
        fileChooser.setTitle("Open Files");
        return fileChooser.showOpenMultipleDialog(null);
    }
    File save() {
        fileChooser.setTitle("Save File");
        return fileChooser.showSaveDialog(null);
    }

}
