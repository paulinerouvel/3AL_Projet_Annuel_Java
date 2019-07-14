package controllers;

import javafx.beans.binding.ListBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
import models.*;
import org.json.JSONArray;
import org.json.JSONObject;
import services.UserInstance;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static services.Product.deleteProduct;


public class UserListController {
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
            lists = services.UserCategories.fetchCategories();


            System.out.println("Listdata =" + lists);

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
            users = services.UserInstance.fetchUsersByCategory(libelle);

            System.out.println("Productdata =" + users);

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
                        ZonedDateTime.parse(user.getString("dateDeNaissance")).toLocalDate(),
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
            System.out.println("Je recharge la catégorie : " + lists.getJSONObject(indexOfListSelected).getString("libelle") );
            displayUsersByCategory(lists.getJSONObject(indexOfListSelected).getString("libelle"));
        }
    }

    @FXML
    public void modifyUser(MouseEvent event) {
        // UPDATE entrepot du produit

        System.out.println("id du user à modifier : " + usersTable.getSelectionModel().getSelectedItem().getId());
        StageManager.loadPageCustomerDetailPage(event, "/views/RootLayout.fxml", "/views/CustomerDetail.fxml", instance, usersTable.getSelectionModel().getSelectedItem().getId());

    }

    public void refreshSelectedIndices() {
        this.indexOfProductSelected = usersTable.getSelectionModel().getSelectedIndex();
        this.indexOfListSelected = userCategoryTable.getSelectionModel().getSelectedIndex();
    }


    // Return button
    public void displayMainPage(ActionEvent actionEvent) throws Exception {
        StageManager.displayMainPage(instance, actionEvent);
    }

    public UserInstance getInstance() {
        return instance;
    }

    public void setInstance(UserInstance instance) {
        this.instance = instance;
    }

}
