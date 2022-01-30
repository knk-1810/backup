package com.websarva.wings.android.backup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

// パスワードが間違えている場合のダイアログ
public class ResultPassDialogFragment extends DialogFragment {

    // フィールド
    private String[] listItems = {};
    private AlertDialog.Builder builder;
    private InputPassDialogFragment inputPassDialogFragment;
    private ConfPassAskFragment confPassAskFragment;
    private FragmentTransaction langTransaction;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        listItems = getResources().getStringArray(R.array.result_dialog_array);

        builder = new AlertDialog.Builder(getActivity());
        // ダイアログの作成処理
        builder.setTitle(getString(R.string.result_dialog_message))
                .setItems(listItems, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 項目名をクリックしたときの処理
                        switch (id) {
                            case 0:
                                // 再試行を選択した時、パスワード入力ダイアログを再び表示
                                inputPassDialogFragment = new InputPassDialogFragment();
                                inputPassDialogFragment.show(getActivity().getSupportFragmentManager(), "userinfo_dialog");
                                break;

                            case 1:
                                // パスワードを忘れた場合を選択した場合、パスワード問い合わせのフラグメントをセット
                                confPassAskFragment = new ConfPassAskFragment();
                                // フラグメントトランザクションの開始
                                langTransaction = getActivity().getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                // レイアウトをfragmentに置き換え（追加）
                                langTransaction.replace(R.id.fragment_container, confPassAskFragment);
                                // 置き換えのトランザクションをバックスタックに保存する
                                langTransaction.addToBackStack(null);
                                // フラグメントトランザクションをコミット
                                langTransaction.commit();
                                break;

                            case 2:
                                // キャンセル処理、なにもしない
                                break;

                        }
                    }
                });
        return builder.create();
    }
}


