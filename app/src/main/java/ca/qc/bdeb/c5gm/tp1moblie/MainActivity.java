package ca.qc.bdeb.c5gm.tp1moblie;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

/**
 * Futur écran de connexion
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        ConnectUtils.testerConnexion();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startActivity(new Intent(MainActivity.this, ConnexionActivity.class));
    }


}