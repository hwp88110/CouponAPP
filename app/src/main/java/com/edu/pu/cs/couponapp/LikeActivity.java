package com.edu.pu.cs.couponapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wilddog.client.ServerValue;
import com.wilddog.client.SyncError;
import com.wilddog.client.SyncReference;
import com.wilddog.client.WilddogSync;
import com.zhy.autolayout.AutoLayoutActivity;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import qiu.niorgai.StatusBarCompat;

/**
 * Created by Administrator on 2016/10/31.
 */

public class LikeActivity extends AutoLayoutActivity  {
    private ListAdapter adapter;
    private ListView listView;
    private String[] a = new String[10];
    private String map = null;
    SQLiteDatabase db;
    String db_name = "coupon";
    String tb_like_name = "like";
    int like_count;
    TextView title_like;



    // 标题集合
    private List<String> titleList = new ArrayList<String>();
    // 图片地址集合
    private List<String> imgAddressList = new ArrayList<String>();
    // 文本描述集合
    private List<String> ContentList = new ArrayList<String>();

    private ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
    //收藏地址集合
    private List<String> directory = new ArrayList<String>();
    //map集合
    private List<String> mapList = new ArrayList<String>();

    private SwipeRefreshLayout refresh_layout = null;
//    private Toolbar toolbar;
    ImageView Liketitle;

    private String listorgrid = "like";

    private List<String> DetailsimgList = new ArrayList<String>();
    String like_logo = null;
    //有效期
    private String begindate,enddate;
    private long servertimestamp;
    private String DateOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_like);
        Liketitle = (ImageView) findViewById(R.id.liketitle);
        int resource = R.drawable.liketitle;
        Glide.with(LikeActivity.this).load(resource).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().thumbnail(0.1f).error(com.example.gridviewimage.R.mipmap.image_error).into(Liketitle);

        SyncReference liketitle = WilddogSync.getInstance().getReference("liketitle/");
        liketitle.addValueEventListener(new com.wilddog.client.ValueEventListener() {
            @Override
            public void onDataChange(com.wilddog.client.DataSnapshot snapshot) {
                like_logo = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(SyncError error) {

            }
        });


        listView = (ListView) findViewById(R.id.listview_2);
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        title_like = (TextView) findViewById(R.id.title_like);
        refresh_layout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refresh_layout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                判断集合里边是否有数据
                if (titleList.size() == 0 || imgAddressList.size() == 0 || ContentList.size() == 0) {
                    Toast.makeText(LikeActivity.this, "获取数据失败...", Toast.LENGTH_SHORT).show();
                } else {
//                    其实这两个判断可以和在一起，但是我现在我没法测试，所以我就没和，这样比较保险，后期有时间你和起来就行
//                    判断集合中获取出来的某个数据是否为空字符串
                    if (!titleList.get(i).equals("") || !imgAddressList.get(i).equals("") || !ContentList.get(i).equals("")) {
                        Intent in = new Intent(LikeActivity.this, DetailsActivity.class);
                        in.putExtra("title", titleList.get(i));
                        in.putExtra("imgAddress", imgAddressList.get(i));
                        in.putExtra("content", ContentList.get(i));
                        in.putExtra("directory", directory.get(i));
                        in.putExtra("TitleLogo",like_logo);
                        in.putExtra("map",mapList.get(i));
                        in.putExtra("listorgrid",listorgrid);
                        in.putExtra("detailsimgOrnot",DetailsimgList.get(i));

                        startActivity(in);
                    } else {
                        Toast.makeText(LikeActivity.this, "获取数据失败...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                clearData();
                getdbData();
            }
        });
    }



    private void clearData(){
        db = null;
        like_count = 0;
        directory = refreshList(directory);
        imgAddressList = refreshList(imgAddressList);
        titleList = refreshList(titleList);
        ContentList = refreshList(ContentList);
        if (list == null) {
            list = new ArrayList<Map<String, String>>();
        } else {
            list.clear();
        }
    }
    private List<String> refreshList(List<String> list) {
        if (list == null) {
            list = new ArrayList<String>();
        } else {
            list.clear();
        }
        return list;
    }

    @Override
    protected void onStart() {
        super.onStart();
        clearData();
        getdbData();
    }

    private void getdbData() {
        db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
        Cursor cursor = null;
        cursor = db.rawQuery("Select * from like", null);
        while (cursor.moveToNext()) {
            directory.add(cursor.getString(cursor.getColumnIndex("directory")));
            mapList.add(cursor.getString(cursor.getColumnIndex("map")));
            //可以将数据set-》实体类对象集合->add到集合
        }
        like_count = directory.size();
        title_like.setText("已收藏优惠券(" + like_count + ")");
        SyncReference ServerTime = WilddogSync.getInstance().getReference("servertimestamp");
        ServerTime.setValue(ServerValue.TIMESTAMP);
        ServerTime.addListenerForSingleValueEvent(new com.wilddog.client.ValueEventListener() {
            @Override
            public void onDataChange(com.wilddog.client.DataSnapshot snapshot) {
                servertimestamp = (long) snapshot.getValue();
            }
            @Override
            public void onCancelled(SyncError error) {

            }
        });
        if (directory.size() == 0) {
            Toast.makeText(LikeActivity.this, "您还没收藏优惠券呦~", Toast.LENGTH_SHORT).show();
           list.clear();
            adapter = new ListAdapter(list, LikeActivity.this);
            listView.setAdapter(adapter);
            refresh_layout.setRefreshing(false);
        } else {
            for (int i = 0; i < like_count; i++) {

//                mFirelike = new Firebase(directory.get(i));
//                final int finalI = i;
//                mFirelike.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Bean like = dataSnapshot.getValue(Bean.class);
//                        imgAddressList.add(like.getImgaddress());
//                        titleList.add(like.getTitle());
//                        ContentList.add(like.getContent());
//                        DetailsimgList.add(like.getDetailsimg());
//                        Map<String, String> map = new HashMap<String, String>();
//                        map.put("title", like.getTitle());
//                        map.put("content", like.getContent());
//                        map.put("imgAddress", like.getImgaddress());
////                    Log.e("TagActivity",bean.getImgaddress());
//                        list.add(map);
//                        if (finalI == like_count - 1) {
//                            adapter = new ListAdapter(list, LikeActivity.this);
//                            listView.setAdapter(adapter);
//                            refresh_layout.setRefreshing(false);
//                            //dialog.cancel();
//                        }
//                    }
//                    @Override
//                    public void onCancelled(FirebaseError firebaseError) {}
//                });
                   //Wilddog版
                //获取服务器时间

                Log.e("TAG---",directory.get(i).toString());
                SyncReference WDlike = WilddogSync.getInstance().getReference(directory.get(i));
                final int finalI = i;
                WDlike.addValueEventListener(new com.wilddog.client.ValueEventListener() {
                    @Override
                    public void onDataChange(com.wilddog.client.DataSnapshot snapshot) {
                        Map bean = (Map) snapshot.getValue();
                        imgAddressList.add((String) bean.get("imgaddress"));
                        titleList.add((String) bean.get("title"));
                        ContentList.add((String) bean.get("content"));
                        DetailsimgList.add((String) bean.get("detailsimg"));
                        begindate = (String) bean.get("begindate");
                        enddate = (String) bean.get("enddate");
                        //判断是否超出有效期
                        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String begintime = begindate+" " + "00:00:00";
                        String endtime = enddate+ " " + "24:00:00";
                        Date date1 = null;
                        Date date2 = null;
                        try {
                            date1 = format.parse(endtime);
                            date2 = format.parse(begintime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //Log.e("TAG", String.valueOf(date.getTime()));
                        long end = date1.getTime();
                        long begin = date2.getTime();
                        if (begin>servertimestamp) {
                            DateOver = "0";//未到优惠券开始时间、未开始
                            Log.e("TAG---",DateOver);
                        }else if (begin<servertimestamp&&servertimestamp<=end) {
                            DateOver = "1";//优惠券的优惠时间、优惠中
                            Log.e("TAG--",DateOver);
                        }else if (servertimestamp>end) {
                            DateOver = "2";//超过优惠券的结束时间、已过期
                            Log.e("TAG--",DateOver);
                        }
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("title", (String) bean.get("title"));
                        map.put("content", (String) bean.get("content"));
                        map.put("imgAddress", (String) bean.get("imgaddress"));
                        map.put("dateover", DateOver);
                        list.add(map);
                        if (finalI == like_count - 1){
                            adapter = new ListAdapter(list, LikeActivity.this);
                            listView.setAdapter(adapter);
                            refresh_layout.setRefreshing(false);
                        }
                    }
                    @Override
                    public void onCancelled(SyncError error) {}
                });

            }
        }
    }

    public void others(View v) {
        Intent it = new Intent(this, AboutActivity.class);
        startActivity(it);
    }
}


