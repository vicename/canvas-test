package com.dc.canvastest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;

import com.generallibrary.utils.Logger;

/**
 * Created by LiDaChang on 17/4/21.
 * __--__---__-------------__----__
 */

public class SimpleBlur {
    Context mContext;
    private Bitmap outBitmap;
    private RenderScript mRenderScript;
    private ScriptIntrinsicBlur mScriptIntrinsicBlur;

    private SimpleBlur(Context context) {
        mContext = context;
        // 初始化Renderscript，该类提供了RenderScript context，创建其他RS类之前必须先创建这个类，其控制RenderScript的初始化，资源管理及释放
        mRenderScript = RenderScript.create(context);
        // 创建高斯模糊对象
        mScriptIntrinsicBlur = ScriptIntrinsicBlur.create(mRenderScript, Element.U8_4(mRenderScript));
    }

    String filenameTemp = "/mnt/sdcard/temp";

    /**
     * takeScreenShot:
     * TODO 截屏    去掉标题栏
     *
     * @param activity
     */
    public static Bitmap takeScreenShot(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Log.i("TAG", "" + statusBarHeight);

        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    public static SimpleBlur create(Context context) {
        return new SimpleBlur(context);
    }

    public Bitmap blurGo(Bitmap bitmap, Context context, float radius) {
        // 用需要创建高斯模糊bitmap创建一个空的bitmap
        outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        // 创建Allocations，此类是将数据传递给RenderScript内核的主要方法，并制定一个后备类型存储给定类型
        Allocation allIn = Allocation.createFromBitmap(mRenderScript, bitmap);
        Logger.i(12);
        Allocation allOut = Allocation.createFromBitmap(mRenderScript, outBitmap);
        //设定模糊度(注：Radius最大只能设置25.f)
        mScriptIntrinsicBlur.setRadius(radius);
        Logger.i(14);
        // Perform the Renderscript
        mScriptIntrinsicBlur.setInput(allIn);
        mScriptIntrinsicBlur.forEach(allOut);
        Logger.i(15);
        // Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);
        Logger.i(16);
        // recycle the original bitmap
        bitmap.recycle();
        return outBitmap;
    }

    public void recycle() {
        // After finishing everything, we destroy the Renderscript.
        mRenderScript.destroy();
    }

    public static Bitmap blurBitmap(Bitmap bitmap, Context context, float radius) {
        // 用需要创建高斯模糊bitmap创建一个空的bitmap
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        // 初始化Renderscript，该类提供了RenderScript context，创建其他RS类之前必须先创建这个类，其控制RenderScript的初始化，资源管理及释放
        RenderScript rs = RenderScript.create(context);

        // 创建高斯模糊对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // 创建Allocations，此类是将数据传递给RenderScript内核的主要方法，并制定一个后备类型存储给定类型
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //设定模糊度(注：Radius最大只能设置25.f)
        blurScript.setRadius(radius);

        // Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        // Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        // recycle the original bitmap
        bitmap.recycle();

        // After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return outBitmap;
    }

    public static Bitmap blurBitmap(Bitmap bitmap, Context context) {
        return blurBitmap(bitmap, context, 25.f);

    }

    public static Bitmap ratio(String imgPath, float pixelW, float pixelH) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        // Get bitmap info, but notice that bitmap is null now
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        float ww = pixelW;    //设置宽度为120f，可以明显看到图片缩小了
        float hh = pixelH;    //设置高度为240f时，可以明显看到图片缩小了

        //缩放比，由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//表示不缩放
        if (w > h && w > ww) {          //如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {   //如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        // 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        // 压缩好比例大小后再进行质量压缩
        //return compress(bitmap, maxSize); //这里再进行质量压缩的意义不大，反而耗资源，删除
        return bitmap;
    }
}
