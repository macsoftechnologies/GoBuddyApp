package com.colourmoon.gobuddy.model;

public class FAQModel {
    private String faqQuestion;
    private String faqAnswer;

    public FAQModel(String faqQuestion, String faqAnswer) {
        this.faqQuestion = faqQuestion;
        this.faqAnswer = faqAnswer;
    }

    public String getFaqQuestion() {
        return faqQuestion;
    }

    public String getFaqAnswer() {
        return faqAnswer;
    }
}
