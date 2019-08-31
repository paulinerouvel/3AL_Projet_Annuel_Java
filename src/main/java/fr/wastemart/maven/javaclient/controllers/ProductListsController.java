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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

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
        if(listArchiveCheckBox != null) {
            listArchiveCheckBox.setSelected(false);
        }

        StringDetail optionDetail = (StringDetail) detail.get(0);
        option = optionDetail.getValue();

        refreshDisplay();
    }

    @FXML
    private boolean refreshDisplay() {
        refreshSelectedIndices();
        clearInfoText();

        boolean result = false;
        if(displayProductLists()){
            listsTable.getSelectionModel().selectFirst();

            switch (option) {
                case "all":
                    result = displayProducts(null);
                    break;
                case "me":
                    result = displayProducts(listsTable.getSelectionModel().getSelectedItem().getId());
                    break;
                case "pro":
                    result = displayProducts(listsTable.getSelectionModel().getSelectedItem().getId());
                    break;
            }
        }
        return result;

    }

    @FXML
    private boolean displayProductLists() {
        try {
            listsTable.getItems().clear();
            System.out.println(String.valueOf(option));


            switch (option) {

                case "all":
                    lists = fetchAllProductLists(UserInstance.getInstance().getTokenValue());
                    break;
                case "listall":
                    lists = fetchAllProductLists(UserInstance.getInstance().getTokenValue());
                    break;
                case "me":
                    lists = fetchProductLists(UserInstance.getInstance().getUser().getId(), UserInstance.getInstance().getTokenValue());
                    break;
                case "pro":
                    lists = fetchAllProductListsByUserCategory(2, UserInstance.getInstance().getTokenValue());
                    System.out.println(lists);
                    break;
            }
            System.out.println(lists);
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

            switch (option) {
                case "all":
                    products = fetchAllProducts();
                    break;
                case "listall":
                    if(!lists.isEmpty()){
                        products = fetchProducts(selectedList, UserInstance.getInstance().getTokenValue());
                    }
                    break;
                case "me":
                    if (!lists.isEmpty()) {
                        System.out.println("me");
                        System.out.println(lists);
                        products = fetchProducts(selectedList, UserInstance.getInstance().getTokenValue());
                    }
                    break;
                case "pro":
                    if (!lists.isEmpty()) {
                        System.out.println("pro");
                        System.out.println(lists);
                        products = fetchProducts(selectedList, UserInstance.getInstance().getTokenValue());
                        System.out.println(products);
                    }
                    break;
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
        System.out.println("Selected : "+ indexOfListSelected);

        if(indexOfListSelected != -1){
            try {
                displayProducts(listsTable.getSelectionModel().getSelectedItem().getId());
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
                    "",
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
                Integer listToRemoveId = listsTable.getSelectionModel().getSelectedItem().getId();
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

            if (indexOfListSelected != -1 && listsTable.getSelectionModel().getSelectedItem().getEstArchive() != 1) {




                ProductList proL = listsTable.getSelectionModel().getSelectedItem();
                proL.setEstArchive(1);

                if(updateProductList(proL, UserInstance.getInstance().getTokenValue())){

                    setInfoText("Liste soumisse ! ");

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
            details.add(new StringDetail("addtolist"));
            details.add(new IntegerDetail(listsTable.getSelectionModel().getSelectedItem().getId())); // Id de la liste
            details.add(new IntegerDetail(getWarehouse())); // Id de l'entrepôt
            details.add(new IntegerDetail(getDestinataire())); // Id du destinataire
            StageManager.getInstance().loadExtraPageWithDetails(dotenv.get("SHARED_DETAILS_PRODUCT"), details);

        } else if(option.equals("me")) {
            setInfoText("Veuillez sélectionner une liste");

        } else if (option.equals("all")) {
            details.add(new StringDetail("add"));
            details.add(new IntegerDetail(getWarehouse())); // Id de l'entrepôt
            details.add(new IntegerDetail(getDestinataire())); // Id du destinataire
            StageManager.getInstance().loadExtraPageWithDetails(dotenv.get("SHARED_DETAILS_PRODUCT"), details);
        }

        refreshDisplay();
    }

    @FXML
    private void displayModifyProduct() {
        clearInfoText();
        refreshSelectedIndices();

        try {
            if(indexOfProductSelected != -1) {
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
    private void affectToList() {
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

        if(indexOfProductSelected != -1) {
            try {
                JSONObject product = products.getJSONObject(indexOfProductSelected).put("enRayon", 1);
                Product productElement = jsonToProduct(product);
                
                if(productElement != null) {
                    productElement.setEnRayon(true);
                    productElement.setEntrepotwm(getWarehouse());
                    productElement.setDateMiseEnRayon(new Date().toInstant().toString().split("T")[0]);

                    if (updateProduct(productElement, UserInstance.getInstance().getTokenValue())) {
                        setInfoText("Produit validé");
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
        else{
            setInfoText("Veuillez séléctionner un produit");
        }
    }

    @FXML
    private void refuseProduct() {
        clearInfoText();
        refreshSelectedIndices();

        try {

            if (indexOfProductSelected != -1) {
                JSONObject product = products.getJSONObject(indexOfProductSelected).put("enRayon", 0);
                Product productElement = jsonToProduct(product);

                if (productElement != null) {
                    productElement.setEnRayon(false);
                    productElement.setEntrepotwm(null);

                    if (updateProduct(productElement, UserInstance.getInstance().getTokenValue())) {
                        setInfoText("Produit refusé");
                    } else {
                        setInfoErrorOccurred();
                    }

                    refreshDisplay();
                }
            }
            else{
                setInfoText("Veuillez séléctionner un produit");
            }
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    private void updateListLibelle(TableColumn.CellEditEvent<Object, Object> e) {
        listsTable.getSelectionModel().getSelectedItem().setLibelle((String)e.getNewValue());
        updateProductList(listsTable.getSelectionModel().getSelectedItem(), UserInstance.getInstance().getTokenValue());
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
            setInfoText("Aucune liste sélectionnée");
        }
    }

    private boolean fillProductLists() {
        if (!lists.isEmpty()) {
            listId.setCellValueFactory(new PropertyValueFactory<>("id"));
            listName.setCellValueFactory(new PropertyValueFactory<>("libelle"));
            StringConverter libelle = new DefaultStringConverter();
            listName.setCellFactory(TextFieldTableCell.forTableColumn(libelle));
            listName.setOnEditCommit(this::updateListLibelle);

            if(option.equals("all") || option.equals("pro")) {
                listUser.setCellValueFactory(new PropertyValueFactory<>("userId"));
            }
            listEstArchive.setCellValueFactory(new PropertyValueFactory<>("estArchive"));

            for (int i = 0; i < lists.length(); i++) {
                if ((listArchiveCheckBox != null && listArchiveCheckBox.isSelected()) || lists.getJSONObject(i).getInt("estArchive") != 1) {
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
        System.out.println("ozejfpoizejopf");
        if(products != null && !products.isEmpty()) {
            System.out.println("ozejfpoizejopf");
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
                System.out.println("Product is : "+ products.getJSONObject(i));
                if(product != null && (listsTable.getSelectionModel().getSelectedItem().getEstArchive() == 0) ||
                        (!option.equals("pro") && (listArchiveCheckBox.isSelected()))){
                    System.out.println("ozejfpoizejopf");
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


    private Integer getWarehouse() {
        Integer result = 0;
        if(indexOfListSelected != -1){
            result = (listsTable.getSelectionModel().getSelectedItem().getId() % 2) + 1;
        } else {
            result = (int) (Math.random() * (2)) + 1 ;
        }
        System.out.println("Warehouse :");
        System.out.println(result);
        return result;
    }

    private Integer getDestinataire() {

        Integer result = (int) (Math.random() * (2)) == 0 ? 1 : 3;
        System.out.println("Destinataire :");
        System.out.println(result);
        return result;
    }

    // Return button
    public void displayMainPage() {
        StageManager.getInstance().displayMainPage(UserInstance.getInstance());
    }

}
