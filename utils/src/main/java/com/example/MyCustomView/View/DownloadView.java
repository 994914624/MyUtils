package com.example.MyCustomView.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by XaChya on 2016/9/13.
 * 带进度条的，下载百分数的安装按钮，使用见DownloadActivity
 */
public class DownloadView extends View {
    private OnCircleButtonClickListener onCircleButtonClickListener=null;

    public void setOnCircleButtonClickListener(OnCircleButtonClickListener onCircleButtonClickListener) {
        this.onCircleButtonClickListener = onCircleButtonClickListener;
    }



    private int downloadState = STOP;

    public int getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(int downloadState) {
        this.downloadState = downloadState;
        invalidate();
    }

   public static final int BEGIN = 0;
   public static final int PAUSE = 1;
   public static final int DOWNLOADING = 2;
   public static final int STOP = 3;

    private int progress = 68;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public DownloadView(Context context) {
        this(context, null);
    }

    public DownloadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DownloadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Paint paint = null;
    private TextPaint textPaint = null;
    private RectF rectF = null;

    //绘制三角形（箭头的一部分）用
    private Path path = null;

    private void init() {
        paint = new Paint();
        textPaint = new TextPaint();
        paint.setAntiAlias(true);
        textPaint.setAntiAlias(true);
        rectF = new RectF();
        path = new Path();
    }


    int cx;
    int cy;
    int r;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        cx = width / 2;
        cy = height / 2;
        r = cx > cy ? cy - 1 : cx - 1;
        //绘制圆形，实心圆，圆弧
        //步骤：
        //绘制大圆
        //绘制扇形
        //绘制小圆
        //绘制文字（箭头）(判断状态)


        //1、绘制大圆
        //颜色是灰色
        paint.setColor(Color.GRAY);
        canvas.drawCircle(cx, cy, r / 5 * 4, paint);

        //2、绘制扇形，半径和大圆一样，开始的角度是-90，结束的角度是360*progress/100
        rectF.set(cx - r / 5 * 4, cy - r / 5 * 4, cx + r / 5 * 4, cy + r / 5 * 4);
        paint.setColor(Color.GREEN);
        canvas.drawArc(rectF, -90, 360 * progress / 100, true, paint);

        //3、绘制小圆
        paint.setColor(Color.BLUE);
        canvas.drawCircle(cx, cy, r / 5 * 3, paint);

        //矩形的边长
        int a = r / 25 * 8;
        switch (downloadState) {
            case BEGIN:
                //绘制箭头，我们认为箭头是两部分，一部分是上方的矩形，另一部分是下方的三角
                //绘制矩形

                paint.setColor(Color.WHITE);
                canvas.drawRect(cx - a / 2, cy - a, cx + a / 2, cy, paint);
                //绘制三角形，要用到path
                path.moveTo(cx - a, cy);
                path.lineTo(cx + a, cy);
                path.lineTo(cx, cy + a);
                path.lineTo(cx - a, cy);
                canvas.drawPath(path, paint);

                break;
            case PAUSE:
//                canvas.drawRect(cx-a/2,cy-a,cx+a/2,cy,paint);
                paint.setColor(Color.WHITE);

                canvas.drawRect(cx - a / 2, cy - a, cx - a / 6, cy + a, paint);
                canvas.drawRect(cx + a / 6, cy - a, cx + a / 2, cy + a, paint);

                break;
            case DOWNLOADING:
                if(progress>100){
                    textPaint.setColor(Color.WHITE);
                    textPaint.setTextSize(a);
                    canvas.drawText("" + 100 + "%", cx - 0.5f * textPaint.measureText("" + 100 + "%"), cy + 1.5f * textPaint.getFontMetrics().bottom, textPaint);

                }else {
                    textPaint.setColor(Color.WHITE);
                    textPaint.setTextSize(a);
                    canvas.drawText("" + progress + "%", cx - 0.5f * textPaint.measureText("" + progress + "%"), cy + 1.5f * textPaint.getFontMetrics().bottom, textPaint);
                }
                break;
            case STOP:
                textPaint.setColor(Color.WHITE);
                textPaint.setTextSize(a);
                canvas.drawText("安装",cx-0.5f*textPaint.measureText("安装"),cy+1.5f*textPaint.getFontMetrics().bottom,textPaint);



                break;

        }


    }
    public interface OnCircleButtonClickListener {
        public void onCircleButtonClick(View view);
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
}
