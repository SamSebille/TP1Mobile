package ca.qc.bdeb.c5gm.tp1moblie;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConnectUtils {
    public static String authEmail = "prof1@test.com";
    public static String authPassword = "secret";
    public static String authToken = "";
    public static String authId = "";


    public static LoginAPI client;
    public static HashMap<String, Object> user;

    public static boolean testerConnexion(){
        final boolean[] connexionReussie = new boolean[1];
        client.testerConnexion(authToken, user).enqueue(
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() != 200) {
                            connexionReussie[0] = true;
                        }
                        connexionReussie[0] =  false;
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        connexionReussie[0] =  false;
                    }
                }
        );
        return connexionReussie[0];
    }

    public static void connecter() {

        client.connecter(user).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        authToken = json.getString("access_token");
                        authId = json.getString("id");
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void getData(){
        client.getComptesEleves(ConnectUtils.authToken).enqueue(
                new Callback<List<ComptePOJO>>() {
                    @Override
                    public void onResponse(Call<List<ComptePOJO>> call, Response<List<ComptePOJO>> response) {
                        if (response.code() == 200) {
                            List<ComptePOJO> eleves = response.body();
                            String display = "Réponse OK\n";
                            for (int i = 0; i < eleves.size(); i++) {
                                display += eleves.get(i).toString() + "\n";

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ComptePOJO>> call, Throwable t) {
                    }
                });
    }

    public static boolean signIn(String[] body){
        final boolean[] connexionReussie = new boolean[1];

        client.inscription(ConnectUtils.authToken, body).enqueue(
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() != 200) {
                            connexionReussie[0] = true;
                        }
                        connexionReussie[0] =  false;
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        connexionReussie[0] =  false;
                    }
                }
        );
        return connexionReussie[0];
    }
}
