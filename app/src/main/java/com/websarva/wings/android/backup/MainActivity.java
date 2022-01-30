package com.websarva.wings.android.backup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

// タイトルアクティビティ
//アプリ起動後に一番最初に表示される画面
public class MainActivity extends AppCompatActivity {

    // フィールド
    private Typeface customFont;
    private TextView titleText;
    private Button loginButton, userAddButton;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        titleText = findViewById(R.id.main_title_txt);
        loginButton = findViewById(R.id.main_login_bt);
        userAddButton = findViewById(R.id.main_user_add_bt);

        // アプリ名のフォントを変更
        customFont = Typeface.createFromAsset(getAssets(), "Kalam-Bold.ttf");
        titleText.setTypeface(customFont);

        // ログインボタンが押されたらログインアクティビティを起動
        loginButton.setOnClickListener((View v) -> {
            intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        //新規登録ボタンが押されたら新規登録アクティビティを起動
        userAddButton.setOnClickListener((View v) -> {
            intent = new Intent(this, UserAddActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

    }

}