package com.flashcardapp.gui;

import com.flashcardapp.FlashcardApp;
import com.flashcardapp.model.Flashcard;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.util.*;

/**
 * Controller for the deck creation scene, managing UI interactions for creating and editing flashcards.
 */
public class DeckCreationSceneController {

    @FXML
    private TextField questionTextField;
    @FXML
    private TextArea answerTextArea;
    @FXML
    private VBox flashcardsListVBox;
    @FXML
    private Label errorMessageLabel;
    @FXML
    private Button addFlashcardButton;
    @FXML
    private TextField hintTextField;
    @FXML
    private VBox deckCreationVBox;
    @FXML
    private ImageView questionImageView;
    @FXML
    private ImageView answerImageView;

    private Map<VBox, Flashcard> uiToFlashcardMap = new HashMap<>();
    private Flashcard currentlyEditing = null;
    private String temporaryQuestionImage;
    private String temporaryAnswerImage;

    /**
     * Initializes controller and loads existing flashcards into the UI.
     */
    @FXML
    public void initialize() {
        FlashcardApp.getInstance().getCurrentDeck().getFlashcards().forEach(this::updateFlashcardsListUI);
        bindUIElements();
    }

    /**
     * Handles the addition of new flashcards or updates existing ones based on user input.
     */
    @FXML
    private void handleAddFlashcard(ActionEvent event) {
        if (validateInputFields()) {
            if (currentlyEditing != null) {
                updateExistingFlashcard();
            } else {
                addNewFlashcard();
            }
            resetInputFields();
        } else {
            showErrorMessage("Both question and answer must be filled.");
        }
    }

    /**
     * Updates the UI list of flashcards to reflect current data.
     * @param flashcard Flashcard to add or update in the UI.
     */
    private void updateFlashcardsListUI(Flashcard flashcard) {
        VBox flashcardBox = createFlashcardBox(flashcard);
        setupInteractionHandlers(flashcardBox, flashcard);
        flashcardsListVBox.getChildren().add(flashcardBox);
        uiToFlashcardMap.put(flashcardBox, flashcard);
    }

    /**
     * Refreshes the list of flashcards displayed on the UI.
     */
    private void refreshFlashcardsListUI() {
        flashcardsListVBox.getChildren().clear();
        uiToFlashcardMap.clear();
        FlashcardApp.getInstance().getCurrentDeck().getFlashcards().forEach(this::updateFlashcardsListUI);
    }

    /**
     * Validates user input for creating or updating flashcards.
     * @return true if inputs are valid, otherwise false.
     */
    private boolean validateInputFields() {
        return !questionTextField.getText().trim().isEmpty() && !answerTextArea.getText().trim().isEmpty();
    }

    /**
     * Resets input fields and temporary variables after adding or updating a flashcard.
     */
    private void resetInputFields() {
        questionTextField.clear();
        answerTextArea.clear();
        hintTextField.clear();
        questionImageView.setImage(null);
        answerImageView.setImage(null);
        temporaryQuestionImage = null;
        temporaryAnswerImage = null;
        currentlyEditing = null;
        addFlashcardButton.setText("Add Flashcard");
    }

    /**
     * Updates an existing flashcard with new content from input fields.
     */
    private void updateExistingFlashcard() {
        currentlyEditing.setQuestion(questionTextField.getText().trim());
        currentlyEditing.setAnswer(answerTextArea.getText().trim());
        currentlyEditing.setHint(getHint());
        currentlyEditing.setQuestionImage(temporaryQuestionImage);
        currentlyEditing.setAnswerImage(temporaryAnswerImage);
        refreshFlashcardsListUI();
    }

    /**
     * Adds a new flashcard based on user input.
     */
    private void addNewFlashcard() {
        Flashcard flashcard = new Flashcard(
                questionTextField.getText().trim(),
                answerTextArea.getText().trim(),
                getHint(),
                temporaryQuestionImage,
                temporaryAnswerImage
        );
        FlashcardApp.getInstance().getCurrentDeck().addFlashcard(flashcard);
        updateFlashcardsListUI(flashcard);
    }

    /**
     * Extracts hint from the hint text field, returning null if it's empty.
     * @return The hint or null if the hint field is empty.
     */
    private String getHint() {
        String hint = hintTextField.getText().trim();
        return hint.isEmpty() ? null : hint;
    }

    /**
     * Binds properties of UI elements to the corresponding dimensions of the parent container.
     */
    private void bindUIElements() {
        questionTextField.maxWidthProperty().bind(deckCreationVBox.widthProperty().subtract(20));
        answerTextArea.maxWidthProperty().bind(deckCreationVBox.widthProperty().subtract(20));
        hintTextField.maxWidthProperty().bind(deckCreationVBox.widthProperty().subtract(20));
        questionTextField.minHeightProperty().bind(deckCreationVBox.heightProperty().divide(20));
        answerTextArea.minHeightProperty().bind(deckCreationVBox.heightProperty().divide(14));
        hintTextField.minHeightProperty().bind(deckCreationVBox.heightProperty().divide(20));
    }

    /**
     * Creates a visual box representing a flashcard.
     * @param flashcard The flashcard to represent.
     * @return A VBox filled with the flashcard's content.
     */
    private VBox createFlashcardBox(Flashcard flashcard) {
        VBox flashcardBox = new VBox();
        flashcardBox.getStyleClass().add("flashcard-summary");
        setupImagesAndText(flashcard, flashcardBox);
        return flashcardBox;
    }

    /**
     * Sets up image and text components within a given flashcard box.
     * @param flashcard The source flashcard.
     * @param flashcardBox The VBox to populate.
     */
    private void setupImagesAndText(Flashcard flashcard, VBox flashcardBox) {
        if (flashcard.getQuestionImage() != null) {
            flashcardBox.getChildren().add(createImageView(flashcard.getQuestionImage()));
        }
        flashcardBox.getChildren().add(createText(flashcard.getQuestion(), "flashcard-question"));
        if (flashcard.getAnswerImage() != null) {
            flashcardBox.getChildren().add(createImageView(flashcard.getAnswerImage()));
        }
        flashcardBox.getChildren().add(createText(flashcard.getAnswer(), "flashcard-answer"));
        if (flashcard.getHint() != null) {
            flashcardBox.getChildren().add(createText("Hint: " + flashcard.getHint(), "flashcard-hint"));
        }
    }

    /**
     * Creates an ImageView for displaying a flashcard image.
     * @param imageUrl The URL of the image to display.
     * @return Configured ImageView.
     */
    private ImageView createImageView(String imageUrl) {
        ImageView imageView = new ImageView(new Image(imageUrl));
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    /**
     * Creates a Text node for displaying text in the flashcard box.
     * @param content The text content to display.
     * @param styleClass The CSS style class to apply.
     * @return Configured Text node.
     */
    private Text createText(String content, String styleClass) {
        Text text = new Text(content);
        text.wrappingWidthProperty().bind(deckCreationVBox.widthProperty().subtract(80));
        text.getStyleClass().add(styleClass);
        return text;
    }

    /**
     * Displays an error message in the UI.
     * @param message The error message to display.
     */
    private void showErrorMessage(String message) {
        errorMessageLabel.setText(message);
        errorMessageLabel.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> errorMessageLabel.setVisible(false));
        pause.play();
    }

    /**
     * Sets up mouse click handlers for the flashcard box to enable editing and context menu interactions.
     * @param flashcardBox The VBox representing a flashcard.
     * @param flashcard The flashcard associated with the VBox.
     */
    private void setupInteractionHandlers(VBox flashcardBox, Flashcard flashcard) {
        flashcardBox.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                loadFlashcardForEditing(flashcard);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                showContextMenu(flashcard, flashcardBox, event.getScreenX(), event.getScreenY());
            }
        });
    }

    /**
     * Loads a flashcard into the editing fields.
     * @param flashcard The flashcard to edit.
     */
    private void loadFlashcardForEditing(Flashcard flashcard) {
        questionTextField.setText(flashcard.getQuestion());
        answerTextArea.setText(flashcard.getAnswer());
        hintTextField.setText(flashcard.getHint() != null ? flashcard.getHint() : "");
        questionImageView.setImage(flashcard.getQuestionImage() != null ? new Image(flashcard.getQuestionImage()) : null);
        answerImageView.setImage(flashcard.getAnswerImage() != null ? new Image(flashcard.getAnswerImage()) : null);
        currentlyEditing = flashcard;
        addFlashcardButton.setText("Update Flashcard");
    }

    /**
     * Displays a context menu for a flashcard, allowing options to duplicate or remove.
     * @param flashcard The flashcard for which the context menu is shown.
     * @param flashcardBox The VBox representing the flashcard.
     * @param x The X screen coordinate for the menu.
     * @param y The Y screen coordinate for the menu.
     */
    private void showContextMenu(Flashcard flashcard, VBox flashcardBox, double x, double y) {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setAutoHide(true);

        MenuItem duplicateItem = new MenuItem("Duplicate");
        duplicateItem.setOnAction(e -> duplicateFlashcard(flashcard));
        MenuItem removeItem = new MenuItem("Remove");
        removeItem.setOnAction(e -> removeFlashcard(flashcard, flashcardBox));

        contextMenu.getItems().addAll(duplicateItem, removeItem);
        contextMenu.show(flashcardBox.getScene().getWindow(), x, y);
    }

    /**
     * Duplicates the contents of a flashcard into the input fields.
     * @param flashcard The flashcard to duplicate.
     */
    private void duplicateFlashcard(Flashcard flashcard) {
        loadFlashcardForEditing(flashcard);
        currentlyEditing = null;  // Clear currentlyEditing to ensure it's treated as a new entry
        addFlashcardButton.setText("Add Flashcard");
    }

    /**
     * Removes a flashcard from the deck and UI.
     * @param flashcard The flashcard to remove.
     * @param flashcardBox The VBox representing the flashcard in the UI.
     */
    private void removeFlashcard(Flashcard flashcard, VBox flashcardBox) {
        FlashcardApp.getInstance().getCurrentDeck().removeFlashcard(flashcard);
        flashcardsListVBox.getChildren().remove(flashcardBox);
        uiToFlashcardMap.remove(flashcardBox);
    }
    
    /**
     * Handles the event to return to the deck selection scene.
     * @param event The action event triggered by clicking the back button.
     */
    @FXML
    private void handleBack(ActionEvent event) {
        FlashcardApp.getInstance().setFXMLScene("MainScene");
    }
    
    /**
     * Handles the event to save the current deck to a file.
     * @param event The action event triggered by clicking the save button.
     */
    @FXML
    private void handleSaveDeck(ActionEvent event) {
        FlashcardApp.getInstance().saveCurrentDeck();
    }
    
    /**
     * Handles the event to clear all flashcards from the current deck.
     * @param event The action event triggered by clicking the clear button.
     */
    @FXML
    private void handleClearAll(ActionEvent event) {
        FlashcardApp.getInstance().getCurrentDeck().clearDeck();
        refreshFlashcardsListUI();
    }

    /**
     * Handles the event to set or update the question image of a flashcard.
     */
    @FXML
    private void handleSetQuestionImage(ActionEvent event) {
        chooseImage().ifPresent(url -> {
            if (currentlyEditing != null) {
                currentlyEditing.setQuestionImage(url);
                temporaryQuestionImage = url;
            } else {
                temporaryQuestionImage = url;
            }
            questionImageView.setImage(new Image(url));
        });
    }

    /**
     * Handles the event to set or update the answer image of a flashcard.
     */
    @FXML
    private void handleSetAnswerImage(ActionEvent event) {
        chooseImage().ifPresent(url -> {
            if (currentlyEditing != null) {
                currentlyEditing.setAnswerImage(url);
                temporaryAnswerImage = url;
            } else {
                temporaryAnswerImage = url;
            }
            answerImageView.setImage(new Image(url));
        });
    }
    
    /**
     * Handles the event to remove the question image of a flashcard.
     */
    @FXML
    private void handleRemoveQuestionImage(ActionEvent event) {
        questionImageView.setImage(null);
        if (currentlyEditing != null) {
            currentlyEditing.setQuestionImage(null);
        } else {
            temporaryQuestionImage = null;
        }
    }
    
    /**
     * Handles the event to remove the answer image of a flashcard.
     */
    @FXML
    private void handleRemoveAnswerImage(ActionEvent event) {
        answerImageView.setImage(null);
        if (currentlyEditing != null) {
            currentlyEditing.setAnswerImage(null);
        } else {
            temporaryAnswerImage = null;
        }
    }

    /**
     * Opens a dialog to let the user choose between entering a URL or selecting a file for an image.
     * @return An Optional containing the image URL if selected, otherwise an empty Optional.
     */
    private Optional<String> chooseImage() {
        List<String> choices = List.of("Enter URL", "Select File");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Enter URL", choices);
        dialog.setTitle("Image Selection");
        dialog.setHeaderText("Add an Image");
        dialog.setContentText("Choose your method:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            if ("Enter URL".equals(result.get())) {
                return promptForImageUrl();
            } else {
                return selectImageFile();
            }
        }
        return Optional.empty();
    }

    /**
     * Prompts the user to enter an image URL.
     * @return An Optional containing the entered URL if provided, otherwise an empty Optional.
     */
    private Optional<String> promptForImageUrl() {
        TextInputDialog urlDialog = new TextInputDialog("http://");
        urlDialog.setTitle("Enter Image URL");
        urlDialog.setHeaderText("Image URL");
        urlDialog.setContentText("Please enter the URL of the image:");
        return urlDialog.showAndWait();
    }

    /**
     * Opens a file chooser for the user to select an image file.
     * @return An Optional containing the file URL if a file is selected, otherwise an empty Optional.
     */
    private Optional<String> selectImageFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File file = fileChooser.showOpenDialog(null);
        return Optional.ofNullable(file).map(File::toURI).map(Object::toString);
    }
}
