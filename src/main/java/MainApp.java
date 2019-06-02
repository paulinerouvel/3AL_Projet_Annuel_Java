import java.io.IOException;

import controllers.LoginController;
import controllers.RootLayoutController;
import controllers.StageManager;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import services.UserInstance;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class MainApp extends Application {


    private AnchorPane login;
    private Stage stage;
    private Scene actualScene;





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
            loader.setLocation(MainApp.class.getResource("views/Login.fxml"));
            login = loader.load();

            LoginController controller = loader.getController();
            controller.setStageManager(new StageManager());

            // Set person overview into the center of root layout.
            actualScene = new Scene(login);
            stage.setScene(actualScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}