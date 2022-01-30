package com.websarva.wings.android.backup;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Locale;

// 設定画面の言語変更のフラグメント生成クラス
public class ConfLangFragment extends Fragment {

    // フィールド
    private CheckBox japaneseCB, englishCB;
    private Button backBt, refBt;
    private FragmentManager fm;
    private DatabaseOpenHelper databaseOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
    private int langType;
    private int currentType;

    public ConfLangFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conf_lang, container, false);

        japaneseCB = (CheckBox) view.findViewById(R.id.conf_lang_japanese_cb);
        englishCB = (CheckBox) view.findViewById(R.id.conf_lang_english_cb);
        backBt = (Button) view.findViewById(R.id.conf_lang_back_bt);
        refBt = (Button) view.findViewById(R.id.conf_lang_reflect_bt);

        currentType = languageData();
        langType = currentType;
        //refBt.setEnabled(false);

        if (langType == 1) {
            japaneseCB.setChecked(true);
            englishCB.setChecked(false);
            setLocale("ja");
        } else {
            japaneseCB.setChecked(false); // 日本語のチェックをはずす
            englishCB.setChecked(true); // 英語にチェックを入れる
            setLocale("en");
        }

        // 日本語が選択された時の処理
        japaneseCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                japaneseCB.setChecked(true); // 日本語にチェックを入れる
                englishCB.setChecked(false); // 英語のチェックをはずす
                setLocale("ja");
                langType = 1;
//                if (langType != currentType) {
//                    refBt.setEnabled(true);
//                }
            }
        });

        // 英語が選択された時の処理
        englishCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                japaneseCB.setChecked(false); // 日本語のチェックをはずす
                englishCB.setChecked(true); // 英語にチェックを入れる
                setLocale("en");
                langType = 2;
//                if (langType != currentType) {
//                    refBt.setEnabled(true);
//                }
            }
        });

        // 戻るボタンが押された時に前の画面へ戻る
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });

        refBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLangData(langType);
                Intent intent = new Intent(getContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        return view;
    }

    public void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, null);
    }

    private int languageData() {
        int type = 1;
        if (databaseOpenHelper == null) {
            databaseOpenHelper = new DatabaseOpenHelper(getActivity());
        }
        if (sqLiteDatabase == null) {
            sqLiteDatabase = databaseOpenHelper.getWritableDatabase();
        }

        String sql = "select userLanguage from userinfo";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            type = cursor.getInt(0);
        }
        cursor.close();
        return type;

    }

    private void setLangData(int i) {
        if (databaseOpenHelper == null) {
            databaseOpenHelper = new DatabaseOpenHelper(getActivity());
        }
        if (sqLiteDatabase == null) {
            sqLiteDatabase = databaseOpenHelper.getWritableDatabase();
        }

        ContentValues values = new ContentValues();
        values.put("userLanguage", i);

        sqLiteDatabase.update("userinfo", values, "_id=1", null);
    }
}
