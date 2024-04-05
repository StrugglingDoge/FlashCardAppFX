package com.flashcardapp.logic;

import com.flashcardapp.model.Flashcard;

import java.util.ArrayList;
import java.util.List;

public class BasicRotationalAlgorithm implements IAlgorithm {
    private List<Flashcard> flashcards;
    private int currentIndex = -1;

    @Override
    public void initialize(List<Flashcard> flashcards) {
        this.flashcards = new ArrayList<>(flashcards); // Create a copy to avoid modifying the original list
        currentIndex = -1; // Reset index
    }

    @Override
    public void recordResponse(Flashcard flashcard, boolean isCorrect) {
        // can adjust order here based on correctness
    }

    @Override
    public boolean isSessionComplete() {
        // Implementations could track progress and return true when the session should end.
        return false;
    }

    @Override
    public int getPreviousFlashcardIndex() {
        var previousIndex = currentIndex - 1 < 0 ? flashcards.size() - 1 : currentIndex - 1;

        currentIndex = previousIndex;

        return previousIndex;
    }

    @Override
    public int getNextFlashcardIndex() {
        var nextIndex = (currentIndex + 1) % flashcards.size();

        currentIndex = nextIndex;

        return nextIndex;
    }
}