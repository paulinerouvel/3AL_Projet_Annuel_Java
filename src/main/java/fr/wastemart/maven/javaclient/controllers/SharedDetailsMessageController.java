package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.Logger;
import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import static fr.wastemart.maven.javaclient.services.User.sendMail;

public class SharedDetailsMessageController extends GenericController {
    private String destinataire = null;

    @FXML private TextField objet;
    @FXML private TextField message;
    @FXML private Label info;

    public void init(String destinataire){
        this.destinataire = destinataire;
    }

    public void submitMessage(ActionEvent event) {
        try {
            objet.setStyle("-fx-background-color: #FFFFFF");
            message.setStyle("-fx-background-color: #FFFFFF");

            Integer indexWrong = areTextFieldsValid();

            if(indexWrong != -1){
                switch(indexWrong) {
                    case 0:
                        objet.setStyle("-fx-background-color: #ff7980");
                        break;
                    case 1:
                        message.setStyle("-fx-background-color: #ff7980");
                        break;
                    default:
                        break;

                }
            } else if(destinataire == null || objet.getText().isEmpty() || message.getText().isEmpty()) {
                Integer contactRes = sendMail(destinataire, objet.getText(), message.getText());
                if(contactRes > 299){
                    info.setText("Echec de l'envoi du message");
                } else {
                    //result = fr.wastemart.maven.javaclient.services.Product.updateProduct(newProduct) == 201 ? "Produit Ajouté" : "L'ajout a échoué";
                    clearFields();
                    info.setText("Message envoyé");
                }
            }
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    private Integer areTextFieldsValid()  {
        if(objet.getText().trim().isEmpty()){ return 0; }
        else if(message.getText().trim().isEmpty()){ return 1; }
        return -1;
    }

    private void clearFields() {
        objet.clear();
        message.clear();
        info.setText("");
    }

    // Return button
    public void displayMainPage(ActionEvent actionEvent) {
        StageManager.getInstance().displayMainPage(UserInstance.getInstance(), actionEvent);
    }

    public String getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }
}
