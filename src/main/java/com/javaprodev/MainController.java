package com.javaprodev;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.stage.Stage;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainController {

    @FXML
    private MenuItem fileNew;
    @FXML
    private MenuItem fileOpen;
    @FXML
    private MenuItem fileSave;
    @FXML
    private MenuItem fileSaveAs;
    @FXML
    private MenuItem fileClose;
    @FXML
    private Menu editMenu;
    @FXML
    private MenuItem editCopy;
    @FXML
    private MenuItem editCut;
    @FXML
    private MenuItem editPaste;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button fileNewButton;
    @FXML
    private Button fileOpenButton;
    @FXML
    private Button fileSaveButton;
    @FXML
    private Button fileSaveAsButton;
    @FXML
    private Button fileSaveAllButton;
    @FXML
    private Button editCopyButton;
    @FXML
    private Button editCutButton;
    @FXML
    private Button editPasteButton;
    @FXML
    private Button undoButton;
    @FXML
    private Button redoButton;

    private Stage primaryStage;
    private Clipboard systemClipboard = Clipboard.getSystemClipboard();

    static FileChooserUtil fileChooserUtil = new FileChooserUtil();
    static List<EditorController> controllers = new ArrayList<>();
    static int currentlySelectedTab = 0;

    void initialize(Stage primaryStage) {

        initPrimaryStage(primaryStage);
        initTabPane();
        initMenuItems();
        initButtons();
        initManagerOfAvailableControls();

        createNewTab(null);
    }

    private void initMenuItems() {
        fileNew.setOnAction(event -> newFile());
        fileOpen.setOnAction(event -> openFile());
        fileSave.setOnAction(event -> saveFile());
        fileSaveAs.setOnAction(event -> saveAs());
        fileClose.setOnAction(event -> exit());
        editCopy.setOnAction(event -> copy());
        editCut.setOnAction(event -> cut());
        editPaste.setOnAction(event -> paste());
    }

    private void initButtons() {
        Image fileNewImage = new Image("/icons/document_new.png");
        fileNewButton.setGraphic(new ImageView(fileNewImage));
        fileNewButton.setOnAction((ActionEvent event) -> newFile());

        Image fileOpenImage = new Image("/icons/document_open.png");
        fileOpenButton.setGraphic(new ImageView(fileOpenImage));
        fileOpenButton.setOnAction((ActionEvent event) -> openFile());

        Image fileSaveImage = new Image("/icons/document_save.png");
        fileSaveButton.setGraphic(new ImageView(fileSaveImage));
        fileSaveButton.setOnAction((ActionEvent event) -> {
            if (controllers.size() != 0) {
                saveFile();
            }
        });

        Image fileSaveAsImage = new Image("/icons/document_save_as.png");
        fileSaveAsButton.setGraphic(new ImageView(fileSaveAsImage));
        fileSaveAsButton.setOnAction((ActionEvent event) -> {
            if (controllers.size() != 0) {
                saveAs();
            }
        });

        Image fileSaveAllImage = new Image("/icons/document_save_all.png");
        fileSaveAllButton.setGraphic(new ImageView(fileSaveAllImage));
        fileSaveAllButton.setOnAction((ActionEvent event) -> {
            if (controllers.size() > 0) {
                saveAll();
            }
        });

        Image editCopyImage = new Image("/icons/editcopy.png");
        editCopyButton.setGraphic(new ImageView(editCopyImage));
        editCopyButton.setOnAction((ActionEvent event) -> {
            if (controllers.size() != 0) {
                copy();
            }
        });
        Image editPasteImage = new Image("/icons/editpaste.png");
        editPasteButton.setGraphic(new ImageView(editPasteImage));
        editPasteButton.setOnAction((ActionEvent event) -> {
            if (controllers.size() != 0) {
                paste();
            }
        });
        Image editCutImage = new Image("/icons/editcut.png");
        editCutButton.setGraphic(new ImageView(editCutImage));
        editCutButton.setOnAction((ActionEvent event) -> {
            if (controllers.size() != 0) {
                cut();
            }
        });

        Image editRedo = new Image("/icons/edit_redo.png");
        redoButton.setGraphic(new ImageView(editRedo));
        redoButton.setOnAction((ActionEvent event) -> {
            if (controllers.size() != 0) {
                redo();
            }
        });

        Image editUndo = new Image("/icons/edit_undo.png");
        undoButton.setGraphic(new ImageView(editUndo));
        undoButton.setOnAction((ActionEvent event) -> {
            if (controllers.size() != 0) {
                undo();
            }
        });
    }

    private void copy() {
        String text = controllers.get(currentlySelectedTab).getEditorTextArea().getSelectedText();
        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        systemClipboard.setContent(content);
    }

    private void cut() {
        TextArea textArea = controllers.get(currentlySelectedTab).getEditorTextArea();

        String text = textArea.getSelectedText();

        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        systemClipboard.setContent(content);

        IndexRange range = textArea.getSelection();
        String origText = textArea.getText();
        String firstPart = StringUtils.substring(origText, 0, range.getStart());
        String lastPart = StringUtils.substring(origText, range.getEnd(), StringUtils.length(origText));
        textArea.setText(firstPart + lastPart);

        textArea.positionCaret(range.getStart());
    }

    private void paste() {
        if (!systemClipboard.hasContent(DataFormat.PLAIN_TEXT)) {
            return;
        }

        String clipboardText = systemClipboard.getString();

        TextArea textArea = controllers.get(currentlySelectedTab).getEditorTextArea();
        IndexRange range = textArea.getSelection();

        String origText = textArea.getText();

        int endPos;
        String updatedText;
        String firstPart = StringUtils.substring(origText, 0, range.getStart());
        String lastPart = StringUtils.substring(origText, range.getEnd(), StringUtils.length(origText));

        updatedText = firstPart + clipboardText + lastPart;

        if (range.getStart() == range.getEnd()) {
            endPos = range.getEnd() + StringUtils.length(clipboardText);
        } else {
            endPos = range.getStart() + StringUtils.length(clipboardText);
        }

        textArea.setText(updatedText);
        textArea.positionCaret(endPos);
    }

    private void newFile() {
        createNewTab(null);
    }

    private void openFile() {
        List<File> files = fileChooserUtil.openMultiple();
        if (files != null)
            for (File file : files) {
                if (file != null)
                    if (!fileIsAlreadyOpen(file.getPath())) {
                        createNewTab(file);
                    } else {
                      AlertsUtil.showFileIsAlreadyOpened();
                  }
            }
    }

    private boolean fileIsAlreadyOpen(String filePath) {
        for (EditorController controller : controllers) {
            if (controller.getFilePath().equals(filePath)) {
                return true;

            }
        }
        return false;
    }

    private void saveFile() {
        controllers.get(tabPane.getSelectionModel().getSelectedIndex()).saveFile();
    }

    private void saveAs() {
        File file = fileChooserUtil.save();
        controllers.get(tabPane.getSelectionModel().getSelectedIndex()).saveAsFile(file);
    }

    private void saveAll() {
        for (EditorController controller : controllers) {
            if (controller.wasTextModified()) {
                if (controller.getFileName() == null) {
                    AlertsAction action = AlertsUtil.showSaveAlert2(controller.getTab().getText());
                    if (action == AlertsAction.SAVE) {
                        saveFile();
                    }
                } else {
                    controller.saveFile();
                }
            }
        }


    }

    void closeAll() {
        System.out.println("sdds");
        saveAll();
        tabPane.getTabs().removeAll(tabPane.getTabs());
        controllers = new ArrayList<>();
    }

    private void exit() {
        primaryStage.close();
    }

    private void undo() {
        controllers.get(tabPane.getSelectionModel().getSelectedIndex()).getEditorTextArea().undo();
    }

    private void redo() {
        controllers.get(tabPane.getSelectionModel().getSelectedIndex()).getEditorTextArea().redo();
    }

    private void initPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setOnCloseRequest(event -> {
            //TODO check for opened unsaved TABS
        });
    }

    private void initTabPane() {
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
    }

    private void createNewTab(File file) {

        int index = controllers.size();

        EditorTab newEditorTab = new EditorTab(index, file, tabPane);
        tabPane.getTabs().add(newEditorTab.getTab());
        tabPane.getSelectionModel().selectLast();

        controllers.add(newEditorTab.getController());
    }

    private void initManagerOfAvailableControls() {
        tabPane.getTabs().addListener((ListChangeListener<Tab>) c -> {
            int tabsNumber = tabPane.getTabs().size();
            switch (tabsNumber) {
                case 0:
                    fileSave.setDisable(true);
                    fileSaveAs.setDisable(true);

                    editMenu.setDisable(true);

                    fileSaveButton.setDisable(true);
                    fileSaveAsButton.setDisable(true);
                    fileSaveAllButton.setDisable(true);

                    undoButton.setDisable(true);
                    redoButton.setDisable(true);

                    editCopyButton.setDisable(true);
                    editCutButton.setDisable(true);
                    editPasteButton.setDisable(true);

                    break;
                case 1:
                    fileSave.setDisable(false);
                    fileSaveAs.setDisable(false);

                    editMenu.setDisable(false);

                    fileSaveButton.setDisable(false);
                    fileSaveAsButton.setDisable(false);
                    fileSaveAllButton.setDisable(true);

                    undoButton.setDisable(false);
                    redoButton.setDisable(false);

                    editCopyButton.setDisable(false);
                    editCutButton.setDisable(false);
                    editPasteButton.setDisable(false);
                    break;
                default:
                    fileSave.setDisable(false);
                    fileSaveAs.setDisable(false);

                    editMenu.setDisable(false);

                    fileSaveButton.setDisable(false);
                    fileSaveAsButton.setDisable(false);
                    fileSaveAllButton.setDisable(false);

                    undoButton.setDisable(false);
                    redoButton.setDisable(false);

                    editCopyButton.setDisable(false);
                    editCutButton.setDisable(false);
                    editPasteButton.setDisable(false);
                    break;
            }
        });


    }
}