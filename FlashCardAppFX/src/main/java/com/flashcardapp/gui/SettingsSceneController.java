package com.flashcardapp.gui;

import com.flashcardapp.FlashcardApp;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;

public class SettingsSceneController {
    public void handleBack(ActionEvent actionEvent) {
        FlashcardApp.getInstance().setFXMLScene("MainScene");
    }

    public void handleThemeChange(ActionEvent actionEvent) {
    }

    public void handleAbout(ActionEvent actionEvent) {
        // Show an alert dialog with information about the app
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Flashcard Study Helper");
        alert.setHeaderText("Flashcard Study Helper");
        alert.setContentText("This application is a simple flashcard study helper that allows you to create, edit, and study flashcards.\n\n" +
                "Version: 1.0\n" +
                "Authors: Carson Kelley, Andrew Abdelaty, Brayden Kielb, Spencer Morse, and Vic Westmoreland\n" +
                "License: MIT License\n\n" +
                "This application was created as a final project for the CPS 240 course at Central Michigan University.");

        alert.showAndWait();
    }

    public void handleImportData(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Data");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON Files", "*.json"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        fileChooser.setInitialDirectory(FlashcardApp.getInstance().getDataDirectory());
        File file = fileChooser.showOpenDialog(FlashcardApp.getInstance().getPrimaryStage());

        if (file != null) {
            FlashcardApp.getInstance().loadDeck(file.getAbsolutePath());
        }
    }

    public void handleExportData(ActionEvent actionEvent) {
        // open file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Data");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON Files", "*.json"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        fileChooser.setInitialDirectory(FlashcardApp.getInstance().getDataDirectory());
        File file = fileChooser.showSaveDialog(FlashcardApp.getInstance().getPrimaryStage());

        // save the deck to the file
        if (file != null) {
            FlashcardApp.getInstance().saveDeck(FlashcardApp.getInstance().getCurrentDeck(), file.getAbsolutePath());
        }

    }

    public void handleResetData(ActionEvent actionEvent) {
    }
}
