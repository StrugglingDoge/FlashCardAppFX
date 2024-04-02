package com.flashcardapp.logic;

import com.flashcardapp.model.Flashcard;

import java.util.Comparator;
import java.util.List;

// IAlgorithm is an interface for study deck algorithms, so it will need to order the cards in a deck
public interface IAlgorithm extends Comparator {
    List<Flashcard> orderFlashcards(List<Flashcard> flashcards);

}
