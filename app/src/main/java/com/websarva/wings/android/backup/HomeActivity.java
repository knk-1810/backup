package com.websarva.wings.android.backup;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Locale;

// ログイン後に表示されるホーム画面
// ツールバー、ナビゲーションボタンの設置、画面中央のフラグメントによる画面遷移処理
public class HomeActivity extends AppCompatActivity {

    // フィールド
    // データベース関連
    public static String DATABASE_NAME;
    public static Boolean isShowAlert = true;
    private static DatabaseOpenHelper databaseOpenHelper;
    private static SQLiteDatabase sqLiteDatabase;
    private static String getId, getPass;
    private GetFlagTask flagTask;

    private Typeface customFont;
    private Button appNameBt;
    private Toolbar toolbar;
    private BottomNavigationView bottonNav;
    private MenuInflater inflater;
    private int itemId;
    private DialogFragment logoutDialogFragment, upgradeDialogFragment;
    private Fragment selectFragment;
    private FragmentTransaction ft;
    private int langType = 1; // 言語フラグ
    private MenuItem menuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        getId = intent.getStringExtra("GETID");
        getPass = intent.getStringExtra("GETPASS");


        DATABASE_NAME = getId + ".db";
        if (databaseOpenHelper == null) {
            databaseOpenHelper = new DatabaseOpenHelper(getApplicationContext());
        }

        if(sqLiteDatabase == null){
            sqLiteDatabase = databaseOpenHelper.getReadableDatabase();
        }

        langType = languageData();
        if (langType == 1) {
            setLocale("ja");
        } else {
            setLocale("en");
        }

        setContentView(R.layout.activity_home);
        appNameBt = findViewById(R.id.home_title_bt);
        toolbar = findViewById(R.id.toolbar);
        bottonNav = findViewById(R.id.bottom_navigation);


        // アプリ名のフォントを変更
        customFont = Typeface.createFromAsset(getAssets(), "Kalam-Bold.ttf");
        appNameBt.setTypeface(customFont);

        // ツールバーの作成
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // フラグメントの設置・初期状態は入力フラグメント
        bottonNav.setOnItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InputFragment()).commit();


    }


    // オプションメニューを表示
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        menuItem = menu.findItem(R.id.menu_upgrade_itm);
        menuItem.setEnabled(true);
        flagTask = new GetFlagTask();
        flagTask.setListener(createListener(menuItem));
        flagTask.execute(getId);
        return true;
    }

    // オプションメニュー選択メソッド
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        // オプションメニューの選択時処理
        itemId = item.getItemId();

        switch (itemId) {
            case R.id.menu_userinfo_itm:
                break;

            case R.id.menu_logout_itm:
                //ログアウトのダイアログ表示処理
                DATABASE_NAME = null;
                getId = null;
                getPass = null;
                logoutDialogFragment = new LogoutDialogFlagment();
                logoutDialogFragment.show(getSupportFragmentManager(), "logout_dialog");
                break;

            case R.id.menu_upgrade_itm:
                 // アップグレードのダイアログ表示処理

                Bundle bundle = new Bundle();
                bundle.putString("GET_ID", getId);
                upgradeDialogFragment = new UpgradeDialogFragment();
                upgradeDialogFragment.setArguments(bundle);
                upgradeDialogFragment.show(getSupportFragmentManager(), "upgrade_dialog");
                break;



            case R.id.menu_rule_itm:
                // 利用規約
                break;

        }
        return true;
    }

    // ナビゲーションボタンの選択
    private NavigationBarView.OnItemSelectedListener navListener =
            item -> {
                selectFragment = null;

                // 各ボタンに対するフラグメントの設置処理
                switch (item.getItemId()) {
                    case R.id.nav_in_itm:
                        // 入力ボタンが押された
                        selectFragment = new InputFragment();
                        break;

                    case R.id.nav_money_itm:
                        // 支出ボタンが押された
                        selectFragment = new MoneyFragment();
                        break;

                    case R.id.nav_conf_itm:
                        // 設定ボタンが押された
                        selectFragment = new ConfFragment();
                        break;

                    case R.id.nav_list_itm:
                        // 一覧ボタンが押された
                        selectFragment = new ListFragment();
                        break;
                }
                ft = getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.replace(R.id.fragment_container, selectFragment).commit();
                return true;
            };

    public static String getId() {
        return getId;
    }

    public static void setId(String id) {
        getId = id;
    }

    public static String getPass() {
        return getPass;
    }

    public static void setPass(String pass) {
        getPass = pass;
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
            databaseOpenHelper = new DatabaseOpenHelper(this);
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

    private GetFlagTask.Listener createListener(MenuItem menuItem) {
        return new GetFlagTask.Listener() {
            @Override
            public void onSuccess(String result) {
                if (result.equals("true")) {
                    menuItem.setEnabled(false);
                }
                invalidateOptionsMenu();

            }
        };
    }

}