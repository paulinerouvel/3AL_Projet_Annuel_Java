package fr.wastemart.maven.javaclient;

import fr.wastemart.maven.javaclient.services.StageManager;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends Application {
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
        primaryStage.setTitle("WasteMart");
        primaryStage.getIcons().add(new Image("/fr.wastemart.maven.javaclient/images/WasteMart.ico"));
        primaryStage.setResizable(false);

        StageManager.getInstance().setStage(primaryStage);
        StageManager.getInstance().loadLoginPage(null);
    }
}