package com.example.MyCustomView.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by XaChya on 2016/9/12.
 */
public class PathView extends View{
    public PathView(Context context) {
        this(context,null);
    }

    public PathView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private Paint paint=null;
    private Path path=null;
    private void init(){
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10);
        //设置笔的样式是描边
        paint.setStyle(Paint.Style.STROKE);
        path=new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {
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
