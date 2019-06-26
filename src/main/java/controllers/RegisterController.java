package controllers;

import javafx.event.ActionEvent;
import services.UserInstance;

public class RegisterController {

    private StageManager stageManager;
    private UserInstance instance;

    public void setStageManager(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    public void displayLoginPage(ActionEvent actionEvent) throws Exception {
        StageManager.loadRootlessPage(actionEvent, "/views/Login.fxml");
    }
}
