package com.kdg6.activity.kdg;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.kdg6.R;
import com.kdg6.activity.FrameActivity;
import com.kdg6.activity.util.BaiduMapActivity;
import com.kdg6.activity.w.GlzdShowActivity;
import com.kdg6.cache.DataCache;
import com.kdg6.cache.ServiceReportCache;
import com.kdg6.common.Constant;
import com.kdg6.utils.Config;
import com.kdg6.utils.DateUtil;
import com.kdg6.zxing.CaptureActivity;

/**
 * 快递柜-上门定位
 *
 * @author zdkj
 *
 */
public class SmdwKdgWx extends FrameActivity {

	private EditText et_gjbm;
	private TextView tv_time, tv_jd, tv_wd, tv_dz, tv_jssx,tv_ewm;
	private Button confirm, cancel,btn_sm;
	private Spinner spinner_gzdl, spinner_gzzl, spinner_gzxl;
	private ImageView iv_telphone,iv_help;
	private List<Map<String, String>> data_gzbm, data_all, gzbm_2_list,gzbm_3_list;
	private String flag, zbh, msgStr,cssj,lxdh,djsStr,keyStr,ewmStr="";
	private String zlbm,zlmc;
	private String[] from;
	private int[] to;
	private BDLocation location;
	private LocationClient mLocClient;
	private BDLocationListener myListener = new MyLocationListener();
	private boolean hasDw = false;
	private boolean iscs = false;
	private Timer  timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_smdw);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				getWebService("query");
			}
		});
	}

	@Override
	protected void initVariable() {

		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("提交");
		cancel.setText("返回");
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		et_gjbm = (EditText) findViewById(R.id.et_gjbm);
		btn_sm = (Button) findViewById(R.id.btn_sm);
		tv_ewm = (TextView) findViewById(R.id.tv_ewm);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_jd = (TextView) findViewById(R.id.tv_jd);
		tv_wd = (TextView) findViewById(R.id.tv_wd);
		tv_dz = (TextView) findViewById(R.id.tv_dz);
		iv_telphone = (ImageView) findViewById(R.id.iv_telphone);
		iv_help = (ImageView) findViewById(R.id.iv_help);
		spinner_gzdl = (Spinner) findViewById(R.id.spinner_gzdl);
		spinner_gzzl = (Spinner) findViewById(R.id.spinner_gzzl);
		spinner_gzxl = (Spinner) findViewById(R.id.spinner_gzxl);

		data_gzbm = new ArrayList<Map<String, String>>();
		gzbm_2_list = new ArrayList<Map<String, String>>();
		gzbm_3_list = new ArrayList<Map<String, String>>();

		from = new String[] { "id", "name" };
		to = new int[] { R.id.bm, R.id.name };

		final Map<String, Object> itemmap = ServiceReportCache.getObjectdata().get(ServiceReportCache.getIndex());

		zbh = (String) itemmap.get("zbh");
		cssj = (String) itemmap.get("cssj");
		lxdh = (String) itemmap.get("lxdh");
		keyStr = (String) itemmap.get("xqmc");
		zlbm = (String) itemmap.get("kzzf4_bm");
		zlmc = (String) itemmap.get("kzzf4");
		((TextView) findViewById(R.id.tv_1)).setText((String)itemmap.get("zbh"));
		((TextView) findViewById(R.id.tv_2)).setText((String)itemmap.get("xqmc"));
		((TextView) findViewById(R.id.tv_3)).setText((String)itemmap.get("xxdz"));
		((TextView) findViewById(R.id.tv_4)).setText("");
		((TextView) findViewById(R.id.tv_5)).setText((String)itemmap.get("lxdh"));

		((TextView) findViewById(R.id.tv_gzmk)).setText(zlmc);
		((TextView) findViewById(R.id.tv_gzmk)).setTag(zlbm);

		mLocClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocClient.registerLocationListener(myListener); // 注册监听函数

		setLocationClientOption();



		tv_jssx = (TextView) findViewById(R.id.tv_4);
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				showDjs();
			}
		});
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
//					if(tv_jd.getText().toString().indexOf("4.9E")!=-1){
//						dialogShowMessage_P("定位失败，请到开阔地重试", null);
//						return;
//					}
//					long now = new Date().getTime();
//					long sj = DateUtil.StringToDate(bzsj).getTime()+15*60*1000;
//					if(now<sj){
//						toastShowMessage("时间未到，不能定位。");
//						return;
//					}
//					String smfsbm = data_smfs.get(spinner_smfs.getSelectedItemPosition()).get("id");
//					if ("".equals(smfsbm)) {
//						toastShowMessage("请选择上门方式");
//						return;
//					}
//					String str = ((Map<String, String>) spinner_gzxl.getSelectedItem()).get("name");
//					if (" ".equals(str)) {
//						toastShowMessage("请选择故障原因！");
//						return;
//					}
						if(!isNotNull(tv_ewm)&&!isNotNull(et_gjbm)){
							toastShowMessage("柜机编码或二维码不能为空！");
							return;
						}
						if (hasDw) {
							showProgressDialog();
							Config.getExecutorService().execute(new Runnable() {

								@Override
								public void run() {
									getWebService("submit");
								}
							});
						} else {
							toastShowMessage("定位中，请稍后......");
						}
						break;
					case R.id.btn_sm:
						startSm();
						break;
					default:
						break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);
		btn_sm.setOnClickListener(backonClickListener);

		OnItemSelectedListener onItemSelectedListener_gzdl = new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {

				String select_id = data_gzbm.get(arg2).get("id");
				gzbm_2_list.clear();
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", "     ");
				map.put("name", " ");
				gzbm_2_list.add(map);
				// 选择的大类 设置中类
				for (int i = 0; i < data_all.size(); i++) {

					String parent_id = data_all.get(i).get("parent");
					if (parent_id.startsWith(select_id)) {
						// 相等添加到维护厂商显示的list里
						gzbm_2_list.add(data_all.get(i));
					}
				}

				SimpleAdapter adapter = new SimpleAdapter(SmdwKdgWx.this,
						gzbm_2_list, R.layout.spinner_item, from, to);
				spinner_gzzl.setAdapter(adapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		};

		OnItemSelectedListener onItemSelectedListener_gzzl = new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {

				String select_id = gzbm_2_list.get(arg2).get("id");
				gzbm_3_list.clear();
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", "     ");

				map.put("name", " ");
				gzbm_3_list.add(map);

				for (int i = 0; i < data_all.size(); i++) {

					String parent_id = data_all.get(i).get("parent");
					if (parent_id.startsWith(select_id)) {
						// 相等添加到维护厂商显示的list里
						gzbm_3_list.add(data_all.get(i));
					}
				}

				SimpleAdapter adapter = new SimpleAdapter(SmdwKdgWx.this,
						gzbm_3_list, R.layout.spinner_item, from, to);
				spinner_gzxl.setAdapter(adapter);


			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		};

		spinner_gzdl.setOnItemSelectedListener(onItemSelectedListener_gzdl);// 故障大类
		spinner_gzzl.setOnItemSelectedListener(onItemSelectedListener_gzzl);// 故障中类

		iv_telphone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("".equals(lxdh)) {
					toastShowMessage("请选择联系电话！");
					return;
				}
				Call(lxdh);
			}
		});

		iv_help.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),GlzdShowActivity.class);
				intent.putExtra("zlbm", zlbm);
				startActivity(intent);
			}
		});

		findViewById(R.id.iv_baidumap).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						BaiduMapActivity.class);
				intent.putExtra("keyStr", keyStr);
				startActivity(intent);
			}
		});
	}

	private void startSm() {
		// 二维码
		Intent intent = new Intent(getApplicationContext(),
				CaptureActivity.class);
		startActivityForResult(intent, 2);
	}

	/**
	 * 倒计时
	 */
	private void showDjs(){
		try {

			final Date csDate = DateUtil.StringToDate(cssj);
			timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					djsStr = "";
					Date now = new Date();
					long last = csDate.getTime()-now.getTime();
					if(last<0){
						iscs = true;
					}
					String dayStr = "",hoursStr = "",minuteStr = "",secondStr = "";
					int day = (int) (last/(24*3600*1000));
					long leave1=last%(24*3600*1000);    //计算天数后剩余的毫秒数
					int hours=(int) (leave1/(3600*1000));
					//计算相差分钟数
					long leave2=leave1%(3600*1000);        //计算小时数后剩余的毫秒数
					int minutes=(int) (leave2/(60*1000));
					//计算相差秒数
					long leave3=leave2%(60*1000);      //计算分钟数后剩余的毫秒数
					int seconds=Math.round(leave3/1000);

					dayStr = ""+day;
					hoursStr = ""+hours;
					minuteStr = ""+minutes;
					secondStr = ""+seconds;
					djsStr = dayStr + "天" + hoursStr + "小时" + minuteStr+ "分钟" + secondStr + "秒";

					Message msg = new Message();
					msg.what = Constant.NUM_9;
					handler.sendMessage(msg);
				}
			}, 0,1000);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void loadGzdl(){


		String select_id = data_gzbm.get(1).get("id");
		gzbm_2_list.clear();
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", "     ");
		map.put("name", " ");
		gzbm_2_list.add(map);
		// 选择的大类 设置中类
		for (int i = 0; i < data_all.size(); i++) {

			String parent_id = data_all.get(i).get("parent");
			if (parent_id.startsWith(select_id)) {
				// 相等添加到维护厂商显示的list里
				gzbm_2_list.add(data_all.get(i));
			}
		}

		SimpleAdapter adapter = new SimpleAdapter(SmdwKdgWx.this,
				gzbm_2_list, R.layout.spinner_item, from, to);
		spinner_gzzl.setAdapter(adapter);

	}

	@Override
	protected void getWebService(String s) {

		if(s.equals("query")){
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBGZLB", "", "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				data_all = new ArrayList<Map<String, String>>();
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					Map<String, String> item = new HashMap<String, String>();
					item.put("id", "     ");
					item.put("name", " ");
					data_gzbm.add(item);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						item = new HashMap<String, String>();
						String id = temp.getString("gzbm");
						String sjbm = temp.getString("sjlb");
						item.put("id", id);
						item.put("name", temp.getString("gzmc"));
						item.put("parent", sjbm);
						if ("00".equals(sjbm)) {
							data_gzbm.add(item);
						}
						data_all.add(item);
					}
					Message msg = new Message();
					msg.what = Constant.NUM_6;
					handler.sendMessage(msg);
				}else{
					Message msg = new Message();
					msg.what = Constant.FAIL;
					handler.sendMessage(msg);
				}

			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}

		if (s.equals("submit")) {// 提交
			try {
				String gjbm = et_gjbm.getText().toString().trim();
				if("".equals(gjbm)){
					gjbm = "";
				}
				if("".equals(ewmStr)){
					ewmStr = "";
				}
				String csStr = zbh+"*"+gjbm+"*"+ewmStr;
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_YWGL_KDG_SMDWYZ", csStr, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {

					String typeStr = "smdy";
					msgStr = "定位成功！";
					String str = zbh + "*PAM*" + DataCache.getinition().getUserId();
					str += "*PAM*";
					str += tv_jd.getText().toString();
					str += "*PAM*";
					str += tv_wd.getText().toString();
					str += "*PAM*";
					str += tv_dz.getText().toString();

					JSONObject json = this.callWebserviceImp.getWebServerInfo(
							"c#_PAD_KDG_ALL", str, typeStr, typeStr,
							"uf_json_setdata2", this);
					flag = json.getString("flag");
					if (Integer.parseInt(flag) > 0) {
						Message msg = new Message();
						msg.what = Constant.SUCCESS;
						handler.sendMessage(msg);
					} else {
						flag = json.getString("msg");
						Message msg = new Message();
						msg.what = Constant.FAIL;
						handler.sendMessage(msg);
					}
				}else{
					flag = "柜机编码不匹配，请重新录入";
					Message msg = new Message();
					msg.what = Constant.NUM_8;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2 && resultCode == 2 && data != null) {
			// 二维码
			ewmStr = data.getStringExtra("result").trim();
			Message msg = new Message();
			msg.what = Constant.NUM_10;
			handler.sendMessage(msg);

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
				msg.what = Constant.NUM_7;// 成功
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

	@Override
	protected void onDestroy() {
		mLocClient.stop();
		super.onDestroy();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
				case Constant.FAIL:
					dialogShowMessage_P("失败，" + flag, null);
					break;
				case Constant.SUCCESS:
					dialogShowMessage_P(msgStr,new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface face,int paramAnonymous2Int) {
							onBackPressed();
						}
					});
					break;
				case Constant.NETWORK_ERROR:
					dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
					break;
				case Constant.NUM_6:
					SimpleAdapter adapter = new SimpleAdapter(SmdwKdgWx.this,
							data_gzbm, R.layout.spinner_item, from, to);
					spinner_gzdl.setAdapter(adapter);

					spinner_gzdl.setSelection(1);
					loadGzdl();

					break;
				case Constant.NUM_7:
					tv_time.setText(location.getTime());
					tv_jd.setText("" + location.getLongitude());
					tv_wd.setText("" + location.getLatitude());
					tv_dz.setText("" + location.getAddrStr());
					mLocClient.stop();
					mLocClient.unRegisterLocationListener(myListener);
					hasDw = true;
					break;
				case Constant.NUM_8:
					et_gjbm.setText("");
					tv_ewm.setText("");
					dialogShowMessage_P("失败，" + flag, null);
					break;
				case Constant.NUM_9:
					tv_jssx.setText(djsStr);
					if(iscs){
						tv_jssx.setTextColor(getResources().getColor(R.color.red));
					}
					break;
				case Constant.NUM_10:
					tv_ewm.setText(ewmStr);
					break;
			}

			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	};
//
//	@Override
//	public void onBackPressed() {
//		Intent intent = new Intent(this, MainActivity.class);
//		intent.putExtra("currType", 1);
//		startActivity(intent);
//		finish();
//	}

}