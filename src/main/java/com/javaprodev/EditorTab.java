package com.javaprodev;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;

class EditorTab {

    private EditorController editorController;
    private Tab tab;
    private int index;
    private ContextMenu contextMenu = new ContextMenu();
    private TabPane editorTabPane;
    private FXMLLoader fxmlLoader;

    EditorTab(int index, File file, TabPane editorTabPane) {

        this.editorTabPane = editorTabPane;
        this.index = index;

        setupFXMLLoader();
        editorController = fxmlLoader.getController();
        setupTabContextMenu();
        setupTabIndexing();

        if (file != null) {
            tab.setText(file.getName());
            editorController.initialize(file);

        } else {
            tab.setText("Untitled " + index);
            editorController.initialize(null);
        }
    }

    Tab getTab() {
        return tab;
    }

    EditorController getController() {
        return this.editorController;
    }

    private void setupFXMLLoader() {
        fxmlLoader = new FXMLLoader(this.getClass().getResource("/editor.fxml"));
        try {
            tab = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupTabContextMenu() {

        MenuItem save = new MenuItem("Save");
        save.setOnAction(event -> {
            editorController.saveFile();
        });
        MenuItem close = new MenuItem("Close");
        close.setOnAction(event -> {
            checkIfContentWasModifiedAndIfWasThenAskForSave(null);
            editorTabPane.getTabs().remove(this.index);
        });
        MenuItem closeAll = new MenuItem("Close All");
        closeAll.setOnAction((ActionEvent event) -> {
            MainController mainController = Main.fxmlLoader.getController();
            mainController.closeAll();
        });

        contextMenu.getItems().addAll(save, close, closeAll);
        tab.setContextMenu(contextMenu);
    }

    private void checkIfContentWasModifiedAndIfWasThenAskForSave(Event event) {
        if (editorController.wasTextModified()) {

            AlertsAction action;
            String fileName = editorController.getFileName();

            //checkIfThisIsUntitled
            if (fileName != null) {
                action = AlertsUtil.showSaveAlert(fileName);
            } else {
                fileName = "Untitled " + index;
                action = AlertsUtil.showSaveAlert(fileName);
            }

            processSaveDialogBox(action, event);

        }
    }

    private void processSaveDialogBox(AlertsAction action, Event event) {
        //processDialogBox
        if (action == AlertsAction.CANCEL)
            if (event != null)
                event.consume();

        if (action == AlertsAction.SAVE) {
            editorController.saveFile();
            if (event != null)
                tabCloseRequest(null);
        }

        if (action == AlertsAction.DONTSAVE)
            if (event != null)
                tabCloseRequest(null);
    }

    private void tabCloseRequest(Event event) {

        checkIfContentWasModifiedAndIfWasThenAskForSave(event);
        MainController.controllers.remove(this);
        MainController.controllers.remove(this.editorController);

    }

    private void setupTabIndexing() {

        tab.setOnSelectionChanged(event -> {
            if (this.tab.isSelected()) {
                MainController.currentlySelectedTab = index;
                //  System.out.println(MainController.currentlySelectedTab);
            }
        });

        tab.setOnCloseRequest(this::tabCloseRequest);
    }

}
