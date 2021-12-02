package com.codecademy.christiansoe.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.text.HtmlCompat;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.location.Location;
import android.os.Looper;
import android.provider.Settings;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codecademy.christiansoe.helper.DownloadImageTask;
import com.codecademy.christiansoe.helper.RetrofitInitializer;
import com.codecademy.christiansoe.model.Field;
import com.codecademy.christiansoe.R;
import com.codecademy.christiansoe.model.BingoBoard;
import com.codecademy.christiansoe.model.UserBingoBoard;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Game extends AppCompatActivity {

    private BingoBoard bingoBoard;
    private UserBingoBoard userBingoBoard;
    private TextView bingoBoardTextView;
    private List<Field> fieldList = new ArrayList<>();
    private RetrofitInitializer retrofitInitializer = new RetrofitInitializer();
    private String userId;
    private int mapId;


    //----------------------------------- GPS ---------------------------------------------
    private static final int REQUEST_CHECK_SETTINGS = 1;
    private static final int REQUEST_GRANT_PERMISSION = 2;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private Location currentLocation;
    private LocationCallback locationCallback;
    private Button distanceToFerry;

    //Changed onCrate to onResume so that the page reload every time with the updated field information'.
    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_game);

        //Gets the unique phone/user id
        userId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);


        //Gets the bingoBoard object from the chosen theme on previous activity.
        Intent intent = getIntent();
        bingoBoard = (BingoBoard) intent.getSerializableExtra("chosenBingoBoard");
        mapId = intent.getIntExtra("mapId", 0);

        //Fill out information's from the BingoBoard to the activity display.
        bingoBoardTextView = findViewById(R.id.bingoBoardTextView);
        String name = bingoBoard.getName();
        bingoBoardTextView.setText(name);

        Call<UserBingoBoard> userBingoBoardCall = retrofitInitializer.getUserBingoBoards(bingoBoard.getId(), userId);

        userBingoBoardCall.enqueue(new Callback<UserBingoBoard>() {
            @Override
            public void onResponse(Call<UserBingoBoard> call, Response<UserBingoBoard> response) {

                if(response.isSuccessful()){

                    userBingoBoard = response.body();

                    getUserFields(userBingoBoard.getId());

                } else if(response.code() == 404){
                    userBingoBoard = new UserBingoBoard(false, bingoBoard.getId(), userId);
                    createUserFields(userBingoBoard);
                }
            }

            @Override
            public void onFailure(Call<UserBingoBoard> call, Throwable t) {

            }
        });


        //-------------------------------- GPS --------------------------------
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();
        settingsCheck();
        distanceToFerry = findViewById(R.id.distanceToFerry);

        distanceToFerry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(Game.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(Game.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_GRANT_PERMISSION);
                    return;
                }
                buildLocationCallback();
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

                Location ferry = new Location("");
                ferry.setLatitude(55.320736);
                ferry.setLongitude(15.186038);
                Location you = new Location("");
                you.setLatitude(currentLocation.getLatitude());
                you.setLongitude(currentLocation.getLongitude());

                float distanceInMeters = ferry.distanceTo(you);



                //4km i timen
                int speedMetersPerMinutes = 67;
                float estimatedWalkToFerry = distanceInMeters / speedMetersPerMinutes;

                TextView distanceMeters = findViewById(R.id.distanceMeters);
                distanceMeters.setText(HtmlCompat.fromHtml(String.format("Afstand til færgen: <b>%d</b> meter", Math.round(distanceInMeters)), HtmlCompat.FROM_HTML_MODE_COMPACT));

                TextView distanceTime = findViewById(R.id.distanceTime);
                distanceTime.setText(HtmlCompat.fromHtml(String.format("Afsæt <b>%d</b> minutter", Math.round( estimatedWalkToFerry ) ), HtmlCompat.FROM_HTML_MODE_COMPACT));
            }
        });
    }

    public void showFieldInfo(View view){

        int imageIndex = 0;

        int clickedId = view.getId();

        //Looks for the position of the field that is clicked on
        for(int i = 1; i < 10; i++){
            int imageId = getResources().getIdentifier("imageViewBoard"+i, "id", getPackageName());

            if(clickedId == imageId){
                imageIndex = i - 1;
            }
        }
        //uses the position to get the chosen field.
        Field clickedField = fieldList.get(imageIndex);
        clickedField.setUserId(userId);

        //Sends the user to the next activity with the field object that was clicked on.
        Intent intent = new Intent(this, FieldInformation.class);
        intent.putExtra("clickedField", clickedField);
        startActivity(intent);
    }

    public void getUserFields(int bingoBoardId){

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        //Uses the helper class retrofitInitializer, to make a api call, which returns the fields from the bingoBoard.
        Call<List<Field>> call = retrofitInitializer.getFields(bingoBoardId);

        //Makes the actually call, and checks if the call is valid.
        call.enqueue(new Callback<List<Field>>() {
            @Override
            public void onResponse(Call<List<Field>> call, Response<List<Field>> response) {

                if (!response.isSuccessful()) {
                    return;
                }
                //Looping over the fields, and uses the helper class DownloadImageTask to "draw" the pictures.
                fieldList = response.body();

                int counter = 1;
                for(Field field : fieldList){
                    int id = getResources().getIdentifier("imageViewBoard" + counter, "id", getPackageName());
                    ImageView imageView = findViewById(id);
                    new DownloadImageTask(imageView).execute((field.getPictureUrl()));

                    if(field.isMarked()){
                        imageView.setColorFilter(new ColorMatrixColorFilter(matrix));
                    }

                    counter++;
                }

                winnerCheck();

            }

            @Override
            public void onFailure(Call<List<Field>> call, Throwable t) {

            }
        });




    }

    public void createUserFields(UserBingoBoard userBingoBoard){
       Call<UserBingoBoard> call = retrofitInitializer.createUserBingoBoard(userBingoBoard);

       call.enqueue(new Callback<UserBingoBoard>() {
           @Override
           public void onResponse(Call<UserBingoBoard> call, Response<UserBingoBoard> response) {
               getUserFields(response.body().getId());
           }

           @Override
           public void onFailure(Call<UserBingoBoard> call, Throwable t) {

           }
       });
    }

    public void winnerCheck(){

        //This checks if all the fields in fieldList has isMarked set as true
        boolean allSameName = fieldList.stream().allMatch(Field::isMarked);

        if(allSameName){

            userBingoBoard.setDone(true);

            Call<UserBingoBoard> userBingoBoardCall = retrofitInitializer.resetBingoBoard(userBingoBoard);

            userBingoBoardCall.enqueue(new Callback<UserBingoBoard>() {
                @Override
                public void onResponse(Call<UserBingoBoard> call, Response<UserBingoBoard> response) {

                    if(response.isSuccessful()){
                        gameWon();
                    }
                }

                @Override
                public void onFailure(Call<UserBingoBoard> call, Throwable t) {

                }
            });

        }

    }

    public void gameWon(){

        Intent intent = new Intent(this, WinnerScreen.class);
        intent.putExtra("routeName", bingoBoard.getName());
        startActivity(intent);

    }

    public void showMap(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("long", currentLocation.getLongitude());
        intent.putExtra("lat", currentLocation.getLatitude());
        intent.putExtra("mapId", mapId);
        startActivity(intent);
    }

    // -------------------------------------------- GPS --------------------------------------------------
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
                        resolvable.startResolutionForResult(Game.this,
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