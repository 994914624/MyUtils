package com.example.MyCustomView.View;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.example.utils.R;


/**
 * Created by XaChya on 2016/9/14.
 * 生成圆形头像
 *
 */
public class CircleIconView extends View{
    //图片着色器
    private BitmapShader bitmapShader=null;
    public CircleIconView(Context context) {
        this(context,null);
    }

    public CircleIconView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private Paint paint=null;
    private Bitmap bitmap=null;
    private void init(){
        paint=new Paint();

        bitmap= createThumbnail(getResources(), R.mipmap.icon2,50,50);
        //图片着色器的实例化
        bitmapShader=new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        paint.setShader(bitmapShader);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawRect(0,0,bitmap.getWidth(),bitmap.getHeight(),paint);
        int r=bitmap.getWidth()/2>bitmap.getHeight()/2?bitmap.getHeight()/2:bitmap.getWidth()/2;
        canvas.drawCircle(bitmap.getWidth()/2,bitmap.getHeight()/2,r,paint);
    }
    public static Bitmap createThumbnail(Resources res, int id, int width, int height) {
        //options它是一个图片采样的参数
        BitmapFactory.Options options = new BitmapFactory.Options();

        //只采样边界
        options.inJustDecodeBounds = true;

        //把设置好的采样属性应用到具体的采样过程上
        BitmapFactory.decodeResource(res, id, options);

        //取出图片的宽高
        int oldWidth = options.outWidth;
        int oldHeight = options.outHeight;

        //计算宽高的比例值
        int ratioWidth = oldWidth / width;
        int ratioHeight = oldHeight / height;

        //把比例值设置给采样的参数。
        //可能会使得图片发模糊，但是节约内存
//        options.inSampleSize=ratioHeight>ratioWidth?ratioHeight:ratioWidth;
        //不会造成图片模糊，但是消耗内存
        options.inSampleSize = ratioHeight < ratioWidth ? ratioHeight : ratioWidth;

        //为第二次采样做准备
        options.inJustDecodeBounds=false;
        //设置像素点的格式
        options.inPreferredConfig= Bitmap.Config.RGB_565;

        //把第二次采样的结果返回。
        return BitmapFactory.decodeResource(res,id,options);
    }
}
