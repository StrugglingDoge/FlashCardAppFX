package com.flashcardapp.logic;

import com.flashcardapp.model.Flashcard;

import java.util.*;

/**
 * A basic implementation of a flashcard study algorithm that rotates through the flashcards in order.
 */
public class BasicRotationalAlgorithm implements IAlgorithm {
    private List<Flashcard> flashcards;
    private Set<Flashcard> answeredThisCycle;
    private Map<Flashcard, int[]> masteryMap;
    private int currentIndex = -1;

    private int totalCorrect;
    private int totalIncorrect;
    private int totalCycles;
    private int maxCycles;
    
    public BasicRotationalAlgorithm(int maxCycles) {
        this.maxCycles = maxCycles;
    }

    @Override
    public void initialize(List<Flashcard> flashcards) {
        this.flashcards = new ArrayList<>(flashcards);
        answeredThisCycle = new HashSet<>();
        masteryMap = new HashMap<>();
        for (Flashcard card : flashcards) {
            masteryMap.put(card, new int[2]); // [0] for correct counts, [1] for attempts
        }
        currentIndex = -1;
        totalCycles = 0;
        
        totalCorrect = 0;
        totalIncorrect = 0;
        
        // show the flashcard
        moveToNext();
    }

    @Override
    public void recordResponse(Flashcard flashcard, boolean isCorrect) {
        if (isCorrect) {
            totalCorrect++;
            masteryMap.get(flashcard)[0]++; // Increment correct count
        } else {
            totalIncorrect++;
        }
        masteryMap.get(flashcard)[1]++; // Increment attempt count
        answeredThisCycle.add(flashcard);
        if (answeredThisCycle.size() == flashcards.size()) {
            totalCycles++;
            answeredThisCycle.clear();
        }
    }

    @Override
    public String getMasteryLevel(Flashcard card) {
        int[] stats = masteryMap.get(card);
        if (stats != null && stats[1] > 0) {
            double masteryPercentage = (double) stats[0] / stats[1] * 100;
            return String.format("%.2f%%", masteryPercentage);
        }
        return "Unattempted";
    }

    @Override
    public boolean isSessionComplete() {
        return maxCycles > 0 && totalCycles >= maxCycles;
    }

    @Override
    public Flashcard getCurrentFlashcard() {
        if (currentIndex >= 0 && currentIndex < flashcards.size()) {
            return flashcards.get(currentIndex);
        }
        return null;
    }

    public int getPreviousFlashcardIndex() {
        int previousIndex = currentIndex - 1 < 0 ? flashcards.size() - 1 : currentIndex - 1;
        currentIndex = previousIndex;
        return previousIndex;
    }

    public int getNextFlashcardIndex() {
        int nextIndex = (currentIndex + 1) % flashcards.size();
        currentIndex = nextIndex;
        return nextIndex;
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
        return totalCycles;
    }

    @Override
    public boolean hasAnsweredThisCycle(Flashcard flashcard) {
        return answeredThisCycle.contains(flashcard);
    }

    @Override
    public boolean moveToNext() {
        currentIndex = getNextFlashcardIndex();
        return true; // Always returns true as we are in a continuous cycle
    }

    @Override
    public boolean moveToPrevious() {
        currentIndex = getPreviousFlashcardIndex();
        return true; // Always returns true as we are in a continuous cycle
    }

    @Override
    public boolean isFirstCard() {
        return currentIndex == 0;
    }

    @Override
    public void resetSession() {
        answeredThisCycle.clear();
        currentIndex = -1;
        totalCorrect = 0;
        totalIncorrect = 0;
        totalCycles = 0;
        for (int[] stats : masteryMap.values()) {
            stats[0] = 0;
            stats[1] = 0;
        }
        moveToNext();
    }
}