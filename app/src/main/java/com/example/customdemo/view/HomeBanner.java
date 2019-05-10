package com.example.customdemo.view;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.customdemo.R;
import com.example.customdemo.bean.BannerResult;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangjunzhong on 2018/3/19.
 */

public class HomeBanner extends LinearLayout {

    ViewPager mBannerPager;
    PagerIndicator pagerIndicator;

    private Context mContext;
    private OnBannerQueClick onBannerQueClick;

    ImageCycleAdapter mAdvAdapter;
    Disposable disposable;

    public HomeBanner(Context context) {
        this(context, null);
        mContext = context;
        intView();
    }

    public HomeBanner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_rippleheader, this, true);
        ButterKnife.bind(this, view);
        intView();
    }

    public void setOnBannerQuoClick(OnBannerQueClick click) {
        onBannerQueClick = click;
    }


    public View getTradeView() {
        return mBannerPager;
    }

    private void intView() {
        mBannerPager = findViewById(R.id.bannerViewpager);
        pagerIndicator = findViewById(R.id.pagerIndicate);
        initViewPage();
    }

    private void initViewPage() {
//        mBannerPager.setFocusable(true);
//        mBannerPager.setFocusableInTouchMode(true);
//        mBannerPager.requestFocus();
//        setScrollTime(2000);
        mBannerPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int pageIndex = position % pagerIndicator.getCount();
                pagerIndicator.onPageScrolled(pageIndex, positionOffset);

                //滚动图片视图适配器
                startImageTimerTask();
//                startTime();

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        mBannerPager.setOnTouchListener(new OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_UP:
//                        startTime();
//                        // 开始图片滚动
////                        startImageTimerTask();
//                        break;
//                    case MotionEvent.ACTION_DOWN:
//                        stopTime();
//                        break;
//                    default:
//                        // 停止图片滚动
//                        stopTime();
////                        stopImageTimerTask();
//                        break;
//                }
//                return false;
//            }
//        });

        setScrollTime(1000);
        //滚动图片视图适配器
        startImageTimerTask();
    }

    public void startTime() {

    }

    public void stopTime() {
        if (disposable != null)
            disposable.dispose();
    }

    public void setBannerData(List<BannerResult> list) {
        pagerIndicator.setCount(list.size());
        mAdvAdapter = new ImageCycleAdapter(mContext, list);
        mBannerPager.setAdapter(mAdvAdapter);
        mAdvAdapter.notifyDataSetChanged();
        if (list.size() == 1) {
            pagerIndicator.setVisibility(INVISIBLE);
            stopImageTimerTask();
        } else {
            pagerIndicator.setVisibility(VISIBLE);
//            startTime();
            startImageTimerTask();
//            setScrollTime(1000);
        }
        mBannerPager.setCurrentItem(list.size() * 100 + 100);
    }

    public interface OnBannerQueClick {
        void onClickView(int position);
    }


    public void setScrollTime(int time) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            // 设置速率对象 DecelerateInterpolator：动画从开始到结束，变化率是一个减速的过程
            BannerScroller scroller = new BannerScroller(mBannerPager.getContext(), new DecelerateInterpolator());
            field.set(mBannerPager, scroller);
            scroller.setmDuration(time);
        } catch (Exception e) {
            Log.e("BannerView", "" + e.getMessage());
        }
    }


    /**
     * 开始轮播(手动控制自动轮播与否，便于资源控制)
     */
    public void startImageCycle() {
        startImageTimerTask();
    }

    /**
     * 暂停轮播——用于节省资源
     */
    public void pushImageCycle() {
        stopImageTimerTask();
    }

    /**
     * 开始图片滚动任务
     */
    private void startImageTimerTask() {
        stopImageTimerTask();
        // 图片每2秒滚动一次
        mHandler.postDelayed(mImageTimerTask, 2500);
    }

    /**
     * 停止图片滚动任务
     */
    private void stopImageTimerTask() {
        mHandler.removeCallbacks(mImageTimerTask);
    }

    private Handler mHandler = new Handler();

    /**
     * 图片自动轮播Task
     */
    private Runnable mImageTimerTask = new Runnable() {

        @Override
        public void run() {
            mBannerPager.setCurrentItem(mBannerPager.getCurrentItem() + 1);

        }
    };


    private class ImageCycleAdapter extends PagerAdapter {

        /**
         * 图片视图缓存列表
         */
        private List<ImageView> mImageViewCacheList;

        /**
         * 图片资源列表
         */
        private List<BannerResult> mAdList = new ArrayList<BannerResult>();

        private Context mContext;

        public ImageCycleAdapter(Context context, List<BannerResult> adList) {
            mContext = context;
            mAdList = adList;

            mImageViewCacheList = new ArrayList<ImageView>();
        }

        @Override
        public int getCount() {
            if (mAdList.size() > 1) {
                return Integer.MAX_VALUE;
            } else {
                return mAdList.size();
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            String imageUrl = mAdList.get(position % mAdList.size()).getUrl();
            ImageView imageView;
            if (mImageViewCacheList.isEmpty()) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            } else {
                imageView = mImageViewCacheList.remove(0);
            }
            // 设置图片点击监听
            imageView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (onBannerQueClick != null)
                        onBannerQueClick.onClickView(position % mAdList.size());
                }
            });

            container.addView(imageView);
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(mContext).load(imageUrl).apply(options).into(imageView);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView view = (ImageView) object;
            container.removeView(view);
            mImageViewCacheList.add(view);
        }

    }

    interface Disposable {
        /**
         * Dispose the resource, the operation should be idempotent.
         */
        void dispose();

        /**
         * Returns true if this resource has been disposed.
         *
         * @return true if this resource has been disposed
         */
        boolean isDisposed();
    }

}
