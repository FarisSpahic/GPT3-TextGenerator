package com.example.textgeneratorfinalversion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static ImageButton readBtn;
    private static ImageButton generateBtn;
    private static ImageButton tuneBtn;
    private TextToSpeech tts;
    private String promptStr, generatedStr;
    private static TextView generatedTV;
    private static TextInputEditText promptET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readBtn = findViewById(R.id.readButton);
        generateBtn = findViewById(R.id.translateButton);
        tuneBtn = findViewById(R.id.settingsButton);
        generatedTV = findViewById(R.id.resultTextView);
        promptET = findViewById(R.id.promptEditText);

        readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generatedStr = generatedTV.getText().toString();
                if (!generatedStr.equals("")){
                    tts.setLanguage(Locale.ENGLISH);
                    tts.speak(generatedStr, TextToSpeech.QUEUE_FLUSH, null, null);
                }

            }
        });


        tuneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });



        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptStr = promptET.getText().toString();

                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/json");
                String value = "{\r\"prompt\": \"In a shocking finding, scientist discovered a herd of unicorns living in a remote, previously unexplored valley, in the Andes Mountains. Even more surprising to the researchers was the fact that the unicorns spoke perfect English.\",\r\"temperature\": 0.8\r }";
                @androidx.annotation.NonNull
                RequestBody body = RequestBody.create(mediaType, value);
            Request request = new Request.Builder()
                    .url("https://gpt-text-generation.p.rapidapi.com/completions?temperature=1&stream=false&top_k=999&seed=0")
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .addHeader("X-RapidAPI-Host", "gpt-text-generation.p.rapidapi.com")
                    .addHeader("X-RapidAPI-Key", "946b1f6709msh02a21458e3673fdp1f72c2jsn1f79547e501d")
                    .build();

                try {
                    Response response = client.newCall(request).execute();
                    generatedTV.setText(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}