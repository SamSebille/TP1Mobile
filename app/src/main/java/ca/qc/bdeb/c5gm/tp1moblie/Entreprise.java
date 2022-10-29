package ca.qc.bdeb.c5gm.tp1moblie;

public class Entreprise {
    private long id;
    private String nom;
    private String contact;
    private String courriel;
    private String telephone;
    private String web;
    private String adresse;
    private String date;
    private boolean favori;

    public Entreprise(String nom, String contact, String courriel, String telephone, String web, String adresse, String date) {
        this.nom = nom;
        this.contact = contact;
        this.courriel = courriel;
        this.telephone = telephone;
        this.web = web;
        this.adresse = adresse;
        this.date = date;
        this.favori = false;
    }

    public Entreprise(long id, String nom, String contact, String courriel, String telephone, String web, String adresse, String date) {
        this.id = id;
        this.nom = nom;
        this.contact = contact;
        this.courriel = courriel;
        this.telephone = telephone;
        this.web = web;
        this.adresse = adresse;
        this.date = date;
        this.favori = false;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFavori(boolean favori) {
        this.favori = favori;
    }

    public long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getContact() {
        return contact;
    }

    public String getCourriel() {
        return courriel;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getWeb() {
        return web;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getDate() {
        return date;
    }
}
