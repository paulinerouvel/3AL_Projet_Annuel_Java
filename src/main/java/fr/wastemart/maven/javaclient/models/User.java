package fr.wastemart.maven.javaclient.models;

public class User {

    private Integer id;
    private String libelle;
    private String categorieUtilisateur;
    private String nom;
    private String prenom;
    private String mail;
    private String tel;
    private String adresse;
    private String ville;
    private Integer codePostal;
    private String pseudo;
    private String mdp;
    private String photo;
    private String desc;
    private Integer tailleOrganisme;
    private Boolean estValide;
    private String siret;
    private String dateDeNaissance;
    private Integer nbPointsSourire;

    // Prend null ou .isEmpty() car il peut y avoir null OU ""
    public User(Integer id, String libelle, String categorieUtilisateur, String nom, String prenom, String mail,
                String tel, String adresse, String ville, Integer codePostal, String pseudo, String mdp, String photo,
                String desc, Integer tailleOrganisme, Boolean estValide, String siret, String dateDeNaissance,
                Integer nbPointsSourire) {
        this.id = id;
        this.libelle = (libelle == null ||libelle.isEmpty()) ? null : libelle; // Nullable
        this.categorieUtilisateur = categorieUtilisateur;
        this.nom = (nom == null || nom.isEmpty()) ? null : nom; // Nullable
        this.prenom = prenom.isEmpty() ? null : prenom; // Nullable
        this.mail = mail;
        this.tel = tel;
        this.adresse = adresse;
        this.ville = ville;
        this.codePostal = codePostal;
        this.pseudo = pseudo;
        this.mdp = mdp;
        this.photo = (photo == null || photo.isEmpty()) ? null : photo; // Nullable
        this.desc = (desc == null || desc.isEmpty()) ? null : desc; // Nullable
        this.tailleOrganisme = tailleOrganisme; // Nullable
        this.estValide = estValide;
        this.siret = (siret == null || siret.isEmpty()) ? null : siret; // Nullable
        this.dateDeNaissance = (dateDeNaissance == null || dateDeNaissance.isEmpty()) ? null : dateDeNaissance; // Nullable
        this.nbPointsSourire = nbPointsSourire; // Nullable
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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Integer getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(Integer codePostal) {
        this.codePostal = codePostal;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getTailleOrganisme() {
        return tailleOrganisme;
    }

    public void setTailleOrganisme(Integer tailleOrganisme) {
        this.tailleOrganisme = tailleOrganisme;
    }

    public Boolean getEstValide() {
        return estValide;
    }

    public void setEstValide(Boolean estValide) {
        this.estValide = estValide;
    }

    public String getSiret() {
        return siret;
    }

    public void setSiret(String siret) {
        this.siret = siret;
    }

    public String getDateDeNaissance() {
        return dateDeNaissance;
    }

    public void setDateDeNaissance(String dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public Integer getNbPointsSourire() {
        return nbPointsSourire;
    }

    public void setNbPointsSourire(Integer nbPointsSourire) {
        this.nbPointsSourire = nbPointsSourire;
    }

    public String getCategorieUtilisateur() {
        return categorieUtilisateur;
    }

    public void setCategorieUtilisateur(String categorieUtilisateur) {
        this.categorieUtilisateur = categorieUtilisateur;
    }
}
