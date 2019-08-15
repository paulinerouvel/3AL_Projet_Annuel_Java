package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.models.User;
import fr.wastemart.maven.javaclient.models.UserCategory;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.json.JSONArray;
import org.json.JSONObject;

public class UserListController extends GenericController {
    private UserInstance instance;
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

    public void init(){

        try{
            displayCategoryLists();
            displayUsersByCategory(lists.getJSONObject(0).getString("libelle"));
            userCategoryTable.getSelectionModel().selectFirst();
        }
        catch (Exception ex) {
            System.out.println("Problème init" + ex);
        }
    }

    private void displayCategoryLists() {

        try {
            userCategoryTable.getItems().clear();
            lists = fr.wastemart.maven.javaclient.services.UserCategories.fetchCategories();



            category.setCellValueFactory(new PropertyValueFactory<>("libelle"));
            for (int i = 0; i < lists.length(); i++) {
                JSONObject list = lists.getJSONObject(i);
                UserCategory userCategory = new UserCategory(list.getString("libelle")
                );
                userCategoryTable.getItems().add(userCategory);

            }

        }
        catch (Exception ex) {
            System.out.println("displayCategoryLists" + ex);
        }


    }


    private void displayUsersByCategory(String libelle) {
        try {
            usersTable.getItems().clear();
            users = UserInstance.fetchUsersByCategory(libelle);

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
                User userElement = new User(user.getInt("Utilisateur_id"),
                        user.getString("libelle"),
                        user.getInt("Categorie_utilisateur_id"),
                        user.getString("nom"),
                        user.getString("prenom"),
                        user.getString("mail"),
                        user.getString("tel"),
                        user.getString("adresse"),
                        user.getString("ville"),
                        user.getInt("codePostal"),
                        user.getString("pseudo"),
                        user.getString("mdp"),
                        user.getString("photo"),
                        user.getString("desc"),
                        user.getInt("tailleOrganisme"),
                        user.getInt("estValide"),
                        user.getString("siret"),
                        user.getString("dateDeNaissance"),
                        user.getInt("nbPointsSourire")

                );
                usersTable.getItems().add(userElement);
            }
        }
        catch(Exception ex) {
            System.out.println("probleme displayUsersByCategory " + ex);
        }

    }

    @FXML
    public void clickItem(MouseEvent event) {
        refreshSelectedIndices();

        if(indexOfListSelected != -1){
            displayUsersByCategory(lists.getJSONObject(indexOfListSelected).getString("libelle"));
        }
    }

    @FXML
    public void modifyUser(MouseEvent event) {
        // UPDATE entrepot du produit
        StageManager.getInstance().loadPageCustomerDetailPage(event, "/fr.wastemart.maven.javaclient/views/RootLayout.fxml", "/fr.wastemart.maven.javaclient/views/CustomerDetail.fxml", instance, usersTable.getSelectionModel().getSelectedItem().getId());
    }

    public void refreshSelectedIndices() {
        this.indexOfProductSelected = usersTable.getSelectionModel().getSelectedIndex();
        this.indexOfListSelected = userCategoryTable.getSelectionModel().getSelectedIndex();
    }


    // Return button
    public void displayMainPage(ActionEvent actionEvent) {
        StageManager.getInstance().displayMainPage(instance, actionEvent);
    }

    public UserInstance getInstance() {
        return instance;
    }

    public void setInstance(UserInstance instance) {
        this.instance = instance;
    }

}