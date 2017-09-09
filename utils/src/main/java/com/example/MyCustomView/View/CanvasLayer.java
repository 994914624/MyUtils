package com.example.MyCustomView.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by XaChya on 2016/9/12.
 */
public class CanvasLayer extends ImageView {
    private String str = "ǧ�滥��";

    public CanvasLayer(Context context) {
        this(context, null);
    }

    public CanvasLayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CanvasLayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private TextPaint textPaint = null;
    private Paint paint = null;

    private void init() {
        paint = new Paint();
        paint.setColor(Color.GREEN);
        textPaint = new TextPaint();
        textPaint.setColor(0xffdddddd);
        //1dp=density px
        //50dp=50*density  px

        int dp = (int) (getResources().getDisplayMetrics().density * 30);
        textPaint.setTextSize(dp);
    }

    //���canvasÿһ����onDraw�г��ֶ���ÿ��ת��
    //canvas������onDraw�е�rotate���ۼӵ�
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //ǰ�ĸ�������˵�����ͼ���ж��
        //͸����0~255��127λ��͸��,��ͼ��ȫ������
        //��һ��ִ���꣬�㻭��ʱ�����ͼ���ϻ��ˡ�
        canvas.saveLayerAlpha(0, 0, getWidth(), getHeight(), 127, Canvas.ALL_SAVE_FLAG);
        canvas.rotate(-60);

//        canvas.drawCircle(100,100,3000,paint);
        int l= (int) Math.sqrt(getWidth()*getWidth()+getHeight()*getHeight());

        int lineHeight = (int) (textPaint.getFontMetrics().bottom * 5);
        int lineCount = (int) (2*l / (lineHeight));
        for (int i = 0; i <=lineCount; i++) {
            int colWidth = (int) textPaint.measureText(str + "   ");
            int colCount = 2*l / colWidth;
            for (int j = 0; j <=colCount; j++) {
                canvas.drawText(str,
                        -l+colWidth * j,
                        -l+i * lineHeight, textPaint);
            }

        }


    }
}
