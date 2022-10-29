package ca.qc.bdeb.c5gm.tp1moblie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void onClickMaps(View view) {
        startActivity(new Intent(this, MapsActivity.class));
    }

    public void onClickPlus(View view) {
        startActivity(new Intent(this, EntrepriseActivity.class));
    }
}