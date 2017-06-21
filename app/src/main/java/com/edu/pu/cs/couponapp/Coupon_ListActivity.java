package com.edu.pu.cs.couponapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.edu.pu.cs.couponapp.ui.TabShow;

import com.github.anzewei.parallaxbacklayout.ParallaxActivityBase;
import com.wilddog.client.ChildEventListener;
import com.wilddog.client.Query;
import com.wilddog.client.ServerValue;
import com.wilddog.client.SyncError;
import com.wilddog.client.SyncReference;
import com.wilddog.client.WilddogSync;
import com.zhy.autolayout.AutoLayoutActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import qiu.niorgai.StatusBarCompat;

/**
 * Created by Administrator on 2016/10/29.
 */

public class Coupon_ListActivity extends ParallaxActivityBase {
    private ListAdapter adapter;
    private ListView listView;
    private String[] a = new String[10];
    private String map = null;

    private List<String> BeginDateList = new ArrayList<>();
    private List<String> EndDateList = new ArrayList<>();
    // 标题集合
    private List<String> titleList = new ArrayList<String>();
    // 图片地址集合
    private List<String> imgAddressList = new ArrayList<String>();
    // 文本描述集合
    private List<String> ContentList = new ArrayList<String>();
    // 所有数据的MAP集合
    private ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

    private String[] b = new String[10];

    ImageView titlelogo;

    private String TitleLogo = null;

    public static Coupon_ListActivity instance = null;

    private String listorgrid = "list";

    private List<String> DetailsimgList = new ArrayList<String>();

    private Context mContext;


    //有效期
    private String begindate,enddate;
    private long servertimestamp;
    private String DateOver;

    private int countdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackEnable(true);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_couponlist);
        StatusBarCompat.setStatusBarColor(Coupon_ListActivity.this, getResources().getColor(R.color.background_color_blue));

        instance = this;

        final ProgressDialog dialog5 = ProgressDialog.show(this, "稍候片刻", "折价券即将呈现", true, true);
        dialog5.onStart();
        titlelogo = (ImageView) findViewById(R.id.titlelogo);
        listView = (ListView) findViewById(R.id.listview_2);

        map = getIntent().getStringExtra("map");

        final String url = getIntent().getStringExtra("url");
//              url = "https://fireapp-1a82c.firebaseio.com/kfc_";
        String c = url + "title";
        final String counturl = getIntent().getStringExtra("counturl");
        //获取顶部logo
        String Titlelogo = counturl + "/titlelogo";
        Log.e("counturl", counturl + "");
        Log.e("url", url + "");
        Log.e("Titlelogo", Titlelogo + "");

//        mFiretitlelogo = new Firebase(Titlelogo);
//        mFiretitlelogo.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String logo = dataSnapshot.getValue(String.class);
//                Glide.with(Coupon_ListActivity.this).load(logo).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().thumbnail(0.1f).error(com.example.gridviewimage.R.mipmap.image_error).into(titlelogo);
//                TitleLogo = logo;
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//            }
//        });

//       获取该商家条数,count
//        mFirebasecount = new Firebase(counturl);
//
//        mFirebasecount.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                long count = dataSnapshot.getChildrenCount();
//                System.out.println("count=----" + count);
//                Log.e("count", count + "");
//                a = new String[(int) count - 1];
//                b = new String[(int) count - 1];
//                getStringValue(a, url);
//                dialog5.cancel();
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//            }
//        });
           //Wilddog版
        if (!counturl.equals("coupon_mc")) {
            //Wilddog版
            SyncReference WDtitlelogo = WilddogSync.getInstance().getReference(Titlelogo);
            WDtitlelogo.addValueEventListener(new com.wilddog.client.ValueEventListener() {
                @Override
                public void onDataChange(com.wilddog.client.DataSnapshot snapshot) {
                    String logo = snapshot.getValue().toString();
                    Glide.with(Coupon_ListActivity.this).load(logo).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().thumbnail(0.1f).error(com.example.gridviewimage.R.mipmap.image_error).into(titlelogo);
                    TitleLogo = logo;
                }

                @Override
                public void onCancelled(SyncError error) {

                }
            });
            SyncReference WDcount = WilddogSync.getInstance().getReference(counturl);
            WDcount.addValueEventListener(new com.wilddog.client.ValueEventListener() {
                @Override
                public void onDataChange(com.wilddog.client.DataSnapshot snapshot) {
                    long count = snapshot.getChildrenCount();
                    a = new String[(int) count - 1];
                    b = new String[(int) count - 1];
                    getStringValue(a, url);
                    dialog5.cancel();
                }

                @Override
                public void onCancelled(SyncError error) {
                }
            });
        }else if (counturl.equals("coupon_mc")) {
            TitleLogo = "http://couponapp.image.alimmdn.com/titlelogo/mc1.png?t=1487955104530";
            Glide.with(this).load(TitleLogo).into(titlelogo);
            //条数设置
            SyncReference WDcount = WilddogSync.getInstance().getReference(counturl);
            WDcount.addValueEventListener(new com.wilddog.client.ValueEventListener() {
                @Override
                public void onDataChange(com.wilddog.client.DataSnapshot snapshot) {
                    long count = snapshot.getChildrenCount();
                    countdata = (int) count;
                    System.out.println("countdata......"+countdata);
                    downloadData();
                }

                private void downloadData() {
                    //获取服务器时间
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
                    for (int i = 0;i<countdata-1;i++) {
                        SyncReference data = WilddogSync.getInstance().getReference(counturl);
                        final int finalI = i;
                        data.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(com.wilddog.client.DataSnapshot snapshot, String previousChildName) {
                                b[finalI] = counturl+"/"+snapshot.getKey();
                            }

                            @Override
                            public void onChildChanged(com.wilddog.client.DataSnapshot snapshot, String previousChildName) {

                            }

                            @Override
                            public void onChildRemoved(com.wilddog.client.DataSnapshot snapshot) {

                            }

                            @Override
                            public void onChildMoved(com.wilddog.client.DataSnapshot snapshot, String previousChildName) {

                            }

                            @Override
                            public void onCancelled(SyncError error) {

                            }
                        });
                    }
                    //下载数据
                    SyncReference WDdata = WilddogSync.getInstance().getReference(counturl);
                    Query wddata = WDdata.limitToLast(countdata);
                    wddata.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(com.wilddog.client.DataSnapshot snapshot, String previousChildName) {
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
                            //装载到map集合
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("title", (String) bean.get("title"));
                            map.put("content", (String) bean.get("content"));
                            map.put("imgAddress", (String) bean.get("imgaddress"));
                            map.put("dateover", DateOver);
                            list.add(map);
                            //Collections.reverse(list);
                            adapter = new ListAdapter(list, Coupon_ListActivity.this);
                            listView.setAdapter(adapter);

                        }


                        @Override
                        public void onChildChanged(com.wilddog.client.DataSnapshot snapshot, String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(com.wilddog.client.DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(com.wilddog.client.DataSnapshot snapshot, String previousChildName) {

                        }

                        @Override
                        public void onCancelled(SyncError error) {

                        }
                    });
                    System.out.println("zhixing...");
                }

                @Override
                public void onCancelled(SyncError error) {
                }
            });
            dialog5.cancel();
        }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                判断集合里边是否有数据
                    if (titleList.size() == 0 || imgAddressList.size() == 0 || ContentList.size() == 0) {
                        Toast.makeText(Coupon_ListActivity.this, "获取数据失败...", Toast.LENGTH_SHORT).show();
                    } else {
//                    其实这两个判断可以和在一起，但是我现在我没法测试，所以我就没和，这样比较保险，后期有时间你和起来就行
//                    判断集合中获取出来的某个数据是否为空字符串
                        if (!titleList.get(i).equals("") || !imgAddressList.get(i).equals("") || !ContentList.get(i).equals("")) {

                            Intent in = new Intent(Coupon_ListActivity.this, DetailsActivity.class);
                            in.putExtra("title", titleList.get(i));
                            in.putExtra("imgAddress", imgAddressList.get(i));
                            in.putExtra("content", ContentList.get(i));
                            in.putExtra("directory", b[i]);
                            in.putExtra("TitleLogo", TitleLogo);
                            in.putExtra("map", map);

                            in.putExtra("listorgrid", listorgrid);
                            in.putExtra("detailsimgOrnot", DetailsimgList.get(i));

                            startActivity(in);


                        } else {
                            Toast.makeText(Coupon_ListActivity.this, "获取数据失败...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });


    }

    //    private void getStringValue(String[] z, String url) {
//
//        //String b = "https://fireapp-1a82c.firebaseio.com/kfc_";
//
//        for (int i = a.length - 1; i >= 0; i--) {
//            int p = i + 1;
//            z[i] = url + p;
//            System.out.println(a[i] + "-------a[" + i + "]");
//
//            mFirebaseDatabase = new Firebase(a[i]);
//            final int finalI = i;
//
//            mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    long datacount = dataSnapshot.getChildrenCount();
//                    if (datacount < 4) {
//                        return;
//                    } else {
////                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                        //Getting the data from snapshot
//                        Bean bean = dataSnapshot.getValue(Bean.class);
//                        System.out.print("content=" + bean.getContent());
//                        System.out.print("||");
//                        System.out.print("imgaddress=" + bean.getImgaddress());
//                        System.out.print("||");
//                        System.out.println("title=" + bean.getTitle());
//                        imgAddressList.add(bean.getImgaddress());
//                        titleList.add(bean.getTitle());
//                        ContentList.add(bean.getContent());
//                        DetailsimgList.add(bean.getDetailsimg());
//                        Map<String, String> map = new HashMap<String, String>();
//                        map.put("title", bean.getTitle());
//                        map.put("content", bean.getContent());
//                        map.put("imgAddress", bean.getImgaddress());
////                    Log.e("TagActivity",bean.getImgaddress());
//                        list.add(map);
//                        if (finalI == a.length - 1) {
//                            adapter = new ListAdapter(list, Coupon_ListActivity.this);
//                            listView.setAdapter(adapter);
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//                    System.out.println("The read failed: " + firebaseError.getMessage());
//
//                }
//            });
//
//        }
//        for (int c = 0; c < a.length; c++) {
//            int d = c + 1;
//            int e = a.length - d;
//            b[c] = a[e];
//            System.out.println(b[c] + "-------b[" + c + "]");
//        }
//
//    }
       //Wilddog版
    private void getStringValue(String[] z, String url) {
        //获取服务器时间
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
        for (int i = a.length - 1;i >= 0;i--){
            int p = i + 1;
            z[i] = url + p;
            Log.e("TAG---",a[i]);
            SyncReference WDdata = WilddogSync.getInstance().getReference(a[i]);
            final int finalI = i;
            WDdata.addValueEventListener(new com.wilddog.client.ValueEventListener() {
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
                    if (finalI == a.length - 1) {
                        adapter = new ListAdapter(list, Coupon_ListActivity.this);
                        listView.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(SyncError error) {

                }
            });
        }
        for (int c = 0; c < a.length; c++) {
            int d = c + 1;
            int e = a.length - d;
            b[c] = a[e];
            System.out.println(b[c] + "-------b[" + c + "]");
        }
    }

    public void gomap(View v) {
        if (checkPackage("com.baidu.BaiduMap")) {
            Intent intent = new Intent();
            intent.setData(Uri.parse("baidumap://map/place/search?query="+map));
            startActivity(intent);
        }else {
            ToastUtils.showShortToast(mContext,"请先安装百度地图~");
        }
    }

    public void backbtn(View v) {
        finish();
    }


    /**
     * 检测该包名所对应的应用是否存在
     * @param packageName
     * @return
     */
    public boolean checkPackage(String packageName)
    {
        if (packageName == null || "".equals(packageName))
            return false;
        try
        {
            getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }
}
