 package com.cq.jd.order.widget.mzbanner;

 import android.app.Activity;
 import android.content.Context;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.widget.FrameLayout;
 import android.widget.ImageView;

 import com.common.library.util.glide.ImageUtils;
 import com.cq.jd.order.R;
 import com.cq.jd.order.widget.mzbanner.holder.MZHolderCreator;
 import com.cq.jd.order.widget.mzbanner.holder.MZViewHolder;

 import java.util.List;


 public class BannerViewHolder implements MZViewHolder<String> {

    private ImageView mImageView;
    private FrameLayout flAd;
    private Activity activity;
    private static BannerViewHolder bannerViewHolder;

    public BannerViewHolder(Activity activity) {
        this.activity = activity;
    }

    public static BannerViewHolder getInstance() {
        return bannerViewHolder;
    }

    @Override
    public View createView(Context context) {
        // 返回页面布局文件
        View view = LayoutInflater.from(context).inflate(R.layout.order_banner_item, null);
        mImageView = (ImageView) view.findViewById(R.id.banner_image);
        flAd = (FrameLayout) view.findViewById(R.id.flAd);
        return view;
    }

    @Override
    public void onBind(Context context, int position, String data) {
        mImageView.setVisibility(View.VISIBLE);
        ImageUtils.loadImage(data, mImageView);
    }

    public static void setPageItem(Activity activity, MZBannerView mNormalBanner, List<String> list) {
        bannerViewHolder = new BannerViewHolder(activity);
        mNormalBanner.setPages(list, (MZHolderCreator<BannerViewHolder>) () -> bannerViewHolder);
    }

}
