package com.websarva.wings.android.backup;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;

public class MoneyDayLVFragment extends Fragment {

    // フィールド
    private String getYear, getMonth;
    private int currentDate;
    private String getSumDatePrice;
    private ListView sumPriceListView;
    private TextView sumTextView, alertTextView;
    private ArrayList<SumPriceinfo> sumPriceinfoList;
    private List2Adapter priceAdapter;
    private DatabaseOpenHelper databaseOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Calendar calendarRange;
    private int sumMonth;
    private int getUpperLimitPrice = 0;
    private Arc arc;
    private int endAngle = 0;
    private int animationPeriod = 2000;

    public MoneyDayLVFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_money_day_lv, container, false);

        // 前フラグメントからの変数を受け取る
        getSumDatePrice = getArguments().getString("SUM_DATE_PRICE");
        sumMonth = getArguments().getInt("SUM_PRICE");
        getYear = getArguments().getString("CURRENT_YEAR");
        getMonth = getArguments().getString("CURRENT_MONTH");
        currentDate = Integer.parseInt(getYear + getMonth) * 100;

        sumPriceListView = (ListView) view.findViewById(R.id.money_day_lv);
        sumTextView = (TextView)view.findViewById(R.id.money_month_sum_tv);
        alertTextView = (TextView) view.findViewById(R.id.money_month_txt2_tv);
        arc = (Arc) view.findViewById(R.id.arc);


        calendarRange = Calendar.getInstance();
        calendarRange.set(Calendar.YEAR, Integer.parseInt(getYear));
        calendarRange.set(Calendar.MONTH, Integer.parseInt(getMonth) - 1);

        // リストの初期化
        sumPriceinfoList = new ArrayList<>();
        priceAdapter = new List2Adapter(getContext());

        sumTextView.setText(getSumDatePrice);
        readSumDate();
        drawArc();

        priceAdapter.setFoodList(sumPriceinfoList);
        sumPriceListView.setAdapter(priceAdapter);

        return view;
    }

    private void readSumDate() {
        if (databaseOpenHelper == null) {
            databaseOpenHelper = new DatabaseOpenHelper(getActivity());
        }

        if(sqLiteDatabase == null){
            sqLiteDatabase = databaseOpenHelper.getReadableDatabase();
        }
        int lastDay = calendarRange.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int dayCounter = 1; dayCounter <= lastDay; dayCounter++) {
            currentDate += 1;
            String sql = "select sum(foodPrice) from foodinfo where buyDate = " + currentDate;
            Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                SumPriceinfo sumPriceinfo = new SumPriceinfo();
                sumPriceinfo.setDay(dayCounter + getString(R.string.money_lv_text1));
                sumPriceinfo.setSumPrice(integerChangeFormat(cursor.getInt(0)));
                sumPriceinfoList.add(sumPriceinfo);
            }
            cursor.close();
        }
    }

    private void readLimitDate() {
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
    }

    private String integerChangeFormat(int price) {
        String changePrice;
        changePrice = String.format("￥%,d", price);
        return changePrice;
    }

    private void drawArc() {
        readLimitDate();

        // 角度を合わせる
        if (getUpperLimitPrice <= sumMonth) {
            endAngle = 360;
        } else {
            double div = (double)sumMonth / getUpperLimitPrice;
            endAngle = (int) (div * 360);
        }

        AnimationArc animation = new AnimationArc(arc, endAngle);
        // アニメーションの起動期間を設定
        animation.setDuration(animationPeriod);
        arc.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (360 <= endAngle) {
                    arc.setColor();
                    alertTextView.setVisibility(View.VISIBLE);
                    sumTextView.setTextColor(Color.RED);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
