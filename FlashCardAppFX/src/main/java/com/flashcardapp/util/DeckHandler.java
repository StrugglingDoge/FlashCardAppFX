package com.flashcardapp.util;

import com.flashcardapp.model.Deck;
import com.flashcardapp.model.Flashcard;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Handles loading and saving of deck objects to and from files.
 */
public class DeckHandler implements IDeckHandler {

    /**
     * Loads a deck from a specified file path.
     * @param path The path to the file containing the deck data.
     * @return A Deck object loaded from the file, or null if an error occurs.
     */
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
                        cardJson.optString("hint", ""),
                        loadImageFromData(cardJson.optString("questionImage")),
                        loadImageFromData(cardJson.optString("answerImage"))
                );
                deck.addFlashcard(card);
            }

            return deck;
        } catch (IOException e) {
            System.err.println("Error loading deck: " + e.getMessage());
            return null;
        }
    }

    /**
     * Decodes an image from Base64 or returns the path if it's a direct URL or file path.
     * @param imageData Encoded image data or a path.
     * @return The URI of the image as a string, or null if an error occurs.
     */
    private String loadImageFromData(String imageData) {
        if (imageData == null || imageData.isEmpty()) {
            return null;
        }

        // Decode Base64 to an image if not a URL or local file path
        if (imageData.length() > 100 && !imageData.startsWith("http") && !imageData.startsWith("file:/")) {
            try {
                byte[] imageBytes = Base64.decodeBase64(imageData);
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
                File tempFile = File.createTempFile("tempImage", ".png");
                ImageIO.write(image, "png", tempFile);
                return tempFile.toURI().toString();
            } catch (IOException e) {
                System.err.println("Error decoding image data: " + e.getMessage());
                return null;
            }
        } else {
            return imageData;
        }
    }

    /**
     * Saves a deck to a specified file path.
     * @param deck The deck to save.
     * @param filePath The path to save the deck file.
     */
    @Override
    public void saveDeck(Deck deck, String filePath) {
        JSONObject deckJson = new JSONObject();
        deckJson.put("name", deck.getName());
        JSONArray cardsJson = getObjects(deck, new File(filePath).getParent());
        deckJson.put("flashcards", cardsJson);

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(deckJson.toString(4));
        } catch (IOException e) {
            System.err.println("Error saving deck: " + e.getMessage());
        }
    }

    /**
     * Encodes images from the flashcards to Base64 and compiles them into a JSON array.
     * @param deck The deck containing the flashcards.
     * @param directoryPath The directory path to assist with relative paths, if necessary.
     * @return A JSONArray containing the JSON representation of flashcards.
     */
    private static JSONArray getObjects(Deck deck, String directoryPath) {
        JSONArray cardsJson = new JSONArray();
        for (Flashcard card : deck.getFlashcards()) {
            JSONObject cardJson = new JSONObject();
            cardJson.put("question", card.getQuestion());
            cardJson.put("answer", card.getAnswer());
            cardJson.putOpt("hint", card.getHint());

            if (card.getQuestionImage() != null) {
                cardJson.put("questionImage", encodeImageToBase64(card.getQuestionImage()));
            }
            if (card.getAnswerImage() != null) {
                cardJson.put("answerImage", encodeImageToBase64(card.getAnswerImage()));
            }

            cardsJson.put(cardJson);
        }
        return cardsJson;
    }

    /**
     * Encodes an image file to Base64.
     * @param imagePath The path to the image file.
     * @return The Base64 encoded string of the image, or null if the image cannot be processed.
     */
    private static String encodeImageToBase64(String imagePath) {
        try {
            BufferedImage image;
            if (imagePath.startsWith("http:") || imagePath.startsWith("https:")) {
                URL imageUrl = new URL(imagePath);
                image = ImageIO.read(imageUrl.openStream());
            } else if (imagePath.startsWith("file:/")) {
                image = ImageIO.read(new File(URI.create(imagePath)));
            } else {
                return null;
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            return Base64.encodeBase64String(imageBytes);
        } catch (IOException e) {
            System.err.println("Error encoding image: " + e.getMessage());
            return null;
        }
    }
}