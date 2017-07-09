package com.dc.canvastest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import com.dc.canvastest.pw_blur.BlurAlgorithm;
import com.dc.canvastest.pw_blur.RenderScriptBlur;
import com.generallibrary.utils.Logger;

/**
 * Created by Li DaChang on 17/5/4.
 * ..-..---.-.--..---.-...-..-....-.
 */

public class ViewLayerController {
    private View mRootView;
    private View mCurrentView;
    private Bitmap internalBitmap;
    private Canvas internalCanvas;
    private Rect relativeViewBounds = new Rect();
    private BlurAlgorithm blurAlgorithm;
    private Context mContext;
    private static int SCALE_NUM = 5;
    public final int TAG_CUT_TYPE_NORMAL = 10;
    public final int TAG_CUT_TYPE_BOTTOM = 11;
    private int mCutType = TAG_CUT_TYPE_NORMAL;
    private final float DEFAULT_BLUR_RADIUS = 16f;
    private float mBlurRadius = DEFAULT_BLUR_RADIUS;

    public ViewLayerController(Context context, View mCurrentView, View mRootView) {
        this.mRootView = mRootView;
        this.mCurrentView = mCurrentView;
        mContext = context;
        blurAlgorithm = new RenderScriptBlur(mContext);
        int measuredWidth = mCurrentView.getMeasuredWidth();
        int measuredHeight = mCurrentView.getMeasuredHeight();
        init(measuredWidth, measuredHeight);
    }

    private void init(int measuredWidth, int measuredHeight) {
        if (measuredWidth == 0 || measuredHeight == 0) {
            mCurrentView.setWillNotDraw(true);
            return;
        }
        mCurrentView.setWillNotDraw(false);
        internalBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
        internalCanvas = new Canvas(internalBitmap);
    }

    public void radius(float radius) {
        mBlurRadius = radius;
    }

    public void type(int cutType) {
        mCutType = cutType;
    }

    public void drawGo(Canvas canvas) {
        internalCanvas.save();
        getSc();
        drawUnderlyingViews();
        internalCanvas.restore();
        blurGo();
        draw(canvas);
    }

    private void blurGo() {
        internalBitmap = blurAlgorithm.blur(internalBitmap, 12);
    }

    private void draw(Canvas canvas) {
        canvas.save();
        canvas.scale(SCALE_NUM, SCALE_NUM);
        canvas.drawBitmap(internalBitmap, 0, 0, null);
        canvas.restore();
    }

    private void drawUnderlyingViews() {
        mRootView.draw(internalCanvas);
    }

    private void getSc() {
        mCurrentView.getGlobalVisibleRect(relativeViewBounds);
        Logger.i(1, "rect:" + relativeViewBounds);
        float tranX = relativeViewBounds.left;
        float tranY = relativeViewBounds.top;
        float tranBottom = relativeViewBounds.bottom;
        float tranHeight = relativeViewBounds.height();
        mRootView.getGlobalVisibleRect(relativeViewBounds);
        Logger.i(1, "rect:" + relativeViewBounds);
        float sX = relativeViewBounds.left;
        float sY = relativeViewBounds.top;
        float cBottom = relativeViewBounds.bottom;
        float cheight = relativeViewBounds.height();

        float tX = sX - tranX;
        float tY = sY - tranY;
        Logger.i(1, tX, tY);
        if (mCutType == TAG_CUT_TYPE_NORMAL) {
            internalCanvas.translate(tX / SCALE_NUM, tY / SCALE_NUM);
        } else if (mCutType == TAG_CUT_TYPE_BOTTOM) {
            internalCanvas.translate(0, (tranHeight - cheight) / SCALE_NUM);
        }
        internalCanvas.scale(1f / SCALE_NUM, 1f / SCALE_NUM);
        Logger.i(1, "internalCanvas:" + internalCanvas.getWidth() + "," + internalCanvas.getHeight());
    }

}
