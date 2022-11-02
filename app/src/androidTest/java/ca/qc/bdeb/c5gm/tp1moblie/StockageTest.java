package ca.qc.bdeb.c5gm.tp1moblie;

import android.content.Context;
import android.database.CursorIndexOutOfBoundsException;

import androidx.test.core.app.ApplicationProvider;

import com.google.common.truth.Truth;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;


public class StockageTest {
    private Stockage stockage;
    private Context context = ApplicationProvider.getApplicationContext();


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
        Entreprise entreprise = new Entreprise("", "", "", "", "", "", "");
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
        Entreprise entreprise1 = new Entreprise("", "", "", "", "", "", "");
        stockage.ajouterEntreprise(entreprise1);
        ArrayList<Entreprise> entreprises = stockage.getEntreprises();
        Truth.assertThat(entreprises).isNotNull();
    }

    @Test
    public void getEntreprise() {
        Entreprise entreprise1 = new Entreprise("", "", "", "", "", "", "");
        stockage.ajouterEntreprise(entreprise1);
        Entreprise entreprises = stockage.getEntreprise(1);
        Truth.assertThat(entreprises).isNotNull();
    }

    @Test
    public void updateEntreprise() {
        Entreprise entreprise = new Entreprise("", "", "", "", "", "", "");
        stockage.ajouterEntreprise(entreprise);
        int id = entreprise.getId();
        entreprise = new Entreprise(id, "test", "test", "test", "test", "test", "yul", "?");
        stockage.updateEntreprise(entreprise);

        Entreprise entreprise1 = stockage.getEntreprise(id);

        // test de tout les paramètres
        Truth.assertThat(entreprise.getNom()).isEqualTo(entreprise1.getNom());
        Truth.assertThat(entreprise.getAdresse()).isEqualTo(entreprise1.getAdresse());
        Truth.assertThat(entreprise.getCourriel()).isEqualTo(entreprise1.getCourriel());
        Truth.assertThat(entreprise.getDate()).isEqualTo(entreprise1.getDate());
        Truth.assertThat(entreprise.getContact()).isEqualTo(entreprise1.getContact());
        Truth.assertThat(entreprise.getTelephone()).isEqualTo(entreprise1.getTelephone());
        Truth.assertThat(entreprise.getWeb()).isEqualTo(entreprise1.getWeb());

    }

    @Test
    public void deleteEntreprise() {
        Entreprise entreprise = new Entreprise("", "", "", "", "", "", "");
        stockage.ajouterEntreprise(entreprise);
        int id = entreprise.getId();
        boolean isSupr = false;
        stockage.deleteEntreprise(entreprise);
        try {
            stockage.getEntreprise(id);
        } catch (CursorIndexOutOfBoundsException e) {// si l'entreprise n'est pas présente, getEntreprise() renvoi cette exception
            isSupr = true;
        }

        Truth.assertThat(isSupr).isEqualTo(true);

    }
}