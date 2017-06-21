package com.edu.pu.cs.couponapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.pu.cs.couponapp.ui.TabShow;
import com.example.gridviewimage.view.acitvity.MaxPictureActivity;
import com.example.gridviewimage.view.adapter.GridViewImageAdapter;
import com.example.gridviewimage.view.controls.ImageGridView;

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
import java.util.Map;

import me.iwf.photopicker.PhotoPreview;
import qiu.niorgai.StatusBarCompat;


public class SearchAcitivity extends AutoLayoutActivity {
	SearchView sv = null;
	ListView lv = null;
	SQLiteDatabase db;
	String db_name = "coupon";
	String tb_name = "CouponList";
	ArrayList<String> photos = new ArrayList<String>();
	String url = null;
	String counturl = null;
	private String[] a = new String[10];
	ImageGridView image_gridView = null;
	int countdata;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search);
//		StatusBarCompat.setStatusBarColor(SearchAcitivity.this,getResources().getColor(R.color.background_color_orange));
//		final ProgressDialog dialog = ProgressDialog.show(this, "稍候片刻", "折價券即將呈現", true, true);
		image_gridView=(ImageGridView)findViewById(R.id.image_gridView);
		image_gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));


		sv = (SearchView) this.findViewById(R.id.sv);

//        sv.setIconifiedByDefault(false);
		sv.clearFocus();
		int currentapiVersion=android.os.Build.VERSION.SDK_INT;



		int search_mag_icon_id = sv.getContext().getResources().getIdentifier("android:id/search_mag_icon", null, null);
		ImageView search_mag_icon = (ImageView)sv.findViewById(search_mag_icon_id);//获取搜索图标
		search_mag_icon.setImageResource(R.drawable.searchview);



		sv.setSubmitButtonEnabled(true);

		if(sv == null) {
			return;
		}
		int id = sv.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
		TextView textView = (TextView) sv.findViewById(id);

		if (currentapiVersion>=21)
		{
			sv.setQueryHint(Html.fromHtml("<font color = #000000>" + getResources().getString(R.string.search_input) + "</font>"));
			textView.setTextColor(getResources().getColor(R.color.background_color_orange));
		}else if (currentapiVersion<21){
			sv.setQueryHint(Html.fromHtml("<font color = #ffffff>" + getResources().getString(R.string.search_input) + "</font>"));
			textView.setTextColor(Color.WHITE);
		}


		sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextChange(String str) {
				photos.clear();
				return false;
			}

			@Override
			public boolean onQueryTextSubmit(String str) {

				System.out.println(str+"--------------str");
				url = null;
				counturl = null;
				photos.clear();
				sv.clearFocus();
				db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
				Cursor cursor = null;
				cursor = db.rawQuery("Select * from CouponList where chinese like '%"+str+"%'", null);
				while (cursor.moveToNext()) {
					url = cursor.getString(cursor.getColumnIndex("url"));
					counturl = cursor.getString(cursor.getColumnIndex("counturl"));
					//可以将数据set-》实体类对象集合->photos
				}
				if (url==null|| counturl==null)
				{
					Toast.makeText(SearchAcitivity.this, "搜索失败，未收入此商家优惠信息", Toast.LENGTH_SHORT).show();

				}else {
					if (counturl.equals("coupon_mc")) {
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

								//下载数据
								SyncReference WDdata = WilddogSync.getInstance().getReference(counturl);
								Query wddata = WDdata.limitToLast(countdata);
								wddata.addChildEventListener(new ChildEventListener() {
									@Override
									public void onChildAdded(com.wilddog.client.DataSnapshot snapshot, String previousChildName) {
										Map bean = (Map) snapshot.getValue();
										photos.add((String) bean.get("imgaddress"));
										image_gridView.setAdapter(new GridViewImageAdapter(SearchAcitivity.this, photos, 320, 320));
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
					}else {
						//Wilddog版
						SyncReference count = WilddogSync.getInstance().getReference(counturl);
						count.addValueEventListener(new com.wilddog.client.ValueEventListener() {
							@Override
							public void onDataChange(com.wilddog.client.DataSnapshot snapshot) {
								long count = snapshot.getChildrenCount();
								a = new String[(int) count - 1];
								for (int i = 0; i < a.length; i++) {
									int p = i + 1;
									a[i] = url + p;
									SyncReference imgaddress = WilddogSync.getInstance().getReference(a[i]);
									final int finalI = i;
									imgaddress.addValueEventListener(new com.wilddog.client.ValueEventListener() {
										@Override
										public void onDataChange(com.wilddog.client.DataSnapshot snapshot) {
											Map bean = (Map) snapshot.getValue();
											photos.add((String) bean.get("imgaddress"));
											image_gridView.setAdapter(new GridViewImageAdapter(SearchAcitivity.this, photos, 320, 320));

											if (finalI == a.length - 1) {
												Toast.makeText(SearchAcitivity.this, "搜索成功", Toast.LENGTH_SHORT)
														.show();
											}
										}

										@Override
										public void onCancelled(SyncError error) {

										}
									});
								}
							}

							@Override
							public void onCancelled(SyncError error) {

							}
						});
					}
				}

				//        单点事件
				image_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//						Intent in = new Intent();
//						in.setClass(SearchAcitivity.this, MaxPictureActivity.class);
//						in.putExtra("pos", i);//必传项,i为当前点击的位置
//						in.putStringArrayListExtra("imageAddress", photos);//必传项,photos为要显示的图片地址集合
//						startActivity(in);
						PhotoPreview.builder()
								.setPhotos(photos)
								.setCurrentItem(i)
								.setShowDeleteButton(false)
								.start(SearchAcitivity.this);

					}
				});
				/**
				 *  MainActivity.this:为当前界面上下文
				 *  photos:photos为要显示的图片地址集合
				 * */

				return false;
			}

		});
	}

}