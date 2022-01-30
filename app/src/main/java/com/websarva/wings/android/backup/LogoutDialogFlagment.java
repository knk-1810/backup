package com.websarva.wings.android.backup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

// ログアウトのダイアログ
public class LogoutDialogFlagment extends DialogFragment {

    // フィールド
    private AlertDialog.Builder builder;
    private Intent intent;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);

        // ダイアログの作成処理
        builder.setTitle(getString(R.string.logout_dialog_message1))
                .setMessage(getString(R.string.logout_dialog_message2))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // "はい"を押した時の処理内容
                        intent = new Intent(getContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null);
        return builder.create();
    }
}
