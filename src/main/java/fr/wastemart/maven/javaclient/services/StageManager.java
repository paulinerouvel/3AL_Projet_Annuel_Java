package fr.wastemart.maven.javaclient.services;

import fr.wastemart.maven.javaclient.controllers.GenericController;
import fr.wastemart.maven.javaclient.controllers.GlobalLoginController;
import fr.wastemart.maven.javaclient.controllers.GlobalRootLayoutController;
import fr.wastemart.maven.javaclient.services.Details.Detail;
import fr.wastemart.maven.javaclient.services.Details.StringDetail;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class StageManager {
    private Dotenv dotenv;
    private FXMLLoader loader;

    private Stage stage;
    private BorderPane rootLayout;
    private AnchorPane mainPane;

    /** Constructeur privé */
    private StageManager() {
        //String envFile = System.getProperty("user.dir")+"/src/main/resources/fr.wastemart.maven.javaclient/";
        String envFile = System.getProperty("user.dir");
        dotenv = Dotenv.configure()
                .directory(envFile)
                .load();
    }

    /** Instance unique */
    private static StageManager INSTANCE;

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
            loadRootlessPage(dotenv.get("GLOBAL_LOGIN"));
        }
        try {
            GenericController genericController = loadController(mainView, instance);

            try {
                genericController.init();
            } catch (Exception e) {
                Logger.getInstance().reportError(e);
                genericController.initFail();
            }

            // Display the Page
            rootLayout.setCenter(mainPane);
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }
    }

    // Loads a page with detail and root
    public void loadPage(String mainView, UserInstance instance, List<Detail> details) {
        if(userInstanceIsValid(instance)) {
            loadRootlessPage(dotenv.get("GLOBAL_LOGIN"));
        }

        try {
            GenericController genericController = loadController(mainView, instance);

            try {
                genericController.init(details);
            } catch (Exception e) {
                Logger.getInstance().reportError(e);
            }

            // Display the Page
            rootLayout.setCenter(mainPane);
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }
    }

    public void loadExtraPageWithDetails(String mainView, List<Detail> details) {
        try {
            mainPane = (AnchorPane) loadResource(mainView);
            GenericController genericController = loader.getController();

            genericController.init(details);

            // Display the Page
            Scene newScene = new Scene(mainPane);
            Stage inputStage = new Stage();

            inputStage.initOwner(getStage());
            inputStage.setResizable(false);
            inputStage.setScene(newScene);
            inputStage.showAndWait();
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

            showPane(mainPane);
            getStage().show();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }
    }


    // Loads a page without root (register) + list detail
    public void loadRootlessPage(String mainView, List<Detail> details) {
        try {
            GenericController genericController = loadController(mainView,  null);
            try {
                genericController.init(details);
            } catch (Exception e) {
                Logger.getInstance().reportError(e);
                genericController.initFail();
            }

            showPane(mainPane);
            getStage().show();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }
    }

    // Loads login page from the menu bar or beginning
    public void loadLoginPage(UserInstance userInstance){
        try {
            GlobalLoginController globalLoginController = (GlobalLoginController) loadController(dotenv.get("GLOBAL_LOGIN"), userInstance);

            List<Detail> loginMessage = new ArrayList<>();

            if(userInstance != null) {
                userInstance.disconnect();
                loginMessage.add(new StringDetail("Déconnexion réussie"));
                globalLoginController.init(loginMessage);
            } else {
                loginMessage.add(new StringDetail("Veuillez vous connecter"));
                globalLoginController.init(loginMessage);
            }

            showPane(mainPane);
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
            showPane(rootLayout);
        }

        mainPane = (AnchorPane) loadResource(mainView);
        return loader.getController();
    }

    // Loads the rootLayout controller
    private void loadRootLayoutController(){
        rootLayout = new BorderPane();

        try {
            // Load the Root Layout fxml
            rootLayout = (BorderPane) loadResource(dotenv.get("GLOBAL_ROOTLAYOUT"));
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

    private void showPane(Parent parent) throws Exception {
        // Show the scene containing the root layout.
        Scene rootScene = new Scene(parent);
        getStage().setScene(rootScene);
        getStage().show();
    }

    public void displayMainPage(UserInstance userInstance) {
        if (userInstance.getTokenUserCategory().equals(4)) {
            loadPage(dotenv.get("EMPLOYEE_MAIN"), userInstance);
        } else if (userInstance.getTokenUserCategory().equals(5)) {
            loadPage(dotenv.get("ADMIN_MAIN"), userInstance);
        } else if (userInstance.getTokenUserCategory().equals(2)) {
            loadPage(dotenv.get("PRO_MAIN"), userInstance);
        }
    }

    public Boolean userInstanceIsValid(UserInstance instance) {
        return instance.tokenIsValid() && instance.getUser().getEstValide().equals(1);
    }

    public void setCursor(Cursor type){
        mainPane.setCursor(type);
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Dotenv getDotenv() {
        return dotenv;
    }
}
