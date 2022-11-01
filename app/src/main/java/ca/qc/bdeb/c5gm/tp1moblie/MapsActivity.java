package ca.qc.bdeb.c5gm.tp1moblie;

import static android.content.ContentValues.TAG;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ca.qc.bdeb.c5gm.tp1moblie.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       // LatLng position = getPosition("Montreal");
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").icon(vectorToBitmap(R.drawable.logo_framage, Color.parseColor("#FF0000"))));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

    public LatLng getPosition(String adresse){
        LatLng position = null;
        if (Geocoder.isPresent()){
       Geocoder geocoder = new Geocoder(this, Locale.getDefault());
       List<Address> adresses;
            try {
               adresses = geocoder.getFromLocationName(adresse,2);
               if (adresses.size()>0){
                   Address resultAdrresse = adresses.get(0);
                   position = new LatLng(resultAdrresse.getLatitude(),resultAdrresse.getLongitude());
                   Log.d(TAG, "getPosition: " + position.toString());
               }
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        return position;
    }

    private BitmapDescriptor vectorToBitmap(@DrawableRes int id, @ColorInt int color){
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(),id,null);
        Bitmap bitmap  = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0,0,canvas.getWidth(),canvas.getHeight());
        DrawableCompat.setTint(vectorDrawable,color);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


}