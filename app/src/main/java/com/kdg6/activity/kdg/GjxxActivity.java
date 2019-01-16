package com.kdg6.activity.kdg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kdg6.R;
import com.kdg6.activity.FrameActivity;
import com.kdg6.activity.main.MainActivity;
import com.kdg6.cache.DataCache;
import com.kdg6.cache.ServiceReportCache;
import com.kdg6.common.Constant;
import com.kdg6.utils.Config;
/**
 * 快递柜-柜机详情
 * @author zdkj
 *
 */
public class GjxxActivity extends FrameActivity {

	private Button confirm,cancel;
	private String flag,gjbm,type="1",message;
	private ArrayList<Map<String, String>> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_gjxx);
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

		title.setText("柜机详情");
		gjbm = getIntent().getStringExtra("gjbm");

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
						onBackPressed();
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
				data = new ArrayList<Map<String,String>>();
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_YWCX_WX_SBXX", gjbm, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("sblx_mc", temp.getString("sblx_mc"));
						item.put("qx_mc", temp.getString("qx_mc"));
						item.put("wdbmwd_mc", temp.getString("wdbmwd_mc"));
						item.put("jddz", temp.getString("jddz"));
						item.put("xxdz", temp.getString("xxdz"));
						item.put("kzsz3", temp.getString("kzsz3"));
						item.put("kzsz4", temp.getString("kzsz4"));
						data.add(item);
					}

					Message msg = new Message();
					msg.what = Constant.SUCCESS;
					handler.sendMessage(msg);
				} else {
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
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case Constant.FAIL:
					dialogShowMessage_P("失败，" + flag, null);
					break;
				case Constant.SUCCESS:
					if(data!=null&&data.size()>0){
						Map<String, String> map = data.get(0);
						((TextView) findViewById(R.id.tv_1)).setText(map.get("sblx_mc"));
						((TextView) findViewById(R.id.tv_2)).setText(map.get("qx_mc"));
						((TextView) findViewById(R.id.tv_3)).setText(map.get("wdbmwd_mc"));
						((TextView) findViewById(R.id.tv_4)).setText(map.get("jddz"));
						((TextView) findViewById(R.id.tv_5)).setText(map.get("xxdz"));
						((TextView) findViewById(R.id.tv_6)).setText(map.get("kzsz3"));
						((TextView) findViewById(R.id.tv_7)).setText(map.get("kzsz4"));
					}

					break;
				case Constant.NETWORK_ERROR:
					dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
					break;
			}
			if(progressDialog!=null){
				progressDialog.dismiss();
			}
		}
	};

}