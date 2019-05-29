import java.time.LocalDate;

public class Product {

    private Integer id;
    private String libelle;
    private String photo;
    private Integer reduction;
    private String codeBarre;
    private LocalDate dateMiseEnRayon;
    private Integer categorieProduit_id;
    private Integer stock_id;
    private Integer Liste_de_Produits_id;

    public Product(Integer id, String libelle, String photo, Integer reduction, String codeBarre, LocalDate dateMiseEnRayon, Integer categorieProduit_id, Integer stock_id, Integer liste_de_Produits_id) {
        this.id = id;
        this.libelle = libelle;
        this.photo = photo;
        this.reduction = reduction;
        this.codeBarre = codeBarre;
        this.dateMiseEnRayon = dateMiseEnRayon;
        this.categorieProduit_id = categorieProduit_id;
        this.stock_id = stock_id;
        Liste_de_Produits_id = liste_de_Produits_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getReduction() {
        return reduction;
    }

    public void setReduction(Integer reduction) {
        this.reduction = reduction;
    }

    public String getCodeBarre() {
        return codeBarre;
    }

    public void setCodeBarre(String codeBarre) {
        this.codeBarre = codeBarre;
    }

    public LocalDate getDateMiseEnRayon() {
        return dateMiseEnRayon;
    }

    public void setDateMiseEnRayon(LocalDate dateMiseEnRayon) {
        this.dateMiseEnRayon = dateMiseEnRayon;
    }

    public Integer getCategorieProduit_id() {
        return categorieProduit_id;
    }

    public void setCategorieProduit_id(Integer categorieProduit_id) {
        this.categorieProduit_id = categorieProduit_id;
    }

    public Integer getStock_id() {
        return stock_id;
    }

    public void setStock_id(Integer stock_id) {
        this.stock_id = stock_id;
    }

    public Integer getListe_de_Produits_id() {
        return Liste_de_Produits_id;
    }

    public void setListe_de_Produits_id(Integer liste_de_Produits_id) {
        Liste_de_Produits_id = liste_de_Produits_id;
    }
}
