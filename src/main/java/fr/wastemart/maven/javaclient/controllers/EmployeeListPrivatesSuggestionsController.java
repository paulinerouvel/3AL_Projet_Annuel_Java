package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.models.Product;
import fr.wastemart.maven.javaclient.models.ProductList;
import fr.wastemart.maven.javaclient.services.Logger;
import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import static fr.wastemart.maven.javaclient.services.Product.*;
import static fr.wastemart.maven.javaclient.services.ProductList.*;


public class EmployeeListPrivatesSuggestionsController extends GenericController {
    private JSONArray lists;
    private JSONArray products;
    private Integer indexOfProductSelected;
    private Integer indexOfListSelected;

    @FXML
    TableView<ProductList> listsTable;
    @FXML
    TableColumn<Object, Object> listId;
    @FXML
    TableColumn<Object, Object> listName;
    @FXML
    TableColumn<Object, Object> listUser;

    @FXML
    TableView<Product> productsTable;
    @FXML
    TableColumn<Object, Object> productName;
    @FXML
    TableColumn<Object, Object> productDesc;
    @FXML
    TableColumn<Object, Object> productPrice;
    @FXML
    TableColumn<Object, Object> productInitialPrice;
    @FXML
    TableColumn<Object, Object> productDlc;
    @FXML
    TableColumn<Object, Object> productAvailable;
    @FXML
    TableColumn<Object, Object> productDate;
    @FXML
    TableColumn<Object, Object> productQuantity;

    public void init() throws Exception {
        displayProductLists();
        displayProducts(lists.getJSONObject(0).getInt("id"));
        listsTable.getSelectionModel().selectFirst();
    }

    private void displayProductLists() throws Exception {
        listsTable.getItems().clear();
        //lists = services.ProductList.fetchAllProductLists();
        lists = fetchAllProductListsByUserCategory(3, UserInstance.getInstance().getTokenValue());

        listId.setCellValueFactory(new PropertyValueFactory<>("id"));
        listName.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        listUser.setCellValueFactory(new PropertyValueFactory<>("userId"));


        for(int i = 0; i < lists.length(); i++){
            JSONObject list = lists.getJSONObject(i);
            ProductList listElement = jsonToProductList(list);

            listsTable.getItems().add(listElement);
        }
    }

    private void displayProducts(Integer id) throws Exception {
        productsTable.getItems().clear();
        products = fetchProducts(id, UserInstance.getInstance().getTokenValue());

        productName.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        productDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("prix"));
        productInitialPrice.setCellValueFactory(new PropertyValueFactory<>("prixInitial"));
        productDlc.setCellValueFactory(new PropertyValueFactory<>("dlc"));
        productAvailable.setCellValueFactory(new PropertyValueFactory<>("enRayon"));
        productDate.setCellValueFactory(new PropertyValueFactory<>("dateMiseEnRayon"));
        productQuantity.setCellValueFactory(new PropertyValueFactory<>("quantite"));


        for (int i = 0; i < products.length(); i++) {
            JSONObject product = products.getJSONObject(i);
            Product productElement = jsonToProduct(product);

            productsTable.getItems().add(productElement);
        }
    }

    @FXML
    public void clickItem() {
        refreshSelectedIndices();

        if(indexOfListSelected != -1){
            try {
                displayProducts(lists.getJSONObject(indexOfListSelected).getInt("id"));
            } catch (Exception e) {
                Logger.getInstance().reportError(e);
                setInfoText("An error occurred, see logs");
            }
        }
    }

    @FXML
    public void validateProduct(){
        refreshSelectedIndices();

        if(indexOfListSelected != -1) {
            try {
                JSONObject product = products.getJSONObject(indexOfProductSelected).put("enRayon", 0);
                Product productElement = jsonToProduct(product);

                productElement.setEnRayon(true);
                updateProduct(productElement, UserInstance.getInstance().getTokenValue());

                displayProducts(lists.getJSONObject(indexOfListSelected).getInt("id"));

            } catch (Exception e) {
                Logger.getInstance().reportError(e);
                setInfoErrorOccurred();
            }
        }
    }

    @FXML
    public void refuseProduct() throws Exception {
        try {
            refreshSelectedIndices();

            if (indexOfListSelected != -1) {
                JSONObject product = products.getJSONObject(indexOfProductSelected).put("enRayon", 0);
                Product productElement = jsonToProduct(product);

                productElement.setEnRayon(false);
                updateProduct(productElement, UserInstance.getInstance().getTokenValue());

                displayProducts(lists.getJSONObject(indexOfListSelected).getInt("id"));

            }
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    public void submitList() {
        try {
            refreshSelectedIndices();

            if (indexOfListSelected != -1 && lists.getJSONObject(indexOfListSelected).getInt("estArchive") != 1) {

                JSONObject list = lists.getJSONObject(indexOfListSelected);
                ProductList listElement = jsonToProductList(list);

                ArrayList<Product> productList = new ArrayList<Product>();
                for (int i = 0; i < products.length(); i++) {
                    JSONObject product = products.getJSONObject(i);
                    productList.add(jsonToProduct(product));
                }

                // Affecte la liste de produits à un entrepot
                Integer affectProductListToWarehouseRes = affectProductListToWarehouse(productList, UserInstance.getInstance().getUser().getVille(), UserInstance.getInstance().getTokenValue());

                if (affectProductListToWarehouseRes != 0 && affectProductListToWarehouseRes < 299) { // Erreur
                    listElement.setEstArchive(1);
                    listElement.setDate(LocalDate.now());
                    updateProductList(listElement, UserInstance.getInstance().getTokenValue());
                }
            }
            displayProductLists();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    public void displayAddProduct(ActionEvent actionEvent) {
        try {
            refreshSelectedIndices();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr.wastemart.maven.javaclient/views/SharedDetailsProduct.fxml"));

            Scene newScene;
            newScene = new Scene(loader.load());

            SharedDetailsProductController controller = loader.getController();
            controller.init(lists.getJSONObject(indexOfListSelected).getInt("id"), "Add", null);
            Stage stageNodeRoot = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            Stage inputStage = new Stage();
            inputStage.initOwner(stageNodeRoot);
            inputStage.setScene(newScene);
            inputStage.showAndWait();

            displayProducts(lists.getJSONObject(indexOfListSelected).getInt("id"));
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    public void displayModifyProduct(ActionEvent actionEvent) {
        try {
            refreshSelectedIndices();

            if (indexOfProductSelected != -1) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr.wastemart.maven.javaclient/views/SharedDetailsProduct.fxml"));

                Scene newScene;
                try {
                    newScene = new Scene(loader.load());
                } catch (IOException ex) {
                    // TODO: handle error
                    return;
                }

                SharedDetailsProductController controller = loader.getController();
                JSONObject product = products.getJSONObject(indexOfProductSelected);

                Product productElement = jsonToProduct(product);
                controller.init(lists.getJSONObject(indexOfListSelected).getInt("id"), "Modify", productElement);

                Stage stageNodeRoot = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

                Stage inputStage = new Stage();
                inputStage.initOwner(stageNodeRoot);
                inputStage.setScene(newScene);
                inputStage.showAndWait();
            }

            displayProducts(lists.getJSONObject(indexOfListSelected).getInt("id"));
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    public void refreshSelectedIndices() {
        this.indexOfProductSelected = productsTable.getSelectionModel().getSelectedIndex();
        this.indexOfListSelected = listsTable.getSelectionModel().getSelectedIndex();

    }

    // Return button
    public void displayMainPage() {
        StageManager.getInstance().displayMainPage(UserInstance.getInstance());
    }
}
