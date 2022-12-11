package ca.qc.bdeb.c5gm.tp1moblie.REST;

import java.util.HashMap;
import java.util.List;

import ca.qc.bdeb.c5gm.tp1moblie.Activities.Entreprise;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LoginAPI {

    @POST("/auth/connexion")
    Call<CompteResult> connecter(@Body LoginData loginData);

    @POST("/auth/deconnexion")
    Call<ResponseBody> deconnecter(@Header("Authorization") String token);

    @POST("/auth/testerconnexion")
    Call<ResponseBody> testerConnexion(@Header("Authorization") String token, @Body HashMap<String, Object> userId);

    @GET("/compte/getcomptesetudiantsactifs")
    Call<List<ComptePOJO>> getComptesEleves(@Header("Authorization") String token);

    @GET("/compte/getetudiant")
    Call<ComptePOJO> getEtudiantConnecte(@Header("Authorization") String token);

    @DELETE("/stage/{idStage}")
    Call<ResponseBody> supprStage(@Header("Authorization") String token, @Path("idStage") String idStage);

    @PATCH("/compte/stagetrouve")
    public Call<ComptePOJO> trouverStage(@Header("Authorization") String token);

    @POST("/entreprise")
    Call<Entreprise> creerEntreprise(@Header("Authorization") String token, @Body Entreprise entreprise);

    @GET("/entreprise")
    Call<List<Entreprise>> lireEntreprises(@Header("Authorization") String token);

    @PATCH("/entreprise/{idEntreprise}")
    Call<Entreprise> modifierEntreprise(@Header("Authorization") String token, @Path("idEntreprise") String idEntreprise, @Body Entreprise entreprise);

    @DELETE("/entreprise/{idEntreprise}")
    Call<Entreprise> supprEntreprise(@Header("Authorization") String token, @Path("idEntreprise") String idEntreprise);

    @POST("/inscription")
    Call<ResponseBody> inscription(@Body HashMap<String, String> coord);
}
