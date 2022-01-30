package com.websarva.wings.android.backup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

// リストビューのアダプタ
public class ListViewAdapter extends BaseAdapter {

    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }

    private final LayoutInflater inflater;
    private final int itemLayoutId;
    private final List<String> titles;
    private final List<Integer> ids;

    ListViewAdapter(Context context, int itemLayoutId,
                    List<String> itemNames, List<Integer> itemImages) {
        super();
        this.inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        this.itemLayoutId = itemLayoutId;
        this.titles = itemNames;
        this.ids = itemImages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // 最初だけ View を inflate して、それを再利用する
        if (convertView == null) {
            // activity_main.xml に list.xml を inflate して convertView とする
            convertView = inflater.inflate(itemLayoutId, parent, false);

            // ViewHolder を生成
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.conf_row_tv);
            holder.imageView = convertView.findViewById(R.id.conf_row_iv);

            convertView.setTag(holder);

            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT, 250
            );

            // setContentViewに設定
            convertView.setLayoutParams(params);

        }
        // holder を使って再利用
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        // holder の imageView にセット
        holder.imageView.setImageResource(ids.get(position));
        // 現在の position にあるファイル名リストを holder の textView にセット
        holder.textView.setText(titles.get(position));

        return convertView;
    }

    @Override
    public int getCount() {
        // texts 配列の要素数
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
