package com.example.administrator.message_b;

import android.os.*;
import android.os.Message;
import android.webkit.WebView;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2017/1/10.
 */
public class client extends Thread {
    String url;
    Handler handler;

public client(String url, Handler handler){
    this.url=url;
    this.handler=handler;
}
    @Override
    public void run() {
        try {

            URL httpurl=new URL(url);
            try {
                HttpURLConnection connection=(HttpURLConnection) httpurl.openConnection();
                connection.setReadTimeout(5000);
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = inputStream.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    baos.write(buffer, 0, len);
                }
                // 释放资源
                inputStream.close();
                baos.close();
                // 返回字符串
                final String result = new String(baos.toByteArray());
                android.os.Message msg = new Message();
                msg.obj = result;
                handler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}

