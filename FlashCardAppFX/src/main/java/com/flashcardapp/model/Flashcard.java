package com.flashcardapp.model;

/**
 * Represents a flashcard in the FlashcardApp.
 */
public class Flashcard {
    private String question;
    private String answer;
    private String hint = "";
    private String questionImage; // Path or URL for the question image
    private String answerImage;   // Path or URL for the answer image

    /**
     * Constructs a new flashcard with the given question and answer.
     * @param question The question on the flashcard.
     * @param answer The answer to the question.
     */
    public Flashcard(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    /**
     * Constructs a new flashcard with the given question, answer, and hint.
     * @param question The question on the flashcard.
     * @param answer The answer to the question.
     * @param hint A hint to help the user remember the answer.
     */
    public Flashcard(String question, String answer, String hint) {
        this.question = question;
        this.answer = answer;
        this.hint = hint;
    }

    /**
     * Constructs a new flashcard with the given question, answer, hint, and images.
     * @param question The question on the flashcard.
     * @param answer The answer to the question.
     * @param hint A hint to help the user remember the answer.
     * @param questionImage The path or URL for the question image.
     * @param answerImage The path or URL for the answer image.
     */
    public Flashcard(String question, String answer, String hint, String questionImage, String answerImage) {
        this.question = question;
        this.answer = answer;
        this.hint = hint;
        this.questionImage = questionImage;
        this.answerImage = answerImage;
    }

    /*
     * Returns the question image path or URL.
     */
    public String getQuestionImage() {
        return questionImage;
    }

    /*
     * Returns the answer image path or URL.
     */
    public String getAnswerImage() {
        return answerImage;
    }
    
    /*
     * Returns the question on the flashcard.
     */
    public String getQuestion() {
        return question;
    }

    /*
     * Returns the answer to the question.
     */
    public String getAnswer() {
        return answer;
    }

    /*
     * Returns the hint to help the user remember the answer.
     */
    public String getHint() {
        return hint;
    }

    /*
     * Sets the question on the flashcard.
     * @param question The question on the flashcard.
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /*
     * Sets the answer to the question.
     * @param answer The answer to the question.
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /*
     * Sets the hint to help the user remember the answer.
     * @param hint A hint to help the user remember the answer.
     */
    public void setHint(String hint) {
        this.hint = hint;
    }

    /*
     * Sets the question image path or URL.
     * @param questionImage The path or URL for the question image.
     */
    public void setAnswerImage(String answerImage) {
        this.answerImage = answerImage;
    }

    /*
     * Sets the answer image path or URL.
     * @param answerImage The path or URL for the answer image.
     */
    public void setQuestionImage(String questionImage) {
        this.questionImage = questionImage;
    }
}
