package com.edu.pu.cs.couponapp;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/30.
 */

public class ListAdapter extends BaseAdapter {
    ArrayList<Map<String, String>> data;
    private Context context;

    public ListAdapter(Context context) {
        this.context = context;
    }

    public ListAdapter(ArrayList<Map<String, String>> data, Context context) {
        this.context = context;
        this.data = data;
    }

    public void addList(ArrayList<Map<String, String>> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        //装载view
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list, null, false);
            holder.imageView = (ImageView) convertView.findViewById(R.id.img);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.biaoqian = (TextView) convertView.findViewById(R.id.item_biaoqian);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        //设置对应的标签
        if (data.get(i).get("dateover").equals("0")) {//未开始
            holder.biaoqian.setText("预热中");
            holder.biaoqian.setTextColor(context.getResources().getColor(R.color.colorOrange));
            holder.biaoqian.setBackground(context.getResources().getDrawable(R.drawable.shape_yure));
        } else if (data.get(i).get("dateover").equals("1")) {//优惠中
            holder.biaoqian.setText("优惠中");
            holder.biaoqian.setTextColor(context.getResources().getColor(R.color.colorRed));
            holder.biaoqian.setBackground(context.getResources().getDrawable(R.drawable.shape_cx));
        } else if (data.get(i).get("dateover").equals("2")) {//已过期
            holder.biaoqian.setText("已过期");
            holder.biaoqian.setTextColor(context.getResources().getColor(R.color.colorGray));
            holder.biaoqian.setBackground(context.getResources().getDrawable(R.drawable.shape_guoqi));
        }
        holder.title.setText(data.get(i).get("title"));
        holder.content.setText(Html.fromHtml(data.get(i).get("content")));
        Glide.with(context).load(data.get(i).get("imgAddress")).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade()
                .thumbnail(0.1f).into(holder.imageView);
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView title;
        TextView content;
        TextView biaoqian;
    }

}