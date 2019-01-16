package com.kdg6.activity.js;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.kdg6.R;
import com.kdg6.activity.FrameActivity;
import com.kdg6.activity.main.MainActivity;
import com.kdg6.cache.DataCache;
import com.kdg6.cache.ServiceReportCache;
import com.kdg6.utils.Config;

/**
 * 应收款list
 *
 * @author
 */
@SuppressLint("HandlerLeak")
public class YingShouKuanList extends FrameActivity {

	private String flag;
	private List<Map<String, String>> data;
	private TextView tv_dqlj,tv_msg;
	private String msg_str = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_yigshoukuan);
		initVariable();
		initView();
		initListeners();
		if (!backboolean) {
			showProgressDialog();
		}
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				getWebService("query");
			}
		});

	}

	@Override
	protected void initVariable() {

	}

	@Override
	protected void initView() {
		data = new ArrayList<Map<String, String>>();
		title.setText(DataCache.getinition().getTitle());
		tv_dqlj = (TextView) findViewById(R.id.tv_dqlj);
		tv_msg = (TextView) findViewById(R.id.tv_msg);
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

	}

	@Override
	protected void getWebService(String s) {

		if ("query".equals(s) && !backboolean) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_DQYSK_SM", DataCache.getinition().getUserId(),
						"uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					JSONObject temp = jsonArray.getJSONObject(0);
					msg_str = temp.getString("bz");
				}

				jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_DQZCX", DataCache.getinition().getUserId(),
						"uf_json_getdata", this);

				flag = jsonObject.getString("flag");
				jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("dqlj", formatData(temp.getString("dqlj")));
						data.add(item);
					}

					Message msg = new Message();
					msg.what = SUCCESSFUL;// 成功
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = FAIL;// 失败
					handler.sendMessage(msg);
				}

			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}

		} else {
			// 直接加载
			Message msg = new Message();
			msg.what = SUCCESSFUL;// 成功
			handler.sendMessage(msg);
		}

	}

	private String formatData(String val){
		try {
			val = Double.valueOf(val).toString()+"0";
		} catch (Exception e) {
			e.printStackTrace();
		}

		return val;
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.putExtra("currType", 3);
		startActivity(intent);
		finish();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case NETWORK_ERROR:
					dialogShowMessage_P("网络连接出错，请检查你的网络设置", null);
					break;

				case SUCCESSFUL:
					try {
						Map<String, String> map = data.get(0);
						tv_dqlj.setText(map.get("dqlj")+"元");
						if(!"".equals(msg_str)){
							tv_msg.setText("ps:"+msg_str);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

					break;

				case FAIL:
					dialogShowMessage_P("查询失败，数据错误！",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
													int which) {
									onBackPressed();
								}

							});
					break;

			}
			if (!backboolean) {
				progressDialog.dismiss();
			}
		}

	};

}
