package com.flashcardapp.gui;

import com.flashcardapp.FlashcardApp;
import com.flashcardapp.util.ConfigHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;

/**
 * Controller for handling settings-related interactions in the FlashcardApp.
 */
public class SettingsSceneController {

    @FXML
    private ChoiceBox<String> themeChoiceBox;
    
    /**
     * Initializes the settings scene with the current theme choice.
     */
    @FXML
    public void initialize() {
        themeChoiceBox.getItems().addAll("light", "dark");
        String currentTheme = ConfigHandler.getInstance().getOption("theme", String.class);
        themeChoiceBox.setValue(currentTheme);
    }

    /**
     * Handles navigation back to the main scene.
     */
    @FXML
    public void handleBack(ActionEvent actionEvent) {
        FlashcardApp.getInstance().setFXMLScene("MainScene");
    }

    /**
     * Handles the theme change operation.
     */
    @FXML
    public void handleThemeChange(ActionEvent actionEvent) {
        String selectedTheme = themeChoiceBox.getValue().toLowerCase();
        FlashcardApp.getInstance().setTheme(selectedTheme);
        ConfigHandler.getInstance().saveOption("theme", selectedTheme);
    }

    /**
     * Displays an informational dialog about the application.
     */
    @FXML
    public void handleAbout(ActionEvent actionEvent) {
        Alert alert = createAboutAlert();
        alert.showAndWait();
    }

    /**
     * Handles data reset with confirmation.
     */
    @FXML
    public void handleResetData(ActionEvent actionEvent) {
        if (confirmAction("Reset Data", "Are you sure you want to reset all data?",
                "This will delete all decks and flashcards. This action cannot be undone.")) {
            FlashcardApp.getInstance().resetData();
            ConfigHandler.getInstance().saveOption("theme", "light"); // Resets theme to default
        }
    }

    /**
     * Creates an alert dialog for the 'About' section with app details.
     * @return Configured Alert.
     */
    private Alert createAboutAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(FlashcardApp.getInstance().getPrimaryStage());
        // update style
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/com/flashcardapp/gui/" + FlashcardApp.getInstance().getTheme()).toExternalForm());
        alert.setTitle("About Flashcard Study Helper");
        alert.setHeaderText("Flashcard Study Helper");
        alert.setContentText("This application is a simple flashcard study helper that allows you to " +
                "create, edit, and study flashcards.\n\nVersion: 1.0\n" +
                "Authors: Carson Kelley, Andrew Abdelaty, Brayden Kielb, Spencer Morse, " +
                "and Vic Westmoreland\nLicense: MIT License\n\n" +
                "Icon by Freepik from www.flaticon.com\nhttps://www.flaticon.com/authors/freepik\n\n" +
                "Created as a final project for CPS 240 at Central Michigan University.");
        return alert;
    }

    /**
     * Displays a confirmation dialog and returns the result.
     * @param title Title of the dialog.
     * @param header Header text for the dialog.
     * @param content Detailed content/message of the dialog.
     * @return true if the user confirmed, false otherwise.
     */
    private boolean confirmAction(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }
}