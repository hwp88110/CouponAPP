package com.edu.pu.cs.couponapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.edu.pu.cs.couponapp.ui.HomePicAdapter;
import com.edu.pu.cs.couponapp.ui.TabShow;
import com.github.anzewei.parallaxbacklayout.ParallaxActivityBase;
import com.hejunlin.superindicatorlibray.CircleIndicator;
import com.hejunlin.superindicatorlibray.LoopViewPager;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.wilddog.client.SyncError;
import com.wilddog.client.SyncReference;
import com.wilddog.client.WilddogSync;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import qiu.niorgai.StatusBarCompat;


public class HomeActivity extends AutoLayoutActivity implements OnClickListener {
    private ImageView img1, img2, img3;
    private GridView gridview2,gridview3;
    private HomeGridAdapter adapter;
    private String[] b2 = new String[10];
    private String[] b3 = new String[10];

    private List<String> textlogoGird = new ArrayList<String>();
    // 图片地址集合
    private List<String> imgAddressGrid = new ArrayList<String>();
    //logo缩写
    private List<String> abbreviationGrid = new ArrayList<String>();

    private ArrayList<Map<String, String>> grid2 = new ArrayList<Map<String, String>>();
    private ArrayList<Map<String, String>> grid3 = new ArrayList<Map<String, String>>();

    //商家数据url集合，传参到Coupon_ListActivity
    private List<String> urlList2 = new ArrayList<String>();
    private List<String> urlList3 = new ArrayList<String>();
    //商家数据条数counturl集合，传参到Coupon_ListActivity
    private List<String> counturlList2 = new ArrayList<String>();
    private List<String> counturlList3 = new ArrayList<String>();
    //商家位置map集合，传参到Coupon_ListActivity
    private List<String> mapList2 = new ArrayList<String>();
    private List<String> mapList3 = new ArrayList<String>();
    //固定的数据库url
    String databaseurl = "https://coupon-ed7bf.firebaseio.com/coupon_";
    String databaseurl_wd = "coupon_";
    //map的固定形式
    String mapurl = "geo:0,0?q=";

    String db_name = "coupon";
    String tb_like_name = "like";
    String tb_search_name = "CouponList";
    SQLiteDatabase db;
    //广告位信息
    String adimgaddress = null;
    String adcontent = null;
    String adtitle = null;
    String adwebsite ;
    // 标题集合

    // 文本描述集合
    List<Map<String,String>> list = new ArrayList();
    private String[]a = new String[4];
    LoopViewPager viewpager;
    CircleIndicator indicator;

    ImageView Hometitle,Adimageview;
    TextView Adtitle,Adcontent;
    RelativeLayout Adview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setBackEnable(false);
        setContentView(R.layout.home);
//        StatusBarCompat.setStatusBarColor(HomeActivity.this,getResources().getColor(R.color.background_color_orange));
        gridview2 = (GridView) findViewById(R.id.gridview2);
        gridview3 = (GridView) findViewById(R.id.gridview3);
        gridview2.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridview3.setSelector(new ColorDrawable(Color.TRANSPARENT));

        initView();
        String resource = "http://couponapp.image.alimmdn.com/titlelogo/hometitle.png?t=1487999390623";
        Glide.with(HomeActivity.this).load(resource).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().thumbnail(0.1f).error(com.example.gridviewimage.R.mipmap.image_error).into(Hometitle);


        //banner 轮播图片加载及点击事件(Firebase版)
//         viewpager = (LoopViewPager) findViewById(R.id.viewpager);
//         indicator = (CircleIndicator) findViewById(R.id.indicator);
//        String banner = "https://coupon-ed7bf.firebaseio.com/banner/banner";
//        for (int i=0;i<4;i++){
//            int p = i+1;
//            a[i] = banner + p;
//            mFirebaseBanner = new Firebase(a[i]);
//            final int finalI = i;
//            mFirebaseBanner.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Bean banner = dataSnapshot.getValue(Bean.class);
//                    System.out.println("hotlogo=" + banner.getImgaddress());
//                    Map<String,String> map = new HashMap<String, String>();
//                    map.put("imageAddress",banner.getImgaddress());
//                    map.put("title",banner.getTitle());
//                    map.put("content",banner.getContent());
//                    map.put("directory",a[finalI]);
//                    map.put("hotlogo",banner.getHotlogo());
//                    map.put("map",mapurl+banner.getTextlogo());
//                    map.put("detailsimgOrnot",banner.getDetailsimg());
//                    list.add(map);
//                    viewpager.setAdapter(new HomePicAdapter(HomeActivity.this, list));
//                    viewpager.setLooperPic(true);//是否设置自动轮播
//                    indicator.setViewPager(viewpager);
//                }
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {}
//            });
//        }
           //Wilddog版
        viewpager = (LoopViewPager) findViewById(R.id.viewpager);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        String banner = "banner/banner";
        for (int i=0;i<4;i++){
            int p = i+1;
            a[i] = banner + p;
            SyncReference WDbanner = WilddogSync.getInstance().getReference(a[i]);
            final int finalI = i;
            WDbanner.addValueEventListener(new com.wilddog.client.ValueEventListener() {
                @Override
                public void onDataChange(com.wilddog.client.DataSnapshot snapshot) {
                    Map banner = (Map) snapshot.getValue();
                    Map<String,String> map = new HashMap<String, String>();
                    map.put("imageAddress", (String) banner.get("imgaddress"));
                    map.put("title",(String) banner.get("title"));
                    map.put("content",(String) banner.get("content"));
                    map.put("directory",a[finalI]);
                    map.put("hotlogo",(String) banner.get("hotlogo"));
                    map.put("map",(String) banner.get("textlogo"));
                    map.put("detailsimgOrnot",(String) banner.get("detailsimg"));
                    list.add(map);
                    viewpager.setAdapter(new HomePicAdapter(HomeActivity.this, list));
                    viewpager.setLooperPic(true);//是否设置自动轮播
                    indicator.setViewPager(viewpager);
                }
                @Override
                public void onCancelled(SyncError error) {}
            });
        }

        //加载第一排商户logo
        String imglogo1 = "http://couponapp.image.alimmdn.com/Logo/KFC.png?t=1487955075694";
        String imglogo2 = "http://couponapp.image.alimmdn.com/Logo/McDonald.png?t=1487955075602";
        String imglogo3 = "http://couponapp.image.alimmdn.com/Logo/BurgerKing.png?t=1487955075648";
        Glide.with(HomeActivity.this).load(imglogo1).diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().thumbnail(0.1f).error(com.example.gridviewimage.R.mipmap.image_error).placeholder(com.example.gridviewimage.R.mipmap.loading).into(img1);
        Glide.with(HomeActivity.this).load(imglogo2).diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().thumbnail(0.1f).error(com.example.gridviewimage.R.mipmap.image_error).placeholder(com.example.gridviewimage.R.mipmap.loading).into(img2);
        Glide.with(HomeActivity.this).load(imglogo3).diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().thumbnail(0.1f).error(com.example.gridviewimage.R.mipmap.image_error).placeholder(com.example.gridviewimage.R.mipmap.loading).into(img3);

        //建数据库，建like表
        db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
        SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        SharedPreferences.Editor editor = sharedPreferences.edit();
           //Firebase版
//        if (isFirstRun) {
//            String createTable = "CREATE TABLE IF NOT EXISTS " + tb_like_name + "(_id integer primary key autoincrement,directory TEXT,map TEXT)";
//            db.execSQL(createTable);
//
//
//            String createSearchTable = "CREATE TABLE IF NOT EXISTS " + tb_search_name + "(_id integer primary key autoincrement,name TEXT,chinese TEXT,url TEXT,counturl TEXT)";
//            db.execSQL(createSearchTable);
//
//            db.execSQL("insert into CouponList (name,chinese,url,counturl) values('kfc','肯德基','https://coupon-ed7bf.firebaseio.com/coupon_kfc/kfc_','https://coupon-ed7bf.firebaseio.com/coupon_kfc')");
//            db.execSQL("insert into CouponList (name,chinese,url,counturl) values('mc','麥當勞','https://coupon-ed7bf.firebaseio.com/coupon_mc/mc_','https://coupon-ed7bf.firebaseio.com/coupon_mc')");
//            db.execSQL("insert into CouponList (name,chinese,url,counturl) values('bk','漢堡王','https://coupon-ed7bf.firebaseio.com/coupon_bk/bk_','https://coupon-ed7bf.firebaseio.com/coupon_bk')");
//        }else
//        {
//            db.delete("CouponList", null, null);
//            String createTable = "CREATE TABLE IF NOT EXISTS " + tb_like_name + "(_id integer primary key autoincrement,directory TEXT,map TEXT)";
//            db.execSQL(createTable);
//
//
//            String createSearchTable = "CREATE TABLE IF NOT EXISTS " + tb_search_name + "(_id integer primary key autoincrement,name TEXT,chinese TEXT,url TEXT,counturl TEXT)";
//            db.execSQL(createSearchTable);
//
//            db.execSQL("insert into CouponList (name,chinese,url,counturl) values('kfc','肯德基','https://coupon-ed7bf.firebaseio.com/coupon_kfc/kfc_','https://coupon-ed7bf.firebaseio.com/coupon_kfc')");
//            db.execSQL("insert into CouponList (name,chinese,url,counturl) values('mc','麥當勞','https://coupon-ed7bf.firebaseio.com/coupon_mc/mc_','https://coupon-ed7bf.firebaseio.com/coupon_mc')");
//            db.execSQL("insert into CouponList (name,chinese,url,counturl) values('bk','漢堡王','https://coupon-ed7bf.firebaseio.com/coupon_bk/bk_','https://coupon-ed7bf.firebaseio.com/coupon_bk')");
//
//        }
           //Wilddog版
        if (isFirstRun) {
            String createTable = "CREATE TABLE IF NOT EXISTS " + tb_like_name + "(_id integer primary key autoincrement,directory TEXT,map TEXT)";
            db.execSQL(createTable);


            String createSearchTable = "CREATE TABLE IF NOT EXISTS " + tb_search_name + "(_id integer primary key autoincrement,name TEXT,chinese TEXT,url TEXT,counturl TEXT)";
            db.execSQL(createSearchTable);

            db.execSQL("insert into CouponList (name,chinese,url,counturl) values('kfc','肯德基','coupon_kfc/kfc_','coupon_kfc')");
            db.execSQL("insert into CouponList (name,chinese,url,counturl) values('mc','麦当劳','coupon_mc/mc_','coupon_mc')");
            db.execSQL("insert into CouponList (name,chinese,url,counturl) values('bk','汉堡王','coupon_bk/bk_','coupon_bk')");
        }else
        {
            db.delete("CouponList", null, null);
            String createTable = "CREATE TABLE IF NOT EXISTS " + tb_like_name + "(_id integer primary key autoincrement,directory TEXT,map TEXT)";
            db.execSQL(createTable);


            String createSearchTable = "CREATE TABLE IF NOT EXISTS " + tb_search_name + "(_id integer primary key autoincrement,name TEXT,chinese TEXT,url TEXT,counturl TEXT)";
            db.execSQL(createSearchTable);

            db.execSQL("insert into CouponList (name,chinese,url,counturl) values('kfc','肯德基','coupon_kfc/kfc_','coupon_kfc')");
            db.execSQL("insert into CouponList (name,chinese,url,counturl) values('mc','麦当劳','coupon_mc/mc_','coupon_mc')");
            db.execSQL("insert into CouponList (name,chinese,url,counturl) values('bk','汉堡王','coupon_bk/bk_','coupon_bk')");

        }
        //加载广告(Firebase版)
//        mFirehomead = new Firebase("https://coupon-ed7bf.firebaseio.com/homead");
//        mFirehomead.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Bean homead = dataSnapshot.getValue(Bean.class);
//                adimgaddress = homead.getImgaddress();
//                adcontent = homead.getContent();
//                adtitle = homead.getTitle();
//                adwebsite = homead.getWebsite();
//                if (adimgaddress.length() == 0 || adcontent.length() == 0 || adtitle.length() == 0 || adwebsite.length() == 0){
//                    Adview.setVisibility(View.GONE);
//                }else {
//                    Adcontent.setText(adcontent);
//                    Adtitle.setText(adtitle);
//                    Glide.with(HomeActivity.this).load(adimgaddress).diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().thumbnail(0.1f).error(com.example.gridviewimage.R.mipmap.image_error).placeholder(com.example.gridviewimage.R.mipmap.loading).into(Adimageview);
//                    Adview.setVisibility(View.VISIBLE);
//                }
//            }
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {}
//        });
           //Wilddog版
        SyncReference WDhomead = WilddogSync.getInstance().getReference("homead");
        WDhomead.addValueEventListener(new com.wilddog.client.ValueEventListener() {
            @Override
            public void onDataChange(com.wilddog.client.DataSnapshot snapshot) {
                Map homead = (Map) snapshot.getValue();
                adimgaddress = (String) homead.get("imgaddress");
                adcontent = (String) homead.get("content");
                adtitle = (String) homead.get("title");
                adwebsite = (String) homead.get("website");
                if (adimgaddress.length() == 0 || adcontent.length() == 0 || adtitle.length() == 0 || adwebsite.length() == 0){
                    Adview.setVisibility(View.GONE);
                }else {
                    Adcontent.setText(adcontent);
                    Adtitle.setText(adtitle);
                    Glide.with(HomeActivity.this).load(adimgaddress).diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().thumbnail(0.1f).error(com.example.gridviewimage.R.mipmap.image_error).placeholder(com.example.gridviewimage.R.mipmap.loading).into(Adimageview);
                    Adview.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(SyncError error) {}
        });

        //加载商家Logo和名称，以及点击事件
        ////第二排商户(Firebase版)

//        String buscount = "https://coupon-ed7bf.firebaseio.com/business_2";
//        final String busurl = "https://coupon-ed7bf.firebaseio.com/business_2/";
//        mFirebuscount2 = new Firebase(buscount);
//        mFirebuscount2.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                long count = dataSnapshot.getChildrenCount();
//
//                b2 = new String[(int) count ];
//                getStringValue2(b2, busurl);
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//            }
//        });
           //Wilddog版
        String buscount = "business_2";
        final String busurl = "business_2/";
        SyncReference buscount2 = WilddogSync.getInstance().getReference(buscount);
        buscount2.addValueEventListener(new com.wilddog.client.ValueEventListener() {
            @Override
            public void onDataChange(com.wilddog.client.DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();
                b2 = new String[(int) count ];
                getStringValue2(b2,busurl);
            }

            @Override
            public void onCancelled(SyncError error) {

            }
        });


        ////第三排商户
//        String buscount3 = "https://coupon-ed7bf.firebaseio.com/business_3";
//        final String busurl3 = "https://coupon-ed7bf.firebaseio.com/business_3/";
//        mFirebuscount3 = new Firebase(buscount3);
//        mFirebuscount3.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                long count = dataSnapshot.getChildrenCount();
//
//                b3 = new String[(int) count ];
//                getStringValue3(b3, busurl3);
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//            }
//        });

           //Wilddog版
        String buscount3 = "business_3";
        final String busurl3 = "business_3/";
        SyncReference buscount_3 = WilddogSync.getInstance().getReference(buscount3);
        buscount_3.addValueEventListener(new com.wilddog.client.ValueEventListener() {
            @Override
            public void onDataChange(com.wilddog.client.DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();
                b3 = new String[(int) count ];
                getStringValue3(b3,busurl3);
            }

            @Override
            public void onCancelled(SyncError error) {

            }
        });



        //db.delete("like",null,null);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kfc_click();
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mcd_click();
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bk_click();
            }
        });

        gridview2.setFocusable(false);
        gridview3.setFocusable(false);
    }



    //获取数据的方法
    ////第二排商户的GridView（便利商店）
//    private void getStringValue2(String[] z, String url) {
//
//        //String b = "https://fireapp-1a82c.firebaseio.com/kfc_";
//
//        for (int i = 0; i < b2.length; i++) {
//            int p = i + 1;
//            z[i] = url + p;
//            System.out.println(b2[i] + "-------a[" + i + "]");
//
//            mFirebusdata2 = new Firebase(b2[i]);
//            final int finalI = i;
//
//            final int finalI1 = i;
//            mFirebusdata2.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
////                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    //Getting the data from snapshot
//                    Bean bean = dataSnapshot.getValue(Bean.class);
//
//                    imgAddressGrid.add(bean.getImglogo());
//                    textlogoGird.add(bean.getTextlogo());
//                    abbreviationGrid.add(bean.getAbbreviation());
//                    System.out.println(bean.getImglogo()+"-----------bean.getimglogo3");
//                    System.out.println(bean.getImglogo()+"-----------bean.getTextlogo3");
//                    System.out.println(bean.getImglogo()+"-----------bean.getAbbreviation3");
//                    Map<String, String> map = new HashMap<String, String>();
//                    map.put("textlogo", bean.getTextlogo());
//                    map.put("imgAddress", bean.getImglogo());
//
//                    grid2.add(map);
//                    if (finalI == b2.length - 1) {
//                        adapter = new HomeGridAdapter(grid2, HomeActivity.this);
//                        gridview2.setAdapter(adapter);
//                    }
//                    //给三个传参集合添加数据
//                    urlList2.add(databaseurl + bean.getAbbreviation() + "/" + bean.getAbbreviation() + "_");
//                    counturlList2.add(databaseurl + bean.getAbbreviation());
//                    mapList2.add(mapurl + bean.getTextlogo());
//                    System.out.println(urlList2.get(finalI1)+"--------urlList");
//                    System.out.println(counturlList2.get(finalI1)+"--------counturlList");
//                    System.out.println(mapList2.get(finalI1)+"--------mapList");
//                    //给搜索的表插入数据
//
//                    db.execSQL("insert into CouponList (name,chinese,url,counturl) values('" + bean.getAbbreviation() + "','" + bean.getTextlogo() + "','" + urlList2.get(finalI1) + "','" + counturlList2.get(finalI1) + "')");
//
//                    gridview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                判断集合里边是否有数据
//                            if (urlList2.size() == 0 || counturlList2.size() == 0 || mapList2.size() == 0) {
//                                Toast.makeText(HomeActivity.this, "获取数据失败...", Toast.LENGTH_SHORT).show();
//                            } else {
////                    其实这两个判断可以和在一起，但是我现在我没法测试，所以我就没和，这样比较保险，后期有时间你和起来就行
////                    判断集合中获取出来的某个数据是否为空字符串
//                                if (!urlList2.get(i).equals("") || !counturlList2.get(i).equals("") || !mapList2.get(i).equals("")) {
//                                    Intent in = new Intent(HomeActivity.this, Coupon_ListActivity.class);
//                                    in.putExtra("url", urlList2.get(i));
//                                    in.putExtra("counturl", counturlList2.get(i));
//                                    in.putExtra("map", mapList2.get(i));
//
//                                    startActivity(in);
//                                } else {
//                                    Toast.makeText(HomeActivity.this, "获取数据失败...", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }
//                    });
//                }
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//                    System.out.println("The read failed: " + firebaseError.getMessage());
//                }
//            });
//        }
//    }

       //Wilddog版
    private void getStringValue2(String[] z,String url) {
        for (int i = 0;i < b2.length;i++){
            int p = i + 1;
            z[i] = url + p;
            SyncReference busdata2 = WilddogSync.getInstance().getReference(b2[i]);
            final int finalI = i;
            busdata2.addValueEventListener(new com.wilddog.client.ValueEventListener() {
                @Override
                public void onDataChange(com.wilddog.client.DataSnapshot snapshot) {
                    Map data2 = (Map) snapshot.getValue();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("textlogo", (String) data2.get("textlogo"));
                    map.put("imgAddress",(String) data2.get("imglogo"));
                    grid2.add(map);
                    if (finalI == b2.length - 1){
                        adapter = new HomeGridAdapter(grid2, HomeActivity.this);
                        gridview2.setAdapter(adapter);
                    }
                    urlList2.add(databaseurl_wd + data2.get("abbreviation") + "/" + data2.get("abbreviation") + "_");
                    counturlList2.add(databaseurl_wd + data2.get("abbreviation"));
                    mapList2.add((String) data2.get("textlogo"));

                    db.execSQL("insert into CouponList (name,chinese,url,counturl) values('" + data2.get("abbreviation") + "','" + data2.get("textlogo") + "','" + urlList2.get(finalI) + "','" + counturlList2.get(finalI) + "')");

                    gridview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if (urlList2.size() == 0 || counturlList2.size() == 0 || mapList2.size() == 0) {
                                Toast.makeText(HomeActivity.this, "获取数据失败...", Toast.LENGTH_SHORT).show();
                            } else {
//                    其实这两个判断可以和在一起，但是我现在我没法测试，所以我就没和，这样比较保险，后期有时间你和起来就行
//                    判断集合中获取出来的某个数据是否为空字符串
                                if (!urlList2.get(i).equals("") || !counturlList2.get(i).equals("") || !mapList2.get(i).equals("")) {
                                    Intent in = new Intent(HomeActivity.this, Coupon_ListActivity.class);
                                    in.putExtra("url", urlList2.get(i));
                                    in.putExtra("counturl", counturlList2.get(i));
                                    in.putExtra("map", mapList2.get(i));

                                    startActivity(in);
                                } else {
                                    Toast.makeText(HomeActivity.this, "获取数据失败...", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    });
                }

                @Override
                public void onCancelled(SyncError error) {

                }
            });
        }
    }

    ////第三排商户的GridView（餐厅美食）
//    private void getStringValue3(String[] z, String url) {
//
//        //String b = "https://fireapp-1a82c.firebaseio.com/kfc_";
//
//        for (int i = 0; i < b3.length; i++) {
//            int p = i + 1;
//            z[i] = url + p;
//            System.out.println(b3[i] + "-------a[" + i + "]");
//            mFirebusdata3 = new Firebase(b3[i]);
//            final int finalI = i;
//            mFirebusdata3.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
////                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    //Getting the data from snapshot
//                    Bean bean = dataSnapshot.getValue(Bean.class);
//
//                    imgAddressGrid.add(bean.getImglogo());
//                    textlogoGird.add(bean.getTextlogo());
//                    abbreviationGrid.add(bean.getAbbreviation());
//                    System.out.println(bean.getImglogo()+"-----------bean.getimglogo3");
//                    System.out.println(bean.getImglogo()+"-----------bean.getTextlogo3");
//                    System.out.println(bean.getImglogo()+"-----------bean.getAbbreviation3");
//                    Map<String, String> map = new HashMap<String, String>();
//                    map.put("textlogo", bean.getTextlogo());
//                    map.put("imgAddress", bean.getImglogo());
//                    grid3.add(map);
//                    if (finalI == b3.length - 1) {
//                        adapter = new HomeGridAdapter(grid3, HomeActivity.this);
//                        gridview3.setAdapter(adapter);
//                    }
//                    urlList3.add(databaseurl + bean.getAbbreviation() + "/" + bean.getAbbreviation() + "_");
//                    counturlList3.add(databaseurl + bean.getAbbreviation());
//                    mapList3.add(mapurl + bean.getTextlogo());
//
//                    db.execSQL("insert into CouponList (name,chinese,url,counturl) values('" + bean.getAbbreviation() + "','" + bean.getTextlogo() + "','" + urlList3.get(finalI) + "','" + counturlList3.get(finalI) + "')");
//
//
//                    gridview3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                判断集合里边是否有数据
//                            if (urlList3.size() == 0 || counturlList3.size() == 0 || mapList3.size() == 0) {
//                                Toast.makeText(HomeActivity.this, "获取数据失败...", Toast.LENGTH_SHORT).show();
//                            } else {
////                    其实这两个判断可以和在一起，但是我现在我没法测试，所以我就没和，这样比较保险，后期有时间你和起来就行
////                    判断集合中获取出来的某个数据是否为空字符串
//                                if (!urlList3.get(i).equals("") || !counturlList3.get(i).equals("") || !mapList3.get(i).equals("")) {
//                                    Intent in = new Intent(HomeActivity.this, Coupon_ListActivity.class);
//                                    in.putExtra("url", urlList3.get(i));
//                                    in.putExtra("counturl", counturlList3.get(i));
//                                    in.putExtra("map", mapList3.get(i));
//
//                                    startActivity(in);
//                                } else {
//                                    Toast.makeText(HomeActivity.this, "获取数据失败...", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }
//                    });
//                }
//
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//                    System.out.println("The read failed: " + firebaseError.getMessage());
//                }
//            });
//        }
//    }
       //Wilddog版
    private void getStringValue3(String[] z,String url) {
        for (int i = 0;i < b3.length;i++){
            int p = i + 1;
            z[i] = url + p;
            SyncReference busdata3 = WilddogSync.getInstance().getReference(b3[i]);
            final int finalI = i;
            busdata3.addValueEventListener(new com.wilddog.client.ValueEventListener() {
                @Override
                public void onDataChange(com.wilddog.client.DataSnapshot snapshot) {
                    Map data3 = (Map) snapshot.getValue();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("textlogo", (String) data3.get("textlogo"));
                    map.put("imgAddress",(String) data3.get("imglogo"));
                    grid3.add(map);
                    if (finalI == b3.length - 1){
                        adapter = new HomeGridAdapter(grid3, HomeActivity.this);
                        gridview3.setAdapter(adapter);
                    }
                    urlList3.add(databaseurl_wd + data3.get("abbreviation") + "/" + data3.get("abbreviation") + "_");
                    counturlList3.add(databaseurl_wd + data3.get("abbreviation"));
                    mapList3.add((String) data3.get("textlogo"));

                    db.execSQL("insert into CouponList (name,chinese,url,counturl) values('" + data3.get("abbreviation") + "','" + data3.get("textlogo") + "','" + urlList3.get(finalI) + "','" + counturlList3.get(finalI) + "')");

                    gridview3.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if (urlList3.size() == 0 || counturlList3.size() == 0 || mapList3.size() == 0) {
                                Toast.makeText(HomeActivity.this, "获取数据失败...", Toast.LENGTH_SHORT).show();
                            } else {
//                    其实这两个判断可以和在一起，但是我现在我没法测试，所以我就没和，这样比较保险，后期有时间你和起来就行
//                    判断集合中获取出来的某个数据是否为空字符串
                                if (!urlList3.get(i).equals("") || !counturlList3.get(i).equals("") || !mapList3.get(i).equals("")) {
                                    Intent in = new Intent(HomeActivity.this, Coupon_ListActivity.class);
                                    in.putExtra("url", urlList3.get(i));
                                    in.putExtra("counturl", counturlList3.get(i));
                                    in.putExtra("map", mapList3.get(i));

                                    startActivity(in);
                                } else {
                                    Toast.makeText(HomeActivity.this, "获取数据失败...", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    });
                }

                @Override
                public void onCancelled(SyncError error) {

                }
            });
        }
    }



    public void adclick(View v) {
        Intent it = new Intent(this, AdActivity.class);
        it.putExtra("adwebsite",adwebsite);
        startActivity(it);
    }

    public void kfc_click() {
        Intent it = new Intent(this, Coupon_GridActivity.class);
        it.putExtra("url", "coupon_kfc/kfc_");
        it.putExtra("counturl", "coupon_kfc");
        it.putExtra("map", "肯德基");
        startActivity(it);

    }

    public void mcd_click() {
        Intent it = new Intent(this, Coupon_ListActivity.class);
        it.putExtra("url", "coupon_mc/mc_");
        it.putExtra("counturl", "coupon_mc");
        it.putExtra("map", "麦当劳");
        startActivity(it);


    }

    public void bk_click() {
        Intent it = new Intent(this, Coupon_GridActivity.class);
        it.putExtra("url", "coupon_bk/bk_");
        it.putExtra("counturl", "coupon_bk");
        it.putExtra("map", "汉堡王");
        startActivity(it);


    }


    private void initView() {
        img1 = (ImageView) findViewById(R.id.imageLogo_1);
        img2 = (ImageView) findViewById(R.id.imageLogo_2);
        img3 = (ImageView) findViewById(R.id.imageLogo_3);
        Hometitle = (ImageView) findViewById(R.id.hometitle);
        Adimageview = (ImageView) findViewById(R.id.adimageview);
        Adtitle = (TextView) findViewById(R.id.adtitle);
        Adcontent = (TextView) findViewById(R.id.adcontent);
        Adview = (RelativeLayout) findViewById(R.id.adview);

    }

    @Override
    public void onClick(View v) {

    }
}
