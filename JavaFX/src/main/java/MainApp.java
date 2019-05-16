import java.io.IOException;
import java.util.Stack;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
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

    // Point d'entrée, récupère le primaryStage créé par JavaFX
    @Override
    public void start(Stage primaryStage) throws Exception {
        /*
        Code pour application JavaFX.
        (Stage, scene, scene graph)
        */
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("WasteMart");

        initRootLayout();

        showLogin();
    }

    // Appelle start() en interne
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

            // This is the usual method
            //Parent root = FXMLLoader.load(getClass().getResource("views/RootLayout.fxml"));
            //Scene scene = new Scene(root,600,400);


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


}