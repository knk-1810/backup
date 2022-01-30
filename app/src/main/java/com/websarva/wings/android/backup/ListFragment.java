package com.websarva.wings.android.backup;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

//一覧フラグメント
public class ListFragment extends Fragment {

    // フィールド
    private Button searchBt, searchOptionBt, searchExecBt, searchResetBt;
    private EditText searchWord;
    private CheckBox searchExpiredCb;
    private DatabaseOpenHelper databaseOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
    private ListView foodinfoListView;
    private ArrayList<Foodinfo> foodinfoList;
    private ListAdapter listAdapter;
    private PopupWindow mPopupWindow;
    private RadioGroup radioGroup1, radioGroup2, radioGroup3;
    private RadioButton buyRadioButton1, buyRadioButton2, buyRadioButton3;
    private RadioButton limitRadioButton1, limitRadioButton2, limitRadioButton3;
    private RadioButton priceRadioButton1, priceRadioButton2, priceRadioButton3;

    // 下限上限の年のスピナに表示させるアイテム一覧
    private String[] lowerYearSpItems = {};
    private String[] upperYearSpItems = {};

    // 下限上限の月のスピナに表示させるアイテム一覧
    private String[] lowerMonthSpItems = {};
    private String[] upperMonthSpItems = {};

    private ArrayAdapter<String> lowerAdapter, upperAdapter;
    private ArrayAdapter<String> lowerMonthAdapter, upperMonthAdapter;

    private Spinner lowerYearSpinner, upperYearSpinner;
    private Spinner lowerMonthSpinner, upperMonthSpinner;

    // 検索範囲 between 0000000 and 999999 に設定
    private String getLowerYearItem = "0000", getUpperYearItem = "9999";
    private String getLowerMonthItem = "00", getUpperMonthItem = "99";

    private int yearDay, yearDay2; // select文のbetweenでの範囲
    private Boolean isBuySort = false;
    private Boolean isLimitSort = false;
    private Boolean isPriceSort = false;
    private String buySort = "", limitSort = "", priceSort = "";

    private String orderBy = "";
    private String like = "'%'";
    private int limitDate = 99999999;
    private View popupView;

    private AlertLimitFoodDialogFragment alertLimitFoodDialogFragment;


    public ListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        lowerYearSpItems = getResources().getStringArray(R.array.list_sp_array1);
        upperYearSpItems = getResources().getStringArray(R.array.list_sp_array2);
        lowerMonthSpItems = getResources().getStringArray(R.array.list_sp_array3);
        upperMonthSpItems = getResources().getStringArray(R.array.list_sp_array3);

        String topSQL = "select * from foodinfo";

        searchBt = (Button) view.findViewById(R.id.list_search_bt) ;
        searchOptionBt = (Button) view.findViewById(R.id.list_search_option_bt);
        foodinfoListView = (ListView) view.findViewById(R.id.list_lv);
        searchWord = (EditText) view.findViewById(R.id.list_search_word);

        if (HomeActivity.isShowAlert == true) {
            alertLimitFoodDialogFragment = new AlertLimitFoodDialogFragment();
            alertLimitFoodDialogFragment.show(getActivity().getSupportFragmentManager(), "show_alert");
        }

        // ------------------------以下ポップアップメニュー関連---------------------------------------------
        mPopupWindow = new PopupWindow(getContext());

        popupView = getLayoutInflater().inflate(R.layout.popupwindow_list_search, null);

        lowerYearSpinner = popupView.findViewById(R.id.popup_sp1);
        upperYearSpinner = popupView.findViewById(R.id.popup_sp2);
        lowerMonthSpinner = popupView.findViewById(R.id.popup_sp3);
        upperMonthSpinner = popupView.findViewById(R.id.popup_sp4);
        searchExecBt = popupView.findViewById(R.id.popup_search_bt);
        searchResetBt = popupView.findViewById(R.id.popup_reset_bt);
        searchExpiredCb = popupView.findViewById(R.id.popup_list_cb);
        radioGroup1 = popupView.findViewById(R.id.popup_buy_radiogroup1);
        buyRadioButton1 = popupView.findViewById(R.id.popup_buy_radiobutton1);
        buyRadioButton2 = popupView.findViewById(R.id.popup_buy_radiobutton2);
        buyRadioButton3 = popupView.findViewById(R.id.popup_buy_radiobutton3);
        radioGroup2 = popupView.findViewById(R.id.popup_limit_radiogroup);
        limitRadioButton1 = popupView.findViewById(R.id.popup_limit_radiobutton1);
        limitRadioButton2 = popupView.findViewById(R.id.popup_limit_radiobutton2);
        limitRadioButton3 = popupView.findViewById(R.id.popup_limit_radiobutton3);
        radioGroup3 = popupView.findViewById(R.id.popup_price_radiogroup);
        priceRadioButton1 = popupView.findViewById(R.id.popup_price_radiobutton1);
        priceRadioButton2 = popupView.findViewById(R.id.popup_price_radiobutton2);
        priceRadioButton3 = popupView.findViewById(R.id.popup_price_radiobutton3);


        lowerMonthSpinner.setVisibility(View.GONE);
        upperMonthSpinner.setVisibility(View.GONE);

        lowerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, lowerYearSpItems);
        lowerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lowerYearSpinner.setAdapter(lowerAdapter);

        upperAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, upperYearSpItems);
        upperAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        upperYearSpinner.setAdapter(upperAdapter);

        lowerMonthAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, lowerMonthSpItems);
        lowerMonthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lowerMonthSpinner.setAdapter(lowerMonthAdapter);

        upperMonthAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, upperMonthSpItems);
        upperMonthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        upperMonthSpinner.setAdapter(upperMonthAdapter);
        // ------------------------------------------------------------------------------------------------------

        readData(topSQL);

        listAdapter.setFoodList(foodinfoList);
        foodinfoListView.setAdapter(listAdapter);

        // 検索する範囲を「すべて」に初期化
        yearDay = changeSpItems(getLowerYearItem, getLowerMonthItem);
        yearDay2 = changeSpItems(getUpperYearItem, getUpperMonthItem);

        // 取得したい文字を入力して検索ボタンを押した時の処理
        searchBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getWord = searchWord.getText().toString();
                String searchSql;
                if (getWord.equals("")) {
                    like = "'%'";
                } else {
                    like = "'%" + getWord + "%'";
                }
                searchSql = "select * from foodinfo where foodName like " + like + " and buyDate between " + yearDay + " and " + yearDay2 + " and dueDate < " + limitDate + orderBy;
                readData(searchSql);
                listAdapter.setFoodList(foodinfoList);
                foodinfoListView.setAdapter(listAdapter);
            }
        });

        // 検索条件ボタンを押すとポップアップ画面を表示
        searchOptionBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPopup();
            }
        });

        foodinfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView foodinfo = (ListView) parent;
                int foodinfoId = (int) listAdapter.getItemId(position);

                SelectFoodinfoDialogFragment selectFoodinfoDialogFragment = new SelectFoodinfoDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("FOODINFOID", foodinfoId);
                selectFoodinfoDialogFragment.setArguments(bundle);
                selectFoodinfoDialogFragment.show(getActivity().getSupportFragmentManager(), "foodinfo_dialog");
            }
        });
        return view;
    }

    // ポップアップ画面表示処理
    private void searchPopup(){

        popupView.findViewById(R.id.popup_list_close_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            }
        });

        mPopupWindow.setContentView(popupView);

        // 背景設定
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_background));

        // タップ時に他のViewでキャッチされないための設定
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);

        // 表示サイズの設定
        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 380, getResources().getDisplayMetrics());
        mPopupWindow.setWindowLayoutMode((int) width, WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setWidth((int) width);
        mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // 画面中央に表示
        mPopupWindow.showAtLocation(searchOptionBt, Gravity.CENTER, 0, 0);

        // 検索したい購入日付の範囲の下限の年をスピナから取得
        lowerYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner)parent;
                if (position == 0) {
                    lowerMonthSpinner.setVisibility(View.GONE);
                    getLowerYearItem = "0000";
                    getLowerMonthItem = "00";
                } else {
                    lowerMonthSpinner.setVisibility(View.VISIBLE);
                    getLowerYearItem = (String)spinner.getSelectedItem();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 検索したい購入日付の範囲の上限の年をスピナから取得
        upperYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner)parent;
                if (position == 0) {
                    upperMonthSpinner.setVisibility(View.GONE);
                    getUpperYearItem = "9999";
                    getUpperMonthItem = "99";
                } else {
                    upperMonthSpinner.setVisibility(View.VISIBLE);
                    getUpperYearItem = (String)spinner.getSelectedItem();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 検索したい購入日付の範囲の下限の月をスピナから取得
        lowerMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner)parent;
                getLowerMonthItem = (String) spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 検索したい購入日付の範囲の上限の月をスピナから取得
        upperMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner)parent;
                getUpperMonthItem = (String) spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 「この条件で検索する」を押した時の処理
        searchExecBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearDay = changeSpItems(getLowerYearItem, getLowerMonthItem);
                yearDay2 = changeSpItems(getUpperYearItem, getUpperMonthItem);

                String sql;
                if (isBuySort == true) {
                    if (isLimitSort == true) {
                        if (isPriceSort == true) {
                            orderBy = " order by " + buySort + ", " + limitSort + ", " + priceSort;
                        } else {
                            orderBy = " order by " + buySort + ", " + limitSort;
                        }
                    } else {
                        if (isPriceSort == true) {
                            orderBy = " order by " + buySort + ", " + priceSort;
                        } else {
                            orderBy = " order by " + buySort;
                        }
                    }
                } else if (isLimitSort == true) {
                    if (isPriceSort == true) {
                        orderBy = " order by " + limitSort + ", " + priceSort;
                    } else {
                        orderBy = " order by " + limitSort;
                    }
                } else if (isPriceSort == true) {
                    orderBy = " order by " + priceSort;
                } else {
                    orderBy = "";
                }

                sql = "select * from foodinfo where foodName like " + like + " and buyDate between " + yearDay + " and " + yearDay2 + " and dueDate < " + limitDate + orderBy;
                readData(sql);
                listAdapter.setFoodList(foodinfoList);
                foodinfoListView.setAdapter(listAdapter);
                mPopupWindow.dismiss();
            }
        });

        // 購入日の並び替え
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // sql文に"order by"を追加してソートを行うか, ソートしないかを選択
                switch (checkedId) {
                    case R.id.popup_buy_radiobutton1:
                        buySort = "";
                        isBuySort = false;
                        break;

                    case R.id.popup_buy_radiobutton2:
                        buySort = "buyDate asc";
                        isBuySort = true;
                        break;

                    case R.id.popup_buy_radiobutton3:
                        buySort = "buyDate desc";
                        isBuySort = true;
                        break;
                }
            }
        });

        // 期限日の並び替え
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // sql文に"order by"を追加してソートを行うか, ソートしないかを選択
                switch (checkedId) {
                    case R.id.popup_limit_radiobutton1:
                        limitSort = "";
                        isLimitSort = false;
                        break;

                    case R.id.popup_limit_radiobutton2:
                        limitSort = "dueDate asc ";
                        isLimitSort = true;
                        break;

                    case R.id.popup_limit_radiobutton3:
                        limitSort = "dueDate desc ";
                        isLimitSort = true;
                        break;
                }
            }
        });

        // 金額の並び替え
        radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // sql文に"order by"を追加してソートを行うか, ソートしないかを選択
                switch (checkedId) {
                    case R.id.popup_price_radiobutton1:
                        isPriceSort = false;
                        break;

                    case R.id.popup_price_radiobutton2:
                        priceSort = "foodPrice asc";
                        isPriceSort = true;
                        break;

                    case R.id.popup_price_radiobutton3:
                        priceSort = "foodPrice desc";
                        isPriceSort = true;
                        break;

                }
            }
        });

        // リセットボタンを押した時の処理
        // スピナ, ラジオボタンを初期化
        searchResetBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lowerYearSpinner.setAdapter(lowerAdapter);
                upperYearSpinner.setAdapter(upperAdapter);
                lowerMonthSpinner.setAdapter(lowerMonthAdapter);
                upperMonthSpinner.setAdapter(upperMonthAdapter);

                // デフォルト値に設定
                buyRadioButton1.setChecked(true);
                buySort = "";
                isBuySort = false;
                limitRadioButton1.setChecked(true);
                limitSort = "";
                isLimitSort = false;
                priceRadioButton1.setChecked(true);
                priceSort = "";
                isPriceSort = false;
                orderBy = "";
                searchExpiredCb.setChecked(false);
                limitDate = 99999999;
            }
        });

        // 「期限が切れた食品のみ表示」ボタンが押された時の処理
        searchExpiredCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchExpiredCb.isChecked() == true) {
                    // limitDate を現在の日付に変更
                    Calendar nowTime = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    limitDate = Integer.parseInt(sdf.format(nowTime.getTime()));
                } else {
                    // チェックを外すとデフォルトの値に戻す
                    limitDate = 99999999;
                }
            }
        });

    }

    private void readData(String sql){
        if (databaseOpenHelper == null) {
            databaseOpenHelper = new DatabaseOpenHelper(getActivity());
        }

        if(sqLiteDatabase == null){
            sqLiteDatabase = databaseOpenHelper.getReadableDatabase();
        }

        // リストの初期化
        foodinfoList = new ArrayList<>();
        listAdapter = new ListAdapter(getContext());

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Foodinfo food = new Foodinfo();
            food.setId(cursor.getInt(0));
            food.setFoodName(cursor.getString(1));
            food.setBuyDate(iToString(cursor.getInt(2)));
            food.setDueDate(iToString(cursor.getInt(3)));
            food.setPrice(integerChangeFormat(cursor.getInt(4)));
            foodinfoList.add(food);
        }
        cursor.close();
    }

    // 日付に区切り線を挿入, 整数型を文字列型に変換するメソッド
    private String iToString(int i){
        String str = String.valueOf(i);
        String yyyy = str.substring(0, 4);
        String MM = str.substring(4, 6);
        String dd = str.substring(6, 8);
        str =  yyyy + "/" + MM + "/" + dd;
        return str;
    }

    private String integerChangeFormat(int price) {
        String changePrice;
        changePrice = String.format("￥%,d", price);
        return changePrice;
    }

    // スピナで選択された年月を結合し整数型で返すメソッド
    private int changeSpItems(String s, String s2){
        String yyyy = s.substring(0, 4);
        String MM = s2.substring(0, 2);
        int i = Integer.parseInt(yyyy + MM) * 100;
        return i;
    }


}
