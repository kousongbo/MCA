package com.example.mycleanapp;

import java.io.File;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private TextView welcomeText;

    // ==================== ADB 检测相关 ====================

    private boolean isAuthorizedDevice() {
        try {
            File whiteListFile = new File(getExternalFilesDir(null), ".myapp_whitelist");
            return whiteListFile.exists();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTraced() {
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.FileReader("/proc/self/status")
            );
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("TracerPid:")) {
                    int tracerPid = Integer.parseInt(line.substring(10).trim());
                    reader.close();
                    return tracerPid != 0;
                }
            }
            reader.close();
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private void killApp() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void checkAdbAndExit() {
        if (isAuthorizedDevice()) {
            return;
        }
        if (isTraced()) {
            killApp();
        }
    }

    // ==================== 时间验证相关 ====================

    private void startTimeValidation() {
        new TimeValidator(this, new TimeValidator.Callback() {
            @Override
            public void onSuccess() {
                startApp();
            }

            @Override
            public void onExpired() {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("版本已过期")
                        .setMessage("请前往 B 站或 GitHub 下载最新版本")
                        .setCancelable(false)
                        .setPositiveButton("退出", (d, w) -> finishAffinity())
                        .show();
            }

            @Override
            public void onError(int code) {
                String msg = code == 1 ? "需要网络连接" : "获取时间失败，请检查网络";
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("验证失败")
                        .setMessage(msg)
                        .setCancelable(false)
                        .setPositiveButton("重试", (d, w) -> startTimeValidation())
                        .setNegativeButton("退出", (d, w) -> finishAffinity())
                        .show();
            }
        }).start();
    }

    // ==================== 语言设置 ====================

    private void applyLanguage() {
        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferences("MyAppSettings", MODE_PRIVATE);
        }
        int languageCode = sharedPreferences.getInt("language", 0);
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
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    // ==================== 欢迎语刷新 ====================

    private void refreshWelcomeText() {
        if (welcomeText != null && sharedPreferences != null) {
            String nickname = sharedPreferences.getString("nickname", "");
            if (nickname.isEmpty()) {
                nickname = "用户";
            }
            welcomeText.setText(String.format(getString(R.string.welcome), nickname));
        }
    }

    // ==================== 正常启动 ====================

    private void startApp() {
        // 1. 初始化 SharedPreferences
        sharedPreferences = getSharedPreferences("MyAppSettings", MODE_PRIVATE);

        // 2. 应用语言设置
        applyLanguage();

        // 3. 应用夜间模式
        applyNightMode();

        // 4. 加载布局
        setContentView(R.layout.activity_main);

        // 5. 获取欢迎语 TextView 并刷新
        welcomeText = findViewById(R.id.welcomeText);
        refreshWelcomeText();

        // 6. 关于按钮
        Button aboutBtn = findViewById(R.id.aboutBtn);
        aboutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        // 7. 设置按钮
        Button openSettingBtn = findViewById(R.id.openSettingBtn);
        openSettingBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 从设置界面返回时，刷新界面
        if (requestCode == 1) {
            recreate();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPreferences != null) {
            applyNightMode();
            refreshWelcomeText();
        }
    }

    // ==================== 生命周期 ====================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkAdbAndExit();
        startTimeValidation();
    }

    private void applyNightMode() {
        if (sharedPreferences != null) {
            boolean isNightMode = sharedPreferences.getBoolean("nightMode", false);
            if (isNightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
    }
}