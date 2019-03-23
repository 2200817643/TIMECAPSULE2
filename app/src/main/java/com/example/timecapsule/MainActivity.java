package com.example.timecapsule;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView mTextMessage;
    private WebView webView;
    private ConstraintLayout container;
    private ConstraintLayout mylayout;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.seal_up_or_off:
                    return true;
                case R.id.join_sealing:
                    Log.w(TAG, "onNavigationItemSelected: ");
                    webView.loadUrl("http://www.baidu.com");

                    return true;
                case R.id.my_capsules:
                    Log.w(TAG, "onNavigationItemSelected2: ");
                    webView.loadUrl("http://10.176.133.251:9090/operation/setup.html");
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = findViewById(R.id.message);
        container = findViewById(R.id.container);
        mylayout = findViewById(R.id.mylayout);
//        mylayout.removeAllViews();
////在mylaout上加载webview，容器为主容器
//        LayoutInflater.from(mylayout.getContext()).inflate(R.layout.layout_webview, container, true);
//        //之后获取webview的引用
//        webView = findViewById(R.id.webview);

//如果不行，尝试上面
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        System.out.println("MainActivity.onCreate");
            mylayout.removeAllViews();
            webView = new WebView(getApplicationContext());
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


            });
           //webView.setWebChromeClient(new PQChromeClient());
        mylayout.addView(webView);

            WebSettings webSettings = webView.getSettings();

            webView.getSettings().setJavaScriptEnabled(true);
//            webSettings.setAppCacheEnabled(true);
//            webSettings.setDomStorageEnabled(true);
//            webSettings.supportMultipleWindows();
//            webSettings.setAllowContentAccess(true);
//            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//            webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
//            //   webSettings.setUseWideViewPort(true);
//            webSettings.setLoadWithOverviewMode(true);
//            webSettings.setSavePassword(true);
//            webSettings.setSaveFormData(true);
//            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//            webSettings.setLoadsImagesAutomatically(true);
//
//
//            webSettings.setCacheMode(webSettings.LOAD_DEFAULT);
//      //  webSettings.setCacheMode(webSettings.LOAD_CACHE_ELSE_NETWORK);//不管缓存有没有过期，都加载缓存
//        webSettings.setAllowFileAccess(true);
//            webSettings.setDatabaseEnabled(true);
//
//            String cacheDirPath = getFilesDir().getAbsolutePath() + "cachedir";
//            //      String cacheDirPath = getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME;
//            Log.i(TAG, "cacheDirPath=" + cacheDirPath);
//            //设置数据库缓存路径
//            webSettings.setDatabasePath(cacheDirPath);
//            //设置  Application Caches 缓存目录
//            webSettings.setAppCachePath(cacheDirPath);
//        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        //可以在主线程访问网络，否则android.os.NetworkOnMainThreadException
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != INPUT_FILE_REQUEST_CODE || mUploadMessage == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        try {
            String file_path = mCameraPhotoPath.replace("file:","");
            File file = new File(file_path);
            size = file.length();

        }catch (Exception e){
            Log.e("Error!", "Error while opening image file" + e.getLocalizedMessage());
        }

        if (data != null || mCameraPhotoPath != null) {
            Integer count = 0; //fix fby https://github.com/nnian
            ClipData images = null;
            try {
                images = data.getClipData();
            }catch (Exception e) {
                Log.e("Error!", e.getLocalizedMessage());
            }

            if (images == null && data != null && data.getDataString() != null) {
                count = data.getDataString().length();
            } else if (images != null) {
                count = images.getItemCount();
            }
            Uri[] results = new Uri[count];
            // Check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (size != 0) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else if (data.getClipData() == null) {
                    results = new Uri[]{Uri.parse(data.getDataString())};
                } else {

                    for (int i = 0; i < images.getItemCount(); i++) {
                        results[i] = images.getItemAt(i).getUri();
                    }
                }
            }

            mUploadMessage.onReceiveValue(results);
            mUploadMessage = null;
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }
    private long size = 0;
    private ValueCallback<Uri[]> mUploadMessage;
    private String mCameraPhotoPath = null;
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    public class PQChromeClient extends WebChromeClient {



        // For Android 5.0+
        public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, WebChromeClient.FileChooserParams fileChooserParams) {
            // Double check that we don't have any existing callbacks
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
            }
            mUploadMessage = filePath;
            Log.e("FileCooserParams => ", filePath.toString());

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Log.e(TAG, "Unable to create Image File", ex);
                }

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                } else {
                    takePictureIntent = null;
                }
            }

            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            contentSelectionIntent.setType("image/*");

            Intent[] intentArray;
            if (takePictureIntent != null) {
                intentArray = new Intent[]{takePictureIntent};
            } else {
                intentArray = new Intent[2];
            }

            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
            startActivityForResult(Intent.createChooser(chooserIntent, "Select images"), 1);

            return true;

        }
    }

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
}


