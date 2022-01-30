package com.websarva.wings.android.backup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

// パスワードを入力するダイアログ生成クラス
public class InputPassDialogFragment extends DialogFragment {

    // フィールド
    private String inputPass; // 入力されたパスワードを受け取る変数
    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private View dialogView;
    private TextInputLayout textInputLayout;
    private TextInputEditText textInputEditText;
    private ConfUserInfoFragment userinfoFragment;
    private ResultPassDialogFragment resultPassDialogFragment;
    private FragmentTransaction langTransaction;
    private String getPass = HomeActivity.getPass();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        inflater = LayoutInflater.from(this.getContext());
        dialogView = inflater.inflate(R.layout.dialog_pass, null);
        textInputEditText = (TextInputEditText) dialogView.findViewById(R.id.pass_dialog_pass);
        textInputLayout = (TextInputLayout)dialogView.findViewById(R.id.pass_dialog_til);


        builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        builder.setView(dialogView)
                .setTitle(getString(R.string.input_dialog_message1))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        inputPass = textInputEditText.getText().toString();

                        if (inputPass.equals(getPass)) {
                            // パスワードが一致していた
                            // 利用者情報の変更画面へ遷移
                            userinfoFragment = new ConfUserInfoFragment();
                            setFragment(userinfoFragment);
                        } else {
                            resultPassDialogFragment = new ResultPassDialogFragment();
                            resultPassDialogFragment.show(getActivity().getSupportFragmentManager(), "result_dialog");
                        }
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        /*キャンセルされたときの処理*/
                    }
                });

        return builder.create();
    }

    // フラグメントセットメソッド
    public void setFragment(Fragment fragment) {
        // フラグメントトランザクションの開始
        langTransaction = getActivity().getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // レイアウトをfragmentに置き換え（追加）
        langTransaction.replace(R.id.fragment_container, fragment);
        // 置き換えのトランザクションをバックスタックに保存する
        langTransaction.addToBackStack(null);
        // フラグメントトランザクションをコミット
        langTransaction.commit();
    }
}
