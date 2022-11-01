package ca.qc.bdeb.c5gm.tp1moblie;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Futur écran de connexion
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickConnexion(View view) {
        startActivity(new Intent(MainActivity.this, MenuActivity.class));
    }
}