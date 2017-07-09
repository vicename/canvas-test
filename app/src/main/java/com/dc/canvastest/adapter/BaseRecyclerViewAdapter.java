package com.dc.canvastest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

/**
 */
abstract class BaseRecyclerViewAdapter<T> extends Adapter<ViewHolder> {

    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 接收传递过来的数据
     */
    private List<T> mDatas;
    /**
     * 获得holder
     */
    private BaseViewHolder baseHolder;

    private int mCurrentPos;
    private OnItemClickRecyclerListener mOnItemClickRecyclerListener;

    private View mHeader;

    public BaseRecyclerViewAdapter(Context mContext, List<T> mDatas) {
        this.mContext = mContext;
        setmDatas(mDatas);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return getItemHolder(mContext, mOnItemClickRecyclerListener, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder != null) {
            baseHolder = (BaseViewHolder) holder;
            baseHolder.setPosition(position);
            baseHolder.setData(mDatas.get(position));
        }

        mCurrentPos = position;
    }

    public void addHeader(View header) {
        mHeader = header;
    }

    public int getCurrentPosition() {
        return mCurrentPos;
    }

    @Override
    public int getItemCount() {
        return mHeader == null ? mDatas.size() : mDatas.size() + 1;
    }

    public List<T> getmDatas() {
        return mDatas;
    }

    public void setmDatas(List<T> mDatas) {
        this.mDatas = mDatas;
    }

    /**
     * 获得Holder
     */
    public abstract BaseViewHolder getItemHolder(Context mContext, OnItemClickRecyclerListener listener, ViewGroup parent);

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickRecyclerListener listener) {
        this.mOnItemClickRecyclerListener = listener;
    }
}