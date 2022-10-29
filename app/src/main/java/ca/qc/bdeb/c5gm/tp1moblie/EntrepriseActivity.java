package ca.qc.bdeb.c5gm.tp1moblie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class EntrepriseActivity extends AppCompatActivity {

    Stockage stockage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entreprise);

        stockage = Stockage.getInstance(getApplicationContext());
    }
}