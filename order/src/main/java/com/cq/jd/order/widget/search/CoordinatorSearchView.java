package com.cq.jd.order.widget.search;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.common.library.widget.SimpleRatingBar;
import com.cq.jd.order.R;
import com.cq.jd.order.widget.coordinatortablayout.listener.LoadHeaderImagesListener;
import com.cq.jd.order.widget.coordinatortablayout.listener.OnTabSelectedListener;
import com.cq.jd.order.widget.coordinatortablayout.utils.SystemView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;


/**
 * @author hugeterry(http://hugeterry.cn)
 */

public class CoordinatorSearchView extends CoordinatorLayout {
    private int[] mImageArray, mColorArray;

    private Context mContext;
    private Toolbar mToolbar;
    private ActionBar mActionbar;
    private ImageView mImageView;
    private ImageView ivLogo;
    private ImageView ivMore;
    private ImageView ivCollect;
    private SimpleRatingBar sRating;
    private TextView tvName;
    private TextView tvDistance;
    private TextView tvNum;
    private TextView tvYyTime;
    private AppBarLayout appBar;
    private EditText etSearch;
    private TextView tvSinglePrice;
    private LinearLayout llSearch;
    private LinearLayout llTag;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private LoadHeaderImagesListener mLoadHeaderImagesListener;
    private OnTabSelectedListener mOnTabSelectedListener;

    public ImageView getIvMore() {
        return ivMore;
    }

    public CoordinatorSearchView(Context context) {
        super(context);
        mContext = context;
    }

    public CoordinatorSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        if (!isInEditMode()) {
            initView(context);
            initWidget(context, attrs);
        }
    }

    public CoordinatorSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        if (!isInEditMode()) {
            initView(context);
            initWidget(context, attrs);
        }
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_coordinatorsearch, this, true);
        initToolbar();
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbarlayout);
        mImageView = (ImageView) findViewById(R.id.imageview);
        appBar = (AppBarLayout) findViewById(R.id.appBar);
        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        ivMore = (ImageView) findViewById(R.id.ivMore);
        tvName = (TextView) findViewById(R.id.tvName);
        tvNum = (TextView) findViewById(R.id.tvNum);
        tvYyTime = (TextView) findViewById(R.id.tvYyTime);
        etSearch = (EditText) findViewById(R.id.etSearch);
        tvSinglePrice = (TextView) findViewById(R.id.tvSinglePrice);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        ivCollect = (ImageView) findViewById(R.id.ivCollect);
        sRating = (SimpleRatingBar) findViewById(R.id.sRating);
        llSearch = (LinearLayout) findViewById(R.id.llSearch);
        llTag = (LinearLayout) findViewById(R.id.llTag);
    }

    public ImageView getmImageView() {
        return mImageView;
    }

    public Toolbar getmToolbar() {
        return mToolbar;
    }

    public LinearLayout getLlTag() {
        return llTag;
    }

    public ImageView getIvLogo() {
        return ivLogo;
    }

    public ImageView getIvCollect() {
        return ivCollect;
    }


    public SimpleRatingBar getsRating() {
        return sRating;
    }


    public AppBarLayout getAppBar() {
        return appBar;
    }

    public TextView getTvName() {
        return tvName;
    }


    public TextView getTvDistance() {
        return tvDistance;
    }


    public TextView getTvNum() {
        return tvNum;
    }

    public EditText getEtSearch() {
        return etSearch;
    }


    public TextView getTvYyTime() {
        return tvYyTime;
    }



    public TextView getTvSinglePrice() {
        return tvSinglePrice;
    }


    public LinearLayout getLlSearch() {
        return llSearch;
    }


    private void initWidget(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs
                , R.styleable.CoordinatorTabLayout);

        TypedValue typedValue = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int contentScrimColor = typedArray.getColor(
                R.styleable.CoordinatorTabLayout_contentScrim, typedValue.data);
        mCollapsingToolbarLayout.setContentScrimColor(contentScrimColor);

        typedArray.recycle();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        ((AppCompatActivity) mContext).setSupportActionBar(mToolbar);
        mActionbar = ((AppCompatActivity) mContext).getSupportActionBar();
    }

    /**
     * 设置Toolbar标题
     *
     * @param title 标题
     * @return CoordinatorRecommendTabLayout
     */
    public CoordinatorSearchView setTitle(String title) {
        if (mActionbar != null) {
            mActionbar.setTitle(title);
        }
        return this;
    }

    /**
     * 设置Toolbar显示返回按钮及标题
     *
     * @param canBack 是否返回
     * @return CoordinatorRecommendTabLayout
     */
    public CoordinatorSearchView setBackEnable(Boolean canBack) {
        if (canBack && mActionbar != null) {
            mActionbar.setDisplayHomeAsUpEnabled(true);
            mActionbar.setHomeAsUpIndicator(R.mipmap.icbackwhite);
        }
        return this;
    }

    /**
     * 设置每个tab对应的头部图片
     *
     * @param imageArray 图片数组
     * @return CoordinatorRecommendTabLayout
     */
    public CoordinatorSearchView setImageArray(@NonNull int[] imageArray) {
        mImageArray = imageArray;
        return this;
    }

    /**
     * 设置每个tab对应的头部照片和ContentScrimColor
     *
     * @param imageArray 图片数组
     * @param colorArray ContentScrimColor数组
     * @return CoordinatorRecommendTabLayout
     */
    public CoordinatorSearchView setImageArray(@NonNull int[] imageArray, @NonNull int[] colorArray) {
        mImageArray = imageArray;
        mColorArray = colorArray;
        return this;
    }

    /**
     * 设置每个tab对应的ContentScrimColor
     *
     * @param colorArray 图片数组
     * @return CoordinatorRecommendTabLayout
     */
    public CoordinatorSearchView setContentScrimColorArray(@NonNull int[] colorArray) {
        mColorArray = colorArray;
        return this;
    }




    /**
     * 获取该组件中的ActionBar
     */
    public ActionBar getActionBar() {
        return mActionbar;
    }

    /**
     * 获取该组件中的ImageView
     */
    public ImageView getImageView() {
        return mImageView;
    }

    /**
     * 设置LoadHeaderImagesListener
     *
     * @param loadHeaderImagesListener 设置LoadHeaderImagesListener
     * @return CoordinatorRecommendTabLayout
     */
    public CoordinatorSearchView setLoadHeaderImagesListener(LoadHeaderImagesListener loadHeaderImagesListener) {
        mLoadHeaderImagesListener = loadHeaderImagesListener;
        return this;
    }

    /**
     * 设置onTabSelectedListener
     *
     * @param onTabSelectedListener 设置onTabSelectedListener
     * @return CoordinatorRecommendTabLayout
     */
    public CoordinatorSearchView addOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        mOnTabSelectedListener = onTabSelectedListener;
        return this;
    }

    /**
     * 设置透明状态栏
     *
     * @param activity 当前展示的activity
     * @return CoordinatorRecommendTabLayout
     */
    public CoordinatorSearchView setTranslucentStatusBar(@NonNull Activity activity) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return this;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (mToolbar != null) {
            MarginLayoutParams layoutParams = (MarginLayoutParams) mToolbar.getLayoutParams();
            layoutParams.setMargins(
                    layoutParams.leftMargin,
                    layoutParams.topMargin + SystemView.getStatusBarHeight(activity),
                    layoutParams.rightMargin,
                    layoutParams.bottomMargin);
        }

        return this;
    }

    /**
     * 设置沉浸式
     *
     * @param activity 当前展示的activity
     * @return CoordinatorRecommendTabLayout
     */
    public CoordinatorSearchView setTranslucentNavigationBar(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return this;
        } else {
            mToolbar.setPadding(0, SystemView.getStatusBarHeight(activity) >> 1, 0, 0);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        return this;
    }

}