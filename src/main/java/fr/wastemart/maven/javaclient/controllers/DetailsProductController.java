package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.models.Product;
import fr.wastemart.maven.javaclient.services.DateFormatter;
import fr.wastemart.maven.javaclient.services.Details.Detail;
import fr.wastemart.maven.javaclient.services.Details.ProductDetail;
import fr.wastemart.maven.javaclient.services.Details.StringDetail;
import fr.wastemart.maven.javaclient.services.Logger;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import org.apache.commons.lang.math.NumberUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.List;

import static fr.wastemart.maven.javaclient.services.Product.*;

public class DetailsProductController extends GenericController {

    private Integer listId;
    private String option;
    private Product product;

    @FXML private TextField libelle;
    @FXML private TextField desc;
    @FXML private TextField photo;
    @FXML private TextField prix;
    @FXML private TextField quantite;
    @FXML private DatePicker dlc;
    @FXML private TextField codeBarre;
    @FXML private ChoiceBox<String> categorieProduit;

    public void init(List<Detail> detail) throws Exception {
        StringDetail optionDetail = (StringDetail) detail.get(0);
        option = optionDetail.getValue();

        setProductCategories();

        if(option.equals("add")){
            clearFields();
            categorieProduit.getSelectionModel().selectFirst();
        } else if (option.equals("modify")){
            ProductDetail productDetail = (ProductDetail) detail.get(1);
            product = productDetail.getValue();
            setFields(product);
        }

        categorieProduit.setTooltip(new Tooltip("Sélectionnez un type de produit"));
    }

    public void submitProduct() {
        try {
            libelle.setStyle("-fx-background-color: #FFFFFF");
            desc.setStyle("-fx-background-color: #FFFFFF");
            prix.setStyle("-fx-background-color: #FFFFFF");
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
                        quantite.setStyle("-fx-background-color: #ff7980");
                        break;
                    case 4:
                        dlc.setStyle("-fx-background-color: #ff7980");
                        break;
                    case 5:
                        codeBarre.setStyle("-fx-background-color: #ff7980");
                        break;
                    default:
                        break;

                }
                setInfoText("Remplissez tous les champs");
            } else {

                Product newProduct = new Product(product == null ? -1 : product.getId(),
                        libelle.getText(),
                        desc.getText(),
                        photo.getText(),
                        Float.valueOf(prix.getText()),
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

                if (option.equals("add")) {
                    if(addProductToList(newProduct, UserInstance.getInstance().getTokenValue())){
                        clearFields();
                        setInfoText("Produit créé avec succès");
                    } else {
                        setInfoText("Le produit n'a pas pu être créé");
                    }

                } else if (option.equals("modify")) {
                    if(updateProduct(newProduct, UserInstance.getInstance().getTokenValue())){
                        setInfoText("Produit modifié avec succès");
                    } else {
                        setInfoText("Le produit n'a pas pu être modifié");
                    }
                }
            }
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    private Integer areTextFieldsValid() {
        if(libelle.getText().trim().isEmpty()){ return 0; }
        else if(desc.getText().trim().isEmpty()){ return 1; }
        else if(!NumberUtils.isNumber(prix.getText()) || prix.getText().trim().isEmpty()){ return 2; }
        else if(!NumberUtils.isNumber(quantite.getText()) ||quantite.getText().trim().isEmpty()){ return 3; }
        else if(!dlc.getValue().toString().trim().isEmpty() && dlc.getValue().compareTo(LocalDate.now()) < 0) { return 4; }
        return -1;
    }

    private void clearFields() {
        libelle.clear();
        desc.clear();
        photo.clear();
        prix.clear();
        quantite.clear();
        dlc.setValue(LocalDate.now());
        codeBarre.clear();
        setInfoText("");
    }

    private void setFields(Product product) {
            libelle.setText(product.getLibelle());
            desc.setText(product.getDesc());
            photo.setText(product.getPhoto() == null ? "" : product.getPhoto()); // Impossible
            prix.setText(String.valueOf(product.getPrix()));
            quantite.setText(String.valueOf(product.getQuantite()));
            dlc.setValue(product.getCodeBarre() == null ? LocalDate.now() : LocalDate.parse(product.getDlc())); // Nullable
            codeBarre.setText(product.getCodeBarre() == null ? "" : product.getCodeBarre()); // Nullable
            categorieProduit.getSelectionModel().select(product.getCategorieProduit() - 1);
            setInfoText("");
    }

    private void setProductCategories(){
        JSONArray productCategories = fetchProductCategories();

        ObservableList<String> categories = FXCollections.observableArrayList();

        for (int i = 0; i < productCategories.length(); i++){
            JSONObject category = (JSONObject) productCategories.get(i);
            categories.add(String.valueOf(category.get("libelle")));
        }

        categorieProduit.setItems(categories);
    }

    public void setListId(Integer id) { this.listId = id; }
}
