package com.kdg6.activity.kdg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.kdg6.R;
import com.kdg6.activity.FrameActivity;
import com.kdg6.activity.login.LoginActivity;
import com.kdg6.cache.DataCache;
import com.kdg6.common.Constant;
import com.kdg6.utils.Config;

/**
 * 快递柜-首次巡检
 *
 * @author zdkj
 *
 */
public class ZcdjQuery extends FrameActivity {

	private Button confirm, cancel;
	private SimpleAdapter adapter;
	private Spinner spinner_sf, spinner_ds, spinner_qx;
	private ArrayList<Map<String, String>> data_sf, data_ds, data_qx;
	private String[] from;
	private int[] to;
	private String sfbm="", dsbm="", qxbm="",msgStr;
	private SharedPreferences spf;
	private SharedPreferences.Editor spfe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_zcdjquery);
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
		confirm.setText("待巡检");
		cancel.setText("未通过");

		spinner_sf = (Spinner) findViewById(R.id.spinner_sf);
		spinner_ds = (Spinner) findViewById(R.id.spinner_ds);
		spinner_qx = (Spinner) findViewById(R.id.spinner_qx);
		from = new String[] { "id", "name" };
		to = new int[] { R.id.bm, R.id.name };

		title.setText("首次巡检");

		loadData();
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
						if(qxbm==null||"".equals(qxbm)){
							toastShowMessage("请选择区县");
							return;
						}

						try {
							JSONObject json = new JSONObject();
							json.put("sfbm", sfbm);
							json.put("dsbm", dsbm);
							json.put("qxbm", qxbm);
							spfe = spf.edit();
							spfe.putString("ssx", json.toString());
							spfe.commit();
						} catch (Exception e) {
							// TODO: handle exception
						}
						DataCache.getinition().setTitle("首次巡检未通过");
						String userid = DataCache.getinition().getUserId();
						DataCache.getinition().setQueryType(2601);
						Intent intent = new Intent(getApplicationContext(),ZcdjList.class);
						intent.putExtra("cs", userid+"*"+qxbm);
						intent.putExtra("sqlid", "_PAD_KDG_SCXJ_YXJ");
						intent.putExtra("type", "yxj");
						startActivity(intent);
						break;
					case R.id.confirm:
						if(qxbm==null||"".equals(qxbm)){
							toastShowMessage("请选择区县");
							return;
						}

						try {
							JSONObject json = new JSONObject();
							json.put("sfbm", sfbm);
							json.put("dsbm", dsbm);
							json.put("qxbm", qxbm);
							spfe = spf.edit();
							spfe.putString("ssx", json.toString());
							spfe.commit();
						} catch (Exception e) {
							// TODO: handle exception
						}

						DataCache.getinition().setTitle("待首次巡检");
						DataCache.getinition().setQueryType(2601);
						intent = new Intent(getApplicationContext(),ZcdjList.class);
						intent.putExtra("cs", qxbm);
						intent.putExtra("sqlid", "_PAD_ZCGL_SB_ZCDJ1");
						intent.putExtra("type", "dxj");
						startActivity(intent);

						break;
					default:
						break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);

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
	}


	private void loadData(){
		try {
			spf = getSharedPreferences("zcdj", ZcdjQuery.MODE_PRIVATE);
			String jsonStr = spf.getString("ssx", "");
			if(!"".equals(jsonStr)){
				JSONObject json = new JSONObject(jsonStr);
				sfbm = json.getString("sfbm");
				dsbm = json.getString("dsbm");
				qxbm = json.getString("qxbm");
			}
		} catch (Exception e) {

		}
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
				case Constant.NUM_6:
					adapter = new SimpleAdapter(ZcdjQuery.this, data_sf,
							R.layout.spinner_item, from, to);
					spinner_sf.setAdapter(adapter);
					if(!"".equals(sfbm)){
						for (int i = 0; i < data_sf.size(); i++) {
							Map<String, String> map = data_sf.get(i);
							if (sfbm.equals(map.get("id"))) {
								spinner_sf.setSelection(i);
							}
						}
					}

					break;
				case Constant.NUM_7:
					adapter = new SimpleAdapter(ZcdjQuery.this, data_ds,
							R.layout.spinner_item, from, to);
					spinner_ds.setAdapter(adapter);
					if(!"".equals(dsbm)){
						for (int i = 0; i < data_ds.size(); i++) {
							Map<String, String> map = data_ds.get(i);
							if (dsbm.equals(map.get("id"))) {
								spinner_ds.setSelection(i);
							}
						}
					}
					break;
				case Constant.NUM_8:
					adapter = new SimpleAdapter(ZcdjQuery.this, data_qx,
							R.layout.spinner_item, from, to);
					spinner_qx.setAdapter(adapter);
					if(!"".equals(qxbm)){
						for (int i = 0; i < data_qx.size(); i++) {
							Map<String, String> map = data_qx.get(i);
							if (qxbm.equals(map.get("id"))) {
								spinner_qx.setSelection(i);
							}
						}
					}

					break;

			}

		}
	};


}
