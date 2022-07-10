package com.cq.jd.start.webview;

import android.net.Uri;

public class WebUrlHelper {
    static final String PROTOCOL_PREFIX = "mdsjsc";

    public static boolean checkUrl(Uri url) {
        if (null != url.getScheme()) {
            return url.getScheme().startsWith(PROTOCOL_PREFIX);
        }
        return false;
    }
}
