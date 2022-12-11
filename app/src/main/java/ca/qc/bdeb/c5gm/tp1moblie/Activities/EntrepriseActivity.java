package ca.qc.bdeb.c5gm.tp1moblie.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.UUID;
import java.util.regex.Pattern;

import ca.qc.bdeb.c5gm.tp1moblie.BD.Stockage;
import ca.qc.bdeb.c5gm.tp1moblie.R;
import ca.qc.bdeb.c5gm.tp1moblie.REST.ConnectUtils;

/**
 * Classe pour les activités de création et de modification d'entreprise.
 */
public class EntrepriseActivity extends AppCompatActivity {

    // L'entreprise a modifier
    private UUID entreprise_id;
    private Entreprise entreprise;

    private Stockage stockage;

    private TextView nomEntreprise;
    private static final EditText[] saisies = new EditText[6];
    private static TextView date;

    // Si l'activité est une activité de modification
    private static boolean isModifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entreprise);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        stockage = Stockage.getInstance(getApplicationContext());

        nomEntreprise = findViewById(R.id.menu_nom_etudiant);
        saisies[0] = findViewById(R.id.sai_nom_entreprise);
        saisies[1] = findViewById(R.id.sai_contact);
        saisies[2] = findViewById(R.id.sai_courriel);
        saisies[3] = findViewById(R.id.sai_telephone);
        saisies[4] = findViewById(R.id.sai_web);
        saisies[5] = findViewById(R.id.sai_adresse);
        date = findViewById(R.id.sai_date);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        // On recupere l'entreprise cliquée
        Bundle extras = getIntent().getExtras();
        isModifier = extras.getBoolean("ISMODIFIER");

        // L'affichage est adapté en foncton du type d'activité (creation ou modification)
        if (isModifier) {
            getSupportActionBar().setTitle(R.string.title_activity_modifier_entreprise);

            entreprise_id = UUID.fromString(extras.getString("ENTREPRISE_ID"));

            entreprise = stockage.getEntreprise(entreprise_id);

            nomEntreprise.setText(entreprise.getNom());
            saisies[1].setText(entreprise.getContact());
            saisies[2].setText(entreprise.getCourriel());
            saisies[3].setText(entreprise.getTelephone());
            saisies[4].setText(entreprise.getWeb());
            saisies[5].setText(entreprise.getAdresse());
            date.setText(entreprise.getDate());

            saisies[0].setVisibility(View.GONE);

        } else {
            getSupportActionBar().setTitle(R.string.title_activity_ajouter_entreprise);

            findViewById(R.id.btn_supprimer).setVisibility(View.GONE);
        }
    }

    // Intent pour l'envoi de courriel
    public void onClickCourriel(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, saisies[2].getText().toString());
        startActivity(intent);
    }

    // Intent pour les appels telephoniques
    public void onClickTelephone(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + saisies[3].getText().toString()));
        startActivity(intent);
    }

    // Intent pour l'ouverture d'une page web
    public void onClickWeb(View view) {
        Uri webpage;

        // Si l'utilisateur n'as pas ajouté le http://, on le rajoute
        if (saisies[4].getText().toString().startsWith("http://")
                || saisies[4].getText().toString().startsWith("https://"))
            webpage = Uri.parse(saisies[4].getText().toString());
        else
            webpage = Uri.parse("http://" + saisies[4].getText().toString());

        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(intent);
    }

    /**
     * Methode pour valider les changements ou la creation d'une entreprise
     *
     * @param view
     */
    public void onClickValider(View view) {

        // On vérifie si un champ est vide
        for (EditText champ : saisies) {
            if (champ.getText().toString().trim().length() == 0 && !(isModifier && champ == saisies[0])) {
                Toast.makeText(this,
                        "Veuillez remplir tout les champs.", Toast.LENGTH_LONG).show();
                return;
            }
        }

        // On verifie que le courriel est au bon format
        Pattern emailRegex = Pattern.compile(".+@.+\\..+");
        if (!emailRegex.matcher(saisies[2].getText().toString()).matches()) {
            Toast.makeText(this,
                    "Veuillez insérer un bon format pour l'adresse couriel (exemple@test.com)",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // On verifie que la date correspond au format DD/MM/YYYY
        Pattern dateRegex = Pattern.compile("\\d{2}/\\d{2}/\\d{4}");
        if (!dateRegex.matcher(date.getText().toString()).matches()) {
            Toast.makeText(this,
                    "Veuillez insérer un bon format de date (DD/MM/YYYY)", Toast.LENGTH_LONG).show();
            return;
        }


        Entreprise entreprise;
        if (isModifier) {
            entreprise = new Entreprise(
                    entreprise_id, nomEntreprise.getText().toString(), saisies[1].getText().toString(),
                    saisies[2].getText().toString(), saisies[3].getText().toString(),
                    saisies[4].getText().toString(), saisies[5].getText().toString(),
                    date.getText().toString());

            ConnectUtils.modifierEntreprise(this, entreprise);

            Toast.makeText(this,
                    "Modifications enregistrée.", Toast.LENGTH_LONG).show();

        } else {
            entreprise = new Entreprise(
                    saisies[0].getText().toString(), saisies[1].getText().toString(),
                    saisies[2].getText().toString(), saisies[3].getText().toString(),
                    saisies[4].getText().toString(), saisies[5].getText().toString(),
                    date.getText().toString());

            ConnectUtils.ajouterEntreprise(this, entreprise);

            Toast.makeText(this,
                    "Entreprise enregistrée.", Toast.LENGTH_LONG).show();
        }
        finish();
    }

    /**
     * Methode pour la suppression de l'entreprise en cours de modification
     *
     * @param view
     */
    public void onClickSupprimer(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Attention");
        builder.setMessage("Supprimer définitivement cette entreprise?");

        builder.setPositiveButton("Confirmer",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        validerSupprimer(entreprise);
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

    public void validerSupprimer(Entreprise entreprise){
        ConnectUtils.supprimerEntreprise(this, entreprise);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_btn_deconnexion){
            ConnectUtils.deconnexion(this);
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    // code from https://developer.android.com/develop/ui/views/components/pickers#DatePicker
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new EntrepriseActivity.DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(requireContext(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // On veut le format DD/MM/YYYY
            month++;
            String sday = day < 10 ? "0" + day : "" + day;
            String smonth = month < 10 ? "0" + month : "" + month;

            date.setText(sday + "/" + smonth + "/" + year);
        }
    }
}