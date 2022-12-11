package ca.qc.bdeb.c5gm.tp1moblie.REST;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

/**
 * Classe contenant les données et methodes statique ayant rapport avec la connexion a l'API
 */
public class ConnectUtils {

    public static String authId = "";
    public static String authToken = "";

    public static ComptePOJO.TypeUtilisateur typeCompte;

    private static final LoginAPI client = LoginAPIClient.getRetrofit().create(LoginAPI.class);

    /**
     * Methode pour la premiere connexion d'un utilisateur
     *
     * @param connexionActivity L'activité de connexion
     * @param loginData         Les identifiants de connexion entrées par l'utiilisateur
     */
    public static void connecter(ConnexionActivity connexionActivity, LoginData loginData) {

        client.connecter(loginData).enqueue(new Callback<CompteResult>() {
            @Override
            public void onResponse(Call<CompteResult> call, Response<CompteResult> response) {
                if (response.isSuccessful()) {
                    CompteResult json = response.body();
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

    /**
     * Methode pour charger la BD des etudiants via l'API
     */
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

    /**
     * Methode pour charger la BD des entreprises via l'API
     */
    public static void chargerBDEntreprises(Activity activity) {

        client.getEtudiantConnecte(ConnectUtils.authToken).enqueue(
                new Callback<ComptePOJO>() {
                    @Override
                    public void onResponse(Call<ComptePOJO> call, Response<ComptePOJO> response) {
                        if (response.isSuccessful()) {
                            ComptePOJO etudiant = response.body();
                            List<Entreprise> entreprises = etudiant.getEntreprises();
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
                    public void onFailure(Call<ComptePOJO> call, Throwable t) {
                        activity.startActivity(new Intent(activity, MainActivity.class));
                    }
                }
        );
    }

    /**
     * Methode pour l'inscription d'un nouveau compte dans l'API
     */
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

    /**
     * Methode pour tester la connexion avec le token enregistré de la derniere connexion
     */
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

    /**
     * Methode pour la deconnexion du compte actif
     */
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

    /**
     * Methode pour supprimer une entreprise de l'API
     */
    public static void supprimerEntreprise(Activity activity, Entreprise entreprise) {
        client.supprEntreprise(ConnectUtils.authToken, entreprise.getId().toString()).enqueue(
                new Callback<Entreprise>() {
                    @Override
                    public void onResponse(Call<Entreprise> call, Response<Entreprise> response) {
                        if (response.isSuccessful()) {
                            Stockage stockage = Stockage.getInstance(activity.getApplicationContext());
                            stockage.deleteEntreprise(entreprise);
                            activity.finish();
                        } else {
                            Toast.makeText(activity,
                                    "L'entreprise n'existe pas où a deja été supprimée."
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Entreprise> call, Throwable t) {
                        Toast.makeText(activity,
                                "Supression impossible en mode hors ligne."
                                , Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    /**
     * Methode pour ajouter une entreprise a l'API
     */
    public static void ajouterEntreprise(Activity activity, Entreprise entreprise) {
        client.creerEntreprise(ConnectUtils.authToken, entreprise).enqueue(
                new Callback<Entreprise>() {
                    @Override
                    public void onResponse(Call<Entreprise> call, Response<Entreprise> response) {
                        if (response.isSuccessful()) {
                            Stockage stockage = Stockage.getInstance(activity.getApplicationContext());
                            stockage.ajouterEntreprise(entreprise);
                            Toast.makeText(activity,
                                    "Entreprise enregistrée.", Toast.LENGTH_LONG).show();
                            activity.finish();
                        } else {
                            Toast.makeText(activity,
                                    "L'entreprise existe déjà."
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Entreprise> call, Throwable t) {
                        Toast.makeText(activity,
                                "Ajout impossible en mode hors ligne."
                                , Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    /**
     * Methode pour modifier une entreprise de l'API
     */
    public static void modifierEntreprise(Activity activity, Entreprise entreprise, boolean finish) {
        client.modifierEntreprise(ConnectUtils.authToken, entreprise.getId().toString(), entreprise).enqueue(
                new Callback<Entreprise>() {
                    @Override
                    public void onResponse(Call<Entreprise> call, Response<Entreprise> response) {
                        if (response.isSuccessful()) {
                            Stockage stockage = Stockage.getInstance(activity.getApplicationContext());
                            stockage.updateEntreprise(entreprise);
                            Toast.makeText(activity,
                                    "Modifications enregistrée.", Toast.LENGTH_SHORT).show();
                            if (finish)
                                activity.finish();
                        } else {
                            Toast.makeText(activity,
                                    "L'entreprise n'as pas été trouvée."
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Entreprise> call, Throwable t) {
                        Toast.makeText(activity,
                                "Modification impossible en mode hors ligne."
                                , Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

}
