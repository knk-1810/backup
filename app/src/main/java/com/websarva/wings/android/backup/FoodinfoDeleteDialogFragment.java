package com.websarva.wings.android.backup;

import android.app.AlertDialog;
        import android.app.Dialog;
        import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

public class FoodinfoDeleteDialogFragment extends DialogFragment {

    // フィールド
    private AlertDialog.Builder builder;
    private FragmentTransaction langTransaction;
    private int foodinfoId;
    public DatabaseOpenHelper databaseOpenHelper;
    public SQLiteDatabase sqLiteDatabase;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        foodinfoId = getArguments().getInt("DELETEFOODINFO");

        builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);

        //ダイアログの作成処理
        builder.setTitle(getString(R.string.delete_dialog_message))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // "はい"を押した時の処理内容

                        String deleteSql = "delete from foodinfo where _id = " + foodinfoId;
                        if (databaseOpenHelper == null) {
                            databaseOpenHelper = new DatabaseOpenHelper(getActivity());
                        }

                        if(sqLiteDatabase == null){
                            sqLiteDatabase = databaseOpenHelper.getReadableDatabase();
                        }
                        sqLiteDatabase.execSQL(deleteSql);

                        ListFragment selectFragment = new ListFragment();
                        FragmentTransaction ft;
                        ft = getActivity().getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.replace(R.id.fragment_container, selectFragment).commit();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null);
        return builder.create();
    }
}