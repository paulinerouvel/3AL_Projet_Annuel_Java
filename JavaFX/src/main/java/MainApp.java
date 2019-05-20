import java.io.IOException;

import controllers.LoginController;
import controllers.RootLayoutController;
import javafx.scene.layout.AnchorPane;
import services.UserInstance;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class MainApp extends Application {

    private UserInstance instance = new UserInstance();
    private Stage primaryStage;
    private AnchorPane rootLayout;

    /**
     * Constructor
     */
    public MainApp() {

    }

    /**
     * Entry point, gets primaryStage created by JavaFX
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("WasteMart");
        primaryStage.setResizable(false);

        initRootLayout();
        showLogin();
    }

    /**
     * Calls start() internally
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
             // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("views/RootLayout.fxml"));
            rootLayout = loader.load();

            RootLayoutController controller = loader.getController();
            controller.setInstance(instance);

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the login overview inside the root layout.
     */
    public void showLogin() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("views/Login.fxml"));
            AnchorPane Login = loader.load();

            LoginController controller = loader.getController();
            controller.setInstance(instance);

            // Set person overview into the center of root layout.
            rootLayout.getChildren().add(Login);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }


}