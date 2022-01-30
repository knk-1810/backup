package com.websarva.wings.android.backup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TapPagerAdapter extends FragmentStateAdapter {
    public TapPagerAdapter(MoneyFragment fragment) {
        super(fragment);
    }

    /**
     * 指定されたタブの位置(position) に対応するタブページ（Fragment）を作成する
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new MoneyDayFragment();
        } else if (position == 1) {
            fragment = new MoneyMonthFragment();
        } else if (position == 2) {
            fragment = new MoneyLimitConfFragment();
        }
        return fragment;
    }
    /**
     * タブの数を返す
     */
    @Override
    public int getItemCount() {
        return 3;
    }
}