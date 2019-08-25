package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.Details.Detail;
import fr.wastemart.maven.javaclient.services.StageManager;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

public abstract class GenericController {
    @FXML private Label info;
    public Dotenv dotenv;

    protected GenericController() {
        String envFile = System.getProperty("user.dir")+"/src/main/resources/fr.wastemart.maven.javaclient/";
        dotenv = Dotenv.configure()
                .directory(envFile)
                .load();
    }

    public void init(List<Detail> details) throws Exception {
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
        setInfoText("Une erreur s'est produite");
    }

    public void handCursor() {
        StageManager.getInstance().setCursor(Cursor.HAND);
    }

    public void defaultCursor() {
        StageManager.getInstance().setCursor(Cursor.DEFAULT);
    }

    public void loadingCursor() {
        StageManager.getInstance().setCursor(Cursor.WAIT);
    }

}
