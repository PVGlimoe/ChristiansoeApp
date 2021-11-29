package com.codecademy.christiansoe.activity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ImageView;

import com.codecademy.christiansoe.R;
import com.codecademy.christiansoe.helper.DownloadImageTask;
import com.codecademy.christiansoe.helper.RetrofitInitializer;
import com.codecademy.christiansoe.model.Field;
import com.codecademy.christiansoe.model.Map;
import com.codecademy.christiansoe.model.Point;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.codecademy.christiansoe.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private RetrofitInitializer retrofitInitializer = new RetrofitInitializer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        List<LatLng> route = new ArrayList<>();

        //TO DO GET DYNAMIC MAP ID !!
        Call<Map> call = retrofitInitializer.getMaps(1);

        call.enqueue(new Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {

                if (!response.isSuccessful()) {
                    return;
                }
                Map map = new Map();
                map = response.body();

                List<Point> pointList = map.getPoints();

                for (Point p : pointList) {
                    route.add(new LatLng(p.getLatitude(), p.getLongitude()));
                }

                drawRoute(route);
            }

            @Override
            public void onFailure(Call<Map> call, Throwable t) {

            }
        });

    }

    public void drawRoute(List<LatLng> route) {

        mMap.addPolyline(new PolylineOptions().addAll(route).width(5).color(Color.RED));
        LatLng startPos = new LatLng(route.get(0).latitude, route.get(0).longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPos, 17.5f));



    }
}