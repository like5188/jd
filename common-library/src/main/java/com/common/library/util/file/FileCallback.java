package com.common.library.util.file;


import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * yuanluu FileCallback
 */
public abstract class FileCallback implements Callback<ResponseBody> {

    /**
     * 订阅下载进度
     */
    /**
     * 目标文件存储的文件夹路径
     */
    private final String destFileDir;
    /**
     * 目标文件存储的文件名
     */
    private final String destFileName;

    protected FileCallback(String destFileDir, String destFileName) {
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
//        subscribeLoadProgress();
    }

    /**
     * 成功后回调，在主线程中执行
     * @param file 可能为null
     */
    public abstract void onSuccess(File file);

    /**
     * 下载过程回调，在主线程中执行
     */
    public abstract void onLoading(long progress, long total);

    /**
     * 请求成功后保存文件
     */
    @SuppressLint("CheckResult")
    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
        InputStream inputStream ;
        if (response.body() != null) {
            long total = response.body().contentLength();
            inputStream = response.body().byteStream();
//            Observable.just(inputStream)
//                    .subscribeOn(Schedulers.io())
//                    .unsubscribeOn(Schedulers.io())
//                    .map(is -> saveFile(is,total)) // 保存文件必须在子线程中执行，否则报错
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(this::onSuccess);
        }
    }

    /**
     * 通过IO流写入文件
     */
    private File saveFile(InputStream is,long total) {
        FileOutputStream os = null;
        BufferedInputStream bis = null;
        byte[] buf = new byte[2048];
        int len;
        try {
            File dir = new File(destFileDir);
            File file = new File(dir, destFileName);
            if (file.exists()){
                file.delete();
            }
            os = new FileOutputStream(file,true);
            bis = new BufferedInputStream(is);
            long sum = 0;
            while ((len = bis.read(buf)) != -1) {
                os.write(buf, 0, len);
                sum += len;
                long progress = sum  * 100 / total ;
                // 发送当前下载进度事件，此处也可写成listener形式
                onLoading(progress, total);
//                RxBus.getInstance().post(new FileDownloadEvent(total, progress));
            }
            os.flush();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("shit", "saveFile: "+e.getMessage() );
        } finally {
            unSubscribe();
            IOUtils.close(os);
            IOUtils.close(bis);
            IOUtils.close(is);
        }
        return null;
    }

    /**
     * 订阅文件下载进度
     */
//    private void subscribeLoadProgress() {
//        rxSubscriptions.add(
//                RxBus.getInstance()
//                        .toObservable(FileDownloadEvent.class)
//                        .onBackpressureBuffer()
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(fileLoadEvent -> onLoading(fileLoadEvent.progress, fileLoadEvent.total))
//        );
//    }

    /**
     * 取消订阅，防止内存泄漏
     */
    private void unSubscribe() {
//        if (!rxSubscriptions.isUnsubscribed()) {
//            rxSubscriptions.unsubscribe();
//        }
    }

}