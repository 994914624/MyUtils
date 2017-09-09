package com.example.MyCustomView.View;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by XaChya on 2016/9/10.
 * <p/>
 * 这是一个可以倒计时的button
 */
public class NumberButton extends Button {


    public NumberButton(Context context) {
        this(context, null);
    }

    public NumberButton(Context context, AttributeSet attrs) {
//      我们通过看button的源代码，知道了button的按钮样式需要com.android.internal.R.attr.buttonStyle
//        但是我们使用这个却找不到id
        //安卓源代码中的引用方式我们用不了，源代码中的com.android.internal.R我们访问不到，
        //因此现在给我们用的android源生R文件是android.R
        //因此buttonStyle在这个android.R中
        //于是代码变成了android.R.attr.buttonStyle，就有了button的样式
        this(context, attrs, android.R.attr.buttonStyle);
    }

    public NumberButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //在倒计时的时候，左侧有文本，右侧有文本，中间有数字
    private String leftText="还剩";
    private String rightText="秒";
    private String endText="重新获取";

    public String getEndText() {
        return endText;
    }

    public NumberButton setEndText(String endText) {
        this.endText = endText;
        return this;
    }

    private int num=60;

    public String getLeftText() {
        return leftText;
    }

    public NumberButton setLeftText(String leftText) {
        this.leftText = leftText;
        return this;
    }

    public String getRightText() {
        return rightText;
    }

    public NumberButton setRightText(String rightText) {
        this.rightText = rightText;
        return this;
    }

    public int getNum() {
        return num;
    }

    public NumberButton setNum(int num) {
        this.num = num;
        return this;
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int num=msg.arg1;
            //接到0，就代表结束
            if(num==0){
                NumberButton.this.setClickable(true);
                NumberButton.this.setTextColor(0xff000000);
                NumberButton.this.setText(endText);
                isStarting=false;
            }else{
                NumberButton.this.setText(leftText+num+rightText);
            }

        }
    };

    private boolean isStarting=false;

    //开启倒计时的方法
    public void start(){

        if(!isStarting) {
            isStarting=true;
            //设置为不可点击
            this.setClickable(false);
            //设置字体颜色为灰色
            this.setTextColor(0xff666666);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = num; i >= 0 ; i--) {
                        Message message=Message.obtain();
                        message.arg1=i;
                        handler.sendMessage(message);
                        sleep(1000);

                    }
                }
            }).start();

        }
    }

    private void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
