package com.flashcardapp.logic;

import com.flashcardapp.model.Flashcard;
import java.util.List;

public interface IAlgorithm {

    // Initializes the algorithm with a list of flashcards
    void initialize(List<Flashcard> flashcards);

    // Records the user's response to the flashcard (correct, incorrect, etc.)
    void recordResponse(Flashcard flashcard, boolean isCorrect);

    // Determines if the study session is complete
    boolean isSessionComplete();

    int getPreviousFlashcardIndex();
    int getNextFlashcardIndex();
}
