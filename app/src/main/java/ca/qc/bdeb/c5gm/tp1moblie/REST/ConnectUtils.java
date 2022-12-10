package ca.qc.bdeb.c5gm.tp1moblie.REST;

import static android.content.Context.MODE_PRIVATE;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import ca.qc.bdeb.c5gm.tp1moblie.Activities.ConnexionActivity;
import ca.qc.bdeb.c5gm.tp1moblie.Activities.Entreprise;
import ca.qc.bdeb.c5gm.tp1moblie.Activities.Etudiant;
import ca.qc.bdeb.c5gm.tp1moblie.Activities.InscriptionActivity;
import ca.qc.bdeb.c5gm.tp1moblie.Activities.MainActivity;
import ca.qc.bdeb.c5gm.tp1moblie.Activities.MenuActivity;
import ca.qc.bdeb.c5gm.tp1moblie.Activities.MenuProfActivity;
import ca.qc.bdeb.c5gm.tp1moblie.BD.Stockage;
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

    public static void chargerBDEtudiants(Activity activity) {
        client.getComptesEleves(ConnectUtils.authToken).enqueue(
                new Callback<List<ComptePOJO>>() {
                    @Override
                    public void onResponse(Call<List<ComptePOJO>> call, Response<List<ComptePOJO>> response) {
                        if (response.isSuccessful()) {
                            List<ComptePOJO> etudiants = response.body();
                            Stockage stockage = Stockage.getInstance(activity.getApplicationContext());

                            stockage.clearTables();

                            for (ComptePOJO comptePOJO : etudiants) {
                                Etudiant etudiant = new Etudiant(comptePOJO);
                                stockage.ajouterEtudiant(etudiant);
                            }

                            activity.startActivity(new Intent(activity, MenuProfActivity.class));
                        } else {
                            Toast.makeText(activity,
                                    "Une erreur inconnue est survenue, réessayez plus tard."
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<List<ComptePOJO>> call, Throwable t) {
                        activity.startActivity(new Intent(activity, MainActivity.class));
                    }
                }
        );

    }

    public static void chargerBDEntreprises(Activity activity) {

        client.lireEntreprises(ConnectUtils.authToken).enqueue(
                new Callback<List<Entreprise>>() {
                    @Override
                    public void onResponse(Call<List<Entreprise>> call, Response<List<Entreprise>> response) {
                        if (response.isSuccessful()) {
                            List<Entreprise> entreprises = response.body();
                            Stockage stockage = Stockage.getInstance(activity.getApplicationContext());

                            stockage.clearTables();

                            for (Entreprise entreprise : entreprises) {
                                stockage.ajouterEntreprise(entreprise);
                            }

                            activity.startActivity(new Intent(activity, MenuActivity.class));
                        } else {
                            Toast.makeText(activity,
                                    "Une erreur inconnue est survenue, réessayez plus tard."
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Entreprise>> call, Throwable t) {
                        activity.startActivity(new Intent(activity, MainActivity.class));
                    }
                }
        );
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

    public static void deconnexion(Activity activity) {
        HashMap<String, Object> user = new HashMap<>();
        user.put("id_compte", ConnectUtils.authId);
        client.deconnecter(ConnectUtils.authToken).enqueue(
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        // Clear des preferences
                        SharedPreferences prefs = activity.getSharedPreferences("prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("token_compte", "");
                        editor.putString("id_compte", "");
                        editor.apply();
                        activity.startActivity(new Intent(activity, MainActivity.class));
                        Toast.makeText(activity,
                                "Deconnexion..."
                                , Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(activity,
                                "Deconnexion impossible en mode hors ligne."
                                , Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
