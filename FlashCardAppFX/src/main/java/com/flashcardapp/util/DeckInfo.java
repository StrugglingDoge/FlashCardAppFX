package com.flashcardapp.util;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.flashcardapp.model.Flashcard;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents information about a deck of flashcards including the deck's name, file path, count of flashcards,
 * and mastery levels of individual flashcards.
 */
public class DeckInfo {

    @JsonDeserialize
    private String name;

    @JsonDeserialize
    private String path;

    @JsonDeserialize
    private int flashcardCount;

    @JsonDeserialize(as = HashMap.class)
    private Map<String, Integer> masteryLevels;

    /**
     * Default constructor for creating an empty deck information instance.
     */
    public DeckInfo() {
        this.masteryLevels = new HashMap<>();
    }

    /**
     * Constructs a new DeckInfo instance with specified name, path, and number of flashcards.
     * @param name the name of the deck
     * @param path the file path to the deck
     * @param flashcardCount the number of flashcards in the deck
     */
    public DeckInfo(String name, String path, int flashcardCount) {
        this.name = name;
        this.path = path;
        this.flashcardCount = flashcardCount;
        this.masteryLevels = new HashMap<>();
    }

    /**
     * Returns the name of the deck.
     * @return the deck name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the path to the deck file.
     * @return the deck file path
     */
    public String getPath() {
        return path;
    }

    /**
     * Returns the count of flashcards in the deck.
     * @return the number of flashcards
     */
    public int getFlashcardCount() {
        return flashcardCount;
    }

    /**
     * Returns the mastery levels map, indicating mastery level per flashcard question.
     * @return the map of flashcard questions to their respective mastery levels
     */
    public Map<String, Integer> getMasteryLevels() {
        return new HashMap<>(masteryLevels);
    }

    /**
     * Sets the mastery levels for the deck, mapping from flashcard questions to mastery levels.
     * @param masteryLevels the map of flashcard questions to mastery levels
     */
    public void setMasteryLevels(Map<String, Integer> masteryLevels) {
        this.masteryLevels = new HashMap<>(masteryLevels);
    }

    /**
     * Provides a string representation of the deck information.
     * @return string describing the deck
     */
    @Override
    public String toString() {
        return name + " (" + flashcardCount + " flashcards)";
    }

    /**
     * Compares this DeckInfo with another object for equality, based on the deck name.
     * @param obj the object to compare with
     * @return true if the given object represents a DeckInfo with the same name, otherwise false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DeckInfo deckInfo = (DeckInfo) obj;
        return name != null ? name.equals(deckInfo.name) : deckInfo.name == null;
    }

    /**
     * Generates a hash code for this DeckInfo.
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}