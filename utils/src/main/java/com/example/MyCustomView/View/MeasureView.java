package com.example.MyCustomView.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by XaChya on 2016/9/12.
 */
public class MeasureView extends View{
    public MeasureView(Context context) {
        this(context,null);
    }

    public MeasureView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MeasureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        paint=new Paint();
    }
    private Paint paint=null;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(50,50,50,paint);
    }

    //测量view的方法(计算自己的大小)
    //参数是这个view的父view的宽高

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //设置这个view有多大
        setMeasuredDimension(80,80);
    }
}
