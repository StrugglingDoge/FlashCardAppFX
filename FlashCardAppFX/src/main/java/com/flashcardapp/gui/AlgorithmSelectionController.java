package com.flashcardapp.gui;

import com.flashcardapp.FlashcardApp;
import com.flashcardapp.logic.BasicRotationalAlgorithm;
import com.flashcardapp.logic.IAlgorithm;
import com.flashcardapp.logic.SpacedRepetitionAlgorithm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * Controller for selecting the study algorithm and setting the cycle count for Spaced Repetition.
 */
public class AlgorithmSelectionController {

    @FXML
    private ComboBox<String> algorithmComboBox;

    @FXML
    private TextField cycleCountField;

    /**
     * Initializes the combo box with algorithm choices and sets the default selected algorithm.
     */
    public void initialize() {
        algorithmComboBox.getItems().addAll("Basic Rotation", "Spaced Repetition");
        algorithmComboBox.getSelectionModel().selectFirst();

    }

    /**
     * Handles the action of the start button to set the study algorithm based on user selection.
     * @param event The action event triggered by clicking the start button.
     */
    @FXML
    private void handleStartButton(ActionEvent event) {
        try {
            String selectedAlgorithm = algorithmComboBox.getSelectionModel().getSelectedItem();
            int cycles = parseCycleCount(cycleCountField.getText());
            IAlgorithm algorithm = createAlgorithm(selectedAlgorithm, cycles);

            FlashcardApp.getInstance().setStudyAlgorithm(algorithm);
            FlashcardApp.getInstance().setFXMLScene("StudySessionScene");

            // Close the popup window
            closeWindow();
        } catch (NumberFormatException e) {
            System.err.println("Invalid cycle count entered: " + e.getMessage());
        }
    }

    /**
     * Creates and returns the appropriate algorithm instance based on the selection.
     * @param selectedAlgorithm The name of the selected algorithm.
     * @param cycles The number of cycles (used only for Spaced Repetition).
     * @return The corresponding algorithm instance.
     */
    private IAlgorithm createAlgorithm(String selectedAlgorithm, int cycles) {
        return "Spaced Repetition".equals(selectedAlgorithm) ? new SpacedRepetitionAlgorithm(cycles) : new BasicRotationalAlgorithm(cycles);
    }

    /**
     * Parses the cycle count from the text field input.
     * @param cycleText The text input representing the number of cycles.
     * @return The integer value of the cycle count.
     * @throws NumberFormatException if the input is not a valid integer.
     */
    private int parseCycleCount(String cycleText) throws NumberFormatException {
        return Integer.parseInt(cycleText);
    }

    /**
     * Closes the current window containing the combo box.
     */
    private void closeWindow() {
        algorithmComboBox.getScene().getWindow().hide();
    }
}