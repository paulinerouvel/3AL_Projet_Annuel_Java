package fr.wastemart.maven.javaclient.services;

import fr.wastemart.maven.javaclient.controllers.GenericController;
import fr.wastemart.maven.javaclient.controllers.GlobalLoginController;
import fr.wastemart.maven.javaclient.controllers.GlobalRootLayoutController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class StageManager {
    private FXMLLoader loader;
    private BorderPane rootLayout;
    private AnchorPane mainPane;

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

    // Loads a page with root
    public void loadPage(ActionEvent actionEvent, String mainView, UserInstance instance) {
        if(userInstanceIsValid(instance)){
            loadRootlessPage(actionEvent, "/fr.wastemart.maven.javaclient/views/GlobalLogin.fxml");
        }
        try {
            GenericController genericController = loadController(actionEvent, mainView, instance);

            try {
                genericController.init();
            } catch (Exception e) {
                Logger.getInstance().reportError(e);
                e.printStackTrace();
                genericController.initFail();
            }

            // Display the Page
            rootLayout.setCenter(mainPane);
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }
    }

    // Loads a page with details and root
    public void loadPageWithDetails(ActionEvent actionEvent, String mainView, UserInstance instance, Integer data) {
        if(userInstanceIsValid(instance)) {
            loadRootlessPage(actionEvent, "/fr.wastemart.maven.javaclient/views/GlobalLogin.fxml");
        }

        try {
            GenericController genericController = loadController(actionEvent, mainView, instance);

            try {
                genericController.init(data);
            } catch (Exception e) {
                Logger.getInstance().reportError(e);
            }

            // Display the Page
            rootLayout.setCenter(mainPane);
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }
    }

    // Loads a page without root (register, login)
    public void loadRootlessPage(ActionEvent actionEvent, String mainView) {
        //instance.disconnect();
        Stage stageNodeRoot = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        try {
            GenericController genericController = loadController(actionEvent, mainView, null);
            try {
                genericController.init();
            } catch (Exception e) {
                Logger.getInstance().reportError(e);
                genericController.initFail();
            }

            showBorderPane(stageNodeRoot, mainPane);
            stageNodeRoot.show();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }
    }

    // Loads login page from the menu bar
    public void loadLoginPageFromMenuBar(UserInstance instance, MenuBar menuBar){
        try {
            instance.disconnect();

            loader = new FXMLLoader();
            loader.setLocation(StageManager.class.getResource("/fr.wastemart.maven.javaclient/views/GlobalLogin.fxml"));
            AnchorPane rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);

            Stage stageNodeRoot = (Stage) menuBar.getScene().getWindow();

            GlobalLoginController globalLoginController = loader.getController();

            try {
                globalLoginController.init("Successfully Disconnected");
            } catch (Exception e) {
                Logger.getInstance().reportError(e);
            }

            stageNodeRoot.setScene(scene);
            stageNodeRoot.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Loads a controller from ressource with fxml (with root if there is instance)
    private GenericController loadController(ActionEvent actionEvent, String mainView, UserInstance instance) throws Exception {
        // Display the Root Layout if there is an instance
        if(instance != null){
            loadRootLayoutController(instance);
            Stage stageNodeRoot = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            showBorderPane(stageNodeRoot, rootLayout);
        }

        mainPane = (AnchorPane) loadResource(mainView);
        return loader.getController();
    }

    // Loads the rootLayout controller
    private void loadRootLayoutController(UserInstance instance){
        // Load the Root Layout fxml into local variable
        rootLayout = new BorderPane();

        try {
            rootLayout = (BorderPane) loadResource("/fr.wastemart.maven.javaclient/views/GlobalRootLayout.fxml");

            // Set user instance of the Root Layout
            GlobalRootLayoutController globalRootLayoutController = loader.getController();

            globalRootLayoutController.init();

        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }
    }

    // Loads resource from fxml
    private Pane loadResource(String fxml) throws Exception {
        loader = new FXMLLoader();
        loader.setLocation(StageManager.class.getResource(fxml));
        return loader.load();

    }

    private void showBorderPane(Stage stage, Parent rootLayout) throws Exception {
        // Show the scene containing the root layout.
        Scene rootScene = new Scene(rootLayout);
        stage.setScene(rootScene);
        stage.show();

    }

    public void displayMainPage(UserInstance userInstance, ActionEvent actionEvent) {
        if (userInstance.getTokenUserCategory().equals(4)) {
            loadPage(actionEvent,
                    "/fr.wastemart.maven.javaclient/views/EmployeeMain.fxml",
                    userInstance);
        } else if (userInstance.getTokenUserCategory().equals(5)) {
            loadPage(actionEvent,
                    "/fr.wastemart.maven.javaclient/views/AdminMain.fxml",
                    userInstance);
        } else if (userInstance.getTokenUserCategory().equals(2)) {
            loadPage(actionEvent,
                    "/fr.wastemart.maven.javaclient/views/ProfessionalMain.fxml",
                    userInstance);
        }
    }

    public Boolean userInstanceIsValid(UserInstance instance){
        return instance.tokenIsValid() && instance.getUser().getEstValide().equals(1);
    }

}
