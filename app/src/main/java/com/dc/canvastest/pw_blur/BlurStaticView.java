package com.dc.canvastest.pw_blur;

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

import com.dc.canvastest.R;

/**
 * Created by Li DaChang on 17/5/12.
 * ..-..---.-.--..---.-...-..-....-.
 */

public class BlurStaticView extends FrameLayout {
    private ViewLayerController mController;
    private Context mContext;

    public final int TYPE_CUT_NORMAL = 10;
    public final int TYPE_CUT_BOTTOM = 11;
    @ColorInt
    private static final int TRANSPARENT = 0x00000000;
    @ColorInt
    private int overlayColor;

    public BlurStaticView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.BlurView, defStyleAttr, 0);
        overlayColor = a.getColor(R.styleable.BlurView_blurOverlayColor, TRANSPARENT);
        a.recycle();

        setWillNotDraw(false);
    }

    public NormalBlurView.ControllerSettings setUpWith(Context context, View view, Drawable drawable) {
        mController = new ViewLayerController(context, this, view);
        invalidate();
        return new NormalBlurView.ControllerSettings(mController);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void draw(Canvas canvas) {
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
