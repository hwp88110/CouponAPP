package com.edu.pu.cs.couponapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.gridviewimage.view.adapter.GridViewImageAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/14.
 */

public class DetailsGridAdapter extends BaseAdapter {
    private List<String> gridItemList;
    private Context context;

    public DetailsGridAdapter(Context context,List<String> list ) {
        this.gridItemList = list;
        this.context = context;

    }


    @Override
    public int getCount()  {
        return gridItemList == null ? 0 : gridItemList.size();
    }


    @Override
    public Object getItem(int i) {
        return gridItemList == null ? null : gridItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        view = layoutInflater.inflate(R.layout.item_detailsgrid, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.img);
        Glide.with(context).load(gridItemList.get(i)).diskCacheStrategy(DiskCacheStrategy.ALL).priority( Priority.HIGH ).crossFade().fitCenter().error(com.example.gridviewimage.R.mipmap.image_error).placeholder(R.drawable.loading).into(imageView);




        return view;
    }
}