package com.websarva.wings.android.backup;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MoneyLimitConfFragment extends Fragment {

    // フィールド
    private TextInputLayout textInputLayout;
    private TextInputEditText textInputEditText;
    private Button updateBt;
    private String upperLimitText;
    private String currentLimitText;
    private DatabaseOpenHelper databaseOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
    private MoneyFragment moneyFragment;
    private FragmentTransaction ft;

    public MoneyLimitConfFragment() {
    }

    public static MoneyLimitConfFragment newInstance() {
        MoneyLimitConfFragment fragment = new MoneyLimitConfFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_money_limit_conf, container, false);

        textInputLayout = (TextInputLayout) view.findViewById(R.id.money_day_limit_til);
        textInputEditText = (TextInputEditText) view.findViewById(R.id.money_day_limit_edttext);
        updateBt = (Button) view.findViewById(R.id.money_limit_conf_bt);

        upperLimitText = readUpperLimitPriceData();
        currentLimitText = upperLimitText;
        textInputEditText.setText(upperLimitText);

        updateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upperLimitText = textInputEditText.getText().toString();

                if (upperLimitText.equals("") || upperLimitText.length() < 4 || 7 < upperLimitText.length()) {
                    textInputLayout.setError(getString(R.string.error_message10));
                } else if (currentLimitText.equals(upperLimitText)) {
                    textInputLayout.setError(getString(R.string.error_message11));

                } else {
                    updateData(Integer.parseInt(upperLimitText));
                    moneyFragment = new MoneyFragment();
                    ft = getActivity().getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.replace(R.id.fragment_container, moneyFragment).commit();

                }

            }
        });

        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // テキストが修正する直前に行う処理
                // エラー表示内容をリセット
                textInputLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private String readUpperLimitPriceData() {
        int getUpperLimitPrice = 0;

        if (databaseOpenHelper == null) {
            databaseOpenHelper = new DatabaseOpenHelper(getActivity());
        }

        if(sqLiteDatabase == null){
            sqLiteDatabase = databaseOpenHelper.getReadableDatabase();
        }
        String sql = "select upperLimitPrice from userinfo";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            getUpperLimitPrice = cursor.getInt(0);
        }
        cursor.close();
        return String.valueOf(getUpperLimitPrice);
    }

    private void updateData(int i) {
        if (databaseOpenHelper == null) {
            databaseOpenHelper = new DatabaseOpenHelper(getActivity());
        }
        if (sqLiteDatabase == null) {
            sqLiteDatabase = databaseOpenHelper.getWritableDatabase();
        }

        ContentValues values = new ContentValues();
        values.put("upperLimitPrice", i);

        sqLiteDatabase.update("userinfo", values, "_id=1", null);
    }

}
