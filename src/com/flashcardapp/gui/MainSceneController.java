package com.flashcardapp.gui;

import com.flashcardapp.FlashcardApp;
import javafx.event.ActionEvent;

public class MainSceneController {
    public void handleCreateDeck(ActionEvent actionEvent) {
        FlashcardApp.getInstance().setFXMLScene("DeckCreationScene");
    }

    public void handleStartStudying(ActionEvent actionEvent) {
        FlashcardApp.getInstance().setFXMLScene("StudySessionScene");
    }

    public void handleSettings(ActionEvent actionEvent) {
        FlashcardApp.getInstance().setFXMLScene("SettingsScene");
    }
}
