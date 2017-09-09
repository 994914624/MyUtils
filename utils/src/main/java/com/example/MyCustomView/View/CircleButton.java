package com.example.MyCustomView.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by XaChya on 2016/9/12.
 *
 * 圆形的，可点击的按钮，只有点击在圆内部才有效
 */
public class CircleButton extends View{
    private OnCircleButtonClickListener onCircleButtonClickListener=null;

    public void setOnCircleButtonClickListener(OnCircleButtonClickListener onCircleButtonClickListener) {
        this.onCircleButtonClickListener = onCircleButtonClickListener;
    }

    public CircleButton(Context context) {
        this(context,null);
    }

    public CircleButton(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    //用于绘制圆形。
    private Paint paint=null;

    private void init(){
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xff88ff88);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private float cx=0;
    private float cy=0;
    private float r=0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        cx=getWidth()/2;
        cy=getHeight()/2;
        r=getWidth()/2-paint.getStrokeWidth()-10;
        canvas.drawCircle(cx,cy,r,paint);
    }


    private boolean isDown=false;
    //手指触摸的监听
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x=event.getX();
        float y=event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //范围正确
                if(inCircle(x,y)){
                    isDown=true;
                }

                break;
            case MotionEvent.ACTION_MOVE:

                //可以使用move的时候判断坐标来控制用户手指挪动时的交互性。
                if(inCircle(x,y)){
                    paint.setColor(0xff88ff88);

                }else{
                    paint.setColor(0xff00ff00);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //范围正确，isDown也是true，就触发onCircleButtonClickListener.onCircleButtonClick方法
                if(inCircle(x,y)){
                    if(isDown){
                        if(onCircleButtonClickListener!=null) {
                            onCircleButtonClickListener.onCircleButtonClick(this);
                        }
                    }
                }
                //把isDown还原，给下次点击的时候使用。
                isDown=false;
                break;
        }

        return true;
    }
    private boolean inCircle(float x,float y){
        if((cx-x)*(cx-x)+(cy-y)*(cy-y)<=r*r){
            return true;
        }
        return false;
    }


    public interface OnCircleButtonClickListener {
        public void onCircleButtonClick(View view);
    }

}

