package ca.qc.bdeb.c5gm.tp1moblie.Activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import ca.qc.bdeb.c5gm.tp1moblie.R;
import ca.qc.bdeb.c5gm.tp1moblie.REST.ComptePOJO;
import ca.qc.bdeb.c5gm.tp1moblie.REST.ConnectUtils;

/**
 * Futur écran de connexion
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    public void startActivity(int is_reussie){
        switch (is_reussie) {
            case 1: {
                if (ConnectUtils.typeCompte == ComptePOJO.TypeUtilisateur.ETUDIANT)
                    startActivity(new Intent(MainActivity.this, MenuActivity.class));
                else
                    startActivity(new Intent(MainActivity.this, MenuActivity.class));
                break;
            }
            case 0: {
                startActivity(new Intent(MainActivity.this, ConnexionActivity.class));
                break;
            }
            case -1: {
                if (!ConnectUtils.authId.matches("")){
                    if (ConnectUtils.typeCompte == ComptePOJO.TypeUtilisateur.ETUDIANT)
                        startActivity(new Intent(MainActivity.this, MenuActivity.class));
                    else
                        startActivity(new Intent(MainActivity.this, MenuProfActivity.class));
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setCancelable(true);
                    builder.setTitle("Erreur");
                    builder.setMessage("La connexion a échouée. Vérifiez votre connexion internet avant de reesayer.");

                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs= getSharedPreferences("prefs", MODE_PRIVATE);
        ConnectUtils.authId = prefs.getString("id_compte", "");
        ConnectUtils.authToken = prefs.getString("token_compte", "");
        ConnectUtils.typeCompte = ComptePOJO.TypeUtilisateur.valueOf(prefs.getString("type_utilisateur", "ETUDIANT"));
        ConnectUtils.testerConnexion(this);
    }


}