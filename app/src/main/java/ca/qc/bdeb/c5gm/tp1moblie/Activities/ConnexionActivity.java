package ca.qc.bdeb.c5gm.tp1moblie.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import ca.qc.bdeb.c5gm.tp1moblie.R;
import ca.qc.bdeb.c5gm.tp1moblie.REST.ComptePOJO;
import ca.qc.bdeb.c5gm.tp1moblie.REST.ConnectUtils;
import ca.qc.bdeb.c5gm.tp1moblie.REST.LoginData;

public class ConnexionActivity extends AppCompatActivity {

    private EditText et_username;
    private EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        et_username = findViewById(R.id.login_username);
        et_password = findViewById(R.id.login_password);
    }

    public void onClickConnexion(View view) {
        if (et_username.getText().toString().trim().length() == 0 ||
                et_password.getText().toString().trim().length() == 0){
            Toast.makeText(this,
                    "Veuillez remplir tout les champs.", Toast.LENGTH_LONG).show();
            return;
        }

        ConnectUtils.connecter(this, new LoginData(et_username.getText().toString(),
                et_password.getText().toString()));
    }

    public void connexionReussie(int is_reussie){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        switch (is_reussie){
            case 1 :{
                SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("token_compte", ConnectUtils.authToken);
                editor.putString("id_compte", ConnectUtils.authId);
                editor.putString("type_utilisateur", ConnectUtils.typeCompte.toString());
                editor.apply();
                if (ConnectUtils.typeCompte == ComptePOJO.TypeUtilisateur.ETUDIANT)
                    startActivity(new Intent(ConnexionActivity.this, MenuActivity.class));
                else
                    startActivity(new Intent(ConnexionActivity.this, MenuProfActivity.class));
                break;
            }
            case 0 :{
                builder.setTitle("Erreur");
                builder.setMessage("Les identifiants sont invalides.");
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                et_password.setText("");
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            }
            case -1 :{
                builder.setTitle("Erreur");
                builder.setMessage("La connexion a échouée. Vérifiez votre connexion internet.");

                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            }
        }
    }

    public void onClickInscription(View view) {
        startActivity(new Intent(ConnexionActivity.this, InscriptionActivity.class));
    }
}