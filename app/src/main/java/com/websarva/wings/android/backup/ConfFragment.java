package com.websarva.wings.android.backup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 設定画面のメインのフラグメント生成クラス
public class ConfFragment extends Fragment {

    // フィールド
//    private static final String[] lvItems = {
//            // リストビューの各アイテム
//            "利用者情報変更",
//            "通知変更",
//            "言語変更"
//    };
    private static final String[] lvItems = new String[3];
    private static final Integer[] lvImages = {
            // リストビューの各アイテムに表示するアイコン
            R.drawable.ic_baseline_accessibility_new_24,
            R.drawable.ic_baseline_notifications_24,
            R.drawable.ic_baseline_language_24
    };
    private List<String> items;
    private List<Integer> images;
    private ListView lv;
    private BaseAdapter adapter;
    private InputPassDialogFragment inputPassDialogFragment;
    private ConfNotifyFragment notifyFragment;
    private ConfLangFragment langFragment;
    private FragmentTransaction langTransaction;

    public ConfFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conf, container, false);
        lvItems[0] = getString(R.string.conf_lv_item1);
        lvItems[1] = getString(R.string.conf_lv_item2);
        lvItems[2] = getString(R.string.conf_lv_item3);


        items = new ArrayList<>(Arrays.asList(lvItems));
        images = new ArrayList<>(Arrays.asList(lvImages));
        lv = (ListView) view.findViewById(R.id.conf_lv);
        adapter = new ListViewAdapter(this.getContext(), R.layout.fragment_conf_row, items, images);

        // 各リストアイテムが選択された時の処理
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // パスワードを入力させるダイアログの表示
                        inputPassDialogFragment = new InputPassDialogFragment();
                        inputPassDialogFragment.show(getActivity().getSupportFragmentManager(), "userinfo_dialog");
                        break;

                    case 1:
                        // 通知変更画面の出力
                        notifyFragment = new ConfNotifyFragment();
                        //通知情報のフラグメントをセット
                        setFragment(notifyFragment);
                        break;

                    case 2:
                        // 言語変更画面の出力
                        langFragment = new ConfLangFragment();
                        // 言語変更のフラグメントをセット
                        setFragment(langFragment);
                        break;
                }
            }
        });

        return view;
    }

    // フラグメントセットメソッド
    public void setFragment(Fragment fragment) {
        // フラグメントトランザクションの開始・アニメーションの設定
        langTransaction = getActivity().getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // レイアウトをfragmentに置き換え（追加）
        langTransaction.replace(R.id.fragment_container, fragment);
        // 置き換えのトランザクションをバックスタックに保存する
        langTransaction.addToBackStack(null);
        // フラグメントトランザクションをコミット
        langTransaction.commit();
    }



}