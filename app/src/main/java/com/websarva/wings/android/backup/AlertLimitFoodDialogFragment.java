package com.websarva.wings.android.backup;

import android.app.AlertDialog;
        import android.app.Dialog;
        import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
        import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

// アップグレードするかどうかのダイアログ
public class AlertLimitFoodDialogFragment extends DialogFragment {

    // フィールド
    public AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private DatabaseOpenHelper databaseOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
    private View dialogView;
    private Calendar c;
    private int count;
    private SimpleDateFormat sdf;
    private int currentDate;
    private CheckBox checkBox;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        inflater = LayoutInflater.from(this.getContext());
        dialogView = inflater.inflate(R.layout.dialog_alert_limit_food, null);
        checkBox = (CheckBox) dialogView.findViewById(R.id.alert_limit_food_checkbox);
        sdf = new SimpleDateFormat("yyyyMMdd");

        c = Calendar.getInstance();
        currentDate = Integer.parseInt(sdf.format(c.getTime()));

        count = countData();

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked() == true) {
                    HomeActivity.isShowAlert = false;
                } else {
                    HomeActivity.isShowAlert = true;
                }
            }
        });

        //ダイアログの作成処理
        builder.setView(dialogView)
                .setTitle(getString(R.string.alert_message1))
                .setMessage(getString(R.string.alert_message4) + count + getString(R.string.alert_message5))
                .setPositiveButton(getString(R.string.alert_message2), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        return builder.create();
    }

    private int countData(){
        int count = 0;
        if (databaseOpenHelper == null) {
            databaseOpenHelper = new DatabaseOpenHelper(getActivity());
        }

        if(sqLiteDatabase == null){
            sqLiteDatabase = databaseOpenHelper.getReadableDatabase();
        }

        String sql = "select * from foodinfo where dueDate < " + currentDate;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            count = cursor.getCount();
        }
        cursor.close();
        return count;
    }
}