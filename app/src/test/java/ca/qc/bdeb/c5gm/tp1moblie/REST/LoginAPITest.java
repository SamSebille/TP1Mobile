package ca.qc.bdeb.c5gm.tp1moblie.REST;

import org.checkerframework.checker.units.qual.A;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import com.google.common.truth.Truth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import ca.qc.bdeb.c5gm.tp1moblie.Activities.Entreprise;
import ca.qc.bdeb.c5gm.tp1moblie.Activities.Etudiant;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.mock.Calls;

public class LoginAPITest {
    @Mock
    private LoginAPI loginAPI;

    private CompteResult compteProf;
    private CompteResult compteEtudiant;
    private ComptePOJO etudiant;
    private Entreprise entreprise;
    private ResponseBody res;
    private List<Entreprise> entreprises = new ArrayList<>();
    private List<ComptePOJO> etudiants = new ArrayList<>();
    private String id;
    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        
        id = UUID.randomUUID().toString();

        Entreprise entreprise= new Entreprise("test","test","test","test","test","test","",false);
        entreprises.add(entreprise);

        etudiant = new ComptePOJO(id, "","","",false, ComptePOJO.TypeUtilisateur.ETUDIANT, entreprises);
        etudiants.add(etudiant);


        res = ResponseBody.create(MediaType.parse("text/plain"),"test");


        compteProf = new CompteResult(id, "John", "Doe", "johndoe@test.com", ComptePOJO.TypeUtilisateur.PROFESSEUR, ConnectUtils.authToken);
        compteEtudiant = new CompteResult(id, "Stewart", "Steve", "stewartsteve@test.com", ComptePOJO.TypeUtilisateur.ETUDIANT, ConnectUtils.authToken);



    }



    @Test
    public void connecter() {
        LoginData loginData = new LoginData("prof1@test.com", "secret");

        Mockito.when(loginAPI.connecter(loginData)).thenReturn(Calls.response(compteProf));

        Response<CompteResult> compteResultResponse = null;
        try {
            compteResultResponse = loginAPI.connecter(loginData).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
        String token = "";
        if (compteResultResponse.isSuccessful()) {
            token = compteResultResponse.body().getAccessToken();
        }

        // verify test
        Truth.assertThat(token).isEqualTo(ConnectUtils.authToken);



    }

    //@Test
    public void testerConnexion() {

        HashMap<String, Object> user = new HashMap<>();
        user.put("id_compte", ConnectUtils.authId);

        //Mockito.when(loginAPI.testerConnexion(ConnectUtils.authToken,user)).thenReturn(Calls.response(ConnectUtils.authToken,user);
        Response<CompteResult> compteResultResponse = null;

        String token = "";
        if (compteResultResponse.isSuccessful()) {
            token = compteResultResponse.body().getAccessToken();
        }

        // verify test
        Truth.assertThat(token).isEqualTo(ConnectUtils.authToken);

    }

    //@Test
    public void deconnecter() {
    }

    @Test
    public void getComptesEleves() {

        Mockito.when(loginAPI.getComptesEleves(ConnectUtils.authToken)).thenReturn(Calls.response(etudiants));
        Response<List<ComptePOJO>> compteResultResponse = null;

        try {
            compteResultResponse = loginAPI.getComptesEleves(ConnectUtils.authToken).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }

        List<ComptePOJO> liste_etudiant = new ArrayList<>();
        if (compteResultResponse.isSuccessful()) {
            liste_etudiant = compteResultResponse.body();
        }

        // verify test
        Truth.assertThat(liste_etudiant).isEqualTo(etudiants);


    }

    @Test
    public void getEtudiantConnecte() {
        Mockito.when(loginAPI.getEtudiantConnecte(ConnectUtils.authToken)).thenReturn(Calls.response(etudiant));
        Response<ComptePOJO> compteResultResponse = null;

        try {
            compteResultResponse = loginAPI.getEtudiantConnecte(ConnectUtils.authToken).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }

        ComptePOJO etudiant_connect = new ComptePOJO();
        if (compteResultResponse.isSuccessful()) {
            etudiant_connect = compteResultResponse.body();
        }

        // verify test
        Truth.assertThat(etudiant_connect).isEqualTo(etudiant);



    }

    //@Test
    public void supprStage() {
    }

    //@Test
    public void trouverStage() {
    }

    @Test
    public void creerEntreprise() {
        Mockito.when(loginAPI.creerEntreprise(ConnectUtils.authToken, entreprise)).thenReturn(Calls.response(entreprise));
        Response<Entreprise> compteResultResponse = null;

        try {
            compteResultResponse = loginAPI.creerEntreprise(ConnectUtils.authToken,entreprise).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Entreprise entreprise_cree = new Entreprise("","","","","","","",false);
        if (compteResultResponse.isSuccessful()) {
            entreprise_cree = compteResultResponse.body();
        }

        // verify test
        Truth.assertThat(entreprise_cree).isEqualTo(entreprise);


    }

    //@Test
    public void lireEntreprises() {
    }

    @Test
    public void modifierEntreprise() {
        Mockito.when(loginAPI.modifierEntreprise(ConnectUtils.authToken,id, entreprise)).thenReturn(Calls.response(entreprise));
        Response<Entreprise> compteResultResponse = null;

        try {
            compteResultResponse = loginAPI.modifierEntreprise(ConnectUtils.authToken,id,entreprise).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Entreprise entreprise_modif = new Entreprise("","","","","","","",false);
        if (compteResultResponse.isSuccessful()) {
            entreprise_modif = compteResultResponse.body();
        }

        // verify test
        Truth.assertThat(entreprise_modif).isEqualTo(entreprise);
    }

    @Test
    public void supprEntreprise() {
        Mockito.when(loginAPI.supprEntreprise(ConnectUtils.authToken, id)).thenReturn(Calls.response(entreprise));
        Response<Entreprise> compteResultResponse = null;

        try {
            compteResultResponse = loginAPI.supprEntreprise(ConnectUtils.authToken,id).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Entreprise entreprise_supr = new Entreprise("","","","","","","",false);
        if (compteResultResponse.isSuccessful()) {
            entreprise_supr = compteResultResponse.body();
        }

        // verify test
        Truth.assertThat(entreprise_supr).isEqualTo(entreprise);
    }

    //@Test
    public void inscription() {
    }
}
