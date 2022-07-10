package com.common.library.util.file;

/**
 * yuanluu  通知下载apk进度更新
 */
public class FileDownloadEvent {

    /**
     * 文件大小
     */
    public long total;
    /**
     * 已下载大小
     */
    public long progress;

    public FileDownloadEvent(long total, long progress) {
        this.total = total;
        this.progress = progress;
    }

}
