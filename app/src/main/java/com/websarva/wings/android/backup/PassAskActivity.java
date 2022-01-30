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

import java.util.Random;

// パスワード問い合わせアクティビティ（ログインする前の）
public class PassAskActivity extends AppCompatActivity {

    // フィールド
    private Typeface customFont;
    private Button appNameBt, returnButton, sendButton, appNameButton, inputPassBt;
    private TextInputLayout textInputLayout;
    private TextInputEditText textInputEditText;
    private int passKey;
    private static String mailAddress;
    private Intent intent;
    private InputMethodManager mInputMethodManager;
    private ConstraintLayout passAskLayout;
    private KeyInputDialogFragment keyInputDialogFragment;
    private int senderType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pass_ask);

        appNameBt = findViewById(R.id.pass_ask_title_bt);
        returnButton = findViewById(R.id.pass_ask_return_bt);
        sendButton = findViewById(R.id.pass_ask_send_bt);
        appNameButton = findViewById(R.id.pass_ask_title_bt);
        mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        passAskLayout = findViewById(R.id.pass_ask_layout);
        textInputLayout = findViewById(R.id.pass_ask_til);
        textInputEditText = findViewById(R.id.pass_ask_tiet);
        inputPassBt = findViewById(R.id.pass_ask_passinput_bt);


        //アプリ名のフォントを変更
        customFont = Typeface.createFromAsset(getAssets(), "Kalam-Bold.ttf");
        appNameBt.setTypeface(customFont);

        //戻るボタンが押されたらアクティビティを終了
        returnButton.setOnClickListener((View view2)->{
            finish();
        });

        //送信ボタンが押されたらsetSendCompメソッドを起動
        sendButton.setOnClickListener((View v) -> {

            mailAddress = textInputEditText.getText().toString();

            if (mailAddress.equals("")) {
                textInputLayout.setError(getString(R.string.error_message1));
            } else {
                Random rnd = new Random();
//                passKey = rnd.nextInt(100000) + 10000;
                passKey = 10000;
                asyncTask a = new asyncTask();
                a.execute("software.project.uto","uto1230304");
                inputPassBt.setVisibility(View.VISIBLE);
            }

        });

        //タイトルが押されてらタイトルアクティビティを起動
        appNameButton.setOnClickListener((View v) -> {
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                textInputLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputPassBt.setOnClickListener((View v) -> {
            keyInputDialogFragment = new KeyInputDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("PASS_KEY", passKey);
            bundle.putInt("SENDERTYPE", senderType);
            keyInputDialogFragment.setArguments(bundle);
            keyInputDialogFragment.show(getSupportFragmentManager(), "key_input_dialog");
        });

    }

    // メール送信
    private class asyncTask extends android.os.AsyncTask{
        protected String account;
        protected String password;

        @Override
        protected Object doInBackground(Object... obj){
            account=(String)obj[0];
            password=(String)obj[1];

            java.util.Properties properties = new java.util.Properties();
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.socketFactory.post", "587");
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.starttls.enable", "true");


            final javax.mail.Message msg = new javax.mail.internet.MimeMessage(javax.mail.Session.getDefaultInstance(properties, new javax.mail.Authenticator(){
                @Override
                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new javax.mail.PasswordAuthentication(account,password);
                }
            }));

            try {
                msg.setFrom(new javax.mail.internet.InternetAddress(account + "@gmail.com"));
                //自分自身にメールを送信
                msg.setRecipients(javax.mail.Message.RecipientType.TO, javax.mail.internet.InternetAddress.parse(mailAddress));
                //msg.setRecipients(javax.mail.Message.RecipientType.TO, javax.mail.internet.InternetAddress.parse("chino1771@icloud.com"));
                msg.setSubject(getString(R.string.key_message));
                msg.setText(getString(R.string.mail_message1) + passKey + getString(R.string.mail_message2));

                javax.mail.Transport.send(msg);

            } catch (Exception e) {
                return e.toString();
            }

            return getString(R.string.mail_message3);

        }
        @Override
        protected void onPostExecute(Object obj) {
            //画面にメッセージを表示する
            android.content.Context context = getApplicationContext();
            android.widget.Toast t=android.widget.Toast.makeText(context,(String)obj, android.widget.Toast.LENGTH_LONG);
            t.show();
        }
    }


    // EditText編集時に背景をタップしたら呼ばれる
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // キーボードを隠す
        mInputMethodManager.hideSoftInputFromWindow(passAskLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        // 背景にフォーカスを移す
        passAskLayout.requestFocus();
        return false;
    }

    public static String getMailAddress() {
        return mailAddress;
    }


}


