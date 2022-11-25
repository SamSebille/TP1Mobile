package ca.qc.bdeb.c5gm.tp1moblie.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import ca.qc.bdeb.c5gm.tp1moblie.BD.Stockage;
import ca.qc.bdeb.c5gm.tp1moblie.R;
import ca.qc.bdeb.c5gm.tp1moblie.databinding.ActivityMapsBinding;

/**
 * Classe qui implemente le fragment de Google Map afin d'afficher une carte interactive
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

    private static final int LOCATION_REQUEST_CODE = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Stockage stockage;
    private static ArrayList<Entreprise> entreprises;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stockage = Stockage.getInstance(this);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        entreprises = stockage.getEntreprises();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_activity_maps);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_frame);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener((GoogleMap.OnMyLocationButtonClickListener) this);
        mMap.setOnMyLocationClickListener((GoogleMap.OnMyLocationClickListener) this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            mMap.setMyLocationEnabled(true);
            localisationUser();

        }
        ajoutMarcker();
    }

    @SuppressLint("MissingPermission")
    private void localisationUser() {
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        mMap.setMyLocationEnabled(true);

                        if (location != null) {
                            Log.d("USERLOCATION", "onSuccess: " + location.getLatitude() + ", " + location.getLongitude());

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10));
                        }
                    }
                });

    }

    private void ajoutMarcker() {
        Log.d("AJOUTMARCKER", "ajoutMarcker: avant addMarcker");
        Geocoder geocoder = new Geocoder(this);
        if (entreprises != null) {
            for (Entreprise entreprise : entreprises) {
                LatLng position = getPosition(entreprise.getAdresse(),geocoder);
                Log.d("AJOUTMARCKER", "position: " + position);
                if (position != null) {
                    mMap.addMarker(new MarkerOptions().position(position).title(entreprise.getNom()).icon(BitmapDescriptorFactory.fromResource(R.drawable.france)));
                }
            }
        }

    }

    public LatLng getPosition(String adresse, Geocoder geocoder) {
        LatLng position = null;
        if (Geocoder.isPresent()) {
            List<Address> adresses = null;
            try {
                adresses = geocoder.getFromLocationName(adresse,1);
                Log.d("GETPOSITION", "getPosition: " + adresses);
                if (adresses != null) {
                    if (adresses.size() > 0) {
                        Address resultAdrresse = adresses.get(0);
                        position = new LatLng(resultAdrresse.getLatitude(), resultAdrresse.getLongitude());
                        Log.d("GETPOSITION", "getPosition: " + resultAdrresse.toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("GETPOSITION", "getPosition: " + adresses);
        }
        return position;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (isPermissionAuth(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION) ||
                    isPermissionAuth(permissions, grantResults, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                localisationUser();
            }
        }
    }


    private boolean isPermissionAuth(String[] permissions, int[] grantResults, String accessFineLocation) {
        for (int i = 0; i < permissions.length; i++) {
            return (grantResults[i] == PackageManager.PERMISSION_GRANTED);
        }
        return false;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }
}