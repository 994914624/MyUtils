package com.example.MyCustomView.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by XaChya on 2016/9/14.
 */
public class ZheXianView extends View {
    public void setList(List<Integer> list) {
        this.list = list;

        invalidate();
    }

    public ZheXianView(Context context) {
        this(context, null);
    }

    public ZheXianView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZheXianView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //初始化画笔
        paint = new Paint();
        paint.setAntiAlias(true);
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(10);
        //初始化设置折线统计图的参数，包括行列间距，预留位置
        intr = 30;
        intY = 50;
        intX = 50;
//        intl=50;它最好用x轴平分N份。
        lineC = 0xffff0000;
        title="一周天气变化";

        list=new ArrayList<Integer>();
        Random random=new Random();
        for (int i = 0; i < 7; i++) {
            list.add(random.nextInt(35));
        }


    }

    //这是一个数据源，决定了绘制的折线的形状。
    private List<Integer> list = null;
    //画折线，坐标轴，圆的画笔。
    private Paint paint = null;
    //绘制文字的画笔。
    private TextPaint textPaint = null;

    //定义行间距
    private int intr = 0;
    //定义列间距
    private int intl = 0;

    //预留Y轴提示信息的宽度
    private int intY = 0;

    //预留X轴提示信息的高度
    private int intX = 0;

    //折线的颜色
    private int lineC = 0;


    //表格的标题
    private String title = null;


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取到左上右下的内边距，以便使用
        int pl = getPaddingLeft();
        int pr = getPaddingRight();
        int pt = getPaddingTop();
        int pb = getPaddingBottom();
        //获取view的宽高
        int h = getHeight();
        int w = getWidth();

        //绘制X轴和它的小伙伴们
        //把画笔设置为坐标纸的颜色
        paint.setColor(0xffdddddd);
        //画一组横线
        for (int i = 0; i < 8; i++) {
            canvas.drawLine(pl + intY,
                    h - pb - intX - i * intr,
                    w - pr,
                    h - pb - intX - i * intr,
                    paint);
            //绘制Y轴的提示信息
            if (i > 0) {
                canvas.drawText("" + i * 5, pl + intY - textPaint.measureText("" + i * 5) - 5,
                        h - pb - intX - i * intr + 1.5f * textPaint.getFontMetrics().bottom,
                        textPaint);

            }

        }

        //把画笔设置为坐标轴的颜色
        paint.setColor(0xff000000);
        //绘制X轴
        canvas.drawLine(pl + intY,
                h - pb - intX,
                w - pr,
                h - pb - intX,
                paint);

        //绘制Y轴和它的小伙伴们
        //把画笔设置为坐标纸的颜色
        paint.setColor(0xffdddddd);
        intl = (w - pl - pr - intY) / 8;
        //画一组竖线
        for (int i = 0; i < 8; i++) {
            canvas.drawLine(
                    pl + intY + i * intl,
                    h - pb - intX,
                    pl + intY + i * intl,
                    h - pb - intX - 7.5f * intr, paint);
            //绘制X轴的提示信息
            if (i > 0) {
                canvas.drawText(getNum(i),
                        pl + intY + i * intl-0.5f*textPaint.measureText(getNum(i)),
                        h-pb-intX+textPaint.getFontMetrics().bottom*4.0f,
                        textPaint);
            }
        }
        //把画笔设置为坐标轴的颜色
        paint.setColor(0xff000000);
        //绘制Y轴
        canvas.drawLine(
                pl + intY,
                h - pb - intX,
                pl + intY,
                h - pb - intX - 7.5f * intr, paint);

        //绘制0
        canvas.drawText("0",
                pl + intY - textPaint.measureText("0") - 5,
                h-pb-intX+textPaint.getFontMetrics().bottom*3.0f,
                textPaint);
        //绘制折线统计图的标题
        if(title!=null){
            canvas.drawText(title,
                    (w-pl-pr)/2+pl-0.5f*textPaint.measureText(title),
                    h - pb - intX - 8f * intr,
                    textPaint);
        }
        //至此，坐标纸绘制完毕。

        //下面开始绘制折线
        if(list!=null){
            //画笔设置为折线的颜色
            paint.setColor(lineC);
            //绘制折线
            for (int i = 0; i < list.size()-1; i++) {
                canvas.drawLine(
                        pl+intY+(i+1)*intl,
                        h-pb-intX-list.get(i)*6,
                        pl+intY+(i+2)*intl,
                        h-pb-intX-list.get(i+1)*6,
                        paint
                );
            }
            //画点,采用三个圆圈的视觉效果
            for (int i = 0; i < list.size(); i++) {
                canvas.drawCircle(pl+intY+(i+1)*intl,
                        h-pb-intX-list.get(i)*6,
                        6,paint);
            }
            paint.setColor(Color.WHITE);
            for (int i = 0; i < list.size(); i++) {
                canvas.drawCircle(pl+intY+(i+1)*intl,
                        h-pb-intX-list.get(i)*6,
                        4,paint);
            }
            paint.setColor(lineC);
            for (int i = 0; i < list.size(); i++) {
                canvas.drawCircle(pl+intY+(i+1)*intl,
                        h-pb-intX-list.get(i)*6,
                        2,paint);
            }

            
        }

    }

    private String getNum(int i) {
        //i    0~6
        //实际传入1~7
        i--;
        String str = "一二三四五六日";
        return "周"+str.charAt(i);
    }
}
