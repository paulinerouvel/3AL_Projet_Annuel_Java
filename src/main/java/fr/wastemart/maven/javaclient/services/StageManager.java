package fr.wastemart.maven.javaclient.services;

import fr.wastemart.maven.javaclient.controllers.GenericController;
import fr.wastemart.maven.javaclient.controllers.GlobalLoginController;
import fr.wastemart.maven.javaclient.controllers.GlobalRootLayoutController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class StageManager {
    private FXMLLoader loader;

    private Stage stage;
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
    public void loadPage(String mainView, UserInstance instance) {
        if(userInstanceIsValid(instance)){
            loadRootlessPage("/fr.wastemart.maven.javaclient/views/GlobalLogin.fxml");
        }
        try {
            GenericController genericController = loadController(mainView, instance);

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
    public void loadPageWithDetails(String mainView, UserInstance instance, Integer data) {
        if(userInstanceIsValid(instance)) {
            loadRootlessPage("/fr.wastemart.maven.javaclient/views/GlobalLogin.fxml");
        }

        try {
            GenericController genericController = loadController(mainView, instance);

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

    // Loads a page without root (register)
    public void loadRootlessPage(String mainView) {
        try {
            GenericController genericController = loadController(mainView, null);
            try {
                genericController.init();
            } catch (Exception e) {
                Logger.getInstance().reportError(e);
                genericController.initFail();
            }

            showBorderPane(mainPane);
            getStage().show();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }
    }

    // Loads login page from the menu bar or beginning
    public void loadLoginPage(UserInstance userInstance, MenuBar menuBar){
        try {
            if(userInstance != null){
                userInstance.disconnect();
            }

            GlobalLoginController globalLoginController = (GlobalLoginController) loadController("/fr.wastemart.maven.javaclient/views/GlobalLogin.fxml", userInstance);

            if(menuBar != null) {
                globalLoginController.init("Déconnexion réussie");
            } else {
                globalLoginController.init("Veuillez vous connecter");
            }

            showBorderPane(mainPane);
            getStage().show();

        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }
    }
    
    // Loads a controller from ressource with fxml (with root if there is instance)
    private GenericController loadController(String mainView, UserInstance instance) throws Exception {
        // Display the Root Layout if there is an instance
        if(instance != null){
            loadRootLayoutController();
            showBorderPane(rootLayout);
        }

        mainPane = (AnchorPane) loadResource(mainView);
        return loader.getController();
    }

    // Loads the rootLayout controller
    private void loadRootLayoutController(){
        rootLayout = new BorderPane();

        try {
            // Load the Root Layout fxml
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

    private void showBorderPane(Parent rootLayout) throws Exception {
        // Show the scene containing the root layout.
        Scene rootScene = new Scene(rootLayout);
        getStage().setScene(rootScene);
        getStage().show();

    }

    public void displayMainPage(UserInstance userInstance) {
        if (userInstance.getTokenUserCategory().equals(4)) {
            loadPage(
                    "/fr.wastemart.maven.javaclient/views/EmployeeMain.fxml",
                    userInstance);
        } else if (userInstance.getTokenUserCategory().equals(5)) {
            loadPage(
                    "/fr.wastemart.maven.javaclient/views/AdminMain.fxml",
                    userInstance);
        } else if (userInstance.getTokenUserCategory().equals(2)) {
            loadPage(
                    "/fr.wastemart.maven.javaclient/views/ProfessionalMain.fxml",
                    userInstance);
        }
    }

    public Boolean userInstanceIsValid(UserInstance instance){
        return instance.tokenIsValid() && instance.getUser().getEstValide().equals(1);
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
