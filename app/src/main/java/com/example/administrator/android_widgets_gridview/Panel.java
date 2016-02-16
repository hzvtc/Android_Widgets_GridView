package com.example.administrator.android_widgets_gridview;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/2/16.
 */
public class Panel extends LinearLayout {

    public interface PanelClosedEvent {
        void OnPanelClosed(View Panel);
    }

    public interface PanelOpenedEvent {
        void OnPanelOpened(View Panel);
    }

    /*Handle的宽度 和Panel等宽*/
    private static final int HANDLE_WIDTH = 30;

    /*每次自动展开 收缩的范围*/
    private static final int MOVE_WIDTH = 20;
    private Button btnHandle;
    private LinearLayout PanelContainer;
    private int mRightMargin = 0;
    private Context mContext;
    private PanelClosedEvent panelClosedEvent;
    private PanelOpenedEvent panelOpenedEvent;

    /*设置收缩时的回调函数*/
    public void setPanelClosedEvent(PanelClosedEvent panelClosedEvent) {
        this.panelClosedEvent = panelClosedEvent;
    }

    /*设置打开时的回调函数*/
    public void setPanelOpenedEvent(PanelOpenedEvent panelOpenedEvent) {
        this.panelOpenedEvent = panelOpenedEvent;
    }

    public Panel(Context context, View OtherView, int width, int height) {
        super(context);
        this.mContext = context;
        //改变Panel附近的组件的属性
        LayoutParams otherLP = (LayoutParams) OtherView.getLayoutParams();
        otherLP.weight = 1;
        OtherView.setLayoutParams(otherLP);

        //设置Panel本身属性
        LayoutParams lp = new LayoutParams(width, height);
        lp.rightMargin = -lp.width + HANDLE_WIDTH;
        mRightMargin = Math.abs(lp.rightMargin);
        this.setLayoutParams(lp);
        this.setOrientation(LinearLayout.HORIZONTAL);

        //设置Handle的属性
        btnHandle = new Button(context);
        btnHandle.setLayoutParams(new LayoutParams(HANDLE_WIDTH, height));
        btnHandle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutParams lp = (LayoutParams) Panel.this.getLayoutParams();
                if (lp.rightMargin < 0) {
                    //关闭状态
                    new AsynMove().execute(new Integer[]{MOVE_WIDTH});
                } else if (lp.rightMargin > 0) {
                    //打开的状态
                    new AsynMove().execute(new Integer[]{-MOVE_WIDTH});
                }
            }
        });

        this.addView(btnHandle);

        //设置Container的属性
        PanelContainer = new LinearLayout(context);
        PanelContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        this.addView(PanelContainer);
    }

    /*把View放在Panel的Container中*/
    public void fillPanelContainer(View v) {
        PanelContainer.addView(v);
    }

    /**
     * 异步移动Panel
     *
     * @author mine
     */
    class AsynMove extends AsyncTask<Integer,Integer,Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            int times;
            if (mRightMargin % Math.abs(params[0]) == 0) {//整除
                times = mRightMargin / Math.abs(params[0]);
            } else {//有余数
                times = mRightMargin / Math.abs(params[0]) + 1;
            }

            for (int i = 0; i < times; i++) {
                publishProgress(params);
                try {
                    Thread.sleep(Math.abs(params[0]));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            LayoutParams lp = (LayoutParams) Panel.this.getLayoutParams();
            if (values[0]>0){
                lp.rightMargin = Math.max(lp.rightMargin+values[0],(-lp.rightMargin));
            }
            else {
                lp.rightMargin=Math.min(lp.rightMargin+values[0],0);
            }

            if(lp.rightMargin==0&&panelOpenedEvent!=null){//展开以后
                panelOpenedEvent.OnPanelOpened(Panel.this);//调用回调函数
            }
            else if(lp.rightMargin==-mRightMargin&&panelClosedEvent!=null){
                panelClosedEvent.OnPanelClosed(Panel.this);
            }

            Panel.this.setLayoutParams(lp);
        }
    }
}
