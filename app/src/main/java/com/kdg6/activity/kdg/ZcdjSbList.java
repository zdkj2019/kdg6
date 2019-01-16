package com.kdg6.activity.kdg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.kdg6.R;
import com.kdg6.activity.FrameActivity;
import com.kdg6.activity.main.MainActivity;
import com.kdg6.cache.DataCache;
import com.kdg6.cache.ServiceReportCache;
import com.kdg6.common.Constant;
import com.kdg6.utils.Config;

/**
 * 快递柜-资产登记list
 *
 * @author zdkj
 *
 */
public class ZcdjSbList extends FrameActivity {

	private String flag,name;
	private EditText et_search;
	private ListView listView;
	private SimpleAdapter adapter;
	private List<Map<String, Object>> data, currdata;
	private String[] from;
	private int[] to;
	private String sqlid,cs,wdbm,type;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_kdg_zcdjsblist);
		initVariable();
		initView();
		initListeners();

	}

	@Override
	protected void initVariable() {
		et_search = (EditText) findViewById(R.id.et_search);
		et_search.setHint("请输入柜机编码");
		listView = (ListView) findViewById(R.id.listView);

		from = new String[] { "textView1", "faultuser", "zbh", "timemy",
				"datemy", "ztzt" };
		to = new int[] { R.id.textView1, R.id.yytmy, R.id.pgdhmy, R.id.timemy,
				R.id.datemy, R.id.ztzt };
	}

	@Override
	protected void initView() {
		//findViewById(R.id.ll_filter).setVisibility(View.VISIBLE);
		title.setText(DataCache.getinition().getTitle());
		sqlid = getIntent().getStringExtra("sqlid");
		cs = getIntent().getStringExtra("cs");
		wdbm = getIntent().getStringExtra("wdbm");
		type = getIntent().getStringExtra("type");

	}

	@Override
	protected void initListeners() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.bt_topback:
						onBackPressed();
						break;

				}
			}
		};

		topBack.setOnClickListener(onClickListener);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int arg2,
									long id) {
				ServiceReportCache.setIndex(arg2);
				if (arg2 >= 0) {
					Intent intent = null;
					intent = new Intent(ZcdjSbList.this, ZcdjInfo.class);
					intent.putExtra("type", "dxj".equals(type)?"2":"4");
					startActivity(intent);
				}

			}
		});


		et_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(final CharSequence s, int start,
									  int before, int count) {
				name = s.toString();
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {

						getWebService("getdata");
					}
				});
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				getWebService("query");
			}
		});
	}


	@Override
	protected void getWebService(String s) {

		if ("query".equals(s)) {
			try {

				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						sqlid, cs, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				data = new ArrayList<Map<String, Object>>();
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, Object> item = new HashMap<String, Object>();
						item.put("textView1", getListItemIcon(i));
						item.put("faultuser", temp.getString("sbbm"));
						item.put("zbh", temp.getString("xxdz"));
						item.put("zzbh", temp.getString("zbh"));
						item.put("sbbm", temp.getString("sbbm"));
						item.put("xxdz", temp.getString("xxdz"));
						item.put("jddz", temp.getString("jddz"));
						item.put("hpbm", temp.getString("hpbm"));
						item.put("sblx", temp.getString("sblx"));
						item.put("sf", temp.getString("sf"));
						item.put("ds", temp.getString("ds"));
						item.put("qx", temp.getString("qx"));
						item.put("wdbmwd", temp.getString("wdbmwd"));
						item.put("kzsz4", temp.getString("kzsz4"));
						item.put("sf_mc", temp.getString("sf_mc"));
						item.put("ds_mc", temp.getString("ds_mc"));
						item.put("qx_mc", temp.getString("qx_mc"));
						item.put("wdbmwd_mc", temp.getString("wdbmwd_mc"));
						item.put("sblx_mc", temp.getString("sblx_mc"));
						item.put("sblx", temp.getString("sblx"));
						item.put("sblx_mc", temp.getString("sblx_mc"));
						item.put("fwnr", temp.getString("fwnr"));
						item.put("zgbm", temp.getString("zgbm"));
						item.put("fgbm", temp.getString("fgbm"));
						item.put("kzzf1", temp.getString("kzzf1"));
						item.put("djzt", temp.getString("djzt"));
						item.put("bz", temp.getString("bz"));
						item.put("pgbm", temp.getString("pgbm"));
						item.put("red", "0".equals(temp.getString("djzt"))?"1":"2");

						item.put("timemy", "");
						item.put("datemy", "");
						item.put("ztzt", "");
						data.add(item);
					}
					currdata = data;
					ServiceReportCache.setObjectdata(currdata);
					Message msg = new Message();
					msg.what = Constant.SUCCESS;// 成功
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = Constant.FAIL;// 失败
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

		if ("getdata".equals(s)) {
			currdata = new ArrayList<Map<String, Object>>();
			try {
				for (int i = 0; i < data.size(); i++) {
					Map<String, Object> map = data.get(i);
					if (((String) map.get("faultuser")).indexOf(name) != -1) {
						currdata.add(map);
					}
				}
				ServiceReportCache.setObjectdata(currdata);
				Message msg = new Message();
				msg.what = Constant.SUCCESS;
				handler.sendMessage(msg);
			} catch (Exception e) {
				Message msg = new Message();
				msg.what = Constant.SUCCESS;
				handler.sendMessage(msg);
			}
		}

	}

	private class CurrAdapter extends SimpleAdapter {

		public CurrAdapter(Context context,
						   List<? extends Map<String, ?>> data, int resource,
						   String[] from, int[] to) {
			super(context, data, resource, from, to);

		}

		@SuppressLint("ResourceAsColor")
		@Override
		public View getView(final int position, View convertView,
							final ViewGroup parent) {
			final View view = super.getView(position, convertView, parent);
			try {
				Map<String, Object> item = currdata.get(position);
				String red =  item.get("red").toString();
				if ("1".equals(red)) {
					view.setBackgroundResource(R.color.tomato);
				} else {
					view.setBackgroundResource(R.color.white);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return view;
		}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			switch (msg.what) {
				case Constant.NETWORK_ERROR:
					dialogShowMessage_P("网络连接出错，请检查你的网络设置", null);
					break;
				case Constant.SUCCESS:
					adapter = new CurrAdapter(ZcdjSbList.this,
							currdata,
							R.layout.listview_dispatchinginformationreceiving_item,
							from, to);
					listView.setAdapter(adapter);
					break;
				case Constant.FAIL:
					dialogShowMessage_P("没有查询数据",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
													int which) {
									Intent intent = new Intent(
											getApplicationContext(),
											MainActivity.class);
									startActivity(intent);
									finish();
								}
							});
					break;
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}

	};

}
