package com.kdg6.activity.kdg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.kdg6.R;
import com.kdg6.activity.FrameActivity;
import com.kdg6.cache.DataCache;
import com.kdg6.common.Constant;
import com.kdg6.utils.Config;

/**
 * 快递柜-选单定位
 *
 * @author zdkj
 *
 */
public class Xddwcx extends FrameActivity {

	private Button confirm, cancel;
	private LinearLayout ll_cxtj,ll_cxfs;
	private RadioGroup rg_0,rg_gdzt_0;
	private SimpleAdapter adapter;
	private Spinner spinner_sf, spinner_ds, spinner_qx, spinner_wdmc;
	private ArrayList<Map<String, String>> data_sf, data_ds, data_qx, data_wdmc;
	private String[] from;
	private int[] to;
	private String sfbm, dsbm, qxbm,  wdbm,msgStr;
	private BDLocation location;
	private LocationClient mLocClient;
	private BDLocationListener myListener = new MyLocationListener();
	private boolean hasDw = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_xddwcx);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				getWebService("getsf");
			}
		});
	}

	@Override
	protected void initVariable() {

	}

	@Override
	protected void initView() {

		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("查询");
		cancel.setText("取消");

		rg_gdzt_0 = (RadioGroup) findViewById(R.id.rg_gdzt_0);
		rg_0 = (RadioGroup) findViewById(R.id.rg_0);
		ll_cxtj = (LinearLayout) findViewById(R.id.ll_cxtj);
		ll_cxfs = (LinearLayout) findViewById(R.id.ll_cxfs);
		spinner_sf = (Spinner) findViewById(R.id.spinner_sf);
		spinner_ds = (Spinner) findViewById(R.id.spinner_ds);
		spinner_qx = (Spinner) findViewById(R.id.spinner_qx);
		spinner_wdmc = (Spinner) findViewById(R.id.spinner_wdmc);
		from = new String[] { "id", "name" };
		to = new int[] { R.id.bm, R.id.name };

		title.setText(DataCache.getinition().getTitle());

		mLocClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocClient.registerLocationListener(myListener); // 注册监听函数

		setLocationClientOption();
	}

	@Override
	protected void initListeners() {
		//
		OnClickListener backonClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.bt_topback:
						onBackPressed();
						break;
					case R.id.cancel:
						onBackPressed();

						break;
					case R.id.confirm:
						if(rg_gdzt_0.getCheckedRadioButtonId()==R.id.rb_gdzt_1){
							if(rg_0.getCheckedRadioButtonId()==R.id.rb_1){
								if(hasDw){
									String jd = location.getLongitude()+"";
									String wd = location.getLatitude()+"";
									String cs1 = jd+"*"+wd+"*"+jd+"*"+wd;
									String sqlid1 = "_PAD_YWGL_XJ_XZJDFJ";
									Intent intent1 = new Intent(getApplicationContext(),XddwList.class);
									intent1.putExtra("cs", cs1);
									intent1.putExtra("type", "wdj");
									intent1.putExtra("sqlid", sqlid1);
									startActivity(intent1);
								}else{
									toastShowMessage("未获取到位置信息，请重新定位");
								}
							}else{
								if(qxbm==null){
									toastShowMessage("请选择区县");
									return;
								}
								if(wdbm==null){
									toastShowMessage("请选择小区");
									return;
								}
								String cs = qxbm+"*"+wdbm+"*"+wdbm;
								String sqlid = "_PAD_YWGL_XJ_XZJDCZ";
								Intent intent = new Intent(getApplicationContext(),XddwList.class);
								intent.putExtra("cs", cs);
								intent.putExtra("type", "wdj");
								intent.putExtra("sqlid", sqlid);
								startActivity(intent);
							}
						}else{
							String cs = DataCache.getinition().getUserId();
							String sqlid = "_PAD_YWGL_XJ_CGXJ_YJD";
							Intent intent = new Intent(getApplicationContext(),XddwList.class);
							intent.putExtra("cs", cs);
							intent.putExtra("type", "ydj");
							intent.putExtra("sqlid", sqlid);
							startActivity(intent);
						}

						break;
					default:
						break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);

		rg_0.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId==R.id.rb_1){
					ll_cxtj.setVisibility(View.GONE);
				}else{
					ll_cxtj.setVisibility(View.VISIBLE);
				}

			}
		});

		rg_gdzt_0.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId==R.id.rb_gdzt_1){
					ll_cxfs.setVisibility(View.VISIBLE);
					if(rg_0.getCheckedRadioButtonId()==R.id.rb_1){
						ll_cxtj.setVisibility(View.GONE);
					}else{
						ll_cxtj.setVisibility(View.VISIBLE);
					}
				}else{
					ll_cxfs.setVisibility(View.GONE);
					ll_cxtj.setVisibility(View.GONE);
				}

			}
		});

		spinner_sf.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				sfbm = data_sf.get(position).get("id");
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {

						getWebService("getds");
					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spinner_ds.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				dsbm = data_ds.get(position).get("id");
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {

						getWebService("getqx");
					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spinner_qx.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				qxbm = data_qx.get(position).get("id");
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {

						getWebService("getwdmc");
					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spinner_wdmc.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				wdbm = data_wdmc.get(position).get("id");
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	protected void getWebService(String s) {
		if ("getsf".equals(s)) {
			try {
				data_sf = new ArrayList<Map<String, String>>();
				Map<String, String> item = new HashMap<String, String>();
				item.put("id", "");
				item.put("name", "");
				data_sf.add(item);
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBGL_SBLR_DQXX1", "", "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						item = new HashMap<String, String>();
						item.put("id", temp.getString("sfbm"));
						item.put("name", temp.getString("sfmc"));
						data_sf.add(item);
					}

					Message msg = new Message();
					msg.what = Constant.NUM_6;
					handler.sendMessage(msg);
				} else {
					msgStr = "获取省份信息失败";
					Message msg = new Message();
					msg.what = Constant.FAIL;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}


		if ("getds".equals(s)) {
			try {
				data_ds = new ArrayList<Map<String, String>>();
				Map<String, String> item = new HashMap<String, String>();
				item.put("id", "");
				item.put("name", "");
				data_ds.add(item);
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBGL_SBLR_DQXX2", sfbm, "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						item = new HashMap<String, String>();
						item.put("id", temp.getString("dsbm"));
						item.put("name", temp.getString("dsmc"));
						data_ds.add(item);
					}
				}
				Message msg = new Message();
				msg.what = Constant.NUM_7;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

		if ("getqx".equals(s)) {
			try {
				data_qx = new ArrayList<Map<String, String>>();
				Map<String, String> item = new HashMap<String, String>();
				item.put("id", "");
				item.put("name", "");
				data_qx.add(item);
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBGL_SBLR_DQXX3", dsbm, "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						item = new HashMap<String, String>();
						item.put("id", temp.getString("qxbm"));
						item.put("name", temp.getString("qxmc"));
						data_qx.add(item);
					}

				}
				Message msg = new Message();
				msg.what = Constant.NUM_8;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

		if ("getwdmc".equals(s)) {
			try {
				data_wdmc = new ArrayList<Map<String, String>>();
				Map<String, String> item = new HashMap<String, String>();
				item.put("id", "");
				item.put("name", "");
				data_wdmc.add(item);
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBGL_SBLR_WDXX1", qxbm, "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						item = new HashMap<String, String>();
						item.put("id", temp.getString("wdbm"));
						item.put("name", temp.getString("wdmc"));
						item.put("kzzf5", temp.getString("kzzf5"));
						item.put("xxdz", temp.getString("xxdz"));
						data_wdmc.add(item);
					}

				}
				Message msg = new Message();
				msg.what = Constant.NUM_10;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

	}

	/*
	 * BDLocationListener接口有2个方法需要实现： 1.接收异步返回的定位结果，参数是BDLocation类型参数。
	 * 2.接收异步返回的POI查询结果，参数是BDLocation类型参数。
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation locations) {
			if (locations == null) {
				return;
			} else {
				location = locations;
				Message msg = new Message();
				msg.what = Constant.NUM_9;// 成功
				handler.sendMessage(msg);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {

		}

		@Override
		public void onConnectHotSpotMessage(String arg0, int arg1) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * 设置定位参数包括：定位模式（单次定位，定时定位），返回坐标类型，是否打开GPS等等。
	 */
	private void setLocationClientOption() {

		final LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(1000);// 设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);// 禁止启用缓存定位
		option.setPriority(LocationClientOption.GpsFirst);
		option.setAddrType("all");
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			switch (msg.what) {
				case Constant.SUCCESS:
					if (progressDialog != null) {
						progressDialog.dismiss();
					}

					break;
				case Constant.FAIL:
					if (progressDialog != null) {
						progressDialog.dismiss();
					}
					dialogShowMessage_P("查询失败,"+msgStr, null);
					break;
				case Constant.NETWORK_ERROR:
					if (progressDialog != null) {
						progressDialog.dismiss();
					}
					dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
					break;
				case Constant.NUM_9:
					mLocClient.stop();
					mLocClient.unRegisterLocationListener(myListener);
					hasDw = true;

					break;
				case Constant.NUM_6:
					adapter = new SimpleAdapter(Xddwcx.this, data_sf,
							R.layout.spinner_item, from, to);
					spinner_sf.setAdapter(adapter);

					break;
				case Constant.NUM_7:
					adapter = new SimpleAdapter(Xddwcx.this, data_ds,
							R.layout.spinner_item, from, to);
					spinner_ds.setAdapter(adapter);

					break;
				case Constant.NUM_8:
					adapter = new SimpleAdapter(Xddwcx.this, data_qx,
							R.layout.spinner_item, from, to);
					spinner_qx.setAdapter(adapter);

					break;
				case Constant.NUM_10:
					adapter = new SimpleAdapter(Xddwcx.this, data_wdmc,
							R.layout.spinner_item, from, to);
					spinner_wdmc.setAdapter(adapter);

					break;

			}

		}
	};


}
