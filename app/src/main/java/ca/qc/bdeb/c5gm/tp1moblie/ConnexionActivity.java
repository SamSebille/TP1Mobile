package ca.qc.bdeb.c5gm.tp1moblie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ConnexionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
    }


    public void onClickConnexion(View view) {
        startActivity(new Intent(ConnexionActivity.this, MenuActivity.class));
    }

    @Override
    public void onBackPressed() {

    }
}