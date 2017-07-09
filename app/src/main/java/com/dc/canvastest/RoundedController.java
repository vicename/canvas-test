package com.dc.canvastest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;


/**
 * Created by Li DaChang on 17/5/7.
 * ..-..---.-.--..---.-...-..-....-.
 */

public class RoundedController {


    private float mTopLeftRadius = 0;
    private float mTopRightRadius = 0;
    private float mBottomLeftRadius = 0;
    private float mBottomRightRadius = 0;

    private Paint roundPaint;

    private int mWidth;
    private int mHeight;

    public RoundedController() {
        roundPaint = new Paint();
        roundPaint.setColor(Color.WHITE);
        roundPaint.setAntiAlias(true);
        roundPaint.setStyle(Paint.Style.FILL);
        roundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }

    public RoundedController(float radius) {
        roundPaint = new Paint();
        roundPaint.setColor(Color.WHITE);
        roundPaint.setAntiAlias(true);
        roundPaint.setStyle(Paint.Style.FILL);
        roundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mTopLeftRadius = radius;
        mTopRightRadius = radius;
        mBottomRightRadius = radius;
        mBottomLeftRadius = radius;
    }

    public RoundedController(float mTopLeftRadius, float mTopRightRadius, float mBottomLeftRadius, float mBottomRightRadius) {
        this.mTopLeftRadius = mTopLeftRadius;
        this.mTopRightRadius = mTopRightRadius;
        this.mBottomLeftRadius = mBottomLeftRadius;
        this.mBottomRightRadius = mBottomRightRadius;

    }

    public void setRadius(float radius) {
        mTopLeftRadius = radius;
        mTopRightRadius = radius;
        mBottomRightRadius = radius;
        mBottomLeftRadius = radius;
    }

    public void setRadius(float topLeftRadius, float topRightRadius, float bottomRightRadius, float bottomLeftRadius) {
        this.mTopLeftRadius = topLeftRadius;
        this.mTopRightRadius = topRightRadius;
        this.mBottomRightRadius = bottomRightRadius;
        this.mBottomLeftRadius = bottomLeftRadius;
    }

    public void drawCorners(Canvas canvas, int width, int height) {
        mWidth = width;
        mHeight = height;
        drawTopLeft(canvas);
        drawTopRight(canvas);
        drawBottomLeft(canvas);
        drawBottomRight(canvas);
    }

    private void drawTopLeft(Canvas canvas) {
        if (mTopLeftRadius > 0) {
            Path path = new Path();
            path.moveTo(0, mTopLeftRadius);
            path.lineTo(0, 0);
            path.lineTo(mTopLeftRadius, 0);
            path.arcTo(new RectF(0, 0, mTopLeftRadius * 2, mTopLeftRadius * 2),
                    -90, -90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }

    private void drawTopRight(Canvas canvas) {
        if (mTopRightRadius > 0) {
            int width = mWidth;
            Path path = new Path();
            path.moveTo(width - mTopRightRadius, 0);
            path.lineTo(width, 0);
            path.lineTo(width, mTopRightRadius);
            path.arcTo(new RectF(width - 2 * mTopRightRadius, 0, width,
                    mTopRightRadius * 2), 0, -90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }

    private void drawBottomLeft(Canvas canvas) {
        if (mBottomLeftRadius > 0) {
            int height = mHeight;
            Path path = new Path();
            path.moveTo(0, height - mBottomLeftRadius);
            path.lineTo(0, height);
            path.lineTo(mBottomLeftRadius, height);
            path.arcTo(new RectF(0, height - 2 * mBottomLeftRadius,
                    mBottomLeftRadius * 2, height), 90, 90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }

    private void drawBottomRight(Canvas canvas) {
        if (mBottomRightRadius > 0) {
            int height = mHeight;
            int width = mWidth;
            Path path = new Path();
            path.moveTo(width - mBottomRightRadius, height);
            path.lineTo(width, height);
            path.lineTo(width, height - mBottomRightRadius);
            path.arcTo(new RectF(width - 2 * mBottomRightRadius, height - 2
                    * mBottomRightRadius, width, height), 0, 90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }
}
