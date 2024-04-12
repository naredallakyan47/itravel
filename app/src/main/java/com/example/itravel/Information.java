package com.example.itravel;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Information extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private TextView Name;
    private TextToSpeech textToSpeech;
    private TextView textView;
    private List<String> listSentences;
    private String name = PlaceFragment.placeName;
    private int currentIndex = 0;
    private boolean isPlaying = false;
    private DatabaseReference mDatabase;
    private ImageView imageView;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private ImageView imageView5;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        Bundle bundle = getIntent().getExtras();
        textView = findViewById(R.id.textView);
        Name = findViewById(R.id.name);
        imageView = findViewById(R.id.image);
        imageView2 = findViewById(R.id.image2);
        imageView3 = findViewById(R.id.image3);
        imageView4 = findViewById(R.id.image4);
        imageView5 = findViewById(R.id.image5);

        textToSpeech = new TextToSpeech(this, this);
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                isPlaying = true;
            }

            @Override
            public void onDone(String utteranceId) {
                currentIndex++;
                if (currentIndex < listSentences.size()) {
                    speakNextSentence();
                } else {
                    isPlaying = false;
                }
            }

            @Override
            public void onError(String utteranceId) {
                isPlaying = false;
            }

            @Override
            public void onStop(String utteranceId, boolean interrupted) {
                isPlaying = false;
            }
        });

        Name.setText(name);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Places");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int imageViewIndex = 0; // Индекс текущего ImageView
                for (DataSnapshot placeSnapshot : dataSnapshot.getChildren()) {
                    String placeName = placeSnapshot.getKey();
                    String placeDescription = placeSnapshot.child("Desc").getValue(String.class);
                    if (placeName.equals(name)) {
                        textView.setText(placeDescription);
                        listSentences = Arrays.asList(placeDescription.split("\\."));

                        // Получаем ссылки на изображения и загружаем их в соответствующие ImageView
                        for (DataSnapshot imageSnapshot : placeSnapshot.child("Images").getChildren()) {
                            String imageUrl = imageSnapshot.getValue(String.class);
                            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
                            final int currentImageViewIndex = imageViewIndex;
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    switch (currentImageViewIndex) {
                                        case 0:
                                            new PlaceFragment.DownloadImageTask(imageView).execute(uri.toString());
                                            break;
                                        case 1:
                                            new PlaceFragment.DownloadImageTask(imageView2).execute(uri.toString());
                                            break;
                                        case 2:
                                            new PlaceFragment.DownloadImageTask(imageView3).execute(uri.toString());
                                            break;
                                        case 3:
                                            new PlaceFragment.DownloadImageTask(imageView4).execute(uri.toString());
                                            break;
                                        case 4:
                                            new PlaceFragment.DownloadImageTask(imageView5).execute(uri.toString());
                                            break;
                                    }
                                }
                            }).addOnFailureListener(e -> Log.e(TAG, "Error downloading image", e));
                            imageViewIndex++;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

    public void togglePlayPause(View view) {
        if (!isPlaying) {
            speakNextSentence();
            ((Button) view).setText("Pause");
        } else {
            if (textToSpeech != null && textToSpeech.isSpeaking()) {
                textToSpeech.stop();
            }
            ((Button) view).setText("Play");
        }
        isPlaying = !isPlaying;
    }

    private void speakNextSentence() {
        if (currentIndex < listSentences.size()) {
            String sentence = listSentences.get(currentIndex);
            textToSpeech.speak(sentence, TextToSpeech.QUEUE_ADD, null, String.valueOf(currentIndex));
        }
    }
    static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageUrl = urls[0];
            try {
                InputStream inputStream = new java.net.URL(imageUrl).openStream();
                return BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                Log.e(TAG, "Error downloading image", e);
                return null;
            }
        }


        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            }
        }
    }}
