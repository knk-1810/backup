package com.websarva.wings.android.backup;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

// 合言葉を入力するダイアログ生成クラス
public class KeyInputDialogFragment extends DialogFragment {

    // フィールド
    private String getPassKey, inputPassKey;
    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private View dialogView;
    private EditText editText;
    private ResultPassKeyDialogFragment resultPassKeyDialogFragment;
    private int getSenderType;
    private FragmentTransaction ft;
    private ConfUserInfoFragment confUserInfoFragment;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        inflater = LayoutInflater.from(this.getContext());
        dialogView = inflater.inflate(R.layout.dialog_pass_key, null);
        editText = (EditText) dialogView.findViewById(R.id.pass_key_dialog_pass);

        getPassKey = String.valueOf(getArguments().getInt("PASS_KEY"));
        getSenderType = getArguments().getInt("SENDERTYPE");

        builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        builder.setView(dialogView)
                .setTitle(getString(R.string.key_dialog_message))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        inputPassKey = editText.getText().toString();

                        if (inputPassKey.equals(getPassKey)) {
                            // 合言葉が一致していた
                            // パスワードの変更画面へ遷移
                            if (getSenderType == 1) {
                                Intent intent = new Intent(getContext(), PassUpdateActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                confUserInfoFragment = new ConfUserInfoFragment();

                                ft = getActivity().getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                // レイアウトをfragmentに置き換え（追加）
                                ft.replace(R.id.fragment_container, confUserInfoFragment);
                                // 置き換えのトランザクションをバックスタックに保存する
                                ft.addToBackStack(null);
                                // フラグメントトランザクションをコミット
                                ft.commit();
                            }

                        } else {
                            resultPassKeyDialogFragment = new ResultPassKeyDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("PASS_KEY2", Integer.parseInt(getPassKey));
                            bundle.putInt("SENDERTYPE2", getSenderType);
                            resultPassKeyDialogFragment.setArguments(bundle);
                            resultPassKeyDialogFragment.show(getActivity().getSupportFragmentManager(), "result_dialog");
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

}