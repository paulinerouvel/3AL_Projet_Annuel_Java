package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.Details.Detail;
import fr.wastemart.maven.javaclient.services.Details.StringDetail;
import fr.wastemart.maven.javaclient.services.HttpResponse;
import fr.wastemart.maven.javaclient.services.Logger;
import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class GlobalLoginController extends GenericController {
    @FXML public TextField login;
    @FXML public PasswordField password;
    @FXML public AnchorPane fieldsArea;

    @Override
    public void init(List<Detail> details) throws Exception {
        StringDetail stringDetail = (StringDetail) details.get(0);
        setInfoText(stringDetail.getValue());

        fieldsArea.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER)
            {
                authenticate();
            }
        });
    }

    public void authenticate() {
        new Thread(() -> {
            HttpResponse loginResponse = UserInstance.getInstance().login(login.getText(), password.getText());

            Platform.runLater(() -> {
                try {
                    processLoginAttempt(loginResponse);
                } catch (Exception e) {
                    Logger.getInstance().reportError(e);
                    setInfoText("Test");
                    setInfoErrorOccurred();
                }
            });
        }).start();
    }

    public void processLoginAttempt(HttpResponse loginResponse) throws Exception {
        if(loginResponse.getResponseCode() > 299){
            if(loginResponse.getResponseCode() == 503){
                setInfoText("Timeout");
            } else if(loginResponse.getResponseCode() == 400) {
                setInfoText("Identifiant ou mot de passe incorrect");
            } else if(loginResponse.getResponseCode() == 401) {
                setInfoText("Cet utilisateur n'existe pas ou n'est pas activé");
            } else {
                setInfoText("Erreur interne. Veuillez re-essayer plus tard.");
            }
        } else {
            UserInstance.getInstance().setToken(loginResponse.getDataAsJSONObject());

            if(UserInstance.getInstance().tokenIsValid()) {
                UserInstance.getInstance().initUser();
                UserInstance.getInstance().setConnected(true);

                StageManager.getInstance().displayMainPage(UserInstance.getInstance());

            }
            else {
                setInfoText("Token incorrect. Re-essayez.");
            }
        }
    }

    public void displayRegister() {
        StageManager.getInstance().loadRootlessPage(dotenv.get("GLOBAL_REGISTER"));
    }
}
