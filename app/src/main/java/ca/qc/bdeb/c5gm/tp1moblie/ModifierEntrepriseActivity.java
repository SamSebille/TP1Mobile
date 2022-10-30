package ca.qc.bdeb.c5gm.tp1moblie;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ModifierEntrepriseActivity extends AppCompatActivity {

    private int entreprise_id;
    private Stockage stockage;

    private Entreprise entreprise;

    private EditText[] saisies = new EditText[6];
    private TextView nomEntreprise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_entreprise);

        stockage = Stockage.getInstance(getApplicationContext());

        nomEntreprise = findViewById(R.id.menu_nom_entreprise);
        saisies[0] = findViewById(R.id.sai_contact);
        saisies[1] = findViewById(R.id.sai_courriel);
        saisies[2] = findViewById(R.id.sai_telephone);
        saisies[3] = findViewById(R.id.sai_web);
        saisies[4] = findViewById(R.id.sai_adresse);
        saisies[5] = findViewById(R.id.sai_date);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Bundle extras = getIntent().getExtras();

        System.out.println(extras.getInt("ENTREPRISE_ID"));
        entreprise_id = extras.getInt("ENTREPRISE_ID");

        entreprise = stockage.getEntreprise(entreprise_id);

        nomEntreprise.setText(entreprise.getNom());
        saisies[0].setText(entreprise.getContact());
        saisies[1].setText(entreprise.getCourriel());
        saisies[2].setText(entreprise.getTelephone());
        saisies[3].setText(entreprise.getWeb());
        saisies[4].setText(entreprise.getAdresse());
        saisies[5].setText(entreprise.getDate());
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
                    entreprise_id, nomEntreprise.getText().toString(), saisies[0].getText().toString(),
                    saisies[1].getText().toString(), saisies[2].getText().toString(),
                    saisies[3].getText().toString(), saisies[4].getText().toString(),
                    saisies[5].getText().toString());

            stockage.updateEntreprise(entreprise);

            Toast.makeText(this,
                    "Modifications enregistrée.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this,
                    "Veuillez remplir tout les champs.", Toast.LENGTH_LONG).show();
        }
    }

    public void onClickSupprimer(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Attention");
        builder.setMessage("Supprimer définitivement cette entreprise?");

        builder.setPositiveButton("Confirmer",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stockage.deleteEntreprise(entreprise);
                        finish();
                    }
                });

        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // On ne fait rien
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}