package fr.wastemart.maven.javaclient.services;

import fr.wastemart.maven.javaclient.models.Product;

import java.util.List;

interface Details {

}

abstract class ProductDetails implements Details {
    private List<Product> productList;

    public ProductDetails(List<Product> products) {
        productList=products;
    }

    public Product returnProduct(Integer index){
        return productList.get(index);
    }
}
