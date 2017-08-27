package com.javaprodev;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;

import java.io.*;

public class EditorController {

    @FXML
    private Tab tab;
    @FXML
    private TextArea editorTextArea;

    private File file;
    private boolean wasTextModified = false;

    void initialize(File file) {
        this.file = file;
        if (file != null) {
            readTextFromFile();
        }
        makeTextAreaListenForChange();
    }

    Tab getTab() {
        return tab;
    }

    public String getFilePath() {
        if(file == null){
            return "";
        } else {
            return file.getPath();
        }
    }

    String getFileName(){
        if(file != null){
            return file.getName();
        }
        return null;
    }

    boolean wasTextModified() {
        return wasTextModified;
    }

    TextArea getEditorTextArea() {
        return editorTextArea;
    }

    void saveFile() {
        System.out.println(this.file);
        if (this.file != null) {
            writeTextTo(this.file);
        } else {
            saveAsFile(MainController.fileChooserUtil.save());
        }
    }

    void saveAsFile(File userSelectedFile) {
        if(userSelectedFile != null){
            this.file = userSelectedFile;
            writeTextTo(userSelectedFile);
        }
    }

    private void writeTextTo(File fileToSave) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileOutputStream(fileToSave));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (CharSequence charSequence : editorTextArea.getParagraphs()) {
            pw.println(charSequence);
        }
        pw.close();
        this.wasTextModified = false;
        tab.setText(fileToSave.getName());
    }

    private void readTextFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;
            while ((line = reader.readLine()) != null) {
                editorTextArea.appendText(line + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeTextAreaListenForChange() {
        editorTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!wasTextModified) {
                wasTextModified = true;
                tab.setText(tab.getText() + "*");
            }
        });
    }
}
