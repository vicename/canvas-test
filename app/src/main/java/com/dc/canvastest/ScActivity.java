package com.dc.canvastest;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.dc.canvastest.pw_blur.wall.AccentBlurWall;
import com.dc.canvastest.pw_blur.wall.BlurAccentWindow;
import com.dc.canvastest.pw_blur.wall.BlurSimpleWindow;
import com.generallibrary.base.LibBaseActivity;
import com.generallibrary.utils.Logger;

import java.util.logging.Handler;

public class ScActivity extends LibBaseActivity {
    private BlurAccentWindow mWindow;
    private ImageView mIvBase1;
    private ImageView mIvBlur1;
    private View mBtnShowWindow;
    private AccentBlurWall mBlur;

    @Override
    protected void initVar() {
        Test.center();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_sc);
        mIvBase1 = ((ImageView) findViewById(R.id.iv_base1));
        mIvBlur1 = ((ImageView) findViewById(R.id.iv_blur1));
        mBtnShowWindow = findViewById(R.id.btn_show_window);
        mBtnShowWindow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mWindow == null) {
                    mWindow = new BlurAccentWindow(mContext);
                }
                mWindow.show();
                mWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mWindow = null;
                    }
                });
            }
        });

        mBlur = ((AccentBlurWall) findViewById(R.id.view_a_blur));
    }

    @Override
    protected void initListener() {

    }


    @Override
    protected void loadData() {
        Glide.with(mContext).load(CommonDefine.TEST_URL).crossFade(400).into(mIvBase1);
        Glide.with(mContext).load(CommonDefine.TEST_URL).crossFade(400).transform(new TestTransfromation(mContext, mIvBase1, mIvBlur1, ContextCompat.getColor(mContext, R.color.transOverBlack))).into(mIvBlur1);

    }

    @Override
    public void handleMessage(Message msg) {

    }

}
