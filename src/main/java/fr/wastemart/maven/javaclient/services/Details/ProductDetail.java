package fr.wastemart.maven.javaclient.services.Details;

import fr.wastemart.maven.javaclient.models.Product;

public class ProductDetail implements Detail {
    private fr.wastemart.maven.javaclient.models.Product product;

    public ProductDetail(Product productDetail) {
        product = productDetail;
    }

    public Product getValue(Integer index){
        return product;
    }


}