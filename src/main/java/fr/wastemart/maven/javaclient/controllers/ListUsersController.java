package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.models.User;
import fr.wastemart.maven.javaclient.models.UserCategory;
import fr.wastemart.maven.javaclient.services.Details.Detail;
import fr.wastemart.maven.javaclient.services.Details.IntegerDetail;
import fr.wastemart.maven.javaclient.services.Details.StringDetail;
import fr.wastemart.maven.javaclient.services.Details.UserDetail;
import fr.wastemart.maven.javaclient.services.Logger;
import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static fr.wastemart.maven.javaclient.services.User.*;

public class ListUsersController extends GenericController {
    private JSONArray lists;
    private JSONArray users;
    private Integer indexOfUserSelected;
    private Integer indexOfCategorySelected;

    // category
    @FXML TableView<UserCategory> categoryTable;
    @FXML TableColumn<Object, Object> category;

    // users
    @FXML TableView<User> usersTable;

    @FXML TableView<User> usersTableRegistration;

    @FXML TableColumn<Object, Object> userLibelle;
    @FXML TableColumn<Object, Object> userFirstName;
    @FXML TableColumn<Object, Object> userLastName;
    @FXML TableColumn<Object, Object> userBirthDate;

    @FXML TableColumn<Object, Object> userEmail;
    @FXML TableColumn<Object, Object> userNumber;
    @FXML TableColumn<Object, Object> userAddress;
    @FXML TableColumn<Object, Object> userCity;
    @FXML TableColumn<Object, Object> userPostalCode;

    @FXML TableColumn<Object, Object> userPseudo;
    @FXML TableColumn<Object, Object> userSiret;
    @FXML TableColumn<Object, Object> userTaille;
    @FXML TableColumn<Object, Object> userDesc;

    @FXML
    Label saveLabel;

    @Override
    public void init() throws Exception {

        if(usersTable == null){
            usersTable = usersTableRegistration;
        }


        displayCategoryLists();
        displayUsersByCategory(lists.getJSONObject(0).getString("libelle"), 1);
        categoryTable.getSelectionModel().selectFirst();
    }

    // Displays User Categories on init
    private void displayCategoryLists() throws Exception {
        categoryTable.getItems().clear();
        lists = fetchCategories();

        category.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        for (int i = 0; i < lists.length(); i++) {
            JSONObject list = lists.getJSONObject(i);
            UserCategory userCategory = new UserCategory(list.getString("libelle")
            );
            categoryTable.getItems().add(userCategory);
        }
    }

    // Displays Users on init and clickCategory
    private void displayUsersByCategory(String libelle, int idCategory) throws Exception {


        usersTable.getItems().clear();
        try {


            System.out.println(usersTable.getId());
            if(usersTable.getId().equals( "usersTableRegistration")){
                System.out.println("Registration !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                users = fetchInvalidUsersByCategory(libelle);
            }
            else{
                users = fetchUsersByCategory(libelle);
            }




            if(!users.isEmpty()) {



                if(idCategory == 0 || idCategory == 1){


                    userLibelle.setCellValueFactory(new PropertyValueFactory<>("libelle"));


                    userEmail.setCellValueFactory(new PropertyValueFactory<>("mail"));
                    userNumber.setCellValueFactory(new PropertyValueFactory<>("tel"));
                    userAddress.setCellValueFactory(new PropertyValueFactory<>("adresse"));
                    userCity.setCellValueFactory(new PropertyValueFactory<>("ville"));

                    userPostalCode.setCellValueFactory(new PropertyValueFactory<>("codePostal"));
                    userPseudo.setCellValueFactory(new PropertyValueFactory<>("pseudo"));
                    userSiret.setCellValueFactory(new PropertyValueFactory<>("siret"));
                    userTaille.setCellValueFactory(new PropertyValueFactory<>("tailleOrganisme"));
                    userDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));


                    for (int i = 0; i < users.length(); i++) {
                        JSONObject user = users.getJSONObject(i);
                        //User userElement = fr.wastemart.maven.javaclient.services.User.jsonToUser(user);
                        User userElement = jsonToUser(user);

                        usersTable.getItems().add(userElement);

                        setInfoText("");
                    }
                }

                else{



                    userLibelle.setCellValueFactory(new PropertyValueFactory<>(""));
                    userSiret.setCellValueFactory(new PropertyValueFactory<>(""));
                    userTaille.setCellValueFactory(new PropertyValueFactory<>(""));
                    userDesc.setCellValueFactory(new PropertyValueFactory<>(""));
                    userTaille.setCellValueFactory(new PropertyValueFactory<>(""));

                    userFirstName.setCellValueFactory(new PropertyValueFactory<>("prenom"));
                    userLastName.setCellValueFactory(new PropertyValueFactory<>("nom"));
                    userBirthDate.setCellValueFactory(new PropertyValueFactory<>("dateDeNaissance"));

                    userNumber.setCellValueFactory(new PropertyValueFactory<>("tel"));
                    userEmail.setCellValueFactory(new PropertyValueFactory<>("mail"));
                    userCity.setCellValueFactory(new PropertyValueFactory<>("ville"));
                    userAddress.setCellValueFactory(new PropertyValueFactory<>("adresse"));
                    userPostalCode.setCellValueFactory(new PropertyValueFactory<>("codePostal"));
                    userPseudo.setCellValueFactory(new PropertyValueFactory<>("pseudo"));

                    for (int i = 0; i < users.length(); i++) {
                        JSONObject user = users.getJSONObject(i);
                        //User userElement = fr.wastemart.maven.javaclient.services.User.jsonToUser(user);


                        User userElement = jsonToUser(user);

                        usersTable.getItems().add(userElement);

                        setInfoText("");
                    }
                }

            } else {
                setInfoText("Aucun utilisateur dans cette catégorie");
            }
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    @FXML
    public void clickCategory() {
        refreshSelectedIndices();
        if(indexOfCategorySelected != -1){

            System.out.println(indexOfCategorySelected);
            try {
                displayUsersByCategory(lists.getJSONObject(indexOfCategorySelected).getString("libelle"), indexOfCategorySelected);
            } catch (Exception e) {
                Logger.getInstance().reportError(e);
                setInfoErrorOccurred();
            }
        }
    }

    @FXML
    private void displayAddUser() {
        clearInfoText();


        try{

            refreshSelectedIndices();

            List<Detail> details = new ArrayList<Detail>();


            details.add(new StringDetail("add"));
            StageManager.getInstance().loadExtraPageWithDetails(dotenv.get("SHARED_DETAILS_USER"), details);

            displayUsersByCategory( categoryTable.getSelectionModel().getSelectedItem().getLibelle(), indexOfCategorySelected);


            } catch (Exception e) {
                Logger.getInstance().reportError(e);
                setInfoErrorOccurred();
            }

    }

    @FXML
    private void displayModifyUser() {
        clearInfoText();
        refreshSelectedIndices();

        System.out.println(indexOfUserSelected);


        try{



            if(indexOfUserSelected != -1) {

                User selectedUser = jsonToUser(users.getJSONObject(indexOfUserSelected));

                List<Detail> details = new ArrayList<Detail>();


                details.add(new StringDetail("modify"));
                details.add(new UserDetail(selectedUser));
                StageManager.getInstance().loadExtraPageWithDetails(dotenv.get("SHARED_DETAILS_USER"), details);

                displayUsersByCategory( categoryTable.getSelectionModel().getSelectedItem().getLibelle(), indexOfCategorySelected);

            } else {
                setInfoText("Veuillez sélectionner un utilisateur");
            }


        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }




    }

    @FXML
    private void deleteUser() {
        clearInfoText();

        try {
            refreshSelectedIndices();

            if (indexOfUserSelected != -1){
                if(fr.wastemart.maven.javaclient.services.User.deleteUser(users.getJSONObject(indexOfUserSelected).getInt("id"), UserInstance.getInstance().getTokenValue())){
                    setInfoText("Utilisateur supprimé");
                } else {
                    setInfoErrorOccurred();
                }
            }

            displayUsersByCategory( categoryTable.getSelectionModel().getSelectedItem().getLibelle(), indexOfCategorySelected);
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    @FXML
    public void contactUser() {

        try {
            refreshSelectedIndices();

            if (indexOfUserSelected != -1) {
                StringDetail mail = new StringDetail(usersTable.getSelectionModel().getSelectedItem().getMail());

                List<Detail> contactDetails = new ArrayList<>();
                contactDetails.add(mail);

                StageManager.getInstance().loadExtraPageWithDetails(dotenv.get("SHARED_DETAILS_CONTACT"), contactDetails);
            }
            else{
                setInfoText("Veuillez sélectionner un utilisateur");
            }

        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }

    }

    @FXML
    public void validateUser(){
        try {
            refreshSelectedIndices();

            if (indexOfUserSelected != -1) {

                User user = new User(usersTable.getSelectionModel().getSelectedItem().getId(),
                        usersTable.getSelectionModel().getSelectedItem().getLibelle(),
                        null,
                        usersTable.getSelectionModel().getSelectedItem().getNom(),
                        usersTable.getSelectionModel().getSelectedItem().getPrenom(),
                        usersTable.getSelectionModel().getSelectedItem().getMail(),
                        usersTable.getSelectionModel().getSelectedItem().getTel(),
                        usersTable.getSelectionModel().getSelectedItem().getAdresse(),
                        usersTable.getSelectionModel().getSelectedItem().getVille(),
                        usersTable.getSelectionModel().getSelectedItem().getCodePostal(),
                        usersTable.getSelectionModel().getSelectedItem().getPseudo(),
                        usersTable.getSelectionModel().getSelectedItem().getMdp(),
                        usersTable.getSelectionModel().getSelectedItem().getPhoto(),
                        usersTable.getSelectionModel().getSelectedItem().getDesc(),
                        usersTable.getSelectionModel().getSelectedItem().getTailleOrganisme(),
                        true,
                        usersTable.getSelectionModel().getSelectedItem().getSiret(),
                        usersTable.getSelectionModel().getSelectedItem().getDateDeNaissance(),
                        0);



                if(fr.wastemart.maven.javaclient.services.User.updateUser(user, UserInstance.getInstance().getTokenValue())){
                    setInfoText("Utilisateur validé");
                } else {
                    setInfoErrorOccurred();
                }

                displayUsersByCategory( categoryTable.getSelectionModel().getSelectedItem().getLibelle(), indexOfCategorySelected);
            }
            else{
                setInfoText("Veuillez sélectionner un utilisateur");
            }

        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    @FXML
    public void refuseUser(){
        try {
            refreshSelectedIndices();

            if (indexOfUserSelected != -1) {

                /*User user = new User(usersTable.getSelectionModel().getSelectedItem().getId(),
                        usersTable.getSelectionModel().getSelectedItem().getLibelle(),
                        null,
                        usersTable.getSelectionModel().getSelectedItem().getNom(),
                        usersTable.getSelectionModel().getSelectedItem().getPrenom(),
                        usersTable.getSelectionModel().getSelectedItem().getMail(),
                        usersTable.getSelectionModel().getSelectedItem().getTel(),
                        usersTable.getSelectionModel().getSelectedItem().getAdresse(),
                        usersTable.getSelectionModel().getSelectedItem().getVille(),
                        usersTable.getSelectionModel().getSelectedItem().getCodePostal(),
                        usersTable.getSelectionModel().getSelectedItem().getPseudo(),
                        usersTable.getSelectionModel().getSelectedItem().getMdp(),
                        usersTable.getSelectionModel().getSelectedItem().getPhoto(),
                        usersTable.getSelectionModel().getSelectedItem().getDesc(),
                        usersTable.getSelectionModel().getSelectedItem().getTailleOrganisme(),
                        false,
                        usersTable.getSelectionModel().getSelectedItem().getSiret(),
                        usersTable.getSelectionModel().getSelectedItem().getDateDeNaissance(),
                        0);*/


                /*if(fr.wastemart.maven.javaclient.services.User.updateUser(user, UserInstance.getInstance().getTokenValue())){
                    setInfoText("Utilisateur refusé");
                } else {
                    setInfoErrorOccurred();
                }*/

                if(fr.wastemart.maven.javaclient.services.User.deleteUser(users.getJSONObject(indexOfUserSelected).getInt("id"), UserInstance.getInstance().getTokenValue())){
                    setInfoText("Utilisateur refusé et supprimé");
                } else {
                    setInfoErrorOccurred();
                }

                displayUsersByCategory( categoryTable.getSelectionModel().getSelectedItem().getLibelle(), indexOfCategorySelected);
            }
            else{
                setInfoText("Veuillez sélectionner un utilisateur");
            }

        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    public void refreshSelectedIndices() {
        this.indexOfUserSelected = usersTable.getSelectionModel().getSelectedIndex();
        this.indexOfCategorySelected = categoryTable.getSelectionModel().getSelectedIndex();
    }

    // Return button
    public void displayMainPage() {
        StageManager.getInstance().displayMainPage(UserInstance.getInstance());
    }


}