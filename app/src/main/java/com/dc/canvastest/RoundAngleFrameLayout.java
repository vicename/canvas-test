package com.dc.canvastest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.generallibrary.utils.Logger;

/**
 * Created by Li DaChang on 17/5/7.
 * ..-..---.-.--..---.-...-..-....-.
 */

public class RoundAngleFrameLayout extends FrameLayout {

    private float topLeftRadius;
    private float topRightRadius;
    private float bottomLeftRadius;
    private float bottomRightRadius;

    private float mRadius;
    private float mTopLeftRadius;
    private float mTopRightRadius;
    private float mBottomLeftRadius;
    private float mBottomRightRadius;

    private Paint roundPaint;
    private Paint imagePaint;

    public RoundAngleFrameLayout(Context context) {
        this(context, null);
    }

    public RoundAngleFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundAngleFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (attrs != null) {
//            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundAngleFrameLayout);
//            float radius = ta.getDimension(R.styleable.RoundAngleFrameLayout_radius, 0);
//            topLeftRadius = ta.getDimension(R.styleable.RoundAngleFrameLayout_topLeftRadius, radius);
//            topRightRadius = ta.getDimension(R.styleable.RoundAngleFrameLayout_topRightRadius, radius);
//            bottomLeftRadius = ta.getDimension(R.styleable.RoundAngleFrameLayout_bottomLeftRadius, radius);
//            bottomRightRadius = ta.getDimension(R.styleable.RoundAngleFrameLayout_bottomRightRadius, radius);
//            ta.recycle();
            TypedArray b = getContext().obtainStyledAttributes(attrs, R.styleable.RoundCorner, defStyle, 0);
            mRadius = b.getDimension(R.styleable.RoundCorner_cornerRadius, 0);
            topLeftRadius = b.getDimension(R.styleable.RoundCorner_topLeftRadius, mRadius);
            topRightRadius = b.getDimension(R.styleable.RoundCorner_topRightRadius, mRadius);
            bottomRightRadius = b.getDimension(R.styleable.RoundCorner_bottomRightRadius, mRadius);
            bottomLeftRadius = b.getDimension(R.styleable.RoundCorner_bottomLeftRadius, mRadius);
            Logger.i(1, mRadius, mTopLeftRadius, mTopRightRadius, mBottomRightRadius, mBottomLeftRadius);
            b.recycle();
        }
        roundPaint = new Paint();
        roundPaint.setColor(Color.WHITE);
        roundPaint.setAntiAlias(true);
        roundPaint.setStyle(Paint.Style.FILL);
        roundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        imagePaint = new Paint();
        imagePaint.setXfermode(null);
    }
    ////实现1
//    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        int width = getWidth();
//        int height = getHeight();
//        Path path = new Path();
//        path.moveTo(0, topLeftRadius);
//        path.arcTo(new RectF(0, 0, topLeftRadius * 2, topLeftRadius * 2), -180, 90);
//        path.lineTo(width - topRightRadius, 0);
//        path.arcTo(new RectF(width - 2 * topRightRadius, 0, width, topRightRadius * 2), -90, 90);
//        path.lineTo(width, height - bottomRightRadius);
//        path.arcTo(new RectF(width - 2 * bottomRightRadius, height - 2 * bottomRightRadius, width, height), 0, 90);
//        path.lineTo(bottomLeftRadius, height);
//        path.arcTo(new RectF(0, height - 2 * bottomLeftRadius, bottomLeftRadius * 2, height), 90, 90);
//        path.close();
//        canvas.clipPath(path);
//        super.dispatchDraw(canvas);
//    }
    ////实现2
    //    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        super.dispatchDraw(canvas);
//        drawTopLeft(canvas);//用PorterDuffXfermode
//        drawTopRight(canvas);//用PorterDuffXfermode
//        drawBottomLeft(canvas);//用PorterDuffXfermode
//        drawBottomRight(canvas);//用PorterDuffXfermode
//    }
    ////实现3
//    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas newCanvas = new Canvas(bitmap);
//        super.dispatchDraw(newCanvas);
//        drawTopLeft(newCanvas);
//        drawTopRight(newCanvas);
//        drawBottomLeft(newCanvas);
//        drawBottomRight(newCanvas);
//        canvas.drawBitmap(bitmap, 0, 0, imagePaint);
////        invalidate();
//    }

    //实现4
    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), imagePaint, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        drawTopLeft(canvas);
        drawTopRight(canvas);
        drawBottomLeft(canvas);
        drawBottomRight(canvas);
        canvas.restore();
    }

    private void drawTopLeft(Canvas canvas) {
        if (topLeftRadius > 0) {
            Path path = new Path();
            path.moveTo(0, topLeftRadius);
            path.lineTo(0, 0);
            path.lineTo(topLeftRadius, 0);
            path.arcTo(new RectF(0, 0, topLeftRadius * 2, topLeftRadius * 2),
                    -90, -90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }

    private void drawTopRight(Canvas canvas) {
        if (topRightRadius > 0) {
            int width = getWidth();
            Path path = new Path();
            path.moveTo(width - topRightRadius, 0);
            path.lineTo(width, 0);
            path.lineTo(width, topRightRadius);
            path.arcTo(new RectF(width - 2 * topRightRadius, 0, width,
                    topRightRadius * 2), 0, -90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }

    private void drawBottomLeft(Canvas canvas) {
        if (bottomLeftRadius > 0) {
            int height = getHeight();
            Path path = new Path();
            path.moveTo(0, height - bottomLeftRadius);
            path.lineTo(0, height);
            path.lineTo(bottomLeftRadius, height);
            path.arcTo(new RectF(0, height - 2 * bottomLeftRadius,
                    bottomLeftRadius * 2, height), 90, 90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Logger.i(1, "widddd:" + widthMeasureSpec + "," + heightMeasureSpec);
    }

    private void drawBottomRight(Canvas canvas) {
        if (bottomRightRadius > 0) {
            getMeasuredWidth();
            Logger.i(1, "height:" + getHeight());
            Logger.i(1, "width:" + getWidth());
            Logger.i(1, "width:" + getMeasuredWidth());
            int lWidth = getLayoutParams().width;
            int lHeight = getLayoutParams().height;
//            Logger.i(1, "width:" + h);
//            int height = getHeight();
//            int width = getWidth();
            int height = lHeight;
            int width = lWidth;
            Path path = new Path();
            path.moveTo(width - bottomRightRadius, height);
            path.lineTo(width, height);
            path.lineTo(width, height - bottomRightRadius);
            path.arcTo(new RectF(width - 2 * bottomRightRadius, height - 2
                    * bottomRightRadius, width, height), 0, 90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }

}