package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.models.Product;
import fr.wastemart.maven.javaclient.models.ProductList;
import fr.wastemart.maven.javaclient.services.Details.Detail;
import fr.wastemart.maven.javaclient.services.Logger;
import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
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
import java.util.List;

import static fr.wastemart.maven.javaclient.services.Product.*;
import static fr.wastemart.maven.javaclient.services.ProductList.*;


public class ProfessionnalListProductsController extends GenericController {
    private JSONArray lists;
    private JSONArray products;
    private Integer indexOfProductSelected;
    private Integer indexOfListSelected;

    @FXML CheckBox listArchiveCheckBox;
    @FXML
    TableView<ProductList> listsTable;
    @FXML
    TableColumn<Object, Object> listId;
    @FXML
    TableColumn<Object, Object> listName;
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

    public void init() throws Exception {
        listArchiveCheckBox.setSelected(false);
        displayProductLists();
        displayProducts(lists.getJSONObject(0).getInt("id"));
        listsTable.getSelectionModel().selectFirst();
    }

    @FXML
    private void displayProductLists() throws Exception {
        listsTable.getItems().clear();
        lists = fetchProductLists(UserInstance.getInstance().getUser().getId(), UserInstance.getInstance().getTokenValue());

        listId.setCellValueFactory(new PropertyValueFactory<>("id"));
        listName.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        listEstArchive.setCellValueFactory(new PropertyValueFactory<>("estArchive"));


        for(int i = 0; i < lists.length(); i++){
            if(listArchiveCheckBox.isSelected() || lists.getJSONObject(i).getInt("estArchive") != 1){
                JSONObject list = lists.getJSONObject(i).put("Utilisateur_id", UserInstance.getInstance().getUser().getId());
                ProductList listElement = jsonToProductList(list);
                listsTable.getItems().add(listElement);
            }

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

        for(int i = 0; i < products.length(); i++) {
            JSONObject product = products.getJSONObject(i);
            Product productElement = jsonToProduct(product);

            productsTable.getItems().add(productElement);
        }
    }

    @FXML
    public void clickItem() {
        try {
            refreshSelectedIndices();

            if (indexOfListSelected != -1) {
                displayProducts(lists.getJSONObject(indexOfListSelected).getInt("id"));
            }
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    public void removeList() {
        try {
            refreshSelectedIndices();

            if(indexOfListSelected != -1){
                Integer listToRemoveId = lists.getJSONObject(indexOfListSelected).getInt("id");
                Integer removeProductListRes = removeProductsList(listToRemoveId, UserInstance.getInstance().getTokenValue());
            }

            displayProducts(lists.getJSONObject(0).getInt("id"));
            displayProductLists();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    public void createList() {
        try {
            refreshSelectedIndices();
            ProductList productList = new ProductList(-1,
                    "Test",
                    LocalDate.now(),
                    UserInstance.getInstance().getUser().getId(),
                    0);

            createProductList(productList, UserInstance.getInstance().getTokenValue());
            displayProductLists();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    public void removeProduct() {
        try {
            refreshSelectedIndices();

            if (indexOfProductSelected != -1){
                deleteProduct(products.getJSONObject(indexOfProductSelected).getInt("id"), UserInstance.getInstance().getTokenValue());
            }

            displayProducts(lists.getJSONObject(indexOfListSelected).getInt("id"));
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    public void displayAddProduct(ActionEvent actionEvent) {
        try {
            refreshSelectedIndices();

            if(indexOfListSelected != -1) {
                List<Detail> productDetails = new ArrayList<>();
                StageManager.getInstance().loadExtraPageWithDetails(dotenv.get("SHARED_DETAILS_PRODUCT"), productDetails);
            }

            displayProducts(lists.getJSONObject(indexOfListSelected).getInt("id"));
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    public void displayModifyProduct(ActionEvent actionEvent) throws Exception {
        try {
            refreshSelectedIndices();

            if(indexOfProductSelected != -1) {

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
                Product productToModify = jsonToProduct(product);

                controller.init(lists.getJSONObject(indexOfListSelected).getInt("id"), "Modify", productToModify);

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
