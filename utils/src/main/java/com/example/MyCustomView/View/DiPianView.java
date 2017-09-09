package com.example.MyCustomView.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.utils.R;


/**
 * Created by XaChya on 2016/9/14.
 */
public class DiPianView extends View{
    private Paint paint=null;
    private Bitmap bitmap=null;
    public DiPianView(Context context) {
        this(context,null);
    }

    public DiPianView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DiPianView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        //一个显示佐助的bitmap
        bitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.icon2);

        //调用底片效果处理的方法
        bitmap=swapPixels(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap,0,0,paint);
    }

    //处理像素点的方法,传入的bitmap的底片会作为返回值传出。
    private Bitmap swapPixels(Bitmap bitmap){
        //这个数组用于保存所有的bitmap上的像素点。
        int [] pixels=new int[bitmap.getWidth()*bitmap.getHeight()];
        //第一个参数是用于接收bitmap的像素数据的数组
//        @param pixels   The array to receive the bitmap's colors
//        * @param offset   The first index to write into pixels[]
//        * @param stride   The number of entries in pixels[] to skip between
//        *                 rows (must be >= bitmap's width). Can be negative.
//        * @param x        The x coordinate of the first pixel to read from
//        *                 the bitmap
//        * @param y        The y coordinate of the first pixel to read from
//        *                 the bitmap
//        * @param width    The number of pixels to read from each row
//        * @param height   The number of rows to read
        bitmap.getPixels(pixels,0,bitmap.getWidth(),0,0,bitmap.getWidth(),bitmap.getHeight());
      //从此开始bitmap的像素数据在pixels数组中
        //处理像素
        for (int i = 0; i < pixels.length; i++) {
            //获取到每一个像素中表示argb的四个部分
            int a= Color.alpha(pixels[i]);
            int r=Color.red(pixels[i]);
            int g=Color.green(pixels[i]);
            int b=Color.blue(pixels[i]);

            //颜色反转，实现底片效果
            r=255-r;
            g=255-g;
            b=255-b;

            //把计算结果合成回来
            pixels[i]=Color.argb(a,r,g,b);

        }
        Bitmap ret=Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        //把刚才处理的结果交给ret
        ret.setPixels(pixels,0,bitmap.getWidth(),0,0,bitmap.getWidth(),bitmap.getHeight());
        return ret;
    }
}
