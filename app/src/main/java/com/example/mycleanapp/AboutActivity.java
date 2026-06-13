package com.example.mycleanapp;  // 改成你的包名

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class AboutActivity extends AppCompatActivity {

    private int clickCount = 0;
    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView versionText = findViewById(R.id.version_text);
        versionText.setText("版本号: Aardvark 1 (v1.0)");

        versionText.setOnClickListener(v -> {
            long now = System.currentTimeMillis();
            if (now - lastClickTime > 500) {
                clickCount = 0;
            }
            lastClickTime = now;
            clickCount++;

            if (clickCount >= 7) {
                createWhitelistFile();
                clickCount = 0;
            }
        });
    }

    private void createWhitelistFile() {
        try {
            File whiteListFile = new File(getExternalFilesDir(null), ".myapp_whitelist");
            if (!whiteListFile.exists()) {
                if (whiteListFile.createNewFile()) {
                    Toast.makeText(this, "✅ 白名单已创建，重启应用生效", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "❌ 白名单创建失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "白名单已存在，无需重复创建", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "创建失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}