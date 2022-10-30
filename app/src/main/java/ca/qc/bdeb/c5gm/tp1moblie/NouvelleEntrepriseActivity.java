package ca.qc.bdeb.c5gm.tp1moblie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.regex.Pattern;

public class NouvelleEntrepriseActivity extends AppCompatActivity {

    private Stockage stockage;

    private static final EditText[] saisies = new EditText[6];
    private static TextView date;

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
        date = findViewById(R.id.sai_date);
    }

    public void onClickValider(View view) {
        boolean isChampVide = false;

        for (EditText champ : saisies){
            if (champ.getText().toString().trim().length() == 0){
                isChampVide = true;
                break;
            }
        }

        Pattern dateRegex = Pattern.compile("\\d{2}/\\d{2}/\\d{4}");

        if (!isChampVide && dateRegex.matcher(date.getText().toString()).matches()){
            Entreprise entreprise = new Entreprise(
                    saisies[0].getText().toString(), saisies[1].getText().toString(),
                    saisies[2].getText().toString(), saisies[3].getText().toString(),
                    saisies[4].getText().toString(), saisies[5].getText().toString(),
                    date.getText().toString());

            stockage.ajouterEntreprise(entreprise);

            Toast.makeText(this,
                    "Entreprise enregistrée.", Toast.LENGTH_LONG).show();
            finish();
        } else if (isChampVide) {
            Toast.makeText(this,
                    "Veuillez remplir tout les champs.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this,
                    "Veuillez insérer un bon format de date (DD/MM/YYYY)",
                    Toast.LENGTH_LONG).show();
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

    // code from https://developer.android.com/develop/ui/views/components/pickers#DatePicker
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
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
            String sday = day < 10 ? "0" + day : "" + day;
            String smonth = month < 10 ? "0" + month : "" + month;

            date.setText(sday + "/" + smonth + "/" + year);
        }
    }
}