package com.example.MyCustomView.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by XaChya on 2016/9/12.
 */
public class TxtView extends EditText {
    public static final int CACHE = 0;
    public static final int FILES = 1;
    public void save(){
        otherWriter("mytxt",(this.getText()+"").getBytes(),this.getContext(),CACHE);
    }
    public void load(){
        byte [] data=otherReader("mytxt",this.getContext(),CACHE);
        String str=new String(data);
        this.setText(str);
    }


    public TxtView(Context context) {
        this(context, null);
    }

    public TxtView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TxtView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Paint paint = null;

    private void init() {
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画编辑的内容就是super.onDraw
        super.onDraw(canvas);
        //每一行的下划线
        //在edittext中可以使用getLineCount这个方法获取内容一共有多少行。
        //当你只写了一行时，getLineCount的返回值就是1.
        //但是你此时要在整个记事本中全都画满下划线。
        //矛盾的是，你画满需要计算一共要画多少条，你只是把view高度除以行高度=n。
        //画多少条，是固定的。
        //当你写的内容超过了屏幕范围，getLineCount>n。再写的内容就没有下划线了。
        //所以，你要去判断写的内容的行数，和屏幕能容纳的行数的大小关系，取较大的一个
        //获取行的高度在edittext里用getLineHeight

        //获取一个屏幕能容纳的最多行数
        int lineCount = (getHeight() - getPaddingTop() - getPaddingBottom()) / getLineHeight();
        int maxLineCount = lineCount > getLineCount() ? lineCount : getLineCount();
        //绘制下滑线
        for (int i = 0; i < maxLineCount; i++) {
            canvas.drawLine(getPaddingLeft(), getPaddingTop() + (i + 1) * getLineHeight(), getWidth() - getPaddingRight(), getPaddingTop() + (i + 1) * getLineHeight(), paint);
        }

    }
    // 第二种文件保存方式
    public static void otherWriter(String fileName, byte[] content,
                                   Context context, int type) {
        // 判断文件名
        if (fileName == null || "".equals(fileName)) {
            return;
        }
        FileOutputStream outputStream = null;
        File file = null;
        switch (type) {
            case CACHE:
                file = new File(context.getCacheDir(), fileName);
                break;
            case FILES:
                file = new File(context.getFilesDir(), fileName);
                break;

            default:
                break;
        }
        // 对文件进行操作
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(content);
            outputStream.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    // 第二种读取内部存储中的文件的方法
    public static byte[] otherReader(String fileName, Context context, int type) {
        // 判断文件名
        if (fileName == null || "".equals(fileName)) {
            return null;
        }
        FileInputStream inputStream = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        File file = null;
        switch (type) {
            case CACHE:
                file = new File(context.getCacheDir(), fileName);
                break;
            case FILES:
                file = new File(context.getFilesDir(), fileName);
                break;

            default:
                break;
        }
        if(!file.exists()){
            return "".getBytes();
        }
        // 操作文件
        try {
            inputStream = new FileInputStream(file);
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
                baos.flush();
            }
            return baos.toByteArray();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return null;
    }



}
