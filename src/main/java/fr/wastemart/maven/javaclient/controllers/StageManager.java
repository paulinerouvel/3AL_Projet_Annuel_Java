package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class StageManager {
    private FXMLLoader loader;
    private BorderPane parentRootLayout;
    private AnchorPane parentMain;

    /** Constructeur privé */
    private StageManager(){}

    /** Instance unique pré-initialisée */
    private static StageManager INSTANCE = new StageManager();

    /** Point d'accès pour l'instance unique du Singleton */
    public static StageManager getInstance(){
        if (INSTANCE == null){
            INSTANCE = new StageManager();
        }
        return INSTANCE;
    }

    void loadPage(ActionEvent actionEvent, String mainView, UserInstance instance){
        //if(userInstanceIsValid(instance)){
        //    loadRootlessPage(actionEvent, "/fr.wastemart.maven.javaclient/views/Login.fxml");
        //} // TODO WIP uncoment
        GenericController genericController = loadController(actionEvent, "/fr.wastemart.maven.javaclient/views/RootLayout.fxml", mainView, instance);
        genericController.init(instance);

        // Display the Menu in center of Root Layout
        getParentRootLayout().setCenter(getParentMain());
    }

    void loadPageWithDetails(ActionEvent actionEvent, String mainView, UserInstance instance, Integer data) {
        //if(userInstanceIsValid(instance)){
        //    loadRootlessPage(actionEvent, "/fr.wastemart.maven.javaclient/views/Login.fxml");
        //} // TODO WIP uncoment
        GenericController genericController = loadController(actionEvent, "/fr.wastemart.maven.javaclient/views/RootLayout.fxml", mainView, instance);
        genericController.init(instance, data);

        // Display the Menu in center of Root Layout
        getParentRootLayout().setCenter(getParentMain());
    }

    public Boolean userInstanceIsValid(UserInstance instance){
        return instance.tokenIsValid() && instance.getUser().getEstValide().equals(1);
    }

    private GenericController loadController(ActionEvent actionEvent, String rootLayout, String mainView, UserInstance instance) {
        // Load the Root Layout fxml
        parentRootLayout = loadBorderPane(rootLayout);

        // Set user instance of the Root Layout
        RootLayoutController rootLayoutController = loader.getController();
        rootLayoutController.setInstance(instance);

        // Display the Root Layout
        Stage stageNodeRoot = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        if(stageNodeRoot != null){
            showBorderPane(stageNodeRoot, parentRootLayout);
        }

        // Load the Menu fxml
        parentMain = loadAnchorPane(mainView);

        return loader.getController();
    }

    // Loads a page without root (register, login)
    public void loadRootlessPage(ActionEvent actionEvent, String mainView) {
        //instance.disconnect();
        Stage stageNodeRoot = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        parentMain = loadAnchorPane(mainView);

        Class<?> controllerClassType = loader.getController().getClass();
        if(controllerClassType == RegisterController.class) {
            RegisterController registerController = loader.getController();
            registerController.init();
        } else if (controllerClassType == LoginController.class){
            LoginController loginController = loader.getController();
            loginController.setInfo("User error");
        }

        showBorderPane(stageNodeRoot, getParentMain());
        stageNodeRoot.show();

    }

    // Loads login page from the menu bar
    public void loadLoginPageFromMenuBar(UserInstance instance, MenuBar menuBar){
        try {
            instance.disconnect();

            loader = new FXMLLoader();
            loader.setLocation(StageManager.class.getResource("/fr.wastemart.maven.javaclient/views/Login.fxml"));
            AnchorPane rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);

            Stage stageNodeRoot = (Stage) menuBar.getScene().getWindow();

            LoginController loginController = loader.getController();

            stageNodeRoot.setScene(scene);
            stageNodeRoot.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Load Border Pane from fxml file.
    private BorderPane loadBorderPane(String fxml) {
        BorderPane borderPane = new BorderPane();
        try {
            loader = new FXMLLoader();
            loader.setLocation(StageManager.class.getResource(fxml));
            borderPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return borderPane;
    }


    private void showBorderPane(Stage stage, Parent rootLayout){
        // Show the scene containing the root layout.
        Scene rootScene = new Scene(rootLayout);
        stage.setScene(rootScene);
        stage.show();

    }

    // Load Border Pane from fxml file.
    private AnchorPane loadAnchorPane(String fxml) {
        AnchorPane anchorPane = new AnchorPane();
        try {
            loader = new FXMLLoader();
            loader.setLocation(StageManager.class.getResource(fxml));
            anchorPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return anchorPane;
    }

    public void displayMainPage(UserInstance userInstance, ActionEvent actionEvent) {
        if (userInstance.getTokenUserCategory().equals(4)) {
            loadPage(actionEvent,
                    "/fr.wastemart.maven.javaclient/views/MainEmployee.fxml",
                    userInstance);
        } else if (userInstance.getTokenUserCategory().equals(5)) {
            loadPage(actionEvent,
                    "/fr.wastemart.maven.javaclient/views/MainAdmin.fxml",
                    userInstance);
        } else if (userInstance.getTokenUserCategory().equals(2)) {
            loadPage(actionEvent,
                    "/fr.wastemart.maven.javaclient/views/MainProfessionnal.fxml",
                    userInstance);
        }
    }

    private BorderPane getParentRootLayout() {
        return parentRootLayout;
    }

    private AnchorPane getParentMain() {
        return parentMain;
    }
}
