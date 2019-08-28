package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.models.Product;
import fr.wastemart.maven.javaclient.models.ProductList;
import fr.wastemart.maven.javaclient.services.Details.Detail;
import fr.wastemart.maven.javaclient.services.Details.IntegerDetail;
import fr.wastemart.maven.javaclient.services.Details.ProductDetail;
import fr.wastemart.maven.javaclient.services.Details.StringDetail;
import fr.wastemart.maven.javaclient.services.Logger;
import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
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
import static fr.wastemart.maven.javaclient.services.User.fetchUser;


public class ProductListsController extends GenericController {
    private String option;
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
    TableColumn<Object, Object> productDlc;
    @FXML
    TableColumn<Object, Object> productQuantity;
    @FXML
    TableColumn<Object, Object> productAvailable;
    @FXML
    TableColumn<Object, Object> productDate;
    @FXML
    TableColumn<Object, Object> productList;

    public void init(List<Detail> detail) throws Exception {
        listArchiveCheckBox.setSelected(false);


        StringDetail optionDetail = (StringDetail) detail.get(0);
        option = optionDetail.getValue();

        refreshDisplay();

        refreshSelectedIndices();
    }

    @FXML
    private boolean refreshDisplay() {
        boolean result = false;
        if(displayProductLists()){
            if (option.equals("all")) {
                result = displayProducts(null);
            } else if (option.equals("me")) {
                result = displayProducts(lists.getJSONObject(0).getInt("id"));
                listsTable.getSelectionModel().selectFirst();
            } else if (option.equals("pro")) {
                result = displayProducts(lists.getJSONObject(0).getInt("id"));
                listsTable.getSelectionModel().selectFirst();
            }
        }
        return result;

    }

    @FXML
    private boolean displayProductLists() {
        try {
            listsTable.getItems().clear();

            if (option.equals("all")) {
                lists = fetchAllProductLists(UserInstance.getInstance().getTokenValue());
            } else if (option.equals("me")) {
                lists = fetchProductLists(UserInstance.getInstance().getUser().getId(), UserInstance.getInstance().getTokenValue());
            } else if (option.equals("pro")) {
                lists = fetchAllProductListsByUserCategory(2, UserInstance.getInstance().getTokenValue());
            }
            return fillProductLists();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            return false;
        }
    }

    @FXML
    private boolean displayProducts(Integer selectedList) {
        try {
            productsTable.getItems().clear();

            if (option.equals("all")) {
                products = fetchAllProducts();
            } else if (option.equals("me")) {
                if (!lists.isEmpty()) {
                    products = fetchProducts(selectedList, UserInstance.getInstance().getTokenValue());
                }
            } else if (option.equals("pro")) {
                if (!lists.isEmpty()) {
                    products = fetchProducts(selectedList, UserInstance.getInstance().getTokenValue());
                }
            }
            return fillProducts();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            return false;
        }
    }


    @FXML
    public void clickMyList() {
        clearInfoText();
        refreshSelectedIndices();

        if(indexOfListSelected != -1){
            try {
                displayProducts(lists.getJSONObject(indexOfListSelected).getInt("id"));
            } catch (Exception e) {
                Logger.getInstance().reportError(e);
                setInfoErrorOccurred();
            }
        }
    }

    @FXML
    public void clickListAll() {
        refreshSelectedIndices();
    }

    @FXML
    private void createList() {
        clearInfoText();
        refreshSelectedIndices();

        try {
            ProductList productList = new ProductList(-1,
                    "Test",
                    LocalDate.now(),
                    UserInstance.getInstance().getUser().getId(),
                    0);

            createProductList(productList, UserInstance.getInstance().getTokenValue());

            displayProductLists();
            if(option.equals("all")) {
                displayProducts(null);
            } else if(option.equals("me")) {
                displayProductLists();
            }
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    @FXML
    private void deleteList() {
        clearInfoText();

        try {
            refreshSelectedIndices();

            if(indexOfListSelected != -1){
                Integer listToRemoveId = lists.getJSONObject(indexOfListSelected).getInt("id");
                if(removeProductsList(listToRemoveId, UserInstance.getInstance().getTokenValue())){
                    setInfoText("Liste supprimmée");
                    refreshDisplay();
                } else {
                    setInfoErrorOccurred();
                }
            }
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    @FXML
    private void submitList() {
        clearInfoText();
        refreshSelectedIndices();

        try {

            if (indexOfListSelected != -1 && lists.getJSONObject(indexOfListSelected).getInt("estArchive") != 1) {

                JSONObject list = lists.getJSONObject(indexOfListSelected);
                ProductList listElement = jsonToProductList(list);

                ArrayList<Product> productList = new ArrayList<Product>();
                for (int i = 0; i < products.length(); i++) {
                    JSONObject product = products.getJSONObject(i);
                    productList.add(jsonToProduct(product));
                }

                // Affecte la liste de produits à un entrepot
                if(affectProductListToWarehouse(productList, UserInstance.getInstance().getUser().getVille(), UserInstance.getInstance().getTokenValue())){ // Réussite
                    listElement.setEstArchive(1);
                    listElement.setDate(LocalDate.now());
                    updateProductList(listElement, UserInstance.getInstance().getTokenValue());
                    setInfoText("Liste postée");
                } else {
                    setInfoErrorOccurred();
                }
            } else {
                setInfoText("Veuillez sélectionner une liste");
            }

            refreshDisplay();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }


    @FXML
    private void displayAddProduct() {
        clearInfoText();
        refreshSelectedIndices();

        List<Detail> details = new ArrayList<Detail>();

        if(option.equals("me") && indexOfListSelected != -1) {
            details.add(new StringDetail("add"));
            details.add(new IntegerDetail(listsTable.getSelectionModel().getSelectedItem().getId()));
            StageManager.getInstance().loadExtraPageWithDetails(dotenv.get("SHARED_DETAILS_PRODUCT"), details);
        } else if(option.equals("me")) {
            setInfoText("Veuillez sélectionner une liste");
        } else if (option.equals("all")) {
            details.add(new StringDetail("add"));
            StageManager.getInstance().loadExtraPageWithDetails(dotenv.get("SHARED_DETAILS_PRODUCT"), details);
        }
        refreshDisplay();
    }

    @FXML
    private void displayModifyProduct() {
        clearInfoText();
        refreshSelectedIndices();

        try {
            if(indexOfProductSelected != 1) {
                Product selectedProduct = jsonToProduct(products.getJSONObject(indexOfProductSelected));

                List<Detail> details = new ArrayList<Detail>();
                details.add(new StringDetail("modify"));
                details.add(new ProductDetail(selectedProduct));


                StageManager.getInstance().loadExtraPageWithDetails(dotenv.get("SHARED_DETAILS_PRODUCT"), details);

                refreshDisplay();
            } else {
                setInfoText("Veuillez sélectionner un produit");
            }
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    @FXML
    private void deleteProduct() {
        clearInfoText();
        refreshSelectedIndices();

        try {
            if (indexOfProductSelected != -1){
                if(fr.wastemart.maven.javaclient.services.Product.deleteProduct(products.getJSONObject(indexOfProductSelected).getInt("id"), UserInstance.getInstance().getTokenValue())){
                    setInfoText("Produit supprimé");
                } else {
                    setInfoErrorOccurred();
                }
            }

            refreshDisplay();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    @FXML
    private void addToList() {
        clearInfoText();
        refreshSelectedIndices();

        if (indexOfListSelected != -1 && indexOfProductSelected != -1) {
            Product productToAdd = productsTable.getSelectionModel().getSelectedItem();
            productToAdd.setListProduct(listsTable.getSelectionModel().getSelectedItem().getId());

            if (updateProduct(productToAdd, UserInstance.getInstance().getTokenValue())) {

                refreshDisplay();
                setInfoText("Produit ajouté à la liste");
            } else {
                setInfoErrorOccurred();
            }
        } else {
            setInfoText("Veuillez sélectionner une liste et un produit");
        }
    }

    @FXML
    private void removeFromList() {
        clearInfoText();
        refreshSelectedIndices();

        if (indexOfListSelected != -1 && indexOfProductSelected != -1) {
            Product productToRemove = productsTable.getSelectionModel().getSelectedItem();
            productToRemove.setListProduct(null);

            if(updateProduct(productToRemove, UserInstance.getInstance().getTokenValue())){
                refreshDisplay();
                setInfoText("Produit supprimé de la liste");
            } else {
                setInfoErrorOccurred();
            }
        } else {
            setInfoText("Veuillez sélectionner une liste et un produit");
        }
    }

    @FXML
    private void validateProduct() {
        clearInfoText();
        refreshSelectedIndices();

        if(indexOfListSelected != -1) {
            try {
                JSONObject product = products.getJSONObject(indexOfProductSelected).put("enRayon", 1);
                Product productElement = jsonToProduct(product);

                productElement.setEnRayon(true);
                if(updateProduct(productElement, UserInstance.getInstance().getTokenValue())){
                    setInfoText("Produit validé");
                } else {
                    setInfoErrorOccurred();
                }

                refreshDisplay();
            } catch (Exception e) {
                Logger.getInstance().reportError(e);
                setInfoErrorOccurred();
            }
        }
    }

    @FXML
    private void refuseProduct() {
        clearInfoText();
        refreshSelectedIndices();

        try {

            if (indexOfListSelected != -1) {
                JSONObject product = products.getJSONObject(indexOfProductSelected).put("enRayon", 0);
                Product productElement = jsonToProduct(product);

                productElement.setEnRayon(false);

                if(updateProduct(productElement, UserInstance.getInstance().getTokenValue())){
                    setInfoText("Produit refusé");
                } else {
                    setInfoErrorOccurred();
                }

                refreshDisplay();
            }
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    @FXML
    public void contactUser() {
        clearInfoText();
        refreshSelectedIndices();

        if (indexOfListSelected != -1) {
            try {
                String userMail = fetchUser(listsTable.getSelectionModel().getSelectedItem().getUserId()).getMail();

                StringDetail mail = new StringDetail(userMail);

                List<Detail> contactDetails = new ArrayList<>();
                contactDetails.add(mail);

                StageManager.getInstance().loadExtraPageWithDetails(dotenv.get("SHARED_DETAILS_CONTACT"), contactDetails);
            } catch (Exception e) {
                Logger.getInstance().reportError(e);
                setInfoErrorOccurred();
            }
        } else {
            System.out.println("Test");
            setInfoText("Aucune liste sélectionnée");
        }
    }

    private boolean fillProductLists() {
        if (!lists.isEmpty()) {
            listId.setCellValueFactory(new PropertyValueFactory<>("id"));
            listName.setCellValueFactory(new PropertyValueFactory<>("libelle"));
            if(option.equals("all") || option.equals("pro")) {
                listUser.setCellValueFactory(new PropertyValueFactory<>("userId"));
            }
            listEstArchive.setCellValueFactory(new PropertyValueFactory<>("estArchive"));

            for (int i = 0; i < lists.length(); i++) {
                if (listArchiveCheckBox.isSelected() || lists.getJSONObject(i).getInt("estArchive") != 1) {
                    ProductList list = jsonToProductList(lists.getJSONObject(i));
                    if(list != null){
                        listsTable.getItems().add(list);
                    } else {
                        setInfoText("Une ou plusieurs listes n'ont pas pu être récupérées");
                    }

                }
            }
            return true;
        } else {
            setInfoText("Aucune liste trouvée");
            return false;
        }

    }

    private boolean fillProducts() {
        if(!products.isEmpty()) {
            productName.setCellValueFactory(new PropertyValueFactory<>("libelle"));
            productDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
            productPrice.setCellValueFactory(new PropertyValueFactory<>("prix"));
            productDlc.setCellValueFactory(new PropertyValueFactory<>("dlc"));
            productQuantity.setCellValueFactory(new PropertyValueFactory<>("quantite"));
            productAvailable.setCellValueFactory(new PropertyValueFactory<>("enRayon"));
            productDate.setCellValueFactory(new PropertyValueFactory<>("dateMiseEnRayon"));
            if(option.equals("all")) {
                productList.setCellValueFactory(new PropertyValueFactory<>("listProduct"));
            }

            for (int i = 0; i < products.length(); i++) {
                Product product = jsonToProduct(products.getJSONObject(i));
                if(product != null) {
                    productsTable.getItems().add(product);
                } else {
                    setInfoText("Un ou plusieurs produits n'ont pas pu être récupérés");
                }
            }
            return true;
        } else {
            setInfoText("La liste est vide !");
            return false;
        }
    }

    private void refreshSelectedIndices() {
        this.indexOfProductSelected = productsTable.getSelectionModel().getSelectedIndex();
        this.indexOfListSelected = listsTable.getSelectionModel().getSelectedIndex();
    }



    // Return button
    public void displayMainPage() {
        StageManager.getInstance().displayMainPage(UserInstance.getInstance());
    }

}
