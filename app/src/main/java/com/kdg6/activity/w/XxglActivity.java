package com.kdg6.activity.w;

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
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.kdg6.R;
import com.kdg6.activity.FrameActivity;
import com.kdg6.activity.login.LoginActivity;
import com.kdg6.activity.main.MainActivity;
import com.kdg6.cache.DataCache;
import com.kdg6.cache.ServiceReportCache;
import com.kdg6.utils.Config;

/**
 * 消息管理列表
 *
 * @author
 */
@SuppressLint("HandlerLeak")
public class XxglActivity extends FrameActivity {

	private String flag;
	private ListView listView;
	private SimpleAdapter adapter;
	private List<Map<String, String>> data;
	private String[] from;
	private int[] to;
	private SharedPreferences spf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_dispatchinginformationreceiving);
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

		listView = (ListView) findViewById(R.id.listView);
		data = new ArrayList<Map<String, String>>();

		from = new String[] { "textView1", "zbh", "num", "var_kzzd1", "zdrq",
				"xxzt" };
		to = new int[] { R.id.textView1, R.id.tv_0, R.id.tv_1, R.id.tv_2,
				R.id.tv_3, R.id.tv_4 };

	}

	@Override
	protected void initView() {
		title.setText(DataCache.getinition().getTitle());
		spf = getSharedPreferences("loginsp", LoginActivity.MODE_PRIVATE);
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

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int arg2,
									long id) {

				ServiceReportCache.setIndex(arg2);
				if (arg2 >= 0) {
					int num = DataCache.getinition().getZzxx_num();
					if (num > 0) {
						DataCache.getinition().setZzxx_num(num - 1);
					}

					Intent intent = new Intent(XxglActivity.this,
							XxglShowActivity.class);
					startActivity(intent);
				}

			}
		});
	}

	@Override
	protected void getWebService(String s) {

		if ("query".equals(s) && !backboolean) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_YGXX_XXNR", spf.getString("userId", ""),
						"uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("num","");
						item.put("textView1", getListItemIcon(i) + "");
						item.put("zdrq", temp.getString("zdrq"));
						item.put("var_kzzd1", temp.getString("var_kzzd1"));
						item.put("ryid", temp.getString("ryid"));
						item.put("xxzt", temp.getString("xxzt"));
						item.put("zbh", temp.getString("zbh"));
						item.put("bz", temp.getString("bz"));
						data.add(item);
					}
					ServiceReportCache.setData(data);
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

	private String formatData(String val) {
		try {
			val = Double.valueOf(val).toString() + "0";
		} catch (Exception e) {
			e.printStackTrace();
		}

		return val;
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.putExtra("currType", 5);
		startActivity(intent);
		finish();
	}

	private class CurrAdapter extends SimpleAdapter {

		public CurrAdapter(Context context,
						   List<? extends Map<String, ?>> data, int resource,
						   String[] from, int[] to) {
			super(context, data, resource, from, to);
		}

		@Override
		public View getView(final int position, View convertView,
							final ViewGroup parent) {
			final View view = super.getView(position, convertView, parent);

			TextView tv_4 = (TextView) view.findViewById(R.id.tv_4);
			TextView tv_2 = (TextView) view.findViewById(R.id.tv_2);
			if ("0".equals(tv_4.getText().toString().trim())) {
				TextPaint tp = tv_2.getPaint();
				tp.setFakeBoldText(true);
			}
			return view;
		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case NETWORK_ERROR:
					dialogShowMessage_P("网络连接出错，请检查你的网络设置",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
													int which) {
									onBackPressed();
								}

							});
					break;

				case SUCCESSFUL:
					adapter = new CurrAdapter(XxglActivity.this,
							ServiceReportCache.getData(),
							R.layout.listview_item_zyxx, from, to);
					listView.setAdapter(adapter);
					break;

				case FAIL:
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
			if (!backboolean) {
				progressDialog.dismiss();
			}
		}

	};

}
