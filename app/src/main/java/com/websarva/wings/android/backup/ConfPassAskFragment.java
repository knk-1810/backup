package com.websarva.wings.android.backup;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Random;

// 設定画面からパスワードを入力する際、パスワードを忘れた場合のフラグメント生成クラス
public class ConfPassAskFragment extends Fragment {

    // フィールド
    private Button sendBt, returnBt, inputPassBt;
    private TextInputLayout textInputLayout;
    private TextInputEditText textInputEditText;
    private int passKey;
    private String mailAddress;
    private Intent intent;
    private FragmentManager fm;
    private KeyInputDialogFragment keyInputDialogFragment;
    private int senderType = 2;


    public ConfPassAskFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conf_pass_ask, container, false);

        returnBt = (Button)view.findViewById(R.id.conf_pass_ask_return_bt);
        sendBt = (Button)view.findViewById(R.id.conf_pass_ask_send_bt);
        textInputLayout = (TextInputLayout) view.findViewById(R.id.conf_pass_ask_til);
        textInputEditText = (TextInputEditText) view.findViewById(R.id.conf_pass_ask_tiet);
        inputPassBt = (Button) view.findViewById(R.id.conf_pass_ask_passinput_bt);

        returnBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });

        // 送信ボタンを押した時
        sendBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailAddress = textInputEditText.getText().toString();

                if (mailAddress.equals("")) {
                    textInputLayout.setError(getString(R.string.error_message1));
                } else {
                    Random rnd = new Random();
//                    passKey = rnd.nextInt(100000) + 10000;
                    passKey = 10000;
                    asyncTask a = new asyncTask();
                    a.execute("software.project.uto","uto1230304");
                    inputPassBt.setVisibility(View.VISIBLE);
                }
            }
        });

        inputPassBt.setOnClickListener((View v) -> {
            keyInputDialogFragment = new KeyInputDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("PASS_KEY", passKey);
            bundle.putInt("SENDERTYPE", senderType);
            keyInputDialogFragment.setArguments(bundle);
            keyInputDialogFragment.show(getActivity().getSupportFragmentManager(), "key_input_dialog");
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

        return view;
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
            android.content.Context context = getContext().getApplicationContext();
            android.widget.Toast t=android.widget.Toast.makeText(context,(String)obj, android.widget.Toast.LENGTH_LONG);
            t.show();
        }
    }

}
