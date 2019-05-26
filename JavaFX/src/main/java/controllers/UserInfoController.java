package controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import models.User;

public class UserInfoController {

    public void displayUserInfos(ActionEvent actionEvent, User user) throws Exception {

    }

    /*private void loadMainEmployeePage(ActionEvent actionEvent){
        // Load the Root Layout fxml
        setParentRootLayout(loadBorderPane("/views/RootLayout.fxml"));

        // Set user instance of the Root Layout
        RootLayoutController rootLayoutController = loader.getController();
        rootLayoutController.setInstance(this.instance);

        // Display the Root Layout
        Stage stageNodeRoot = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        showBorderPane(stageNodeRoot, parentRootLayout);

        // Load the Main fxml
        setParentMain(loadAnchorPane("/views/MainEmployee.fxml"));

        // Init Main Controller
        MainEmployeeController mainEmployeeController = loader.getController();
        mainEmployeeController.init(this.parentRootLayout, this.instance);

        // Display the Main in center of Root Layout
        parentRootLayout.setCenter(parentMain);

    }*/
}
