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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
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

        SettingsActivity.tempVal = new Float(0.8);
        SettingsActivity.topKVal = 999;

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
                String value = "{\"prompt\": \""+ promptStr +"\",\"temperature\": "+SettingsActivity.tempVal+", \"top_k\": " + SettingsActivity.topKVal + " }";
                System.out.println("Passu " + value);

                RequestBody body = RequestBody.create(mediaType, value);
                Request request = new Request.Builder()
                        .url("https://api.textsynth.com/v1/engines/gptj_6B/completions")
                        .post(body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer 70f14c8ecbdc3a2a160c672ba96eb1b9")
                        .build();

                client.newCall(request).enqueue(new Callback() {
                   @Override
                   public void onFailure(@NonNull Call call, @NonNull IOException e) {
                       e.printStackTrace();
                   }

                   @Override
                   public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                       if(response.isSuccessful()) {
                        String txt = response.body().string();

                              // JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                               //System.out.println(json);

                           MainActivity.this.runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                       generatedTV.setText(txt);

                               }
                           });
                       }
                   }
               });

            }
        });

    }
}