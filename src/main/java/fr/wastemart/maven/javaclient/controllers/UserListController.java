package fr.wastemart.maven.javaclient.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import fr.wastemart.maven.javaclient.models.User;
import org.json.JSONArray;
import org.json.JSONObject;
import fr.wastemart.maven.javaclient.services.UserInstance;

import static fr.wastemart.maven.javaclient.services.User.jsonToUser;

import java.io.IOException;


public class UserListController {
    private UserInstance instance;
    private JSONArray users;

    private Integer indexOfUserSelected;

    @FXML
    TableView<User> usersTable;
    @FXML
    TableColumn<Object, Object> id;
    @FXML
    TableColumn<Object, Object> libelle;
    @FXML
    TableColumn<Object, Object> categorieUtilisateur;
    @FXML
    TableColumn<Object, Object> nom;
    @FXML
    TableColumn<Object, Object> prenom;
    @FXML
    TableColumn<Object, Object> dateDeNaissance;
    @FXML
    TableColumn<Object, Object> mail;
    @FXML
    TableColumn<Object, Object> tel;
    @FXML
    TableColumn<Object, Object> adresse;
    @FXML
    TableColumn<Object, Object> ville;
    @FXML
    TableColumn<Object, Object> codePostal;
    @FXML
    TableColumn<Object, Object> pseudo;
    @FXML
    TableColumn<Object, Object> photo;
    @FXML
    TableColumn<Object, Object> desc;
    @FXML
    TableColumn<Object, Object> tailleOrganisme;
    @FXML
    TableColumn<Object, Object> estValide;
    @FXML
    TableColumn<Object, Object> siret;
    @FXML
    TableColumn<Object, Object> nbPointsSourire;


    public void init(){
        displayUsers();
        usersTable.getSelectionModel().selectFirst();
    }

    private void displayUsers() {
        usersTable.getItems().clear();
        users = fr.wastemart.maven.javaclient.services.User.fetchAllUsers();


        System.out.println("Listdata =" + users);

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        libelle.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        categorieUtilisateur.setCellValueFactory(new PropertyValueFactory<>("categorieUtilisateur"));
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        dateDeNaissance.setCellValueFactory(new PropertyValueFactory<>("dateDeNaissance"));
        mail.setCellValueFactory(new PropertyValueFactory<>("mail"));
        tel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        ville.setCellValueFactory(new PropertyValueFactory<>("ville"));
        codePostal.setCellValueFactory(new PropertyValueFactory<>("codePostal"));
        pseudo.setCellValueFactory(new PropertyValueFactory<>("pseudo"));
        photo.setCellValueFactory(new PropertyValueFactory<>("photo"));
        desc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        tailleOrganisme.setCellValueFactory(new PropertyValueFactory<>("tailleOrganisme"));
        estValide.setCellValueFactory(new PropertyValueFactory<>("estValide"));
        siret.setCellValueFactory(new PropertyValueFactory<>("siret"));
        nbPointsSourire.setCellValueFactory(new PropertyValueFactory<>("nbPointsSourire"));

        for(int i = 0; i < users.length(); i++){
            JSONObject user = users.getJSONObject(i);
            User userElement = new User(user.getInt("id"),
                    user.getString("Libelle"),
                    user.getInt("categorieUtilisateur"),
                    user.getString("nom"),
                    user.getString("prenom"),
                    user.getString("mail"),
                    user.getString("tel"),
                    user.getString("adresse"),
                    user.getString("ville"),
                    user.getInt("codePostal"),
                    user.getString("prenom"),
                    user.getString("mdp"),
                    user.getString("photo"),
                    user.getString("desc"),
                    user.getInt("tailleOrganisme"),
                    user.getInt("estValide"),
                    user.getString("siret"),
                    user.isNull("dateDeNaissance") ? "" : user.getString("dateDeNaissance"),

                    //LocalDate.parse(user.getString("dateDeNaissance")),
                    user.getInt("nbPointsSourire"));

            usersTable.getItems().add(userElement);
        }
    }

    @FXML
    public void clickItem(MouseEvent mouseEvent) {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() == 2){
                System.out.println("Double clicked");
            }
        }
    }


    @FXML
    public void activateUser(ActionEvent event){
        refreshSelectedIndices();

        if(indexOfUserSelected != -1) {
            try {
                JSONObject user = users.getJSONObject(indexOfUserSelected);
                User userElement = jsonToUser(user);
                userElement.setEstValide(1);

                if(fr.wastemart.maven.javaclient.services.User.updateUser(userElement) < 299){
                    String objet = "Compte Validé !";
                    String message = "Bonjour, <br/> Votre compte à été validé, vous pouvez désormais vous" +
                    " connecter sur WasteMart ! <br/> Cordialement, <br/> L'équipe WasteMart";

                    System.out.println(fr.wastemart.maven.javaclient.services.User.sendMail(userElement.getMail(), objet, message));
                }

                displayUsers();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void refuseProduct(ActionEvent event) {
        refreshSelectedIndices();

        if(indexOfUserSelected != -1) {
            try {
                JSONObject user = users.getJSONObject(indexOfUserSelected);
                User userElement = fr.wastemart.maven.javaclient.services.User.jsonToUser(user);
                userElement.setEstValide(0);

                Integer updateResult = fr.wastemart.maven.javaclient.services.User.updateUser(userElement);

                displayUsers();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void displayAddUser(ActionEvent actionEvent) throws Exception {
        refreshSelectedIndices();

        if(indexOfUserSelected != -1) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr.wastemart.maven.javaclient/views/ManageUser.fxml"));

            Scene newScene;
            try {
                newScene = new Scene(loader.load());
            } catch (IOException ex) {
                // TODO: handle error
                return;
            }

            ManageProductController controller = loader.getController();
            controller.init(users.getJSONObject(indexOfUserSelected).getInt("id"), "Add", null);

            Stage stageNodeRoot = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            Stage inputStage = new Stage();
            inputStage.initOwner(stageNodeRoot);
            inputStage.setScene(newScene);
            inputStage.showAndWait();

        }

        displayUsers();

    }

    public void displayModifyUser(ActionEvent actionEvent) throws Exception {
        refreshSelectedIndices();

        if(indexOfUserSelected != -1) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr.wastemart.maven.javaclient/views/ManageUser.fxml"));

            Scene newScene;
            try {
                newScene = new Scene(loader.load());
            } catch (IOException ex) {
                System.out.println(ex);
                return;
            }

            ManageUserController controller = loader.getController();
            JSONObject user = users.getJSONObject(indexOfUserSelected);

            User userToModify = new User(user.getInt("id"),
                    user.getString("Libelle"),
                    user.getInt("categorieUtilisateur"),
                    user.getString("nom"),
                    user.getString("prenom"),
                    user.getString("mail"),
                    user.getString("tel"),
                    user.getString("adresse"),
                    user.getString("ville"),
                    user.getInt("codePostal"),
                    user.getString("prenom"),
                    user.getString("mdp"),
                    user.getString("photo"),
                    user.getString("desc"),
                    user.getInt("tailleOrganisme"),
                    user.getInt("estValide"),
                    user.getString("siret"),
                    user.getString("dateDeNaissance"),
                    user.getInt("nbPointsSourire")
            );

            controller.init(users.getJSONObject(indexOfUserSelected).getInt("id"), "Modify", userToModify);

            Stage stageNodeRoot = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            Stage inputStage = new Stage();
            inputStage.initOwner(stageNodeRoot);
            inputStage.setScene(newScene);
            inputStage.showAndWait();
        }

        displayUsers();

    }

    public void contactUser(ActionEvent actionEvent) {
        refreshSelectedIndices();

        if(indexOfUserSelected != -1) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr.wastemart.maven.javaclient/views/Message.fxml"));

            Scene newScene;
            try {
                newScene = new Scene(loader.load());
            } catch (IOException ex) {
                System.out.println(ex);
                return;
            }

            MessageController controller = loader.getController();
            JSONObject user = users.getJSONObject(indexOfUserSelected);

            controller.init(user.getString("mail"));

            Stage stageNodeRoot = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            Stage inputStage = new Stage();
            inputStage.initOwner(stageNodeRoot);
            inputStage.setScene(newScene);
            inputStage.showAndWait();
        }
    }

    public void refreshSelectedIndices() {
        this.indexOfUserSelected = usersTable.getSelectionModel().getSelectedIndex();

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
