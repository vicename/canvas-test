package com.dc.canvastest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.FloatRange;
import android.view.View;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.dc.canvastest.pw_blur.BlurAlgorithm;
import com.dc.canvastest.pw_blur.RenderScriptBlur;
import com.generallibrary.utils.Logger;

/**
 * Created by LiDaChang on 17/5/10.
 * __--__---__-------------__----__
 */

public class BlurTransformation extends BitmapTransformation {

    private static final String ID = "org.ligboy.glide.BlurTransformation";

    public static final float DEFAULT_RADIUS = 25.0f;
    public static final float MAX_RADIUS = 25.0f;
    private static final float DEFAULT_SAMPLING = 1.0f;

    private Context mContext;
    //    private float mSampling = DEFAULT_SAMPLING;
    private float mRadius;
    private int mColor;
    private View mViewTarget;
    private Canvas ccc;
    private float mScaler;

    public static class Builder {

        private Context mContext;
        private float mRadius = DEFAULT_RADIUS;
        private int mColor = Color.TRANSPARENT;

        public Builder(Context mContext) {
            this.mContext = mContext;
        }

        public float getRadius() {
            return mRadius;
        }

        public Builder setRadius(float radius) {
            mRadius = radius;
            return this;
        }

        public int getColor() {
            return mColor;
        }

        public Builder setColor(int color) {
            mColor = color;
            return this;
        }

        public BlurTransformation build() {
            return new BlurTransformation(mContext, mRadius, mColor);
        }

    }

    public BlurTransformation(Context context, @FloatRange(from = 0.0f) float radius, int color) {
        super(context);
        mContext = context;
        if (radius > MAX_RADIUS) {
            mScaler = radius / 25.0f;
            mRadius = MAX_RADIUS;
        } else {
            mRadius = radius;
        }
        mColor = color;
        init();
    }

    public BlurTransformation(Context context, @FloatRange(from = 0.0f) float radius) {
        this(context, radius, Color.TRANSPARENT);
        init();
    }

    public BlurTransformation(Context context) {
        this(context, DEFAULT_RADIUS);
//        mViewTarget.draw(ccc);
        init();
    }

    private void init() {
        mScaler = 8;
    }


    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        boolean needScaled = mScaler == DEFAULT_SAMPLING;
        int originWidth = toTransform.getWidth();
        int originHeight = toTransform.getHeight();
        int width, height;
        if (needScaled) {
            width = originWidth;
            height = originHeight;
        } else {
            width = (int) (originWidth / mScaler);
            height = (int) (originHeight / mScaler);
        }
        Logger.i(1, originWidth, originHeight, width, height, outWidth, outHeight);
//        toTransform = ImageUtil.scalePicByMaxSide(toTransform, 120);
        Bitmap bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        if (mScaler != DEFAULT_SAMPLING) {
            canvas.scale(1 / mScaler, 1 / mScaler);
        }
        canvas.drawBitmap(toTransform, 0, 0, null);
        Logger.i(1, "canvas:", canvas.getWidth(), canvas.getHeight());
        BlurAlgorithm blurAlgorithm = new RenderScriptBlur(mContext);
        bitmap = blurAlgorithm.blur(bitmap, 12);
        blurAlgorithm.destroy();

        int zz = 200;
        int zx = 120;
        Logger.i(1, "width:" + bitmap.getWidth() + ",height:" + bitmap.getHeight());

        float ddd = (float) outHeight / outWidth;
        Logger.i("ddd:" + ddd);
        float ccHeight = bitmap.getWidth() * ddd;
        Logger.i(1, "ccHeight:" + ccHeight);
        int ddY = (int) (bitmap.getHeight() - ccHeight);
        Bitmap result = Bitmap.createBitmap(bitmap, 0, ddY, bitmap.getWidth(), (int) ccHeight);
//        Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, outWidth, outHeight);
        Logger.i(1, "result:", result.getWidth(), result.getHeight());
        return result;
    }

    @Override
    public String getId() {
        StringBuilder sb = new StringBuilder(ID);
        sb.append('-').append(mRadius).append('-').append(mScaler).append("-").append(mColor).append(15);
        return sb.toString();
    }
}