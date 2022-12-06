package ca.qc.bdeb.c5gm.tp1moblie.Activities;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import ca.qc.bdeb.c5gm.tp1moblie.R;
import ca.qc.bdeb.c5gm.tp1moblie.REST.ConnectUtils;

/**
 * Futur écran de connexion
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        ConnectUtils.testerConnexion(this);
    }

    public void startActivity(boolean succesConnexion){
        if (succesConnexion)
            startActivity(new Intent(MainActivity.this, ConnexionActivity.class));
        else
            startActivity(new Intent(MainActivity.this, MenuActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


}