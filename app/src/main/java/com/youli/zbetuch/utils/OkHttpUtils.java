package com.youli.zbetuch.utils;


import java.io.IOException;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtils {

   public static final String BaseUrl="http://192.168.11.11:91";//账号：admin，密码:123
 //   public static final String BaseUrl="http://192.168.43.217:122";//账号：admin，密码:123
 // public static final String BaseUrl="http://192.168.1.101:80";//账号：admin，密码:123
 // public static final String BaseUrl="http://10.130.34.140:188";//账号：admin，密码:123
    static OkHttpClient okHttpClient = null;

    //懒汉
    private static synchronized OkHttpClient getInstance() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        return okHttpClient;
    }

    /**
     * OKHttp 阻塞 Get
     *
     * @param url 请求网址
     * @return 获取到数据返回Response，若未获取到数据返回null
     */
    public static Response okHttpGet(String url) {
        getInstance();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }

    /**
     * 阻塞 Post
     *
     * @param url      url
     * @param userName 用户名
     * @param psd      密码
     * @return Response
     */
    public Response okHttpPost(String url, String userName, String psd) {
        getInstance();
        RequestBody requestBody = new FormBody.Builder()
                .add("username", userName)
                .add("password", psd)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }
    //异步Get
    public static void okHttpAsynGet(String url, String cookies,Callback callback) {
        getInstance();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie",cookies)
                .build();

        okHttpClient.newCall(request).enqueue(callback);

    }
    //异步 Post
    public static void okHttpAsyncPost(String url, String userName, String psd, Callback callback) {
        getInstance();
        RequestBody requestBody = new FormBody.Builder()
                .add("username", userName)
                .add("password", psd)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
