package com.websarva.wings.android.backup;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 設定画面から、IDとパスワードを変更するフラグメント生成クラス
public class ConfUserInfoFragment extends Fragment  {

    // フィールド
    private String inputId, inputPass, inputRePass; // 入力されたID、パスワード、確認用パスワード
    private String regex_Alphabet = "^[A-Za-z]+$" ; // アルファベットのみ
    private Boolean isUpdate = true, checkResult = true; // 入力されたID、パスワード、確認用パスワードを変更して良いか判断する変数
    private TextInputLayout idTextInputLayout, passTextInputLayout, repassTextInputLayout;
    private TextInputEditText userinfoEt1, userinfoEt2, userinfoEt3;
    private TextView resultTv;
    private Button userinfoBt, backBt;
    private FragmentManager fm;
    private UpdateTask task;
    private String currentId = HomeActivity.getId();


    public ConfUserInfoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conf_userinfo, container, false);

        resultTv = (TextView) view.findViewById(R.id.conf_userinfo_update_tv);
        idTextInputLayout  = (TextInputLayout)view.findViewById(R.id.conf_userinfo_id_til);
        passTextInputLayout  = (TextInputLayout)view.findViewById(R.id.conf_userinfo_pass_til);
        repassTextInputLayout  = (TextInputLayout)view.findViewById(R.id.conf_userinfo_repass_til);
        userinfoEt1 = (TextInputEditText) view.findViewById(R.id.conf_userinfo_id);
        userinfoEt2 = (TextInputEditText) view.findViewById(R.id.conf_userinfo_pass);
        userinfoEt3 = (TextInputEditText) view.findViewById(R.id.conf_userinfo_repass);
        userinfoBt = (Button) view.findViewById(R.id.conf_userinfo_bt);
        backBt = (Button) view.findViewById(R.id.conf_userinfo_back_bt);

        // userinfoEt1にリスナを設定
        userinfoEt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // テキストが修正する直前に行う処理
                // エラー表示内容をリセット
                idTextInputLayout.setError(null);
                resultTv.setVisibility(View.INVISIBLE);
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
                resultTv.setVisibility(View.INVISIBLE);
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
                resultTv.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 更新ボタンが押された時の処理
        userinfoBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isUpdate = true;

                // 入力データを取得
                inputId = userinfoEt1.getText().toString();
                inputPass = userinfoEt2.getText().toString();
                inputRePass = userinfoEt3.getText().toString();

                // メールアドレス、パスワード、確認用パスワードが入力されているかの判定処理
                // 入力文字がない場合、警告を表示
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

                    /*
                    ここにメールアドレス, パスワードを変更する処理を記述
                    変更する値の変数は, inputId, inputPass
                     */
                    task = new UpdateTask();
                    task.setListener(createListener());
                    task.execute(currentId, inputId, inputPass);

                    resultTv.setVisibility(View.VISIBLE);

                    HomeActivity.setId(inputId);
                    HomeActivity.setPass(inputPass);
                }
            }

        });

        // 戻るボタンが押された時に前の画面へ戻る
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
                fm.popBackStack();
            }
        });
        return view;
    }

    // 受け取った引数が、アルファベットと数字を含んでいるか判定を行うメソッド
    public boolean checkLogic(String regex, String target) {
        checkResult = true;
        if( target == null || target.isEmpty() ) return false ;
        // 引数に指定した正規表現regexがtargetにマッチするか確認する
        Pattern p1 = Pattern.compile(regex); // 正規表現パターンの読み込み
        Matcher m1 = p1.matcher(target); // パターンと検査対象文字列の照合
        checkResult = m1.matches(); // 照合結果をtrueかfalseで取得
        return checkResult;
    }

    private UpdateTask.Listener createListener() {
        return new UpdateTask.Listener() {
            @Override
            public void onSuccess(String result) {

            }
        };
    }

}
