package com.flashcardapp.logic;

import com.flashcardapp.model.Flashcard;

import java.util.List;

public class StudyAlgorithm implements IAlgorithm {

    @Override
    public void initialize(List<Flashcard> flashcards) {

    }

    @Override
    public void recordResponse(Flashcard flashcard, boolean isCorrect) {

    }

    @Override
    public boolean isSessionComplete() {
        return false;
    }

    @Override
    public int getPreviousFlashcardIndex() {
        return 0;
    }

    @Override
    public int getNextFlashcardIndex() {
        return 0;
    }
}
