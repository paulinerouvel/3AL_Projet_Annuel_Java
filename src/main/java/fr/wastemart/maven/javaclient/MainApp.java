package fr.wastemart.maven.javaclient;

import fr.wastemart.maven.javaclient.controllers.GlobalLoginController;
import fr.wastemart.maven.javaclient.services.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;


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

        showLogin();

    }

    /**
     * Shows the login overview inside the root layout.
     */
    public void showLogin() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fr.wastemart.maven.javaclient/views/GlobalLogin.fxml"));
            AnchorPane login = loader.load();

            GlobalLoginController globalLoginController = loader.getController();
            globalLoginController.init("Veuillez vous connecter");

            // Set person overview into the center of root layout.
            Scene actualScene = new Scene(login);
            stage.setScene(actualScene);
            stage.show();
        } catch (IOException e) {
            Logger.getInstance().reportError(e);
            //e.printStackTrace();
        }
    }
}