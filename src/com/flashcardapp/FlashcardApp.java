package com.flashcardapp;

import com.flashcardapp.model.Deck;
import com.flashcardapp.model.Flashcard;
import com.flashcardapp.util.FileHandler;
import com.flashcardapp.util.IHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.util.List;

public class FlashcardApp extends Application {

    private static FlashcardApp instance = null;
    private Stage primaryStage;
    private Scene currentScene;

    private Deck currentDeck;
    private IHandler handler;

    public FlashcardApp()
    {
        handler = new FileHandler();
        instance = this;
        //TODO: Actually handle loading decks from file
        currentDeck = new Deck("Countries and Capitals");
        currentDeck.addFlashcard(new Flashcard("What is the capital of France?", "Paris", "It's a city in France"));
        currentDeck.addFlashcard(new Flashcard("What is the capital of Germany?", "Berlin", "It's a city in Germany"));
        currentDeck.addFlashcard(new Flashcard("What is the capital of Italy?", "Rome", "It's a city in Italy"));
        currentDeck.addFlashcard(new Flashcard("What is the capital of Spain?", "Madrid"));
        currentDeck.addFlashcard(new Flashcard("What is the capital of the United Kingdom?", "London"));
    }

    public static FlashcardApp getInstance() {
        return instance;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            this.primaryStage = primaryStage;
            // Load the FXML file for MainScene
            Parent root = FXMLLoader.load(getClass().getResource("/com/flashcardapp/gui/MainScene.fxml"));

            // Specify the initial size but allow resizing
            currentScene = new Scene(root, 800, 600); // Set initial width and height

            primaryStage.setScene(currentScene);
            primaryStage.setTitle("Flashcard Study Helper");

            // Set minimum width and height for the window
            primaryStage.setMinWidth(600); // Set minimum window width
            primaryStage.setMinHeight(450); // Set minimum window height

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFXMLScene(String sceneName){
        // path will be /com/flashcardapp/gui/ + sceneName + .fxml
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/flashcardapp/gui/" + sceneName + ".fxml"));
            currentScene.setRoot(root);
            primaryStage.setScene(currentScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Deck getCurrentDeck() {
        return currentDeck;
    }

    public Window getPrimaryStage() {
        return primaryStage;
    }

    public File getDataDirectory() {
        return new File(System.getProperty("user.home"));
    }

    public void saveDeck(Deck currentDeck, String absolutePath) {
        handler.saveDeck(currentDeck, absolutePath);
    }

    public void loadDeck(String path) {
        currentDeck = handler.loadDeck(path);
    }
}
