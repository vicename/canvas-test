package com.dc.canvastest.pw_blur.wall;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.dc.canvastest.ImageUtil;
import com.dc.canvastest.pw_blur.RenderScriptBlur;
import com.generallibrary.utils.Logger;

/**
 * Created by Li DaChang on 17/7/8.
 * ..-..---.-.--..---.-...-..-....-.
 */

public class AccentBlurWall extends FrameLayout {
    private final RenderScriptBlur blurAlgorithm;
    private Canvas mCanvas;
    private static final int SCALED_SIZE = 400;
    private static final float SCALE_PRE = 3f;
    private static final int ANIM_DURATION = 300;
    private static final int UPDATE_HZ = 20;
    private static final float STATIC_RADIUS = 10f;
    private float mActiveRadius;
    private Bitmap mScreenBitmap;

    private Bitmap mBitmapToDraw;
    private SimpleBlur mSimpleBlur;

    private Activity mActivity;
    private Bitmap internalBitmap;
    private View viewScreenShot;
    private float mScaler;
    private ValueAnimator mVa;
    private boolean isReverse;
    private ONGOGO mlisentere;
    private int color;

    public AccentBlurWall(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mActivity = (Activity) context;
        blurAlgorithm = new RenderScriptBlur(context);
//        mCanvas = new Canvas();
        init();
    }

    public void init() {
        setWillNotDraw(false);

    }

    public void setUP() {
//        setVisibility(INVISIBLE);

        refresh(mActivity);
        final float limit = 0.25f;
        mVa = ValueAnimator.ofFloat(0.01f, 1);
        mVa.setDuration(700);
//        mVa.setInterpolator(new AccelerateInterpolator());
        mVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mActiveRadius = value * 22f * (1 / limit);
                if (mActiveRadius > 22f) {
                    mActiveRadius = 22;
                }
                float alpha = value * (1 / limit);
                if (alpha > 1) {
                    alpha = 1;
                }
//                setAlpha(alpha);
                int colorAlpha = (int) ((value - 0.5f) * 100);
                if (colorAlpha < 0) {
                    colorAlpha = 0;
                }
                color = ColorUtils.setAlphaComponent(Color.WHITE, colorAlpha);
                if (value >= limit) {
                    mScaler = (value - limit) * 2f + 1f;
                }
//                Logger.i(1, "anim go:" + mScaler);
                invalidate();
            }
        });
        mVa.addListener(new OnAnimatorEndListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (isReverse) {
                    if (mlisentere != null) {
                        mlisentere.onReversEnd();
                        isReverse = false;
                    }
                }
            }
        });
    }

    public void refresh(Activity activity) {
        mActiveRadius = 0.1f;
        mScaler = 1f;
        color = Color.parseColor("#99ffffff");
        viewScreenShot = activity.getWindow().getDecorView();
        mScreenBitmap = ImageUtil.getBitmapFromView(viewScreenShot);
//        mScreenBitmap = ImageUtil.scalePicByMaxSide(mScreenBitmap, 400);
        //使用矩阵处理图片
        Matrix matrix = new Matrix();
        matrix.postScale(1f / SCALE_PRE, 1f / SCALE_PRE);
//        //生成新的bitmap
        mScreenBitmap = Bitmap.createBitmap(mScreenBitmap, 0, 0, mScreenBitmap.getWidth(), mScreenBitmap.getHeight(), matrix, true);
        internalBitmap = Bitmap.createBitmap(mScreenBitmap.getWidth(), mScreenBitmap.getHeight(), Bitmap.Config.ARGB_4444);
//        internalBitmap = Bitmap.createBitmap((int) (viewScreenShot.getMeasuredWidth() / SCALE_PRE), (int) (viewScreenShot.getMeasuredHeight() / SCALE_PRE), Bitmap.Config.ARGB_4444);
        Logger.i(1, "width:" + internalBitmap.getWidth());
        mCanvas = new Canvas(internalBitmap);
//        mCanvas.scale(1f / SCALE_PRE/2, 1f / SCALE_PRE);
    }

    public void doBlur() {
        mVa.start();
    }

    public void reverse() {
        isReverse = true;
        mVa.reverse();
    }

    @Override
    public void draw(Canvas canvas) {
        if (mScreenBitmap != null) {
            mCanvas.save();
            mCanvas.scale(1f / mScaler, 1f / mScaler);
//            viewScreenShot.draw(mCanvas);
            mCanvas.drawBitmap(mScreenBitmap, 0, 0, null);
            Logger.i(1, "mCanvas width:" + mCanvas.getWidth());
            mCanvas.restore();
            Logger.i(1, "width:" + internalBitmap.getWidth());
            mBitmapToDraw = blurAlgorithm.blur(internalBitmap, mActiveRadius);
            Logger.i(1, "draw bit width:" + mBitmapToDraw.getWidth());
            canvas.save();
//            canvas.translate(-100, -100);
            Logger.i(1, "canvas width:" + canvas.getWidth());
            canvas.scale(SCALE_PRE, SCALE_PRE);
            canvas.scale(mScaler, mScaler);
            Logger.i(1, "canvas width:" + canvas.getWidth());
            canvas.drawBitmap(mBitmapToDraw, 0, 0, null);
            canvas.restore();
            canvas.drawColor(color);
        }
        super.draw(canvas);
    }

    public Bitmap getBitmap() {
        return mScreenBitmap;
    }

    public void recycle() {
        blurAlgorithm.destroy();
        if (mSimpleBlur != null) {
            mSimpleBlur.recycle();
            mSimpleBlur = null;
        }
//        mDrawableBg = null;
        if (mScreenBitmap != null) {
            mScreenBitmap.recycle();
        }
        mScreenBitmap = null;
//        added_radius = 1f;
    }

    public void setOnEndListener(ONGOGO listener) {
        mlisentere = listener;
    }

    public interface ONGOGO {
        void onReversEnd();
    }

}
