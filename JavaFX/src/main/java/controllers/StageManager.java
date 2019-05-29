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
    private FXMLLoader loader;
    private BorderPane parentRootLayout;
    private AnchorPane parentMain;

    public void loadPage(ActionEvent actionEvent, String rootLayout, String mainView, UserInstance instance){
        if(!instance.tokenIsValid()){
            loadLoginPage(actionEvent, instance);
        }

        // Load the Root Layout fxml
        setParentRootLayout(loadBorderPane(rootLayout));

        // Set user instance of the Root Layout
        RootLayoutController rootLayoutController = getLoader().getController();
        rootLayoutController.setInstance(instance);
        rootLayoutController.setStageManager(this);

        // Display the Root Layout
        Stage stageNodeRoot = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        showBorderPane(stageNodeRoot, getParentRootLayout());

        // Load the Menu fxml
        setParentMain(loadAnchorPane(mainView));

        // Init Menu Controller
        Class<?> controllerClassType = getLoader().getController().getClass();
        if(controllerClassType == MainEmployeeController.class){

            MainEmployeeController mainEmployeeController = getLoader().getController();
            mainEmployeeController.setStageManager(this);
            mainEmployeeController.init(instance);

        } else if (controllerClassType == PluginPageController.class){
            PluginPageController pluginPageController = getLoader().getController();
            pluginPageController.setStageManager(this);
            pluginPageController.init(instance);
        }

        // Display the Menu in center of Root Layout
        getParentRootLayout().setCenter(getParentMain());

    }


    // Load Border Pane from fxml file.
    public BorderPane loadBorderPane(String fxml) {
        FXMLLoader loader = new FXMLLoader();
        BorderPane borderPane = new BorderPane();
        try {
            loader.setLocation(this.getClass().getResource(fxml));
            borderPane = loader.load();
            setLoader(loader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return borderPane;
    }


    public void showBorderPane(Stage stage, Parent rootLayout){
        // Show the scene containing the root layout.
        Scene rootScene = new Scene(rootLayout);
        stage.setScene(rootScene);
        stage.show();

    }

    // Load Border Pane from fxml file.
    public AnchorPane loadAnchorPane(String fxml) {
        FXMLLoader loader = new FXMLLoader();
        AnchorPane anchorPane = new AnchorPane();
        try {
            loader.setLocation(this.getClass().getResource(fxml));
            anchorPane = loader.load();
            setLoader(loader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return anchorPane;
    }

    public void loadLoginPage(ActionEvent actionEvent, UserInstance instance){
        try {
            instance.disconnect();
            Stage stageNodeRoot = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/Login.fxml"));
            AnchorPane rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);

            LoginController loginController = loader.getController();
            loginController.setStageManager(this);

            //Stage stageNodeRoot = (Stage) rootLayout.getScene().getWindow();

            stageNodeRoot.setScene(scene);
            stageNodeRoot.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadLoginPageFromMenuBar(UserInstance instance, MenuBar menuBar){
        try {
            instance.disconnect();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/Login.fxml"));
            AnchorPane rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);

            Stage stageNodeRoot = (Stage) menuBar.getScene().getWindow();

            LoginController loginController = loader.getController();
            loginController.setStageManager(this);


            stageNodeRoot.setScene(scene);
            stageNodeRoot.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLoader(FXMLLoader loader) {
        this.loader = loader;
    }

    public FXMLLoader getLoader() {
        return loader;
    }

    public BorderPane getParentRootLayout() {
        return parentRootLayout;
    }

    public void setParentRootLayout(BorderPane parentRootLayout) {
        this.parentRootLayout = parentRootLayout;
    }

    public AnchorPane getParentMain() {
        return parentMain;
    }

    public void setParentMain(AnchorPane parentMain) {
        this.parentMain = parentMain;
    }
}
