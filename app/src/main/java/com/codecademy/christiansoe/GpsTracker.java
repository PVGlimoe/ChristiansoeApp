package com.codecademy.christiansoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class GpsTracker extends AppCompatActivity {

    private static final int REQUEST_CHECK_SETTINGS = 1;
    private static final int REQUEST_GRANT_PERMISSION = 2;
    private FusedLocationProviderClient fusedLocationClient;
    LocationRequest locationRequest;
    private Location currentLocation;
    private LocationCallback locationCallback;
    Button getUpdates;
    TextView latitude2;
    TextView longitude2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_tracker);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();
        settingsCheck();
        getUpdates = findViewById(R.id.getGps);





        getUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(GpsTracker.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(GpsTracker.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_GRANT_PERMISSION);
                    return;
                }
                    buildLocationCallback();
                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

                    latitude2 = findViewById(R.id.lat);
                    latitude2.setText(String.valueOf(currentLocation.getLatitude()));

                    longitude2 = findViewById(R.id.lon);
                    longitude2.setText(String.valueOf(currentLocation.getLongitude()));



            }
        });

    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    // Check for location settings
    public void settingsCheck() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                Log.d("TAG", "onSuccess: settingsCheck");
                getCurrentLocation();
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    Log.d("TAG", "onFailure: settingsCheck");
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(GpsTracker.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    public void getCurrentLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Log.d("TAG", "onSuccess: getLastLocation");
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            currentLocation=location;
                            Log.d("TAG", "onSuccess:latitude "+location.getLatitude());
                            Log.d("TAG", "onSuccess:longitude "+location.getLongitude());
                        }else{
                            Log.d("TAG", "location is null");
                            buildLocationCallback();
                        }
                    }
                });
    }

    private void buildLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    currentLocation=location;
                    Log.d("TAG", "onLocationResult: "+currentLocation.getLatitude());
                }
            };
        };
    }

    //called after user responds to location permission popup
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_GRANT_PERMISSION){
            getCurrentLocation();
        }
    }
    //called after user responds to location settings popup
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG", "onActivityResult: ");
        if(requestCode==REQUEST_CHECK_SETTINGS && resultCode==RESULT_OK)
            getCurrentLocation();
        if(requestCode==REQUEST_CHECK_SETTINGS && resultCode==RESULT_CANCELED)
            Toast.makeText(this, "Please enable Location settings...!!!", Toast.LENGTH_SHORT).show();
    }

}