package com.flashcardapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a deck of flashcards in the FlashcardApp.
 */
public class Deck {

    private String name;
    private List<Flashcard> flashcards;

    /**
     * Constructs a new deck with the given name.
     * @param name The name of the deck.
     */
    public Deck(String name) {
        this.name = name;
        this.flashcards = new ArrayList<>();
    }

    /**
     * Adds a flashcard to the deck.
     * @param flashcard The flashcard to add.
     */
    public void addFlashcard(Flashcard flashcard) {
        flashcards.add(flashcard);
    }

    /**
     * Returns the list of flashcards in the deck.
     */
    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    /**
     * Returns the flashcard at the specified index.
     * @param index The index of the flashcard to retrieve.
     */
    public Flashcard getFlashcard(int index) {
        return flashcards.get(index);
    }

    /**
     * Returns the number of flashcards in the deck.
     */
    public int getFlashcardCount() {
        return flashcards.size();
    }

    /**
     * Removes the flashcard at the specified index.
     * @param index The index of the flashcard to remove.
     */
    public void removeFlashcard(int index) {
        flashcards.remove(index);
    }

    /**
     * Removes the specified flashcard from the deck.
     * @param flashcard The flashcard to remove.
     */
    public void removeFlashcard(Flashcard flashcard) {
        flashcards.remove(flashcard);
    }

    /**
     * Clears all flashcards from the deck.
     */
    public void clearDeck() {
        flashcards.clear();
    }

    /**
     * Returns the name of the deck.
     */
    public String getName() {
        return name;
    }
}
