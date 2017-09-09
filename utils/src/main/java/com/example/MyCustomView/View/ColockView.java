package com.example.MyCustomView.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XaChya on 2016/9/13.
 *
 * 画的钟表
 */
public class ColockView extends View {

    private static final float sqrt3 = 1.732f;

    private double hour = 2 * Math.PI / 60 * 58;
    private double min = 2 * Math.PI / 60 * 35;
    private double second = 2 * Math.PI / 60 * 18;

    public void setTime(int hourOf24, int minOf60, int secondOf60) {
        second = 2 * Math.PI / 60 * secondOf60;
        min = 2 * Math.PI / 60 * minOf60 + 2 * Math.PI / 60 * second / 60;
        hour = 2 * Math.PI / 60 * hourOf24 + 2 * Math.PI / 60 * min / 60;
        invalidate();
    }

    private List<Float> textX = null;
    private List<Float> textY = null;
    private float r;

    public ColockView(Context context) {
        this(context, null);
    }

    public ColockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Paint paint = null;
    private TextPaint textPaint = null;

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textX = new ArrayList<Float>();
        textY = new ArrayList<Float>();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        textX.clear();
        textY.clear();
        r = getWidth() / 2 - 60;
        float cx = (getWidth() / 2);
        float cy = (getHeight() / 2);
        //第一个点
        textX.add(cx);
        textY.add(cy - r);
        //第二个点
        textX.add(cx + r / 2);
        textY.add(cy - r / 2 * sqrt3);
        //第三个点
        textX.add(cx + r / 2 * sqrt3);
        textY.add(cy - r / 2);
        //第四个点
        textX.add(cx + r);
        textY.add(cy);
        //第五个点
        textX.add(cx + r / 2 * sqrt3);
        textY.add(cy + r / 2);
        //第六个点
        textX.add(cx + r / 2);
        textY.add(cy + r / 2 * sqrt3);
        //第七个点
        textX.add(cx);
        textY.add(cy + r);


        //第八个点开始中心对称从第二个
        //第二个点
        textX.add(cx - r / 2);
        textY.add(cy + r / 2 * sqrt3);
        //第三个点
        textX.add(cx - r / 2 * sqrt3);
        textY.add(cy + r / 2);
        //第四个点
        textX.add(cx - r);
        textY.add(cy);
        //第五个点
        textX.add(cx - r / 2 * sqrt3);
        textY.add(cy - r / 2);
        //第六个点
        textX.add(cx - r / 2);
        textY.add(cy - r / 2 * sqrt3);


        for (int i = 0; i < 360; i += 6) {
            paint.setStrokeWidth(1);
            canvas.drawLine(getWidth() / 2, getHeight() / 2 - getWidth() / 2 + 10, getWidth() / 2, getHeight() / 2 - getWidth() / 2 + 20, paint);
            canvas.rotate(6, getWidth() / 2, getHeight() / 2);
        }
        for (int i = 0; i < 360; i += 30) {
            paint.setStrokeWidth(1);
            canvas.drawLine(getWidth() / 2, getHeight() / 2 - getWidth() / 2 + 10, getWidth() / 2, getHeight() / 2 - getWidth() / 2 + 30, paint);
            canvas.rotate(30, getWidth() / 2, getHeight() / 2);
        }
        for (int i = 0; i < 360; i += 90) {
            paint.setStrokeWidth(3);
            canvas.drawLine(getWidth() / 2, getHeight() / 2 - getWidth() / 2 + 10, getWidth() / 2, getHeight() / 2 - getWidth() / 2 + 40, paint);
            canvas.rotate(90, getWidth() / 2, getHeight() / 2);
        }

        //绘制文字
        for (int i = 0; i < textX.size(); i++) {
            int drawInt = i;
            if (drawInt == 0) {
                drawInt = 12;
            }
            canvas.drawText(drawInt + "", textX.get(i) - textPaint.measureText(drawInt + "") / 2, textY.get(i) + 1.5f * textPaint.getFontMetrics().bottom, textPaint);
        }


//        (float)Math.cos(hour)
        //(float)Math.sin(hour)
        //绘制两个针
        canvas.drawLine(cx, cy, cx + (float) Math.sin(hour) * (r - 100), cy - (float) Math.cos(hour) * (r - 100), paint);

        canvas.drawLine(cx, cy, cx + (float) Math.sin(min) * (r - 50), cy - (float) Math.cos(min) * (r - 50), paint);


        canvas.drawLine(cx, cy, cx + (float) Math.sin(second) * (r + 10), cy - (float) Math.cos(second) * (r + 10), paint);
//        canvas.drawLine(cx,cy,cx+(float)Math.sin(hour)*r,cy-(float)Math.cos(hour)*r,paint);


    }
}
