package com.example.timecapsule;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.example.timecapsule.R.id.imageView;
import static java.net.URLDecoder.*;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView mTextMessage;
    private WebView webView;
    private ConstraintLayout container;
    private ConstraintLayout mylayout;
    int REQUEST_CODE_SCAN=0XFFFF;
    ImageView imageView;
    //TODO:修改这个
    String url="http://192.168.43.93:9090";
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Log.i(TAG, "onNavigationItemSelected0000: ");
            imageView.setVisibility(View.GONE);
            switch (item.getItemId()) {

                case R.id.seal_up_or_off:
                    Log.w(TAG, "onNavigationItemSelected1: ");

                    webView.loadUrl(url+"/operation/choose_ep_or_dp.html");

                        //webView.loadUrl(url+"/operation/download_file.html?processid=74828&capsulename=%25E3%2580%258A%25E6%2597%25B6%25E9%2597%25B4%25E8%2583%25B6%25E5%259B%258A%25E3%2580%258B%25E8%25AF%25BE%25E9%25A2%2598%25E7%25BB%2584%25E6%2588%2590%25E6%259E%259C%25E5%25B1%2595%25E7%25A4%25BA");
                    return true;
                case R.id.join_sealing:
                    Log.w(TAG, "onNavigationItemSelected2: ");
                    //webView.loadUrl("http://www.baidu.com");
                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                    return true;
                case R.id.my_capsules:
                    Log.w(TAG, "onNavigationItemSelected3: ");

//                   // webView.loadUrl(url+"/user/waiting_others_ep.html?processid=2");
//                    webView.loadUrl(url+"/download?action=dp&processid=844&capsulename=%E6%92%92%E5%A4%A7%E5%A3%B0%E5%9C%B0");

                    Intent intent2=new Intent(MainActivity.this,ViewCapsules.class);

                    startActivity(intent2);
                    imageView.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView=(ImageView) findViewById(R.id.imageView);
        mTextMessage = (TextView)findViewById(R.id.message);
        container = (ConstraintLayout)findViewById(R.id.container);
        mylayout =(ConstraintLayout) findViewById(R.id.mylayout);
//        mylayout.removeAllViews();
////在mylaout上加载webview，容器为主容器
//        LayoutInflater.from(mylayout.getContext()).inflate(R.layout.layout_webview, container, true);
//        //之后获取webview的引用
//        webView = findViewById(R.id.webview);

//如果不行，尝试上面

        {

//            Thread myThread = new Thread() {//创建子线程
//                @Override
//                public void run() {
//                    try {
//                        sleep(3000);//使程序休眠五秒
//                        Notification n=new NotificationCompat.Builder(getBaseContext())
//                                .setContentTitle("胶囊的解封时间到啦")
//                                .setWhen(System.currentTimeMillis())
//                                .setSmallIcon(R.drawable.e)
//                                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                                .setStyle(new NotificationCompat.BigTextStyle().bigText("胶囊名称：《时间胶囊》课题组成果展示\n解封时间：2019年03月28日 下午1:57\n联系朋友们一起解封胶囊吧！"))
//                                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.e))
//                                .build();
//
//                        NotificationManager m= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                        m.notify(1,n);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//            myThread.start();


        }



        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        System.out.println("MainActivity.onCreate");
        mylayout.removeAllViews();
        webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient() {
            List<String> jslist;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.w(TAG, "shouldOverrideUrlLoading: " + url);

                return false;
            }

            @Override
            public void onLoadResource(WebView view, String url) {


                Log.w(TAG, "加载资源--" + url);
//监听下载
                if (url.contains("/download?")) {
                    Log.i(TAG, "试图下载");
                   String[] ss=url.split("&");
                   String capsulename=null;
                   for(String s:ss){
                       if(s.startsWith("capsulename")){
                           int start=s.indexOf('=');
                           capsulename=s.substring(start+1,s.length());
                           break;
                       }
                   }
                   boolean iszip=false;
                   if(url.contains("action=dp")){
                       iszip=true;
                   }

                    try {

                        downloadBySystem(url, decode(decode(capsulename)+(iszip?".zip":""),"UTF-8" ) );
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(), "正在下载胶囊...", Toast.LENGTH_SHORT).show();
                }
                super.onLoadResource(view, url);

            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                try {
                    String[] splits = url.split("/");
                    String js = splits[splits.length - 1];
                    if (jslist == null) {
                        jslist = Arrays.asList(getBaseContext().getAssets().list(""));
                        System.out.println(jslist);
                    }
                    String type = "asdsdasdasd";
                    if (js.endsWith(".js")) {
                        type = "application/x-javascript";
                    } else if (js.endsWith(".css")) {
                        type = "text/css";
                    } else if (js.endsWith(".html")) {
                        type = "text/html";
                    }
                    for (String have : jslist) {
                        if (have.endsWith(js)) {
                            System.out.println("MainActivity.shouldInterceptRequest");
                            return new WebResourceResponse(type, "utf-8", getBaseContext().getAssets().open(js));
                        }
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    return shouldInterceptRequest(view, request.getUrl().toString());
                }
                return super.shouldInterceptRequest(view, request);
            }

            ProgressDialog dialog;
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if(dialog==null){
                    dialog = new ProgressDialog(MainActivity.this); //创建进度条
                }
                if(!dialog.isShowing()) {
                    dialog.show();
                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(dialog==null){
                    dialog = new ProgressDialog(MainActivity.this); //创建进度条
                }
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
                super.onPageFinished(view, url);
            }

        });
        webView.setWebChromeClient(new PQChromeClient());
        mylayout.addView(webView);

        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);

         webSettings.setAppCacheEnabled(true);
        // webSettings.setDomStorageEnabled(true);
        webSettings.supportMultipleWindows();
        webSettings.setAllowContentAccess(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //   webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
      //  webSettings.setSavePassword(true);
    //    webSettings.setSaveFormData(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);


        webSettings.setCacheMode(webSettings.LOAD_DEFAULT);
        webView.clearFormData();
        webView.clearMatches();
        webView.clearHistory();
        webView.clearCache(true);

        //  webSettings.setCacheMode(webSettings.LOAD_CACHE_ELSE_NETWORK);//不管缓存有没有过期，都加载缓存
        webSettings.setAllowFileAccess(true);
//            webSettings.setDatabaseEnabled(true);
//
//            String cacheDirPath = getFilesDir().getAbsolutePath() + "cachedir";
//            //      String cacheDirPath = getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME;
//            Log.i(TAG, "cacheDirPath=" + cacheDirPath);
//            //设置数据库缓存路径
//            webSettings.setDatabasePath(cacheDirPath);
//            //设置  Application Caches 缓存目录
//            webSettings.setAppCachePath(cacheDirPath);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //可以在主线程访问网络，否则android.os.NetworkOnMainThreadException
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        verifyStoragePermissions(this);

        //添加文件下载器
        DownloadCompleteReceiver receiver = new DownloadCompleteReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(receiver, intentFilter);


       // webView.loadUrl("http://10.176.133.251:9090/download?processid=14");

    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED || cameraPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    /**
     * https://www.jianshu.com/p/6e38e1ef203a
     * downloadBySystem(String url, String contentDisposition, String mimeType)
     */
    private void downloadBySystem(String url, String fileName) {
        // 指定下载地址
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        // 允许媒体扫描，根据下载的文件类型被加入相册、音乐等媒体库
        request.allowScanningByMediaScanner();
        // case REQUEST_CODE_ 设置通知的显示类型，下载进行时和完成后显示通知
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        // 设置通知栏的标题，如果不设置，默认使用文件名
        request.setTitle("正在为你下载时间胶囊文件");
        // 设置通知栏的描述
        //  request.setDescription("This is description");
        // 允许在计费流量下下载
        request.setAllowedOverMetered(false);
        // 允许该记录在下载管理界面可见
        request.setVisibleInDownloadsUi(false);
        // 允许漫游时下载
        request.setAllowedOverRoaming(true);
        // 允许下载的网路类型
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        // 设置下载文件保存的路径和文件名
       // String fileName = URLUtil.guessFileName(url, contentDisposition, mimeType);
        Log.d("fileName:{}", fileName);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
//        另外可选一下方法，自定义下载路径
//        request.setDestinationUri()
//        request.setDestinationInExternalFilesDir()
        final DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        // 添加一个下载任务
        long downloadId = downloadManager.enqueue(request);
        Log.d("downloadId:{}", String.valueOf(downloadId));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(Constant.CODED_CONTENT);
                webView.loadUrl(content);
                System.out.println("扫描结果："+content);
               // Toast.makeText(this, "解析结果:" + content, Toast.LENGTH_LONG).show();
            }
        }
        switch (requestCode) {
            case RESULT_CODE_ICE_CREAM:
                Uri uri = null;
                if (data != null) {
                    uri = data.getData();
                }
                Log.i(TAG, "onActivityResult: RESULT_CODE_ICE_CREAM"+uri);
                mUploadMessage.onReceiveValue(uri);
                mUploadMessage = null;
                break;
            case REQUEST_CODE_LOLIPOP:
                Uri[] results = null;
                // Check that the response is a good one
                if (resultCode == Activity.RESULT_OK) {
                    if (data == null) {
                        // If there is not data, then we may have taken a photo
                        if (mCameraPhotoPath != null) {
                            Log.d("AppChooserFragment", mCameraPhotoPath);
                            System.out.println("路径"+Uri.fromFile(photoFile));
                            results = new Uri[]{Uri.fromFile(photoFile)};
                        }
                    } else {
                        Log.i(TAG, "onActivityResult: data为不空，取文件"+data);
                        String dataString = data.getDataString();
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};
                        }
                    }
                }

                mFilePathCallback.onReceiveValue(results);
                mFilePathCallback = null;
                break;
        }
    }

    /**
     * More info this method can be found at
     * http://developer.android.com/training/camera/photobasics.html
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        return imageFile;
    }

    private long size = 0;
    private String mCameraPhotoPath = null;
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    public static final int REQUEST_CODE_LOLIPOP = 1;
    private final static int RESULT_CODE_ICE_CREAM = 2;

    private ValueCallback<Uri[]> mFilePathCallback;

    private ValueCallback<Uri> mUploadMessage;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)

        return super.onKeyDown(keyCode, event);
    }

    File photoFile = null;
    public class PQChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
            b.setTitle("Alert");
            b.setMessage(message);
            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            });
            b.setCancelable(false);
            b.create().show();
            return true;
        }
        //设置响应js 的Confirm()函数
        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
            b.setTitle("Confirm");
            b.setMessage(message);
            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            });
            b.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            });
            b.create().show();
            return true;
        }


        @Override
        public void onConsoleMessage(String message, int lineNumber, String sourceID) {
            Log.i("MyApplication", message + " -- From line "
                    + lineNumber + " of "
                    + sourceID);

        }

        private void openFileChooser(String type) {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType(type);
            startActivityForResult(Intent.createChooser(i, "选择文件"),
                    RESULT_CODE_ICE_CREAM);
        }

        private void onShowFileChooser(Intent cameraIntent) {
            //整个弹出框为:相机、相册、文件管理
            //如果安装了其他的相机、文件管理程序，也有可能会弹出
            //selectionIntent(相册、文件管理)
            //Intent selectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            //selectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            //selectionIntent.setType("image/*");

            //------------------------------------
            //如果通过下面的方式，则弹出的选择框有:相机、相册(Android9.0,Android8.0)
            //如果是小米Android6.0系统上，依然是：相机、相册、文件管理
            //如果安装了其他的相机(百度魔拍)、文件管理程序(ES文件管理器)，也有可能会弹出
            Intent selectionIntent = new Intent(Intent.ACTION_PICK,null);
            selectionIntent.setType("*/*");
            //------------------------------------


            Intent[] intentArray;
            if (cameraIntent != null) {
                intentArray = new Intent[]{cameraIntent};
            } else {
                intentArray = new Intent[0];
            }

            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_TITLE,"选择文件");
            chooserIntent.putExtra(Intent.EXTRA_INTENT, selectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

            startActivityForResult(chooserIntent, REQUEST_CODE_LOLIPOP);

        }


        //The undocumented magic method override
        //Eclipse will swear at you if you try to put @Override here
        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMessage = uploadMsg;
            openFileChooser("image/*");
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            openFileChooser("*/*");
        }

        //For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            openFileChooser("image/*");
        }

        //For Android5.0+
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = filePathCallback;

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(webView.getContext().getApplicationContext().getPackageManager()) != null) {
                // Create the File where the photo should go

                try {
                    photoFile = createImageFile();
                    takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                } else {
                    takePictureIntent = null;
                }
            }
            onShowFileChooser(takePictureIntent);
            return true;
        }

    }
}


