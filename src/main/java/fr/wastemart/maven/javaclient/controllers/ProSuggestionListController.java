package fr.wastemart.maven.javaclient.controllers;

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
import fr.wastemart.maven.javaclient.models.Product;
import fr.wastemart.maven.javaclient.models.ProductList;
import org.json.JSONArray;
import org.json.JSONObject;
import fr.wastemart.maven.javaclient.services.UserInstance;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;


public class ProSuggestionListController {
    private UserInstance instance;
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
    TableColumn<Object, Object> listEstArchive;

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

    public void init(){
        displayProductLists();
        displayProducts(lists.getJSONObject(0).getInt("id"));
        listsTable.getSelectionModel().selectFirst();
    }

    private void displayProductLists() {
        listsTable.getItems().clear();
        lists = fr.wastemart.maven.javaclient.services.ProductList.fetchAllProductLists();


        System.out.println("Listdata =" +lists);

        listId.setCellValueFactory(new PropertyValueFactory<>("id"));
        listName.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        listUser.setCellValueFactory(new PropertyValueFactory<>("userId"));
        listEstArchive.setCellValueFactory(new PropertyValueFactory<>("estArchive"));


        for(int i = 0; i < lists.length(); i++){
            JSONObject list = lists.getJSONObject(i);
            ProductList listElement = fr.wastemart.maven.javaclient.services.ProductList.jsonToProductList(list);
            listsTable.getItems().add(listElement);

        }
    }

    private void displayProducts(Integer id) {
        productsTable.getItems().clear();
        products = fr.wastemart.maven.javaclient.services.Product.fetchProducts(id);

        System.out.println("Productdata =" +products);

        productName.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        productDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("prix"));
        productInitialPrice.setCellValueFactory(new PropertyValueFactory<>("prixInitial"));
        productDlc.setCellValueFactory(new PropertyValueFactory<>("dlc"));
        productAvailable.setCellValueFactory(new PropertyValueFactory<>("enRayon"));
        productDate.setCellValueFactory(new PropertyValueFactory<>("dateMiseEnRayon"));

        for(int i = 0; i < products.length(); i++) {
            JSONObject product = products.getJSONObject(i);
            Product productElement = fr.wastemart.maven.javaclient.services.Product.jsonToProduct(product);

            productsTable.getItems().add(productElement);
        }
    }

    @FXML
    public void clickItem(MouseEvent event) {
        refreshSelectedIndices();

        if(indexOfListSelected != -1){
            displayProducts(lists.getJSONObject(indexOfListSelected).getInt("id"));
        }
    }

    @FXML
    public void validateProduct(ActionEvent event){
        refreshSelectedIndices();

        if(indexOfListSelected != -1) {
            try {
                JSONObject product = products.getJSONObject(indexOfProductSelected).put("enRayon", 1);
                Product productElement = fr.wastemart.maven.javaclient.services.Product.jsonToProduct(product);

                System.out.println(fr.wastemart.maven.javaclient.services.Product.updateProduct(productElement));

                displayProducts(lists.getJSONObject(indexOfListSelected).getInt("id"));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void refuseProduct(ActionEvent event) {
        refreshSelectedIndices();

        if(indexOfListSelected != -1){
            try {
                JSONObject product = products.getJSONObject(indexOfProductSelected).put("enRayon", 0);
                Product productElement = fr.wastemart.maven.javaclient.services.Product.jsonToProduct(product);

                System.out.println(fr.wastemart.maven.javaclient.services.Product.updateProduct(productElement));

                displayProducts(lists.getJSONObject(indexOfListSelected).getInt("id"));

            } catch (Exception e) {
                e.printStackTrace();
            }        }
    }

    public void submitList(ActionEvent event) {
        refreshSelectedIndices();

        if (indexOfListSelected != -1 && lists.getJSONObject(indexOfListSelected).getInt("estArchive") != 1){

            JSONObject list = lists.getJSONObject(indexOfListSelected);
            ProductList listElement = fr.wastemart.maven.javaclient.services.ProductList.jsonToProductList(list);

            ArrayList<Product> productList = new ArrayList<Product>();
            for(int i = 0; i < products.length(); i++) {
                JSONObject product = products.getJSONObject(i);
                productList.add(fr.wastemart.maven.javaclient.services.Product.jsonToProduct(product));
            }

            // Affecte la liste de produits à un entrepot
            Integer affectProductListToWarehouseRes = fr.wastemart.maven.javaclient.services.ProductList.affectProductListToWarehouse(productList, instance.getUser().getVille());
            System.out.println("affectProductListToWarehouseRes res : " +affectProductListToWarehouseRes);
            if(affectProductListToWarehouseRes == 0){ // Aucune place de libre
                System.out.println("No space available");
            }else if (affectProductListToWarehouseRes > 299){ // Erreur
                System.out.println("Error : "+ affectProductListToWarehouseRes);
            } else { // Réussite
                System.out.println("Reussite");
                listElement.setEstArchive(1);
                listElement.setDate(LocalDate.now());
                fr.wastemart.maven.javaclient.services.ProductList.updateList(listElement);
            }
        }
        displayProductLists();
    }

    public void displayAddProduct(ActionEvent actionEvent) throws Exception {
        refreshSelectedIndices();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr.wastemart.maven.javaclient/views/ManageProduct.fxml"));

        Scene newScene;
        try {
            newScene = new Scene(loader.load());
        } catch (IOException ex) {
            // TODO: handle error
            return;
        }

        ManageProductController controller = loader.getController();
        controller.init(lists.getJSONObject(indexOfListSelected).getInt("id"), "Add", null);

        Stage stageNodeRoot = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        Stage inputStage = new Stage();
        inputStage.initOwner(stageNodeRoot);
        inputStage.setScene(newScene);
        inputStage.showAndWait();

        displayProducts(lists.getJSONObject(indexOfListSelected).getInt("id"));
    }

    public void displayModifyProduct(ActionEvent actionEvent) throws Exception {
        refreshSelectedIndices();

        if(indexOfProductSelected != -1) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr.wastemart.maven.javaclient/views/ManageProduct.fxml"));

            Scene newScene;
            try {
                newScene = new Scene(loader.load());
            } catch (IOException ex) {
                // TODO: handle error
                return;
            }

            ManageProductController controller = loader.getController();
            JSONObject product = products.getJSONObject(indexOfProductSelected);
            Product productToModify = fr.wastemart.maven.javaclient.services.Product.jsonToProduct(product);

            controller.init(lists.getJSONObject(indexOfListSelected).getInt("id"), "Modify", productToModify);

            Stage stageNodeRoot = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            Stage inputStage = new Stage();
            inputStage.initOwner(stageNodeRoot);
            inputStage.setScene(newScene);
            inputStage.showAndWait();
        }

        displayProducts(lists.getJSONObject(indexOfListSelected).getInt("id"));

    }

    public void refreshSelectedIndices() {
        this.indexOfProductSelected = productsTable.getSelectionModel().getSelectedIndex();
        this.indexOfListSelected = listsTable.getSelectionModel().getSelectedIndex();

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
