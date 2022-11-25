package ca.qc.bdeb.c5gm.tp1moblie.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import ca.qc.bdeb.c5gm.tp1moblie.R;

public class ConnexionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
    }


    public void onClickConnexion(View view) {
        startActivity(new Intent(ConnexionActivity.this, MenuActivity.class));
    }

    public void onClickInscription(View view) {
        startActivity(new Intent(ConnexionActivity.this, InscriptionActivity.class));
    }

    @Override
    public void onBackPressed() {

    }
}