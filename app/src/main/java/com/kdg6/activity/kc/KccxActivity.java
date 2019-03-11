package com.kdg6.activity.kc;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.kdg6.R;
import com.kdg6.activity.FrameActivity;
import com.kdg6.activity.main.MainActivity;
import com.kdg6.cache.DataCache;
import com.kdg6.cache.ServiceReportCache;
import com.kdg6.common.Constant;
import com.kdg6.utils.Config;

/**
 * 当前库存查询
 *
 * @author
 */
@SuppressLint("HandlerLeak")
public class KccxActivity extends FrameActivity {

	private String flag, name;
	private EditText et_search;
	private ListView listView;
	private SimpleAdapter adapter;
	private List<Map<String, Object>> data, currdata;
	private String[] from;
	private int[] to;
	private String rg1,rg2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_dispatchinginformationreceiving);
		initVariable();
		initView();
		initListeners();

	}

	@Override
	protected void initVariable() {

		listView = (ListView) findViewById(R.id.listView);

		et_search = (EditText) findViewById(R.id.et_search);
		et_search.setHint("请输入货品名称");
		from = new String[] {"jgmc_1","hpmc","dqkc" };
		to = new int[] { R.id.tv_cwmc, R.id.tv_kfmc, R.id.tv_dqkc };

		rg1 = getIntent().getStringExtra("rg1");
		rg2 = getIntent().getStringExtra("rg2");

		findViewById(R.id.ll_filter).setVisibility(View.VISIBLE);
	}

	@Override
	protected void initView() {
		title.setText(DataCache.getinition().getTitle());
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
				for(int i=0;i<parent.getChildCount();i++){
					parent.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.white));
				}
				view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//				ServiceReportCache.setIndex(arg2);
//				if (arg2 >= 0) {
//
//					Intent intent = new Intent(KccxActivity.this,
//							KccxShowActivity.class);
//					startActivity(intent);
//				}

			}
		});

		et_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(final CharSequence s, int start,
									  int before, int count) {
				name = s.toString();
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {

						getWebService("getdata");
					}
				});
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				getWebService("query");
			}
		});
	}

	@Override
	protected void getWebService(String s) {

		if ("query".equals(s) && !backboolean) {
			try {
				data = new ArrayList<Map<String, Object>>();
				data = new ArrayList<Map<String, Object>>();
				String userid = DataCache.getinition().getUserId();
				String cs,sqlid;
				if("1".equals(rg2)){
					sqlid = "_PAD_HPKF_DQBGZ";
					cs = userid+"*"+rg1;
				}else{
					sqlid = "_PAD_HPKF_DQBGZ1";
					cs = userid+"*"+rg1;
				}
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						sqlid, cs, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, Object> item = new HashMap<String, Object>();
						item.put("num", "");
						item.put("textView1", getListItemIcon(i));
						item.put("sqjc", temp.getString("sqjc"));// 上期结存
						item.put("jgmc_1", temp.getString("cwmc"));// 仓位名称
						item.put("dqkc", temp.getString("dqkc"));// 当前库存
						item.put("hpmc", temp.getString("hpmc"));// 货品名称
						item.put("jgmc", temp.getString("kfmc"));// 库房名称

						data.add(item);
					}
					currdata = data;
					ServiceReportCache.setObjectdata(currdata);
					Message msg = new Message();
					msg.what = Constant.SUCCESS;// 成功
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = Constant.FAIL;// 失败
					handler.sendMessage(msg);
				}

			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}

		} else {
			// 直接加载
			Message msg = new Message();
			msg.what = Constant.SUCCESS;// 成功
			handler.sendMessage(msg);
		}

		if ("getdata".equals(s)) {
			currdata = new ArrayList<Map<String, Object>>();
			try {
				for (int i = 0; i < data.size(); i++) {
					Map<String, Object> map = data.get(i);
					if (((String) map.get("hpmc")).indexOf(name) != -1) {
						currdata.add(map);
					}
				}
				ServiceReportCache.setObjectdata(currdata);
				Message msg = new Message();
				msg.what = Constant.SUCCESS;
				handler.sendMessage(msg);
			} catch (Exception e) {
				Message msg = new Message();
				msg.what = Constant.SUCCESS;
				handler.sendMessage(msg);
			}
		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case Constant.NETWORK_ERROR:
					dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
					break;

				case Constant.SUCCESS:
					adapter = new SimpleAdapter(KccxActivity.this,
							ServiceReportCache.getObjectdata(),
							R.layout.listview_item_kcxx, from, to);
					listView.setAdapter(adapter);
					break;

				case Constant.FAIL:
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
