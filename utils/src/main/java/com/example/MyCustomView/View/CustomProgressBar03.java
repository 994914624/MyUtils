package com.example.MyCustomView.View;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

public class CustomProgressBar03 extends View
{

	private Paint mPaint;
	/**
	 * 当前进度
	 */
	private int mProgress=0;

	
	private int startTime;

	

	public CustomProgressBar03(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public CustomProgressBar03(Context context)
	{
		this(context, null);
	}

	public CustomProgressBar03(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		mPaint = new Paint();
		
		mPaint.setStrokeWidth(5); 
			mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setAntiAlias(true); // 消除锯齿
			mPaint.setStyle(Paint.Style.STROKE); // 设置空心
			startTime=(int) System.currentTimeMillis();
			
			new Thread()
			{
				public void run() 
				{
					
					while(true)
					{
						
						int nextTime=(int) System.currentTimeMillis();
						int cha=(nextTime-startTime);
						if(cha>=2)
						{
							startTime=nextTime;
							mProgress+=3;
							
						
							if (mProgress >= 360)
							{
								mProgress = 0;
								
							}
							postInvalidate();
						}
					}
					
					
					
				};
			}.start();
			
			
			
			
			
			
			

	}

	@Override
	protected void onDraw(Canvas canvas)
	{

		int centre = getWidth() / 2; // 获取圆心的x坐标
	
		int radius = centre - 6 / 2;
		
		RectF oval = new RectF(centre - radius, centre - radius, centre
				+ radius, centre + radius); // 用于定义的圆弧的形状和大小的界限

			mPaint.setColor(0xFFCCCCCC); 
			canvas.drawCircle(centre, centre, radius, mPaint); // 画出圆环

			mPaint.setColor(0xFFFFFFFF); 
			
			
				canvas.drawArc(oval, 270+mProgress, 90, false, mPaint);


	}

}
