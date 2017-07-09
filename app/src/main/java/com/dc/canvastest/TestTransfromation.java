package com.dc.canvastest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.dc.canvastest.pw_blur.BlurAlgorithm;
import com.dc.canvastest.pw_blur.RenderScriptBlur;
import com.generallibrary.utils.Logger;

/**
 * Created by Li DaChang on 17/5/12.
 * ..-..---.-.--..---.-...-..-....-.
 */

public class TestTransfromation extends BitmapTransformation {
    private int mColor;
    private View mTargetView;
    private View mCurrentView;
    private Context mContext;
    private Canvas mCanvas;
    private final float mScaler = 8f;

    public TestTransfromation(Context context) {
        super(context);
    }

    public TestTransfromation(Context context, View mTargetView) {
        super(context);
        this.mTargetView = mTargetView;
    }

    public TestTransfromation(Context context, View mTargetView, View mCurrentView) {
        super(context);
        this.mTargetView = mTargetView;
        this.mCurrentView = mCurrentView;
        this.mContext = context;
    }

    public TestTransfromation(Context context, View mTargetView, View mCurrentView, int color) {
        super(context);
        this.mTargetView = mTargetView;
        this.mCurrentView = mCurrentView;
        this.mContext = context;
        mColor = color;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        Logger.i(9900);
        int originWidth = toTransform.getWidth();
        int originHeight = toTransform.getHeight();
        int width, height;

        width = (int) (originWidth / mScaler);
        height = (int) (originHeight / mScaler);

        Logger.i(1, originWidth, originHeight, width, height, outWidth, outHeight);
        Bitmap bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }
        mCanvas = new Canvas(bitmap);
        mCanvas.scale(1 / mScaler, 1 / mScaler);
        mCanvas.drawBitmap(toTransform, 0, 0, null);
        Logger.i(1, "mCanvas:", mCanvas.getWidth(), mCanvas.getHeight());
        Logger.i(1, "bitmap:", bitmap.getWidth(), bitmap.getHeight());
        Logger.i(1, "bitmap:", bitmap.getWidth(), bitmap.getHeight());
        BlurAlgorithm blurAlgorithm = new RenderScriptBlur(mContext);
        bitmap = blurAlgorithm.blur(bitmap, 10);
        blurAlgorithm.destroy();
        if (mColor > 0) {
            mCanvas.drawColor(mColor);
        }
        int dwidth = width;
        int dheight = height;

        int vwidth = mTargetView.getWidth();
        int vheight = mTargetView.getHeight();
        int cWidth = mCurrentView.getWidth();
        int cHeight = mCurrentView.getHeight();
        float scale;
        float dx = 0, dy = 0;
        int ww;
        int hh;
        float zx = 0;
        float zy = 0;
        //若D比较扁
        if (dwidth * vheight > vwidth * dheight) {
            scale = (float) vheight / (float) dheight;
            dx = (vwidth - dwidth * scale) * 0.5f;
            hh = height;
            ww = (int) ((float) vwidth / scale);
            zx = dx / scale;
        } else {
            scale = (float) vwidth / (float) dwidth;
            dy = (vheight - dheight * scale) * 0.5f;
            ww = width;
            hh = (int) ((float) vheight / scale);
            zy = dy / scale;
        }
        float scale2 = cWidth / ww;
        int cW = ww;
        int cH = (int) (cHeight / scale2);
        Bitmap cBitmap = Bitmap.createBitmap(bitmap, (int) -zx, height - cH, cW, cH);
        Logger.i(9900);
        return cBitmap;
    }

    public void centerCrop(int cW, int cH, int tW, int tH) {
        cW = 1000;
        cH = 600;
        tW = 800;
        tH = 600;
        float sss = (float) tW / tH;
//        int
        int x;
        int y;
        int xEnd;
        int yEnd;
        boolean isFatter = false;
        if (isFatter) {
            x = 0;
            y = (cH - tH) / 2;
            xEnd = cW;
            yEnd = cH - y;

        } else {
            y = 0;
            yEnd = cH;
            x = (cW - tW) / 2;
            xEnd = cW - x;
        }
    }


    @Override
    public String getId() {
        return String.valueOf(System.currentTimeMillis());
    }

}
