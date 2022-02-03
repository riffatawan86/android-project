package com.test.project.samplelocation.activities;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.test.project.samplelocation.R;
import com.test.project.samplelocation.models.MyLocationModel;
import com.test.project.samplelocation.utils.MyConstants;
import com.test.project.samplelocation.utils.MyGeocoderUtils;

public class AddLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    private View mapView;
    private Marker marker;
    private double latitude;
    private double longitude;
    private String address;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        init();
    }

    private void init() {
        latitude = MyConstants.Companion.getLatitude();
        longitude = MyConstants.Companion.getLongitude();
        address = MyConstants.Companion.getAddress();
        findViewById(R.id.searchIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executeCommand();
            }
        });
        editText = findViewById(R.id.editText);
        findViewById(R.id.clearBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                executeCommand();
                return true;
            }
        });
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.setRetainInstance(true);
        mapView = mapFragment.getView();
        findViewById(R.id.addLocCv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyLocationModel myLocationModel = new MyLocationModel(latitude, longitude);
                Intent intent = new Intent();
                intent.putExtra(MyConstants.Companion.getDATA_FROM_LOCATION(), myLocationModel);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    private void executeCommand() {
        String myLoc = editText.getText().toString().trim();
        if (!myLoc.equals("")) {
            LatLng latLng = MyGeocoderUtils.Companion.getLocationFromAddress(this, myLoc);
            if (latLng != null) {
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                //address = myLoc;
                addMarker(latLng);
            }
        } else {
            Toast.makeText(this, "Please enter any location.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng latLngGet = new LatLng(latitude, longitude);
        addMarker(latLngGet);
    }

    public void addMarker(LatLng latLng) {
        googleMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(address + "");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                address = MyGeocoderUtils.Companion.getAddressFromLocation(AddLocationActivity.this, latitude, longitude);
                addMarker(latLng);
            }
        });

    }
}
