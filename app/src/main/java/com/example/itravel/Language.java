package com.example.itravel;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Language extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        // Инициализация элементов интерфейса
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        RadioButton englishRadioButton = findViewById(R.id.english);
        RadioButton russianRadioButton = findViewById(R.id.russian);

        // Загрузка текущего языка из SharedPreferences
        SharedPreferences preferences = getSharedPreferences("LanguagePrefs", MODE_PRIVATE);
        String savedLanguage = preferences.getString("language", "");

        // Установка выбранного языка, если он сохранен в SharedPreferences
        if (savedLanguage.equals("english")) {
            englishRadioButton.setChecked(true);
        } else if (savedLanguage.equals("russian")) {
            russianRadioButton.setChecked(true);
        }

        // Слушатель изменения выбора языка
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Определение выбранного языка
                String selectedLanguage;
                if (checkedId == R.id.english) {
                    selectedLanguage = "english";
                } else {
                    selectedLanguage = "russian";
                }

                // Сохранение выбранного языка в SharedPreferences
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("language", selectedLanguage);
                editor.apply();
            }
        });
    }

    // Метод для обработки нажатия на кнопку "Save"
    public void saveLanguage(View view) {
        Toast.makeText(this, "Language settings saved", Toast.LENGTH_SHORT).show();
        // Здесь можно добавить дополнительные действия при сохранении языка
    }
}
