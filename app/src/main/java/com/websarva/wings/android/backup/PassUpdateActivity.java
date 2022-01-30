package com.websarva.wings.android.backup;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PassUpdateActivity extends AppCompatActivity {

    // フィールド
    private Typeface customFont;
    private Button appNameBt, returnButton, updateBt, compAppNameBt, compLoginBt;
    private TextInputLayout textInputLayout, textInputLayout2;
    private TextInputEditText textInputEditText, textInputEditText2;
    private Intent intent;
    private String inputPass, inputRePass;
    private String getId = PassAskActivity.getMailAddress();
    private Boolean isUpdate = true, checkResult = true;
    private PassUpdateTask task;

    //正規表現パターン
    private String regex_Alphabet = "^[A-Za-z]+$" ; // アルファベットのみ
    private String regex_AlphaNum = "^[A-Za-z0-9]+$" ; // 半角英数字のみ

    private ConstraintLayout constraintLayout;
    private InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pass_update);
        constraintLayout = findViewById(R.id.pass_ask_update_layout);
        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        appNameBt = findViewById(R.id.pass_update_title_bt);
        returnButton = findViewById(R.id.pass_update_return_bt);
        updateBt = findViewById(R.id.pass_update_update_bt);
        textInputLayout = findViewById(R.id.paas_update_til1);
        textInputLayout2 = findViewById(R.id.paas_update_til2);
        textInputEditText = findViewById(R.id.pass_update_tiet1);
        textInputEditText2 = findViewById(R.id.pass_update_tiet2);

        //アプリ名のフォントを変更
        customFont = Typeface.createFromAsset(getAssets(), "Kalam-Bold.ttf");
        appNameBt.setTypeface(customFont);

        appNameBt.setOnClickListener((View v) -> {
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        returnButton.setOnClickListener((View v) -> {
            finish();
        });

        updateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isUpdate = true;

                // 入力データを取得
                inputPass = textInputEditText.getText().toString();
                inputRePass = textInputEditText2.getText().toString();

                // パスワード、確認用パスワードが入力されているかの判定処理
                if (inputPass.equals("")) {
                    textInputLayout.setError(getString(R.string.error_message2));
                    isUpdate = false;

                }
                if (inputRePass.equals("")) {
                    textInputLayout2.setError(getString(R.string.error_message3));
                    isUpdate = false;

                }
                if (inputPass.equals(inputRePass)) {
                    // パスワードと確認用パスワードが一致している場合

                    // パスワードの文字制限を判定
                    if (inputPass.length() < 6 || inputPass.length() > 12) {
                        textInputLayout.setError(getString(R.string.error_message4));
                        isUpdate = false;

                        // パスワードがアルファベットと数字を含んでいるかを判定
                    } else if (checkLogic(regex_Alphabet, inputPass)) {
                        textInputLayout.setError(getString(R.string.error_message5));
                        isUpdate = false;
                    }

                } else {
                    // パスワードと確認用パスワードが一致していない
                    textInputLayout.setError(getString(R.string.error_message6));
                    isUpdate = false;
                }
                if (isUpdate == true) {
                    //更新成功処理
                    //エラー表示内容をリセット
                    textInputLayout.setError(null);
                    textInputLayout2.setError(null);

                    /* ここにパスワード更新処理
                        更新するパスワードは inputPass に入っている
                     */
                    task = new PassUpdateTask();
                    task.setListener(createListener());
                    task.execute(getId, inputPass);

                    setPassUpdateComp();
                }
            }
        });


        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // テキストが修正する直前に行う処理
                // エラー表示内容をリセット
                textInputLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textInputEditText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // テキストが修正する直前に行う処理
                // エラー表示内容をリセット
                textInputLayout2.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
        inputMethodManager.hideSoftInputFromWindow(constraintLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        // 背景にフォーカスを移す
        constraintLayout.requestFocus();
        return false;
    }

    private PassUpdateTask.Listener createListener() {
        return new PassUpdateTask.Listener() {
            @Override
            public void onSuccess(String result) {

            }
        };
    }

    // 新規登録完了した後のメソッド
    private void setPassUpdateComp() {
        setContentView(R.layout.activity_pass_update_comp);

        compAppNameBt = findViewById(R.id.pass_update_comp_title_bt);
        compLoginBt = findViewById(R.id.pass_update_login_bt);

        //アプリ名のフォントを変更
        customFont = Typeface.createFromAsset(getAssets(), "Kalam-Bold.ttf");
        compAppNameBt.setTypeface(customFont);

        // ログインボタンが押されたらログインアクティビティを起動
        compLoginBt.setOnClickListener((View v) -> {
            intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        // タイトルが押されたらタイトルアクティビティを起動
        compAppNameBt.setOnClickListener((View v) -> {
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

}