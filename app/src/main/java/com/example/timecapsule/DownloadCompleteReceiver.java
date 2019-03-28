package com.example.timecapsule;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import static android.content.Context.DOWNLOAD_SERVICE;

class DownloadCompleteReceiver extends BroadcastReceiver {
    private static final String TAG = "DownloadCompleteReceive";
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("onReceive. intent:{}", intent != null ? intent.toUri(0) : null);

            if (intent != null) {
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
                    long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);


                    Log.d("downloadId:{}", String.valueOf(downloadId));
                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);

                    String type = downloadManager.getMimeTypeForDownloadedFile(downloadId);
                    Log.d("getMimeTypeFor:{}", type);
                    if (TextUtils.isEmpty(type)) {
                        type = "*/*";
                    }
                    Uri uri = downloadManager.getUriForDownloadedFile(downloadId);

                    Toast.makeText(context.getApplicationContext(), "下载完毕，储存位置："+getFilePath(context,uri), Toast.LENGTH_LONG).show();
                    Log.d("UriForDownloadedFile:{}", String.valueOf(uri));
                    if (uri != null&&getFilePath(context,uri).contains(".zip")) {
                        Intent handlerIntent = new Intent(Intent.ACTION_VIEW);

                        // 绕过文件权限检查
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                            StrictMode.setVmPolicy(builder.build());
                        }
                        File file=new File(getFilePath(context,uri));
                        handlerIntent.setDataAndType(Uri.fromFile(file), "application/x-zip-compressed");

                        try{
                            //胶囊文件，不能用来打开（除非弄个界面
                            context.startActivity(handlerIntent);
                        }catch (Exception e){
                            Toast.makeText(context.getApplicationContext(), "自动打开文件出错啦，请手动打开文件"+getFilePath(context,uri), Toast.LENGTH_LONG).show();
                        }

                    }
                }
            }
        }



    public static String getFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;

        final String scheme = uri.getScheme();
        String data = null;

        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
    }

