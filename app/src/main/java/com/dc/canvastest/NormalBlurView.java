package com.dc.canvastest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.generallibrary.utils.Logger;


/**
 * Created by Li DaChang on 17/5/4.
 * ..-..---.-.--..---.-...-..-....-.
 */

public class NormalBlurView extends FrameLayout {
    private ViewLayerController mController;
    private Context mContext;

    public final int TYPE_CUT_NORMAL = 10;
    public final int TYPE_CUT_BOTTOM = 11;
    @ColorInt
    private static final int TRANSPARENT = 0x00000000;
    @ColorInt
    private int overlayColor;
    private RoundedController mRoundedController;

    private float mRadius;
    private float mTopLeftRadius;
    private float mTopRightRadius;
    private float mBottomLeftRadius;
    private float mBottomRightRadius;

    public NormalBlurView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.BlurView, defStyleAttr, 0);
        overlayColor = a.getColor(R.styleable.BlurView_blurOverlayColor, TRANSPARENT);
        a.recycle();
        mRadius = 0;
        TypedArray b = getContext().obtainStyledAttributes(attrs, R.styleable.RoundCorner, defStyleAttr, 0);
        mRadius = b.getDimension(R.styleable.RoundCorner_cornerRadius, 0);
        mTopLeftRadius = b.getDimension(R.styleable.RoundCorner_topLeftRadius, mRadius);
        mTopRightRadius = b.getDimension(R.styleable.RoundCorner_topRightRadius, mRadius);
        mBottomRightRadius = b.getDimension(R.styleable.RoundCorner_bottomRightRadius, mRadius);
        mBottomLeftRadius = b.getDimension(R.styleable.RoundCorner_bottomLeftRadius, mRadius);
        Logger.i(1, mRadius, mTopLeftRadius, mTopRightRadius, mBottomRightRadius, mBottomLeftRadius);
        b.recycle();

        setWillNotDraw(false);
    }

    public ControllerSettings setUpWith(Context context, View view) {
        Logger.i(51);
        mController = new ViewLayerController(context, this, view);
        if (mRadius + mTopLeftRadius + mTopRightRadius + mBottomRightRadius + mBottomLeftRadius > 0) {
            if (mRoundedController == null) {
                mRoundedController = new RoundedController(mRadius);
            }
            mRoundedController.setRadius(mTopLeftRadius, mTopRightRadius, mBottomRightRadius, mBottomLeftRadius);
        }
        invalidate();
        return new ControllerSettings(mController);
    }


    public void setConnerRadius(float radius) {
        Logger.i(52);
        mRadius = radius;
        mTopLeftRadius = radius;
        mTopRightRadius = radius;
        mBottomRightRadius = radius;
        mBottomLeftRadius = radius;
        if (mRoundedController != null) {
            mRoundedController.setRadius(mRadius);
        } else {
            mRoundedController = new RoundedController(radius);
        }
        invalidate();
    }

    public void setConnerRadius(float topLeftRadius, float topRightRadius, float bottomRightRadius, float bottomLeftRadius) {
        mTopLeftRadius = topLeftRadius;
        mTopRightRadius = topRightRadius;
        mBottomRightRadius = bottomRightRadius;
        mBottomLeftRadius = bottomLeftRadius;
        if (mRoundedController != null) {
            mRoundedController.setRadius(topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius);
        } else {
            mRoundedController = new RoundedController(topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius);
        }
        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        Logger.i(122);
        if (mController == null) {
            return;
        }
        if (canvas.isHardwareAccelerated()) {
            mController.drawGo(canvas);
            drawColorOverlay(canvas);

            super.draw(canvas);
        } else if (!isHardwareAccelerated()) {
            //if view is in a not hardware accelerated window, don't draw blur
            super.draw(canvas);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Logger.i(121);
        super.onDraw(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Logger.i(123);
        if (mRoundedController != null) {
            Logger.i(1, "draw corner!==" + mRadius);
            mRoundedController.drawCorners(canvas, getWidth(), getHeight());
        }
    }

    private void drawColorOverlay(Canvas canvas) {
        canvas.drawColor(overlayColor);
    }

    public static class ControllerSettings {
        ViewLayerController blurController;

        ControllerSettings() {
        }

        ControllerSettings(ViewLayerController blurController) {
            this.blurController = blurController;
        }

        public ControllerSettings radius(float radius) {
            blurController.radius(radius);
            return this;
        }

        public ControllerSettings type(int type) {
            blurController.type(type);
            return this;
        }

//        public ControllerSettings windowBackground(@Nullable Drawable windowBackground) {
//            blurController.setWindowBackground(windowBackground);
//            return this;
//        }
    }
}
