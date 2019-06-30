package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import services.UserInstance;

import java.io.IOException;

public class StageManager {
    private static FXMLLoader loader;
    private static BorderPane parentRootLayout;
    private static AnchorPane parentMain;

    public static void loadPage(ActionEvent actionEvent, String rootLayout, String mainView, UserInstance instance){
        if(!instance.tokenIsValid() || instance.getUser().getEstValide().equals(0)){
            loadRootlessPage(actionEvent, "/views/Login.fxml");

        }
        // Load the Root Layout fxml
        parentRootLayout = loadBorderPane(rootLayout);

        // Set user instance of the Root Layout
        RootLayoutController rootLayoutController = loader.getController();
        rootLayoutController.setInstance(instance);
        //-----rootLayoutController.setStageManager(this);

        // Display the Root Layout
        Stage stageNodeRoot = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        if(stageNodeRoot != null){
            showBorderPane(stageNodeRoot, parentRootLayout);
        }


        // Load the Menu fxml
        parentMain = loadAnchorPane(mainView);

        // Init Menu Controller
        Class<?> controllerClassType = loader.getController().getClass();
        if(controllerClassType == MainEmployeeController.class) {
            MainEmployeeController mainEmployeeController = loader.getController();
            //-----mainEmployeeController.setStageManager(this);
            mainEmployeeController.init(instance);

        } else if(controllerClassType == MainAdminController.class){
            MainAdminController mainAdminController = loader.getController();
            //-----mainAdminController.setStageManager(this);
            mainAdminController.init(instance);

        } else if(controllerClassType == MainProfessionnalController.class){
            MainProfessionnalController mainProfessionnalController = loader.getController();
            //-----mainProfessionnalController.setStageManager(this);
            mainProfessionnalController.init(instance);

        } else if (controllerClassType == PluginPageController.class){
            PluginPageController pluginPageController = loader.getController();
            //-----pluginPageController.setStageManager(this);
            pluginPageController.init(instance);

        } else if (controllerClassType == UserInfoController.class) {
            UserInfoController userInfoController = loader.getController();
            //-----userInfoController.setStageManager(this);
            userInfoController.setInstance(instance);
            userInfoController.init(instance);
        }


        // Display the Menu in center of Root Layout
        getParentRootLayout().setCenter(getParentMain());
    }

    // Loads a page without root (register, login)
    public static void loadRootlessPage(ActionEvent actionEvent, String mainView) {
        //instance.disconnect();
        Stage stageNodeRoot = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        parentMain = loadAnchorPane(mainView);

        Class<?> controllerClassType = loader.getController().getClass();
        if(controllerClassType == RegisterController.class) {
            RegisterController registerController = loader.getController();
            registerController.init();
        } else if (controllerClassType == LoginController.class){
            LoginController loginController = loader.getController();
            //-----loginController.setStageManager(this);
        }

        showBorderPane(stageNodeRoot, getParentMain());
        stageNodeRoot.show();

    }

    // Loads login page from the menu bar
    public static void loadLoginPageFromMenuBar(UserInstance instance, MenuBar menuBar){
        try {
            instance.disconnect();

            loader = new FXMLLoader();
            loader.setLocation(StageManager.class.getResource("/views/Login.fxml"));
            AnchorPane rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);

            Stage stageNodeRoot = (Stage) menuBar.getScene().getWindow();

            LoginController loginController = loader.getController();
            //loginController.setStageManager(this);


            stageNodeRoot.setScene(scene);
            stageNodeRoot.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Load Border Pane from fxml file.
    public static BorderPane loadBorderPane(String fxml) {
        BorderPane borderPane = new BorderPane();
        try {
            loader = new FXMLLoader();
            loader.setLocation(StageManager.class.getResource(fxml));
            borderPane = loader.load();
            //-----setLoader(loader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return borderPane;
    }


    public static void showBorderPane(Stage stage, Parent rootLayout){
        // Show the scene containing the root layout.
        Scene rootScene = new Scene(rootLayout);
        stage.setScene(rootScene);
        stage.show();

    }

    // Load Border Pane from fxml file.
    public static AnchorPane loadAnchorPane(String fxml) {
        AnchorPane anchorPane = new AnchorPane();
        try {
            loader = new FXMLLoader();
            loader.setLocation(StageManager.class.getResource(fxml));
            anchorPane = loader.load();
            //-----setLoader(loader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return anchorPane;
    }

    public static void displayMainPage(UserInstance userInstance, ActionEvent actionEvent) {
        if (userInstance.getTokenUserCategory().equals(4)) {
            StageManager.loadPage(actionEvent,
                    "/views/RootLayout.fxml",
                    "/views/MainEmployee.fxml",
                    userInstance);
        } else if (userInstance.getTokenUserCategory().equals(5)) {
            StageManager.loadPage(actionEvent,
                    "/views/RootLayout.fxml",
                    "/views/MainAdmin.fxml",
                    userInstance);
        } else if (userInstance.getTokenUserCategory().equals(2)) {
            StageManager.loadPage(actionEvent,
                    "/views/RootLayout.fxml",
                    "/views/MainProfessionnal.fxml",
                    userInstance);
        }
    }

    public static void setLoader(FXMLLoader loader) {
        loader = loader;
    }

    public static FXMLLoader getLoader() {
        return loader;
    }

    public static BorderPane getParentRootLayout() {
        return parentRootLayout;
    }

    public static void setParentRootLayout(BorderPane parentRootLayout) {
        parentRootLayout = parentRootLayout;
    }

    public static AnchorPane getParentMain() {
        return parentMain;
    }

    public static void setParentMain(AnchorPane parentMain) {
        parentMain = parentMain;
    }
}
