package com.websarva.wings.android.backup;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

// 入力フラグメント
public class InputFragment extends Fragment {

    // フィールド
    private TextInputLayout foodNameTextInputLayout, moneyTextInputLayout;
    private TextInputEditText foodNameTextInputEditText, moneyTextInputEditText;
    private EditText setBuyDayText, setLimitDayText;
    private String getFoodName, getFoodPrice;
    int getBuyDate, getLimitDate, upperLimitPrice;
    private Boolean isEntry = true;
    private Button entryBt;
    private Calendar nowCalender, setCalender;
    private DatePickerDialog datePickerDialog;
    private DatabaseOpenHelper databaseOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
    private int currentDate, sumFoodPrice;

    private SimpleDateFormat sdf, sdf2;


    public InputFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input, container, false);

        foodNameTextInputLayout = (TextInputLayout) view.findViewById(R.id.input_name_layout);
        moneyTextInputLayout = (TextInputLayout) view.findViewById(R.id.input_money_layout);
        foodNameTextInputEditText = (TextInputEditText)view.findViewById(R.id.input_name_et);
        moneyTextInputEditText = (TextInputEditText) view.findViewById(R.id.input_money_et);
        setBuyDayText = (EditText) view.findViewById(R.id.input_buy_day_et);
        setLimitDayText = (EditText) view.findViewById(R.id.input_limit_day_et);
        entryBt = (Button) view.findViewById(R.id.input_entry_bt);
        nowCalender = Calendar.getInstance();

        sdf = new SimpleDateFormat("yyyyMMdd");
        sdf2 = new SimpleDateFormat("yyyyMM");
        upperLimitPrice = readUpperLimitPriceData();

        foodNameTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // テキストが修正する直前に行う処理
                // エラー表示内容をリセット
                foodNameTextInputLayout.setError(null);
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
                moneyTextInputLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 現在の日付をEditTextにセット
        setBuyDayText.setText(getString(R.string.input_text1) + String.format("%d/%02d/%02d", nowCalender.get(Calendar.YEAR), nowCalender.get(Calendar.MONTH) + 1, nowCalender.get(Calendar.DAY_OF_MONTH)));
        getBuyDate = Integer.parseInt(sdf.format(nowCalender.getTime()));

       // 購入日付のEditTextが押された時にダイアログ表示メソッドを呼び出す
        setBuyDayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBuyOrLimitCalender(1);
            }
        });

        nowCalender.add(Calendar.DAY_OF_MONTH, 3); // 現在の日付の3日後の日付を取得
        // 期限をEditTextにセット
        setLimitDayText.setText(getString(R.string.input_text2) + String.format("%d/%02d/%02d", nowCalender.get(Calendar.YEAR), nowCalender.get(Calendar.MONTH) + 1, nowCalender.get(Calendar.DAY_OF_MONTH)));
        getLimitDate = Integer.parseInt(sdf.format(nowCalender.getTime()));
        setLimitDayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBuyOrLimitCalender(2);
            }
        });

        entryBt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
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

                    insertData(getFoodName, getBuyDate, getLimitDate, Integer.parseInt(getFoodPrice));
                    foodNameTextInputEditText.setText("");
                    moneyTextInputEditText.setText("");

                    sumFoodPrice = readSumDate(getBuyDate);
                    if (upperLimitPrice <= sumFoodPrice) {
                        // 通知チャンネルの生成
                        createNotificationChannel();

                        // 通知の送信
                        sendNotification();
                    }
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
                        nowCalender.set(Calendar.YEAR, year);
                        nowCalender.set(Calendar.MONTH, month);
                        nowCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        if (type == 1) {
                            // setした日付を取得して表示
                            getBuyDate = Integer.parseInt(sdf.format(nowCalender.getTime()));
                            //getBuyDate = sdf.format(nowCalender.getTime());
                            setBuyDayText.setText(getString(R.string.input_text1) + String.format("%d/%02d/%02d", year, month+1, dayOfMonth));

                        } else if (type == 2) {
                            // setした日付を取得して表示
                            getLimitDate = Integer.parseInt(sdf.format(nowCalender.getTime()));
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

    private void insertData(String foodName, int buyDate, int dueDate, int foodPrice){
        if (databaseOpenHelper == null) {
            databaseOpenHelper = new DatabaseOpenHelper(getActivity());
        }
        if (sqLiteDatabase == null) {
            sqLiteDatabase = databaseOpenHelper.getWritableDatabase();
        }
        ContentValues values = new ContentValues();
        values.put("foodName", foodName);
        values.put("buyDate", buyDate);
        values.put("dueDate", dueDate);
        values.put("foodPrice", foodPrice);

        sqLiteDatabase.insert("foodinfo", null, values);
    }

    private int readUpperLimitPriceData() {
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
        return getUpperLimitPrice;
    }

    private int readSumDate(int buyDate) {
        int sum = 0;

        if (databaseOpenHelper == null) {
            databaseOpenHelper = new DatabaseOpenHelper(getActivity());
        }

        if(sqLiteDatabase == null){
            sqLiteDatabase = databaseOpenHelper.getReadableDatabase();
        }
        currentDate = getBuyDate / 100;
        currentDate = currentDate * 100;

        int startMonth = currentDate;
        int endMonth = startMonth + 100;

        String sql = "select sum(foodPrice) from foodinfo where buyDate between " + startMonth + " and " + endMonth;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            sum = cursor.getInt(0);
        }
        cursor.close();

        return sum;
    }



    // 通知チャンネルの生成
    private void createNotificationChannel() {
        // 通知チャンネルの生成
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "amount"; // 通知チャンネルのIDにする任意の文字列
            String name = "上限金額通知"; // 通知チャンネル名
            int importance = NotificationManager.IMPORTANCE_HIGH; // デフォルトの重要度
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);


            // 通知チャンネルの設定のデフォルト値。設定必須ではなく、ユーザーが変更可能。
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC); // ロック画面で通知を表示するかどうか
            channel.enableVibration(true);
            channel.enableLights(true);
            //channel.setLightColor(Color.GREEN); //通知ライト
            //channel.setSound(uri, audioAttributes); //通知音
            // システムに通知チャンネルを登録
            // NotificationManagerCompatにcreateNotificationChannel()は無い。
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // 通知の送信
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void sendNotification() {
        String message = iToString(getBuyDate/100);
        // 通知の生成
        Notification builder = new NotificationCompat.Builder(getContext(), "amount")
                .setSmallIcon(R.drawable.ic_launcher_foreground) // 小アイコン
                .setContentTitle("Change Your Life") // タイトル
                .setContentText(getString(R.string.notifi_text1) + "(" + message + ")") // テキスト
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        // 通知の送信
        NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder); // 通知ID (更新、削除で利用)
    }

    private String iToString(int i){
        String str = String.valueOf(i);
        String yyyy = str.substring(0, 4);
        String MM = str.substring(4, 6);
        str =  yyyy + "/" + MM + "/";
        return str;
    }

}

