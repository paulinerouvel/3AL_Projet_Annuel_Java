package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.Details.Detail;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

public abstract class GenericController {
    @FXML private Label info;
    public Dotenv dotenv = Dotenv.load();

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
}
