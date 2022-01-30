package com.websarva.wings.android.backup;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

// 入力内容更新フラグメント
public class ListUpdateFragment extends Fragment {

    // フィールド
    private TextView resultTv;
    private TextInputLayout foodNameTextInputLayout, moneyTextInputLayout;
    private TextInputEditText foodNameTextInputEditText, moneyTextInputEditText;
    private EditText setBuyDayText, setLimitDayText;
    private String getFoodName, getFoodPrice;
    int getBuyDate, getLimitDate;
    private Boolean isEntry = true;
    private Button updateBt, backBt;
    private Calendar nowCalender, setCalender;
    private DatePickerDialog datePickerDialog;
    public DatabaseOpenHelper databaseOpenHelper;
    public SQLiteDatabase sqLiteDatabase;
    private FragmentManager fm;

    private int foodinfoId;

    private SimpleDateFormat sdf;


    public ListUpdateFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_update, container, false);

        resultTv = (TextView) view.findViewById(R.id.list_update_result_tv);
        foodNameTextInputLayout = (TextInputLayout) view.findViewById(R.id.list_update_name_layout);
        moneyTextInputLayout = (TextInputLayout) view.findViewById(R.id.list_update_money_layout);
        foodNameTextInputEditText = (TextInputEditText)view.findViewById(R.id.list_update_name_et);
        moneyTextInputEditText = (TextInputEditText) view.findViewById(R.id.list_update_money_et);
        setBuyDayText = (EditText) view.findViewById(R.id.list_update_buy_day_et);
        setLimitDayText = (EditText) view.findViewById(R.id.list_update_limit_day_et);
        updateBt = (Button) view.findViewById(R.id.list_update_entry_bt);
        backBt = (Button) view.findViewById(R.id.list_update_back_bt);
        nowCalender = Calendar.getInstance();

        foodinfoId = getArguments().getInt("UPDATEFOODINFO");

        sdf = new SimpleDateFormat("yyyyMMdd");

        setDateToEditText();

        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });

        foodNameTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // テキストが修正する直前に行う処理
                // エラー表示内容をリセット
                foodNameTextInputLayout.setError(null);
                resultTv.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        moneyTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // テキストが修正する直前に行う処理
                // エラー表示内容をリセット
                moneyTextInputLayout.setError(null);
                resultTv.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setBuyDayText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                resultTv.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setLimitDayText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                resultTv.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // 購入日付のEditTextが押された時にダイアログ表示メソッドを呼び出す
        setBuyDayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBuyOrLimitCalender(1);
            }
        });


        setLimitDayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBuyOrLimitCalender(2);
            }
        });

        updateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEntry = true;

                getFoodName = foodNameTextInputEditText.getText().toString();
                getFoodPrice = moneyTextInputEditText.getText().toString();

                if (getFoodName.equals("")) {
                    foodNameTextInputLayout.setError(getString(R.string.error_message7));
                    isEntry = false;
                }
                if (getFoodPrice.equals("")) {
                    moneyTextInputLayout.setError(getString(R.string.error_message8));
                    isEntry = false;
                }

                if (isEntry == true) {
                    if (databaseOpenHelper == null) {
                        databaseOpenHelper = new DatabaseOpenHelper(getActivity());
                    }
                    if (sqLiteDatabase == null) {
                        sqLiteDatabase = databaseOpenHelper.getWritableDatabase();
                    }

                    updateDate(getFoodName, getBuyDate, getLimitDate, Integer.parseInt(getFoodPrice));
                    foodNameTextInputEditText.setText("");
                    moneyTextInputEditText.setText("");
                    resultTv.setVisibility(View.VISIBLE);
                }

            }
        });

        return view;
    }

    //カレンダーのダイアログを表示
    public void setBuyOrLimitCalender(int type) {
        // type: 購入日付か期限か 1:購入日付, 2:期限

        //Calendarインスタンスを取得
        setCalender = Calendar.getInstance();

        //DatePickerDialogインスタンスを取得
        datePickerDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //nowCalender.set(Calendar.YEAR, year);
                        //nowCalender.set(Calendar.MONTH, month);
                        //nowCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        if (type == 1) {
                            // setした日付を取得して表示
                            getBuyDate = year * 10000 + month * 100  + dayOfMonth + 100;
                            //getBuyDate = Integer.parseInt(sdf.format(nowCalender.getTime()));
                            //getBuyDate = sdf.format(nowCalender.getTime());
                            setBuyDayText.setText(getString(R.string.input_text1) + String.format("%d/%02d/%02d", year, month+1, dayOfMonth));

                        } else if (type == 2) {
                            // setした日付を取得して表示
                            getLimitDate = year * 10000 + month * 100 + dayOfMonth + 100;
                            //getLimitDate = Integer.parseInt(sdf.format(nowCalender.getTime()));
                            //getLimitDate = sdf.format(nowCalender.getTime());
                            setLimitDayText.setText(getString(R.string.input_text2) + String.format("%d/%02d/%02d", year, month + 1, dayOfMonth));

                        }

                    }
                },
                setCalender.get(Calendar.YEAR),
                setCalender.get(Calendar.MONTH),
                setCalender.get(Calendar.DATE)
        );
        // ダイアログを表示
        datePickerDialog.show();
    }

    private void setDateToEditText() {
        if (databaseOpenHelper == null) {
            databaseOpenHelper = new DatabaseOpenHelper(getActivity());
        }

        if(sqLiteDatabase == null){
            sqLiteDatabase = databaseOpenHelper.getReadableDatabase();
        }
        String sql = "select * from foodinfo where _id = " + foodinfoId;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            foodNameTextInputEditText.setText(cursor.getString(1));
            moneyTextInputEditText.setText(String.valueOf(cursor.getString(4)));
            getBuyDate = cursor.getInt(2);
            getLimitDate = cursor.getInt(3);
        }
        cursor.close();
        setBuyDayText.setText(getString(R.string.input_text1) + iToString(getBuyDate));
        setLimitDayText.setText(getString(R.string.input_text2) + iToString(getLimitDate));
    }

    private void updateDate(String foodName, int buyDate, int dueDate, int foodPrice) {
        if (databaseOpenHelper == null) {
            databaseOpenHelper = new DatabaseOpenHelper(getActivity());
        }
        if (sqLiteDatabase == null) {
            sqLiteDatabase = databaseOpenHelper.getWritableDatabase();
        }
        String id = String.valueOf(foodinfoId);

        ContentValues values = new ContentValues();
        values.put("foodName", foodName);
        values.put("buyDate", buyDate);
        values.put("dueDate", dueDate);
        values.put("foodPrice", foodPrice);

        sqLiteDatabase.update("foodinfo", values, "_id=?", new String[]{id});
    }

    // 日付に区切り線を挿入, 整数型を文字列型に変換するメソッド
    private String iToString(int i){
        String str = String.valueOf(i);
        String yyyy = str.substring(0, 4);
        String MM = str.substring(4, 6);
        String dd = str.substring(6, 8);
        str = yyyy + "/" + MM + "/" + dd;
        return str;
    }
}
