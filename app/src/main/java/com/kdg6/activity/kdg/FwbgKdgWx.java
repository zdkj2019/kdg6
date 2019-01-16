package com.kdg6.activity.kdg;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.kdg6.R;
import com.kdg6.activity.FrameActivity;
import com.kdg6.activity.esp.AddParts;
import com.kdg6.cache.DataCache;
import com.kdg6.cache.ServiceReportCache;
import com.kdg6.common.Constant;
import com.kdg6.utils.Config;
import com.kdg6.utils.DateUtil;
import com.kdg6.utils.ImageUtil;

/**
 * 快递柜-服务报告
 *
 * @author zdkj
 *
 */
public class FwbgKdgWx extends FrameActivity {

	private Button confirm, cancel;
	private String flag, zbh, msgStr, sblx, sfecgd, sfecsm,gjbm,
			ecsmyy, dlbm, zlbm, xlbm,ddh,cssj,djsStr;
	private CheckBox cb_xzpj;
	private Spinner spinner_gzdl, spinner_gzzl, spinner_gzxl,spinner_csyy;
	private TextView tv_xzpj, tv_btgyy,tv_wxnr,tv_jssx;
	private EditText et_smyy,et_clgc,et_csnr;
	private ImageView iv_telphone;
	private RadioGroup rg_0;
	private ArrayList<Map<String, String>> data_gzbm, data_all, gzbm_2_list,
			gzbm_3_list, data_zp, data_xj, data_load_yhpj,data_csyy,data_sfcs;
	private ArrayList<Map<String, Object>> data_ywdx;
	private String[] from;
	private int[] to;
	private ArrayList<String> hpdata;
	private String xzpj_str, djzt,lxdh;
	private int kzsz4,jsxh;
	private TextView tv_curr;
	private LinearLayout ll_show;
	private Map<String, ArrayList<String>> filemap;
	private boolean iscs = false;
	private Timer  timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_fwbgwx);
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
		confirm.setText("返回");
		cancel.setText("下一步");

	}

	@SuppressLint("ResourceAsColor")
	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		ll_show = (LinearLayout) findViewById(R.id.ll_show);
		spinner_gzdl = (Spinner) findViewById(R.id.spinner_gzdl);
		spinner_gzzl = (Spinner) findViewById(R.id.spinner_gzzl);
		spinner_gzxl = (Spinner) findViewById(R.id.spinner_gzxl);
		spinner_csyy = (Spinner) findViewById(R.id.spinner_csyy);
		cb_xzpj = (CheckBox) findViewById(R.id.cb_xzpj);
		tv_xzpj = (TextView) findViewById(R.id.tv_xzpj);
		iv_telphone = (ImageView) findViewById(R.id.iv_telphone);
		et_clgc = (EditText) findViewById(R.id.et_clgc);
		et_csnr = (EditText) findViewById(R.id.et_csnr);
		et_smyy = (EditText) findViewById(R.id.et_smyy);
		rg_0 = (RadioGroup) findViewById(R.id.rg_0);
		tv_btgyy = (TextView) findViewById(R.id.tv_btgyy);
		tv_wxnr = (TextView) findViewById(R.id.tv_wxnr);
		from = new String[] { "id", "name" };
		to = new int[] { R.id.bm, R.id.name };
		data_gzbm = new ArrayList<Map<String, String>>();
		gzbm_2_list = new ArrayList<Map<String, String>>();
		gzbm_3_list = new ArrayList<Map<String, String>>();
		filemap = new HashMap<String, ArrayList<String>>();
		final Map<String, Object> itemmap = ServiceReportCache.getObjectdata()
				.get(ServiceReportCache.getIndex());

		zbh =(String) itemmap.get("zbh");
		gjbm =(String) itemmap.get("axdh");
		djzt = (String) itemmap.get("djzt");
		cssj = (String) itemmap.get("cssj");
		sfecgd = (String) itemmap.get("sfecgd");
		sfecsm = (String) itemmap.get("sfecsm");
		ecsmyy = (String) itemmap.get("ecsmyy");
		dlbm = (String) itemmap.get("kzzf3_bm");
		zlbm = (String) itemmap.get("kzzf4_bm");
		xlbm = (String) itemmap.get("kzzf5_bm");
		ddh = (String) itemmap.get("ddh");
		lxdh = (String) itemmap.get("lxdh");
		jsxh = Integer.parseInt((String) itemmap.get("jsxh"));
		kzsz4 = Integer.parseInt((String) itemmap.get("kzsz4"));

		if(kzsz4==1){
			confirm.setText("返回");
			cancel.setText("下一步");
		}else{
			confirm.setText("上一步");
			cancel.setText("下一步");
		}

		((TextView) findViewById(R.id.tv_1)).setText(zbh);
		((TextView) findViewById(R.id.tv_2)).setText((String) itemmap.get("axdh"));
		((TextView) findViewById(R.id.tv_3)).setText((String) itemmap.get("sx"));
		((TextView) findViewById(R.id.tv_4)).setText((String) itemmap.get("qy"));
		((TextView) findViewById(R.id.tv_5)).setText((String) itemmap.get("xqmc"));
		((TextView) findViewById(R.id.tv_6)).setText((String) itemmap.get("xxdz"));
		((TextView) findViewById(R.id.tv_7)).setText((String) itemmap.get("ddh_mc"));
		((TextView) findViewById(R.id.tv_8)).setText((String) itemmap.get("bzsj"));
//		((TextView) findViewById(R.id.tv_9)).setText((String) itemmap.get("yqsx"));
		((TextView) findViewById(R.id.tv_10)).setText((String) itemmap.get("lxdh"));
		((TextView) findViewById(R.id.tv_11)).setText((String) itemmap.get("dygdh1_mc"));
		((TextView) findViewById(R.id.tv_12)).setText((String) itemmap.get("dygdh2_mc"));
		((TextView) findViewById(R.id.tv_13)).setText((String) itemmap.get("gzxx"));
		((TextView) findViewById(R.id.tv_18)).setText((String) itemmap.get("bz"));
		((TextView) findViewById(R.id.tv_chyy)).setText((String) itemmap.get("fwnr"));
		((TextView) findViewById(R.id.et_clgc)).setText((String) itemmap.get("kzzf1"));
		((TextView) findViewById(R.id.tv_jddz)).setText((String) itemmap.get("jddz"));

		findViewById(R.id.ll_xzpj).setVisibility(View.GONE);
		findViewById(R.id.ll_xzpj_content).setVisibility(View.GONE);
		findViewById(R.id.ll_ecsm_title).setVisibility(View.GONE);
		findViewById(R.id.ll_ecsm_content).setVisibility(View.GONE);
		findViewById(R.id.ll_csyy).setVisibility(View.GONE);
		findViewById(R.id.ll_csyy_content).setVisibility(View.GONE);
		findViewById(R.id.ll_yhpj).setVisibility(View.GONE);
		findViewById(R.id.ll_yhpj_content).setVisibility(View.GONE);

		tv_jssx = (TextView) findViewById(R.id.tv_9);
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
					case R.id.confirm:
						if(kzsz4==1){
							onBackPressed();
						}else{
							if(kzsz4==99){
//							cancel.setText("完成");
//							findViewById(R.id.ll_show_title).setVisibility(View.GONE);
//							findViewById(R.id.ll_show).setVisibility(View.GONE);
//							findViewById(R.id.ll_xzpj_content).setVisibility(View.VISIBLE);
//							findViewById(R.id.ll_xzpj).setVisibility(View.VISIBLE);
//							findViewById(R.id.ll_csyy).setVisibility(View.GONE);
//							findViewById(R.id.ll_csyy_content).setVisibility(View.GONE);
								onBackPressed();
							}else{
								cancel.setText("下一步");
								findViewById(R.id.ll_show_title).setVisibility(View.VISIBLE);
								findViewById(R.id.ll_show).setVisibility(View.VISIBLE);
								findViewById(R.id.ll_xzpj_content).setVisibility(View.GONE);
								findViewById(R.id.ll_xzpj).setVisibility(View.GONE);
								findViewById(R.id.ll_csyy).setVisibility(View.GONE);
								findViewById(R.id.ll_csyy_content).setVisibility(View.GONE);
								if(kzsz4==0){
									kzsz4 = jsxh;
								}else{
									kzsz4 = kzsz4-1;
								}

								showProgressDialog();
								Config.getExecutorService().execute(new Runnable() {

									@Override
									public void run() {
										getWebService("queryNr");
									}
								});
							}


						}

						break;
					case R.id.cancel:

//					if("4".equals(djzt)){//服务报告
//						if (rg_0.getCheckedRadioButtonId() == -1) {
//							toastShowMessage("请选择是否二次上门！");
//						} else {
//							if (rg_0.getCheckedRadioButtonId() == R.id.rb_1) {
//								if (!isNotNull(et_smyy)) {
//									toastShowMessage("请录入申请二次上门原因！");
//								} else {
//									String str = ((Map<String, String>) spinner_gzxl.getSelectedItem()).get("name");
//									if (" ".equals(str)) {
//										toastShowMessage("请选择故障类别！");
//										return;
//									}
//									if(!isNotNull(et_clgc)){
//										toastShowMessage("请录入处理过程！");
//										return;
//									}
//									if (cb_xzpj.isChecked()) {
//										if (hpdata == null || hpdata.size() == 0) {
//											toastShowMessage("未选择备件！");
//											return;
//										}
//									}
//
//									showProgressDialog();
//									Config.getExecutorService().execute(new Runnable() {
//
//										@Override
//										public void run() {
//											getWebService("submit");
//										}
//									});
//								}
//							} else {
//								checkSubmit();
//							}
//						}
//
//					}
//					if("5".equals(djzt)){//二次上门
//						checkSubmit();
//					}

						checkSubmit();

						break;
					default:
						break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);

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

				SimpleAdapter adapter = new SimpleAdapter(FwbgKdgWx.this,
						gzbm_2_list, R.layout.spinner_item, from, to);
				spinner_gzzl.setAdapter(adapter);

				if (zlbm != null) {
					for (int i = 0; i < gzbm_2_list.size(); i++) {
						map = gzbm_2_list.get(i);
						if (zlbm.equals(map.get("id"))) {
							spinner_gzzl.setSelection(i);
							zlbm = null;
							break;
						}
					}
				}
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

				SimpleAdapter adapter = new SimpleAdapter(FwbgKdgWx.this,
						gzbm_3_list, R.layout.spinner_item, from, to);
				spinner_gzxl.setAdapter(adapter);

				if (xlbm != null) {
					for (int i = 0; i < gzbm_3_list.size(); i++) {
						map = gzbm_3_list.get(i);
						if (xlbm.equals(map.get("id"))) {
							spinner_gzxl.setSelection(i);
							xlbm = null;
							break;
						}
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		};

		spinner_gzdl.setOnItemSelectedListener(onItemSelectedListener_gzdl);// 故障类别
		// 大类
		spinner_gzzl.setOnItemSelectedListener(onItemSelectedListener_gzzl);// 故障类别
		// 中类

		tv_xzpj.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getApplicationContext(),
						AddParts.class);
				intent.putStringArrayListExtra("hpdata", hpdata);
				startActivityForResult(intent, 3);
			}

		});

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

		// rg_0.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(RadioGroup group, int checkedId) {
		// if (checkedId == R.id.rb_1) {
		// findViewById(R.id.ll_xzpj).setVisibility(View.GONE);
		// findViewById(R.id.ll_xzpj_content).setVisibility(View.GONE);
		// } else {
		// findViewById(R.id.ll_xzpj).setVisibility(View.VISIBLE);
		// findViewById(R.id.ll_xzpj_content).setVisibility(
		// View.VISIBLE);
		// }
		// }
		// });
	}

	public void checkSubmit(){

//		String str = ((Map<String, String>) spinner_gzxl.getSelectedItem()).get("name");
//		if (" ".equals(str)) {
//			toastShowMessage("请选择故障类别！");
//			return;
//		}
		if(!isNotNull(et_clgc)){
			toastShowMessage("请录入处理过程！");
			return;
		}
		if(kzsz4==99){
			if(spinner_csyy.getSelectedItemPosition()==0){
				toastShowMessage("请选择超时原因！");
				return;
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
						toastShowMessage("维修内容中选择项不能为空！");
						return;
					}
				}
			}

		}

		if(kzsz4==0){
			if(!isNotNull(tv_xzpj)){
				dialogShowMessage("您没更换配件，是否确认提交？", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface face,
										int paramAnonymous2Int) {
						showProgressDialog();
						Config.getExecutorService().execute(new Runnable() {

							@Override
							public void run() {
								getWebService("submit");
							}
						});
					}
				}, null);
			}else{
				showProgressDialog();
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {
						getWebService("submit");
					}
				});
			}
		}else{
			showProgressDialog();
			Config.getExecutorService().execute(new Runnable() {

				@Override
				public void run() {
					getWebService("submit");
				}
			});
		}

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
					msg.what = Constant.NUM_14;
					handler.sendMessage(msg);
				}
			}, 0,1000);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			ArrayList<String> list = data.getStringArrayListExtra("imglist");
			loadImg(list);
		}
		if (requestCode == 3 && resultCode == RESULT_OK && data != null) {

			xzpj_str = "";
			try {
				// 配件hpsql
				hpdata = data.getStringArrayListExtra("hpdata");
				if (hpdata != null) {
					for (int i = 0; i < hpdata.size(); i++) {
						String[] hps = hpdata.get(i).split(",");
						xzpj_str = xzpj_str + hps[1] + "," + hps[2] + ","
								+ hps[3] + "\n";
					}
					xzpj_str = xzpj_str.substring(0, xzpj_str.length() - 1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			tv_xzpj.setText(xzpj_str);

		}
	}

	@Override
	protected void getWebService(String s) {
		if (s.equals("query")) {
			try {
				data_load_yhpj = new ArrayList<Map<String, String>>();
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

					data_csyy = new ArrayList<Map<String,String>>();
					jsonObject = callWebserviceImp.getWebServerInfo(
							"_PAD_KDG_CSYYLR_CX", "", "uf_json_getdata", this);
					flag = jsonObject.getString("flag");

					item = new HashMap<String, String>();
					item.put("csyy", "");
					item.put("yymc", "");
					data_csyy.add(item);

					if (Integer.parseInt(flag) > 0) {
						jsonArray = jsonObject.getJSONArray("tableA");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject temp = jsonArray.getJSONObject(i);
							item = new HashMap<String, String>();
							item.put("id", temp.getString("csyy"));
							item.put("name", temp.getString("yymc"));
							data_csyy.add(item);
						}
					}

					// 已换配件信息
					jsonObject = callWebserviceImp.getWebServerInfo(
							"_PAD_KDG_FWBG_YHBJCX", zbh, "uf_json_getdata",
							this);
					flag = jsonObject.getString("flag");
					if (Integer.parseInt(flag) > 0) {
						jsonArray = jsonObject.getJSONArray("tableA");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject temp = jsonArray.getJSONObject(i);
							item = new HashMap<String, String>();
							item.put("hpmc", temp.getString("hpmc"));
							item.put("sl", temp.getString("sl"));
							data_load_yhpj.add(item);
						}
					}

					jsonObject = callWebserviceImp.getWebServerInfo(
							"_PAD_SHGL_KDG_FWBG_AZD", zbh + "*" + zbh + "*"+ kzsz4+"*"
									+ zbh+"*"+kzsz4, "uf_json_getdata", this);
					flag = jsonObject.getString("flag");
					data_zp = new ArrayList<Map<String, String>>();
					if (Integer.parseInt(flag) > 0) {
						jsonArray = jsonObject.getJSONArray("tableA");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject temp = jsonArray.getJSONObject(i);
							item = new HashMap<String, String>();
							item.put("tzlmc", temp.getString("tzlmc"));
							item.put("kzzf1", temp.getString("kzzf1"));
							item.put("kzsz1", temp.getString("kzsz1"));
							item.put("str", temp.getString("str"));
							item.put("mxh", temp.getString("mxh"));
							item.put("tzz", temp.getString("tzz"));
							item.put("path", temp.getString("path"));
							data_zp.add(item);
						}

						Message msg = new Message();
						msg.what = Constant.NUM_6;
						handler.sendMessage(msg);

					} else {
						// flag = jsonObject.getString("msg");
						Message msg = new Message();
						msg.what = Constant.NUM_6;// 失败
						handler.sendMessage(msg);
					}
				} else {
					// msgStr = jsonObject.getString("msg");
					Message msg = new Message();
					msg.what = Constant.FAIL;// 失败
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}

		if (s.equals("queryNr")) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SHGL_KDG_FWBG_AZD", zbh + "*" + zbh + "*"+ kzsz4+"*"
								+ zbh+"*"+kzsz4, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				data_zp = new ArrayList<Map<String, String>>();
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("tzlmc", temp.getString("tzlmc"));
						item.put("kzzf1", temp.getString("kzzf1"));
						item.put("kzsz1", temp.getString("kzsz1"));
						item.put("str", temp.getString("str"));
						item.put("mxh", temp.getString("mxh"));
						item.put("tzz", temp.getString("tzz"));
						item.put("path", temp.getString("path"));
						data_zp.add(item);
					}
				}
				Message msg = new Message();
				msg.what = Constant.NUM_8;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}

		if (s.equals("sfcs")) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_YWGL_WXSFCS", zbh, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				data_sfcs = new ArrayList<Map<String, String>>();
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("zbh", temp.getString("zbh"));
						item.put("djzt", temp.getString("djzt"));

						data_sfcs.add(item);
					}
				}

				jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_ZCGL_SB_WXSBSCXJ", gjbm, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				data_ywdx = new ArrayList<Map<String, Object>>();
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
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
						item.put("sccs", temp.getString("sccs"));
						item.put("kzzf3", temp.getString("kzzf3"));
						item.put("kzsz3", temp.getString("kzsz3"));
						item.put("kzsz4", temp.getString("kzsz4"));
						item.put("dypsdh", temp.getString("dypsdh"));
						item.put("sf_mc", temp.getString("sf_mc"));
						item.put("ds_mc", temp.getString("ds_mc"));
						item.put("qx_mc", temp.getString("qx_mc"));
						item.put("wdbmwd_mc", temp.getString("wdbmwd_mc"));
						item.put("sblx_mc", temp.getString("sblx_mc"));
						item.put("sccs_mc", temp.getString("sccs_mc"));
						item.put("dypsdh_mc", temp.getString("dypsdh_mc"));
						item.put("sblx", temp.getString("sblx"));
						item.put("sblx_mc", temp.getString("sblx_mc"));
						item.put("sccs", temp.getString("sccs"));
						item.put("sccs_mc", temp.getString("sccs_mc"));
						item.put("zgbm", temp.getString("zgbm"));
						item.put("fgbm", temp.getString("fgbm"));
						item.put("kzzf1", temp.getString("kzzf1"));
						item.put("djzt", temp.getString("djzt"));
						item.put("bz", temp.getString("bz"));
						item.put("red", "2".equals(temp.getString("djzt"))?"1":"2");

						item.put("timemy", "");
						item.put("datemy", "");
						item.put("ztzt", "");

						data_ywdx.add(item);
					}
				}

				Message msg = new Message();
				msg.what = Constant.NUM_9;
				handler.sendMessage(msg);
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
								String che = rb.getId() == R.id.rb_1 ? "1"
										: "2";
								cs += rb.getTag().toString() +"#@#" + rb.getText().toString();
							}
							cs += "#^#";
						} else if (ll.getChildAt(0) instanceof EditText) {
							EditText et = (EditText) ll.getChildAt(0);
							CheckBox cb = (CheckBox) ll.getChildAt(1);
							String che = cb.isChecked() ? "1" : "2";
							cs += et.getTag().toString()+ "#@#" + et.getText().toString();
							cs += "#^#";
						}
					}

				}
				if (!"".equals(cs)) {
					cs = cs.substring(0, cs.length() - 3);
				}

				// 新增配件
				String xzpj_str = "";
				if(cb_xzpj.isChecked()){
					try {
						if (hpdata != null) {
							for (int i = 0; i < hpdata.size(); i++) {
								String[] hps = hpdata.get(i).split(",");
								String sfhs = hps[3];
								if ("是".equals(sfhs)) {
									sfhs = "1";
								} else {
									sfhs = "2";
								}
								xzpj_str = xzpj_str + hps[0] + "#@#" + hps[2]
										+ "#@#" + sfhs;
								xzpj_str = xzpj_str + "#^#";
							}
							xzpj_str = xzpj_str.substring(0, xzpj_str.length() - 3);
						} else {
							xzpj_str += "";
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}


				// 再提交服务报告
				String str = "";
				String typeStr = "";

				if(kzsz4==99){
					typeStr = "wx_csyytx";
					str = zbh + "*PAM*" + DataCache.getinition().getUserId();
					str += "*PAM*";
					str += data_csyy.get(spinner_csyy.getSelectedItemPosition()).get("id");
					str += "*PAM*";
					str += et_csnr.getText().toString().trim();
				}else{
					typeStr = "wx_fwbg_xyb";
					str = zbh + "*PAM*" + DataCache.getinition().getUserId();
					str += "*PAM*";
					str += et_clgc.getText();
					str += "*PAM*";
					str += cb_xzpj.isChecked()?"1":"0";
					str += "*PAM*";
					str += kzsz4;
					str += "*PAM*";
					if(kzsz4==0){
						str += "2";
						str += "*PAM*";
						str += xzpj_str;
					}else{
						str += "1";
						str += "*PAM*";
						str += cs;
					}
				}


				JSONObject json = this.callWebserviceImp.getWebServerInfo(
						"c#_PAD_KDG_ALL", str,
						DataCache.getinition().getUserId(), typeStr,
						"uf_json_setdata2", this);
				flag = json.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					upload();
					// Message msg = new Message();
					// msg.what = Constant.SUCCESS;
					// handler.sendMessage(msg);
				} else {
					msgStr = json.getString("msg");
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
			for (int i = 0; i < filenum; i++) {
				Map<String, String> map = filelist.get(i);
				if (flag) {
					String mxh = map.get("mxh");
					String filepath = map.get("filepath");
					String num = map.get("num");
					filepath = filepath.substring(7, filepath.length());
					// 压缩图片到100K
					filepath = ImageUtil
							.compressAndGenImage(
									convertBitmap(new File(filepath),
											getScreenWidth()), 200, "jpg");
					File file = new File(filepath);
					// toastShowMessage("开始上传第" + (i + 1) + "/" + filenum +
					// "张图片");
					flag = uploadPic(num, mxh, readJpeg(file),
							"uf_json_setdata", zbh, "c#_PAD_KDGAZ_FWBG_ZPXG");
					file.delete();
				} else {
					flag = false;
					break;
				}
			}
			if (flag) {
				Message msg = new Message();
				msg.what = Constant.NUM_12;
				handler.sendMessage(msg);
			} else {
				Message msg = new Message();
				msg.what = Constant.NUM_12;
				handler.sendMessage(msg);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = Constant.NUM_12;
			handler.sendMessage(msg);
		}

	}

	@SuppressLint("ResourceAsColor")
	protected void loadSbxx(ArrayList<Map<String, String>> data, LinearLayout ll) {
		String errormsg = "";
		try {
			filemap = new HashMap<String, ArrayList<String>>();
			ll.removeAllViews();
			for (int i = 0; i < data.size(); i++) {
				Map<String, String> map = data.get(i);
				String title = map.get("tzlmc");
				errormsg = title;
				String type = map.get("kzzf1");
				String content = map.get("str");
				String tzz = map.get("tzz") == null ? "" : map.get("tzz").toString();
				View view = null;
				if ("2".equals(type)) {// 输入
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.include_xj_text, null);
					TextView tv_name = (TextView) view
							.findViewById(R.id.tv_name);
					EditText et_val = (EditText) view.findViewById(R.id.et_val);
					et_val.setText(tzz);
					et_val.setTag(map.get("mxh"));
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
					tv_name.setText(title);

				}

				ll.addView(view);
			}
		} catch (Exception e) {
			dialogShowMessage_P("数据错误:" + errormsg + ",选项数据类型不匹配,请联系管理员修改",
					null);
			e.printStackTrace();
		}

		if(data.size()==0){
			findViewById(R.id.ll_show_title).setVisibility(View.GONE);
			findViewById(R.id.ll_show).setVisibility(View.GONE);
		}

		tv_wxnr.setText("维修内容（步骤"+kzsz4+"，共"+jsxh+"）");
		if(kzsz4==1){
			confirm.setText("返回");
			cancel.setText("下一步");
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

	private void loadYhpj() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll_yhpj_content);
		for (int i = 0; i < data_load_yhpj.size(); i++) {
			Map<String, String> map = data_load_yhpj.get(i);
			View view = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.include_xj_type_5, null);
			TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
			TextView tv_val = (TextView) view.findViewById(R.id.tv_val);
			tv_name.setText("货品/数量：");
			tv_val.setText(map.get("hpmc") + "/" + map.get("sl"));
			ll.addView(view);
		}
		if (data_load_yhpj.size() == 0) {
			findViewById(R.id.ll_yhpj).setVisibility(View.GONE);
			ll.setVisibility(View.GONE);
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			switch (msg.what) {
				case Constant.FAIL:
					dialogShowMessage_P(msgStr, null);
					break;
				case Constant.SUCCESS:

					break;
				case Constant.NETWORK_ERROR:
					dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
					break;
				case Constant.NUM_6:
					SimpleAdapter adapter = new SimpleAdapter(FwbgKdgWx.this,
							data_gzbm, R.layout.spinner_item, from, to);
					spinner_gzdl.setAdapter(adapter);
					// 加载故障大中小类
					for (int i = 0; i < data_gzbm.size(); i++) {
						Map<String, String> map = data_gzbm.get(i);
						if (dlbm.equals(map.get("id"))) {
							spinner_gzdl.setSelection(i);
						}
					}

					adapter = new SimpleAdapter(FwbgKdgWx.this,
							data_csyy, R.layout.spinner_item, from, to);
					spinner_csyy.setAdapter(adapter);

					loadSbxx(data_zp, ll_show);
					//loadYhpj();

					break;
				case Constant.NUM_7:
					break;
				case Constant.NUM_8:
					loadSbxx(data_zp, ll_show);
					break;
				case Constant.NUM_9:
					if(data_sfcs.size()>0){
						kzsz4 = 99;
						confirm.setText("返回");
						cancel.setText("提交");
						findViewById(R.id.ll_show_title).setVisibility(View.GONE);
						findViewById(R.id.ll_show).setVisibility(View.GONE);
						findViewById(R.id.ll_xzpj_content).setVisibility(View.GONE);
						findViewById(R.id.ll_xzpj).setVisibility(View.GONE);
						findViewById(R.id.ll_csyy).setVisibility(View.VISIBLE);
						findViewById(R.id.ll_csyy_content).setVisibility(View.VISIBLE);
						dialogShowMessage_P("该工单已超时，请填写超时原因",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface face,
														int paramAnonymous2Int) {

									}
								});
					}else{
						if(data_ywdx.size()>0){
							dialogShowMessage("提交成功，查询到该设备未首次巡检，是否进行巡检？",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
													int paramAnonymous2Int) {
									ServiceReportCache.setObjectdata(data_ywdx);
									ServiceReportCache.setIndex(0);
									Intent intent = new Intent(FwbgKdgWx.this, ZcdjInfo.class);
									intent.putExtra("type", "2");
									startActivity(intent);
									finish();
								}
							},new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
													int paramAnonymous2Int) {
									onBackPressed();
								}
							});
						}else{
							dialogShowMessage_P("提交成功",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
													int paramAnonymous2Int) {
									onBackPressed();
								}
							});
						}
					}
					break;
				case Constant.NUM_12:

					if(kzsz4==0){
						showProgressDialog();
						Config.getExecutorService().execute(new Runnable() {

							@Override
							public void run() {
								getWebService("sfcs");
							}
						});
					}else if(kzsz4==jsxh){
						kzsz4 = 0;
						confirm.setText("上一步");
						cancel.setText("完成");
						filemap = new HashMap<String, ArrayList<String>>();
						findViewById(R.id.ll_show_title).setVisibility(View.GONE);
						findViewById(R.id.ll_show).setVisibility(View.GONE);
						findViewById(R.id.ll_xzpj_content).setVisibility(View.VISIBLE);
						findViewById(R.id.ll_xzpj).setVisibility(View.VISIBLE);
						findViewById(R.id.ll_csyy).setVisibility(View.GONE);
						findViewById(R.id.ll_csyy_content).setVisibility(View.GONE);
					}else if(kzsz4==99){
						if(data_ywdx.size()>0){
							dialogShowMessage("提交成功，查询到该设备未首次巡检，是否进行巡检？",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
													int paramAnonymous2Int) {
									ServiceReportCache.setObjectdata(data_ywdx);
									ServiceReportCache.setIndex(0);
									Intent intent = new Intent(FwbgKdgWx.this, ZcdjInfo.class);
									intent.putExtra("type", "2");
									startActivity(intent);
									finish();
								}
							},new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
													int paramAnonymous2Int) {
									onBackPressed();
								}
							});
						}else{
							dialogShowMessage_P("提交成功",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
													int paramAnonymous2Int) {
									onBackPressed();
								}
							});
						}
					}else{
						confirm.setText("上一步");
						cancel.setText("下一步");
						kzsz4 = kzsz4+1;
						findViewById(R.id.ll_show_title).setVisibility(View.VISIBLE);
						findViewById(R.id.ll_show).setVisibility(View.VISIBLE);
						findViewById(R.id.ll_xzpj_content).setVisibility(View.GONE);
						findViewById(R.id.ll_xzpj).setVisibility(View.GONE);
						findViewById(R.id.ll_csyy).setVisibility(View.GONE);
						findViewById(R.id.ll_csyy_content).setVisibility(View.GONE);
						showProgressDialog();
						Config.getExecutorService().execute(new Runnable() {

							@Override
							public void run() {
								getWebService("queryNr");
							}
						});
					}
					break;
				case Constant.NUM_14:
					tv_jssx.setText(djsStr);
					if(iscs){
						tv_jssx.setTextColor(getResources().getColor(R.color.red));
					}
					break;
			}

		}
	};

}
