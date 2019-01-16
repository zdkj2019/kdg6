package com.kdg6.activity.kdg;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.kdg6.R;
import com.kdg6.activity.FrameActivity;
import com.kdg6.activity.main.MainActivity;
import com.kdg6.cache.DataCache;
import com.kdg6.cache.ServiceReportCache;
import com.kdg6.common.Constant;
import com.kdg6.utils.Config;
import com.kdg6.utils.DateUtil;

/**
 * 快递柜-上门定位
 *
 * @author zdkj
 *
 */
public class SmdwKdg extends FrameActivity {

	private TextView tv_time, tv_jd, tv_wd, tv_dz;
	private EditText et_zdh;
	private Button confirm, cancel;
	private String flag, zbh, message, ywlx2, zzdh,bzsj;
	private BDLocation location;
	private LocationClient mLocClient;
	private BDLocationListener myListener = new MyLocationListener();
	private boolean hasDw = false;
	private ArrayList<Map<String, String>> data;
	private LinearLayout ll_sbxx_content;

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
		confirm.setText("确定");
		cancel.setText("取消");
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_jd = (TextView) findViewById(R.id.tv_jd);
		tv_wd = (TextView) findViewById(R.id.tv_wd);
		tv_dz = (TextView) findViewById(R.id.tv_dz);
		et_zdh = (EditText) findViewById(R.id.et_zdh);
		ll_sbxx_content = (LinearLayout) findViewById(R.id.ll_sbxx_content);

		final Map<String, Object> itemmap = ServiceReportCache.getObjectdata()
				.get(ServiceReportCache.getIndex());

		zbh = itemmap.get("zbh").toString();
		ywlx2 = itemmap.get("ywlx2").toString();
		zzdh = itemmap.get("zdh").toString();
		bzsj = itemmap.get("bzsj").toString();
		// if (!"3".equals(ywlx2)) { // 安装才显示 设备信息
		// findViewById(R.id.ll_sbxx).setVisibility(View.GONE);
		// findViewById(R.id.ll_sbxx_content).setVisibility(View.GONE);
		//
		// }


		/*
		 * 此处需要注意：LocationClient类必须在主线程中声明。需要Context类型的参数。
		 * Context需要时全进程有效的context,推荐用getApplicationConext获取全进程有效的context
		 */
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
//					if(tv_jd.getText().toString().indexOf("4.9E")!=-1){
//						dialogShowMessage_P("定位失败，请到开阔地重试", null);
//						return;
//					}
						long now = new Date().getTime();
						long sj = DateUtil.StringToDate(bzsj).getTime()+15*60*1000;
						if(now<sj){
							toastShowMessage("时间未到，不能定位。");
							return;
						}
						for (int i = 0; i < ll_sbxx_content.getChildCount(); i++) {
							LinearLayout ll = (LinearLayout) ll_sbxx_content
									.getChildAt(i);
							if (ll.getChildAt(1) instanceof EditText) {
								EditText et = (EditText) ll.getChildAt(1);
								String tag = et.getTag().toString();
								if (!isNotNull(et)) {
									dialogShowMessage_P(tag + "不能为空，请录入", null);
									return;
								}
							} else if (ll.getChildAt(1) instanceof LinearLayout) {
								ll = (LinearLayout) ll.getChildAt(1);
								RadioGroup rg = (RadioGroup) ll.getChildAt(0);
								if (rg.getCheckedRadioButtonId() == -1) {
									dialogShowMessage_P("各项信息不能为空，请选择", null);
									return;
								}
							}
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
					default:
						break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);
	}

	@Override
	protected void getWebService(String s) {

		if (s.equals("query")) {// 提交
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SHGL_KDG_SMDW_AZD", zbh+"*"+zbh, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				data = new ArrayList<Map<String, String>>();
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("tzlmc", temp.getString("tzlmc"));
						item.put("kzzf1", temp.getString("kzzf1"));
						item.put("kzsz1", temp.getString("kzsz1"));
						item.put("str", temp.getString("str"));
						data.add(item);
					}

					Message msg = new Message();
					msg.what = Constant.NUM_6;
					handler.sendMessage(msg);

				} else {
					//flag = jsonObject.getString("msg");
					Message msg = new Message();
					msg.what = Constant.NUM_6;// 失败
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

				String cs = "";
				for (int i = 0; i < ll_sbxx_content.getChildCount(); i++) {
					LinearLayout ll = (LinearLayout) ll_sbxx_content
							.getChildAt(i);
					if (ll.getChildAt(1) instanceof LinearLayout) {
						ll = (LinearLayout) ll.getChildAt(1);
						RadioGroup rg = (RadioGroup) ll.getChildAt(0);
						RadioButton rb = (RadioButton) rg.findViewById(rg
								.getCheckedRadioButtonId());
						cs += rb.getText().toString();
						cs += "*PAM*";
					} else if (ll.getChildAt(1) instanceof EditText) {
						EditText et = (EditText) ll.getChildAt(1);
						cs += et.getText().toString();
						cs += "*PAM*";
					}

				}

				String typeStr = "smdy";
				message = "定位成功！";
				String str = zbh + "*PAM*" + DataCache.getinition().getUserId();
				str += "*PAM*";
				str += tv_jd.getText().toString();
				str += "*PAM*";
				str += tv_wd.getText().toString();
				str += "*PAM*";
				str += tv_dz.getText().toString();
				str += "*PAM*";
				str += cs;

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
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}
	}


	@SuppressLint("ResourceAsColor")
	protected void LoadSbxx(ArrayList<Map<String, String>> data,LinearLayout ll) {
		if (data.size() == 0) {
			ll_sbxx_content.setVisibility(View.GONE);
			findViewById(R.id.ll_sbxx).setVisibility(View.GONE);
			return;
		}
		try {
			for (int i = 0; i < data.size(); i++) {
				Map<String, String> map = data.get(i);
				String title = map.get("tzlmc");
				String type = map.get("kzzf1");
				String content = map.get("str");
				View view = null;
				if ("2".equals(type)) {// 输入
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.include_xj_text, null);
					TextView tv_name = (TextView) view
							.findViewById(R.id.tv_name);
					EditText et_val = (EditText) view.findViewById(R.id.et_val);
					et_val.setTag(title);
					et_val.setHint(title);
					et_val.setHintTextColor(R.color.gray);
					tv_name.setText(title);
				} else if ("1".equals(type)) {// 选择
					String[] contents = content.split(",");
					if (contents.length == 2) {
						view = LayoutInflater.from(getApplicationContext())
								.inflate(R.layout.include_xj_aqfx, null);
						TextView tv_name = (TextView) view
								.findViewById(R.id.tv_name);
						RadioButton rb_1 = (RadioButton) view
								.findViewById(R.id.rb_1);
						RadioButton rb_2 = (RadioButton) view
								.findViewById(R.id.rb_2);
						rb_1.setText(contents[0]);
						rb_2.setText(contents[1]);
						tv_name.setText(title);
					} else if (contents.length == 3) {
						view = LayoutInflater.from(getApplicationContext())
								.inflate(R.layout.include_xj_type_2, null);
						TextView tv_name = (TextView) view
								.findViewById(R.id.tv_name);
						RadioButton rb_1 = (RadioButton) view
								.findViewById(R.id.rb_1);
						RadioButton rb_2 = (RadioButton) view
								.findViewById(R.id.rb_2);
						RadioButton rb_3 = (RadioButton) view
								.findViewById(R.id.rb_3);
						rb_1.setText(contents[0]);
						rb_2.setText(contents[1]);
						rb_3.setText(contents[2]);
						tv_name.setText(title);
					}
				}else if ("3".equals(type)) {// 图片
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.include_xj_pz, null);
					TextView tv_name = (TextView) view
							.findViewById(R.id.tv_name);
					tv_name.setText(title);
					TextView tv_1 = (TextView) view.findViewById(R.id.tv_1);
					tv_1.setTag(i);
					tv_1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
//							tv_curr = (TextView) v;
//							camera();
						}
					});
				} else if ("4".equals(type)) { //日期
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.include_xj_text, null);
					TextView tv_name = (TextView) view
							.findViewById(R.id.tv_name);
					final EditText et_val = (EditText) view.findViewById(R.id.et_val);
					et_val.setFocusable(false);
					et_val.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							showDateSelector(et_val);
						}
					});
					tv_name.setText(title);
				}else if ("5".equals(type)) { //数值
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.include_xj_text, null);
					TextView tv_name = (TextView) view
							.findViewById(R.id.tv_name);
					EditText et_val = (EditText) view.findViewById(R.id.et_val);
					et_val.setInputType(InputType.TYPE_CLASS_NUMBER);
					et_val.setTag(title);
					et_val.setHint(title);
					et_val.setHintTextColor(R.color.gray);
					tv_name.setText(title);
				}

				ll.addView(view);
			}
		} catch (Exception e) {
			e.printStackTrace();
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

	@SuppressLint("ResourceAsColor")


	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
				case Constant.FAIL:
					dialogShowMessage_P("失败，请检查后重试...错误标识：" + flag, null);
					break;
				case Constant.SUCCESS:
					dialogShowMessage_P(message,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
													int paramAnonymous2Int) {
									Intent intent  = getIntent();
									setResult(-1, intent);
									finish();
								}
							});
					break;
				case Constant.NETWORK_ERROR:
					dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
					break;
				case Constant.NUM_6:
					LoadSbxx(data,ll_sbxx_content);
					break;
				case Constant.NUM_7:
					tv_time.setText(location.getTime());
					tv_jd.setText("" + location.getLongitude());
					tv_wd.setText("" + location.getLatitude());
					tv_dz.setText("" + location.getAddrStr());
					mLocClient.stop();
					hasDw = true;
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