package ca.qc.bdeb.c5gm.tp1moblie.REST;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private static final LoginAPI client = LoginAPIClient.getRetrofit().create(LoginAPI.class);
    private static LoginData user = new LoginData(authEmail, authPassword);

    private List<ComptePOJO> comptes;

    public static void connecter() {

        LoginData loginData = new LoginData("prof1@test.com", "secret");

        client.connecter(loginData).enqueue(new Callback<CompteResult>() {
            @Override
            public void onResponse(Call<CompteResult> call, Response<CompteResult> response) {
                if (response.isSuccessful()) {
                    CompteResult json = response.body();
                    ConnectUtils.authToken = json.getAccessToken();
                    ConnectUtils.authId = json.getId();
                }
            }

            @Override
            public void onFailure(Call<CompteResult> call, Throwable t) {

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

    public static boolean signIn(Map<String, String> body) {
        final boolean[] connexionReussie = new boolean[1];

        //client.inscription(ConnectUtils.authToken, body).execute(
                new Callback<Response>() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        connexionReussie[0] = true;
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        connexionReussie[0] = false;
                    }
                };
        //);
        return connexionReussie[0];
    }

    public static void testerConnexion(MainActivity mainActivity) {
        HashMap<String, Object> user = new HashMap<>();
        user.put("id_compte", ConnectUtils.authId);

        client.testerConnexion(ConnectUtils.authToken, user).enqueue(
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (!response.isSuccessful()) {
                            connecter();
                            mainActivity.startActivity(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        mainActivity.startActivity(false);
                    }
                }
        );
    }
}
