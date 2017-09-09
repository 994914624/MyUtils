package com.example.MyCustomView.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.example.utils.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
public class MyView extends View {

    private TextPaint textPaint = null;
    private Paint paint = null;

    private View view = null;
    private PopupWindow mPopupWindowew = null;
    private MyView imageView = null;
    private EditText editText = null;
    private Button button = null;
    private List<Point> list = null;



    private float x;
    private float y;

    @Override
    public void setX(float x)
    {
        this.x = x;
    }

    @Override
    public void setY(float y)
    {
        this.y = y;
    }

    public MyView(Context context)
    {
        this(context, null);
    }

    public MyView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {

        textPaint = new TextPaint();
        textPaint.setStrokeWidth(5);
        textPaint.setTextSize(50);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);

        list = new ArrayList<>();


        view = LayoutInflater.from(this.getContext()).inflate(R.layout.pupup, null);
        button = (Button) view.findViewById(R.id.button);
        editText = (EditText) view.findViewById(R.id.editText);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {


                Point point = new Point();
                point.setX(x);
                point.setY(y);
                point.setText(editText.getText() + "");


                list.add(point);
                MyView.this.invalidate();
                mPopupWindowew.dismiss();


            }
        });
        mPopupWindowew = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);


    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        for (int i = 0; i < list.size(); i++) {
            canvas.drawText(list.get(i).getText(), list.get(i).getX(), list.get(i).getY(), textPaint);
        }
        //  canvas.drawBitmap(bitmap,0,0,paint);

        //   canvas.saveLayerAlpha(0,0,getWidth(),getHeight(),255,Canvas.ALL_SAVE_FLAG);
        super.onDraw(canvas);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        x = event.getX();
        y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPopupWindowew.showAtLocation(this, Gravity.CENTER, 0, 0);
                break;

            default:
                break;
        }

        return true;
    }

    class Point {
        private float x;
        private float y;
        private String text;

        public void setX(float x)
        {
            this.x = x;
        }

        public void setY(float y)
        {
            this.y = y;
        }

        public void setText(String text)
        {
            this.text = text;
        }

        public float getX()
        {
            return x;
        }

        public float getY()
        {
            return y;
        }

        public String getText()
        {
            return text;
        }
    }
}
