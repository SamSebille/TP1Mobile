package ca.qc.bdeb.c5gm.tp1moblie.Activities;

import java.util.Comparator;
import java.util.Locale;
import java.util.UUID;

/**
 * Classe representant une entreprise ajoutée et modifiable par l'utilisateur.
 */
public class Entreprise {
    private UUID id;
    private String nom;
    private String contact;
    private String email;
    private String telephone;
    private String siteWeb;
    private String adresse;
    private String date_contact;
    private boolean estFavorite;

    public Entreprise(String nom, String contact, String email, String telephone, String siteWeb, String adresse, String date_contact, boolean estFavorite) {
        this.nom = nom;
        this.contact = contact;
        this.email = email;
        this.telephone = telephone;
        this.siteWeb = siteWeb;
        this.adresse = adresse;
        this.date_contact = date_contact;
        this.estFavorite = estFavorite;
    }

    public Entreprise(String nom, String contact, String email, String telephone, String siteWeb, String adresse, String date_contact) {
        this.id = UUID.randomUUID();
        this.nom = nom;
        this.contact = contact;
        this.email = email;
        this.telephone = telephone;
        this.siteWeb = siteWeb;
        this.adresse = adresse;
        this.date_contact = date_contact;
        this.estFavorite = false;
    }

    public Entreprise(UUID id, String nom, String contact, String email, String telephone, String siteWeb, String adresse, String date_contact, boolean estFavorite) {
        this.id = id;
        this.nom = nom;
        this.contact = contact;
        this.email = email;
        this.telephone = telephone;
        this.siteWeb = siteWeb;
        this.adresse = adresse;
        this.date_contact = date_contact;
        this.estFavorite = estFavorite;
    }

    public Entreprise(UUID id, String nom, String contact, String email, String telephone, String siteWeb, String adresse, String date_contact) {
        this.id = id;
        this.nom = nom;
        this.contact = contact;
        this.email = email;
        this.telephone = telephone;
        this.siteWeb = siteWeb;
        this.adresse = adresse;
        this.date_contact = date_contact;
        this.estFavorite = false;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setEstFavorite(boolean est_favorite) {
        this.estFavorite = est_favorite;
    }

    public boolean estFavorite() {
        return estFavorite;
    }

    public UUID getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getSiteWeb() {
        return siteWeb;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getDate_contact() {
        return date_contact;
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

        if (a.getDate_contact() == null || b.getDate_contact() == null)
            return 0;

        String[] tempA = a.getDate_contact().split("/");
        dateA = (Integer.parseInt(tempA[2]) * 10000) +
                (Integer.parseInt(tempA[1]) * 100) + (Integer.parseInt(tempA[0]));

        String[] tempB = b.getDate_contact().split("/");
        dateB = (Integer.parseInt(tempB[2]) * 10000) +
                (Integer.parseInt(tempB[1]) * 100) + (Integer.parseInt(tempB[0]));

        return dateA - dateB;
    }
}
