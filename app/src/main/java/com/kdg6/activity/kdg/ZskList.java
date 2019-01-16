package com.kdg6.activity.kdg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.kdg6.R;
import com.kdg6.activity.FrameActivity;
import com.kdg6.common.Constant;
import com.kdg6.utils.Config;

/**
 * 快递柜-知识库list
 *
 * @author zdkj
 *
 */
public class ZskList extends FrameActivity {

	private Button confirm, cancel;
	private ListView listview;
	private EditText et_search;
	private List<Map<String, String>> data, data_all;
	private String[] from;
	private int[] to;
	private String name = "",zlbm="",flag,currsjbm="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_zsklist);

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

	protected void initVariable() {
		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("返回");
		cancel.setText("上一级");

		title.setText("知识库");
		listview = (ListView) findViewById(R.id.listview);
		et_search = (EditText) findViewById(R.id.et_search);
		from = new String[] { "zsdbm", "zsdmc" };
		to = new int[] { R.id.tv_id, R.id.tv_name };
		zlbm = getIntent().getStringExtra("zlbm");
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	protected void initListeners() {

		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(data!=null&&data.size()>0){
					currsjbm = data.get(0).get("zsdbm_sj");
					if("00".equals(currsjbm)){
						toastShowMessage("没有上级了");
					}else{
						showProgressDialog();
						Config.getExecutorService().execute(new Runnable() {

							@Override
							public void run() {

								getWebService("querySj");
							}
						});
					}
				}else{
					onBackPressed();
				}

			}
		});

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

				if (position >= 0) {
					String zsdbm = data.get(position).get("zsdbm");
					List<Map<String, String>> currdata = new ArrayList<Map<String,String>>();
					for (int i = 0; i < data_all.size(); i++) {
						Map<String, String> map = data_all.get(i);
						if (zsdbm.equals(map.get("zsdbm_sj"))) {
							currdata.add(map);
						}
					}
					if(currdata.size()>0){
						data = currdata;
						Message msg = new Message();
						msg.what = Constant.SUCCESS;
						hander.sendMessage(msg);
					}else{
						toastShowMessage("没有子项了");
					}

				}
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

		topBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	@Override
	protected void getWebService(String s) {

		if("query".equals(s)){

			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_YWCX_ZSDNR", zlbm, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				data_all = new ArrayList<Map<String, String>>();
				data = new ArrayList<Map<String,String>>();
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("zsdbm", temp.getString("zsdbm"));
						item.put("zsdmc", "\u3000\u3000"+temp.getString("zsdnr"));
						item.put("zsdbm_sj", temp.getString("zsdbm_sj"));
						item.put("dsxh", temp.getString("dsxh"));
						item.put("xsxh", temp.getString("xsxh"));
						data_all.add(item);
					}
					for (int i = 0; i < data_all.size(); i++) {
						Map<String, String> map = data_all.get(i);
						if ("00".equals(map.get("zsdbm_sj"))) {
							data.add(map);
						}
					}
					Message msg = new Message();
					msg.what = Constant.SUCCESS;// 成功
					hander.sendMessage(msg);
				} else {
					flag = "没有对应知识点";
					Message msg = new Message();
					msg.what = Constant.NUM_6;// 失败
					hander.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				hander.sendMessage(msg);
			}

		}

		if("querySj".equals(s)){

			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_YWCX_ZSDNR_SJ", currsjbm+"*"+zlbm, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				data = new ArrayList<Map<String,String>>();
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("zsdbm", temp.getString("zsdbm"));
						item.put("zsdmc", "\u3000\u3000"+temp.getString("zsdnr"));
						item.put("zsdbm_sj", temp.getString("zsdbm_sj"));
						item.put("dsxh", temp.getString("dsxh"));
						item.put("xsxh", temp.getString("xsxh"));
						data.add(item);
					}
					Message msg = new Message();
					msg.what = Constant.SUCCESS;// 成功
					hander.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = Constant.FAIL;// 失败
					hander.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				hander.sendMessage(msg);
			}

		}

		if ("getdata".equals(s)) {
			data = new ArrayList<Map<String, String>>();
			try {
				for (int i = 0; i < data_all.size(); i++) {
					Map<String, String> map = data_all.get(i);
					if (map.get("zsdmc").indexOf(name) != -1) {
						data.add(map);
					}
				}
				Message msg = new Message();
				msg.what = Constant.SUCCESS;
				hander.sendMessage(msg);
			} catch (Exception e) {
				Message msg = new Message();
				msg.what = Constant.SUCCESS;
				hander.sendMessage(msg);
			}
		}

	}

	private Handler hander = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case Constant.FAIL:
					dialogShowMessage_P("失败，" + flag, null);
					break;
				case Constant.NETWORK_ERROR:
					dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
					break;
				case Constant.SUCCESS:
					SimpleAdapter adapter = new SimpleAdapter(
							getApplicationContext(), data,
							R.layout.listview_item_filterdata1, from, to);
					listview.setAdapter(adapter);
					break;
				case Constant.NUM_6:
					dialogShowMessage_P(flag, null);
					break;
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}

	};
}
