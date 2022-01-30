package com.websarva.wings.android.backup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MoneyFragment extends Fragment {

    // フィールド
    private String tabItemName[] = {};
    private ViewPager2 pager;
    private TapPagerAdapter adapter;
    private TabLayout tabs;

    public MoneyFragment() {
    }

    public static MoneyFragment newInstance() {
        MoneyFragment fragment = new MoneyFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_money, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabItemName = getResources().getStringArray(R.array.money_tab_item_array);

        pager = (ViewPager2)view.findViewById(R.id.money_pager);
        tabs = (TabLayout)view.findViewById(R.id.money_tab_layout);
        adapter = new TapPagerAdapter(this);

        pager.setUserInputEnabled(true);
        pager.setAdapter(adapter);

        new TabLayoutMediator(tabs, pager,
                (tab, position) -> tab.setText(tabItemName[position])
        ).attach();
    }

}
