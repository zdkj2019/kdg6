package com.kdg6.activity.kdg;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.kdg6.R;
import com.kdg6.activity.FrameActivity;
import com.kdg6.activity.util.BaiduMapActivity;
import com.kdg6.cache.DataCache;
import com.kdg6.cache.ServiceReportCache;
import com.kdg6.common.Constant;
import com.kdg6.utils.Config;
import com.kdg6.utils.DateUtil;
import com.kdg6.zxing.CaptureActivity;

/**
 * 快递柜-知识库
 *
 * @author zdkj
 *
 */
public class ZskActivity extends FrameActivity {

	private Button confirm, cancel;
	private Spinner spinner_gzdl, spinner_gzzl, spinner_gzxl;
	private List<Map<String, String>> data_gzbm, data_all, gzbm_2_list,gzbm_3_list;
	private String flag, zbh, message,dlbm,zlbm,xlbm;
	private String[] from;
	private int[] to;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_zsk);
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
		cancel.setText("问题定位");
	}

	@Override
	protected void initView() {

		title.setText("知识库");
		spinner_gzdl = (Spinner) findViewById(R.id.spinner_gzdl);
		spinner_gzzl = (Spinner) findViewById(R.id.spinner_gzzl);
		spinner_gzxl = (Spinner) findViewById(R.id.spinner_gzxl);

		data_gzbm = new ArrayList<Map<String, String>>();
		gzbm_2_list = new ArrayList<Map<String, String>>();
		gzbm_3_list = new ArrayList<Map<String, String>>();

		from = new String[] { "id", "name" };
		to = new int[] { R.id.bm, R.id.name };

		try {
			final Map<String, Object> itemmap = ServiceReportCache.getObjectdata().get(ServiceReportCache.getIndex());

			zbh = (String) itemmap.get("zbh");
			dlbm = (String) itemmap.get("kzzf3_bm");
			zlbm = (String) itemmap.get("kzzf4_bm");
			xlbm = (String) itemmap.get("kzzf5_bm");
		} catch (Exception e) {
			e.printStackTrace();
		}
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
						String zlbm = ((Map<String, String>) spinner_gzzl.getSelectedItem()).get("id").trim();
						if("".equals(zlbm)){
							toastShowMessage("请选择故障模块");
							return;
						}
						Intent intent = new Intent(getApplicationContext(),ZskList.class);
						intent.putExtra("zlbm", zlbm);
						startActivityForResult(intent, 1);
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

				SimpleAdapter adapter = new SimpleAdapter(ZskActivity.this,
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

				SimpleAdapter adapter = new SimpleAdapter(ZskActivity.this,
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

		spinner_gzdl.setOnItemSelectedListener(onItemSelectedListener_gzdl);// 故障大类
		spinner_gzzl.setOnItemSelectedListener(onItemSelectedListener_gzzl);// 故障中类

	}

	public void loadGzdl(){


		String select_id = data_gzbm.get(1).get("id");
		gzbm_2_list.clear();
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", "     ");
		map.put("name", " ");
		gzbm_2_list.add(map);
		// 选择的大类 设置中类
		for (int i = 0; i < data_all.size(); i++) {

			String kzzf1 = data_all.get(i).get("kzzf1");
			if ("01".equals(kzzf1)) {
				// 相等添加到维护厂商显示的list里
				gzbm_2_list.add(data_all.get(i));
			}
		}

		SimpleAdapter adapter = new SimpleAdapter(ZskActivity.this,
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
	protected void getWebService(String s) {

		if(s.equals("query")){
			try {
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
						item.put("kzzf1", temp.getString("kzzf1"));
						if ("00".equals(sjbm)) {
							data_gzbm.add(item);
						}
						data_all.add(item);
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
				String typeStr = "wx_gzdw";
				message = "定位成功！";
				String str = zbh + "*PAM*" + DataCache.getinition().getUserId();
				str += "*PAM*";
				str += ((Map<String, String>) spinner_gzdl.getSelectedItem()).get("id").trim();
				str += "*PAM*";
				str += ((Map<String, String>) spinner_gzzl.getSelectedItem()).get("id").trim();
				str += "*PAM*";
				str += ((Map<String, String>) spinner_gzxl.getSelectedItem()).get("id").trim();

				JSONObject json = this.callWebserviceImp.getWebServerInfo(
						"c#_PAD_KDG_ALL", str, typeStr, typeStr,
						"uf_json_setdata2", this);
				flag = json.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					Message msg = new Message();
					msg.what = Constant.SUCCESS;
					handler.sendMessage(msg);
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

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
				case Constant.FAIL:
					dialogShowMessage_P("失败，" + flag, null);
					break;
				case Constant.SUCCESS:
					dialogShowMessage_P(message,new DialogInterface.OnClickListener() {
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
					SimpleAdapter adapter = new SimpleAdapter(ZskActivity.this,
							data_gzbm, R.layout.spinner_item, from, to);
					spinner_gzdl.setAdapter(adapter);

					spinner_gzdl.setSelection(1);
					loadGzdl();

					break;
			}

			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	};

}