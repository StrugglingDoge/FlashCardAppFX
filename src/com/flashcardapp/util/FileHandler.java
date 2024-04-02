package com.flashcardapp.util;

import com.flashcardapp.model.Deck;
import com.flashcardapp.model.Flashcard;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHandler implements IHandler {
    @Override
    public Deck loadDeck(String path) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(path)));
            JSONObject deckJson = new JSONObject(content);

            Deck deck = new Deck(deckJson.getString("name"));
            JSONArray cardsJson = deckJson.getJSONArray("flashcards");

            for (int i = 0; i < cardsJson.length(); i++) {
                JSONObject cardJson = cardsJson.getJSONObject(i);
                Flashcard card = new Flashcard(
                        cardJson.getString("question"),
                        cardJson.getString("answer"),
                        cardJson.optString("hint", null) // Use optString to handle optional hint
                );
                deck.addFlashcard(card);
            }

            return deck;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveDeck(Deck deck, String filePath) {
        JSONObject deckJson = new JSONObject();
        deckJson.put("name", deck.getName());

        // Convert flashcards to JSON array
        JSONArray cardsJson = getObjects(deck);
        deckJson.put("flashcards", cardsJson);

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(deckJson.toString(4)); // Write the JSON string with an indentation of 4 spaces
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JSONArray getObjects(Deck deck) {
        JSONArray cardsJson = new JSONArray();
        for (Flashcard card : deck.getFlashcards()) {
            JSONObject cardJson = new JSONObject();
            cardJson.put("question", card.getQuestion());
            cardJson.put("answer", card.getAnswer());
            // Add hint if available and not null/empty
            if (card.getHint() != null && !card.getHint().isEmpty()) {
                cardJson.put("hint", card.getHint());
            }
            cardsJson.put(cardJson);
        }
        return cardsJson;
    }
}
