package com.example.itravel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Information extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    private TextView textView;
    private List<String> listSentences;
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        String textFromDatabase = Database.getText();
        listSentences = Arrays.asList(textFromDatabase.split("\\."));

        textView = findViewById(R.id.textView);
        textView.setText(textFromDatabase);

        textToSpeech = new TextToSpeech(this, this);
    }

    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                textView.setText("Language not supported");
            }
        } else {
            textView.setText("Initialization failed");
        }
    }

    public void playText(View view) {
        currentIndex = 0;
        textToSpeech.speak(listSentences.get(currentIndex), TextToSpeech.QUEUE_FLUSH, null);
    }

    public void pauseText(View view) {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }

}
