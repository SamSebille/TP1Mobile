package ca.qc.bdeb.c5gm.tp1moblie.Activities;

import android.os.Bundle;
import android.view.MenuItem;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.UUID;

import ca.qc.bdeb.c5gm.tp1moblie.BD.Stockage;
import ca.qc.bdeb.c5gm.tp1moblie.R;
import ca.qc.bdeb.c5gm.tp1moblie.REST.ConnectUtils;

public class EtudiantActivity extends AppCompatActivity {
    // L'entreprise a modifier
    private UUID etudiant_id;
    private Etudiant etudiant;

    private Stockage stockage;

    private TextView nomEntreprise;
    private static final TextView[] saisies = new TextView[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etudiant);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        stockage = Stockage.getInstance(getApplicationContext());

        nomEntreprise = findViewById(R.id.menu_nom_etudiant);
        saisies[0] = findViewById(R.id.nom_etudiant);
        saisies[1] = findViewById(R.id.prenom_etudiant);
        saisies[2] = findViewById(R.id.email_etudiant);
        saisies[3] = findViewById(R.id.stage);
        saisies[4] = findViewById(R.id.entreprise);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_btn_deconnexion) {
            ConnectUtils.deconnexion(this);
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // On recupere l'etudiant cliquée
        Bundle extras = getIntent().getExtras();

        getSupportActionBar().setTitle("Détails étudiant");

        etudiant_id = UUID.fromString(extras.getString("ETUDIANT_ID"));

        etudiant = stockage.getEtudiant(etudiant_id);

        saisies[0].setText(etudiant.getNom());
        saisies[1].setText(etudiant.getPrenom());
        saisies[2].setText(etudiant.getEmail());
        if (etudiant.isStageTrouve())
            saisies[3].setText("Oui");
        else
            saisies[3].setText("Non");
        saisies[4].setText(Integer.toString(etudiant.getNombreEntreprise()));
    }
}