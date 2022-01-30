package com.websarva.wings.android.backup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;


public class SelectFoodinfoDialogFragment extends DialogFragment {

    // フィールド
    private String[] listItems = {"", "", "", ""};
    private AlertDialog.Builder builder;
    private FragmentTransaction langTransaction;
    private int foodinfoId;
    private ListUpdateFragment listUpdateFragment;
    private FoodinfoDeleteDialogFragment foodinfoDeleteDialogFragment;
    private DatabaseOpenHelper databaseOpenHelper;
    private SQLiteDatabase sqLiteDatabase;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        listItems = getResources().getStringArray(R.array.select_foodinfo_dialog_array);

        foodinfoId = getArguments().getInt("FOODINFOID");

        builder = new AlertDialog.Builder(getActivity());
        // ダイアログの作成処理
        builder.setTitle(getString(R.string.select_foodinfo_dialog_message))
                .setItems(listItems, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 項目名をクリックしたときの処理
                        switch (id) {
                            case 0:
                                // 更新を選択

                                listUpdateFragment = new ListUpdateFragment();
                                // フラグメントトランザクションの開始
                                langTransaction = getActivity().getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                // レイアウトをfragmentに置き換え（追加）
                                langTransaction.replace(R.id.fragment_container, listUpdateFragment);

                                // 置き換えのトランザクションをバックスタックに保存する
                                langTransaction.addToBackStack(null);
                                Bundle bundle = new Bundle();
                                // 更新フラグメントにfoodinfoIdを渡す
                                bundle.putInt("UPDATEFOODINFO", foodinfoId);
                                listUpdateFragment.setArguments(bundle);
                                // フラグメントトランザクションをコミット
                                langTransaction.commit();
                                break;

                            case 1:
                                // 削除を選択
                                foodinfoDeleteDialogFragment = new FoodinfoDeleteDialogFragment();
                                Bundle bundle2 = new Bundle();
                                bundle2.putInt("DELETEFOODINFO", foodinfoId);
                                foodinfoDeleteDialogFragment.setArguments(bundle2);
                                foodinfoDeleteDialogFragment.show(getActivity().getSupportFragmentManager(), "foodinfo_dialog");
                                break;

                            case 2:
                                // 献立を選択
                                // foodinfoIdから食品の名前をデータベースから取り出す
                                String foodName = readFoodNameDate(foodinfoId);
                                String url = "https://cookpad.com/search/" + foodName;
                                Uri uri = Uri.parse(url);
                                Intent i = new Intent(Intent.ACTION_VIEW,uri);
                                startActivity(i);
                                break;

                            case 3:
                                // キャンセル処理、なにもしない
                                break;

                        }
                    }
                });
        return builder.create();
    }

    // 献立を検索したいidを受け取りデータベースから検索しString型で返すメソッド
    public String readFoodNameDate(int id) {
        String foodName = null;
        String sql = "select foodName from foodinfo where _id = " + id;
        Cursor cursor;

        if (databaseOpenHelper == null) {
            databaseOpenHelper = new DatabaseOpenHelper(getActivity());
        }

        if(sqLiteDatabase == null){
            sqLiteDatabase = databaseOpenHelper.getReadableDatabase();
        }

        cursor = sqLiteDatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            foodName = cursor.getString(0);
        }
        cursor.close();

        return foodName;
    }

}