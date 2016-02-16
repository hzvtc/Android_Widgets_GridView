package com.example.administrator.android_widgets_gridview;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/*
    android:numColumns="auto_fit" 自动调整显示列数
    android:verticalSpacing="10dp" 行之间的边距
    android:horizontalSpacing="10dp" 列之间的边距
    android:gravity="center" 重心横竖方向居中

 */
public class MainActivity extends Activity {
    private Panel panel;
    private LinearLayout contianer;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("可动态布局的抽屉组件之构建基础");

        findViewById();

        //新建测试组件
        TextView tv = new TextView(this);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        tv.setText("测试组件 红字白底");
        tv.setTextColor(Color.RED);
        tv.setBackgroundColor(Color.WHITE);

        panel.fillPanelContainer(tv);
        panel.setPanelOpenedEvent(panelOpenedEvent);
        panel.setPanelClosedEvent(panelClosedEvent);

        //往GridView填充数据
        ArrayList<HashMap<String, Object>> ImageItemList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("itemImage", R.drawable.user_img);
            map.put("itemTextView", "NO." + String.valueOf(i));
            ImageItemList.add(map);
        }

        SimpleAdapter saImageAdapter=new SimpleAdapter(this,
                ImageItemList,
                R.layout.item,
                new String[]{"itemImage","itemTextView"},
                new int[]{R.id.itemImage,R.id.itemTextView});

        gridView.setAdapter(saImageAdapter);
        gridView.setOnItemClickListener(new itemCLickListener());
    }

    Panel.PanelOpenedEvent panelOpenedEvent = new Panel.PanelOpenedEvent() {
        @Override
        public void OnPanelOpened(View Panel) {
            Log.e("PanelOpenedEvent","PanelOpenedEvent");
        }
    };

    Panel.PanelClosedEvent panelClosedEvent = new Panel.PanelClosedEvent() {
            @Override
            public void OnPanelClosed(View Panel) {
                Log.e("PanelClosedEvent","PanelClosedEvent");
            }
    };

    private void findViewById() {
        gridView = (GridView) findViewById(R.id.gridView);
        contianer = (LinearLayout) findViewById(R.id.container);
        panel = new Panel(this, gridView, 200, LinearLayout.LayoutParams.MATCH_PARENT);
        contianer.addView(panel);
    }

    class itemCLickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HashMap<String,Object> item = (HashMap<String,Object>)parent.getItemAtPosition(position);
            Log.e("itemCLickListener",(String)item.get("itemTextView"));
        }
    }
}
