package com.flashcardapp.util;

import com.flashcardapp.model.Deck;

import java.io.IOException;

/**
 * Interface defining the contract for handling deck operations such as loading and saving.
 */
public interface IDeckHandler {

    /**
     * Loads a deck of flashcards from a specified file path.
     *
     * @param path the file path from which the deck is to be loaded
     * @return the loaded Deck object
     * @throws IOException if there is an error reading from the specified path
     */
    Deck loadDeck(String path) throws IOException;

    /**
     * Saves a given deck to a specified file path.
     *
     * @param deck the Deck object to save
     * @param filePath the file path where the deck should be saved
     * @throws IOException if there is an error writing to the specified path
     */
    void saveDeck(Deck deck, String filePath) throws IOException;
}
