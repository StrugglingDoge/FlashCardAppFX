package com.flashcardapp.logic;

import com.flashcardapp.model.Flashcard;
import java.util.List;

/**
 * Interface for algorithms that manage the logic of study sessions using flashcards.
 */
public interface IAlgorithm {

    /**
     * Initializes the algorithm with a given list of flashcards.
     * @param flashcards the list of flashcards to be used in the session.
     */
    void initialize(List<Flashcard> flashcards);

    /**
     * Records the user's response to a flashcard.
     * @param flashcard the flashcard that was answered.
     * @param isCorrect true if the user's response was correct, false otherwise.
     */
    void recordResponse(Flashcard flashcard, boolean isCorrect);

    /**
     * Checks if the study session has been completed.
     * @return true if all cycles in the session are complete, false otherwise.
     */
    boolean isSessionComplete();

    /**
     * Checks if the current flashcard is the first in the deck.
     * @return true if it is the first flashcard, false otherwise.
     */
    boolean isFirstCard();

    /**
     * Retrieves the current flashcard being studied.
     * @return the current flashcard.
     */
    Flashcard getCurrentFlashcard();

    /**
     * Retrieves the total number of correct responses recorded in the session.
     * @return the total number of correct responses.
     */
    int getTotalCorrect();

    /**
     * Retrieves the total number of incorrect responses recorded in the session.
     * @return the total number of incorrect responses.
     */
    int getTotalIncorrect();

    /**
     * Retrieves the total number of cycles completed in the session.
     * @return the total number of completed cycles.
     */
    int getTotalCycles();

    /**
     * Checks if the current flashcard has been answered in this cycle.
     * @param flashcard the flashcard to check.
     * @return true if the flashcard has been answered, false otherwise.
     */
    boolean hasAnsweredThisCycle(Flashcard flashcard);

    /**
     * Resets the session to start anew.
     */
    void resetSession();

    /**
     * Moves to the next flashcard in the deck.
     * @return true if successfully moved to the next card, false if it is the last card.
     */
    boolean moveToNext();

    /**
     * Moves to the previous flashcard in the deck.
     * @return true if successfully moved to the previous card, false if it is the first card.
     */
    boolean moveToPrevious();

    /**
     * Gets the mastery level of a specific flashcard.
     * @param card the flashcard for which to get the mastery level.
     * @return a string representing the mastery level.
     */
    String getMasteryLevel(Flashcard card);
}