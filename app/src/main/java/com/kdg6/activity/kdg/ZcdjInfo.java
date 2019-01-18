package com.kdg6.activity.kdg;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.kdg6.R;
import com.kdg6.activity.FrameActivity;
import com.kdg6.activity.esp.ChooseActivity;
import com.kdg6.activity.login.LoginActivity;
import com.kdg6.cache.DataCache;
import com.kdg6.cache.ServiceReportCache;
import com.kdg6.common.Constant;
import com.kdg6.utils.Config;
import com.kdg6.utils.ImageUtil;
import com.kdg6.zxing.CaptureActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * 新增设备信息
 *
 * @author Administrator 20170410
 */
@SuppressLint("HandlerLeak")
public class ZcdjInfo extends FrameActivity {

	private TextView tv_curr,tv_bhyy,tv_xqmc;
	private EditText et_sbid, et_xxdz,et_jddz,et_bz,et_gjbm,et_zgbm;
	private EditText et_fg1,et_fg2, et_fg3, et_fg4, et_fg5, et_fg6, et_fg7, et_fg8, et_fg9;
	private LinearLayout ll_fg_1, ll_fg_2, ll_fg_3, ll_fg_4, ll_fg_5, ll_fg_6,ll_fg_7, ll_fg_8, ll_fg_9;
	private Button confirm, cancel, btn_sm;
	private Button btn_zg,btn_fg1,btn_fg2,btn_fg3,btn_fg4,btn_fg5,btn_fg6,btn_fg7,btn_fg8,btn_fg9;
	private Spinner spinner_sf, spinner_ds, spinner_qx,spinner_sblx,spinner_fgsl;
	private ArrayList<Map<String, String>> data_sf, data_ds, data_qx,data_wdmc,data_sblx,data_zp,data_fg_choose;
	private Map<String, ArrayList<String>> filemap;
	private List<Map<String, String>> filelistFail;
	private String[] from;
	private int[] to;
	private SimpleAdapter adapter;
	private String sfbm, dsbm, qxbm, sbid, wdbm,hpbm,sbbm,sblxbm,msgStr = "",zbh="",zzbh="",type="2";
	private LinearLayout ll_show;
	private SharedPreferences spf;
	private BDLocation location;
	private LocationClient mLocClient;
	private BDLocationListener myListener = new MyLocationListener();
	private boolean hasDw = false;
	public static final String PATTERN = "^[0-9a-zA-Z]{6,15}$";
	public static final String PATTERN_EWM = "^[0][0-9]{7,15}$";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_zcdjinfo);
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
		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("提交");
		cancel.setText("返回");

		et_sbid = (EditText) findViewById(R.id.et_sbid);
		et_gjbm = (EditText) findViewById(R.id.et_gjbm);
		et_xxdz = (EditText) findViewById(R.id.et_xxdz);
		et_jddz = (EditText) findViewById(R.id.et_jddz);
		et_zgbm = (EditText) findViewById(R.id.et_zgbm);
		tv_xqmc = (TextView) findViewById(R.id.tv_xqmc);
		tv_bhyy = (TextView) findViewById(R.id.tv_bhyy);
		et_bz = (EditText) findViewById(R.id.et_bz);
		ll_show = (LinearLayout) findViewById(R.id.ll_show);
		spinner_fgsl = (Spinner) findViewById(R.id.spinner_fgsl);

		et_fg1 = (EditText) findViewById(R.id.et_fg1);
		et_fg2 = (EditText) findViewById(R.id.et_fg2);
		et_fg3 = (EditText) findViewById(R.id.et_fg3);
		et_fg4 = (EditText) findViewById(R.id.et_fg4);
		et_fg5 = (EditText) findViewById(R.id.et_fg5);
		et_fg6 = (EditText) findViewById(R.id.et_fg6);
		et_fg7 = (EditText) findViewById(R.id.et_fg7);
		et_fg8 = (EditText) findViewById(R.id.et_fg8);
		et_fg9 = (EditText) findViewById(R.id.et_fg9);

		ll_fg_1 = (LinearLayout) findViewById(R.id.ll_fg_1);
		ll_fg_2 = (LinearLayout) findViewById(R.id.ll_fg_2);
		ll_fg_3 = (LinearLayout) findViewById(R.id.ll_fg_3);
		ll_fg_4 = (LinearLayout) findViewById(R.id.ll_fg_4);
		ll_fg_5 = (LinearLayout) findViewById(R.id.ll_fg_5);
		ll_fg_6 = (LinearLayout) findViewById(R.id.ll_fg_6);
		ll_fg_7 = (LinearLayout) findViewById(R.id.ll_fg_7);
		ll_fg_8 = (LinearLayout) findViewById(R.id.ll_fg_8);
		ll_fg_9 = (LinearLayout) findViewById(R.id.ll_fg_9);

		btn_zg = (Button) findViewById(R.id.btn_zg);
		btn_fg1 = (Button) findViewById(R.id.btn_fg1);
		btn_fg2 = (Button) findViewById(R.id.btn_fg2);
		btn_fg3 = (Button) findViewById(R.id.btn_fg3);
		btn_fg4 = (Button) findViewById(R.id.btn_fg4);
		btn_fg5 = (Button) findViewById(R.id.btn_fg5);
		btn_fg6 = (Button) findViewById(R.id.btn_fg6);
		btn_fg7 = (Button) findViewById(R.id.btn_fg7);
		btn_fg8 = (Button) findViewById(R.id.btn_fg8);
		btn_fg9 = (Button) findViewById(R.id.btn_fg9);

		confirm = (Button) findViewById(R.id.confirm);
		cancel = (Button) findViewById(R.id.cancel);

		spinner_sf = (Spinner) findViewById(R.id.spinner_sf);
		spinner_ds = (Spinner) findViewById(R.id.spinner_ds);
		spinner_qx = (Spinner) findViewById(R.id.spinner_qx);
		spinner_sblx = (Spinner) findViewById(R.id.spinner_sblx);
		btn_sm = (Button) findViewById(R.id.btn_sm);

		from = new String[] { "id", "name" };
		to = new int[] { R.id.bm, R.id.name };

		filemap = new HashMap<String, ArrayList<String>>();
		data_fg_choose = new ArrayList<Map<String, String>>();

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map item = new HashMap<String, String>();
		item.put("id", "");
		item.put("name", "一拖1");
		list.add(item);
		item = new HashMap<String, String>();
		item.put("id", "");
		item.put("name", "一拖2");
		list.add(item);
		item = new HashMap<String, String>();
		item.put("id", "");
		item.put("name", "一拖3");
		list.add(item);
		item = new HashMap<String, String>();
		item.put("id", "");
		item.put("name", "一拖4");
		list.add(item);
		item = new HashMap<String, String>();
		item.put("id", "");
		item.put("name", "一拖5");
		list.add(item);
		item = new HashMap<String, String>();
		item.put("id", "");
		item.put("name", "一拖6");
		list.add(item);
		item = new HashMap<String, String>();
		item.put("id", "");
		item.put("name", "一拖7");
		list.add(item);
		item = new HashMap<String, String>();
		item.put("id", "");
		item.put("name", "一拖8");
		list.add(item);
		item = new HashMap<String, String>();
		item.put("id", "");
		item.put("name", "一拖9");
		list.add(item);
		adapter = new SimpleAdapter(ZcdjInfo.this, list,
				R.layout.spinner_item, from, to);
		spinner_fgsl.setAdapter(adapter);
	}

	@SuppressLint("ResourceAsColor")
	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		type = getIntent().getStringExtra("type");
		if("2".equals(type)||"4".equals(type)){
			final Map<String, Object> itemmap = ServiceReportCache.getObjectdata().get(ServiceReportCache.getIndex());
			try {
				sbid = itemmap.get("kzzf1").toString();
			} catch (Exception e) {

			}
			if(sbid==null||"".equals(sbid)){

			}else{
				btn_sm.setEnabled(false);
				btn_sm.setBackgroundDrawable(null);
			}
			zzbh = itemmap.get("zzbh").toString();
			hpbm = itemmap.get("hpbm").toString();
			sbbm = itemmap.get("sbbm").toString();
			sfbm = itemmap.get("sf").toString();
			dsbm = itemmap.get("ds").toString();
			qxbm = itemmap.get("qx").toString();
			wdbm = itemmap.get("wdbmwd").toString();
			sblxbm = itemmap.get("sblx").toString();
			et_sbid.setText(sbid);
			et_gjbm.setText(sbbm);
			et_xxdz.setText(itemmap.get("xxdz").toString());
			et_jddz.setText(itemmap.get("jddz").toString());
			et_bz.setText(itemmap.get("bz").toString());


			try {
				spinner_fgsl.setSelection(Integer.parseInt(itemmap.get("kzsz4").toString())-1);
				et_zgbm.setText(itemmap.get("zgbm").toString());
				String fgbm = itemmap.get("fgbm").toString();
				String[] fgbms = fgbm.split(",");
				for(int i=0;i<fgbms.length;i++){
					if (i == 0) {
						et_fg1.setText(fgbms[0]);
					}
					if (i == 1) {
						et_fg2.setText(fgbms[1]);
					}
					if (i == 2) {
						et_fg3.setText(fgbms[2]);
					}
					if (i == 3) {
						et_fg4.setText(fgbms[3]);
					}
					if (i == 4) {
						et_fg5.setText(fgbms[4]);
					}
					if (i == 5) {
						et_fg6.setText(fgbms[5]);
					}
					if (i == 6) {
						et_fg7.setText(fgbms[6]);
					}
					if (i == 7) {
						et_fg8.setText(fgbms[7]);
					}
					if (i == 8) {
						et_fg9.setText(fgbms[7]);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				msgStr = "主副柜数据错误";
				Message msg = new Message();
				msg.what = Constant.FAIL;
				handler.sendMessage(msg);
			}
			boolean f = true;
			if("4".equals(type)){
				if("3".equals(itemmap.get("pgbm").toString())){
					f = false;
				}
				tv_bhyy.setText(itemmap.get("fwnr").toString());
				findViewById(R.id.ll_bhyy).setVisibility(View.VISIBLE);
				findViewById(R.id.ll_bhyy_line).setVisibility(View.VISIBLE);
			}
			if(f){
				et_gjbm.setEnabled(false);
				et_gjbm.setTextColor(R.color.gray);
			}
		}else{
			try {
				spf = getSharedPreferences("zcdj", ZcdjQuery.MODE_PRIVATE);
				String jsonStr = spf.getString("ssx", "");
				if(!"".equals(jsonStr)){
					JSONObject json = new JSONObject(jsonStr);
					sfbm = json.getString("sfbm");
					dsbm = json.getString("dsbm");
					qxbm = json.getString("qxbm");
				}
				wdbm = getIntent().getStringExtra("wdbm");
			} catch (Exception e) {

			}
		}
		if("2".equals(type)){
			cancel.setText("无此设备");

		}

		mLocClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocClient.registerLocationListener(myListener); // 注册监听函数

		setLocationClientOption();

//		sbid = "128881";
//		et_sbid.setText(sbid);
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
					case R.id.cancel:
						if("2".equals(type)){
							dialogShowMessage("是否确认该网点无此设备？",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
													int paramAnonymous2Int) {
									type="3";
									showProgressDialog();
									Config.getExecutorService().execute(new Runnable() {

										@Override
										public void run() {

											getWebService("submit");
										}
									});
								}
							},null);

						}else{
							onBackPressed();
						}

						break;
					case R.id.confirm:
						if(checkSubmit()){
							showProgressDialog();
							Config.getExecutorService().execute(new Runnable() {

								@Override
								public void run() {

									getWebService("submit");
								}
							});
						}
						break;
					case R.id.btn_sm:
						startSm(2);
						break;
					case R.id.btn_zg:
						startSm(30);
						break;
					case R.id.btn_fg1:
						startSm(31);
						break;
					case R.id.btn_fg2:
						startSm(32);
						break;
					case R.id.btn_fg3:
						startSm(33);
						break;
					case R.id.btn_fg4:
						startSm(34);
						break;
					case R.id.btn_fg5:
						startSm(35);
						break;
					case R.id.btn_fg6:
						startSm(36);
						break;
					case R.id.btn_fg7:
						startSm(37);
						break;
					case R.id.btn_fg8:
						startSm(38);
						break;
					case R.id.btn_fg9:
						startSm(39);
						break;
				}
			}
		};

		topBack.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);
		confirm.setOnClickListener(onClickListener);
		btn_sm.setOnClickListener(onClickListener);
		btn_zg.setOnClickListener(onClickListener);
		btn_fg1.setOnClickListener(onClickListener);
		btn_fg2.setOnClickListener(onClickListener);
		btn_fg3.setOnClickListener(onClickListener);
		btn_fg4.setOnClickListener(onClickListener);
		btn_fg5.setOnClickListener(onClickListener);
		btn_fg6.setOnClickListener(onClickListener);
		btn_fg7.setOnClickListener(onClickListener);
		btn_fg8.setOnClickListener(onClickListener);
		btn_fg9.setOnClickListener(onClickListener);

		spinner_fgsl.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				hideAll();
				data_fg_choose.clear();
				int num = spinner_fgsl.getSelectedItemPosition()+1;
				if (num > 0 && num < 10) {
					for (int i = 0; i < num; i++) {
						if (i == 0) {
							ll_fg_1.setVisibility(View.VISIBLE);
							findViewById(R.id.v_1).setVisibility(View.VISIBLE);
						}
						if (i == 1) {
							ll_fg_2.setVisibility(View.VISIBLE);
							findViewById(R.id.v_2).setVisibility(View.VISIBLE);
						}
						if (i == 2) {
							ll_fg_3.setVisibility(View.VISIBLE);
							findViewById(R.id.v_3).setVisibility(View.VISIBLE);
						}
						if (i == 3) {
							ll_fg_4.setVisibility(View.VISIBLE);
							findViewById(R.id.v_4).setVisibility(View.VISIBLE);
						}
						if (i == 4) {
							ll_fg_5.setVisibility(View.VISIBLE);
							findViewById(R.id.v_5).setVisibility(View.VISIBLE);
						}
						if (i == 5) {
							ll_fg_6.setVisibility(View.VISIBLE);
							findViewById(R.id.v_6).setVisibility(View.VISIBLE);
						}
						if (i == 6) {
							ll_fg_7.setVisibility(View.VISIBLE);
							findViewById(R.id.v_7).setVisibility(View.VISIBLE);
						}
						if (i == 7) {
							ll_fg_8.setVisibility(View.VISIBLE);
							findViewById(R.id.v_8).setVisibility(View.VISIBLE);
						}
						if (i == 8) {
							ll_fg_9.setVisibility(View.VISIBLE);
							findViewById(R.id.v_9).setVisibility(View.VISIBLE);
						}
					}
				} else {
					toastShowMessage("副柜安装数为1-9之间的数字！");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

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

		tv_xqmc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),ChooseActivity.class);
				intent.putExtra("data", data_wdmc);
				startActivityForResult(intent, 3);
			}
		});

//		et_gjbm.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if(!hasFocus){
//					if(!"".equals(sbid)){
//						showProgressDialog();
//						Config.getExecutorService().execute(new Runnable() {
//
//							@Override
//							public void run() {
//
//								getWebService("checkewm");
//							}
//						});
//					}
//
//				}
//
//			}
//		});

	}

	private boolean checkSubmit(){
		if (!isNotNull(et_sbid)) {
			toastShowMessage("请扫描二维码");
			return false;
		}
		sblxbm = data_sblx.get(spinner_sblx.getSelectedItemPosition())
				.get("id");
		if ("".equals(sblxbm)) {
			toastShowMessage("请选择设备类型");
			return false;
		}

		sfbm = data_sf.get(spinner_sf.getSelectedItemPosition())
				.get("id");
		if ("".equals(sfbm)) {
			toastShowMessage("请选择省份");
			return false;
		}
		dsbm = data_ds.get(spinner_ds.getSelectedItemPosition())
				.get("id");
		if ("".equals(dsbm)) {
			toastShowMessage("请选择地市");
			return false;
		}
		qxbm = data_qx.get(spinner_qx.getSelectedItemPosition())
				.get("id");
		if ("".equals(qxbm)) {
			toastShowMessage("请选择区县");
			return false;
		}
		wdbm = (String) tv_xqmc.getTag();
		if ("".equals(wdbm)) {
			toastShowMessage("请选择小区名称");
			return false;
		}
		if (!isNotNull(et_jddz)) {
			toastShowMessage("请录入街道地址");
			return false;
		}
		if (!isNotNull(et_xxdz)) {
			toastShowMessage("请录入投放地址");
			return false;
		}

		if(isNotNull(et_zgbm)){
			if(!Pattern.matches(PATTERN, et_zgbm.getText().toString().trim())){
				toastShowMessage("主柜编码必须是由数字或者字母组成的6位以上字符串");
				return false;
			}
		}
		if(isNotNull(et_fg1)){
			if(!Pattern.matches(PATTERN, et_fg1.getText().toString().trim())){
				toastShowMessage("副柜1编码必须是由数字或者字母组成的6位以上字符串");
				return false;
			}
		}
		if(isNotNull(et_fg2)){
			if(!Pattern.matches(PATTERN, et_fg2.getText().toString().trim())){
				toastShowMessage("副柜2编码必须是由数字或者字母组成的6位以上字符串");
				return false;
			}
		}
		if(isNotNull(et_fg3)){
			if(!Pattern.matches(PATTERN, et_fg3.getText().toString().trim())){
				toastShowMessage("副柜3编码必须是由数字或者字母组成的6位以上字符串");
				return false;
			}
		}
		if(isNotNull(et_fg4)){
			if(!Pattern.matches(PATTERN, et_fg4.getText().toString().trim())){
				toastShowMessage("副柜4编码必须是由数字或者字母组成的6位以上字符串");
				return false;
			}
		}
		if(isNotNull(et_fg5)){
			if(!Pattern.matches(PATTERN, et_fg5.getText().toString().trim())){
				toastShowMessage("副柜5编码必须是由数字或者字母组成的6位以上字符串");
				return false;
			}
		}
		if(isNotNull(et_fg6)){
			if(!Pattern.matches(PATTERN, et_fg6.getText().toString().trim())){
				toastShowMessage("副柜6编码必须是由数字或者字母组成的6位以上字符串");
				return false;
			}
		}
		if(isNotNull(et_fg7)){
			if(!Pattern.matches(PATTERN, et_fg7.getText().toString().trim())){
				toastShowMessage("副柜7编码必须是由数字或者字母组成的6位以上字符串");
				return false;
			}
		}
		if(isNotNull(et_fg8)){
			if(!Pattern.matches(PATTERN, et_fg8.getText().toString().trim())){
				toastShowMessage("副柜8编码必须是由数字或者字母组成的6位以上字符串");
				return false;
			}
		}
		if(isNotNull(et_fg9)){
			if(!Pattern.matches(PATTERN, et_fg9.getText().toString().trim())){
				toastShowMessage("副柜9编码必须是由数字或者字母组成的6位以上字符串");
				return false;
			}
		}


		for (int i = 0; i < ll_show.getChildCount(); i++) {
			LinearLayout ll = (LinearLayout) ll_show
					.getChildAt(i);
			if (ll.getChildAt(1) instanceof LinearLayout) {
				ll = (LinearLayout) ll.getChildAt(1);
				if (ll.getChildAt(0) instanceof RadioGroup) {
					RadioGroup rg = (RadioGroup) ll.getChildAt(0);
					RadioButton rb = (RadioButton) rg.findViewById(rg.getCheckedRadioButtonId());
					if (rb == null) {
						toastShowMessage("巡检内容中选择项不能为空！");
						return false;
					}
				}
			}

		}
//		if(filemap.size() ==0){
//			toastShowMessage("请选择图片！");
//			return false;
//		}
		return true;
	}

	private void startSm(int num) {
		// 二维码
		Intent intent = new Intent(getApplicationContext(),
				CaptureActivity.class);
		startActivityForResult(intent, num);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			ArrayList<String> list = data.getStringArrayListExtra("imglist");
			loadImg(list);
		}
		if (requestCode == 3 && resultCode == 1) {
			ArrayList<Map<String, String>> list = (ArrayList<Map<String, String>>) data.getSerializableExtra("data");
			Map<String, String> map = list.get(0);
			tv_xqmc.setTag(map.get("id"));
			tv_xqmc.setText(map.get("name"));
			et_jddz.setText(map.get("xxdz"));
		}
		if (requestCode == 2 && resultCode == 2 && data != null) {
			// 二维码
			sbid = data.getStringExtra("result").trim();
			Message msg = new Message();
			msg.what = Constant.NUM_11;
			handler.sendMessage(msg);
//			if(isNotNull(et_gjbm)){
//				showProgressDialog();
//				Config.getExecutorService().execute(new Runnable() {
//
//					@Override
//					public void run() {
//
//						getWebService("checkewm");
//					}
//				});
//			}

		}
		if (requestCode == 30 && resultCode == 2 && data != null) {
			String bmStr = data.getStringExtra("result").trim();
			et_zgbm.setText(bmStr);
		}
		if (requestCode == 31 && resultCode == 2 && data != null) {
			String bmStr = data.getStringExtra("result").trim();
			et_fg1.setText(bmStr);
		}
		if (requestCode == 32 && resultCode == 2 && data != null) {
			String bmStr = data.getStringExtra("result").trim();
			et_fg2.setText(bmStr);
		}
		if (requestCode == 33 && resultCode == 2 && data != null) {
			String bmStr = data.getStringExtra("result").trim();
			et_fg3.setText(bmStr);
		}
		if (requestCode == 34 && resultCode == 2 && data != null) {
			String bmStr = data.getStringExtra("result").trim();
			et_fg4.setText(bmStr);
		}
		if (requestCode == 35 && resultCode == 2 && data != null) {
			String bmStr = data.getStringExtra("result").trim();
			et_fg5.setText(bmStr);
		}
		if (requestCode == 36 && resultCode == 2 && data != null) {
			String bmStr = data.getStringExtra("result").trim();
			et_fg6.setText(bmStr);
		}
		if (requestCode == 37 && resultCode == 2 && data != null) {
			String bmStr = data.getStringExtra("result").trim();
			et_fg7.setText(bmStr);
		}
		if (requestCode == 38 && resultCode == 2 && data != null) {
			String bmStr = data.getStringExtra("result").trim();
			et_fg8.setText(bmStr);
		}
		if (requestCode == 39 && resultCode == 2 && data != null) {
			String bmStr = data.getStringExtra("result").trim();
			et_fg9.setText(bmStr);
		}

		if (requestCode == 7 && resultCode == 1) {
			onBackPressed();
		}
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

					String nrsqlid = "_PAD_ZCGL_SB_SCXJNR";
					String nrcs = "";
					if("4".equals(type)){
						nrsqlid = "_PAD_ZCGL_SB_SCXJNR_2";
						nrcs = zzbh+"*"+zzbh;
					}
					jsonObject = callWebserviceImp.getWebServerInfo(
							nrsqlid, nrcs, "uf_json_getdata", this);
					flag = jsonObject.getString("flag");
					data_zp = new ArrayList<Map<String, String>>();
					if (Integer.parseInt(flag) > 0) {
						jsonArray = jsonObject.getJSONArray("tableA");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject temp = jsonArray.getJSONObject(i);
							item = new HashMap<String, String>();
							item.put("tzlmc", temp.getString("tzlmc"));
							item.put("kzzf1", temp.getString("kzzf1"));
							item.put("str", temp.getString("str"));
							item.put("mxh", temp.getString("mxh"));
							item.put("tzz", temp.getString("tzz"));
							item.put("path", temp.getString("path"));
							data_zp.add(item);
						}
					}

					data_sblx = new ArrayList<Map<String, String>>();
					item = new HashMap<String, String>();
					item.put("id", "");
					item.put("name", "");
					data_sblx.add(item);
					jsonObject = callWebserviceImp.getWebServerInfo(
							"_PAD_NT_SBLX", "", "uf_json_getdata", this);
					flag = jsonObject.getString("flag");
					if (Integer.parseInt(flag) > 0) {
						jsonArray = jsonObject.getJSONArray("tableA");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject temp = jsonArray.getJSONObject(i);
							item = new HashMap<String, String>();
							item.put("id", temp.getString("ccd"));
							item.put("name", temp.getString("ccdnm"));
							data_sblx.add(item);
						}
					} else {
						msgStr = "获取设备类型失败";
						Message msg = new Message();
						msg.what = Constant.FAIL;
						handler.sendMessage(msg);
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
				if(data_wdmc!=null){
					wdbm="";
				}
				data_wdmc = new ArrayList<Map<String, String>>();
				Map<String, String> item = new HashMap<String, String>();
				item.put("id", "");
				item.put("name", "");
				data_wdmc.add(item);
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBGL_SBLR_WDXX", qxbm, "uf_json_getdata", this);
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

		if ("checkewm".equals(s)) {
			try {
				String gjbm = et_gjbm.getText().toString();
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_YWCX_SBEWM_YC", sbid+"*"+gjbm+"*"+zzbh, "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					JSONObject temp = jsonArray.getJSONObject(0);
					if("n".equals(temp.get("sfhebz").toString())){
						sbid="";
					}
					Message msg = new Message();
					msg.what = Constant.NUM_11;
					handler.sendMessage(msg);
				}else{
					msgStr = "查询出错";
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

		if ("submit".equals(s)) {

			try {
				String gjbm = et_gjbm.getText().toString();
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_YWCX_SBEWM_YC", sbid+"*"+gjbm+"*"+zzbh, "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					JSONObject temp = jsonArray.getJSONObject(0);
					if("y".equals(temp.get("sfhebz").toString())){
						String cs = "";
						for (int i = 0; i < ll_show.getChildCount(); i++) {
							LinearLayout ll = (LinearLayout) ll_show.getChildAt(i);
							if (ll.getChildAt(1) instanceof LinearLayout) {
								ll = (LinearLayout) ll.getChildAt(1);
								if (ll.getChildAt(0) instanceof RadioGroup) {
									RadioGroup rg = (RadioGroup) ll.getChildAt(0);
									RadioButton rb = (RadioButton) rg.findViewById(rg
											.getCheckedRadioButtonId());
									if (rb == null) {
										cs += "#@##@#0";
									} else {
										cs += rb.getTag().toString() +"#@#" + rb.getText().toString();
									}
									cs += "#^#";
								} else if (ll.getChildAt(0) instanceof EditText) {
									EditText et = (EditText) ll.getChildAt(0);
									cs += et.getTag().toString()+ "#@#" + et.getText().toString();
									cs += "#^#";
								}
							}

						}
						if (!"".equals(cs)) {
							cs = cs.substring(0, cs.length() - 3);
						}

						String str = "";
						String sqlid = "c#_PAD_KDG_SBXXLY3";
						String zgStr = et_zgbm.getText().toString();
						String fgStr = "";
						for (int i = 0; i < spinner_fgsl.getSelectedItemPosition() + 1; i++) {
							if (i == 0) {
								if(isNotNull(et_fg1)){
									fgStr += et_fg1.getText().toString().trim()+",";
								}
							}
							if (i == 1) {
								if(isNotNull(et_fg2)){
									fgStr += et_fg2.getText().toString().trim()+",";
								}
							}
							if (i == 2) {
								if(isNotNull(et_fg3)){
									fgStr += et_fg3.getText().toString().trim()+",";
								}
							}
							if (i == 3) {
								if(isNotNull(et_fg4)){
									fgStr += et_fg4.getText().toString().trim()+",";
								}
							}
							if (i == 4) {
								if(isNotNull(et_fg5)){
									fgStr += et_fg5.getText().toString().trim()+",";
								}
							}
							if (i == 5) {
								if(isNotNull(et_fg6)){
									fgStr += et_fg6.getText().toString().trim()+",";
								}
							}
							if (i == 6) {
								if(isNotNull(et_fg7)){
									fgStr += et_fg7.getText().toString().trim()+",";
								}
							}
							if (i == 7) {
								if(isNotNull(et_fg8)){
									fgStr += et_fg8.getText().toString().trim()+",";
								}
							}
							if (i == 8) {
								if(isNotNull(et_fg9)){
									fgStr += et_fg9.getText().toString().trim()+",";
								}
							}
						}
						if (!"".equals(fgStr)) {
							fgStr = fgStr.substring(0, fgStr.length() - 1);
						}

						if("1".equals(type)){
							str += et_gjbm.getText().toString() + "*PAM*";
							str += et_sbid.getText().toString() + "*PAM*";
							str += getUserId() + "*PAM*";
							str += sblxbm+ "*PAM*";
							str += qxbm + "*PAM*";
							str += wdbm + "*PAM*";
							str += et_jddz.getText().toString().trim() + "*PAM*";;
							str += et_xxdz.getText().toString().trim() + "*PAM*";
							str += (spinner_fgsl.getSelectedItemPosition()+1) + "*PAM*";
							str += et_bz.getText().toString().trim() + "*PAM*";
							str += location.getLongitude() + "*PAM*";
							str += location.getLatitude()+ "*PAM*";
							str += zgStr + "*PAM*";
							str += fgStr + "*PAM*";
							str += cs;

						}else if("2".equals(type)){
							sqlid = "c#_PAD_KDG_SBXXLY";
							str += hpbm + "*PAM*";
							str += et_gjbm.getText().toString() + "*PAM*";
							str += et_sbid.getText().toString() + "*PAM*";
							str += getUserId() + "*PAM*";
							str += sblxbm+ "*PAM*";
							str += qxbm + "*PAM*";
							str += wdbm + "*PAM*";
							str += et_jddz.getText().toString().trim() + "*PAM*";;
							str += et_xxdz.getText().toString().trim() + "*PAM*";
							str += (spinner_fgsl.getSelectedItemPosition()+1) + "*PAM*";
							str += et_bz.getText().toString().trim() + "*PAM*";
							str += location.getLongitude() + "*PAM*";
							str += location.getLatitude()+ "*PAM*";
							str += zgStr + "*PAM*";
							str += fgStr + "*PAM*";
							str += cs;
						}else if("3".equals(type)){
							sqlid = "c#_PAD_KDG_SBXXLY2";
							str += hpbm + "*PAM*";
							str += et_gjbm.getText().toString() + "*PAM*";
							str += et_sbid.getText().toString() + "*PAM*";
							str += getUserId() + "*PAM*";
							str += sblxbm+ "*PAM*";
							str += qxbm + "*PAM*";
							str += wdbm + "*PAM*";
							str += et_jddz.getText().toString().trim() + "*PAM*";;
							str += et_xxdz.getText().toString().trim() + "*PAM*";
							str += (spinner_fgsl.getSelectedItemPosition()+1) + "*PAM*";
							str += et_bz.getText().toString().trim() + "*PAM*";
							str += location.getLongitude() + "*PAM*";
							str += location.getLatitude()+ "*PAM*";
							str += zgStr + "*PAM*";
							str += fgStr + "*PAM*";
							str += cs;
						}else if("4".equals(type)){
							sqlid = "c#_PAD_KDG_SBXXLY4";
							str += zzbh + "*PAM*";
							str += et_gjbm.getText().toString() + "*PAM*";
							str += et_sbid.getText().toString() + "*PAM*";
							str += getUserId() + "*PAM*";
							str += sblxbm+ "*PAM*";
							str += qxbm + "*PAM*";
							str += wdbm + "*PAM*";
							str += et_jddz.getText().toString().trim() + "*PAM*";;
							str += et_xxdz.getText().toString().trim() + "*PAM*";
							str += (spinner_fgsl.getSelectedItemPosition()+1) + "*PAM*";
							str += et_bz.getText().toString().trim() + "*PAM*";
							str += location.getLongitude() + "*PAM*";
							str += location.getLatitude()+ "*PAM*";
							str += zgStr + "*PAM*";
							str += fgStr + "*PAM*";
							str += cs;
						}


						JSONObject json = this.callWebserviceImp.getWebServerInfo(
								sqlid, str, "", "", "uf_json_setdata2",
								this);
						flag = json.getString("flag");
						if (Integer.parseInt(flag) > 0) {
							zbh = json.getString("zbh");
							if (filemap.size() >0) {
								upload();
							} else {
								Message msg = new Message();
								msg.what = Constant.SUCCESS;
								handler.sendMessage(msg);
							}
						} else {
							msgStr = json.getString("msg");
							Message msg = new Message();
							msg.what = Constant.FAIL;
							handler.sendMessage(msg);
						}
					}else{
						sbid="";
						Message msg = new Message();
						msg.what = Constant.NUM_11;
						handler.sendMessage(msg);
					}
				}else{
					msgStr = "查询出错";
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

	private void upload() {
		try {
			boolean flag = true;
			List<Map<String, String>> filelist = new ArrayList<Map<String, String>>();
			for (String mxh : filemap.keySet()) {
				List<String> filepathlist = filemap.get(mxh);
				for (int j = 0; j < filepathlist.size(); j++) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("mxh", mxh);
					map.put("num", j + "");
					String path = filepathlist.get(j);
					map.put("filepath", path);
					if(path.indexOf(Constant.ImgPath)==-1){
						filelist.add(map);
					}
				}
			}
			int filenum = filelist.size();
			filelistFail = new ArrayList<Map<String,String>>();
			for (int i = 0; i < filenum; i++) {
				Map<String, String> map = filelist.get(i);
				String mxh = map.get("mxh");
				String filepath = map.get("filepath");
				String num = map.get("num");
				filepath = filepath.substring(7, filepath.length());
				// 压缩图片到100K
				filepath = ImageUtil.compressAndGenImage(convertBitmap(new File(filepath),getScreenWidth()), 200, "jpg");
				File file = new File(filepath);
				// toastShowMessage("开始上传第" + (i + 1) + "/" + filenum +
				// "张图片");
				boolean f = false;
				try {
					f = uploadPic(num, mxh, readJpeg(file),"uf_json_setdata", zbh, "c#_PAD_KDG_XJ_GDCZP");
					file.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}

				if(!f){
					flag = false;
					filelistFail.add(map);
				}

			}
			if (flag) {
				Message msg = new Message();
				msg.what = Constant.NUM_12;
				handler.sendMessage(msg);
			} else {
				Message msg = new Message();
				msg.what = Constant.NUM_13;
				handler.sendMessage(msg);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = Constant.NUM_13;
			handler.sendMessage(msg);
		}

	}

	private void uploadFail() {
		try {
			boolean flag = true;
			List<Map<String, String>> filelist = filelistFail;
			int filenum = filelist.size();
			filelistFail = new ArrayList<Map<String,String>>();
			for (int i = 0; i < filenum; i++) {
				Map<String, String> map = filelist.get(i);
				String mxh = map.get("mxh");
				String filepath = map.get("filepath");
				String num = map.get("num");
				filepath = filepath.substring(7, filepath.length());
				// 压缩图片到100K
				filepath = ImageUtil.compressAndGenImage(convertBitmap(new File(filepath),getScreenWidth()), 200, "jpg");
				File file = new File(filepath);
				// toastShowMessage("开始上传第" + (i + 1) + "/" + filenum +
				// "张图片");
				boolean f = false;
				try {
					f = uploadPic(num, mxh, readJpeg(file),"uf_json_setdata", zbh, "c#_PAD_KDG_XJ_GDCZP");
					file.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(!f){
					flag = false;
					filelistFail.add(map);
				}
			}
			if (flag) {
				Message msg = new Message();
				msg.what = Constant.NUM_12;
				handler.sendMessage(msg);
			} else {
				Message msg = new Message();
				msg.what = Constant.NUM_13;
				handler.sendMessage(msg);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = Constant.NUM_13;
			handler.sendMessage(msg);
		}

	}

	private void hideAll() {
		ll_fg_1.setVisibility(View.GONE);
		findViewById(R.id.v_1).setVisibility(View.GONE);
		ll_fg_2.setVisibility(View.GONE);
		findViewById(R.id.v_2).setVisibility(View.GONE);
		ll_fg_3.setVisibility(View.GONE);
		findViewById(R.id.v_3).setVisibility(View.GONE);
		ll_fg_4.setVisibility(View.GONE);
		findViewById(R.id.v_4).setVisibility(View.GONE);
		ll_fg_5.setVisibility(View.GONE);
		findViewById(R.id.v_5).setVisibility(View.GONE);
		ll_fg_6.setVisibility(View.GONE);
		findViewById(R.id.v_6).setVisibility(View.GONE);
		ll_fg_7.setVisibility(View.GONE);
		findViewById(R.id.v_7).setVisibility(View.GONE);
		ll_fg_8.setVisibility(View.GONE);
		findViewById(R.id.v_8).setVisibility(View.GONE);
		ll_fg_9.setVisibility(View.GONE);
		findViewById(R.id.v_9).setVisibility(View.GONE);

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

	@SuppressLint("ResourceAsColor")
	protected void loadSbxx(ArrayList<Map<String, String>> data, LinearLayout ll) {
		String errormsg = "";
		try {
			for (int i = 0; i < data.size(); i++) {
				Map<String, String> map = data.get(i);
				String title = map.get("tzlmc");
				errormsg = title;
				String type = map.get("kzzf1");
				String content = map.get("str");
				String tzz = map.get("tzz") == null ? "" : map.get("tzz")
						.toString();
				//String kzzf4 = map.get("kzzf4");
				View view = null;
				if ("2".equals(type)) {// 输入
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.include_xj_text, null);
					TextView tv_name = (TextView) view
							.findViewById(R.id.tv_name);
					EditText et_val = (EditText) view.findViewById(R.id.et_val);
					et_val.setText(tzz);
					et_val.setTag(map.get("mxh"));
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
						rb_1.setTag(map.get("mxh"));
						rb_2.setTag(map.get("mxh"));
						if (tzz.equals(contents[0])) {
							rb_1.setChecked(true);
						} else if (tzz.equals(contents[1])) {
							rb_2.setChecked(true);
						}
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
						rb_1.setTag(map.get("mxh"));
						rb_2.setTag(map.get("mxh"));
						rb_3.setTag(map.get("mxh"));
						if (tzz.equals(contents[0])) {
							rb_1.setChecked(true);
						} else if (tzz.equals(contents[1])) {
							rb_2.setChecked(true);
						} else if (tzz.equals(contents[2])) {
							rb_3.setChecked(true);
						}
						tv_name.setText(title);

					}

				} else if ("3".equals(type)) {// 图片
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.include_xj_pz, null);
					TextView tv_name = (TextView) view
							.findViewById(R.id.tv_name);
					tv_name.setText(title);
					TextView tv_1 = (TextView) view.findViewById(R.id.tv_1);
					final String mxh = map.get("mxh");
					tv_1.setTag(mxh);
					String path = map.get("path");
					try {
						if(!"".equals(path)){
							String[] paths = path.split(",");
							ArrayList<String> arraylist = new ArrayList<String>();
							for(int n=0;n<paths.length;n++){
								arraylist.add(Constant.ImgPath+"/"+tzz+"/"+paths[n]);
							}
							filemap.put(mxh, arraylist);
							tv_1.setText("继续选择");
							tv_1.setBackgroundResource(R.drawable.btn_normal_yellow);
						}
					} catch (Exception e) {

					}
					tv_1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							tv_curr = (TextView) v;
							ArrayList<String> list = filemap.get(mxh);
							camera(1, list);
						}
					});

				} else if ("4".equals(type)) { // 日期
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.include_xj_text, null);
					TextView tv_name = (TextView) view
							.findViewById(R.id.tv_name);
					final EditText et_val = (EditText) view
							.findViewById(R.id.et_val);
					et_val.setFocusable(false);
					et_val.setTag(map.get("mxh"));
					et_val.setText(tzz);
					et_val.setHint(title);
					et_val.setHintTextColor(R.color.gray);
					et_val.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dateDialog(et_val);
						}
					});
					tv_name.setText(title);

				} else if ("5".equals(type)) { // 数值
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.include_xj_text, null);
					TextView tv_name = (TextView) view
							.findViewById(R.id.tv_name);
					EditText et_val = (EditText) view.findViewById(R.id.et_val);
					et_val.setInputType(InputType.TYPE_CLASS_NUMBER);
					et_val.setTag(map.get("mxh"));
					et_val.setText(tzz);
					et_val.setHint(title);
					et_val.setHintTextColor(R.color.gray);
					tv_name.setText(title);

				}

				ll.addView(view);
			}
		} catch (Exception e) {
			dialogShowMessage_P("数据错误:" + errormsg + ",选项数据类型不匹配,请联系管理员修改",
					null);
			e.printStackTrace();
		}
	}

	private void loadImg(final ArrayList<String> list) {
		try {
			String mxh = tv_curr.getTag().toString();
			if (list.size() > 0) {
				tv_curr.setText("继续选择");
				tv_curr.setBackgroundResource(R.drawable.btn_normal_yellow);
			} else {
				tv_curr.setText("选择图片");
				tv_curr.setBackgroundResource(R.drawable.btn_normal);
			}
			filemap.put(mxh, list);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			switch (msg.what) {
				case Constant.FAIL:
					dialogShowMessage_P("失败，" + msgStr, null);
					break;
				case Constant.NETWORK_ERROR:
					dialogShowMessage_P("网络连接出错，请检查你的网络设置", null);
					break;
				case Constant.SUCCESS:
					dialogShowMessage_P("提交成功！",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
													int paramAnonymous2Int) {
									onBackPressed();
								}
							});
					break;
				case Constant.NUM_6:
					adapter = new SimpleAdapter(ZcdjInfo.this, data_sf,
							R.layout.spinner_item, from, to);
					spinner_sf.setAdapter(adapter);
					if (sfbm != null && !"".equals(sfbm)) {
						for (int i = 0; i < data_sf.size(); i++) {
							if (sfbm.equals(data_sf.get(i).get("id"))) {
								spinner_sf.setSelection(i);
							}
						}
					}

					adapter = new SimpleAdapter(ZcdjInfo.this, data_sblx,
							R.layout.spinner_item, from, to);
					spinner_sblx.setAdapter(adapter);
					if (sblxbm != null && !"".equals(sblxbm)) {
						for (int i = 0; i < data_sblx.size(); i++) {
							if (sblxbm.equals(data_sblx.get(i).get("id"))) {
								spinner_sblx.setSelection(i);
							}
						}
					}

					loadSbxx(data_zp, ll_show);
					break;
				case Constant.NUM_7:
					adapter = new SimpleAdapter(ZcdjInfo.this, data_ds,
							R.layout.spinner_item, from, to);
					spinner_ds.setAdapter(adapter);
					if (dsbm != null && !"".equals(dsbm)) {
						for (int i = 0; i < data_ds.size(); i++) {
							if (dsbm.equals(data_ds.get(i).get("id"))) {
								spinner_ds.setSelection(i);
							}
						}
					}
					break;
				case Constant.NUM_8:
					adapter = new SimpleAdapter(ZcdjInfo.this, data_qx,
							R.layout.spinner_item, from, to);
					spinner_qx.setAdapter(adapter);
					if (qxbm != null && !"".equals(qxbm)) {
						for (int i = 0; i < data_qx.size(); i++) {
							if (qxbm.equals(data_qx.get(i).get("id"))) {
								spinner_qx.setSelection(i);
							}
						}
					}
					break;
				case Constant.NUM_9:
					mLocClient.stop();
					mLocClient.unRegisterLocationListener(myListener);
					break;
				case Constant.NUM_10:
					if (wdbm != null && !"".equals(wdbm)) {
						for (int i = 0; i < data_wdmc.size(); i++) {
							if (wdbm.equals(data_wdmc.get(i).get("id"))) {
								tv_xqmc.setTag(data_wdmc.get(i).get("id"));
								tv_xqmc.setText(data_wdmc.get(i).get("name"));
								et_jddz.setText(data_wdmc.get(i).get("xxdz"));
							}
						}
					}else{
						tv_xqmc.setTag("");
						tv_xqmc.setText("");
						et_jddz.setText("");
						et_xxdz.setText("");
					}
					break;
				case Constant.NUM_11:
					if("".equals(sbid)){
						dialogShowMessage_P("该二维码已被使用，请更换二维码",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
												int paramAnonymous2Int) {
								et_sbid.setText("");
							}
						});
					}else{
						if(Pattern.matches(PATTERN_EWM, sbid)){
							et_sbid.setText(sbid);
						}else{
							dialogShowMessage_P("二维码需以0 开头，至少8位数字",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
													int paramAnonymous2Int) {
									et_sbid.setText("");
								}
							});
						}
					}

					break;
				case Constant.NUM_12:
					dialogShowMessage_P("提交成功",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface face,
											int paramAnonymous2Int) {
							onBackPressed();
						}
					});
					break;
				case Constant.NUM_13:
					dialogShowMessage("存在"+filelistFail.size()+"张图片上传失败，是否确认继续上传？",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface face,
											int paramAnonymous2Int) {
							showProgressDialog();
							Config.getExecutorService().execute(new Runnable() {

								@Override
								public void run() {

									uploadFail();
								}
							});
						}
					},new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface face,
											int paramAnonymous2Int) {
							onBackPressed();
						}
					});
					break;
			}

		}

	};
}