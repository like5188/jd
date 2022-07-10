package com.common.library.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Util {

    private static final String TAG = "SDK_Sample.Util";

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    public static byte[] getUrlBitmap(String url, int imageSize) {
        LogUtils.v("bitmap2Bytes getUrlBitmap" + url);
        URL imageurl = null;
        try {
            imageurl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();
            CompressFormat format = CompressFormat.JPEG;
            if (url.contains(".png")) {
                format = CompressFormat.PNG;
            }
            return getStaticSizeBitmapByteByBitmap(bitmap, imageSize, format);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Bitmap转换成byte[]并且进行压缩,压缩到不大于maxkb
     *
     * @param bitmap
     * @param IMAGE_SIZE
     * @return
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap, int IMAGE_SIZE) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, output);
        int options = 100;
        LogUtils.v("bitmap2Bytes  size" + output.toByteArray().length + "IMAGE_SIZE" + IMAGE_SIZE
                + "options==" + options
        );
        while (output.toByteArray().length > IMAGE_SIZE && options != 10) {
            output.reset(); //清空output
            bitmap.compress(CompressFormat.JPEG, options, output);//这里压缩options%，把压缩后的数据存放到output中
            options -= 10;
        }
        LogUtils.v("bitmap2Bytes  size" + output.toByteArray().length + "IMAGE_SIZE" + IMAGE_SIZE
                + "options==" + options
        );
        return output.toByteArray();
    }

    public static byte[] getStaticSizeBitmapByteByBitmap(Bitmap srcBitmap, int maxSize, CompressFormat format) {
        // 首先进行一次大范围的压缩
        Bitmap tempBitmap;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        // 设置矩阵数据
        Matrix matrix = new Matrix();
        srcBitmap.compress(format, 100, output);
        // 如果进行了上面的压缩后，依旧大于32K，就进行小范围的微调压缩
        byte[] bytes = output.toByteArray();
        LogUtils.v(TAG, "压缩之前 = " + bytes.length);
        while (bytes.length > maxSize*1024) {
            matrix.setScale(0.9f, 0.9f);//每次缩小 1/10
            tempBitmap = srcBitmap;
            srcBitmap = Bitmap.createBitmap(
                    tempBitmap, 0, 0,
                    tempBitmap.getWidth(), tempBitmap.getHeight(), matrix, true);
            recyclerBitmaps(tempBitmap);
            output.reset();
            srcBitmap.compress(format, 100, output);
            bytes = output.toByteArray();
            LogUtils.v(TAG, "压缩一次 = " + bytes.length);
        }
        LogUtils.v(TAG, "压缩后的图片输出大小 = " + bytes.length);
        recyclerBitmaps(srcBitmap);
        return bytes;
    }

    public static void recyclerBitmaps(Bitmap... bitmaps) {
        try {
            for (Bitmap bitmap : bitmaps) {
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] getHtmlByteArray(final String url) {
        URL htmlUrl = null;
        InputStream inStream = null;
        try {
            htmlUrl = new URL(url);
            URLConnection connection = htmlUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = inputStreamToByte(inStream);

        return data;
    }

    public static byte[] inputStreamToByte(InputStream is) {
        try {
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                bytestream.write(ch);
            }
            byte imgdata[] = bytestream.toByteArray();
            bytestream.close();
            return imgdata;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] readFromFile(String fileName, int offset, int len) {
        if (fileName == null) {
            return null;
        }

        File file = new File(fileName);
        if (!file.exists()) {
            Log.i(TAG, "readFromFile: file not found");
            return null;
        }

        if (len == -1) {
            len = (int) file.length();
        }

        Log.d(TAG, "readFromFile : offset = " + offset + " len = " + len + " offset + len = " + (offset + len));

        if (offset < 0) {
            Log.e(TAG, "readFromFile invalid offset:" + offset);
            return null;
        }
        if (len <= 0) {
            Log.e(TAG, "readFromFile invalid len:" + len);
            return null;
        }
        if (offset + len > (int) file.length()) {
            Log.e(TAG, "readFromFile invalid file len:" + file.length());
            return null;
        }

        byte[] b = null;
        try {
            RandomAccessFile in = new RandomAccessFile(fileName, "r");
            b = new byte[len]; // 创建合适文件大小的数组
            in.seek(offset);
            in.readFully(b);
            in.close();

        } catch (Exception e) {
            Log.e(TAG, "readFromFile : errMsg = " + e.getMessage());
            e.printStackTrace();
        }
        return b;
    }


    public static int parseInt(final String string, final int def) {
        try {
            return (string == null || string.length() <= 0) ? def : Integer.parseInt(string);

        } catch (Exception e) {
        }
        return def;
    }
}
