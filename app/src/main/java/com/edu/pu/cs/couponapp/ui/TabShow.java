package com.edu.pu.cs.couponapp.ui;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.Toast;

import com.edu.pu.cs.couponapp.HomeActivity;
import com.edu.pu.cs.couponapp.LikeActivity;
import com.edu.pu.cs.couponapp.SearchAcitivity;
import com.edu.pu.cs.couponapp.R;

import com.github.anzewei.parallaxbacklayout.ParallaxBackActivityHelper;
import com.github.anzewei.parallaxbacklayout.ParallaxBackLayout;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;


import java.util.Timer;
import java.util.TimerTask;

import qiu.niorgai.StatusBarCompat;

public class TabShow extends TabActivity {
	private final static String TAG = "TabShow";
	private TabHost mHost;
	private RadioGroup tabItems;
	private RadioButton tabhome,tabsearch,tablike;

	// MineTab �� ���ҵġ� ��һѡ�� ����ʾ
	private MineTab mineTab;
	private PopupWindow minePop;
	private RadioButton mineBut;
	private static boolean FINISH = false;
	boolean isShow;
	Toast backToast;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;

	private ParallaxBackActivityHelper mHelper;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHelper = new ParallaxBackActivityHelper(this);
		setBackEnable(false);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tabshow);
//		StatusBarCompat.translucentStatusBar(TabShow.this);
		StatusBarCompat.setStatusBarColor(TabShow.this,getResources().getColor(R.color.background_color_orange));


		initResourceRefs();
		initSettings();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	private void initResourceRefs() {
		mHost = getTabHost();

		mHost.addTab(mHost.newTabSpec("HOME").setIndicator("HOME")
				.setContent(new Intent(this, HomeActivity.class)));

		mHost.addTab(mHost.newTabSpec("SEARCH").setIndicator("SEARCH")
				.setContent(new Intent(this, SearchAcitivity.class)));


		mHost.addTab(mHost.newTabSpec("LIKE").setIndicator("LIKE")
				.setContent(new Intent(this, LikeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));

		tabItems = (RadioGroup) findViewById(R.id.tab_items);
		tabhome = (RadioButton) findViewById(R.id.tab_item_home);
		tabsearch= (RadioButton) findViewById(R.id.tab_item_search);
		tablike = (RadioButton) findViewById(R.id.tab_item_like);

		//定义底部标签图片大小
		Drawable drawablehome= getResources().getDrawable(R.drawable.item_home_bg);
		drawablehome.setBounds(0, -6, 52, 46);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
		tabhome.setCompoundDrawables(null, drawablehome, null, null);//只放上面


		Drawable drawablelike= getResources().getDrawable(R.drawable.item_like_bg);
		drawablelike.setBounds(0, -6, 52, 46);
		tablike.setCompoundDrawables(null, drawablelike, null, null);

		Drawable drawablesearch= getResources().getDrawable(R.drawable.item_search_bg);
		drawablesearch.setBounds(0, -5, 52, 47);
		tabsearch.setCompoundDrawables(null, drawablesearch, null, null);

		tabItems.check(R.id.tab_item_home);

		mineTab = new MineTab(this);
	}

	private void initSettings() {


		tabItems.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub


				switch (checkedId) {

					case R.id.tab_item_home:
						mHost.setCurrentTabByTag("HOME");
						break;
					case R.id.tab_item_search:
						mHost.setCurrentTabByTag("SEARCH");
						break;
					case R.id.tab_item_like:
						mHost.setCurrentTabByTag("LIKE");
						break;


				}

				mineTab.dismissMine(true);
			}
		});


	}

	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	public Action getIndexApiAction() {
		Thing object = new Thing.Builder()
				.setName("TabShow Page") // TODO: Define a title for the content shown.
				// TODO: Make sure this auto-generated URL is correct.
				.setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
				.build();
		return new Action.Builder(Action.TYPE_VIEW)
				.setObject(object)
				.setActionStatus(Action.STATUS_TYPE_COMPLETED)
				.build();
	}

	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.connect();
		AppIndex.AppIndexApi.start(client, getIndexApiAction());
	}

	@Override
	public void onStop() {
		super.onStop();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		AppIndex.AppIndexApi.end(client, getIndexApiAction());
		client.disconnect();
	}

	/**
	 * popWindow
	 *
	 * @author yuhaiyang
	 */

	public class MineTab {
		private Context mContext;

		private View popView, patentView;

		public MineTab(Context context) {
			mContext = context;
			initRes();
		}

		private void initRes() {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			popView = inflater.inflate(R.layout.mine, null);
			patentView = inflater.inflate(R.layout.tabshow, null);

		}

		public int getId(String tag) {
			if (tag.equals("HOME")) {
				return R.id.tab_item_home;
			} else if (tag.equals("SEARCH")) {
				return R.id.tab_item_search;
			} else if (tag.equals("LIKE")) {
				return R.id.tab_item_like;
			} else {
				return -1;
			}
		}

		/**
		 * ��ʾ popWindow
		 */
		public void showMine() {
			// if(!minePop.isShowing()){

			minePop = new PopupWindow(popView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			int y = tabItems.getHeight();
			int x = mineBut.getLeft() - mineBut.getWidth() / 2;
			Log.i("nian", "top == " + y + " , left == " + minePop.getWidth());

			minePop.showAtLocation(patentView, Gravity.BOTTOM | Gravity.LEFT, x, y);

		}

		/**
		 * ��ʧ�Ի���
		 *
		 * @param isRa �ж��Ƿ��ǵ���� radioButton ����� ����Ҫ�Լ�ȥ�л�
		 */
		public void dismissMine(boolean isRa) {
			if (minePop != null && minePop.isShowing())
				minePop.dismiss();
			minePop = null;
			if (!isRa) {
				String tag = mHost.getCurrentTabTag();
				tabItems.check(getId(tag));
			}
		}

	}


	/**
	 * �˳���ʧ
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

			if (minePop != null && minePop.isShowing()) {
				mineTab.dismissMine(false);
			} else if (!FINISH) {
				backToast = Toast.makeText(this, "再按一次返回键即将退出", Toast.LENGTH_SHORT);
				backToast.show();
				FINISH = true;
				new Timer().schedule(new TimerTask() {

					@Override
					public void run() {
						FINISH = false;

					}
				}, 2000);
			} else {
				return super.dispatchKeyEvent(event);
			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}


	@Override
	protected void onPause() {
		if (FINISH) {
			backToast.cancel();
			FINISH = false;
		}
		mineTab.dismissMine(false);
		super.onPause();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mineTab.dismissMine(false);
		return super.onTouchEvent(event);
	}

	//滑动返回
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
