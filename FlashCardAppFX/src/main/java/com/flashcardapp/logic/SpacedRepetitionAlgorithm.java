package com.flashcardapp.logic;

import com.flashcardapp.model.Flashcard;
import com.flashcardapp.util.ConfigHandler;
import com.flashcardapp.util.DeckInfo;

import java.util.*;

/**
 * An implementation of the spaced repetition algorithm for studying flashcards.
 */
public class SpacedRepetitionAlgorithm implements IAlgorithm {
    private List<Flashcard> flashcards;
    private final Map<Flashcard, Integer> masteryLevels;
    private final Map<Flashcard, Integer> lastReviewed;
    private final Set<Flashcard> answeredThisCycle;
    private int currentReviewIndex;
    private int totalCyclesCompleted; // Track completed cycles

    private final int maxCycles; // Change from maxReviews to maxCycles

    private int totalCorrect;
    private int totalIncorrect;

    /**
     * Constructs a new instance of the spaced repetition algorithm with the specified maximum cycles.
     * @param maxCycles the maximum number of cycles to complete in the session.
     */
    public SpacedRepetitionAlgorithm(int maxCycles) {
        this.maxCycles = maxCycles;
        this.flashcards = new ArrayList<>();
        this.masteryLevels = new HashMap<>();
        this.lastReviewed = new HashMap<>();
        this.answeredThisCycle = new HashSet<>();
        this.totalCyclesCompleted = 0;
        this.totalCorrect = 0;
        this.totalIncorrect = 0;
    }
    
    @Override
    public void initialize(List<Flashcard> flashcards) {
        this.flashcards = new ArrayList<>(flashcards);
        this.flashcards.forEach(flashcard -> {
            masteryLevels.put(flashcard, 0);
            lastReviewed.put(flashcard, -1); // Initialize with -1 indicating it has not been reviewed yet
        });
        this.currentReviewIndex = 0;
    }

    @Override
    public void recordResponse(Flashcard flashcard, boolean isCorrect) {
        if (answeredThisCycle.contains(flashcard)) {
            return; // Ignore additional responses if already answered in this cycle
        }
        answeredThisCycle.add(flashcard);

        int currentLevel = masteryLevels.get(flashcard);
        if (isCorrect) {
            masteryLevels.put(flashcard, currentLevel + 1);
            totalCorrect++;
        } else {
            masteryLevels.put(flashcard, Math.max(0, currentLevel - 1)); // Decrease level but not below 0
            totalIncorrect++;
        }
        lastReviewed.put(flashcard, totalCyclesCompleted); // Update last reviewed cycle

        // Check if all flashcards have been answered in this cycle
        if (answeredThisCycle.size() == flashcards.size()) {
            totalCyclesCompleted++;
            resetAnsweredStates();
        }
        
        try{
            DeckInfo currentDeck = ConfigHandler.getInstance().getOption("lastDeck", DeckInfo.class);
            HashMap<String, Integer> masteryLevels = new HashMap<>();
            for (Flashcard card : this.masteryLevels.keySet()) {
                masteryLevels.put(card.getQuestion(), this.masteryLevels.get(card));
            }
            currentDeck.setMasteryLevels(masteryLevels);
            ConfigHandler.getInstance().saveOption("lastDeck", currentDeck);
        }catch (IllegalArgumentException e) // thrown if the option has not yet been saved
        {
            // Do nothing
        }
    }

    @Override
    public boolean isSessionComplete() {
        return totalCyclesCompleted >= maxCycles;
    }

    @Override
    public boolean isFirstCard() {
        return answeredThisCycle.isEmpty();
    }

    @Override
    public Flashcard getCurrentFlashcard() {
        return flashcards.get(currentReviewIndex);
    }

    @Override
    public boolean moveToNext() {
        if (answeredThisCycle.size() == flashcards.size()) {
            return false; // Do not move to the next flashcard if all have been answered in this cycle
        }
        currentReviewIndex = (currentReviewIndex + 1) % flashcards.size();
        return true;
    }
    
    @Override
    public boolean moveToPrevious() {
        if (answeredThisCycle.size() == flashcards.size()) {
            return false; // Do not move to the previous flashcard if all have been answered in this cycle
        }
        if (currentReviewIndex == 0) {
            currentReviewIndex = flashcards.size() - 1;
        } else {
            currentReviewIndex--;
        }
        return true;
    }
    
    @Override
    public int getTotalCorrect() {
        return totalCorrect;
    }

    @Override
    public int getTotalIncorrect() {
        return totalIncorrect;
    }

    @Override
    public int getTotalCycles() {
        return maxCycles;
    }

    @Override
    public boolean hasAnsweredThisCycle(Flashcard flashcard) {
        return answeredThisCycle.contains(flashcard);
    }

    @Override
    public String getMasteryLevel(Flashcard card) {
        return "Level " + masteryLevels.get(card);
    }

    @Override
    public void resetSession() {
        flashcards.clear();
        masteryLevels.clear();
        lastReviewed.clear();
        answeredThisCycle.clear();
        currentReviewIndex = 0;
        totalCyclesCompleted = 0;
        totalCorrect = 0;
        totalIncorrect = 0;
    }

    /**
     * Resets the answered state for all flashcards and sorts them by mastery level and last reviewed time.
     */
    public void resetAnsweredStates() {
        answeredThisCycle.clear(); // Reset answered state for the new cycle
        flashcards.sort((card1, card2) -> {
            int levelComparison = Integer.compare(masteryLevels.get(card1), masteryLevels.get(card2));
            if (levelComparison != 0) return levelComparison;
            return Integer.compare(lastReviewed.get(card1), lastReviewed.get(card2));
        });
        currentReviewIndex = 0; // Reset the current review index to the first flashcard
    }

}