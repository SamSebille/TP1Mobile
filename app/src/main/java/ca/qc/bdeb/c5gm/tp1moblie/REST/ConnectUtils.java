package ca.qc.bdeb.c5gm.tp1moblie.REST;

import android.content.Intent;
import android.util.Log;

import java.util.HashMap;
import java.util.List;

import ca.qc.bdeb.c5gm.tp1moblie.Activities.ConnexionActivity;
import ca.qc.bdeb.c5gm.tp1moblie.Activities.InscriptionActivity;
import ca.qc.bdeb.c5gm.tp1moblie.Activities.MainActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConnectUtils {
    public static String authEmail = "prof1@test.com";
    public static String authPassword = "secret";
    public static String authId = "";
    public static String authToken = "";
    public static ComptePOJO.TypeUtilisateur typeCompte;

    private static final LoginAPI client = LoginAPIClient.getRetrofit().create(LoginAPI.class);
    private static LoginData user = new LoginData(authEmail, authPassword);

    private List<ComptePOJO> comptes;

    public static void connecter(ConnexionActivity connexionActivity, LoginData loginData) {

        client.connecter(loginData).enqueue(new Callback<CompteResult>() {
            @Override
            public void onResponse(Call<CompteResult> call, Response<CompteResult> response) {
                if (response.isSuccessful()) {
                    CompteResult json = response.body();
                    System.out.println( json.getAccessToken());
                    Log.d("TAG", json.getId());
                    Log.d("TAG", json.getTypeCompte().toString());
                    ConnectUtils.authToken = json.getAccessToken();
                    ConnectUtils.authId = json.getId();
                    ConnectUtils.typeCompte = json.getTypeCompte();
                    connexionActivity.connexionReussie(1);
                } else {
                    connexionActivity.connexionReussie(0);
                }
            }

            @Override
            public void onFailure(Call<CompteResult> call, Throwable t) {
                connexionActivity.connexionReussie(-1);
            }
        });

    }

    private List<ComptePOJO> getEtudiants() {

        client.getComptesEleves(ConnectUtils.authToken).enqueue(
                new Callback<List<ComptePOJO>>() {
                    @Override
                    public void onResponse(Call<List<ComptePOJO>> call, Response<List<ComptePOJO>> response) {
                        if (response.isSuccessful()) {
                            comptes = response.body();
                        }
                    }
                    @Override
                    public void onFailure(Call<List<ComptePOJO>> call, Throwable t) {
                    }
                }
        );

        return comptes;
    }

    public static void Inscription(InscriptionActivity inscriptionActivity, HashMap<String, String> body) {
        client.inscription(body).enqueue(
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (!response.isSuccessful()) {
                            inscriptionActivity.connexionReussie(true);
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        inscriptionActivity.connexionReussie(false);
                    }
                });
    }

    public static void testerConnexion(MainActivity mainActivity) {
        HashMap<String, Object> user = new HashMap<>();
        user.put("id_compte", ConnectUtils.authId);
        client.testerConnexion(ConnectUtils.authToken, user).enqueue(
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            mainActivity.startActivity(1);
                        } else {
                            mainActivity.startActivity(0);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        mainActivity.startActivity(-1);
                    }
                }
        );
    }

    public static void deconnexion(ConnexionActivity connexionActivity) {
        HashMap<String, Object> user = new HashMap<>();
        user.put("id_compte", ConnectUtils.authId);
        client.deconnecter(ConnectUtils.authToken).enqueue(
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        ConnectUtils.authId = "";
                        ConnectUtils.authToken = "";
                        connexionActivity.startActivity(new Intent(connexionActivity, MainActivity.class));
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                    }
                }
        );
    }
}
