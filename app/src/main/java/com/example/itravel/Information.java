package com.example.itravel;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Information extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    private TextView textView;
    private List<String> listSentences;
    private int currentIndex = 0;
    private int currentPlayIndex = 0;
    private int currentPlayPosition = 0;
    private boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        String textFromDatabase = Database.getText();
        listSentences = Arrays.asList(textFromDatabase.split("\\."));

        textView = findViewById(R.id.textView);
        textView.setText(textFromDatabase);

        textToSpeech = new TextToSpeech(this, this);
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {

            }

            @Override
            public void onDone(String utteranceId) {

                speakNextSentence();
            }

            @Override
            public void onError(String utteranceId) {

            }

            @Override
            public void onStop(String utteranceId, boolean interrupted) {

                if (utteranceId.equals(String.valueOf(currentIndex))) {
                    currentPlayPosition = currentIndex;
                }
            }
        });
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
        if (isPaused) {
            isPaused = false;
            speakNextSentence(currentPlayIndex, currentPlayPosition);
        } else {
            currentIndex = 0;
            speakNextSentence();
        }
    }

    private void speakNextSentence() {
        if (currentIndex < listSentences.size()) {
            String sentence = listSentences.get(currentIndex);
            textToSpeech.speak(sentence, TextToSpeech.QUEUE_ADD, null, String.valueOf(currentIndex));
            currentIndex++;
        }
    }

    private void speakNextSentence(int startIndex, int startPosition) {
        if (startIndex < listSentences.size()) {
            for (int i = startIndex; i < listSentences.size(); i++) {
                String sentence = listSentences.get(i);
                textToSpeech.speak(sentence, TextToSpeech.QUEUE_ADD, null, String.valueOf(i));
            }
            currentIndex = startIndex;
        }
    }

    public void pauseText(View view) {
        if (textToSpeech != null && textToSpeech.isSpeaking()) {
            textToSpeech.stop();
            isPaused = true;
            currentPlayIndex = currentIndex;
        }
    }
}
