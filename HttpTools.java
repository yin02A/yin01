package tools;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import constants.Constants;

public class HttpTools {
    //常用网络请求框架  1.okHttp  2.xUtils
    // 3.HttpURLConnection post get put delete
    public static void getData(String path,HttpBackListener backListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder stringBuilder=new StringBuilder();
                try {
                    //创建URL
                    URL url=new URL(path);

                    //获取HttpURLConnection对象
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();

                    //HttpURLConnection 配置超时
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setReadTimeout(5000);

                    //HttpURLConnection 配置请求数据格式
                    httpURLConnection.setRequestProperty("Content-type","application/x-java-serialized-object");

                    //HttpURLConnection 请求方式
                    httpURLConnection.setRequestMethod("GET");

                    //HttpURLConnection 发起连接
                    httpURLConnection.connect();

                    if(httpURLConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                        InputStream inputStream=httpURLConnection.getInputStream();
                        InputStreamReader inputStreamReader=new InputStreamReader(inputStream);

                        BufferedReader reader=new BufferedReader(inputStreamReader);

                        String temp;
                        while ((temp=reader.readLine()) != null) {
                            stringBuilder.append(temp);
                        }
                        reader.close();
                        backListener.onSuccess(stringBuilder.toString(), httpURLConnection.getResponseCode());
                    }else {
                        backListener.onError(httpURLConnection.getResponseMessage(),httpURLConnection.getResponseCode());
                    }
                    httpURLConnection.disconnect();
                    Log.i("HttpTools-GET",stringBuilder.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void postData(String path,HttpBackListener backListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder stringBuilder=new StringBuilder();
                try {
                    //创建URL
                    URL url=new URL(path);

                    //获取HttpURLConnection对象
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();

                    //HttpURLConnection 配置超时
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setReadTimeout(5000);


                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setUseCaches(false);

                    //HttpURLConnection 请求方式
                    httpURLConnection.setRequestMethod("POST");

                    String body="key="+ Constants.NEWS_AppKey;
                    BufferedWriter writer=new BufferedWriter(
                            new OutputStreamWriter(httpURLConnection.getOutputStream(),"UTF-8"));
                    writer.write(body);
                    writer.close();

                    if(httpURLConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                        InputStream inputStream=httpURLConnection.getInputStream();
                        InputStreamReader inputStreamReader=new InputStreamReader(inputStream);

                        BufferedReader reader=new BufferedReader(inputStreamReader);

                        String temp;
                        while ((temp=reader.readLine()) != null) {
                            stringBuilder.append(temp);
                        }
                        reader.close();
                        backListener.onSuccess(stringBuilder.toString(),httpURLConnection.getResponseCode());
                    }else {
                        backListener.onError(httpURLConnection.getResponseMessage(),httpURLConnection.getResponseCode());
                    }

                    httpURLConnection.disconnect();
                    Log.i("HttpTools-Post",stringBuilder.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public interface HttpBackListener{
        void onSuccess(String data,int code);
        void onError(String error,int code);
    }
}
