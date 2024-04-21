package com.example.olioharkka;

public class QuizQuestion {
    private String questionText;
    private String correctAnswer; // Add field for correct answer
    private String[] answerOptions;

    public QuizQuestion(String questionText, String correctAnswer, String[] answerOptions) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.answerOptions = answerOptions;
    }

    // Getters and setters for fields
    public String getQuestionText() {
        return questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String[] getAnswerOptions() {
        return answerOptions;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setAnswerOptions(String[] answerOptions) {
        this.answerOptions = answerOptions;
    }
}