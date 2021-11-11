package com.codecademy.christiansoe.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codecademy.christiansoe.helper.DownloadImageTask;
import com.codecademy.christiansoe.R;
import com.codecademy.christiansoe.model.Field;

import java.util.HashMap;
import java.util.Locale;

public class FieldInformation extends AppCompatActivity {

    private Field field;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_information);

        //Gets the Field object which was sent from "Game"
        Intent intent = getIntent();
        field = (Field) intent.getSerializableExtra("clickedField");


        //Fills out the information's from the field to the activity.
        TextView fieldName = findViewById(R.id.fieldNameTextView);
        fieldName.setText(field.getName());

        TextView fieldDescription = findViewById(R.id.fieldDescriptionTextView);
        fieldDescription.setText(field.getDescription());
        fieldDescription.setMovementMethod(ScrollingMovementMethod.getInstance());

        ImageView imageView = findViewById(R.id.chosenFieldImageView);

        //Uses DownloadImageTask to "draw" the picture from the field url.
        new DownloadImageTask(imageView).execute((field.getPictureUrl()));


        //Creates a new TextToSpeech object, and sets the language to Danish.
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR){
                    textToSpeech.setLanguage(new Locale("da-DK"));
                }

            }
        });

        //Gets the "play button" from the activity
        ImageButton playSound = findViewById(R.id.playSoundButton);

        //When the playSound button is clicked then activate the text to speech.
        playSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = field.getDescription();
                HashMap<String, String> params = new HashMap<>();
                params.put(TextToSpeech.Engine.KEY_PARAM_VOLUME, "1.8");
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, params);
            }
        });

    }
    //Don't know if we need this.
    @Override
    protected void onPause(){
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }

}