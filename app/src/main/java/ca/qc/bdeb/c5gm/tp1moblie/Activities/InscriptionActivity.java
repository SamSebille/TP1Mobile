package ca.qc.bdeb.c5gm.tp1moblie.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.HashMap;
import java.util.regex.Pattern;

import ca.qc.bdeb.c5gm.tp1moblie.R;
import ca.qc.bdeb.c5gm.tp1moblie.REST.ConnectUtils;

public class InscriptionActivity extends AppCompatActivity {

    private final EditText[] saisies = new EditText[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        saisies[0] = findViewById(R.id.inscr_prenom);
        saisies[1] = findViewById(R.id.inscr_nom);
        saisies[2] = findViewById(R.id.inscr_email);
        saisies[3] = findViewById(R.id.inscr_password);
        saisies[4] = findViewById(R.id.inscr_password_confirm);
    }

    public void onClickValider(View view){
        String[] reponse = new String[saisies.length];
        for (int i = 0; i < saisies.length; i++) {
            if (saisies[i].getText().toString().trim().length() == 0){
                Toast.makeText(this,
                        "Veuillez remplir tout les champs.", Toast.LENGTH_LONG).show();
                return;
            }
            reponse[i] = saisies[i].getText().toString();
        }

        // On verifie que le courriel est au bon format
        Pattern emailRegex = Pattern.compile(".+@.+\\..+");
        if (!emailRegex.matcher(saisies[2].getText().toString()).matches()) {
            Toast.makeText(this,
                    "Veuillez insérer un bon format pour l'adresse couriel (exemple@test.com)",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // On verifie que le mot de passe correspond a la confirmation
        if (!saisies[3].getText().toString().matches(saisies[4].getText().toString())){
            Toast.makeText(this,
                    "Les mots de passe ne correspondent pas.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        HashMap<String, String> body = new HashMap<>();
        body.put("nom", saisies[0].getText().toString());
        body.put("prenom", saisies[1].getText().toString());
        body.put("email", saisies[2].getText().toString());
        body.put("mot_de_passe", saisies[3].getText().toString());
        body.put("mot_de_passe_confirmation", saisies[4].getText().toString());

        ConnectUtils.Inscription(this, body);
    }

    public void connexionReussie(boolean is_reussie) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        if (is_reussie){
            builder.setTitle("Bravo");
            builder.setMessage("L'inscription a réussie.");

            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();

        } else {
            builder.setTitle("Erreur");
            builder.setMessage("L'inscription a échouée. Vérifiez votre connexion internet.");

            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // RIENG
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

}