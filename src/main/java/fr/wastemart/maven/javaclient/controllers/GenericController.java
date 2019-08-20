package fr.wastemart.maven.javaclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public abstract class GenericController {
    @FXML private Label info;

    public void init(Integer data) throws Exception {
    }

    public void init(String data) throws Exception {
    }

    public void init() throws Exception {
    }

    public void initFail() {
        setInfoErrorOccurred();
    }

    public void setInfoText(String string) {
        info.setText(string);
    }

    public void setInfoErrorOccurred() {
        setInfoText("An error occurred, see logs for details");
    }
}
