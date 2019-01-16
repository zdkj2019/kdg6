package com.kdg6.activity.esp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.kdg6.R;
import com.kdg6.activity.FrameActivity;
import com.kdg6.cache.DataCache;
import com.kdg6.utils.Config;

public class AddParts extends FrameActivity{

	private Button confirma, cancela, add;
	private String flag, number, bz, machineName, machineId, pgdh,xlbm;
	private List<Map<String, String>> data, data_listView;
	private String[] from, from2;
	private int[] to, to2;
	private Spinner bjmc;
	private ListView listView;
	private ArrayList<String> sqlList;
	private JSONObject jsonObject;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_addparts);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				getWebService("xl");
			}
		});
	}


	@Override
	protected void initVariable() {

		from = new String[] { "id", "name" };
		to = new int[] { R.id.bm, R.id.name };
		from2 = new String[] { "machine", "number","sfhs" };
		to2 = new int[] { R.id.machine, R.id.number, R.id.sfhs };

		add = (Button) findViewById(R.id.add);
		cancela = (Button) findViewById(R.id.cancela);
		confirma = (Button) findViewById(R.id.confirma);

		data = new ArrayList<Map<String, String>>();
		data_listView = new ArrayList<Map<String, String>>();

		listView = (ListView) findViewById(R.id.listView);

		sqlList = new ArrayList<String>();

		title.setText("新增配件列表");

	}

	@Override
	protected void initView() {

		Intent intent = this.getIntent();
		pgdh = intent.getStringExtra("oddnumber");
		ArrayList<String> hpdata = intent.getStringArrayListExtra("hpdata");
		HashMap<String, String> title = new HashMap<String, String>();
		title.put("machine", "配件名称");
		title.put("number", "数量");
		title.put("sfhs", "是否回收");


		data_listView.add(title);
		if(hpdata!=null){
			for(int i=0;i<hpdata.size();i++){
				String[] hps = hpdata.get(i).split(",");
				HashMap<String, String> temp = new HashMap<String, String>();
				temp.put("id",hps[0]);
				temp.put("machine", hps[1]);
				temp.put("number", hps[2]);
				temp.put("sfhs", hps[3]);

				data_listView.add(temp);
			}
		}
		SimpleAdapter adapter = new SimpleAdapter(
				AddParts.this, data_listView,
				R.layout.machine_listview_item, from2, to2);

		listView.setAdapter(adapter);
	}

	@Override
	protected void initListeners() {
		OnClickListener backonClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		};

		topBack.setOnClickListener(backonClickListener);

		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {

				switch (v.getId()) {

					case R.id.confirma:

						Intent intent = new Intent();
						intent.putStringArrayListExtra("hpsql", generateSql());
						intent.putStringArrayListExtra("hpdata", getHpdata());
						setResult(RESULT_OK, intent);
						finish();
						break;
					case R.id.add:
						Intent intent1 = new Intent(AddParts.this,AddPartsAdd.class);
						startActivityForResult(intent1,21);
						//showDialog();
						break;

					case R.id.cancela:
						Intent intent2 = new Intent();
						intent2.putStringArrayListExtra("hpsql", null);
						intent2.putStringArrayListExtra("hpdata", null);
						setResult(RESULT_OK, intent2);
						finish();
						break;

				}
			}
		};

		add.setOnClickListener(onClickListener);
		cancela.setOnClickListener(onClickListener);
		confirma.setOnClickListener(onClickListener);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 21 && resultCode == 21 && data != null) {
			HashMap<String, String> temp = new HashMap<String, String>();
			temp.put("machine", data.getStringExtra("name"));
			temp.put("number", data.getStringExtra("num"));
			temp.put("sfhs", data.getStringExtra("sfhs"));
			temp.put("id", data.getStringExtra("id"));
			data_listView.add(temp);
			SimpleAdapter adapter = new SimpleAdapter(
					AddParts.this, data_listView,
					R.layout.machine_listview_item, from2, to2);

			listView.setAdapter(adapter);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void getWebService(String s) {

		if ("dl".equals(s)) {
			try {
				jsonObject = callWebserviceImp.getWebServerInfo("_PAD_SBPJDL", "", "uf_json_getdata",getApplicationContext());
				//{"tableA":[{"hplbbm":"001001","hplbmc":"自助缴费机"},{"hplbbm":"001002","hplbmc":"自动售货机"},{"hplbbm":"001003","hplbmc":"排队叫号机"},{"hplbbm":"001004","hplbmc":"广告机"},{"hplbbm":"001005","hplbmc":"查询机"},{"hplbbm":"002001","hplbmc":"导航设备"},{"hplbbm":"002002","hplbmc":"EPOS机"}],"flag":"7","costTime":342}
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					Config.getExecutorService().execute(new Runnable() {
						@Override
						public void run() {
							Config.writeFile("_PAD_SBPJDL",jsonObject.toString(), getApplicationContext());
						}
					});
					getWebService("zl");
				} else {
					Message msg = new Message();
					msg.what = -2;// 失败
					handler.sendMessage(msg);
				}

			} catch (Exception e) {
				Message msg = new Message();
				msg.what = NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}

		}

		if ("zl".equals(s)) {
			try {
				jsonObject = callWebserviceImp.getWebServerInfo("_PAD_SBPJZL", "", "uf_json_getdata",getApplicationContext());
				//{"tableA":[{"sjlbbm":"001001","hplbbm":"001001001","hplbmc":"显示器"},{"sjlbbm":"001001","hplbbm":"001001002","hplbmc":"主板"}],"flag":"2","costTime":276}
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					Config.getExecutorService().execute(new Runnable() {
						@Override
						public void run() {
							Config.writeFile("_PAD_SBPJZL",jsonObject.toString(), getApplicationContext());
						}
					});
					getWebService("xl");
				} else {
					Message msg = new Message();
					msg.what = -2;// 失败
					handler.sendMessage(msg);
				}

			} catch (Exception e) {
				Message msg = new Message();
				msg.what = NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}

		}

		if ("xl".equals(s)) {
			try {
				String cs = DataCache.getinition().getUserId()+"*"+DataCache.getinition().getUserId();
				jsonObject = callWebserviceImp.getWebServerInfo("_PAD_SBPJXL", cs, "uf_json_getdata",getApplicationContext());
				//{"tableA":[{"hplbbm":"001001001","hpmc":"三星电脑显示器","hpbm":"0010010010001"},{"hplbbm":"001001001","hpmc":"联想显示器","hpbm":"0010010010002"},{"hplbbm":"001001001","hpmc":"长城显示器","hpbm":"0010010010003"},{"hplbbm":"001001001","hpmc":"HP显示器","hpbm":"0010010010004"},{"hplbbm":"001001002","hpmc":"电脑显","hpbm":"0010010020001"},{"hplbbm":"001001001","hpmc":"长城显","hpbm":"0010010010005"}],"flag":"6","costTime":342}
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					Config.getExecutorService().execute(new Runnable() {
						@Override
						public void run() {
							Config.writeFile("_PAD_SBPJXL",jsonObject.toString(), getApplicationContext());
						}
					});
					Message msg = new Message();
					msg.what = 2;// 成功
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = -2;// 失败
					handler.sendMessage(msg);
				}

			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}

		}
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
					dialogShowMessage_P("设备信息添加成功", null);
					break;
				case 2:
//				SimpleAdapter adapter = new SimpleAdapter(
//						AddParts.this, data, R.layout.spinner_item,
//						from, to);
//				bjmc.setAdapter(adapter);
					break;

				case FAIL:
					dialogShowMessage_P("设备信息添加失败，请检查网络后重试", null);
					break;
				case -2:
					dialogShowMessage_P("查询失败，没有数据", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface face,
											int paramAnonymous2Int) {
							onBackPressed();
						}
					});
					break;

			}
			progressDialog.dismiss();
		}

	};


	private ArrayList<String> generateSql() {

		for (int i = 1; i < data_listView.size(); i++) {

			String sql = "insert into shgl_ywgl_fwbgsbjb (mxh, sxh, zbh, bjmc, sl, bz) values"
					+ "("
					+ "'"
					+ "%s"
					+ "'"
					+ ","
					+ 1
					+ ","
					+ "'"
					+ pgdh
					+ "'"
					+ ",'"
					+ data_listView.get(i).get("id")
					+ "',"
					+ data_listView.get(i).get("number")
					+ ",'"
					+ data_listView.get(i).get("bz") + "')" + "*sql*";
			sqlList.add(sql);

		}

		return sqlList;

	}

	private ArrayList<String> getHpdata() {
		ArrayList<String> hpdata = new ArrayList<String>();
		for (int i = 1; i < data_listView.size(); i++) {

			String sql = data_listView.get(i).get("id")+","
					+data_listView.get(i).get("machine")+","
					+ data_listView.get(i).get("number")+","
					+ data_listView.get(i).get("sfhs");
			hpdata.add(sql);

		}
		return hpdata;

	}

}
