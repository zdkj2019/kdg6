package com.kdg6.activity.kc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.CharacterStyle;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.kdg6.R;
import com.kdg6.Parser.JSONObjectParser;
import com.kdg6.activity.FrameActivity;
import com.kdg6.activity.main.MainActivity;
import com.kdg6.cache.DataCache;
import com.kdg6.cache.ServiceReportCache;
import com.kdg6.common.Constant;
import com.kdg6.utils.Config;
import com.kdg6.utils.DateUtil;

/**
 * 库存盘点
 *
 * @author hs
 */
@SuppressLint({ "HandlerLeak", "WorldReadableFiles" })
public class KcpdActivity extends FrameActivity {

	private EditText edit_gdbz,edit_sgdh;
	private Button confirm, cancel;
	private LinearLayout ll_mx, ll_mx_item;
	private ArrayList<Map<String, String>> data_mx;
	private String flag = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kcpd);
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

		edit_gdbz = (EditText) findViewById(R.id.edit_gdbz);
		edit_sgdh = (EditText) findViewById(R.id.edit_sgdh);
		ll_mx = (LinearLayout) findViewById(R.id.ll_mx);
		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());

	}

	@Override
	protected void initListeners() {

		topBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				if(!isNotNull(edit_sgdh)){
//					toastShowMessage("手工单号不能为空");
//					return;
//				}
//
				boolean flag = true;
//				int count = ll_mx.getChildCount();
//				for (int i = 0; i < count; i++) {
//					ll_mx_item = (LinearLayout) ll_mx.getChildAt(i);
//					EditText et_sjsl = (EditText) ll_mx_item.findViewById(R.id.et_sjsl);
//					if(!isNotNull(et_sjsl)){
//						flag = false;
//						toastShowMessage("实际数量不能为空");
//						break;
//					}
//				}

				if(flag){
					showProgressDialog();
					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {
							getWebService("submit");

						}
					});
				}
			}
		});
	}

	@SuppressLint("InflateParams")
	@Override
	protected void getWebService(String s) {

		if ("query".equals(s)) {
			try {
				data_mx = new ArrayList<Map<String, String>>();
				String userid = DataCache.getinition().getUserId();
				JSONObject jsonObject_mx = callWebserviceImp.getWebServerInfo(
						"_PAD_CCGL_PDCX1", userid+"*"+userid, "uf_json_getdata", this);
				flag = jsonObject_mx.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					try {
						JSONArray array = jsonObject_mx.getJSONArray("tableA");
						data_mx = (ArrayList<Map<String, String>>) JSONObjectParser
								.jsonToListByJson(array);
						ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
						for (int i = 0; i < data_mx.size(); i++) {
							Map<String, String> newMap = new HashMap<String, String>();
							Map<String, String> temp = data_mx.get(i);
							newMap.put("kfbm", temp.get("kfbm"));
							newMap.put("cwbm", temp.get("cwbm"));
							newMap.put("hpbm", temp.get("hpbm"));
							newMap.put("kfmc", temp.get("kfmc"));
							newMap.put("cwmc", temp.get("cwmc"));
							newMap.put("hpmc", temp.get("hpmc"));
							newMap.put("dqkc", temp.get("dqkc"));
							data.add(newMap);
						}
						data_mx = data;
						Message msg = new Message();
						msg.what = Constant.NUM_6;
						handler.sendMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
						Message msg = new Message();
						msg.what = Constant.FAIL;// 解析出错
						handler.sendMessage(msg);
					}
				} else {
					Message msg = new Message();
					msg.what = Constant.FAIL;// 解析出错
					handler.sendMessage(msg);
				}

			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}

		if ("submit".equals(s)) {
			try {
				String sql =DataCache.getinition().getUserId()+"*PAM*"+ edit_sgdh.getText().toString()+"*PAM*"+ edit_gdbz.getText().toString();
				String mxStr = "";
				int count = ll_mx.getChildCount();
				for (int i = 0; i < count; i++) {
					Map<String, String> map = data_mx.get(i);
					ll_mx_item = (LinearLayout) ll_mx.getChildAt(i);
					EditText et_sjsl = (EditText) ll_mx_item.findViewById(R.id.et_sjsl);
					mxStr = mxStr+map.get("kfbm")+"#@#"+map.get("cwbm")+"#@#"+map.get("hpbm")+"#@#"+map.get("dqkc")+"#@#"+et_sjsl.getText().toString().trim()+"#^#";
				}
				if(!"".equals(mxStr)){
					mxStr = mxStr.substring(0, mxStr.length()-3);
				}
				sql = sql+"*PAM*"+mxStr;
				flag = this.callWebserviceImp.getWebServerInfo(
						"c#_PAD_CCGL_HPGL_KCPD",
						sql,
						DataCache.getinition().getUserId(),
						DataCache.getinition().getUserId(),
						"uf_json_setdata2", this).getString("flag");
				if(Integer.parseInt(flag)>0){
					Message msg = new Message();
					msg.what = Constant.SUCCESS;
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

	}

	private Handler handler = new Handler() {
		@SuppressLint("InflateParams")
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case Constant.NETWORK_ERROR:
					dialogShowMessage_P("网络连接错误，请检查您的网络是否正常", null);
					break;
				case Constant.SUCCESS:
					dialogShowMessage_P("提交成功",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
													int paramAnonymous2Int) {
									onBackPressed();
								}
							});
					break;
				case Constant.FAIL:
					dialogShowMessage_P("提交失败，返回"+flag,null);
					break;
				case Constant.NUM_6:
					ll_mx.removeAllViews();
					for (int i = 0; i < data_mx.size(); i++) {
						ll_mx_item = (LinearLayout) LayoutInflater.from(
								getApplicationContext()).inflate(
								R.layout.listview_kfpd_item, null);

						Map<String, String> map = data_mx.get(i);
						((TextView) ll_mx_item.findViewById(R.id.tv_kfmc))
								.setText(map.get("kfmc"));
						((TextView) ll_mx_item.findViewById(R.id.tv_cwmc))
								.setText(map.get("cwmc"));
						((TextView) ll_mx_item.findViewById(R.id.tv_hpmc))
								.setText(map.get("hpmc"));
						((TextView) ll_mx_item.findViewById(R.id.tv_dqkc))
								.setText(map.get("dqkc"));
						ll_mx.addView(ll_mx_item);
					}
					break;
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	};

}
