package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.models.User;
import fr.wastemart.maven.javaclient.models.UserCategory;
import fr.wastemart.maven.javaclient.services.Details.Detail;
import fr.wastemart.maven.javaclient.services.Details.UserDetail;
import fr.wastemart.maven.javaclient.services.Logger;
import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static fr.wastemart.maven.javaclient.services.User.*;

public class AdminListUsersController extends GenericController {
    private JSONArray lists;
    private JSONArray users;
    private Integer indexOfProductSelected;
    private Integer indexOfListSelected;

    // category
    @FXML TableView<UserCategory> userCategoryTable;
    @FXML TableColumn<Object, Object> category;

    // users
    @FXML TableView<User> usersTable;
    @FXML TableColumn<Object, Object> userID;
    @FXML TableColumn<Object, Object> userFirstName;
    @FXML TableColumn<Object, Object> userLastName;
    @FXML TableColumn<Object, Object> userNumber;
    @FXML TableColumn<Object, Object> userEmail;
    @FXML TableColumn<Object, Object> userCity;
    @FXML TableColumn<Object, Object> userAddress;
    @FXML TableColumn<Object, Object> userPostalCode;

    @FXML
    Label saveLabel;

    @Override
    public void init() throws Exception {
        displayCategoryLists();
        displayUsersByCategory(lists.getJSONObject(0).getString("libelle"));
        userCategoryTable.getSelectionModel().selectFirst();
    }

    // Displays User Categories on init
    private void displayCategoryLists() throws Exception {
        userCategoryTable.getItems().clear();
        lists = fetchCategories();

        category.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        for (int i = 0; i < lists.length(); i++) {
            JSONObject list = lists.getJSONObject(i);
            UserCategory userCategory = new UserCategory(list.getString("libelle")
            );
            userCategoryTable.getItems().add(userCategory);
        }
    }

    // Displays Users on init and click
    private void displayUsersByCategory(String libelle) throws Exception {
        usersTable.getItems().clear();
        try {
            users = fetchUsersByCategory(libelle);

            if(!users.isEmpty()) {
                userID.setCellValueFactory(new PropertyValueFactory<>("id"));
                userFirstName.setCellValueFactory(new PropertyValueFactory<>("prenom"));
                userLastName.setCellValueFactory(new PropertyValueFactory<>("nom"));
                userNumber.setCellValueFactory(new PropertyValueFactory<>("tel"));
                userEmail.setCellValueFactory(new PropertyValueFactory<>("mail"));
                userCity.setCellValueFactory(new PropertyValueFactory<>("ville"));
                userAddress.setCellValueFactory(new PropertyValueFactory<>("adresse"));
                userPostalCode.setCellValueFactory(new PropertyValueFactory<>("codePostal"));

                for (int i = 0; i < users.length(); i++) {
                    JSONObject user = users.getJSONObject(i);
                    //User userElement = fr.wastemart.maven.javaclient.services.User.jsonToUser(user);
                    User userElement = jsonToUser(user);

                    usersTable.getItems().add(userElement);
                }
            } else {
                setInfoText("There is no Users in this category");
            }
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    @FXML
    public void clickItem() {
        refreshSelectedIndices();
        if(indexOfListSelected != -1){
            try {
                displayUsersByCategory(lists.getJSONObject(indexOfListSelected).getString("libelle"));
            } catch (Exception e) {
                Logger.getInstance().reportError(e);
                setInfoErrorOccurred();
            }
        }
    }

    @FXML
    public void modifyUser() {
        UserDetail User = new UserDetail(usersTable.getSelectionModel().getSelectedItem());

        List<Detail> detailList = new ArrayList<>();
        detailList.add(User);

        StageManager.getInstance().loadPageWithDetails(dotenv.get("SHARED_DETAILS_CUSTOMER"), UserInstance.getInstance(), detailList);
    }

    @FXML
    public void contactUser() {

    }

    public void refreshSelectedIndices() {
        this.indexOfProductSelected = usersTable.getSelectionModel().getSelectedIndex();
        this.indexOfListSelected = userCategoryTable.getSelectionModel().getSelectedIndex();
    }

    // Return button
    public void displayMainPage() {
        StageManager.getInstance().displayMainPage(UserInstance.getInstance());
    }

}