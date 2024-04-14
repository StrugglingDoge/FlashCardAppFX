package com.flashcardapp.gui;

import com.flashcardapp.FlashcardApp;
import com.flashcardapp.logic.IAlgorithm;
import com.flashcardapp.model.Deck;
import com.flashcardapp.model.Flashcard;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Window;

/**
 * Controller for managing the study session UI and interactions in a Flashcard application.
 */
public class StudySessionSceneController {

    @FXML private Text answerText;
    @FXML private Text questionText;
    @FXML private Text sessionStatsText;
    @FXML private Text hintText;
    @FXML private ImageView questionImageView;
    @FXML private ImageView answerImageView;
    @FXML private VBox flashcardSection;
    @FXML private Button previousButton;
    @FXML private Button nextButton;
    @FXML private Button correctButton;
    @FXML private Button incorrectButton;

    private Deck currentDeck;
    private IAlgorithm studyAlgorithm;
    private boolean wasLastOfCycle = false;
    private int countForCycle = 0;

    /**
     * Initializes the controller. This method sets up the initial state of the UI components,
     * binds their properties, and loads the first flashcard.
     */
    @FXML
    public void initialize() {
        hideAnswer();
        loadDeckAndAlgorithm();
        bindUIComponents();
        showFlashcard();
    }

    /**
     * Loads deck and study algorithm from the application instance.
     */
    private void loadDeckAndAlgorithm() {
        currentDeck = FlashcardApp.getInstance().getCurrentDeck();
        studyAlgorithm = FlashcardApp.getInstance().getStudyAlgorithm();
        studyAlgorithm.initialize(currentDeck.getFlashcards());
    }

    /**
     * Binds the size properties of the UI components to the primary stage's properties.
     */
    private void bindUIComponents() {
        Window primaryStage = FlashcardApp.getInstance().getPrimaryStage();
        if (primaryStage != null) {
            double textScaleFactor = 50;
            double imageFactor = 1.5;
            double wrappingWidthFactor = 2.5;

            bindSizeProperties(primaryStage, textScaleFactor, imageFactor);
            bindTextWrapping(wrappingWidthFactor);
        }
    }

    /**
     * Bind size properties to UI components based on primary stage dimensions.
     * @param primaryStage The primary window of the application.
     * @param textScaleFactor The scale factor for text size relative to component width.
     * @param imageFactor The scale factor for image fit dimensions relative to component size.
     */
    private void bindSizeProperties(Window primaryStage, double textScaleFactor, double imageFactor) {
        flashcardSection.prefWidthProperty().bind(primaryStage.widthProperty().divide(2));
        flashcardSection.prefHeightProperty().bind(primaryStage.heightProperty().divide(3));

        questionImageView.fitWidthProperty().bind(flashcardSection.prefWidthProperty().divide(imageFactor));
        questionImageView.fitHeightProperty().bind(flashcardSection.prefHeightProperty().divide(imageFactor));
        answerImageView.fitWidthProperty().bind(flashcardSection.prefWidthProperty().divide(imageFactor));
        answerImageView.fitHeightProperty().bind(flashcardSection.prefHeightProperty().divide(imageFactor));

        questionText.styleProperty().bind(Bindings.concat("-fx-font-size: ", flashcardSection.prefWidthProperty().divide(textScaleFactor), ";"));
        answerText.styleProperty().bind(Bindings.concat("-fx-font-size: ", flashcardSection.prefWidthProperty().divide(textScaleFactor), ";"));
        hintText.styleProperty().bind(Bindings.concat("-fx-font-size: ", flashcardSection.prefWidthProperty().divide(textScaleFactor), ";"));
        sessionStatsText.styleProperty().bind(Bindings.concat("-fx-font-size: ", flashcardSection.prefWidthProperty().divide(textScaleFactor), ";"));
    }

    /**
     * Bind text wrapping properties based on available width.
     * @param wrappingWidthFactor The factor to subtract from available width for wrapping.
     */
    private void bindTextWrapping(double wrappingWidthFactor) {
        questionText.wrappingWidthProperty().bind(flashcardSection.prefWidthProperty().subtract(flashcardSection.prefWidthProperty().divide(wrappingWidthFactor)));
        answerText.wrappingWidthProperty().bind(flashcardSection.prefWidthProperty().subtract(flashcardSection.prefWidthProperty().divide(wrappingWidthFactor)));
        hintText.wrappingWidthProperty().bind(flashcardSection.prefWidthProperty().subtract(flashcardSection.prefWidthProperty().divide(wrappingWidthFactor)));
        sessionStatsText.wrappingWidthProperty().bind(flashcardSection.prefWidthProperty().subtract(flashcardSection.prefWidthProperty().divide(wrappingWidthFactor)));
    }

    /**
     * Hides the answer text and image.
     */
    private void hideAnswer() {
        answerText.setVisible(false);
        answerImageView.setVisible(false);
    }
    
    /**
     * Handles the action to return to the main scene.
     * @param actionEvent The event that triggered this action.
     */
    @FXML
    public void handleBack(ActionEvent actionEvent) {
        FlashcardApp.getInstance().setFXMLScene("MainScene");
    }

    /**
     * Displays the next flashcard in the deck, if available.
     * @param actionEvent The event that triggered this action.
     */
    @FXML
    public void handleNext(ActionEvent actionEvent) {
        if (wasLastOfCycle) {
            previousButton.setDisable(true);
            wasLastOfCycle = false;
        }
        if (studyAlgorithm.moveToNext()) {
            showFlashcard();
        }
        countForCycle++;
    }

    /**
     * Displays the previous flashcard in the deck, if available.
     * @param actionEvent The event that triggered this action.
     */
    @FXML
    public void handlePrevious(ActionEvent actionEvent) {
        if (studyAlgorithm.moveToPrevious()) {
            showFlashcard();
        }
        countForCycle--;
    }

    /**
     * Toggles the visibility of the answer for the current flashcard.
     * @param event The event that triggered this action.
     */
    @FXML
    private void handleShowAnswer(ActionEvent event) {
        boolean isVisible = !answerText.isVisible();
        answerText.setVisible(isVisible);
        if (answerImageView.getImage() != null) {
            answerImageView.setVisible(isVisible && !answerImageView.getImage().getUrl().isEmpty());
        }
    }

    /**
     * Processes the user's response indicating they recalled the flashcard correctly.
     * @param actionEvent The event that triggered this action.
     */
    @FXML
    public void handleCorrect(ActionEvent actionEvent) {
        processResponse(true);
    }

    /**
     * Processes the user's response indicating they did not recall the flashcard correctly.
     * @param actionEvent The event that triggered this action.
     */
    @FXML
    public void handleIncorrect(ActionEvent actionEvent) {
        processResponse(false);
    }

    /**
     * Handles user action to restart the current study session.
     * @param actionEvent The event that triggered this action.
     */
    @FXML
    public void handleRestartSession(ActionEvent actionEvent) {
        studyAlgorithm.resetSession();
        showFlashcard();
        updateButtons(false); // Re-enable buttons as the answered indexes are now clear
    }

    /**
     * Processes the user's response to a flashcard and updates the session statistics.
     * @param isCorrect Boolean indicating if the user's response was correct.
     */
    private void processResponse(boolean isCorrect) {
        Flashcard currentCard = studyAlgorithm.getCurrentFlashcard();
        studyAlgorithm.recordResponse(currentCard, isCorrect);

        correctButton.setDisable(true);
        incorrectButton.setDisable(true);

        updateSessionStats();
        updateButtons(countForCycle == currentDeck.getFlashcardCount() - 1);
    }

    /**
     * Updates the UI based on the state of the session, such as enabling/disabling buttons.
     * @param isLastCard Boolean indicating if the current card is the last in the cycle.
     */
    private void updateButtons(boolean isLastCard) {
        boolean isFirstCard = studyAlgorithm.isFirstCard();
        previousButton.setDisable(isFirstCard);
        nextButton.setDisable(studyAlgorithm.isSessionComplete());

        Flashcard currentCard = studyAlgorithm.getCurrentFlashcard();
        boolean hasBeenAnswered = studyAlgorithm.hasAnsweredThisCycle(currentCard);
        correctButton.setDisable(hasBeenAnswered);
        incorrectButton.setDisable(hasBeenAnswered);

        this.wasLastOfCycle = isLastCard;
    }

    /**
     * Displays the current flashcard's question, answer, and any associated images.
     */
    private void showFlashcard() {
        Flashcard card = studyAlgorithm.getCurrentFlashcard();
        if (card != null) {
            questionText.setText(card.getQuestion());
            answerText.setText(card.getAnswer());
            sessionStatsText.setText("Mastery Level: " + studyAlgorithm.getMasteryLevel(card));
            answerText.setVisible(false); // Ensure the answer is hidden initially

            updateFlashcardImages(card);
            updateHintDisplay(card);
            updateButtons(false);  // Update button states based on the current card

            correctButton.setDisable(studyAlgorithm.hasAnsweredThisCycle(card));
            incorrectButton.setDisable(studyAlgorithm.hasAnsweredThisCycle(card));
        }
    }

    /**
     * Updates the images associated with the current flashcard.
     * @param card The current flashcard being displayed.
     */
    private void updateFlashcardImages(Flashcard card) {
        updateImageView(questionImageView, card.getQuestionImage());
        updateImageView(answerImageView, card.getAnswerImage(), true);
    }

    /**
     * Updates an ImageView with a new image URL.
     * @param imageView The ImageView to update.
     * @param imageUrl The URL of the new image.
     * @param hideInitially If true, the image view will be hidden initially.
     */
    private void updateImageView(ImageView imageView, String imageUrl, boolean hideInitially) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            imageView.setImage(new Image(imageUrl));
            imageView.setVisible(!hideInitially);
        } else {
            imageView.setImage(null);
            imageView.setVisible(false);
        }
    }

    /**
     * Overloaded method for updating ImageView without initially hiding the image.
     * @param imageView The ImageView to update.
     * @param imageUrl The URL of the new image.
     */
    private void updateImageView(ImageView imageView, String imageUrl) {
        updateImageView(imageView, imageUrl, false);
    }

    /**
     * Updates the hint display based on the current flashcard.
     * @param card The current flashcard being displayed.
     */
    private void updateHintDisplay(Flashcard card) {
        if (card.getHint() != null && !card.getHint().isEmpty()) {
            hintText.setText("Hint: Click to reveal");
            hintText.setOnMouseClicked(event -> hintText.setText(card.getHint()));
        } else {
            hintText.setText("No hint available");
            hintText.setOnMouseClicked(null);  // Remove click action if no hint
        }
    }

    /**
     * Updates the session statistics display with the number of correct, incorrect responses and remaining cards.
     */
    private void updateSessionStats() {
        int totalCards = currentDeck.getFlashcardCount() * studyAlgorithm.getTotalCycles();
        int totalCorrect = studyAlgorithm.getTotalCorrect();
        int totalIncorrect = studyAlgorithm.getTotalIncorrect();

        sessionStatsText.setText(String.format("Correct: %d, Incorrect: %d, Remaining: %d", totalCorrect, totalIncorrect, totalCards - totalCorrect - totalIncorrect));
    }
}