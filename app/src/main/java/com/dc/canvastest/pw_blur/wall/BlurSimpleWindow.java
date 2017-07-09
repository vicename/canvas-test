package com.dc.canvastest.pw_blur.wall;

import android.content.Context;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.dc.canvastest.R;
import com.generallibrary.utils.Logger;

/**
 * Created by LiDaChang on 17/5/23.
 * __--__---__-------------__----__
 */

public class BlurSimpleWindow extends PopupWindow {
    //文件保存的路径
    public static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache/pics";
    private View mIvLogo;
    private View mBox;
    private Context mContext;
    private BlurWall mBg;
    private View mViewWindowBg;
    private OnShowAnimEndListener mListener;
    private View mViewBlurCover;

    private BlurSimpleWindow() {
        super();
    }

    public BlurSimpleWindow(Context context) {
        super(context);
        mContext = context;
        init();
    }

    private void init() {
        setBackgroundDrawable(null);
        setTouchable(true);
        setFocusable(true);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setAnimationStyle(R.style.popupWindow1);

        View content = LayoutInflater.from(mContext).inflate(R.layout.layout_simple_blur_window, null);
        setContentView(content);
        mBg = (BlurWall) content.findViewById(R.id.blur_bg);
        mBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dismiss();
            }
        });
        mBg.setOnUpdatingListener(new OnUpdatingListener());
        mViewWindowBg = content.findViewById(R.id.view_window_cover);
        mViewBlurCover = content.findViewById(R.id.view_blur_cover);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                Logger.i(1, "dis!!");
                mBg.clearAnimation();
                mBg.recycle();
                mBg = null;
            }
        });
    }

    public void show() {
        if (this.isShowing()) {
            return;
        }
        showAtLocation(mBg, Gravity.TOP, 0, 0);
        mBg.doBlur();
        mBg.setAnimationEndListener(new BlurWall.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                if (mListener != null) {
                    mListener.onEnd();
                }
            }
        });
    }

    public void setOnShowAnimEndListener(OnShowAnimEndListener listener) {
        mListener = listener;
    }

    private class OnUpdatingListener implements BlurWall.OnAnimationUpdatingListener {

        @Override
        public void onUpdating(float value) {
            mViewWindowBg.setAlpha(value);
//            mViewBlurCover.setAlpha(value / 2);
        }
    }

    public interface OnShowAnimEndListener {
        void onEnd();
    }

}
