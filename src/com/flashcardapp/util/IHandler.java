package com.flashcardapp.util;

import com.flashcardapp.model.Deck;

public interface IHandler {

    Deck loadDeck(String path);
    void saveDeck(Deck deck, String filePath);
}
