package ca.qc.bdeb.c5gm.tp1moblie;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LoginAPI {

    @POST("/auth/connexion")
    @Headers({
            "Content-Type:application/json",
            "Authorization:Token"
    })
    Call<ResponseBody> connecter
            (@Body HashMap<String, Object> loginData);

    @POST("/auth/deconnexion")
    Call<ResponseBody> deconnecter
            (@Header("Authorization") String token);

    @POST("/auth/testerconnexion")
    Call<ResponseBody> testerConnexion
            (@Header("Authorization") String token, @Body HashMap<String, Object> userId);

    @GET("/compte/getcomptesetudiantsactifs")
    Call<List<ComptePOJO>> getComptesEleves
            (@Header("Authorization") String token);

    @DELETE("/stage/{idStage}")
    Call<ResponseBody> supprStage
            (@Header("Authorization") String token, @Path("idStage") String idStage);

}
