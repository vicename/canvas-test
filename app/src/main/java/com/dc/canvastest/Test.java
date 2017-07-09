package com.dc.canvastest;

import android.graphics.Matrix;
import android.widget.ImageView;

import com.generallibrary.utils.Logger;

/**
 * Created by Li DaChang on 17/5/12.
 * ..-..---.-.--..---.-...-..-....-.
 */

public class Test {
    private static Matrix mDrawMatrix;

    public static void center() {
//        mDrawMatrix = mMatrix;
        mDrawMatrix = new Matrix();
        int dwidth = 1000;
        int dheight = 600;

        int vwidth = 600;
        int vheight = 800;
        float scale;
        float dx = 0, dy = 0;
        //若D比较扁
        if (dwidth * vheight > vwidth * dheight) {
            scale = (float) vheight / (float) dheight;
            dx = (vwidth - dwidth * scale) * 0.5f;
        } else {
            scale = (float) vwidth / (float) dwidth;
            dy = (vheight - dheight * scale) * 0.5f;
        }
        Logger.i(1, "aa:", dx, dy);
        mDrawMatrix.setScale(scale, scale);
        mDrawMatrix.postTranslate(Math.round(dx), Math.round(dy));
    }
}
