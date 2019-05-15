import java.io.IOException;
import java.util.Stack;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private Scene scene;

    /*/**
     * The data as an observable list of Persons.
     */
    /*private ObservableList<Person> personData = FXCollections.observableArrayList();*/

    /**
     * Constructor
     */
    public MainApp() {

    }


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("WasteMart");

        initRootLayout();

        showLogin();
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

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Button calculateButton;
    /**
     * Shows the person overview inside the root layout.
     */
    public void showLogin() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("views/Login.fxml"));
            BorderPane Login = loader.load();


            /*calculateButton = new Button();
            calculateButton.setText("Lancer le truc");
            calculateButton.setOnAction((ActionEvent t) -> doAuthenticate());
            StackPane center = new StackPane();
            center.getChildren().add(calculateButton);

            final BorderPane root = new BorderPane();
            root.setCenter(center);
            scene = new Scene(root, 300, 250);
            primaryStage.setScene(scene);
            primaryStage.show();*/

            // Set person overview into the center of root layout.
            rootLayout.setCenter(Login);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}