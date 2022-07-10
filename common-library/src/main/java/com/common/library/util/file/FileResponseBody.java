package com.common.library.util.file;




import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;

/**
 * yuanluu
 * 目的是实现下载进度的监听
 */
public class FileResponseBody extends ResponseBody {

    private final ResponseBody responseBody;

    public FileResponseBody(ResponseBody responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @NotNull
    @Override
    public BufferedSource source() {
        return Okio.buffer(new ForwardingSource(responseBody.source()) {
            long bytesReaded = 0;
            @Override
            public long read(@NotNull Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                bytesReaded += bytesRead == -1 ? 0 : bytesRead;
                // 发送当前下载进度事件，此处也可写成listener形式
//                RxBus.getInstance().post(new FileDownloadEvent(contentLength(), bytesReaded));
                return bytesRead;
            }
        });
    }

}
