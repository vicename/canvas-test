package com.dc.canvastest;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.dc.canvastest.pw_blur.BlurView;
import com.generallibrary.base.LibBaseActivity;

public class MainActivity extends LibBaseActivity {

    private ImageView mIvM1;
    private ImageView mIvM2;
    private NormalBlurView mTop1;
    private BlurView mBlurView;

    @Override
    protected void initVar() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void loadData() {

    }

    public void clickGoSc(View view) {
        startActivity(new Intent(mContext, ScActivity.class));
    }

    public void clickGoRc(View view) {
        startActivity(new Intent(mContext, RcActivity.class));

    }

    public void clickGoWall(View view) {
        startActivity(new Intent(mContext, RcActivity.class));
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == 11) {
//            mTop1.setUpWith(mContext, mIvM1).radius(2).type(mTop1.TYPE_CUT_BOTTOM);
//            mTop1.setConnerRadius(22);
        }
    }
}
