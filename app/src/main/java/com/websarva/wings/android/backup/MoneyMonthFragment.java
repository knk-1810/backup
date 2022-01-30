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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// グラフ表示
public class MoneyMonthFragment extends Fragment {

    // フィールド
    private int currentYear;
    private int maxSumMonth = 0, minSumMonth = 0;
    private Calendar currentCalendar;
    private TextView monthTextView, sumTextView;
    private Button nextBt, backBt;
    private SimpleDateFormat sdf, sdf2;
    protected BarChart chart;
    private BarData data;
    private YAxis left, right;
    private XAxis xAxis, bottomAxis;
    private DatabaseOpenHelper databaseOpenHelper;
    private SQLiteDatabase sqLiteDatabase;

    // X軸に表示するLabelのリスト(最初の""は原点の位置)
    private String[] labels = {};

    public MoneyMonthFragment() {
    }

    public static MoneyMonthFragment newInstance() {
        MoneyMonthFragment fragment = new MoneyMonthFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        labels = getResources().getStringArray(R.array.money_month_array);
        return inflater.inflate(R.layout.fragment_money_month, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        monthTextView = (TextView) view.findViewById(R.id.money_month_tv);
        sumTextView = (TextView) view.findViewById(R.id.money_month_sum_tv);
        backBt = (Button)view.findViewById(R.id.money_month_back_bt);
        nextBt = (Button) view.findViewById(R.id.money_month_next_bt);
        chart = (BarChart)view.findViewById(R.id.money_month_chart);

        sdf = new SimpleDateFormat("yyyyMM");
        sdf2 = new SimpleDateFormat("yyyy");

        currentCalendar = Calendar.getInstance();
        monthTextView.setText(sdf2.format(currentCalendar.getTime()));
        currentYear = currentCalendar.get(Calendar.YEAR);

        sumYearDate();
        setBarDate();

        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextBt.setVisibility(View.VISIBLE);
                maxSumMonth = 0;
                minSumMonth = 0;
                currentCalendar.add(Calendar.YEAR, -1);
                currentYear = currentCalendar.get(Calendar.YEAR);
                monthTextView.setText(sdf2.format(currentCalendar.getTime()));
                sumYearDate();
                setBarDate();

                if (currentYear == 2020) {
                    backBt.setVisibility(View.INVISIBLE);
                }

            }
        });

        nextBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backBt.setVisibility(View.VISIBLE);
                maxSumMonth = 0;
                minSumMonth = 0;
                currentCalendar.add(Calendar.YEAR, 1);
                currentYear = currentCalendar.get(Calendar.YEAR);
                monthTextView.setText(sdf2.format(currentCalendar.getTime()));
                sumYearDate();
                setBarDate();

                if (currentYear == 2025) {
                    nextBt.setVisibility(View.INVISIBLE);
                }

            }
        });

    }

    private void setBarDate() {
        // 表示データ取得
        data = new BarData(getBarData());
        chart.setData(data);

        // Y軸(左)
        left = chart.getAxisLeft();
        if (minSumMonth < 2000) {
            left.setAxisMinimum(0);
        } else {
            left.setAxisMinimum(minSumMonth - 1000);
        }

        left.setAxisMaximum(maxSumMonth + 1000);

        left.setDrawTopYLabelEntry(true);



        // Y軸(右)
        right = chart.getAxisRight();
        right.setDrawLabels(false);
        right.setDrawGridLines(false);
        right.setDrawZeroLine(true);
        right.setDrawTopYLabelEntry(true);

        // X軸
        xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        bottomAxis = chart.getXAxis();
        bottomAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        bottomAxis.setDrawLabels(true);
        bottomAxis.setDrawGridLines(false);
        bottomAxis.setDrawAxisLine(true);

        // グラフ上の表示
        // chart.setDrawValueAboveBar(true);
        chart.getDescription().setEnabled(false);

        // 一度に表示する数を5として設定、スクロールで12まで表示できる
        chart.setVisibleXRangeMaximum(6);
        chart.moveViewToX(12);

        // 凡例
        chart.getLegend().setEnabled(false);

        chart.setScaleEnabled(false);
        // アニメーション
        // chart.animateY(1200, Easing.EasingOption.Linear);
    }

    // 棒グラフのデータを取得
    private List<IBarDataSet> getBarData(){
        // 表示させるデータ
        ArrayList<BarEntry> entries = new ArrayList<>();
        readSumMonth(entries);

        List<IBarDataSet> bars = new ArrayList<>();
        BarDataSet dataSet = new BarDataSet(entries, "bar");

        // ハイライトさせない
        dataSet.setHighlightEnabled(false);

        // Barの色をセット
        // dataSet.setColors(new int[]{R.color.material_blue, R.color.material_green, R.color.material_yellow}, this);
        bars.add(dataSet);

        return bars;
    }

    private void readSumMonth(ArrayList<BarEntry> entries) {
        if (databaseOpenHelper == null) {
            databaseOpenHelper = new DatabaseOpenHelper(getActivity());
        }

        if(sqLiteDatabase == null){
            sqLiteDatabase = databaseOpenHelper.getReadableDatabase();
        }
        currentCalendar.set(Calendar.MONTH, 0);
        int getSumPrice[] = new int[12];
        String sql;
        for (int monthCounter = 1; monthCounter < 13; monthCounter++) {
            int startMonth = Integer.parseInt(sdf.format(currentCalendar.getTime())) * 100;
            int endMonth = startMonth + 100;
            sql = "select sum(foodPrice) from foodinfo where buyDate between " + startMonth + " and " + endMonth;
            Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
            while (cursor.moveToNext()) {

                entries.add(new BarEntry(monthCounter, cursor.getInt(0)));
                getSumPrice[monthCounter - 1] = cursor.getInt(0);
            }
            cursor.close();

            if (monthCounter != 12) {
                currentCalendar.add(Calendar.MONTH, 1);
            }
        }
        maxSumMonth = getSumPrice[0];
        minSumMonth = getSumPrice[0];
        for (int i = 1; i < 12; i++) {
            if (maxSumMonth <= getSumPrice[i]) {
                maxSumMonth = getSumPrice[i];
            } else if (getSumPrice[i] < minSumMonth) {
                minSumMonth = getSumPrice[i];
            }
        }
    }

    private void sumYearDate() {
        if (databaseOpenHelper == null) {
            databaseOpenHelper = new DatabaseOpenHelper(getActivity());
        }

        if(sqLiteDatabase == null){
            sqLiteDatabase = databaseOpenHelper.getReadableDatabase();
        }
        int startYear = currentYear * 10000;
        int endYear = currentYear * 10000 + 10000;

        String sql;
        sql = "select sum(foodPrice) from foodinfo where buyDate between " + startYear + " and " + endYear;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            sumTextView.setText(integerChangeFormat(cursor.getInt(0)));
        }
        cursor.close();

    }

    private String integerChangeFormat(int price) {
        String changePrice;
        changePrice = String.format("￥%,d", price);
        return changePrice;
    }
}
