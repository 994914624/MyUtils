package com.example.MyCustomView.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.utils.R;

/**
 * Created by XaChya on 2016/9/12.
 * 刮刮乐，
 */
public class GuaGuaLeView extends View{
    private BitmapShader bitmapShader=null;
    private Bitmap bitmap=null;
    private Bitmap bitmap2=null;
    public GuaGuaLeView(Context context) {
        this(context,null);
    }

    public GuaGuaLeView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GuaGuaLeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }
    private Paint paint=null;
    private Paint paint2=null;
    private Path path=null;
    private void init(){
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(80);
        paint2=new Paint();
        paint2.setAntiAlias(true);
        paint2.setStrokeWidth(10);
        //设置笔的样式是描边
        paint.setStyle(Paint.Style.STROKE);
        path=new Path();
        bitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.icon3);
        bitmap2= BitmapFactory.decodeResource(getResources(), R.mipmap.icon2);
        bitmapShader=new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        paint.setShader(bitmapShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap2,0,0,paint2);
        super.onDraw(canvas);
        //绘制路径
        canvas.drawPath(path,paint);


    }

    float fromX=0;
    float fromY=0;

    //路径要随着手的移动来绘制

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x=event.getX();
        float y=event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x,y);
                fromX=x;
                fromY=y;
                break;
            case MotionEvent.ACTION_MOVE:
                if(Math.abs(x-fromX)>0||Math.abs(y-fromY)>0){
                    path.lineTo(x,y);
                    fromX=x;
                    fromY=y;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        invalidate();
        return true;
    }
}
