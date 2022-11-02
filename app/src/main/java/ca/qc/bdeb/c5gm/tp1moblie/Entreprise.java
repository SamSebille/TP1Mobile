package ca.qc.bdeb.c5gm.tp1moblie;

import com.google.android.gms.maps.model.LatLng;

import java.util.Comparator;
import java.util.Locale;

/**
 * Classe representant une entreprise ajoutée et modifiable par l'utilisateur.
 */
public class Entreprise {
    private int id;
    private String nom;
    private String contact;
    private String courriel;
    private String telephone;
    private String web;
    private String adresse;
    private String date;
    private LatLng position;
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

    public Entreprise(int id, String nom, String contact, String courriel, String telephone, String web, String adresse, String date) {
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

    public void setId(int id) {
        this.id = id;
    }

    public void setFavori(boolean favori) {
        this.favori = favori;
    }

    public int getId() {
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

/**
 * Classe implémentant l'interface Comparator pour le tri par nom des entreprises
 */
class SortByName implements Comparator<Entreprise> {
    public int compare(Entreprise a, Entreprise b) {
        return a.getNom().toLowerCase(Locale.ROOT)
                .compareTo(b.getNom().toLowerCase(Locale.ROOT));
    }
}

/**
 * Classe implémentant l'interface Comparator pour le tri par date de contact des entreprises
 */
class SortByDate implements Comparator<Entreprise> {
    public int compare(Entreprise a, Entreprise b) {
        int dateA, dateB;

        String[] tempA = a.getDate().split("/");
        dateA = (Integer.parseInt(tempA[2]) * 10000) +
                (Integer.parseInt(tempA[1]) * 100) + (Integer.parseInt(tempA[0]));

        String[] tempB = b.getDate().split("/");
        dateB = (Integer.parseInt(tempB[2]) * 10000) +
                (Integer.parseInt(tempB[1]) * 100) + (Integer.parseInt(tempB[0]));

        return dateA - dateB;
    }
}
