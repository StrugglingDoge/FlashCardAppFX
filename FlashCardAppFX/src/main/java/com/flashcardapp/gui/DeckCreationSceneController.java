package com.flashcardapp.gui;

import com.flashcardapp.FlashcardApp;
import com.flashcardapp.model.Flashcard;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class DeckCreationSceneController {
    @FXML
    private TextField questionTextField;
    @FXML
    private TextArea answerTextArea;
    @FXML
    private VBox flashcardsListVBox; // Where flashcard summaries will be displayed
    
    @FXML
    private Label errorMessageLabel;

    @FXML
    private Button addFlashcardButton;
    @FXML
    private TextField hintTextField;

    @FXML
    private VBox deckCreationVBox;

    private Map<VBox, Flashcard> uiToFlashcardMap = new HashMap<>();
    private Flashcard currentlyEditing = null;


    @FXML
    public void initialize() {
        // Load the current deck's flashcards
        FlashcardApp.getInstance().getCurrentDeck().getFlashcards().forEach(this::updateFlashcardsListUI);
        questionTextField.maxWidthProperty().bind(deckCreationVBox.widthProperty().subtract(20));
        answerTextArea.maxWidthProperty().bind(deckCreationVBox.widthProperty().subtract(20));
        hintTextField.maxWidthProperty().bind(deckCreationVBox.widthProperty().subtract(20));
        questionTextField.minHeightProperty().bind(deckCreationVBox.heightProperty().divide(20));
        answerTextArea.minHeightProperty().bind(deckCreationVBox.heightProperty().divide(14));
        hintTextField.minHeightProperty().bind(deckCreationVBox.heightProperty().divide(20));
    }

    @FXML
    private void handleAddFlashcard(ActionEvent event) {
        String question = questionTextField.getText().trim();
        String answer = answerTextArea.getText().trim();
        String hint = hintTextField.getText().trim(); // Get the hint text

        if (!question.isEmpty() && !answer.isEmpty()) {
            if (currentlyEditing != null) {
                // Update existing flashcard
                currentlyEditing.setQuestion(question);
                currentlyEditing.setAnswer(answer);
                currentlyEditing.setHint(hint.isEmpty() ? null : hint); // Update hint; set to null if empty

                refreshFlashcardsListUI();
            } else {
                // Add new flashcard
                Flashcard flashcard = new Flashcard(question, answer);
                flashcard.setHint(hint.isEmpty() ? null : hint); // Set hint; set to null if empty
                FlashcardApp.getInstance().getCurrentDeck().addFlashcard(flashcard);
                updateFlashcardsListUI(flashcard);
            }

            questionTextField.clear();
            answerTextArea.clear();
            hintTextField.clear(); // Clear the hint input field
            currentlyEditing = null;
            addFlashcardButton.setText("Add Flashcard");
        } else {
            showErrorMessage("Both question and answer must be filled.");
        }
    }

    private void updateFlashcardsListUI(Flashcard flashcard) {
        // create a box to hold the flashcard
        VBox flashcardBox = new VBox();
        flashcardBox.getStyleClass().add("flashcard-summary");

        // create a text node to display the question
        Text questionText = new Text(flashcard.getQuestion());
        questionText.getStyleClass().add("flashcard-question");

        // create a text node to display the answer
        Text answerText = new Text(flashcard.getAnswer());
        answerText.getStyleClass().add("flashcard-answer");

        // Handle hint display
        Text hintText = null;
        if (flashcard.getHint() != null && !flashcard.getHint().isEmpty()) {
            Text t = new Text("Hint: " + flashcard.getHint());
            t.getStyleClass().add("flashcard-hint");
            hintText = t;
        }

        flashcardBox.getChildren().addAll(questionText, answerText);
        if (hintText != null) {
            flashcardBox.getChildren().add(hintText);
        }

        // add the box to the list of flashcards
        flashcardsListVBox.getChildren().add(flashcardBox);

        // when clicked, add to textfield and textarea
        flashcardBox.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                questionTextField.setText(flashcard.getQuestion());
                answerTextArea.setText(flashcard.getAnswer());
                hintTextField.setText(flashcard.getHint() != null ? flashcard.getHint() : ""); // Populate the hint field
                currentlyEditing = flashcard;
                addFlashcardButton.setText("Update Flashcard");
            } else if (event.getButton() == MouseButton.SECONDARY) {
                // Right click: Show options for Duplicate and Remove
                showContextMenu(flashcard, flashcardBox, event.getScreenX(), event.getScreenY());
            }
        });

        // add the box to the map
        uiToFlashcardMap.put(flashcardBox, flashcard);
    }

    public void handleBack(ActionEvent actionEvent) {
        FlashcardApp.getInstance().setFXMLScene("MainScene");
    }

    public void handleClearAll(ActionEvent actionEvent) {
    }

    public void handleSaveDeck(ActionEvent actionEvent) {
    }

    private void showErrorMessage(String message) {
        errorMessageLabel.setText(message);
        errorMessageLabel.setVisible(true);

        // Create a PauseTransition with a duration of 3 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            errorMessageLabel.setVisible(false); // Hide the label after the pause
        });
        pause.play(); // Start the pause
    }

    private void duplicateFlashcard(Flashcard flashcard) {
        questionTextField.setText(flashcard.getQuestion());
        answerTextArea.setText(flashcard.getAnswer());
        addFlashcardButton.setText("Add Flashcard");
        // Don't enter modify state, as this is a duplication
    }

    private void removeFlashcard(Flashcard flashcard, VBox flashcardBox) {
        FlashcardApp.getInstance().getCurrentDeck().removeFlashcard(flashcard);
        flashcardsListVBox.getChildren().remove(flashcardBox);
        uiToFlashcardMap.remove(flashcardBox);
        // Optional: add logic to persist the removal if necessary
    }

    private void refreshFlashcardsListUI() {
        flashcardsListVBox.getChildren().clear();
        uiToFlashcardMap.clear();
        FlashcardApp.getInstance().getCurrentDeck().getFlashcards().forEach(this::updateFlashcardsListUI);
    }

    private void showContextMenu(Flashcard flashcard, VBox flashcardBox, double x, double y) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem duplicateItem = new MenuItem("Duplicate");
        duplicateItem.setOnAction(e -> duplicateFlashcard(flashcard));

        MenuItem removeItem = new MenuItem("Remove");
        removeItem.setOnAction(e -> removeFlashcard(flashcard, flashcardBox));

        contextMenu.getItems().addAll(duplicateItem, removeItem);

        contextMenu.show(flashcardBox.getScene().getWindow(), x, y);
    }
}
