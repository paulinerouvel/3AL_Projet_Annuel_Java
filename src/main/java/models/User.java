package models;

import java.time.LocalDate;

public class User {

    private Integer id;
    private String libelle;
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
    private Integer estValide;
    private String siret;
    private LocalDate dateDeNaissance;
    private Integer nbPointsSourire;

    public User() {
    }

    public User(Integer id, String libelle, String nom, String prenom, String mail, String tel, String adresse,
                String ville, Integer codePostal, String pseudo, String mdp, String photo, String desc,
                Integer tailleOrganisme, Integer estValide, String siret, LocalDate dateDeNaissance,
                Integer nbPointsSourire) {
        this.id = id;
        this.libelle = libelle;
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.tel = tel;
        this.adresse = adresse;
        this.ville = ville;
        this.codePostal = codePostal;
        this.pseudo = pseudo;
        this.mdp = mdp;
        this.photo = photo;
        this.desc = desc;
        this.tailleOrganisme = tailleOrganisme;
        this.estValide = estValide;
        this.siret = siret;
        this.dateDeNaissance = dateDeNaissance;
        this.nbPointsSourire = nbPointsSourire;
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

    public Integer getEstValide() {
        return estValide;
    }

    public void setEstValide(Integer estValide) {
        this.estValide = estValide;
    }

    public String getSiret() {
        return siret;
    }

    public void setSiret(String siret) {
        this.siret = siret;
    }

    public LocalDate getDateDeNaissance() {
        return dateDeNaissance;
    }

    public void setDateDeNaissance(LocalDate dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public Integer getNbPointsSourire() {
        return nbPointsSourire;
    }

    public void setNbPointsSourire(Integer nbPointsSourire) {
        this.nbPointsSourire = nbPointsSourire;
    }
}
