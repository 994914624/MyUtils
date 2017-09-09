package com.example.Adapter;

import android.content.Context;

/**
 * @author yang   2016/9/15
 * DATA为数据的泛型
 */
public abstract class MyAdapter<DATA> extends MySuperAdapter<DATA, MySuperAdapter.MyViewHolder> {

    public MyAdapter(Context context,int layoutResID) {
        super(context,layoutResID);
    }





}
