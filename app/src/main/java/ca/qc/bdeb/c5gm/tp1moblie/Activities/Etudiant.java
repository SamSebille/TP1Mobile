package ca.qc.bdeb.c5gm.tp1moblie.Activities;

import java.util.Comparator;
import java.util.Locale;
import java.util.UUID;

import ca.qc.bdeb.c5gm.tp1moblie.REST.ComptePOJO;

public class Etudiant {
    UUID id;
    String nom;
    String prenom;
    String email;
    boolean stageTrouve;
    int nombreEntreprise;

    public Etudiant(UUID id, String nom, String prenom, String email, boolean stageTrouve, int nombreEntreprise) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.stageTrouve = stageTrouve;
        this.nombreEntreprise = nombreEntreprise;
    }

    public Etudiant(ComptePOJO comptePOJO) {
        this.id = UUID.fromString(comptePOJO.getId());
        this.nom = comptePOJO.getNom();
        this.prenom = comptePOJO.getPrenom();
        this.email = comptePOJO.getEmail();
        this.stageTrouve = comptePOJO.getStageTrouve();
        this.nombreEntreprise = comptePOJO.getEntreprises().toArray().length;
    }

    public UUID getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public boolean isStageTrouve() {
        return stageTrouve;
    }

    public int getNombreEntreprise() {
        return nombreEntreprise;
    }

}

/**
 * Classe implémentant l'interface Comparator pour le tri par nom des entreprises
 */
class SortByStudentName implements Comparator<Etudiant> {
    public int compare(Etudiant a, Etudiant b) {
        return a.getNom().toLowerCase(Locale.ROOT)
                .compareTo(b.getNom().toLowerCase(Locale.ROOT));
    }
}

