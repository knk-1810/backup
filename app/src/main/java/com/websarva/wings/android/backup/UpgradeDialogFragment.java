package com.websarva.wings.android.backup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

// アップグレードするかどうかのダイアログ
public class UpgradeDialogFragment extends DialogFragment {

    // フィールド
    public AlertDialog.Builder builder;
    private UpdateFlagTask task;
    private String getId;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        getId = getArguments().getString("GET_ID");
        builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);

        //ダイアログの作成処理
        builder.setTitle(getString(R.string.upgrade_dialog_message1))
                .setMessage(getString(R.string.upgrade_dialog_message2))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // "はい"を押した時の処理内容

                        /*
                        データベース接続
                        データベースの課金フラグをtrueにする
                         */
                        task = new UpdateFlagTask();
                        task.setListener(createListener());
                        task.execute(getId);

                    }
                })
                .setNegativeButton(getString(R.string.cancel), null);
        return builder.create();
    }

    private UpdateFlagTask.Listener createListener() {
        return new UpdateFlagTask.Listener() {
            @Override
            public void onSuccess(String result) {

            }
        };
    }
}
