package com.edu.pu.cs.couponapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import com.edu.pu.cs.couponapp.ui.TabShow;
import com.example.gridviewimage.view.acitvity.MaxPictureActivity;

import com.example.gridviewimage.view.adapter.GridViewImageAdapter;
import com.github.anzewei.parallaxbacklayout.ParallaxActivityBase;
import com.github.anzewei.parallaxbacklayout.ParallaxBackActivityHelper;
import com.github.anzewei.parallaxbacklayout.ParallaxBackLayout;
import com.zhy.autolayout.AutoLayoutActivity;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.Shake2Share;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;
import me.iwf.photopicker.PhotoPreview;
import me.xiaopan.sketch.SketchImageView;
import me.xiaopan.sketch.display.FadeInImageDisplayer;
import me.xiaopan.sketch.display.TransitionImageDisplayer;
import me.xiaopan.sketch.feature.zoom.ImageZoomer;
import me.xiaopan.sketch.request.DisplayOptions;
import mehdi.sakout.fancybuttons.FancyButton;
import qiu.niorgai.StatusBarCompat;

public class DetailsActivity extends AutoLayoutActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    private String title, imgAddress, content, directory, TitleLogo;
    private FancyButton Btn_like;
    private String[] a = new String[10];
    //图片listview


    ImageView imageView;
    ArrayList<String> imgList = new ArrayList<String>();
    ArrayList<String> likeornot = new ArrayList<String>();
    TextView textView;
    TextView textView2;
    SQLiteDatabase db;
    String db_name = "coupon";
    String tb_like_name = "like";
    ImageView titlelogo;
    private boolean isLikeOrNot;
    //others
    private Button Others;
    private Button Sharebtn;
    String path;

    ImageZoomer imageZoomer;
    private String map = null;

    private String listorgrid = null;

    private GridViewDetails gridView;

    private ParallaxBackActivityHelper mHelper;


    //摇一摇
    private SensorManager sensorManager;
    private SensorEventListener shakeListener;
    private AlertDialog.Builder dialogBuilder;

    private boolean isRefresh = false;

    private String detailsimgOrnot;

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setBackEnable(true);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_details);
        StatusBarCompat.setStatusBarColor(DetailsActivity.this, getResources().getColor(R.color.background_color_orange));
        mHelper = new ParallaxBackActivityHelper(this);

        //摇一摇
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        shakeListener = new ShakeSensorListener();
        //

        gridView = (GridViewDetails) findViewById(R.id.detailsgrid);
//        listView = (ListView) findViewById(R.id.detailslist);

        //others的设置
        Others = (Button) findViewById(R.id.others);
        Others.setOnClickListener(this);

//        这里的三个分别就是传过来的数据了，进行显示就行
        title = getIntent().getStringExtra("title");
        imgAddress = getIntent().getStringExtra("imgAddress");
        content = getIntent().getStringExtra("content");
        directory = getIntent().getStringExtra("directory");
        TitleLogo = getIntent().getStringExtra("TitleLogo");
        map = getIntent().getStringExtra("map");
        listorgrid = getIntent().getStringExtra("listorgrid");
        detailsimgOrnot = getIntent().getStringExtra("detailsimgOrnot");

        System.out.println(TitleLogo + "-----------titlelogo");

        Btn_like = (FancyButton) findViewById(R.id.btn_like);


        initView();
        //加载顶部Logo
        Glide.with(DetailsActivity.this).load(TitleLogo).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().thumbnail(0.1f).error(com.example.gridviewimage.R.mipmap.image_error).into(titlelogo);

        db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
        Cursor cursor = null;
        cursor = db.rawQuery("select * from like where directory = '" + directory + "'", null);
        while (cursor.moveToNext()) {
            likeornot.add(cursor.getString(cursor.getColumnIndex("directory")));
            System.out.println(cursor.getString(cursor.getColumnIndex("directory")) + "-----directory");
            //可以将数据set-》实体类对象集合->add到集合
        }
        System.out.println(likeornot.size() + "----------sizq");
        if (likeornot.size() == 1) {
            isLikeOrNot = true;
            Btn_like.setText("移出最爱");
            Btn_like.setBackgroundColor(Color.parseColor("#00a0f0"));
            Btn_like.setFocusBackgroundColor(Color.parseColor("#0fafff"));
            Btn_like.setIconResource("\uf00d");
        } else {
            isLikeOrNot = false;
            Btn_like.setText("加入最爱");
            Btn_like.setBackgroundColor(Color.parseColor("#ea5413"));
            Btn_like.setFocusBackgroundColor(Color.parseColor("#ff5b14"));
            Btn_like.setIconResource("\uf08a");
        }
        System.out.println(isLikeOrNot + "---------islikeornot");
        Btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLikeOrNot == false) {
                    db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
                    //String createTable = "CREATE TABLE IF NOT EXISTS " + tb_like_name + "(_id integer primary key autoincrement,directory TEXT)";
                    //db.execSQL(createTable);
                    String insert = "insert into like (directory,map) values('" + directory + "','" + map + "')";
                    db.execSQL(insert);
                    Cursor cursor = null;
                    cursor = db.rawQuery("Select directory from like", null);
                    while (cursor.moveToNext()) {
                        System.out.println(cursor.getString(cursor.getColumnIndex("directory")) + "---jiaruyoumeiyouchengg");
                    }
                    Btn_like.setText("移出最爱");
                    Btn_like.setBackgroundColor(Color.parseColor("#00a0f0"));
                    Btn_like.setFocusBackgroundColor(Color.parseColor("#0fafff"));
                    Btn_like.setIconResource("\uf00d");
                    isLikeOrNot = true;
                } else if (isLikeOrNot == true) {
                    db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
                    String del = "delete from like where directory = '" + directory + "'";
                    db.execSQL(del);
                    Cursor cursor = null;
                    cursor = db.rawQuery("Select directory from like", null);
                    while (cursor.moveToNext()) {
                        System.out.println(cursor.getString(cursor.getColumnIndex("directory")) + "---shanchuyoumeiyouchengg");
                    }
                    Btn_like.setText("加入最爱");
                    Btn_like.setBackgroundColor(Color.parseColor("#ea5413"));
                    Btn_like.setFocusBackgroundColor(Color.parseColor("#ff5b14"));
                    Btn_like.setIconResource("\uf08a");
                    isLikeOrNot = false;
                }

            }
        });


//      把传递过来的title和content加载到预设的textview里面
        textView.setText(title);
        textView2.setText(Html.fromHtml(content));
//      再次读取图片detailsimg
        System.out.println(detailsimgOrnot + "------detailsimgOrnot");
        if (detailsimgOrnot.equals("nul")) {
            imgList.add(imgAddress);
            System.out.println("imgList=" + imgList.toString());
        } else {
            String[] temp = detailsimgOrnot.split("[|]");
            for (int i = 0; i < temp.length; i++) {
                imgList.add(temp[i]);
                System.out.println(imgList.get(i) + "-------imgList[" + i + "]");
            }

        }


        gridView.setAdapter(new DetailsGridAdapter(DetailsActivity.this, imgList));
        //Glide.with(DetailsActivity.this).load(imgList.get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().thumbnail(0.1f).error(com.example.gridviewimage.R.mipmap.image_error).into(imageView);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent in = new Intent();
//                in.setClass(DetailsActivity.this, MaxPictureActivity.class);
//                in.putExtra("pos", i);//必传项,i为当前点击的位置
//                in.putStringArrayListExtra("imageAddress", imgList);//必传项,photos为要显示的图片地址集合
//                startActivity(in);
                PhotoPreview.builder()
                        .setPhotos(imgList)
                        .setCurrentItem(i)
                        .setShowDeleteButton(false)
                        .start(DetailsActivity.this);
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                OnekeyShare oks = new OnekeyShare();
                oks.setImageUrl(imgList.get(i));
                oks.show(DetailsActivity.this);
                return true;
            }
        });
        textView2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                OnekeyShare oks = new OnekeyShare();
                oks.setText(content);
                oks.show(DetailsActivity.this);
                return false;
            }
        });

        ShareSDK.initSDK(this);

        gridView.setFocusable(false);
    }


    //摇一摇
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(shakeListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);

    }

    @Override
    protected void onPause() {
        super.onPause();
        // acitivity后台时取消监听
        sensorManager.unregisterListener(shakeListener);


    }

    private class ShakeSensorListener implements SensorEventListener {
        private static final int ACCELERATE_VALUE = 35;

        @Override
        public void onSensorChanged(SensorEvent event) {

//          Log.e("zhengyi.wzy", "type is :" + event.sensor.getType());

            // 判断是否处于刷新状态(例如微信中的查找附近人)
//            这里判断为true,则就返回什么都不做，为false就执行下边的
            if (isRefresh) {
                return;
            }

            float[] values = event.values;

            /**
             * 一般在这三个方向的重力加速度达到20就达到了摇晃手机的状态 x : x轴方向的重力加速度，向右为正 y :
             * y轴方向的重力加速度，向前为正 z : z轴方向的重力加速度，向上为正
             */
            float x = Math.abs(values[0]);
            float y = Math.abs(values[1]);
            float z = Math.abs(values[2]);

            Log.e("zhengyi.wzy", "x is :" + x + " y is :" + y + " z is :" + z);

            if (x >= ACCELERATE_VALUE || y >= ACCELERATE_VALUE
                    || z >= ACCELERATE_VALUE) {
//                Toast.makeText(
//                        DetailsActivity.this,
//                        "accelerate speed :"
//                                + (x >= ACCELERATE_VALUE ? x
//                                : y >= ACCELERATE_VALUE ? y : z),
//                        Toast.LENGTH_SHORT).show();

                Toast.makeText(DetailsActivity.this, "摇一摇成功，快分享给你的朋友吧", Toast.LENGTH_SHORT).show();
//                这里把值改为true，默认不执行
                isRefresh = true;
//                这里启动了震动，但是这里的震动是一个子线程，因为震动是不可以去影响界面UI的进行的，所以肯定是子线程
//                这里的300为你总共的震动时长
                VibratorHelper.Vibrate(DetailsActivity.this, 300);
//                在这里我做了个子线程，
//                在这个子线程没有执行完的时候，你的isRefresh肯定一直为true，这个时候是没法进行摇一摇的
                new Thread() {
                    @Override
                    public void run() {
                        super.run();

                        try {
//                            这里为让子线程休息300毫秒
                            sleep(300);
//                            子线程休息完300毫秒的时候就会执行下边这一行，这样isRefres=false了，你就可以再次摇一摇了
                            isRefresh = false;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                showShare();
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub
        }

    }
    //

    private void initView() {
        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        titlelogo = (ImageView) findViewById(R.id.titlelogo);
        Sharebtn = (Button) findViewById(R.id.share);
        //imageView = (SketchImageView) findViewById(R.id.imageView);


    }

    public void backbtn(View v) {
        finish();
    }

    //分享菜单的设置
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
//关闭sso授权
        oks.disableSSOWhenAuthorize();
// titleUrl是标题的网络链接，QQ和QQ空间等使用
//        oks.setTitleUrl("http://sharesdk.cn");
// text是分享文本，所有平台都需要这个字段
        oks.setText(title);
// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath(path);//确保SDcard下面存在此张图片
        oks.setImageUrl(imgAddress);
        //oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
// 启动分享GUI
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, cn.sharesdk.framework.Platform.ShareParams paramsToShare) {
                if ("Line".equals(platform.getName())) {
                    paramsToShare.setText(null);
                }
            }
        });
        oks.show(this);
    }

    //点击按钮后，加载弹出式菜单
    @Override
    public void onClick(View v) {
        //创建弹出式菜单对象（最低版本11）
        PopupMenu popup = new PopupMenu(this, v);//第二个参数是绑定的那个view
        //获取菜单填充器
        MenuInflater inflater = popup.getMenuInflater();
        //填充菜单
        inflater.inflate(R.menu.main, popup.getMenu());
        //绑定菜单项的点击事件
        popup.setOnMenuItemClickListener(this);

        try {
            Field field = popup.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            MenuPopupHelper mHelper = (MenuPopupHelper) field.get(popup);
            mHelper.setForceShowIcon(true);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        popup.show(); //这一行代码不要忘记了

    }

    //弹出式菜单的单击事件处理
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.share:
                showShare();
                break;
            case R.id.map:
                Intent it = new Intent();
                Uri u;
                it.setAction(Intent.ACTION_VIEW);
                u = Uri.parse(map);
                it.setData(u);
                startActivity(it);
                break;
            case R.id.home:

                if (listorgrid.equals("list")) {
                    Intent a = new Intent(this, TabShow.class);
                    startActivity(a);
                    Coupon_ListActivity.instance.finish();
                    finish();
                } else if (listorgrid.equals("grid")) {
                    Intent a = new Intent(this, TabShow.class);
                    startActivity(a);
                    Coupon_GridActivity.instance.finish();
                    finish();
                } else if (listorgrid.equals("like")) {
                    Intent a = new Intent(this, TabShow.class);
                    startActivity(a);
                    finish();
                } else if (listorgrid.equals("banner")) {
                    finish();
                }


                break;
            default:
                break;
        }
        return false;
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper.onActivityDestroy();
    }

    public ParallaxBackLayout getBackLayout() {
        return mHelper.getBackLayout();
    }

    public void setBackEnable(boolean enable) {
        getBackLayout().setEnableGesture(enable);
    }

    public void scrollToFinishActivity() {
        mHelper.scrollToFinishActivity();
    }

    @Override
    public void onBackPressed() {
        scrollToFinishActivity();
    }

}
