package com.codecademy.christiansoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Locale;

public class FieldInformation extends AppCompatActivity {

    private Field field;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_information);

        Intent intent = getIntent();
        field = (Field) intent.getSerializableExtra("clickedField");

        TextView fieldName = findViewById(R.id.fieldNameTextView);
        fieldName.setText(field.getName());

        TextView fieldDescription = findViewById(R.id.fieldDescriptionTextView);
        fieldDescription.setText(field.getDescription());
        fieldDescription.setMovementMethod(ScrollingMovementMethod.getInstance());

        ImageView imageView = findViewById(R.id.chosenFieldImageView);

        new DownloadImageTask(imageView).execute((field.getPictureUrl()));



        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR){
                    textToSpeech.setLanguage(new Locale("da-DK"));
                }

            }
        });

        ImageButton playSound = findViewById(R.id.playSoundButton);

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

    @Override
    protected void onPause(){
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }

//    public void fieldFound(){
//        Intent.
//    }
}