package com.dc.canvastest.pw_blur.wall;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.dc.canvastest.ImageUtil;

/**
 * Created by LiDaChang on 17/4/21.
 * __--__---__-------------__----__
 */

public class BlurWall extends FrameLayout {
    private Handler mHandler;
    private Drawable mDrawableBg;
    private static final int SCALED_SIZE = 400;
    private static final int ANIM_DURATION = 300;
    private static final int UPDATE_HZ = 20;
    private static final float STATIC_RADIUS = 10f;
    private float added_radius = 1f;
    private float mActiveRadius;
    private Bitmap mScreenBitmap;
    private RunnableBlur mRunnableBlur;

    private SimpleBlur mSimpleBlur;

    private Activity mActivity;
    private OnAnimationEndListener mListener;
    private OnAnimationUpdatingListener mUpdatingListener;

    public BlurWall(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mActivity = (Activity) context;
        init();
    }

    public void init() {
        mHandler = new Handler();
//        Logger.i(1, "init!");
        added_radius = 1f;
//        LayoutInflater.from(mContext).inflate(R.layout.layout_title_bar, this);
    }

    public void doBlur() {
        mRunnableBlur = new RunnableBlur();
        mSimpleBlur = SimpleBlur.create(mActivity);
        View viewScreenShot = mActivity.getWindow().getDecorView();
        mScreenBitmap = ImageUtil.getBitmapFromView(viewScreenShot);
//        mScreenBitmap = mSimpleBlur.takeScreenShot(mActivity);
//            Bitmap mScreenBitmap = SimpleBlur.takeScreenShot(ArticleDetailsActivity.this);
        mScreenBitmap = ImageUtil.scalePicByMaxSide(mScreenBitmap, SCALED_SIZE);
        mHandler.post(mRunnableBlur);
        ValueAnimator va = ValueAnimator.ofFloat(1f, 24f);
        va.setDuration(ANIM_DURATION);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                mActiveRadius = (float) animation.getAnimatedValue();
                if (mUpdatingListener != null) {
                    mUpdatingListener.onUpdating((float) animation.getAnimatedValue() / 24f);
                }

            }
        });
        va.addListener(new OnAnimatorEndListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mListener != null) {
                    mListener.onAnimationEnd();
                }
            }
        });
        va.start();
    }

    private class RunnableBlur implements Runnable {
        @Override
        public void run() {
            if (mActiveRadius < 24) {
                if (added_radius > 20) {
                    added_radius = 20;
                }
//                Logger.i(1, "ra:" + mActiveRadius);
//                Logger.i(1, "blurGo!" + added_radius);
//                mScreenBitmap = SimpleBlur.blurBitmap(mScreenBitmap, mActivity, added_radius);
                if (mScreenBitmap != null) {
                    mScreenBitmap = mSimpleBlur.blurGo(mScreenBitmap, mActivity, added_radius);
                }
//                Logger.i(1, "bitmap:" + mScreenBitmap);
                added_radius += 2;
                mDrawableBg = ImageUtil.bitmap2Drawable(mScreenBitmap);
                setBackground(mDrawableBg);
                mHandler.postDelayed(this, UPDATE_HZ);
            } else {
                mHandler.removeCallbacks(this);
            }
        }
    }

    public Bitmap getBitmap() {
        return mScreenBitmap;
    }

    public void recycle() {
        if (mSimpleBlur != null) {
            mSimpleBlur.recycle();
            mSimpleBlur = null;
        }
        mDrawableBg = null;
        if (mScreenBitmap != null) {
            mScreenBitmap.recycle();
        }
        mScreenBitmap = null;
        mRunnableBlur = null;
        added_radius = 1f;
    }

    public void setAnimationEndListener(OnAnimationEndListener listener) {
        mListener = listener;
    }

    public void setOnUpdatingListener(OnAnimationUpdatingListener listener) {
        mUpdatingListener = listener;
    }

    public interface OnAnimationEndListener {
        void onAnimationEnd();
    }

    public interface OnAnimationUpdatingListener {
        void onUpdating(float value);
    }
}
