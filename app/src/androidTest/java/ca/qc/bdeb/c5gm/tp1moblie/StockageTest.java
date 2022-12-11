package ca.qc.bdeb.c5gm.tp1moblie;

import android.content.Context;
import android.database.CursorIndexOutOfBoundsException;

import androidx.test.core.app.ApplicationProvider;

import com.google.common.truth.Truth;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

import ca.qc.bdeb.c5gm.tp1moblie.Activities.Entreprise;
import ca.qc.bdeb.c5gm.tp1moblie.Activities.Etudiant;
import ca.qc.bdeb.c5gm.tp1moblie.BD.Stockage;


public class StockageTest {
    private Stockage stockage;
    private Context context = ApplicationProvider.getApplicationContext();
    private UUID id = UUID.randomUUID();


    @Before
    public void setUp() throws Exception {
        stockage = Stockage.getInstance(context);
    }

    @After
    public void tearDown() throws Exception {
        stockage.close();
    }

    @Test
    public void ajouterEntreprise() {
        Entreprise entreprise = new Entreprise(id,"", "", "", "", "", "", "", false);
        boolean ajout = true;
        try {
            stockage.ajouterEntreprise(entreprise);
        } catch (Exception e) {
            ajout = false;
        }
        Truth.assertThat(ajout).isTrue();
    }

    @Test
    public void getEntreprises() {
        Entreprise entreprise1 = new Entreprise(id,"", "", "", "", "", "", "", false);
        stockage.ajouterEntreprise(entreprise1);
        ArrayList<Entreprise> entreprises = stockage.getEntreprises();
        Truth.assertThat(entreprises).isNotNull();
    }

    @Test
    public void getEntreprise() {
        Entreprise entreprise1 = new Entreprise(id,"", "", "", "", "", "", "", false);
        stockage.ajouterEntreprise(entreprise1);
        Entreprise entreprises = stockage.getEntreprise(id);
        Truth.assertThat(entreprises).isNotNull();
    }

    @Test
    public void updateEntreprise() {
        Entreprise entreprise = new Entreprise(id,"", "", "", "", "", "", "", false);
        stockage.ajouterEntreprise(entreprise);
        UUID id = entreprise.getId();
        entreprise = new Entreprise(id, "test", "test", "test", "test", "test", "yul", "?", false);
        stockage.updateEntreprise(entreprise);

        Entreprise entreprise1 = stockage.getEntreprise(id);

        // test de tout les paramètres
        Truth.assertThat(entreprise.getNom()).isEqualTo(entreprise1.getNom());
        Truth.assertThat(entreprise.getAdresse()).isEqualTo(entreprise1.getAdresse());
        Truth.assertThat(entreprise.getEmail()).isEqualTo(entreprise1.getEmail());
        Truth.assertThat(entreprise.getDateContact()).isEqualTo(entreprise1.getDateContact());
        Truth.assertThat(entreprise.getContact()).isEqualTo(entreprise1.getContact());
        Truth.assertThat(entreprise.getTelephone()).isEqualTo(entreprise1.getTelephone());
        Truth.assertThat(entreprise.getSiteWeb()).isEqualTo(entreprise1.getSiteWeb());

    }

    @Test
    public void deleteEntreprise() {
        Entreprise entreprise = new Entreprise(id,"", "", "", "", "", "", "", false);
        stockage.ajouterEntreprise(entreprise);
        UUID id = entreprise.getId();
        boolean isSupr = false;
        stockage.deleteEntreprise(entreprise);
        try {
            stockage.getEntreprise(id);
        } catch (CursorIndexOutOfBoundsException e) {// si l'entreprise n'est pas présente, getEntreprise() renvoi cette exception
            isSupr = true;
        }

        Truth.assertThat(isSupr).isEqualTo(true);

    }

    @Test
    public void ajouterEtudiant(){
        Etudiant etudiant = new Etudiant(id,"","","",true,1000);
        boolean ajout = true;
        try {
            stockage.ajouterEtudiant(etudiant);
        } catch (Exception e) {
            ajout = false;
        }
        Truth.assertThat(ajout).isTrue();
    }
    @Test
    public void getEtudiant(){
        Etudiant etudiant = new Etudiant(id,"","","",true,1000);
        stockage.ajouterEtudiant(etudiant);
        Etudiant etudiants = stockage.getEtudiant(id);
        Truth.assertThat(etudiants).isNotNull();
    }

    @Test
    public void getEtudiants(){
        Etudiant etudiant = new Etudiant(id,"","","",true,1000);
        stockage.ajouterEtudiant(etudiant);
        ArrayList<Etudiant> etudiants = stockage.getEtudiants();
        Truth.assertThat(etudiants).isNotNull();
    }

    @Test
    public void deleteEtudiant(){
        Etudiant etudiant = new Etudiant(id,"","","",true,1000);
        stockage.ajouterEtudiant(etudiant);
        UUID id = etudiant.getId();
        boolean isSupr = false;
        stockage.deleteEtudiant(etudiant);
        try {
            stockage.getEtudiant(id);
        } catch (CursorIndexOutOfBoundsException e) {// si l'entreprise n'est pas présente, getEntreprise() renvoi cette exception
            isSupr = true;
        }

        Truth.assertThat(isSupr).isEqualTo(true);

    }
}