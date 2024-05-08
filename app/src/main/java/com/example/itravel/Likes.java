package com.example.itravel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Likes extends AppCompatActivity {

    private ListView listView;
    private List<String> likedPlaces = new ArrayList<>();
    private FirebaseAuth mAuth;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            username = currentUser.getDisplayName();
        } else {
            // Если пользователь не аутентифицирован, вы можете предпринять необходимые действия, например, перенаправить его на страницу входа.
        }

        listView = findViewById(R.id.listView);

        DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference("Likes").child(username);

        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                likedPlaces.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    likedPlaces.add(snapshot.getKey());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Likes.this, android.R.layout.simple_list_item_1, likedPlaces);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Обработка ошибки
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedPlace = likedPlaces.get(position);
                openPlaceFragment(selectedPlace);
            }
        });
    }

    private void openPlaceFragment(String placeName) {
        PlaceFragment fragment = new PlaceFragment(placeName);
        fragment.show(getSupportFragmentManager(), "place_fragment");
    }

    public void map(View v) {
        Intent intent = new Intent(this, Map.class);
        startActivity(intent);
    }

    public void Profile(View v) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }



    public void home(View v) {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }
}





