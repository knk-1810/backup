package com.websarva.wings.android.backup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

// パスワードが間違えている場合のダイアログ
public class ResultPassKeyDialogFragment extends DialogFragment {

    // フィールド
    private String[] listItems = {};
    private AlertDialog.Builder builder;
    private KeyInputDialogFragment keyInputDialogFragment;
    private String getPassKey;
    private int getSenderType;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        listItems = getResources().getStringArray(R.array.result_key_dialog_array);

        getPassKey = String.valueOf(getArguments().getInt("PASS_KEY2"));
        getSenderType = getArguments().getInt("SENDERTYPE2");

        builder = new AlertDialog.Builder(getActivity());
        // ダイアログの作成処理
        builder.setTitle(getString(R.string.result_key_dialog_message))
                .setItems(listItems, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 項目名をクリックしたときの処理
                        switch (id) {
                            case 0:
                                // 再試行を選択した時、合言葉入力ダイアログを再び表示
                                keyInputDialogFragment = new KeyInputDialogFragment();
                                Bundle bundle = new Bundle();
                                bundle.putInt("PASS_KEY", Integer.parseInt(getPassKey));
                                bundle.putInt("SENDERTYPE", getSenderType);
                                keyInputDialogFragment.setArguments(bundle);
                                keyInputDialogFragment.show(getActivity().getSupportFragmentManager(), "keyinput_dialog");
                                break;

                            case 1:
                                // キャンセル処理、なにもしない
                                break;

                        }
                    }
                });
        return builder.create();
    }
}

