package com.websarva.wings.android.backup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

// 設定画面の通知変更のフラグメント生成クラス
public class ConfNotifyFragment extends Fragment {

    // フィールド
    private TextView notifyTv1, notifyTv2;
    private Spinner notifySp;
    private Switch notifySw1;
    private Button backBt;
    private FragmentManager fm;

    public ConfNotifyFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conf_notify, container, false);

        notifyTv1 = (TextView) view.findViewById(R.id.conf_notify_tv1);
        notifyTv2 = (TextView) view.findViewById(R.id.conf_notify_tv2);
        notifySp = (Spinner) view.findViewById(R.id.conf_notify_sp);
        notifySw1 = (Switch) view.findViewById(R.id.conf_notify_sw1);
        backBt = (Button) view.findViewById(R.id.conf_notify_back_bt);

        // 食品ロス通知がOFFの時、TextViewを非表示
        notifyTv1.setVisibility(View.GONE);
        notifyTv2.setVisibility(View.GONE);
        // 食品ロス通知がOFFの時、Spinnerを非表示
        notifySp.setVisibility(View.GONE);

        // スピナー選択処理
        notifySw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (notifySw1.isChecked()) {
                    // 食品ロス通知がONの時、通知時期変更内容を表示
                    notifyTv1.setVisibility(View.VISIBLE);
                    notifyTv2.setVisibility(View.VISIBLE);
                    notifySp.setVisibility(View.VISIBLE);
                } else {
                    // 食品ロス通知がOFFの時、通知時期変更内容を非表示
                    notifyTv1.setVisibility(View.GONE);
                    notifyTv2.setVisibility(View.GONE);
                    notifySp.setVisibility(View.GONE);
                }
            }
        });

        // 戻るボタンが押された時に前の画面へ戻る
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });

        return view;
    }


}
