package com.example.MyCustomView.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by XaChya on 2016/9/10.
 * <p/>
 * 这是一个取色板
 * 我们知道，颜色是三原色组成，那么我们就只需要写出三种颜色的渐变，就可以了
 */
public class ColorSelectView extends View {

    public ColorSelectView(Context context) {
        this(context, null);
    }

    public ColorSelectView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //画图形的画笔，画文字的画笔。
    private Paint paint = null;
    private TextPaint textPaint = null;

    private void init() {
        paint = new Paint();
        paint.setStrokeWidth(1);
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(30);
    }

    //用于保存三个渐变板的取色结果，取值范围是0X00-OXFF
    private int intR = 0xfa;
    private int intG = 0x12;
    private int intB = 0x65;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        //绘制红色的渐变
        for (int i = 0xff000000, j = 0; i <= 0xffff0000; i += 0x10000, j++) {
            //这里的i取值为：
            //0xff000000,0xff010000
            paint.setColor(i);
            canvas.drawLine(0, j, w / 3, j, paint);
        }
        //绘制绿的渐变
        for (int i = 0xff000000, j = 0; i <= 0xff00ff00; i += 0x100, j++) {
            paint.setColor(i);
            canvas.drawLine(w / 3, j, w / 3 * 2, j, paint);
        }
        //绘制蓝色的渐变
        for (int i = 0xff000000, j = 0; i <= 0xff0000ff; i++, j++) {
            paint.setColor(i);
            canvas.drawLine(w / 3 * 2, j, w, j, paint);
        }

        //画图例
        //intR=0xfa;------0x00fa0000
        //intG=0x12;------0x00001200
        //intB=0x65;------0x00000065
        //fffa1265
        paint.setColor(0xff000000 + intR * 0x10000 + intG * 0x100 + intB);
        canvas.drawRect(0, 280, w, 320, paint);

        //画图例对应的值

        canvas.drawText("0xff" + getHex(intR) + getHex(intG) + getHex(intB),
                w / 2 - textPaint.measureText("0xff" + getHex(intR) + getHex(intG) + getHex(intB))/2, 350, textPaint
        );

    }

    private String getHex1(int n) {
        String[] strs = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
        return strs[n];
    }

    private String getHex2(int n) {
        String str = "0123456789abcdef";
        return str.charAt(n) + "";
    }


    private String getHex(int n) {
        int n1 = n / 16;
        int n2 = n % 16;
        char a1 = 0;
        char a2 = 0;
        if (n1 > 9) {
            n1 = 'a' + n1 - 10;
            a1 = (char) n1;
        } else {
            n1 = '0' + n1;
            a1 = (char) n1;
        }
        if (n2 > 9) {
            n2 = 'a' + n2 - 10;
            a2 = (char) n2;
        } else {
            n2 = '0' + n2;
            a2 = (char) n2;
        }
        return "" + a1 + a2;
    }

    //这个方法是事件底层的方法。
    //这个方法当你有move，up，down的时候都会触发


    //返回值为boolean，代表了事件是否消费，在这里我们要消费事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int w=getWidth();
        //如果if成立，就是摸到view，那么就要去动态改变intR、g、b
        if(event.getAction()==MotionEvent.ACTION_DOWN
                ||event.getAction()==MotionEvent.ACTION_MOVE
                ){
            //如果if成立，就是摸到渐变板
            if(event.getY()<256&&event.getY()>=0){
                //判断到底摸到哪种颜色的渐变板
                if(event.getX()<w/3){
                    //红色变化
                    intR=(int)event.getY();
                }else{
                    if(event.getX()>w/3*2){
                        //蓝色变化
                        intB= (int) event.getY();

                    }else{
                        //绿色变化
                        intG=(int)event.getY();
                    }
                }
            }
            //颜色变化要重新调用ondraw绘制结果，刷新界面
            invalidate();
        }




        return true;
    }
}
