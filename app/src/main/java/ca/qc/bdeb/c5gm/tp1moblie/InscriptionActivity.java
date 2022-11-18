package ca.qc.bdeb.c5gm.tp1moblie;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class InscriptionActivity extends AppCompatActivity {

    private final EditText[] saisies = new EditText[4];

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
    }

    public void onClickValider(View view){
        String[] body = new String[saisies.length];
        for (int i = 0; i < saisies.length; i++) {
            if (saisies[i].getText().toString().trim().length() == 0){
                Toast.makeText(this,
                        "Veuillez remplir tout les champs.", Toast.LENGTH_LONG).show();
                return;
            }
            body[i] = saisies[i].getText().toString();
        }

        if (!ConnectUtils.signIn(body)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Erreur");
            builder.setMessage("L'inscription a échouée. Vérifiez votre connexion.");

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