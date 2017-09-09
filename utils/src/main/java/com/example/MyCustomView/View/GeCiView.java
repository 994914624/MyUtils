package com.example.MyCustomView.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by XaChya on 2016/9/14.
 *
 * 歌词view的使用要注意，歌词不能太长，因为如果换行，是整列颜色变化，歌词长，就再来一个歌词view
 */
public class GeCiView extends TextView {

    private int progress = 37;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    private int backGroundTextColor = Color.GREEN;
    private int progressTextColor = Color.BLUE;

    public GeCiView(Context context) {
        this(context, null);
    }

    public GeCiView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GeCiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //获取到textView的字节码文件,类对象。
        clazz = TextView.class;
    }

    private Class<TextView> clazz = null;

    @Override
    protected void onDraw(Canvas canvas) {
        //画文字,我应该画两次文字，第一次画全部，第二次画部分
        //也就是说调用super.onDraw两次。

        //分析：
        //肯定父类的onDraw有画笔去画文字，那么我们只需要修改画笔的颜色就可以了
        //肯定setTextColor会修改画文字的画笔的颜色。但是我们却不能在onDraw中使用。
        //但是我们还想修改画笔的颜色，怎么办？
        // 反射。
        //我们找到TextView类中用于绘制的画笔，把它的颜色修改掉
        //mCurTextColor这个就是TextView绘制文本的颜色的决定值。
        //从clazz中取出这个mCurTextColor
        try {
            //获取mCurTextColor
            Field field = clazz.getDeclaredField("mCurTextColor");
            //修改mCurTextColor的访问权限
            field.setAccessible(true);
            //修改它的值
            field.set((TextView) this, backGroundTextColor);


        } catch (Exception e) {
            e.printStackTrace();
        }
        //进行第一次绘制
        super.onDraw(canvas);
        //开启一个层，准备第二次绘制,1.0f*progress/100*getWidth(),使得layer的右边界取决于进度progress
        canvas.saveLayerAlpha(0, 0, 1.0f * progress / 100 * getWidth(), getHeight(), 255, Canvas.ALL_SAVE_FLAG);

        try {
            //获取mCurTextColor
            Field field = clazz.getDeclaredField("mCurTextColor");
            //修改mCurTextColor的访问权限
            field.setAccessible(true);
            //修改它的值
            field.set((TextView) this, progressTextColor);


        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDraw(canvas);
    }
}
