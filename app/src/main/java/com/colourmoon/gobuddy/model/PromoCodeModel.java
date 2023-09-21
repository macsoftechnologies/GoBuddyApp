package com.colourmoon.gobuddy.model;

public class PromoCodeModel {
    private String title;
    private String validDate;
    private String usersLimit;
    private String offerAmount;

    public PromoCodeModel(String title, String validDate, String usersLimit, String offerAmount) {
        this.title = title;
        this.validDate = validDate;
        this.usersLimit = usersLimit;
        this.offerAmount = offerAmount;
    }

    public String getTitle() {
        return title;
    }

    public String getValidDate() {
        return validDate;
    }

    public String getUsersLimit() {
        return usersLimit;
    }

    public String getOfferAmount() {
        return offerAmount;
    }
}
