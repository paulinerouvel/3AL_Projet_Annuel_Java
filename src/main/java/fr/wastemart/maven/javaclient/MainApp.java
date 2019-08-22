package fr.wastemart.maven.javaclient;

import fr.wastemart.maven.javaclient.services.StageManager;
import javafx.application.Application;
import javafx.stage.Stage;


public class MainApp extends Application {
    private Stage stage;

    /**
     * Calls start() internally
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Entry point, gets primaryStage created by JavaFX
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        this.stage.setTitle("WasteMart");
        stage.setResizable(false);

        StageManager.getInstance().setStage(stage);
        StageManager.getInstance().loadLoginPage(null,null);
    }
}