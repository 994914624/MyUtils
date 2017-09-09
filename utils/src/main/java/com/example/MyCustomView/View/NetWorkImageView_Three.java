package com.example.MyCustomView.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.LruCache;
import android.widget.ImageView;

import com.example.myutils.MyLogUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by XaChya on 2016/9/10.
 *
 * netWorkImageView直接setUrl()，传入一个Url就能下载图片并在SD卡保存一份，并在内存保存一份，
 * 二次采样+三级缓存，，，，
 *
 */
public class NetWorkImageView_Three extends ImageView {


    private MyLruCache myLruCache = null;

    public NetWorkImageView_Three(Context context) {
        this(context, null);
    }

    // 我们封装方法，以后画扇形就用这个。
    private float progress;
    public NetWorkImageView_Three(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetWorkImageView_Three(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Paint paint=null;
    private RectF rectF=null;



    private void init(){
        paint=new Paint();
        paint.setColor(Color.GRAY);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        //这个初始化，可以把初始化？
        //初始化是可以，但是你获取getWidth和getHeight的结果如果在这里获取就是0.
        //对于一个view而言，它什么时候知道自己多大？那是触发它自己测量自己的方法(onMeasure)的时候，以后才知道
        //当一个view显示出来了，它一定知道自己多大。
        //也就是说，在onDraw中使用getWidth和getHeight就没有问题。
        //因此，这个扇形的四个属性不能在这里设置getWidth()/4,getHeight()/4,getWidth()*3/4,getHeight()*3/4
        rectF=new RectF();
    }


    private String url;
    private int width=0;
    private int height=0;

    //用来保存网络访问的结果的。
    private Bitmap netWorkBitmap;

    public String getUrl() {
        return url;
    }

    public NetWorkImageView_Three setUrl(String url) {
        this.url = url;

        int memoryAmount = (int) (Runtime.getRuntime().maxMemory() / 1024 / 8);
        if (myLruCache == null) {
            //一般而言我们把最大内存的1/8作为图片处理空间
            myLruCache = new MyLruCache(memoryAmount);
        }
        this.measure(0,0);
        width=this.getMeasuredWidth();
        height=this.getMeasuredHeight();
        loadImage(url);

        return this;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            NetWorkImageView_Three.this.setImageBitmap(netWorkBitmap);
        }
    };

    public void loadImage(final String url)
    {
        Bitmap bitmap = null;
        //先在内存中寻找
        if (myLruCache != null) {
            //去myLruCache中寻找，用url作为key，也就是说，我们存入内存的时候也要用url作为key
            bitmap = myLruCache.get(url);
        }
        if (bitmap != null) {
            //找到了，就更新ui
            this.setImageBitmap(bitmap);
            MyLogUtils.i("图片来内存");
        } else {
            //内存中内找不到，去sd卡中寻找
            String fileName = getMD5(url);

            File file = new File(getSDCardPublicDir(Environment.DIRECTORY_DOWNLOADS) + File.separator + fileName);
            if (file.exists()) {
                //从sd卡上找图片，加载到内存中用二次采样的方式

                //触发测量
                this.measure(0, 0);
                bitmap = createThumbnail(file.getAbsolutePath(), this.getMeasuredWidth(), this.getMeasuredHeight());
            }
            //判断bitmap是否在sd卡上加载成功
            if (bitmap != null) {
                //在sd卡上加载了bitmap
                //更新ui
                MyLogUtils.i("图片来SD卡"+url);
                this.setImageBitmap(bitmap);
                //放入内存中一份，留着下次用。
                myLruCache.put(url, bitmap);
            } else {
                //在sd卡上没加载成功
                //就去请求网络


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (url != null && !"".equals(url)) {
                            byte[] data = loadByteFromURL(url);
                            netWorkBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            //通知更新ui，
                            //invalidate();postInvalidate();
                            // 这两方法都是通知更新ui，导致onDraw重新执行的方法
                            //invalidate这个方法要求必须在主线程使用。
                            //postInvalidate这个方法随意，主线程还是子线程都能使用。
                            //invalidate速度快，效率高
                            //postInvalidate速度慢，效率低
                            //所以在实际的开发中，主线程用invalidate，子线程用postInvalidate
                            //postInvalidate();

                            //手动更新ui
                            handler.sendEmptyMessage(1);



                            if (data != null) {
                                String fileName = getMD5(url);

                                //保存到sd卡上一份
                                File file2 = new File(getSDCardPublicDir(Environment.DIRECTORY_DOWNLOADS));
                                if(!file2.exists())
                                {

                                    boolean aa= file2.mkdir();
                                    MyLogUtils.i("创建这个文件夹："+aa);
                                }else
                                {
                                    MyLogUtils.i("Downlowad这个文件夹存在");
                                }
                                boolean isok=saveFileToSDCardPublicDir(data, Environment.DIRECTORY_DOWNLOADS, fileName);

                                MyLogUtils.i("是否保存到SD卡成功："+isok);
                                File file = new File(getSDCardPublicDir(Environment.DIRECTORY_DOWNLOADS) + File.separator + fileName);
                                if(!file.exists()){
                                    MyLogUtils.i("文件不存在");
                                }else {
                                    //获取保存到sd卡上的bitmap对象

                                    Bitmap bitmap = createThumbnail(file.getAbsolutePath(), width, height);
                                    //保存到内存一份
                                    MyLogUtils.i(":" +url);
                                    MyLogUtils.i(":" + bitmap.toString());

                                    myLruCache.put(url, bitmap);
                                }
                            }


                        } else {
//                    Log.i(),url为空
                        }



                    }
                }).start();

            }

        }
    }





    @Override
    protected void onDraw(Canvas canvas) {

        MyLogUtils.i( "执行了");
        //弃坑,在onDraw方法中不要实例对象，今天又知道，不要调用可以导致调用ondraw方法的方法。
//        if (netWorkBitmap != null) {
//            //正成情况下，设置imagebitmap是否会导致imageView重新绘制调用onDraw？
//            //也就是setimageBitmap会导致调用onDraw
//            //setimageBitmap本身就会调用invalidate，导致onDraw重新执行，
//            //这样你才能看见源生的imageview更新ui
//            this.setImageBitmap(netWorkBitmap);
//        }
        super.onDraw(canvas);
        if(progress!=1.0f){
            //rectf这个对象，可以不在初始化的时候指定区域，而在之后用set方法来指定。
            rectF.set(getWidth()/4,getHeight()/4,getWidth()*3/4,getHeight()*3/4);
            //画扇形
            canvas.drawArc(rectF,-90,progress*360,true,paint);
        }

    }



    private byte[] streamToByte(InputStream is,int length) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int c = 0;
        int contentLength=0;
        byte[] buffer = new byte[8*1024];
        try {
            while ((c = is.read(buffer)) != -1) {


                //每一次读取数据，都要更新扇形
                //在这里就要计算扇形
                contentLength+=c;
                progress=1.0f*contentLength/length;
                //更新ui
                postInvalidate();
                baos.write(buffer, 0, c);
                baos.flush();

            }

            return baos.toByteArray();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }

            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }


        }


        return null;
    }


    class MyLruCache extends LruCache<String, Bitmap> {

        /**
         * @param maxSize for caches that do not override {@link #sizeOf}, this is
         *                the maximum number of entries in the cache. For all other caches,
         *                this is the maximum sum of the sizes of the entries in this cache.
         *                我们说lrucache是一个容器，这个参数描述这个容器有多大。
         */

        public MyLruCache(int maxSize) {
            super(maxSize);
        }

        //这个方法通过key来获取每一个对象的大小（计算时使用的大小）
        @Override
        protected int sizeOf(String key, Bitmap value) {
            //两个参数，key，value，就和map一样
            //lrucache使用键值对的形式来处理具体的算法
            //这个lrucache计算大小以k为单位
            return value.getByteCount() / 1024;
        }

        //这个方法在lrucache开始淘汰某个对象时回调
        //有东西移除分为两种情况，
        //1、既然是以键值对的形式存储，就可能key相同存入，key如果已经存在，就把之前的value移除
        //2、当lrucache满了，再往里放入key，value，虽然没有key重复，但是一定要有对象出来，给新的
        //进去的对象提供空间。也就是要去判断哪个是最不可能近期被使用的对象，把它移除。
        //当evicted为真的时候对应第二种情况
        @Override
        protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
            super.entryRemoved(evicted, key, oldValue, newValue);
        }
    }

    private static final char hexDigsits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    //封装的，获取md5的方法
    public static String getMD5(String inStr) {
        //加密需要的字节数组，于是首先拿到内容的字节数组
        byte[] inStrBytes = inStr.getBytes();

        //消息摘要对象,指定为MD5方式
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            //把摘要对象中的数据设置为我们的内容
            messageDigest.update(inStrBytes);
            //拿到MD5算法的结果
            byte[] messageDbytes = messageDigest.digest();
            //我们把结果转化为16进制的格式
            char[] str = new char[messageDbytes.length * 2];
            //循环是使用的角标
            int k = 0;
            for (int i = 0; i < messageDbytes.length; i++) {
                byte temp = messageDbytes[i];
                //把一个字节分裂成两个部分，分别转化为十六进制的字符
                str[k++] = hexDigsits[temp >>> 4 & 0xf];
                str[k++] = hexDigsits[temp & 0xf];
            }
            return new String(str);


        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return null;
    }

    // 检测sd卡是否挂载
    public static boolean isSDCardMounted() {
        // 如果拓展的存储器处于MEDIA_MOUNTED状态，我们即认为sd卡挂载了
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    // 向sd卡的公有目录存数据
    public static boolean saveFileToSDCardPublicDir(byte[] data, String type,
                                                    String fileName) {
        if (isSDCardMounted()) {
            BufferedOutputStream bos = null;
            File file = Environment.getExternalStoragePublicDirectory(type);
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(
                        file, fileName)));
                bos.write(data);
                bos.flush();
                return true;

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    if (bos != null) {
                        bos.close();
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;
        } else {
            return false;
        }
    }

    // 获取sd卡的公有目录,参数为Environment类里的常量
    public static String getSDCardPublicDir(String type) {
        return Environment.getExternalStoragePublicDirectory(type).toString();
    }

    public static byte[] loadByteFromURL(String url) {
        HttpURLConnection httpConn = null;
        BufferedInputStream bis = null;
        try {
            URL urlObj = new URL(url);
            httpConn = (HttpURLConnection) urlObj.openConnection();
            httpConn.setRequestMethod("GET");

            httpConn.setDoInput(true);
            httpConn.setConnectTimeout(5000);
            httpConn.connect();
            if (httpConn.getResponseCode() == 200) {
                bis = new BufferedInputStream(httpConn.getInputStream());
                return streamToByte(bis);
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return null;
    }

    public static byte[] streamToByte(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int c = 0;
        byte[] buffer = new byte[8 * 1024];
        try {
            while ((c = is.read(buffer)) != -1) {
                baos.write(buffer, 0, c);
                baos.flush();

            }

            return baos.toByteArray();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }

            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }


        }


        return null;
    }
    public static Bitmap createThumbnail(String filePath, int width, int height) {
        //options它是一个图片采样的参数
        BitmapFactory.Options options = new BitmapFactory.Options();

        //只采样边界
        options.inJustDecodeBounds = true;


        //把设置好的采样属性应用到具体的采样过程上
        BitmapFactory.decodeFile(filePath,options);

        //取出图片的宽高
        int oldWidth = options.outWidth;
        int oldHeight = options.outHeight;

        //计算宽高的比例值
        int ratioWidth = oldWidth / width;
        int ratioHeight = oldHeight / height;

        //把比例值设置给采样的参数。
        //可能会使得图片发模糊，但是节约内存
//        options.inSampleSize=ratioHeight>ratioWidth?ratioHeight:ratioWidth;
        //不会造成图片模糊，但是消耗内存
        options.inSampleSize = ratioHeight < ratioWidth ? ratioHeight : ratioWidth;

        //为第二次采样做准备
        options.inJustDecodeBounds=false;
        //设置像素点的格式
        options.inPreferredConfig= Bitmap.Config.RGB_565;

        //把第二次采样的结果返回。
        return  BitmapFactory.decodeFile(filePath,options);
    }



}
