package com.flashcardapp.gui;

import com.flashcardapp.FlashcardApp;
import com.flashcardapp.model.Deck;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Controller for the main scene of the FlashcardApp.
 */
public class MainSceneController {

    @FXML
    private Button startStudyingButton;

    /**
     * Initializes the controller by setting the study button's enabled state based on the presence of a current deck.
     */
    @FXML
    public void initialize() {
        startStudyingButton.setDisable(FlashcardApp.getInstance().getCurrentDeck() == null);
    }

    /**
     * Handles creating a new deck.
     * @param actionEvent The event triggered by clicking the "Create Deck" button.
     */
    @FXML
    public void handleCreateDeck(ActionEvent actionEvent) {
        
        // Check to see if we have a deck, if not, create a new one
        if (FlashcardApp.getInstance().getCurrentDeck() == null) {
            FlashcardApp.getInstance().setCurrentDeck(new Deck("New Deck"));
        }
        
        FlashcardApp.getInstance().setFXMLScene("DeckCreationScene");
    }

    /**
     * Handles starting the studying session by loading the algorithm selection popup.
     * @param actionEvent The event triggered by clicking the "Start Studying" button.
     */
    @FXML
    public void handleStartStudying(ActionEvent actionEvent) {
        loadPopupScene("AlgorithmSelectionPopup.fxml", "Select Algorithm");
    }

    /**
     * Handles the action to switch to the settings scene.
     * @param actionEvent The event triggered by clicking the "Settings" button.
     */
    @FXML
    public void handleSettings(ActionEvent actionEvent) {
        FlashcardApp.getInstance().setFXMLScene("SettingsScene");
    }

    /**
     * Handles importing a deck from a file.
     * @param actionEvent The event triggered by clicking the "Import Deck" button.
     */
    @FXML
    public void handleImportDeck(ActionEvent actionEvent) {
        File file = chooseFile("Import Study Deck", "JSON Files", "*.json");
        if (file != null) {
            FlashcardApp.getInstance().loadDeck(file.getAbsolutePath());
            startStudyingButton.setDisable(false);
        }
    }

    /**
     * Handles exporting the current deck to a file.
     * @param actionEvent The event triggered by clicking the "Export Deck" button.
     */
    @FXML
    public void handleExportDeck(ActionEvent actionEvent) {
        File file = chooseFileToSave("Export Study Deck", "JSON Files", "*.json");
        if (file != null) {
            FlashcardApp.getInstance().saveDeck(FlashcardApp.getInstance().getCurrentDeck(), file.getAbsolutePath());
        }
    }

    /**
     * Utility method to load a FXML scene as a modal popup.
     * @param fxmlPath The path to the FXML file.
     * @param title The title of the popup stage.
     */
    private void loadPopupScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println("Error loading the popup scene: " + e.getMessage());
        }
    }

    /**
     * Opens a file chooser to select a file for importing.
     * @param title The title of the file chooser window.
     * @param extensionDescription The description of the allowed file types.
     * @param extensionPattern The file extension pattern.
     * @return The selected file, or null if no file is selected.
     */
    private File chooseFile(String title, String extensionDescription, String extensionPattern) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(extensionDescription, extensionPattern);
        fileChooser.getExtensionFilters().add(extFilter);
        
        return fileChooser.showOpenDialog(FlashcardApp.getInstance().getPrimaryStage());
    }

    /**
     * Opens a file chooser to select a file for saving.
     * @param title The title of the file chooser window.
     * @param extensionDescription The description of the allowed file types.
     * @param extensionPattern The file extension pattern.
     * @return The file selected for saving, or null if no file is selected.
     */
    private File chooseFileToSave(String title, String extensionDescription, String extensionPattern) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(extensionDescription, extensionPattern);
        fileChooser.getExtensionFilters().add(extFilter);
        
        return fileChooser.showSaveDialog(FlashcardApp.getInstance().getPrimaryStage());
    }
}