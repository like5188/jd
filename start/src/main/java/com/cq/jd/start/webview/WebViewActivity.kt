package com.cq.jd.start.webview

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.common.library.router.ARouterPath
import com.common.library.ui.activity.BaseVmActivity
import com.cq.jd.start.R
import com.cq.jd.start.databinding.StartActivityWebviewBinding
import com.tencent.smtt.export.external.interfaces.JsResult
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.export.external.interfaces.WebResourceResponse
import com.tencent.smtt.sdk.*

/**
 * 此页面只牵扯到静态页面展示,不支持js交互,和任何回调
 */
@Route(path = ARouterPath.Start.H5)
class WebViewActivity :
    BaseVmActivity<WebModel, StartActivityWebviewBinding>(R.layout.start_activity_webview) {
    private var mProgressBar: ProgressBar? = null
    private var mWebView: WebView? = null
    private var mViewParent: ViewGroup? = null
    private val CHOOSE = 100 //Android 5.0以下的
    private val CHOOSE_ANDROID_5 = 200 //Android 5.0以上的

    @JvmField
    @Autowired
    var url: String? = null
    private var mValueCallback: ValueCallback<Uri>? = null
    private var mValueCallback2: ValueCallback<Array<Uri>>? = null
    private val ACTION_PAY_ALI = "alipay"
    private val ACTION_PAY_WE_CHAT = "weixin://wap/pay"
    private val ACTION_WE_CHAT = "wx.tenpay.com"
    private val ACTION_SHARE = "mdsjsc://share_recommend"
    override fun onCreate(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        initHardwareAccelerate()
        super.onCreate(savedInstanceState)
    }

    /**
     * 启用硬件加速
     */
    private fun initHardwareAccelerate() {
        val i = if (3 == 3) 2 else if (true) 4 else 0
        try {
            if (Build.VERSION.SDK.toInt() >= 11) {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED)
            }
        } catch (e: Exception) {
        }
    }

    override fun initWidget(savedInstanceState: Bundle?) {
        findViewById<View>(R.id.btn_close).setOnClickListener { view: View? -> finish() }
        findViewById<View>(R.id.btn_back).setOnClickListener { view: View? ->
            if (mWebView != null) {
                judgeClose()
            }
        }
        mProgressBar = findViewById(R.id.progressbar)
        initWebView()
        LogUtils.e("web url==$url")
        if (URLUtil.isHttpUrl(url) || URLUtil.isHttpsUrl(url)) {
            mWebView!!.loadUrl(url)
            mWebView!!.requestFocus()
        } else {
            ToastUtils.showShort("地址有错")
        }
    }

    override fun loadData() {}
    override fun createObserver() {}
    inner class myWebChromeClient : WebChromeClient() {
        override fun onProgressChanged(webView: WebView, i: Int) {
            if (i == 100) {
                mProgressBar!!.visibility = View.GONE
            } else {
                mProgressBar!!.progress = i
            }
        }

        override fun onJsAlert(
            webView: WebView,
            s: String,
            message: String,
            result: JsResult,
        ): Boolean {
            val builder = AlertDialog.Builder(this@WebViewActivity)
            builder.setMessage(message)
            builder.setOnCancelListener { dialog: DialogInterface? -> result.cancel() }
            builder.setNegativeButton("确定") { dialog: DialogInterface?, which: Int -> result.confirm() }
            builder.show()
            return true
        }

        /*// For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> valueCallback) {
            openImageChooserActivity(valueCallback);
        }

        // For Android  >= 3.0
        public void openFileChooser(ValueCallback valueCallback, String acceptType) {
            openFileChooser(valueCallback);
        }*/
        //For Android  >= 4.1
        //腾讯X5内核不用去考虑那些版本兼容,只要重写openFileChooser
        override fun openFileChooser(valueCallback: ValueCallback<Uri>, s: String, s1: String) {
//            super.openFileChooser(valueCallback, s, s1);
//            openFileChooser(valueCallback, s);
            mValueCallback = valueCallback
            openImageChooserActivity()
        }

        override fun onShowFileChooser(
            webView: WebView,
            valueCallback: ValueCallback<Array<Uri>>,
            fileChooserParams: FileChooserParams,
        ): Boolean {
            mValueCallback2 = valueCallback
            val intent = fileChooserParams.createIntent()
            startActivityForResult(intent, CHOOSE_ANDROID_5)
            return true
        }
    }

    var isIntercepted = false
    private fun initWebView() {
        mViewParent = findViewById(R.id.webView1)
        mWebView = X5WebView(this, null)
        mViewParent?.addView(mWebView, FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT))
        mWebView?.setBackgroundColor(resources.getColor(R.color.color_f5))
        mWebView?.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(webView: WebView, s: String) {
                super.onPageFinished(webView, s)
                setTitleText(webView.title)
                //                if (videoCutDownTimer != null && stopTime > 0 && !videoCutDownTimer.isTicking()) {
//                    videoCutDownTimer.start();
//                    tvTimes.setVisibility(View.VISIBLE);
//                }
            }

            override fun onReceivedError(webView: WebView, i: Int, s: String, s1: String) {
                super.onReceivedError(webView, i, s, s1)
                LogUtils.e("$s==$s1")
            }

            override fun onReceivedHttpError(
                webView: WebView,
                webResourceRequest: WebResourceRequest,
                webResourceResponse: WebResourceResponse,
            ) {
                super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse)
                LogUtils.e("=======onReceivedHttpError")
            }

            override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
                LogUtils.e("========$url")
                //                final PayTask task = new PayTask(WebViewActivity.this);
//                boolean isIntercepted = task.payInterceptorWithUrl(url, true, new H5PayCallback() {
//                    @Override
//                    public void onPayResult(final H5PayResultModel result) {
//                        // 支付结果返回
//                        final String url = result.getReturnUrl();
//                        if (!TextUtils.isEmpty(url)) {
//                            WebViewActivity.this.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    webView.loadUrl(url);
//                                }
//                            });
//                        }
//                    }
//                });
                if (isIntercepted) {
                    return super.shouldOverrideUrlLoading(webView, url)
                }
                if (url.startsWith(ACTION_PAY_ALI)) {
                    val intent = Intent()
                    intent.action = "android.intent.action.VIEW"
                    val content_url = Uri.parse(url)
                    intent.data = content_url
                    startActivity(intent)
                    return true
                }
                //                else if (url.startsWith(PROTOCOL_PREFIX)) {
//                    Uri uri = Uri.parse(url);
//                    String host = uri.getHost();
//                    if (host.equals(ACTION_SHARE)) {
//                        doIntent(ARouterPath.User.SHARE);
//                    }
//                }
                return super.shouldOverrideUrlLoading(webView, url)
            }

            override fun shouldOverrideUrlLoading(
                webView: WebView,
                request: WebResourceRequest,
            ): Boolean {
                isIntercepted = true
                super.shouldOverrideUrlLoading(webView, request)
                val mUrl = request.url.toString()
                val url1 = webView.url
                LogUtils.e("=======shouldOverrideUrlLoading2==$url1")
                if (mUrl.contains(ACTION_PAY_WE_CHAT)) { //微信h5支付回调
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.data = Uri.parse(mUrl)
                    startActivity(intent)
                } else if (mUrl.contains(ACTION_WE_CHAT)) { //微信h5支付
                    val headers: Map<String, String> = HashMap()
                    //TODO 添加baseurl
//                    headers.put("Referer", UrlHelper.INSTANCE.getBaseUrl());//商户申请H5时提交的授权域名
                    mWebView?.loadUrl(mUrl, headers)
                } else if (WebUrlHelper.checkUrl(request.url)) {
                    val host = request.url.host
                    if (mUrl == ACTION_SHARE) {
//                        doIntent(ARouterPath.User.SHARE);
                    }
                } else if (mUrl.startsWith(ACTION_PAY_ALI)) {
                    val intent = Intent()
                    intent.action = "android.intent.action.VIEW"
                    val content_url = Uri.parse(mUrl)
                    intent.data = content_url
                    startActivity(intent)
                } else {
//                    if (!mUrl.contains("token")) {
//                        if (mUrl.contains("?")) {
//                            mUrl += "&uid=" + AppConfig.getInstance().getUid() + "&token=" + AppConfig.getInstance().getToken();
//                        } else {
//                            mUrl += "?uid=" + AppConfig.getInstance().getUid() + "&token=" + AppConfig.getInstance().getToken();
//                        }
//                    }
//                    webView.loadUrl(mUrl);
                    return false
                }
                return true
            }
        })
        mWebView?.let {
            it.webChromeClient = myWebChromeClient()
            val webSetting = it.settings
            webSetting.javaScriptEnabled = true
            webSetting.javaScriptCanOpenWindowsAutomatically = true
            webSetting.allowFileAccess = true
            webSetting.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
            webSetting.setSupportZoom(true)
            webSetting.builtInZoomControls = true
            webSetting.useWideViewPort = true
            webSetting.setSupportMultipleWindows(true)
            // webSetting.setLoadWithOverviewMode(true);
            webSetting.setAppCacheEnabled(true)
            // webSetting.setDatabaseEnabled(true);
            webSetting.domStorageEnabled = true
            webSetting.setGeolocationEnabled(true)
            webSetting.setAppCacheMaxSize(Long.MAX_VALUE)
            // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
            webSetting.pluginState = WebSettings.PluginState.ON_DEMAND
            // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
            webSetting.cacheMode = WebSettings.LOAD_NO_CACHE
        }
    }

    private fun openImageChooserActivity() {
        val intent = Intent()
        intent.action = Intent.ACTION_PICK
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(Intent.createChooser(intent, "选择文件"), CHOOSE)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (intent == null) return
        try {
            if (requestCode == CHOOSE) {
                //5.0以下选择图片后的回调
                processResult(resultCode, intent)
            } else if (requestCode == CHOOSE_ANDROID_5) {
                //5.0以上选择图片后的回调
                processResultAndroid5(resultCode, intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun processResult(resultCode: Int, intent: Intent?) {
        if (mValueCallback == null) {
            return
        }
        if (resultCode == RESULT_OK && intent != null) {
            val result = intent.data
            mValueCallback!!.onReceiveValue(result)
        } else {
            mValueCallback!!.onReceiveValue(null)
        }
        mValueCallback = null
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun processResultAndroid5(resultCode: Int, intent: Intent?) {
        if (mValueCallback2 == null) {
            return
        }
        if (resultCode == RESULT_OK && intent != null) {
            mValueCallback2!!.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(
                resultCode,
                intent))
        } else {
            mValueCallback2!!.onReceiveValue(null)
        }
        mValueCallback2 = null
    }

    protected fun canGoBack(): Boolean {
        return mWebView != null && mWebView!!.canGoBack()
    }

    override fun onBackPressed() {
        judgeClose()
    }

    private fun judgeClose() {
        if (canGoBack()) {
            mWebView!!.goBack()
        } else {
            finish()
        }
    }

    override fun onDestroy() {
        if (mWebView != null) {
            val parent = mWebView!!.parent as ViewGroup
            parent?.removeView(mWebView)
        }
        //        if (videoCutDownTimer != null && videoCutDownTimer.isTicking()) {
//            videoCutDownTimer.cancel();
//        }
        super.onDestroy()
    }
}