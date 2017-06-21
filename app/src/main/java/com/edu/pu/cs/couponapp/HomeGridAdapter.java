package com.edu.pu.cs.couponapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.stream.StreamStringLoader;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/3.
 */

public class HomeGridAdapter extends BaseAdapter {
    ArrayList<Map<String, String>> data;
    private Context context;

    public HomeGridAdapter(ArrayList<Map<String, String>> data, Context context) {
        this.data = data;
        this.context = context;



    }
    @Override
    public int getCount()  {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int i) {
        return data == null ? null : data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //装载view
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        view = layoutInflater.inflate(R.layout.item_homegridview, null);
        TextView textlogo = (TextView) view.findViewById(R.id.textlogo);
        System.out.println(data.get(i).get("textlogo")+"------------textlogo");
        System.out.println(data.get(i).get("imgAddress")+"------------imgAddress");
        System.out.println(data.size() + "----------data.size");
        ImageView imageView = (ImageView) view.findViewById(R.id.imglogo);
        textlogo.setText(data.get(i).get("textlogo"));

        Glide.with(context).load(data.get(i).get("imgAddress")).diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().thumbnail(0.1f).error(com.example.gridviewimage.R.mipmap.image_error).placeholder(com.example.gridviewimage.R.mipmap.loading).into(imageView);
        return view;
    }
}
