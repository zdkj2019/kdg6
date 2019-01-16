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

import com.kdg6.R;
import com.kdg6.activity.FrameActivity;
import com.kdg6.activity.main.MainActivity;
import com.kdg6.cache.DataCache;
import com.kdg6.cache.ServiceReportCache;
import com.kdg6.common.Constant;
import com.kdg6.utils.Config;
import com.kdg6.utils.ImageUtil;
/**
 * 快递柜-超时工单查询-数据展示
 * @author zdkj
 *
 */
public class CsgdcxShowKdg extends FrameActivity {

	private Button confirm,cancel;
	private String flag,zbh,type="1",msgStr,lxdh;
	private ImageView iv_telphone;
	private Spinner spinner_csyy;
	private EditText et_csnr;
	private String[] from;
	private int[] to;
	private List<Map<String, Object>> datalist;
	private ArrayList<Map<String, String>> data_csyy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_csgdcxshow);
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
		confirm.setText("保存");
		cancel.setText("取消");
		iv_telphone = (ImageView) findViewById(R.id.iv_telphone);
		spinner_csyy = (Spinner) findViewById(R.id.spinner_csyy);
		et_csnr = (EditText) findViewById(R.id.et_csnr);
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		Map<String, Object> itemmap = ServiceReportCache.getObjectdata().get(ServiceReportCache.getIndex());

		zbh = (String) itemmap.get("zbh");
		from = new String[] { "id", "name" };
		to = new int[] { R.id.bm, R.id.name };
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
						showProgressDialog();
						Config.getExecutorService().execute(new Runnable() {

							@Override
							public void run() {
								getWebService("submit");
							}
						});

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
		if (s.equals("query")) {
			try {
				datalist = new ArrayList<Map<String, Object>>();
				JSONObject jsonObject = null;
				jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_KDG_YWCX_CSGD2", zbh, "uf_json_getdata",
						this);
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, Object> item = new HashMap<String, Object>();
						String timeff = "";
						item.put("textView1", getListItemIcon(i));
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
						item.put("ds", temp.getString("ds"));
						item.put("fwnr", temp.getString("fwnr"));
						item.put("kzzf1", temp.getString("kzzf1"));

						item.put("dygdh1_mc", temp.getString("dygdh1_mc"));
						item.put("dygdh2_mc", temp.getString("dygdh2_mc"));
						item.put("axdh", temp.getString("axdh"));
						item.put("ddh", temp.getString("ddh"));
						item.put("ddh_mc", temp.getString("ddh_mc"));
						item.put("yqsx", temp.getString("yqsx"));
						item.put("lxdh", temp.getString("lxdh"));

						item.put("kzsz4", temp.getString("kzsz4"));
						item.put("jsxh", temp.getString("jsxh"));
						item.put("jddz", temp.getString("jddz"));
						item.put("sfcs", temp.getString("sfcs"));
						item.put("wcsj", temp.getString("wcsj"));
						item.put("djzt2", temp.getString("djzt2"));

						item.put("csyybm", temp.getString("csyybm"));
						item.put("csyysm", temp.getString("csyysm"));

						item.put("kzzf3_bm", temp.getString("kzzf3_bm"));
						item.put("kzzf4_bm", temp.getString("kzzf4_bm"));
						item.put("kzzf5_bm", temp.getString("kzzf5_bm"));

						item.put("timemy", temp.getString("ddh_mc"));
						item.put("datemy", temp.getString("djzt2"));
						datalist.add(item);
					}

					data_csyy = new ArrayList<Map<String,String>>();
					jsonObject = callWebserviceImp.getWebServerInfo(
							"_PAD_KDG_CSYYLR_CX", "", "uf_json_getdata", this);
					flag = jsonObject.getString("flag");

					Map<String, String> item = new HashMap<String, String>();
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

				// 再提交服务报告
				String str = "";
				String typeStr = "az_fwbg_xg";

				typeStr = "wx_csyytx";
				str = zbh + "*PAM*" + DataCache.getinition().getUserId();
				str += "*PAM*";
				str += data_csyy.get(spinner_csyy.getSelectedItemPosition()).get("id");
				str += "*PAM*";
				str += et_csnr.getText().toString().trim();


				JSONObject json = this.callWebserviceImp.getWebServerInfo(
						"c#_PAD_KDG_ALL", str,
						DataCache.getinition().getUserId(), typeStr,
						"uf_json_setdata2", this);
				flag = json.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					Message msg = new Message();
					msg.what = Constant.SUCCESS;
					handler.sendMessage(msg);
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

	@SuppressLint("ResourceAsColor")
	private void loadData(){
		Map<String, Object> itemmap = datalist.get(0);
		lxdh = (String) itemmap.get("lxdh");
		((TextView) findViewById(R.id.tv_1)).setText(zbh);
		((TextView) findViewById(R.id.tv_2)).setText((String) itemmap.get("axdh"));
		((TextView) findViewById(R.id.tv_3)).setText((String) itemmap.get("sx"));
		((TextView) findViewById(R.id.tv_4)).setText((String) itemmap.get("qy"));
		((TextView) findViewById(R.id.tv_5)).setText((String) itemmap.get("xqmc"));
		((TextView) findViewById(R.id.tv_6)).setText((String) itemmap.get("xxdz"));
		((TextView) findViewById(R.id.tv_8)).setText((String) itemmap.get("bzsj"));
		((TextView) findViewById(R.id.tv_9)).setText((String) itemmap.get("yqsx"));
		((TextView) findViewById(R.id.tv_10)).setText((String) itemmap.get("lxdh"));
		((TextView) findViewById(R.id.tv_13)).setText((String) itemmap.get("gzxx"));
		((TextView) findViewById(R.id.tv_14)).setText((String) itemmap.get("sfcs"));
		((TextView) findViewById(R.id.tv_15)).setText((String) itemmap.get("wcsj"));
		((TextView) findViewById(R.id.tv_16)).setText((String) itemmap.get("djzt2"));
		((TextView) findViewById(R.id.tv_17)).setText((String) itemmap.get("bz"));
		((TextView) findViewById(R.id.tv_jddz)).setText((String) itemmap.get("jddz"));

		et_csnr.setText((String) itemmap.get("csyysm"));
		for (int i = 0; i < data_csyy.size(); i++) {
			Map<String, String> map = data_csyy.get(i);
			if (itemmap.get("csyybm").toString().equals(map.get("id"))) {
				spinner_csyy.setSelection(i);
			}
		}
		final Map<String, Object> map0 = ServiceReportCache.getObjectdata().get(ServiceReportCache.getIndex());
		String csshzt_bm = (String) map0.get("csshzt_bm");
		if("2".equals(csshzt_bm)){
			spinner_csyy.setEnabled(false);
			et_csnr.setTextColor(R.color.gray);
			et_csnr.setEnabled(false);
		}

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
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case Constant.FAIL:
					dialogShowMessage_P("失败，错误标识：" + flag, null);
					break;
				case Constant.SUCCESS:
					dialogShowMessage_P("提交成功",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface face,
											int paramAnonymous2Int) {
							onBackPressed();
						}
					});
					break;
				case Constant.NETWORK_ERROR:

					dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
					break;
				case Constant.NUM_6:
					SimpleAdapter adapter = new SimpleAdapter(CsgdcxShowKdg.this,
							data_csyy, R.layout.spinner_item, from, to);
					spinner_csyy.setAdapter(adapter);
					loadData();

					break;
				case Constant.NUM_7:
					break;
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	};


}
