package com.example.MyCustomView.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by XaChya on 2016/9/13.
 *
 * 聊天消息的，红气泡提醒
 */
public class NumberView extends ImageView{
    private int num=1;
    //SHOW_NUM_MODE_0时是显示数字
    //SHOW_NUM_MODE_1时是只显示小的红圆
    private int showNumMode=0;

    public static final int SHOW_NUM_MODE_0=0;
    public static final int SHOW_NUM_MODE_1=1;


    public int getShowNumMode() {
        return showNumMode;
    }

    public void setShowNumMode(int showNumMode) {
        this.showNumMode = showNumMode;
        invalidate();
    }

    public void setNum(int num) {
        this.num = num;
        invalidate();
    }

    public int getNum() {
        return num;
    }

    public NumberView(Context context) {
        this(context,null);
    }

    public NumberView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //画圆圈的画笔
    private Paint paint=null;
    //画数字的画笔
    private TextPaint textPaint=null;
    private void init(){
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        textPaint=new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(getPaddingRight());
        textPaint.setStrokeWidth(5);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //绘制头像
        super.onDraw(canvas);

        switch (showNumMode){
            case SHOW_NUM_MODE_0:
                //当数字为0，或者负数，就不画红圆和文字
                if(num>0&&num<=99){
                    //绘制红色圆圈
                    //用view的宽度-右内边距-文字宽度的一半
                    float cx=getWidth()-getPaddingRight()-0.5f*textPaint.measureText(num+"");
                    //view的上内边距+文字高度的一半
                    float cy=getPaddingTop()+1.5f*textPaint.getFontMetrics().bottom;
                    //r取横向和纵向的较小的值
                    float r=(getWidth()-cx)>cy?cy:(getWidth()-cx);
                    canvas.drawCircle(cx,cy,r,paint);


                    //绘制文字
                    canvas.drawText(num+"",getWidth()-getPaddingRight()-textPaint.measureText(num+""),getPaddingTop()+textPaint.getFontMetrics().bottom*3f,textPaint);

                }else{
                    if(num>99){
                        //绘制红色圆圈
                        //用view的宽度-右内边距-文字宽度的一半
                        float cx=getWidth()-getPaddingRight()-0.5f*textPaint.measureText("99+");
                        //view的上内边距+文字高度的一半
                        float cy=getPaddingTop()+1.5f*textPaint.getFontMetrics().bottom;
                        //r取横向和纵向的较小的值
                        float r=(getWidth()-cx)>cy?cy:(getWidth()-cx);
                        canvas.drawCircle(cx,cy,r,paint);


                        //绘制文字
                        canvas.drawText("99+",getWidth()-getPaddingRight()-textPaint.measureText("99+"),getPaddingTop()+textPaint.getFontMetrics().bottom*3f,textPaint);

                    }
                }
                break;
            case SHOW_NUM_MODE_1:
                //当数字为0，或者负数，就不画红圆和文字
                if(num>0){
                    //绘制红色圆圈
                    //用view的宽度-右内边距-文字宽度的一半
                    float cx=getWidth()-getPaddingRight()-0.5f*textPaint.measureText(num+"");
                    //view的上内边距+文字高度的一半
                    float cy=getPaddingTop()+1.5f*textPaint.getFontMetrics().bottom;
                    //r取横向和纵向的较小的值
                    float r=10;
                    canvas.drawCircle(cx,cy,r,paint);

                }
                break;
        }









    }
}
