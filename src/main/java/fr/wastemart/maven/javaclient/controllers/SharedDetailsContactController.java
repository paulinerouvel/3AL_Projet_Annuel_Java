package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.Details.Detail;
import fr.wastemart.maven.javaclient.services.Details.StringDetail;
import fr.wastemart.maven.javaclient.services.Logger;
import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.List;

import static fr.wastemart.maven.javaclient.services.User.sendMail;

public class SharedDetailsContactController extends GenericController {
    @FXML private Label receiver;
    @FXML private TextField subject;
    @FXML private TextArea body;

    public void init(List<Detail> contactDetails) throws Exception {
        StringDetail mailDetail = (StringDetail) contactDetails.get(0);
        receiver.setText(mailDetail.getValue());
    }

    public void submitMessage(ActionEvent event) {
        try {
            subject.setStyle("-fx-background-color: #FFFFFF");
            body.setStyle("-fx-background-color: #FFFFFF");

            Integer index;
            if((index = areFieldsValid()) != -1){
                switch(index) {
                    case 0:
                        subject.setStyle("-fx-background-color: #ff7980");
                        break;
                    case 1:
                        body.setStyle("-fx-background-color: #ff7980");
                        break;
                    default:
                        break;
                }
            } else {
                body.setText(body.getText().replace("\n","\\n"));
                Integer contactRes = sendMail(receiver.getText(), subject.getText(), body.getText());
                if(contactRes > 299){
                    setInfoText("Echec de l'envoi du message");
                } else {
                    //result = fr.wastemart.maven.javaclient.services.Product.updateProduct(newProduct) == 201 ? "Produit Ajouté" : "L'ajout a échoué";
                    clearFields();
                    setInfoText("Message envoyé");
                }
            }
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    private Integer areFieldsValid()  {
        if(subject.getText().trim().isEmpty()){ return 0; }
        else if(body.getText().trim().isEmpty()){ return 1; }
        return -1;
    }

    private void clearFields() {
        subject.clear();
        body.clear();
        setInfoText("");
    }
}
