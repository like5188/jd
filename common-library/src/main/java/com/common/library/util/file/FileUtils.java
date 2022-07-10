package com.common.library.util.file;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.common.library.BaseApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * 操作文件相关的工具类
 */
public class FileUtils {

    /**
     * 下载文件夹
     */
    public static final String DOWNLOAD = "download";
    public static final String zhilian = "zhilian";


    public static final String TEMP = "temp";

    public static File getDownloadDir() {
        return getDir(DOWNLOAD);
    }


    public static Bitmap getVideoThumb(String path) {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            HashMap<String, String> header = new HashMap<>();
//            header.put("Referer", SPHelper.getInstance().getString(SpConstant.img_referer));
            mediaMetadataRetriever.setDataSource(path, header);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }

        return bitmap;

    }

    public  static Bitmap getVideoLocalThumb(String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return  media.getFrameAtTime();

    }


    /**
     * 版本号比较
     * 0 代表相等，1代表version1大于version2，-1代表version1小于version2
     */
    public static int compareVersion(String version1, String version2) {
        if (TextUtils.isEmpty(version1) || TextUtils.isEmpty(version2)) {
            return -1;
        }
        if (version1.equals(version2)) {
            return 0;
        }

        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        Log.d("HomePageActivity", "version1Array==" + version1Array.length);
        Log.d("HomePageActivity", "version2Array==" + version2Array.length);
        int index = 0;
        // 获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        // 循环判断每位的大小
        Log.d("HomePageActivity", "verTag2=2222=" + version1Array[index]);
        while (index < minLen
                && (diff = Integer.parseInt(version1Array[index])
                - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }


    public static File getZhilian() {
        return getDir(zhilian);
    }

    public static File getTempDir() {
        return getDir(TEMP);
    }

    public static File getDir(String dirName) {
        StringBuilder path = new StringBuilder();
        if (isSdAvailable()) {
            // 当SD卡可用时
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                path.append(BaseApp.application.getExternalFilesDir(""));
            } else {
                path.append(Environment.getExternalStorageDirectory());
            }

        } else {
            // 当SD卡不可用时
            File cacheDir = BaseApp.application.getCacheDir();
            path.append(cacheDir.getAbsolutePath());
        }
        path.append(File.separator);
        path.append(dirName);
        File file = new File(path.toString());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            if (!isAndroidQFileExists(BaseApp.application, path.toString()) || !file.isDirectory()) {
                file.mkdirs();
            }
        } else {
            if (!file.exists() || !file.isDirectory()) {
                // 文件夹不存在,或者不是一个文件夹时,创建文件夹
                file.mkdirs();
            }
        }

        return file;
    }


    public static boolean isAndroidQFileExists(Context context, String path) {
        if (context == null) {
            return false;
        }
        AssetFileDescriptor afd = null;
        ContentResolver cr = context.getContentResolver();
        try {
            Uri uri = Uri.parse(path);
            afd = cr.openAssetFileDescriptor(Uri.parse(path), "r");
            if (afd == null) {
                return false;
            } else {
                afd.close();
            }
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (afd != null) {
                    afd.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * SD卡是够可用
     */
    public static boolean isSdAvailable() {
        if (isSdMounted()) { // sd卡挂载了
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            @SuppressLint("UsableSpace")
            long usableSpace = file.getUsableSpace();
            // 可用空间大于1M
            return usableSpace > 1024 * 1024;
        }
        return false;
    }

    /**
     * SD卡是够挂载
     */
    public static boolean isSdMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 递归删除目录/文件
     */
    public static void deleteDir(File dir) {
        if (!dir.exists() || !dir.isDirectory()) {
            dir.delete();
            return;
        }

        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDir(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    /**
     * 根据文件Uri获取路径
     */
    public static String getFilePathByUri(Uri uri) {
        String filePath = "";
        String scheme = uri.getScheme();
        if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            filePath = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = BaseApp.application.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        filePath = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return filePath;
    }

    /**
     * 递归删除目录/文件
     */
    public static void deleteDir(String path) {
        deleteDir(new File(path));
    }

    /**
     * 根据文件路径获取文件Uri
     */
    public static Uri getFileUriByPath(String path) {
        return Uri.fromFile(new File(path));
    }

    public static void installApk(File file) {
        try {
            Log.e("shit", "installApk:===============> ");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri apkUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                apkUri = FileProvider.getUriForFile(BaseApp.application
                        , "cn.jiandui.android.merchant.fileprovider"
                        , file);
            } else {
                apkUri = Uri.fromFile(file);
            }
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            // 查询所有符合 intent 跳转目标应用类型的应用，注意此方法必须放置在 setDataAndType 方法之后
            List<ResolveInfo> resolveLists = BaseApp.application.getPackageManager().queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            // 然后全部授权
            for (ResolveInfo resolveInfo : resolveLists) {
                String packageName = resolveInfo.activityInfo.packageName;
                BaseApp.application.grantUriPermission(packageName, apkUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            BaseApp.application.startActivity(intent);
        } catch (Exception e) {
            Log.e("shit", "installApk: " + e.getMessage());
        }
    }


}
