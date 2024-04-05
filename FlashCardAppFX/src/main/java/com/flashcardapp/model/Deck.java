package com.flashcardapp.model;

import java.util.ArrayList;
import java.util.List;

public class Deck {

    private String name;
    private List<Flashcard> flashcards;

    // Constructor
    public Deck(String name) {
        this.name = name;
        this.flashcards = new ArrayList<>();
    }

    // Add a flashcard to the deck
    public void addFlashcard(Flashcard flashcard) {
        flashcards.add(flashcard);
    }

    // Get all flashcards in the deck
    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    // Get a flashcard by index
    public Flashcard getFlashcard(int index) {
        return flashcards.get(index);
    }

    // Get the number of flashcards in the deck
    public int getFlashcardCount() {
        return flashcards.size();
    }

    // Remove a flashcard by index
    public void removeFlashcard(int index) {
        flashcards.remove(index);
    }

    public void removeFlashcard(Flashcard flashcard) {
        flashcards.remove(flashcard);
    }

    // Remove all flashcards from the deck
    public void clearDeck() {
        flashcards.clear();
    }

    public String getName() {
        return name;
    }
}
