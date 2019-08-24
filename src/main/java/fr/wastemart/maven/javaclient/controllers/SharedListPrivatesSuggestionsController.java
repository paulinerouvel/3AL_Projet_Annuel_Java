package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.models.Product;
import fr.wastemart.maven.javaclient.models.ProductList;
import fr.wastemart.maven.javaclient.services.Details.Detail;
import fr.wastemart.maven.javaclient.services.Details.ProductDetail;
import fr.wastemart.maven.javaclient.services.Details.StringDetail;
import fr.wastemart.maven.javaclient.services.Logger;
import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static fr.wastemart.maven.javaclient.services.Product.*;
import static fr.wastemart.maven.javaclient.services.ProductList.*;


public class SharedListPrivatesSuggestionsController extends GenericController {
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
        if(displayProductLists()) {
            displayProducts(lists.getJSONObject(0).getInt("id"));
            listsTable.getSelectionModel().selectFirst();
        }
    }

    private boolean displayProductLists() throws Exception {
        listsTable.getItems().clear();
        //lists = services.ProductList.fetchAllProductLists();
        lists = fetchAllProductListsByUserCategory(3, UserInstance.getInstance().getTokenValue());
        if(!lists.isEmpty()) {
            listId.setCellValueFactory(new PropertyValueFactory<>("id"));
            listName.setCellValueFactory(new PropertyValueFactory<>("libelle"));
            listUser.setCellValueFactory(new PropertyValueFactory<>("userId"));


            for (int i = 0; i < lists.length(); i++) {
                JSONObject list = lists.getJSONObject(i);
                ProductList listElement = jsonToProductList(list);

                listsTable.getItems().add(listElement);
            }
            return true;
        } else {
            setInfoText("Aucune liste trouvée");
            return false;
        }
    }

    private void displayProducts(Integer id) throws Exception {
        if(!lists.isEmpty()) {
            productsTable.getItems().clear();
            products = fetchProducts(id, UserInstance.getInstance().getTokenValue());

            if(!products.isEmpty()) {
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
            } else {
                setInfoText("La liste est vide !");
            }
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

            Product selectedProduct = jsonToProduct(lists.getJSONObject(indexOfListSelected));

            List<Detail> detailList = new ArrayList<Detail>();
            detailList.add(new ProductDetail(selectedProduct));
            detailList.add(new StringDetail("Add"));

            StageManager.getInstance().loadPageWithDetails(dotenv.get("SHARED_DETAILS_SUGGESTIONS"), UserInstance.getInstance(), detailList);

            displayProducts(lists.getJSONObject(indexOfListSelected).getInt("id"));
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    public void displayModifyProduct(ActionEvent actionEvent) {
        try {
            refreshSelectedIndices();

            Product selectedProduct = jsonToProduct(lists.getJSONObject(indexOfListSelected));

            List<Detail> detailList = new ArrayList<Detail>();
            detailList.add(new ProductDetail(selectedProduct));
            detailList.add(new StringDetail("Modify"));

            StageManager.getInstance().loadPageWithDetails(dotenv.get("SHARED_DETAILS_SUGGESTIONS"), UserInstance.getInstance(), detailList);

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
