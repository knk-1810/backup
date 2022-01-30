package com.websarva.wings.android.backup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

// ログインアクティビティ
public class LoginActivity extends AppCompatActivity {

    // フィールド
    private LoginTask task;
    private GetFlagTask flagTask;
    private TextInputLayout idTextInputLayout, passTextInputLayout;
    private TextInputEditText userinfoEt1, userinfoEt2;
    private TextView loginResult;
    private String getId, getPass, return_value;
    private Button returnButton, appNameBt, passAskButton, loginPushButton;
    private Boolean isLogin = true;
    private Typeface customFont;
    private Intent intent;
    private InputMethodManager mInputMethodManager;
    private ConstraintLayout loginLayout;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        idTextInputLayout  = findViewById(R.id.login_id_til);
        passTextInputLayout  = findViewById(R.id.login_paas_til);
        userinfoEt1 = findViewById(R.id.login_id);
        userinfoEt2 = findViewById(R.id.login_pass);
        loginResult = findViewById(R.id.login_result);
        returnButton = findViewById(R.id.login_return_bt);
        appNameBt = findViewById(R.id.login_title_bt);
        passAskButton = findViewById(R.id.login_pass_ask_bt);
        loginPushButton = findViewById(R.id.login_push_bt);
        mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        loginLayout = findViewById(R.id.login_layout);

        // userinfoEt1にリスナを設定
        userinfoEt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // テキストが修正する直前に行う処理
                // エラー表示内容をリセット
                idTextInputLayout.setError(null);
                loginResult.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        // userinfoEt2にリスナを設定
        userinfoEt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // テキストが修正する直前に行う処理
                // エラー表示内容をリセット
                passTextInputLayout.setError(null);
                loginResult.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        // アプリ名のフォントを変更
        customFont = Typeface.createFromAsset(getAssets(), "Kalam-Bold.ttf");
        appNameBt.setTypeface(customFont);

        // 戻るボタンが押されたらアクティビティを終了
        returnButton.setOnClickListener((View view2)->{
            finish();
        });

        // パスワードを忘れたボタンが押されたらパスワード問い合わせアクティビティを起動
        passAskButton.setOnClickListener((View v) -> {
            intent = new Intent(this, PassAskActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        // ログインボタンが押されたらログインアクティビティを起動
        loginPushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return_value= "";
                isLogin = true;

                getId = userinfoEt1.getText().toString();
                getPass = userinfoEt2.getText().toString();

                // メールアドレス、パスワード、確認用パスワードが入力されているかの判定処理
                // 入力文字がない場合、警告を表示
                if (getId.equals("")) {
                    idTextInputLayout.setError(getString(R.string.error_message1));
                    isLogin = false;

                }
                if (getPass.equals("")) {
                    passTextInputLayout.setError(getString(R.string.error_message2));
                    isLogin = false;

                }
                if (isLogin == true) {
                    // 入力文字が空以外の時ログイン判定を行う
                    task = new LoginTask();
                    task.setListener(createListener());
                    task.execute(getId, getPass);
                }
            }
        });

        // タイトルが押されたらタイトルアクティビティを起動
        appNameBt.setOnClickListener((View v) -> {
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private LoginTask.Listener createListener() {
        return new LoginTask.Listener() {
            @Override
            public void onSuccess(String result) {
                if(result.equals("true")) {

                    activityStart();

                } else {
                    loginResult.setVisibility(View.VISIBLE);
                }
            }
        };
    }


    public void activityStart(){

        HomeActivity.isShowAlert = true;
        intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("GETID", getId);
        intent.putExtra("GETPASS", getPass);
//        intent.putExtra("GET_UPGRADE_FLAG", isUpgrade);
        startActivity(intent);
    }

    // EditText編集時に背景をタップしたら呼ばれる
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // キーボードを隠す
        mInputMethodManager.hideSoftInputFromWindow(loginLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        // 背景にフォーカスを移す
        loginLayout.requestFocus();
        return false;
    }

}