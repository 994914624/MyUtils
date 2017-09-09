package com.example.MyCustomView.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.utils.R;

public class XuanFuWindowActivity extends AppCompatActivity {

    //View为悬浮的view，现在设置为static
    private static View view = null;
    //要有处理window的对象
    private WindowManager windowManager = null;
    //用于创建view的上下文
    private Context context = null;
    //悬浮窗的样式控制（包裹内容、设置位置）
    private WindowManager.LayoutParams lp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xuanfu();


    }

    private void xuanfu()
    {
        //实例上下文
        context = getApplicationContext();
        //实例windowmanager，它是系统服务，直接get
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        //悬浮窗样式对象
        lp = new WindowManager.LayoutParams();
        //设置成在屏幕悬浮的效果
        //如下写法为api 19以前的写法，新版api要求这个lp.type只能是一个值
        //2003|2006=?     200     011    110     111=7
//        lp.type=WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
//                |WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        lp.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;

        //不获取焦点，不全屏
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN;

        //设置位置
        lp.x =100;
        lp.y = 200;

        //设置一个位置的相对标准
        lp.gravity = Gravity.LEFT | Gravity.TOP;

        //宽高包裹内容
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //悬浮窗一般都是透明效果的，因为怕影响观看桌面的应用
        lp.format = PixelFormat.TRANSPARENT;



        if (view == null) {
            //下面创建要悬浮的view
            view= LayoutInflater.from(context).inflate(R.layout.xuanfu,null,false);
            ImageView mImageView= (ImageView) view.findViewById(R.id.close_xuanfu);
            //设置删除这个悬浮窗
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    windowManager.removeView(view);
                    view = null;
                }
            });
            //我们应该把这个关于view的样式交给windowmanager来进行管理
            windowManager.addView(view, lp);
        } else {
            //如果这个textView已经存在，如何删除它
            windowManager.removeView(view);
            view = null;
        }


        //点击？移动？都怎么办？
        if (view != null) {
            //点击的监听，直接跳转进来
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //这里需要指明intent的Flags
                    Intent intent = new Intent(context, XuanFuWindowActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    );
                    startActivity(intent);
                }
            });

            //移动？
            //用touchlistener来监听，然后实现移动效果
            view.setOnTouchListener(new View.OnTouchListener() {

                private long lastDownTime;
                private int lastX;
                private int lastY;
                //把悬浮窗的属性复制一份
                private WindowManager.LayoutParams mlp=lp;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    boolean ret =false;
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            lastDownTime= System.currentTimeMillis();
                            lastX= (int) event.getRawX();
                            lastY= (int) event.getRawY();
                            ret=true;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            //移动
                            //获取这个event相对于手机屏幕的宽高
                            float x=event.getRawX();
                            float y=event.getRawY();
                            //计算这一次移动的x和y的增量
                            int ix= (int) (x-lastX);
                            int iy= (int) (y-lastY);
                            //把增量设置给lp
                            mlp.x+=ix;
                            mlp.y+=iy;
                            //最后还要把这一次移动的x，y作为lastX设lastY
                            lastX= (int) x;
                            lastY= (int) y;

                            //更新悬浮窗的位置
                            windowManager.updateViewLayout(view,mlp);
                            break;
                        case MotionEvent.ACTION_UP:
                            long time=System.currentTimeMillis();
                            if(time-lastDownTime<300){
                                //触发点击监听
                                view.performClick();
                            }
                            break;

                    }

                    return ret;
                }
            });



        }


    }
}
