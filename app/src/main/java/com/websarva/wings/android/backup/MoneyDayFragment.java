package com.websarva.wings.android.backup;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;

// 支出画面のメイン
public class MoneyDayFragment extends Fragment {

    // フィールド
    private String currentYear, currentMonth, sumDatePrice;
    private int sumMonth = 0;
    private Calendar currentCalendar;
    private SimpleDateFormat sdf;
    private TextView dateTextView, sumTextView;
    private Button nextBt, backBt;
    private MoneyDayLVFragment dayLVFragment;
    private FragmentTransaction transaction;
    private Bundle bundle;
    private DatabaseOpenHelper databaseOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
//    private Arc arc;
//    private int endAngle = 0;
//    private int animationPeriod = 2000;
//    private  boolean showCanvas;

    public MoneyDayFragment() {
    }

    public static MoneyDayFragment newInstance() {
        MoneyDayFragment fragment = new MoneyDayFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_money_day, container, false);

        dateTextView = (TextView)view.findViewById(R.id.money_day_tv);
        //sumTextView = (TextView)view.findViewById(R.id.money_day_sum_tv);
        nextBt = (Button)view.findViewById(R.id.money_day_next_bt);
        backBt = (Button) view.findViewById(R.id.money_day_back_bt);
        //arc = (Arc) view.findViewById(R.id.arc);
        currentCalendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy/MM");


        dateTextView.setText(sdf.format(currentCalendar.getTime()));
        currentYear = String.format("%d", currentCalendar.get(Calendar.YEAR));
        currentMonth = String.format("%02d", currentCalendar.get(Calendar.MONTH) + 1);

        readSumDate();


        //sumTextView.setText(sumDatePrice);
        // ListViewのフラグメントセット
        setFragment();

        // 次へボタンが押された時の処理
        nextBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCalendar.add(Calendar.MONTH, 1);
                currentYear = String.format("%d", currentCalendar.get(Calendar.YEAR));
                currentMonth = String.format("%02d", currentCalendar.get(Calendar.MONTH) + 1);
                dateTextView.setText(sdf.format(currentCalendar.getTime()));
                readSumDate();
                //sumTextView.setText(sumDatePrice);
                setFragment();
            }
        });

        // 戻るボタンが押された時の処理
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCalendar.add(Calendar.MONTH, -1);
                currentYear = String.format("%d", currentCalendar.get(Calendar.YEAR));
                currentMonth = String.format("%02d", currentCalendar.get(Calendar.MONTH)  + 1);
                dateTextView.setText(sdf.format(currentCalendar.getTime()));
                readSumDate();
                //sumTextView.setText(sumDatePrice);
                setFragment();
            }
        });

        return view;
    }

    // フラグメントセット
    public void setFragment() {
        dayLVFragment = new MoneyDayLVFragment();
        // フラグメントトランザクションの開始
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        // レイアウトをfragmentに置き換え（追加）
        transaction.replace(R.id.money_day_fragment_container, dayLVFragment);
        // 置き換えのトランザクションをバックスタックに保存する
        transaction.addToBackStack(null);
        // フラグメント遷移時の変数受け渡し処理
        bundle = new Bundle();
        bundle.putString("CURRENT_MONTH", currentMonth);
        bundle.putString("CURRENT_YEAR", currentYear);
        bundle.putInt("SUM_PRICE", sumMonth);
        bundle.putString("SUM_DATE_PRICE", sumDatePrice);
        dayLVFragment.setArguments(bundle);
        // フラグメントトランザクションをコミット
        transaction.commit();
    }

    private void readSumDate() {
        if (databaseOpenHelper == null) {
            databaseOpenHelper = new DatabaseOpenHelper(getActivity());
        }

        if(sqLiteDatabase == null){
            sqLiteDatabase = databaseOpenHelper.getReadableDatabase();
        }
        int startDay = Integer.parseInt(currentYear + currentMonth) * 100;
        int lastDay = startDay + 100;

        String sql = "select sum(foodPrice) from foodinfo where buyDate between " + startDay + " and " + lastDay;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            sumDatePrice = integerChangeFormat(cursor.getInt(0));
            sumMonth = cursor.getInt(0);
        }
        cursor.close();
//        if (sumDatePrice.equals("0円")) {
//
//        }

    }

    private String integerChangeFormat(int price) {
        String changePrice;
        changePrice = String.format("￥%,d", price);
        return changePrice;
    }



}
