package fr.wastemart.maven.javaclient.services.Details;

import fr.wastemart.maven.javaclient.models.Product;

import java.util.List;

public class ProductListDetail implements Detail {
    private List<fr.wastemart.maven.javaclient.models.Product> productList;

    public ProductListDetail(List<fr.wastemart.maven.javaclient.models.Product> products) {
        productList=products;
    }

    public Product getProductList(Integer index){
        return productList.get(index);
    }
}
