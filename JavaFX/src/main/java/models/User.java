package models;

import java.time.LocalDate;

public class User {

    private Integer id;
    private String libelle;
    private String nom;
    private String prenom;
    private String mail;
    private String pseudo;
    private String mdp;
    private String photo;
    private String desc;
    private Integer tailleOrganisme;
    private String statut;
    private String Siret;
    private LocalDate dateDeNaissance;
    private Integer nbPointsSourire;
    private String tel;
    private String adresse;
    private String ville;
    private Integer codePostal;

    public User() {
    }

    public User(Integer id, String libelle, String nom, String prenom, String mail, String tel, String adresse, String ville, Integer codePostal, String pseudo, String mdp, String photo, String desc, Integer tailleOrganisme, String statut, String siret, LocalDate dateDeNaissance, Integer nbPointsSourire) {
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
        this.statut = statut;
        this.Siret = siret;
        this.dateDeNaissance = dateDeNaissance;
        this.nbPointsSourire = nbPointsSourire;
    }

    public Integer getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getMail() {
        return mail;
    }

    public String getTel() {
        return tel;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getVille() {
        return ville;
    }

    public Integer getCodePostal() {
        return codePostal;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getMdp() {
        return mdp;
    }

    public String getPhoto() {
        return photo;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getTailleOrganisme() {
        return tailleOrganisme;
    }

    public String getStatut() {
        return statut;
    }

    public String getSiret() {
        return Siret;
    }

    public LocalDate getDateDeNaissance() {
        return dateDeNaissance;
    }

    public Integer getNbPointsSourire() {
        return nbPointsSourire;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public void setCodePostal(Integer codePostal) {
        this.codePostal = codePostal;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setTailleOrganisme(Integer tailleOrganisme) {
        this.tailleOrganisme = tailleOrganisme;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setSiret(String siret) {
        Siret = siret;
    }

    public void setDateDeNaissance(LocalDate dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public void setNbPointsSourire(Integer nbPointsSourire) {
        this.nbPointsSourire = nbPointsSourire;
    }
}
