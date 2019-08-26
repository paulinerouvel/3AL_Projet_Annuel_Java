package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.models.Order;
import fr.wastemart.maven.javaclient.models.Product;
import fr.wastemart.maven.javaclient.services.Logger;
import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import static fr.wastemart.maven.javaclient.services.Order.fetchOrder;
import static fr.wastemart.maven.javaclient.services.Order.jsonToOrder;
import static fr.wastemart.maven.javaclient.services.Product.fetchProductsByOrder;
import static fr.wastemart.maven.javaclient.services.Product.jsonToProduct;


public class ListOrdersController extends GenericController {
    private JSONArray orders;
    private JSONArray products;
    private Integer indexOfProductSelected;
    private Integer indexOfListSelected;
    private Integer swapIdWarehouse;

    // order
    @FXML TableView<Order> ordersTable;
    @FXML TableColumn<Object, Object> orderId;
    @FXML TableColumn<Object, Object> orderDate;
    @FXML TableColumn<Object, Object> orderUser;
    // products
    @FXML TableView<Product> productsTable;
    @FXML TableColumn<Object, Object> productName;
    @FXML TableColumn<Object, Object> productDesc;
    @FXML TableColumn<Object, Object> productPrice;
    @FXML TableColumn<Object, Object> productInitialPrice;
    @FXML TableColumn<Object, Object> productDlc;
    @FXML TableColumn<Object, Object> productAvailable;
    @FXML TableColumn<Object, Object> productDate;
    @FXML TableColumn<Object, Object> productQuantity;

    @FXML Label saveLabel;

    @Override
    public void init() throws Exception {
        displayOrderList();
        displayProductsByOrder(orders.getJSONObject(0).getInt("id"));
        ordersTable.getSelectionModel().selectFirst();

    }

    private void displayOrderList() throws Exception {

        ordersTable.getItems().clear();
        orders = fetchOrder(UserInstance.getInstance().getTokenValue());

        orderId.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        orderUser.setCellValueFactory(new PropertyValueFactory<>("utilisateur_id"));

        for (int i = 0; i < orders.length(); i++) {
            JSONObject order = orders.getJSONObject(i);
            //Order orderElement = jsonToOrder(order);

            System.out.println("(SharedListOrdersController.displayOrderList) Order : " + order);
            Order orderElement = jsonToOrder(order);

            ordersTable.getItems().add(orderElement);
        }
    }

    private void displayProductsByOrder(Integer id) throws Exception {
        productsTable.getItems().clear();
        products = fetchProductsByOrder(id);

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

    public void refreshSelectedIndices() {
        this.indexOfProductSelected = productsTable.getSelectionModel().getSelectedIndex();
        this.indexOfListSelected = ordersTable.getSelectionModel().getSelectedIndex();
    }

    public void clickItem() {
        refreshSelectedIndices();

        if(indexOfListSelected != -1){
            try {
                displayProductsByOrder(orders.getJSONObject(indexOfListSelected).getInt("id"));
            } catch (Exception e) {
                Logger.getInstance().reportError(e);
                setInfoErrorOccurred();
            }
        }
    }

    // Return button
    public void displayMainPage() {
        StageManager.getInstance().displayMainPage(UserInstance.getInstance());
    }
}