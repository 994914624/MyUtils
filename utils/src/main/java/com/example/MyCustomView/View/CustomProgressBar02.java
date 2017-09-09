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

public class CustomProgressBar02 extends View
{

	
	private Paint mPaint;
	/**
	 * 当前进度
	 */
	private int mProgress=0;
	private int startTime;
	

	public CustomProgressBar02(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public CustomProgressBar02(Context context)
	{
		this(context, null);
	}

	public CustomProgressBar02(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		mPaint = new Paint();
		
		mPaint.setStrokeWidth(5); 
			mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setAntiAlias(true); // 消除锯齿
			mPaint.setStyle(Paint.Style.STROKE); // 设置空心
			startTime=(int) System.currentTimeMillis();
		// 绘图线程
		new Thread()
		{
			public void run()
			{
				
				while (true)
				{
					
					
				
					int nextTime=(int) System.currentTimeMillis();
					int cha=(nextTime-startTime);
					if(cha>=150)
					{
						startTime=nextTime;
						mProgress+=1;
						
					
						if (mProgress >= 12)
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
	
			mPaint.setColor(0xFFBBBBBB); 
			for(int i=0;i<=12;i++)
			{
				canvas.drawArc(oval, 270+27*i+i*3, 3, false, mPaint);
			}
			
			mPaint.setColor(0xFFFFFFFF); 
			for(int i=0;i<=mProgress;i++)
			{
				canvas.drawArc(oval, 270+27*i+i*3, 3, false, mPaint);
			}

		

	}

}
