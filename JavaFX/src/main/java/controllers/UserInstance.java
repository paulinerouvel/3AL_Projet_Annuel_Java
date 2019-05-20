package controllers;

import services.Authentication;

public class UserInstance {


    private String token = null;

    private Authentication Authentifier;

    public StringProperty textProperty() {
        return text ;
    }

    public final void setText(String text) {
        textProperty().set(text);
    }

    public final String getText() {
        return textProperty().get();
    }

    // other properties as needed...
}