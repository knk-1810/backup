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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAddActivity extends AppCompatActivity {

    private UploadTask task;
    private String inputId, inputPass, inputRePass;
    private TextInputLayout idTextInputLayout, passTextInputLayout, repassTextInputLayout;
    private TextInputEditText userinfoEt1, userinfoEt2, userinfoEt3;
    private Button userAddPushBt, appNameBt, returnButton, appNameButton, userAddLoginButton;
    private Boolean isUpdate = true, checkResult = true;
    //正規表現パターン
    private String regex_Alphabet = "^[A-Za-z]+$" ; // アルファベットのみ
    private String regex_AlphaNum = "^[A-Za-z0-9]+$" ; // 半角英数字のみ
    private String url = "http://3.83.166.248/passcheck.html";
    private Typeface customFont;
    private Intent intent;
    private InputMethodManager mInputMethodManager;
    private ConstraintLayout userAddLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_add);

        userAddPushBt = findViewById(R.id.user_add_push_bt);
        idTextInputLayout  = findViewById(R.id.user_add_id_til);
        passTextInputLayout  = findViewById(R.id.user_add_paas_til);
        repassTextInputLayout  = findViewById(R.id.user_add_repaas_til);
        userinfoEt1 = findViewById(R.id.user_add_id);
        userinfoEt2 = findViewById(R.id.user_add_pass);
        userinfoEt3 = findViewById(R.id.user_add_repass);
        appNameBt = findViewById(R.id.user_add_title_bt);
        returnButton = findViewById(R.id.user_add_return_bt);
        appNameButton = findViewById(R.id.user_add_title_bt);
        mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        userAddLayout = findViewById(R.id.user_add_layout);

        // userinfoEt1にリスナを設定
        userinfoEt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // テキストが修正する直前に行う処理
                // エラー表示内容をリセット
                idTextInputLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // userinfoEt2にリスナを設定
        userinfoEt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // テキストが修正する直前に行う処理
                // エラー表示内容をリセット
                passTextInputLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // userinfoEt3にリスナを設定
        userinfoEt3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // テキストが修正する直前に行う処理
                // エラー表示内容をリセット
                repassTextInputLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // ボタンをタップして非同期処理を開始
        userAddPushBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isUpdate = true;

                // 入力データを取得
                inputId = userinfoEt1.getText().toString();
                inputPass = userinfoEt2.getText().toString();
                inputRePass = userinfoEt3.getText().toString();

                //メールアドレス、パスワード、確認用パスワードが入力されているかの判定処理
                //入力文字がない場合、警告を表示
                if (inputId.equals("")) {
                    idTextInputLayout.setError(getString(R.string.error_message1));
                    isUpdate = false;

                }
                if (inputPass.equals("")) {
                    passTextInputLayout.setError(getString(R.string.error_message2));
                    isUpdate = false;

                }
                if (inputRePass.equals("")) {
                    repassTextInputLayout.setError(getString(R.string.error_message3));
                    isUpdate = false;

                }
                if (inputPass.equals(inputRePass)) {
                    // パスワードと確認用パスワードが一致している場合

                    // パスワードの文字制限を判定
                    if (inputPass.length() < 6 || inputPass.length() > 12) {
                        passTextInputLayout.setError(getString(R.string.error_message4));
                        isUpdate = false;

                        // パスワードがアルファベットと数字を含んでいるかを判定
                    } else if (checkLogic(regex_Alphabet, inputPass)) {
                        passTextInputLayout.setError(getString(R.string.error_message5));
                        isUpdate = false;
                    }

                } else {
                    // パスワードと確認用パスワードが一致していない
                    repassTextInputLayout.setError(getString(R.string.error_message6));
                    isUpdate = false;
                }
                if (isUpdate == true) {
                    //更新成功処理
                    //エラー表示内容をリセット
                    idTextInputLayout.setError(null);
                    passTextInputLayout.setError(null);
                    repassTextInputLayout.setError(null);

                    task = new UploadTask();
                    task.setListener(createListener());
                    task.execute(inputId, inputPass);

                }
            }
        });

        // アプリ名のフォントを変更
        customFont = Typeface.createFromAsset(getAssets(), "Kalam-Bold.ttf");
        appNameBt.setTypeface(customFont);

        // 戻るボタンが押された時に前の画面へ戻る
        returnButton.setOnClickListener((View view) -> {
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // タイトルが押されたらタイトルアクティビティを起動
        appNameButton.setOnClickListener((View v) -> {
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    // 新規登録完了した後のメソッド
    private void setUserAddComp() {
        setContentView(R.layout.activity_user_add_comp);

        appNameBt = findViewById(R.id.user_add_comp_title_bt);
        userAddLoginButton = findViewById(R.id.user_add_comp_login_bt);
        appNameButton = findViewById(R.id.user_add_comp_title_bt);

        //アプリ名のフォントを変更
        customFont = Typeface.createFromAsset(getAssets(), "Kalam-Bold.ttf");
        appNameBt.setTypeface(customFont);

        // ログインボタンが押されたらログインアクティビティを起動
        userAddLoginButton.setOnClickListener((View v) -> {
            intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        // タイトルが押されたらタイトルアクティビティを起動
        appNameButton.setOnClickListener((View v) -> {
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private UploadTask.Listener createListener() {
        return new UploadTask.Listener() {
            @Override
            public void onSuccess(String result) {
                if(result.equals("true")) {
                    setUserAddComp();
                } else {
                    repassTextInputLayout.setError("失敗しました");
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        task.setListener(null);
        super.onDestroy();
    }

    // 受け取った引数が、アルファベットと数字を含んでいるか判定を行うメソッド
    public boolean checkLogic(String regex, String target) {
        checkResult= true;
        if( target == null || target.isEmpty() ) return false ;
        // 引数に指定した正規表現regexがtargetにマッチするか確認する
        Pattern p1 = Pattern.compile(regex); // 正規表現パターンの読み込み
        Matcher m1 = p1.matcher(target); // パターンと検査対象文字列の照合
        checkResult = m1.matches(); // 照合結果をtrueかfalseで取得
        return checkResult;
    }

    // EditText編集時に背景をタップしたら呼ばれる
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // キーボードを隠す
        mInputMethodManager.hideSoftInputFromWindow(userAddLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        // 背景にフォーカスを移す
        userAddLayout.requestFocus();
        return false;
    }

}
