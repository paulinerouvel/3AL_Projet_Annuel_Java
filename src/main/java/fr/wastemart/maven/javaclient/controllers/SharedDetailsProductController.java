package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.models.Product;
import fr.wastemart.maven.javaclient.services.DateFormatter;
import fr.wastemart.maven.javaclient.services.Logger;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

import java.time.LocalDate;

import static fr.wastemart.maven.javaclient.services.Product.*;

public class SharedDetailsProductController extends GenericController {

    private Integer listId;
    private String operation;
    private Product product;

    @FXML private TextField libelle;
    @FXML private TextField desc;
    @FXML private TextField photo;
    @FXML private TextField prix;
    @FXML private TextField prixInitial;
    @FXML private TextField quantite;
    @FXML private DatePicker dlc;
    @FXML private TextField codeBarre;
    @FXML private ChoiceBox<String> categorieProduit;

    public void init(Integer productList, String operation, Product product) throws Exception {
        //JSONArray productCategories = fr.wastemart.maven.javaclient.services.Product.fetchProductCategories();
        this.product = product;
        this.operation = operation;
        categorieProduit.setItems(FXCollections.observableArrayList("Légumes/Fruits", "Viandes/Poissons/Oeufs",
                "Produits Laitiers", "Meubles", "Electroniques", "Electroménagers", "Jouets", "Céréales/Féculents",
                "Boissons", "Cosmétiques", "Condiments", "Sucre/Produits Sucrés"));

        setListId(productList);

        if(operation.equals("Add")){
            clearFields();
            categorieProduit.getSelectionModel().selectFirst();
        } else if(operation.equals("Modify")){
            setFields(product);
        }

        categorieProduit.setTooltip(new Tooltip("Sélectionnez un type de produit"));
    }

    public void submitProduct() {
        try {
            libelle.setStyle("-fx-background-color: #FFFFFF");
            desc.setStyle("-fx-background-color: #FFFFFF");
            prix.setStyle("-fx-background-color: #FFFFFF");
            prixInitial.setStyle("-fx-background-color: #FFFFFF");
            quantite.setStyle("-fx-background-color: #FFFFFF");
            dlc.setStyle("-fx-background-color: #FFFFFF");
            codeBarre.setStyle("-fx-background-color: #FFFFFF");

            Integer indexWrong = areTextFieldsValid();

            if (indexWrong != -1) {
                switch (indexWrong) {
                    case 0:
                        libelle.setStyle("-fx-background-color: #ff7980");
                        break;
                    case 1:
                        desc.setStyle("-fx-background-color: #ff7980");
                        break;
                    case 2:
                        prix.setStyle("-fx-background-color: #ff7980");
                        break;
                    case 3:
                        prixInitial.setStyle("-fx-background-color: #ff7980");
                        break;
                    case 4:
                        quantite.setStyle("-fx-background-color: #ff7980");
                        break;
                    case 5:
                        dlc.setStyle("-fx-background-color: #ff7980");
                        break;
                    case 6:
                        codeBarre.setStyle("-fx-background-color: #ff7980");
                        break;
                    default:
                        break;

                }
            } else {

                Product newProduct = new Product(product == null ? -1 : product.getId(),
                        libelle.getText(),
                        desc.getText(),
                        photo.getText(),
                        Float.valueOf(prix.getText()),
                        Float.valueOf(prixInitial.getText()),
                        Integer.valueOf(quantite.getText()),
                        DateFormatter.dateToString(dlc.getValue().toString()),
                        codeBarre.getText(),
                        false,
                        product == null ? null : product.getDateMiseEnRayon() == null ? null : product.getDateMiseEnRayon().toString(),
                        categorieProduit.getSelectionModel().getSelectedIndex() + 1,
                        listId,
                        product == null ? null : product.getEntrepotwm(),
                        product == null ? null : product.getDestinataire()
                );

                String result = null;
                if (operation.equals("Add")) {
                    addProductToList(newProduct, UserInstance.getInstance().getTokenValue());

                } else if (operation.equals("Modify")) {
                    updateProduct(newProduct, UserInstance.getInstance().getTokenValue());
                }
                setInfoText(result);
            }
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    private Integer areTextFieldsValid() {
        if(libelle.getText().trim().isEmpty()){ return 0; }
        else if(desc.getText().trim().isEmpty()){ return 1; }
        else if(prix.getText().trim().isEmpty()){ return 2; }
        else if(prixInitial.getText().trim().isEmpty()){ return 3; }
        else if(quantite.getText().trim().isEmpty()){ return 4; }
        else if(dlc.getValue().toString().trim().isEmpty()){ return 5; }
        else if(codeBarre.getText().trim().isEmpty()){ return 6; }
        return -1;
    }

    private void clearFields() {
        libelle.clear();
        desc.clear();
        photo.clear();
        prix.clear();
        prixInitial.clear();
        quantite.clear();
        dlc.setValue(LocalDate.now());
        setInfoText("");
    }

    private void setFields(Product product) {
            libelle.setText(product.getLibelle());
            desc.setText(product.getDesc());
            photo.setText(product.getPhoto());
            prix.setText(String.valueOf(product.getPrix()));
            prixInitial.setText(String.valueOf(product.getPrixInitial()));
            quantite.setText(String.valueOf(product.getQuantite()));
            dlc.setValue(LocalDate.parse(product.getDlc()));
            codeBarre.setText(product.getCodeBarre());
            categorieProduit.getSelectionModel().select(product.getCategorieProduit() - 1);
            setInfoText("");
    }

    public void setListId(Integer id) { this.listId = id; }
}
