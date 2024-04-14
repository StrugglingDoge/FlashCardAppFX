package com.flashcardapp;

import com.flashcardapp.logic.IAlgorithm;
import com.flashcardapp.model.Deck;
import com.flashcardapp.util.ConfigHandler;
import com.flashcardapp.util.DeckHandler;
import com.flashcardapp.util.DeckInfo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Main application class for the Flashcard study helper.
 * Manages the application lifecycle, scene transitions, and global state.
 */
public class FlashcardApp extends Application {

    private static FlashcardApp instance;

    private Stage primaryStage;
    private Scene currentScene;
    private Deck currentDeck;
    private IAlgorithm studyAlgorithm;
    private DeckHandler deckHandler;
    private String currentStyle = "style.css"; // Default style

    /**
     * Constructs the FlashcardApp and initializes the singleton instance.
     */
    public FlashcardApp() {
        deckHandler = new DeckHandler();
        instance = this;
    }

    /**
     * Returns the singleton instance of the application.
     */
    public static FlashcardApp getInstance() {
        return instance;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        try {
            loadInitialScene();
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadConfigOptions();
    }

    /**
     * Loads the initial scene and sets up the primary stage with necessary configurations.
     */
    private void loadInitialScene() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/flashcardapp/gui/MainScene.fxml"));
        primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/com/flashcardapp/gui/logo.png")));
        currentScene = new Scene(root, 900, 800);
        primaryStage.setScene(currentScene);
        primaryStage.setTitle("Flashcard Study Helper");
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(800);
        updateStylesheet();
    }

    /**
     * Loads configuration options and applies them to the application state.
     */
    private void loadConfigOptions() {
        try {
            String themeValue = ConfigHandler.getInstance().getOption("theme", String.class);
            setTheme(themeValue);
        } catch (IllegalArgumentException e) {
            ConfigHandler.getInstance().saveOption("theme", "light");
        }

        try {
            DeckInfo deckInfo = ConfigHandler.getInstance().getOption("lastDeck", DeckInfo.class);
            if (deckInfo != null) {
                currentDeck = deckHandler.loadDeck(deckInfo.getPath());
            }
        } catch (IllegalArgumentException ignored) {
        }
        
        // reload the main scene
        setFXMLScene("MainScene");
    }

    /**
     * Changes the FXML scene for the application.
     *
     * @param sceneName The name of the scene to switch to.
     */
    public void setFXMLScene(String sceneName) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/flashcardapp/gui/" + sceneName + ".fxml"));
            currentScene.setRoot(root);
            updateStylesheet();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Saves the currently loaded deck to a specified path.
     */
    public void saveDeck(Deck currentDeck, String absolutePath) {
        deckHandler.saveDeck(currentDeck, absolutePath);
        ConfigHandler.getInstance().saveOption("lastDeck", new DeckInfo(currentDeck.getName(), absolutePath, currentDeck.getFlashcards().size()));
    }

    /**
     * Loads a deck from a specified path and updates the application state.
     */
    public void loadDeck(String path) {
        currentDeck = deckHandler.loadDeck(path);
        ConfigHandler.getInstance().saveOption("lastDeck", new DeckInfo(currentDeck.getName(), path, currentDeck.getFlashcards().size()));
    }

    /**
     * Updates the application's stylesheet based on the current theme.
     */
    private void updateStylesheet() {
        currentScene.getStylesheets().clear();
        currentScene.getStylesheets().add(getClass().getResource("/com/flashcardapp/gui/" + currentStyle).toExternalForm());
    }

    /**
     * Sets the theme of the application based on the selected theme.
     */
    public void setTheme(String selectedTheme) {
        currentStyle = "dark".equals(selectedTheme) ? "style-dark.css" : "style.css";
        updateStylesheet();
    }
    
    /**
     * Returns the primary stage of the application.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    /**
     * Returns the current deck loaded in the application.
     */
    public Deck getCurrentDeck() {
        return currentDeck;
    }
    
    /**
     * Returns the directory where application data is stored.
     */
    public File getDataDirectory() {
        return new File(System.getProperty("user.home") + "/.flashcardapp");
    }
    
    /**
     * Returns the current study algorithm.
     */
    public IAlgorithm getStudyAlgorithm() {
        return studyAlgorithm;
    }
    
    /**
     * Sets the current study algorithm.
     */
    public void setStudyAlgorithm(IAlgorithm studyAlgorithm) {
        this.studyAlgorithm = studyAlgorithm;
    }

    /**
     * Resets the application data and reloads the initial scene.
     */
    public void resetData() {
        currentDeck = null;
        studyAlgorithm = null;
        ConfigHandler.getInstance().saveOption("lastDeck", null);
        setFXMLScene("MainScene");
    }

    /**
     * Saves the current deck, if available, to the last known path.
     */
    public void saveCurrentDeck() {
        if (currentDeck != null) {
            saveDeck(currentDeck, ConfigHandler.getInstance().getOption("lastDeck", DeckInfo.class).getPath());
        }
    }

    /**
     * Sets the current deck to the specified deck.
     */
    public void setCurrentDeck(Deck deck) {
        currentDeck = deck;
    }
}