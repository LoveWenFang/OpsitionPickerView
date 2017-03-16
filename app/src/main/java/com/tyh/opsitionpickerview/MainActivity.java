package com.tyh.opsitionpickerview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    OptionsPickerView pvOptions;

    static ArrayList<RegionInfo> item1;

    static ArrayList<ArrayList<RegionInfo>> item2 = new ArrayList<ArrayList<RegionInfo>>();

    static ArrayList<ArrayList<ArrayList<RegionInfo>>> item3 = new ArrayList<ArrayList<ArrayList<RegionInfo>>>();

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            System.out.println(System.currentTimeMillis());
            // 三级联动效果

            pvOptions.setPicker(item1, item2, item3, true);
            pvOptions.setCyclic(true, true, true);
            pvOptions.setSelectOptions(0, 0, 0);
            tvOptions.setClickable(true);
        }

        ;
    };
    private TextView tvOptions;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        showPickerView();
    }

    private void showPickerView() {
        // 选项选择器
        pvOptions = new OptionsPickerView(this);
        tvOptions.setClickable(false);

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                System.out.println(System.currentTimeMillis());
                if (item1 != null && item2 != null && item3 != null) {
                    handler.sendEmptyMessage(0x123);
                    return;
                }
                item1 = (ArrayList<RegionInfo>) RegionDAO.getProvencesOrCity(1);
                for (RegionInfo regionInfo : item1) {
                    item2.add((ArrayList<RegionInfo>) RegionDAO
                            .getProvencesOrCityOnParent(regionInfo.getId()));

                }

                for (ArrayList<RegionInfo> arrayList : item2) {
                    ArrayList<ArrayList<RegionInfo>> list2 = new ArrayList<ArrayList<RegionInfo>>();
                    for (RegionInfo regionInfo : arrayList) {

                        ArrayList<RegionInfo> q = (ArrayList<RegionInfo>) RegionDAO
                                .getProvencesOrCityOnParent(regionInfo.getId());
                        list2.add(q);

                    }
                    item3.add(list2);
                }

                handler.sendEmptyMessage(0x123);

            }
        }).start();
        // 设置选择的三级单位
        // pwOptions.setLabels("省", "市", "区");
        pvOptions.setTitle("选择城市");

        // 设置默认选中的三级项目
        // 监听确定选择按钮

        pvOptions
                .setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

                    @Override
                    public void onOptionsSelect(int options1, int option2,
                                                int options3) {
                        // 返回的分别是三个级别的选中位置
                        String tx = item1.get(options1).getPickerViewText()
                                + item2.get(options1).get(option2)
                                .getPickerViewText()
                                + item3.get(options1).get(option2)
                                .get(options3).getPickerViewText();
                        tvOptions.setText(tx);

                    }
                });

    }

    private void initView() {
        tvOptions = (TextView) findViewById(R.id.tvOptions);
        btn = (Button) findViewById(R.id.btn);

        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                pvOptions.show();
                break;
        }
    }
}
