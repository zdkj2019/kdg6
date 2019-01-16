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
import android.content.SharedPreferences;
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
import com.kdg6.activity.kc.ThcxShowActivity;
import com.kdg6.activity.login.LoginActivity;
import com.kdg6.activity.w.Pqzgjk;
import com.kdg6.cache.DataCache;
import com.kdg6.cache.ServiceReportCache;
import com.kdg6.common.Constant;
import com.kdg6.utils.Config;

/**
 * 快递柜-通用List
 *
 * @author zdkj
 *
 */
public class ListKdg extends FrameActivity {

	private String flag;
	private ListView listView;
	private EditText et_search;
	private SimpleAdapter adapter;
	private List<Map<String, Object>> data,currdata;
	private String[] from;
	private int[] to;
	private int queryType = DataCache.getinition().getQueryType();
	private String cs, status = "1",name;
	private SharedPreferences spf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_dispatchinginformationreceiving);
		initVariable();
		initView();
		initListeners();


	}

	@Override
	protected void initVariable() {

		listView = (ListView) findViewById(R.id.listView);
		from = new String[] { "textView1", "faultuser", "zbh", "timemy",
				"datemy", "ztzt" };
		to = new int[] { R.id.textView1, R.id.yytmy, R.id.pgdhmy, R.id.timemy,
				R.id.datemy, R.id.ztzt };
		spf = getSharedPreferences("loginsp", LoginActivity.MODE_WORLD_READABLE);
		findViewById(R.id.ll_filter).setVisibility(View.VISIBLE);
		et_search = (EditText) findViewById(R.id.et_search);
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
	}

	@Override
	protected void initListeners() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.bt_topback:
						onBackPressed();
						finish();
						break;

				}
			}
		};

		topBack.setOnClickListener(onClickListener);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				ServiceReportCache.setObjectdata(currdata);
				ServiceReportCache.setIndex(position);
				if (position >= 0) {
					Intent intent = null;
					if (queryType == 201) {
						intent = new Intent(ListKdg.this, JdxyKdg.class);
					} else if (queryType == 203) {
						intent = new Intent(ListKdg.this, SmdwKdg.class);
					} else if (queryType == 204) {
						intent = new Intent(ListKdg.this, FwbgKdg.class);
					} else if (queryType == 2041) {
						intent = new Intent(ListKdg.this, FwbgXgKdg.class);
					} else if (queryType == 205) {
						intent = new Intent(ListKdg.this, JqgdcxShowKdg.class);
					} else if (queryType == 2301) {
						intent = new Intent(ListKdg.this, JdxyXj.class);
					} else if (queryType == 2302) {
						intent = new Intent(ListKdg.this, SmdwXj.class);
					} else if (queryType == 2303) {
						intent = new Intent(ListKdg.this, FwbgXj.class);
					} else if (queryType == 2304) {
						intent = new Intent(ListKdg.this, FwbgXgXj.class);
					} else if (queryType == 2305) {
						intent = new Intent(ListKdg.this, SmdwXj.class);
					} else if (queryType == 2101) {
						intent = new Intent(ListKdg.this, DhjcKdg.class);
					} else if (queryType == 2201) {
						intent = new Intent(ListKdg.this, JdxyKdgWx.class);
					} else if (queryType == 2202) {
						intent = new Intent(ListKdg.this, SmdwKdgWx.class);
					} else if (queryType == 2203) {
						intent = new Intent(ListKdg.this, FwbgKdgWx.class);
					} else if (queryType == 2204) {
						intent = new Intent(ListKdg.this, ZzzpKdgWx.class);
					} else if (queryType == 2205) {
						intent = new Intent(ListKdg.this, FwbgXgKdgWx.class);
					} else if (queryType == 2206) {
						intent = new Intent(ListKdg.this, FwbgKdgWx.class);
					} else if (queryType == 2501) {
						intent = new Intent(ListKdg.this, Pqzgjk.class);
					} else if (queryType == 2801) {
						intent = new Intent(ListKdg.this,ThcxShowActivity.class);
					} else if (queryType == 2901) {
						intent = new Intent(ListKdg.this,CsgdcxShowKdg.class);
					}
					startActivityForResult(intent, 1);
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
	};

	@Override
	protected void getWebService(String s) {

		if ("query".equals(s)) {
			try {
				String sqlid = "";
				data = new ArrayList<Map<String, Object>>();
				int queryType = DataCache.getinition().getQueryType();
				String userid = spf.getString("userId", "");
				cs = userid;
				if (queryType == 201) {
					sqlid = "_PAD_SHGL_KDG_AZ_JDXY";
				} else if (queryType == 202) {
					sqlid = "";
				} else if (queryType == 203) {
					sqlid = "_PAD_SHGL_KDG_AZ_SMDW";
				} else if (queryType == 204) {
					sqlid = "_PAD_SHGL_KDG_AZ_FWBG";
				} else if (queryType == 2041) {
					sqlid = "_PAD_KDGAZ_FWBG_XG";
				} else if (queryType == 205) {
					status = getIntent().getStringExtra("status");
					sqlid = "_PAD_KDG_YWCX_JQGDCX1";
					cs = userid + "*" + status;
				} else if (queryType == 2301) {
					sqlid = "_PAD_KDG_XJ_JDXY";
				} else if (queryType == 2302) {
					sqlid = "_PAD_KDG_XJ_SMDW";
				} else if (queryType == 2303) {
					sqlid = "_PAD_KDG_XJ_FWBG";
				} else if (queryType == 2304) {
					sqlid = "_PAD_KDG_XJ_FWBG_XG";
				} else if (queryType == 2305) {
					sqlid = "_PAD_KDG_XJ_SMDW_ZD";
					cs = getIntent().getStringExtra("status");
				} else if (queryType == 2101) {
					sqlid = "_PAD_SHGL_KDG_AZ_DHJC";
				} else if (queryType == 2201) {
					sqlid = "_PAD_YWGL_WX_WXMKCX";
					cs = "2*"+userid;
				} else if (queryType == 2202) {
					sqlid = "_PAD_YWGL_WX_WXMKCX";
					cs = "3*"+userid;
				} else if (queryType == 2203) {
					sqlid = "_PAD_YWGL_WX_WXMKCX";
					cs = "4*"+userid;
				} else if (queryType == 2204) {
					sqlid = "_PAD_YWGL_WX_WXMKCX";
					cs = "1.2*"+userid;
				} else if (queryType == 2205) {
					sqlid = "_PAD_KDGAZ_FWBG_XG2";
					cs = userid;
				}  else if (queryType == 2206) {
					sqlid = "_PAD_YWGL_WX_WXMKCX";
					cs = "5*"+userid;
				} else if (queryType == 2501) {

					int status = getIntent().getIntExtra("status", 1);
					int sj = getIntent().getIntExtra("sj", 1);
					if (status == 1) {// 已完工
						sqlid = "_PAD_KDG_ZZPQGD_YWG";
						if (sj == 1) {// 本月
							cs = userid + "*" + userid + "*" + userid + "*"
									+ userid + "*0*0";
						} else {// 上月
							cs = userid + "*" + userid + "*" + userid + "*"
									+ userid + "*-1*-1";
						}

					} else {// 未完工
						sqlid = "_PAD_KDG_ZZPQGD_WWG";
						cs = userid + "*" + userid + "*" + userid + "*"
								+ userid;
					}

				} else if (queryType == 2801) {
					sqlid = "_PAD_DBCK_THCX1";
					cs = userid + "*" + userid;
				} else if (queryType == 2901) {
					status = getIntent().getStringExtra("status");
					sqlid = "_PAD_KDG_YWCX_CSGD1";
					cs = userid + "*" + status;
				}

				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						sqlid, cs, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");

				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, Object> item = new HashMap<String, Object>();
						String timeff = "";
						item.put("textView1", getListItemIcon(i));
						if (queryType == 201 || queryType == 203 || queryType == 204) {// 接单相应
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
						}
						if (queryType == 204) {
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
							item.put("sfecgd", temp.getString("sfecgd"));
							item.put("sbewm", temp.getString("sbewm"));
							item.put("dygdh", temp.getString("dygdh"));
							item.put("fwnr", temp.getString("fwnr"));
						}
						if (queryType == 2041) {
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
							item.put("sbewm", temp.getString("sbewm"));
							item.put("fwnr", temp.getString("fwnr"));
						}
						if (queryType == 2201||queryType == 2202||queryType == 2203
								||queryType == 2204||queryType == 2206) {
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc") + "   "
									+ temp.getString("xxdz"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("bz", temp.getString("bz"));
							//item.put("ds", temp.getString("ds"));
							item.put("fwnr", temp.getString("fwnr"));
							item.put("sfecgd", temp.getString("sfecgd"));
							item.put("sfecsm", temp.getString("sfecsm"));
							item.put("ecsmyy", temp.getString("ecsmyy"));
							item.put("kzzf1", temp.getString("kzzf1"));
							item.put("jddz", temp.getString("jddz"));
							item.put("dygdh1_mc", temp.getString("dygdh1_mc"));
							item.put("dygdh2_mc", temp.getString("dygdh2_mc"));
							item.put("axdh", temp.getString("axdh"));
							item.put("ddh", temp.getString("ddh"));
							item.put("ddh_mc", temp.getString("ddh_mc"));
							item.put("yqsx", temp.getString("yqsx"));
							item.put("lxdh", temp.getString("lxdh"));
							item.put("kzzf6", temp.getString("kzzf6"));
							item.put("kzzf7", temp.getString("kzzf7"));
							item.put("kzzf8", temp.getString("kzzf8"));
							item.put("kzsz1", temp.getString("kzsz1"));

							item.put("kzzf7_bm", temp.getString("kzzf7_bm"));
							item.put("kzzf3_bm", temp.getString("kzzf3_bm"));
							item.put("kzzf4_bm", temp.getString("kzzf4_bm"));
							item.put("kzzf5_bm", temp.getString("kzzf5_bm"));

							item.put("red", "2");
						}

						if (queryType == 2205) {
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc") + "   "
									+ temp.getString("xxdz"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("bz", temp.getString("bz"));
							//item.put("ds", temp.getString("ds"));
							item.put("fwnr", temp.getString("fwnr"));
							item.put("sfecgd", temp.getString("sfecgd"));
							item.put("sfecsm", temp.getString("sfecsm"));
							item.put("ecsmyy", temp.getString("ecsmyy"));
							item.put("kzzf1", temp.getString("kzzf1"));
							item.put("dygdh1_mc", temp.getString("dygdh1_mc"));
							item.put("dygdh2_mc", temp.getString("dygdh2_mc"));
							item.put("axdh", temp.getString("axdh"));
							item.put("ddh", temp.getString("ddh"));
							item.put("ddh_mc", temp.getString("ddh_mc"));
							item.put("yqsx", temp.getString("yqsx"));
							item.put("lxdh", temp.getString("lxdh"));
							item.put("kzzf6", temp.getString("kzzf6"));
							item.put("kzzf7", temp.getString("kzzf7"));
							item.put("kzzf8", temp.getString("kzzf8"));
							item.put("kzsz1", temp.getString("kzsz1"));
							item.put("sfhf", temp.getString("sfhf"));
							item.put("red", "2".equals(temp.getString("sfhf"))?"1":"2");
							item.put("jddz", temp.getString("jddz"));
							item.put("kzzf7_bm", temp.getString("kzzf7_bm"));
							item.put("kzzf3_bm", temp.getString("kzzf3_bm"));
							item.put("kzzf4_bm", temp.getString("kzzf4_bm"));
							item.put("kzzf5_bm", temp.getString("kzzf5_bm"));
						}

						if (queryType == 2301||queryType == 2302||queryType == 2303
								||queryType == 2304||queryType == 2305) {
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc") + "   "
									+ temp.getString("xxdz"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sbbm", temp.getString("sbbm"));
							item.put("djzt", temp.getString("djzt"));
							item.put("ds", temp.getString("ds"));
							item.put("ssqx", temp.getString("ssqx"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("jddz", temp.getString("jddz"));
							item.put("zdrq", temp.getString("zdrq"));
							item.put("jsrq", temp.getString("jsrq"));
							item.put("fgsl", temp.getString("fgsl"));
							item.put("sblx", temp.getString("sblx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("bz", temp.getString("bz"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("djzt2", temp.getString("djzt2"));
							item.put("red", "2");
						}

						if(queryType == 2304){
							item.put("fwnr", temp.getString("fwnr"));
							item.put("red","2".equals(temp.getString("sfhf"))?"1":"2");
						}

						if(queryType==205){
							item.put("textView1", getListItemIcon1(temp.getString("djzt")));
							timeff = temp.getString("wcsj");
							item.put("faultuser", temp.getString("xqmc") + "   "
									+ temp.getString("xxdz"));
							item.put("zbh", temp.getString("zbh"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("wcsj", temp.getString("wcsj"));
						}

						if(queryType==2901){
							item.put("textView1", getListItemIcon1(temp.getString("djzt")));
							timeff = temp.getString("wcsj");
							item.put("faultuser", temp.getString("xqmc") + "   "
									+ temp.getString("xxdz"));
							item.put("zbh", temp.getString("zbh"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("wcsj", temp.getString("wcsj"));
							item.put("csshzt", temp.getString("csshzt"));
							item.put("csshzt_bm", temp.getString("csshzt_bm"));
							item.put("red", "0".equals(temp.getString("csshzt_bm"))?"1":"2");
						}

						if (queryType == 2501) {
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
							item.put("jbf", temp.getString("jbf"));
							item.put("jbfdh", temp.getString("jbfdh"));
							item.put("sfcs", temp.getString("sfcs"));
							item.put("csyylx", temp.getString("csyylx"));
							item.put("csnr", temp.getString("csnr"));
							item.put("scsj", temp.getString("scsj"));
							item.put("ddsj", temp.getString("ddsj"));
							item.put("sfwzzm", temp.getString("sfwzzm"));
						}
						if (queryType == 2041) {
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
							item.put("sfecsm", temp.getString("sfecsm"));
							item.put("ecsmyy", temp.getString("ecsmyy"));
						}

						if (queryType == 2801) {
							item.put("textView1", getListItemIcon(i));
							timeff = temp.getString("zdrq");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("sgdh"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sgdh", temp.getString("sgdh"));
							item.put("bz", temp.getString("bz"));
						}

						timeff = timeff.substring(2);
						item.put("timemy", mdateformat(1, timeff));// 时间
						item.put("datemy", mdateformat(0, timeff));// 年月日

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
					if (map.get("zbh").toString().indexOf(name) != -1||map.get("xqmc").toString().indexOf(name) != -1) {
						currdata.add(map);
					}
				}
				ServiceReportCache.setObjectdata(currdata);
				Message msg = new Message();
				msg.what = Constant.SUCCESS;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.SUCCESS;
				handler.sendMessage(msg);
			}
		}

	}

	// @Override
	// public void onBackPressed() {
	// Intent intent = new Intent(this, MainActivity.class);
	// startActivity(intent);
	// finish();
	// }

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
				Map<String, Object> item = data.get(position);
				String red = (String) item.get("red");
				if ("1".equals(red)) {
					view.setBackgroundResource(R.color.red);
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
					adapter = new CurrAdapter(ListKdg.this,
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
									onBackPressed();
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
