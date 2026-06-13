package com.example.mycleanapp;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    private Spinner languageSpinner;
    private Switch nightModeSwitch;
    private EditText nicknameEdit;
    private Button saveButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sharedPreferences = getSharedPreferences("MyAppSettings", MODE_PRIVATE);

        languageSpinner = findViewById(R.id.languageSpinner);
        nightModeSwitch = findViewById(R.id.nightModeSwitch);
        nicknameEdit = findViewById(R.id.nicknameEdit);
        saveButton = findViewById(R.id.saveButton);

        // 设置下拉框选项（使用字符串资源）
        String[] languages = {
                getString(R.string.chinese),
                getString(R.string.english),
                getString(R.string.japanese)
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, languages);
        languageSpinner.setAdapter(adapter);

        loadSettings();

        saveButton.setOnClickListener(v -> saveSettings());
    }

    private void loadSettings() {
        languageSpinner.setSelection(sharedPreferences.getInt("language", 0));
        nightModeSwitch.setChecked(sharedPreferences.getBoolean("nightMode", false));
        nicknameEdit.setText(sharedPreferences.getString("nickname", ""));
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int oldLanguage = sharedPreferences.getInt("language", 0);
        int newLanguage = languageSpinner.getSelectedItemPosition();

        editor.putInt("language", newLanguage);
        editor.putBoolean("nightMode", nightModeSwitch.isChecked());
        editor.putString("nickname", nicknameEdit.getText().toString());
        editor.apply();

        // 如果语言改变了，切换语言
        if (oldLanguage != newLanguage) {
            switchLanguage(newLanguage);
        }

        Toast.makeText(this, getString(R.string.save_settings) + " ✓", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void switchLanguage(int languageCode) {
        Locale locale;
        switch (languageCode) {
            case 1:
                locale = Locale.ENGLISH;
                break;
            case 2:
                locale = new Locale("ja");
                break;
            default:
                locale = Locale.CHINESE;
                break;
        }

        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        // 重启 Activity 让语言生效
        recreate();
    }
}