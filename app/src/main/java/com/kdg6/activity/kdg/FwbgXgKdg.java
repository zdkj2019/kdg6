package com.kdg6.activity.kdg;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.kdg6.utils.ImageUtil;
import com.kdg6.zxing.CaptureActivity;

/**
 * 快递柜-服务报告修改
 *
 * @author zdkj
 *
 */
public class FwbgXgKdg extends FrameActivity {

	private Button confirm, cancel, btn_sm, btn_xz_zg, btn_xz_fg1, btn_xz_fg2,
			btn_xz_fg3, btn_xz_fg4, btn_xz_fg5, btn_xz_fg6, btn_xz_fg7,
			btn_xz_fg8;
	private String flag, zbh, message, sbbm = "0", fbf, wdbm, sfecsm, ecsmyy;
	private CheckBox cb_xzpj;
	private Spinner spinner_fgsjazs;
	private RadioGroup rg_0, rg_sfzjsb;
	private TextView tv_xzpj, tv_curr, tv_btgyy;
	private EditText et_smyy, et_zgxlh, et_sbbm, et_zdh, et_simkh, et_fg1,
			et_fg2, et_fg3, et_fg4, et_fg5, et_fg6, et_fg7, et_fg8;
	private LinearLayout ll_fg_1, ll_fg_2, ll_fg_3, ll_fg_4, ll_fg_5, ll_fg_6,
			ll_fg_7, ll_fg_8;
	private ArrayList<Map<String, String>> data_sb, data_zp, data_fg_choose,
			data_load_sbxx, data_load_pjxx, data_load_yhpj;
	private ArrayList<String> hpdata;
	private String xzpj_str;
	private LinearLayout ll_show;
	private Map<String, ArrayList<String>> filemap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_fwbg);
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
		// if("（暂停）".equals(ztzt)){
		// confirm.setOnClickListener(null);
		// dialogShowMessage("该工单处于暂停状态，不能提交！", null, null);
		// }
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

	@SuppressLint("ResourceAsColor")
	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		btn_sm = (Button) findViewById(R.id.btn_sm);

		btn_xz_zg = (Button) findViewById(R.id.btn_xz_zg);
		btn_xz_fg1 = (Button) findViewById(R.id.btn_xz_fg1);
		btn_xz_fg2 = (Button) findViewById(R.id.btn_xz_fg2);
		btn_xz_fg3 = (Button) findViewById(R.id.btn_xz_fg3);
		btn_xz_fg4 = (Button) findViewById(R.id.btn_xz_fg4);
		btn_xz_fg5 = (Button) findViewById(R.id.btn_xz_fg5);
		btn_xz_fg6 = (Button) findViewById(R.id.btn_xz_fg6);
		btn_xz_fg7 = (Button) findViewById(R.id.btn_xz_fg7);
		btn_xz_fg8 = (Button) findViewById(R.id.btn_xz_fg8);

		spinner_fgsjazs = (Spinner) findViewById(R.id.spinner_fgsjazs);
		et_zgxlh = (EditText) findViewById(R.id.et_zgxlh);
		cb_xzpj = (CheckBox) findViewById(R.id.cb_xzpj);
		ll_show = (LinearLayout) findViewById(R.id.ll_show);
		tv_xzpj = (TextView) findViewById(R.id.tv_xzpj);
		tv_btgyy = (TextView) findViewById(R.id.tv_btgyy);
		et_sbbm = (EditText) findViewById(R.id.et_sbbm);
		et_zdh = (EditText) findViewById(R.id.et_zdh);
		et_simkh = (EditText) findViewById(R.id.et_simkh);
		et_fg1 = (EditText) findViewById(R.id.et_fg1);
		et_fg2 = (EditText) findViewById(R.id.et_fg2);
		et_fg3 = (EditText) findViewById(R.id.et_fg3);
		et_fg4 = (EditText) findViewById(R.id.et_fg4);
		et_fg5 = (EditText) findViewById(R.id.et_fg5);
		et_fg6 = (EditText) findViewById(R.id.et_fg6);
		et_fg7 = (EditText) findViewById(R.id.et_fg7);
		et_fg8 = (EditText) findViewById(R.id.et_fg8);

		et_smyy = (EditText) findViewById(R.id.et_smyy);
		rg_0 = (RadioGroup) findViewById(R.id.rg_0);
		rg_sfzjsb = (RadioGroup) findViewById(R.id.rg_sfzjsb);
		ll_fg_1 = (LinearLayout) findViewById(R.id.ll_fg_1);
		ll_fg_2 = (LinearLayout) findViewById(R.id.ll_fg_2);
		ll_fg_3 = (LinearLayout) findViewById(R.id.ll_fg_3);
		ll_fg_4 = (LinearLayout) findViewById(R.id.ll_fg_4);
		ll_fg_5 = (LinearLayout) findViewById(R.id.ll_fg_5);
		ll_fg_6 = (LinearLayout) findViewById(R.id.ll_fg_6);
		ll_fg_7 = (LinearLayout) findViewById(R.id.ll_fg_7);
		ll_fg_8 = (LinearLayout) findViewById(R.id.ll_fg_8);

		data_sb = new ArrayList<Map<String, String>>();
		data_fg_choose = new ArrayList<Map<String, String>>();
		filemap = new HashMap<String, ArrayList<String>>();

		final Map<String, Object> itemmap = ServiceReportCache.getObjectdata()
				.get(ServiceReportCache.getIndex());

		zbh = itemmap.get("zbh").toString();
		sfecsm = itemmap.get("sfecsm").toString();
		ecsmyy = itemmap.get("ecsmyy").toString();
		sbbm = itemmap.get("sbewm").toString();
		((TextView) findViewById(R.id.tv_zbh)).setText(zbh);
		((TextView) findViewById(R.id.tv_axdh)).setText(itemmap.get("sx")
				.toString());
		((TextView) findViewById(R.id.tv_xqmc)).setText(itemmap.get("qy")
				.toString());
		((TextView) findViewById(R.id.tv_xxdz)).setText(itemmap.get("xqmc")
				.toString());
		((TextView) findViewById(R.id.tv_ywlx)).setText(itemmap.get("xxdz")
				.toString());
		((TextView) findViewById(R.id.tv_sblx)).setText(itemmap.get("gzxx")
				.toString());
		((TextView) findViewById(R.id.tv_gzxx)).setText(itemmap.get("ywlx")
				.toString());
		((TextView) findViewById(R.id.tv_ds)).setText(itemmap.get("bz")
				.toString());

		tv_btgyy.setText(itemmap.get("fwnr").toString());

		String sfec = "1".equals(sfecsm)?"是":"否";
		for (int i = 0; i < rg_0.getChildCount(); i++) {
			RadioButton rb = (RadioButton) rg_0.getChildAt(i);
			if(sfec.equals(rb.getText().toString())){
				rb.setChecked(true);
			}
			rg_0.getChildAt(i).setEnabled(false);
		}
		et_smyy.setText(ecsmyy);
		et_smyy.setTextColor(R.color.gray);
		et_smyy.setEnabled(false);

		hideAll();

		List list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		list.add("7");
		list.add("8");
		ArrayAdapter adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_fgsjazs.setAdapter(adapter);

		et_sbbm.setText(sbbm);
		et_sbbm.setFocusable(false);
		btn_sm.setVisibility(View.GONE);
		findViewById(R.id.ll_xzpj).setVisibility(View.GONE);
		findViewById(R.id.ll_xzpj_content).setVisibility(View.GONE);
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
						if (rg_0.getCheckedRadioButtonId() == -1) {
							toastShowMessage("请选择是否二次上门！");
						} else {
							if (rg_0.getCheckedRadioButtonId() == R.id.rb_1) {
								if (!isNotNull(et_smyy)) {
									toastShowMessage("请录入申请二次上门原因！");
								} else {
									if (rg_sfzjsb.getCheckedRadioButtonId() == R.id.rb_s) {
										boolean f = true;
										int sl = spinner_fgsjazs
												.getSelectedItemPosition() + 1;
										for (int i = 1; i < sl; i++) {
											if (i == 1) {
												if (!isNotNull(et_fg1)) {
													f = false;
												}
											}
											if (i == 2) {
												if (!isNotNull(et_fg2)) {
													f = false;
												}
											}
											if (i == 3) {
												if (!isNotNull(et_fg3)) {
													f = false;
												}
											}
											if (i == 4) {
												if (!isNotNull(et_fg4)) {
													f = false;
												}
											}
											if (i == 5) {
												if (!isNotNull(et_fg5)) {
													f = false;
												}
											}
											if (i == 6) {
												if (!isNotNull(et_fg6)) {
													f = false;
												}
											}
											if (i == 7) {
												if (!isNotNull(et_fg7)) {
													f = false;
												}
											}
											if (i == 8) {
												if (!isNotNull(et_fg8)) {
													f = false;
												}
											}
										}
										if (!isNotNull(et_zdh)
												|| !isNotNull(et_simkh) || !f) {
											toastShowMessage("设备各项信息不能为空！");
											return;
										}
									}
									showProgressDialog();
									Config.getExecutorService().execute(
											new Runnable() {

												@Override
												public void run() {
													getWebService("submit");
												}
											});
								}
							} else {
								if (cb_xzpj.isChecked()) {
									if (hpdata == null || hpdata.size() == 0) {
										toastShowMessage("未选择备件！");
										return;
									}
								}
								if (rg_sfzjsb.getCheckedRadioButtonId() == R.id.rb_s) {
									boolean f = true;
									int sl = spinner_fgsjazs
											.getSelectedItemPosition() + 1;
									for (int i = 1; i < sl; i++) {
										if (i == 1) {
											if (!isNotNull(et_fg1)) {
												f = false;
											}
										}
										if (i == 2) {
											if (!isNotNull(et_fg2)) {
												f = false;
											}
										}
										if (i == 3) {
											if (!isNotNull(et_fg3)) {
												f = false;
											}
										}
										if (i == 4) {
											if (!isNotNull(et_fg4)) {
												f = false;
											}
										}
										if (i == 5) {
											if (!isNotNull(et_fg5)) {
												f = false;
											}
										}
										if (i == 6) {
											if (!isNotNull(et_fg6)) {
												f = false;
											}
										}
										if (i == 7) {
											if (!isNotNull(et_fg7)) {
												f = false;
											}
										}
										if (i == 8) {
											if (!isNotNull(et_fg8)) {
												f = false;
											}
										}
									}
									if (!isNotNull(et_zdh) || !isNotNull(et_simkh)
											|| !f) {
										toastShowMessage("设备各项信息不能为空！");
										return;
									}
								}
								showProgressDialog();
								Config.getExecutorService().execute(new Runnable() {

									@Override
									public void run() {
										getWebService("submit");
									}
								});
							}
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

		btn_xz_zg.setOnClickListener(backonClickListener);
		btn_xz_fg1.setOnClickListener(backonClickListener);
		btn_xz_fg2.setOnClickListener(backonClickListener);
		btn_xz_fg3.setOnClickListener(backonClickListener);
		btn_xz_fg4.setOnClickListener(backonClickListener);
		btn_xz_fg5.setOnClickListener(backonClickListener);
		btn_xz_fg6.setOnClickListener(backonClickListener);
		btn_xz_fg7.setOnClickListener(backonClickListener);
		btn_xz_fg8.setOnClickListener(backonClickListener);

		spinner_fgsjazs.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				hideAll();
				data_fg_choose.clear();
				String str = spinner_fgsjazs.getSelectedItem().toString();
				int num = Integer.parseInt(str);
				if (num > 0 && num < 9) {
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
					}
				} else {
					toastShowMessage("副柜安装数为1-8之间的数字！");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		}); // 中类

		tv_xzpj.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						AddParts.class);
				intent.putStringArrayListExtra("hpdata", hpdata);
				startActivityForResult(intent, 3);
			}

		});

		// rg_0.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(RadioGroup group, int checkedId) {
		// if (checkedId == R.id.rb_1) {
		// findViewById(R.id.ll_sbxx).setVisibility(View.GONE);
		// findViewById(R.id.ll_sbxx_content).setVisibility(View.GONE);
		// findViewById(R.id.ll_xzpj).setVisibility(View.GONE);
		// findViewById(R.id.ll_xzpj_content).setVisibility(View.GONE);
		// } else {
		// findViewById(R.id.ll_sbxx).setVisibility(View.VISIBLE);
		// findViewById(R.id.ll_sbxx_content).setVisibility(
		// View.VISIBLE);
		// findViewById(R.id.ll_xzpj).setVisibility(View.VISIBLE);
		// findViewById(R.id.ll_xzpj_content).setVisibility(
		// View.VISIBLE);
		// }
		// }
		// });
	}

	private void startSm() {
		// 二维码
		Intent intent = new Intent(getApplicationContext(),
				CaptureActivity.class);
		startActivityForResult(intent, 2);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			ArrayList<String> list = data.getStringArrayListExtra("imglist");
			loadImg(list);
		} else if (requestCode == 3 && resultCode == RESULT_OK && data != null) {

			xzpj_str = "";
			try {
				// 配件hpsql
				hpdata = data.getStringArrayListExtra("hpdata");
				if (hpdata != null) {
					for (int i = 0; i < hpdata.size(); i++) {
						String[] hps = hpdata.get(i).split(",");
						if (hps.length < 4) {
							xzpj_str = xzpj_str + hps[1] + "," + hps[2] + ","
									+ "\n";
						} else {
							xzpj_str = xzpj_str + hps[1] + "," + hps[2] + ","
									+ hps[3] + "\n";
						}
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
				data_load_sbxx = new ArrayList<Map<String, String>>();
				data_load_pjxx = new ArrayList<Map<String, String>>();
				data_load_yhpj = new ArrayList<Map<String, String>>();
				data_zp = new ArrayList<Map<String, String>>();
				JSONObject jsonObject = null;
				jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_KDGAZ_FWBG_XG_SBXX", sbbm, "uf_json_getdata",
						this);
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("zdh", temp.getString("zdh"));
						item.put("hpbm", temp.getString("hpbm"));
						item.put("zgfg", temp.getString("zgfg"));
						item.put("simkh", temp.getString("simkh"));
						item.put("xlh", temp.getString("xlh"));
						item.put("fgsl", temp.getString("fgsl"));
						data_load_sbxx.add(item);
					}
				}

				// 已换配件信息
				jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_KDG_FWBG_YHBJCX", zbh, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("hpmc", temp.getString("hpmc"));
						item.put("sl", temp.getString("sl"));
						data_load_yhpj.add(item);
					}
				}

				jsonObject = callWebserviceImp
						.getWebServerInfo("_PAD_KDGAZ_FWBG_XG_PJXX", zbh,
								"uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("hpmc", temp.getString("hpmc"));
						item.put("sl", temp.getString("sl"));
						item.put("mxh", temp.getString("mxh"));
						data_load_pjxx.add(item);
					}
				}
				jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SHGL_KDG_FWBG_AZD_XG", zbh + "*" + zbh + "*"
								+ zbh, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("tzlmc", temp.getString("tzlmc"));
						item.put("kzzf1", temp.getString("kzzf1"));
						item.put("kzsz1", temp.getString("kzsz1"));
						item.put("kzzf4", temp.getString("kzzf4"));
						item.put("str", temp.getString("str"));
						item.put("tzz", temp.getString("tzz"));
						item.put("mxh", temp.getString("mxh"));
						data_zp.add(item);
					}
				}
				Message msg = new Message();
				msg.what = Constant.NUM_6;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}

		if (s.equals("sbbm")) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_KDG_SBCX_ER", zbh, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("id", temp.getString("hpbm"));
						item.put("name", temp.getString("hpmc"));
						item.put("zdh", temp.getString("zdh"));
						item.put("simkh", temp.getString("simkh"));
						item.put("kzsz", temp.getString("kzsz"));
						item.put("zgxlh", temp.getString("zgxlh"));
						item.put("cgxlh", temp.getString("cgxlh"));
						data_sb.add(item);
					}

					Message msg = new Message();
					msg.what = Constant.NUM_7;
					handler.sendMessage(msg);
				} else {
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
								cs += "";
							} else {
								CheckBox cb = (CheckBox) ll.getChildAt(1);
								String che = cb.isChecked() ? "1" : "2";
								cs += rb.getText().toString() + "#@#" + che
										+ "#@#" + rb.getTag().toString();
							}
							cs += "#^#";
						} else if (ll.getChildAt(0) instanceof EditText) {
							EditText et = (EditText) ll.getChildAt(0);
							CheckBox cb = (CheckBox) ll.getChildAt(1);
							String che = cb.isChecked() ? "1" : "2";
							cs += et.getText().toString() + "#@#" + che + "#@#"
									+ et.getTag().toString();
							cs += "#^#";
						}
					}
				}
				if (!"".equals(cs)) {
					cs = cs.substring(0, cs.length() - 3);
				}

				// 先上传设备信息
				message = "提交成功！";
				String str = "";
				String zfgStr = "";
				zfgStr += et_zdh.getText().toString();
				zfgStr += "#@#";
				zfgStr += et_simkh.getText().toString();
				zfgStr += "#@#";
				zfgStr += et_zgxlh.getText().toString();
				zfgStr += "#@#";
				zfgStr += et_zgxlh.getTag().toString();
				zfgStr += "#^#";
				for (int i = 0; i < spinner_fgsjazs.getSelectedItemPosition() + 1; i++) {
					if (i == 0) {
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += et_fg1.getText().toString();
						zfgStr += "#@#";
						zfgStr += et_fg1.getTag().toString();
						zfgStr += "#^#";
					}
					if (i == 1) {
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += et_fg2.getText().toString();
						zfgStr += "#@#";
						zfgStr += et_fg2.getTag().toString();
						zfgStr += "#^#";
					}
					if (i == 2) {
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += et_fg3.getText().toString();
						zfgStr += "#@#";
						zfgStr += et_fg3.getTag().toString();
						zfgStr += "#^#";
					}
					if (i == 3) {
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += et_fg4.getText().toString();
						zfgStr += "#@#";
						zfgStr += et_fg4.getTag().toString();
						zfgStr += "#^#";
					}
					if (i == 4) {
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += et_fg5.getText().toString();
						zfgStr += "#@#";
						zfgStr += et_fg5.getTag().toString();
						zfgStr += "#^#";
					}
					if (i == 5) {
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += et_fg6.getText().toString();
						zfgStr += "#@#";
						zfgStr += et_fg6.getTag().toString();
						zfgStr += "#^#";
					}
					if (i == 6) {
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += et_fg7.getText().toString();
						zfgStr += "#@#";
						zfgStr += et_fg7.getTag().toString();
						zfgStr += "#^#";
					}
					if (i == 7) {
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += "";
						zfgStr += "#@#";
						zfgStr += et_fg8.getText().toString();
						zfgStr += "#@#";
						zfgStr += et_fg8.getTag().toString();
						zfgStr += "#^#";
					}
				}
				if (!"".equals(zfgStr)) {
					zfgStr = zfgStr.substring(0, zfgStr.length() - 3);
				}

				// 再提交服务报告
				String typeStr = "az_fwbg_xg";
				str = zbh + "*PAM*" + DataCache.getinition().getUserId();
				str += "*PAM*";
				str += rg_0.getCheckedRadioButtonId() == R.id.rb_1 ? 1 : 2;
				str += "*PAM*";
				str += et_smyy.getText().toString();
				str += "*PAM*";
				// str += cb_xzpj.isChecked() ? 1 : 2;
				str += 2;
				str += "*PAM*";
				// 新增配件
				String xzpj_str = "";
				try {
					if (hpdata != null) {
						for (int i = 0; i < hpdata.size(); i++) {
							String[] hps = hpdata.get(i).split(",");
							if (hps.length < 4) {
								xzpj_str = xzpj_str + hps[0] + "#@#" + hps[2];
							} else {
								xzpj_str = xzpj_str + hps[0] + "#@#" + hps[2];
							}
							xzpj_str = xzpj_str + "#^#";
						}
						xzpj_str = xzpj_str.substring(0, xzpj_str.length() - 3);
					} else {
						xzpj_str += "0";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				str = str + xzpj_str;
				str += "*PAM*";
				str += zfgStr;
				str += "*PAM*";
				str += cs;
				str += "*PAM*";
				str += "";
				str += "*PAM*";
				str += "";
				str += "*PAM*";
				str += "";
				str += "*PAM*";
				str += "";
				str += "*PAM*";
				str += rg_sfzjsb.getCheckedRadioButtonId() == R.id.rb_s ? "1"
						: "2";
				str += "*PAM*";
				str += "";

				JSONObject json = this.callWebserviceImp.getWebServerInfo(
						"c#_PAD_KDG_ALL", str, typeStr, typeStr,
						"uf_json_setdata2", this);
				flag = json.getString("flag");

				if (Integer.parseInt(flag) > 0) {
					upload();
					// Message msg = new Message();
					// msg.what = Constant.SUCCESS;
					// handler.sendMessage(msg);
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

	private void hideAll() {
		// et_fg1.setText("");
		// et_fg2.setText("");
		// et_fg3.setText("");
		// et_fg4.setText("");
		// et_fg5.setText("");
		// et_fg6.setText("");
		// et_fg7.setText("");
		// et_fg8.setText("");
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
					map.put("filepath", filepathlist.get(j));
					filelist.add(map);
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
				msg.what = 12;
				handler.sendMessage(msg);
			} else {
				Message msg = new Message();
				msg.what = 13;
				handler.sendMessage(msg);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = 13;
			handler.sendMessage(msg);
		}

	}

	@SuppressLint("ResourceAsColor")
	protected void loadSbxx(ArrayList<Map<String, String>> data, LinearLayout ll) {
		try {
			for (int i = 0; i < data.size(); i++) {
				Map<String, String> map = data.get(i);
				String title = map.get("tzlmc");
				String type = map.get("kzzf1");
				String content = map.get("str");
				String tzz = map.get("tzz") == null ? "" : map.get("tzz")
						.toString();
				String kzzf4 = map.get("kzzf4");
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
					CheckBox cb = (CheckBox) view.findViewById(R.id.cb_0);
					cb.setVisibility(View.VISIBLE);
					if ("1".equals(kzzf4)) {
						cb.setChecked(true);
					}
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
					CheckBox cb = (CheckBox) view.findViewById(R.id.cb_0);
					cb.setVisibility(View.VISIBLE);
					if ("1".equals(kzzf4)) {
						cb.setChecked(true);
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
					tv_1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							tv_curr = (TextView) v;
							ArrayList<String> list = filemap.get(mxh);
							camera(1,list);
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
					et_val.setText(tzz);
					et_val.setTag(map.get("mxh"));
					et_val.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dateDialog(et_val);
						}
					});
					tv_name.setText(title);
					CheckBox cb = (CheckBox) view.findViewById(R.id.cb_0);
					cb.setVisibility(View.VISIBLE);
					if ("1".equals(kzzf4)) {
						cb.setChecked(true);
					}
				} else if ("5".equals(type)) { // 数值
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.include_xj_text, null);
					TextView tv_name = (TextView) view
							.findViewById(R.id.tv_name);
					EditText et_val = (EditText) view.findViewById(R.id.et_val);
					et_val.setInputType(InputType.TYPE_CLASS_NUMBER);
					et_val.setText(tzz);
					et_val.setTag(map.get("mxh"));
					tv_name.setText(title);
					CheckBox cb = (CheckBox) view.findViewById(R.id.cb_0);
					cb.setVisibility(View.VISIBLE);
					if ("1".equals(kzzf4)) {
						cb.setChecked(true);
					}
				}

				ll.addView(view);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadImg(final ArrayList<String> list) {
		try {
			String mxh = tv_curr.getTag().toString();
			if(list.size()>0){
				tv_curr.setText("继续选择");
				tv_curr.setBackgroundResource(R.drawable.btn_normal_yellow);
			}else{
				tv_curr.setText("选择图片");
				tv_curr.setBackgroundResource(R.drawable.btn_normal);
			}
			filemap.put(mxh, list);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void loadEcsm() {
		if ("1".equals(sfecsm)) {
			rg_0.check(R.id.rb_1);
		} else {
			rg_0.check(R.id.rb_2);
		}
		et_smyy.setText(ecsmyy);
	}

	private void loadPjxx() {
		if (data_load_pjxx.size() > 0) {
			hpdata = new ArrayList<String>();
			xzpj_str = "";
			for (int i = 0; i < data_load_pjxx.size(); i++) {
				Map<String, String> map = data_load_pjxx.get(i);
				String temp = map.get("mxh") + "," + map.get("hpmc") + ","
						+ map.get("sl") + "," + "";
				hpdata.add(temp);
				xzpj_str = xzpj_str + map.get("hpmc") + "," + map.get("sl")
						+ "," + "\n";
			}
			if (!"".equals(xzpj_str)) {
				xzpj_str = xzpj_str.substring(0, xzpj_str.length() - 1);
			}
			tv_xzpj.setText(xzpj_str);
			cb_xzpj.setChecked(true);
		} else {
			cb_xzpj.setChecked(false);
		}
	}

	private void loadSbxx() {
		if (data_load_sbxx != null) {
			int fgnum = 0;
			for (int i = 0; i < data_load_sbxx.size(); i++) {
				Map<String, String> map = data_load_sbxx.get(i);
				String zgfg = map.get("zgfg");
				if ("1".equals(zgfg)) {
					et_zgxlh.setText(map.get("xlh"));
					et_zgxlh.setTag(map.get("hpbm"));
					et_zdh.setText(map.get("zdh"));
					et_simkh.setText(map.get("simkh"));
					spinner_fgsjazs.setSelection(Integer.parseInt(map.get(
							"fgsl").trim()) - 1);
				} else {
					fgnum++;
					if (fgnum == 1) {
						et_fg1.setText(map.get("xlh"));
						et_fg1.setTag(map.get("hpbm"));
					}
					if (fgnum == 2) {
						et_fg2.setText(map.get("xlh"));
						et_fg2.setTag(map.get("hpbm"));
					}
					if (fgnum == 3) {
						et_fg3.setText(map.get("xlh"));
						et_fg3.setTag(map.get("hpbm"));
					}
					if (fgnum == 4) {
						et_fg4.setText(map.get("xlh"));
						et_fg4.setTag(map.get("hpbm"));
					}
					if (fgnum == 5) {
						et_fg5.setText(map.get("xlh"));
						et_fg5.setTag(map.get("hpbm"));
					}
					if (fgnum == 6) {
						et_fg6.setText(map.get("xlh"));
						et_fg6.setTag(map.get("hpbm"));
					}
					if (fgnum == 7) {
						et_fg7.setText(map.get("xlh"));
						et_fg7.setTag(map.get("hpbm"));
					}
					if (fgnum == 8) {
						et_fg8.setText(map.get("xlh"));
						et_fg8.setTag(map.get("hpbm"));
					}
				}
			}
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

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case Constant.FAIL:
					if (progressDialog != null) {
						progressDialog.dismiss();
					}
					dialogShowMessage_P("失败，请检查后重试...错误标识：" + flag, null);
					break;
				case Constant.SUCCESS:
					if (progressDialog != null) {
						progressDialog.dismiss();
					}
					dialogShowMessage_P(message,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
													int paramAnonymous2Int) {
									onBackPressed();
								}
							});
					break;
				case Constant.NETWORK_ERROR:
					if (progressDialog != null) {
						progressDialog.dismiss();
					}
					dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
					break;
				case Constant.NUM_6:
					if (progressDialog != null) {
						progressDialog.dismiss();
					}
					loadEcsm();
					loadPjxx();
					loadSbxx();
					loadYhpj();
					loadSbxx(data_zp, ll_show);
					break;
				case Constant.NUM_7:
					if (progressDialog != null) {
						progressDialog.dismiss();
					}
					break;
				case Constant.NUM_8:
					if (progressDialog != null) {
						progressDialog.dismiss();
					}
					break;
				case Constant.NUM_9:
					if (progressDialog != null) {
						progressDialog.dismiss();
					}
					message = "扫描编码被占用或不存在，请联系当地负责人";
					dialogShowMessage_P(message,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
													int paramAnonymous2Int) {
								}
							});
					break;
				case 11:
					if (progressDialog != null) {
						progressDialog.dismiss();
					}
					dialogShowMessage_P("仓位权限错误，请联系管理员", null);
					break;
				case 12:
					if (progressDialog != null) {
						progressDialog.dismiss();
					}
					dialogShowMessage_P("服务报告提交成功",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
													int paramAnonymous2Int) {
									onBackPressed();
								}
							});
					break;
				case 13:
					if (progressDialog != null) {
						progressDialog.dismiss();
					}
					dialogShowMessage_P("服务报告提交成功,图片上传失败,请到服务报告修改模块中修改！",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
													int paramAnonymous2Int) {
									onBackPressed();
								}
							});
					break;
				case 14:
					if (progressDialog != null) {
						progressDialog.dismiss();
					}
					dialogShowMessage_P("请拍照！", null);
					break;
				case 15:

					break;
			}

		}
	};
	//
	// @Override
	// public void onBackPressed() {
	// Intent intent = new Intent(this, MainActivity.class);
	// intent.putExtra("currType", 1);
	// startActivity(intent);
	// finish();
	// }

}
