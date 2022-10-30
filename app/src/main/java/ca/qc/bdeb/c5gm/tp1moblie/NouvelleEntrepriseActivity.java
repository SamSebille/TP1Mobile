package ca.qc.bdeb.c5gm.tp1moblie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;

public class NouvelleEntrepriseActivity extends AppCompatActivity {

    private Stockage stockage;

    private EditText[] saisies = new EditText[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouvelle_entreprise);

        stockage = Stockage.getInstance(getApplicationContext());

        saisies[0] = findViewById(R.id.sai_nom_entreprise);
        saisies[1] = findViewById(R.id.sai_contact);
        saisies[2] = findViewById(R.id.sai_courriel);
        saisies[3] = findViewById(R.id.sai_telephone);
        saisies[4] = findViewById(R.id.sai_web);
        saisies[5] = findViewById(R.id.sai_adresse);
        saisies[6] = findViewById(R.id.sai_date);
    }

    public void onClickValider(View view) {
        boolean isChampVide = false;

        for (EditText champ : saisies){
            if (champ.getText().toString().trim().length() == 0){
                isChampVide = true;
                break;
            }
        }

        if (!isChampVide){
            Entreprise entreprise = new Entreprise(
                    saisies[0].getText().toString(), saisies[1].getText().toString(),
                    saisies[2].getText().toString(), saisies[3].getText().toString(),
                    saisies[4].getText().toString(), saisies[5].getText().toString(),
                    saisies[6].getText().toString());

            stockage.ajouterEntreprise(entreprise);

            Toast.makeText(this,
                    "Entreprise enregistrée.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this,
                    "Veuillez remplir tout les champs.", Toast.LENGTH_LONG).show();
        }

    }

    public void onClickCourriel(View view){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, saisies[1].getText().toString());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }
    public void onClickTelephone(View view){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + saisies[2].getText().toString()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
    public void onClickWeb(View view){
        Uri webpage;

        if (saisies[3].getText().toString().startsWith("http://")
                || saisies[3].getText().toString().startsWith("https://"))
            webpage = Uri.parse(saisies[3].getText().toString());
        else
            webpage = Uri.parse("http://" + saisies[3].getText().toString());

        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this,
                    "Le format web est incorrect.", Toast.LENGTH_LONG).show();
        }
    }
}