package com.flashcardapp.gui;

import com.flashcardapp.FlashcardApp;
import com.flashcardapp.logic.BasicRotationalAlgorithm;
import com.flashcardapp.logic.IAlgorithm;
import com.flashcardapp.model.Deck;
import com.flashcardapp.model.Flashcard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class StudySessionSceneController {

    @FXML
    private Text answerText;

    @FXML
    private Text questionText;

    @FXML
    private FlowPane tagsFlowPane;

    @FXML
    private Text hintText;

    private Deck currentDeck;
    private int currentFlashcardIndex = 0;

    private IAlgorithm studyAlgorithm;

    @FXML
    public void initialize() {
        answerText.setVisible(false);
        //TODO: studyAlgorithm = FlashcardApp.getInstance().getStudyAlgorithm();

        currentDeck = FlashcardApp.getInstance().getCurrentDeck();

        studyAlgorithm = new BasicRotationalAlgorithm();
        studyAlgorithm.initialize(currentDeck.getFlashcards());

        // Show the first flashcard
        showFlashcard(0);
    }

    private void showFlashcard(int index) {
        if (index >= 0 && index < currentDeck.getFlashcardCount()) {
            answerText.setVisible(false);
            questionText.setText(currentDeck.getFlashcard(index).getQuestion());
            answerText.setText(currentDeck.getFlashcard(index).getAnswer());
            currentFlashcardIndex = index;
            updateFlashcardDisplay(currentDeck.getFlashcard(index));
        }
    }

    public void handleBack(ActionEvent actionEvent) {
        FlashcardApp.getInstance().setFXMLScene("MainScene");
    }

    public void handleRestartSession(ActionEvent actionEvent) {
        // Show the first flashcard
        showFlashcard(0);
    }

    public void handleEndSession(ActionEvent actionEvent) {
        // Go back to the main scene
        FlashcardApp.getInstance().setFXMLScene("MainScene");
    }

    public void handlePrevious(ActionEvent actionEvent) {
        showFlashcard(studyAlgorithm.getPreviousFlashcardIndex());
    }

    public void handleNext(ActionEvent actionEvent) {
        showFlashcard(studyAlgorithm.getNextFlashcardIndex() + 1);
    }

    @FXML
    private void handleShowAnswer(ActionEvent event) {
        answerText.setVisible(!answerText.isVisible());
    }

    public void handleCorrect(ActionEvent actionEvent) {
        studyAlgorithm.recordResponse(currentDeck.getFlashcard(currentFlashcardIndex), true);
    }

    public void handleIncorrect(ActionEvent actionEvent) {
        studyAlgorithm.recordResponse(currentDeck.getFlashcard(currentFlashcardIndex), false);
    }

    public void updateFlashcardDisplay(Flashcard flashcard) {
        questionText.setText(flashcard.getQuestion());
        answerText.setText(flashcard.getAnswer());
        answerText.setVisible(false); // Hide the answer initially

        // Check if a hint is available
        if (flashcard.getHint() != null && !flashcard.getHint().isEmpty()) {
            hintText.setText("Click to reveal hint");
            hintText.setOnMouseClicked(event -> {
                hintText.setText(flashcard.getHint());
            });
        } else {
            hintText.setText("No hint available");
            hintText.setOnMouseClicked(null); // Remove click action if no hint
        }
    }
}
