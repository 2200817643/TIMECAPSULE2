package com.example.timecapsule;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Provider;
import java.security.Security;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView mTextMessage;
    private WebView webView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:





//                    webView=findViewById(R.id.web_view);
//
//                    webView.getSettings().setJavaScriptEnabled(true);
//                    webView.setWebViewClient(new WebViewClient());
//
//                    webView.loadUrl("http://10.176.133.251:9090/browse/请设置胶囊信息.html");
                    //webView.loadUrl("http://www.baidu.com");
                    return true;
                case R.id.navigation_dashboard:

//                    ListView listview=new ListView();
//                    listview.add
                   // mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    //mTextMessage.setText(R.string.title_notifications);

                    new Thread(new Runnable() {

                        public String streamToString(InputStream is) {
                            try {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                byte[] buffer = new byte[1024];
                                int len = 0;
                                while ((len = is.read(buffer)) != -1) {
                                    baos.write(buffer, 0, len);
                                }
                                baos.close();
                                is.close();
                                byte[] byteArray = baos.toByteArray();
                                return new String(byteArray);
                            } catch (Exception e) {
                                Log.e(TAG, e.toString());
                                return null;
                            }
                        }
                        @Override
                        public void run() {
                            try{
                               // String baseUrl = "http://10.176.133.251:8080";
                                String baseUrl = "http://www.baidu.com";
                                // 新建一个URL对象
                                URL url = new URL(baseUrl);
                                // 打开一个HttpURLConnection连接
                                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                                // 设置连接主机超时时间
                                urlConn.setConnectTimeout(5 * 1000);
                                //设置从主机读取数据超时
                                urlConn.setReadTimeout(5 * 1000);
                                // 设置是否使用缓存  默认是true
                                urlConn.setUseCaches(true);
                                // 设置为Post请求
                                urlConn.setRequestMethod("GET");
                                //urlConn设置请求头信息
                                //设置请求中的媒体类型信息。
                                urlConn.setRequestProperty("Content-Type", "application/json");
                                //设置客户端与服务连接类型
                                urlConn.addRequestProperty("Connection", "Keep-Alive");
                                // 开始连接
                                urlConn.connect();
                                // 判断请求是否成功
                                if (urlConn.getResponseCode() == 200) {
                                    // 获取返回的数据

                                    String result = streamToString(urlConn.getInputStream());
                                    Log.e(TAG, "Get方式请求成功，result--->" + result);
                                } else {
                                    Log.e(TAG, "run: "+urlConn.getResponseCode());
                                    Log.e(TAG, "Get方式请求失败");
                                }
                                // 关闭连接
                                urlConn.disconnect();
                            } catch (Exception e) {
                                Log.e(TAG, e.toString());
                            }
                        }})/*.start()*/;
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//
//        for (Provider provider : Security.getProviders()){
//            Log.e(TAG, "Provider: " + provider.getName());
//            for (Provider.Service service : provider.getServices()){
//                Log.e(TAG,"  Algorithm: " + service.getAlgorithm());
//            }
//            System.out.println("\n");
//        }
//        webView=findViewById(R.id.web_view);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebViewClient(new WebViewClient());

       // mTextMessage.bringToFront();
        //可以在主线程访问网络，否则android.os.NetworkOnMainThreadException
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

}
