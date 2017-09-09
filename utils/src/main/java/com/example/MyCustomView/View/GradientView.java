package com.example.MyCustomView.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by XaChya on 2016/9/14.
 */
public class GradientView extends View {


    public GradientView(Context context) {
        this(context, null);
    }

    public GradientView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //线性渐变、扫描渐变，放射渐变
    //    可以给一个画笔设置渐变，这样就使得画笔本身的颜色失效，而是用渐变的方式绘制
    //    LinearGradient
    //    SweepGradient
    //    RadialGradient
    private Paint paint = null;
    private TextPaint textPaint = null;
    //线性渐变
    private LinearGradient linearGradient = null;
    //扫描渐变
    private SweepGradient sweepGradient = null;
    //放射渐变
    private RadialGradient radialGradient=null;
    private void init() {
        paint = new Paint();
        textPaint = new TextPaint();
        textPaint.setTextSize(40);
        textPaint.setAntiAlias(true);
        //linearGradient实例化
        //前四个参数是两个点的横纵坐标，用于描述线性渐变的矢量
        //第五、第六个参数是开始的颜色和结束的颜色
        //最后一个参数是指平铺布满整个平面的模式：固定、重复、倒影
        linearGradient = new LinearGradient(0, 0, 50, 50, Color.BLUE, Color.RED, Shader.TileMode.MIRROR);


        sweepGradient=new SweepGradient(150,150,Color.BLUE,Color.RED);

        /** Create a shader that draws a radial gradient given the center and radius.
         @param centerX     The x-coordinate of the center of the radius
         @param centerY     The y-coordinate of the center of the radius
         @param radius      Must be positive. The radius of the circle for this gradient
         @param centerColor The color at the center of the circle.
         @param edgeColor   The color at the edge of the circle.
         @param tileMode    The Shader tiling mode
         */
        radialGradient=new RadialGradient(150,150,50,Color.BLUE,Color.RED, Shader.TileMode.MIRROR);
        //把着色器设置给paint
        paint.setShader(radialGradient);
        textPaint.setShader(radialGradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

//        canvas.drawCircle(getWidth()/2,getHeight()/2,150,paint);
        canvas.drawText("奔跑吧青春！",0,getHeight()/2,textPaint);
    }
}
