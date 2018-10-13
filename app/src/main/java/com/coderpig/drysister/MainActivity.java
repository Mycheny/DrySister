package com.coderpig.drysister;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.coderpig.drysister.util.GetData;

public class MainActivity extends AppCompatActivity  {
    private ImageView imgPic;
    private Bitmap bitmap;
    private final static String HTML_URL = "http://www.qq.com";
    private final static String PIC_URL = "http://ww2.sinaimg.cn/large/7a8aed7bgw1evshgr5z3oj20hs0qo0vq.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        imgPic = (ImageView) findViewById(R.id.imgPic);
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
        tv.setText(getResources().getText(R.string.app_name));
        getHtml();
    }

    public void getHtml() {
        System.out.println(Thread.currentThread().getName());
        Thread t1 = new Thread(new MyThread());
        t1.start();
    }

    final Handler myHandler = new Handler() {
        @Override
        //重写handleMessage方法,根据msg中what的值判断是否执行后续操作
        public void handleMessage(Message msg) {
            if(msg.what == 0x123)
            {
                String res = msg.getData().getString("data");
                TextView tv = findViewById(R.id.sample_text);
                tv.setText(res);
                imgPic.setImageBitmap(bitmap);
                Toast.makeText(MainActivity.this, "加载OK!", Toast.LENGTH_SHORT).show();
            }else if(msg.what == 0x124){
                Toast.makeText(MainActivity.this, "加载失败!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    class MyThread implements Runnable{
        @Override
        public void run() {
            GetData gd = new GetData();
            try {
                byte[] data = gd.getImage(PIC_URL);
                String result = rotating(data);
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                String res = gd.getHtml(HTML_URL);
                //构建数据
                Bundle bundle = new Bundle();
                bundle.putString("data" , res);
                // 创建消息
                Message msg = new Message();
                msg.what = 0x123;
                msg.setData(bundle);
                myHandler.sendMessage(msg);
            } catch (Exception e) {
                myHandler.sendEmptyMessage(0x124);
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"-->我是通过实现接口的线程实现方式！");
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public native String rotating(byte[] data);

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
}
