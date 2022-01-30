package com.websarva.wings.android.backup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class List2Adapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<SumPriceinfo> sumPriceinfo;

    public List2Adapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setFoodList(ArrayList<SumPriceinfo> sumPriceinfo) {
        this.sumPriceinfo = sumPriceinfo;
    }

    @Override
    public int getCount() {
        return sumPriceinfo.size();
    }

    @Override
    public Object getItem(int position) {
        return sumPriceinfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return sumPriceinfo.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.fragment_money_day_lv_row,parent,false);

        ((TextView)convertView.findViewById(R.id.money_day_lv_row_tv1)).setText(sumPriceinfo.get(position).getDay());
        ((TextView)convertView.findViewById(R.id.money_day_lv_row_tv2)).setText(sumPriceinfo.get(position).getSumPrice());

        return convertView;
    }
}