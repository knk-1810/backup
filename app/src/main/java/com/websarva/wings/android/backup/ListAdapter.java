package com.websarva.wings.android.backup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<Foodinfo> foodinfoList;

    public ListAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setFoodList(ArrayList<Foodinfo> foodList) {
        this.foodinfoList = foodList;
    }

    @Override
    public int getCount() {
        return foodinfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return foodinfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return foodinfoList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.fragment_list_row,parent,false);

        ((TextView)convertView.findViewById(R.id.list_buy_date)).setText(foodinfoList.get(position).getBuyDate());
        ((TextView)convertView.findViewById(R.id.list_name)).setText(foodinfoList.get(position).getFoodName());
        ((TextView)convertView.findViewById(R.id.list_price)).setText(String.valueOf(foodinfoList.get(position).getPrice()));
        ((TextView)convertView.findViewById(R.id.list_due_date)).setText(foodinfoList.get(position).getDueDate());

        return convertView;
    }
}
