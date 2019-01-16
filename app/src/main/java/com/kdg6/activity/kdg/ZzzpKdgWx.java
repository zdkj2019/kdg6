package com.kdg6.activity.kdg;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
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
import com.kdg6.utils.DateUtil;

/**
 * 快递柜-组长转派（维修）
 *
 * @author zdkj
 *
 */
public class ZzzpKdgWx extends FrameActivity {

	private Button confirm, cancel;
	private TextView tv_phone,tv_jssx;
	private ImageView iv_telphone,iv_telphone1;
	private String flag, zbh, type = "1", msgStr,ywlx,pqid,ddh,lxdh,cssj,djsStr;
	private Spinner spinner_pq,spinner_jdry;
	private String[] from;
	private int[] to;
	private ArrayList<Map<String, String>> data_ry,data_pq;
	private boolean iscs = false;
	private Timer  timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_zzzp);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				getWebService("getpq");
			}
		});

	}

	@Override
	protected void initVariable() {

		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("提交");
		cancel.setText("返回");

	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		iv_telphone = (ImageView) findViewById(R.id.iv_telphone);
		iv_telphone1 = (ImageView) findViewById(R.id.iv_telphone1);
		spinner_jdry = (Spinner) findViewById(R.id.spinner_jdry);
		spinner_pq = (Spinner) findViewById(R.id.spinner_pq);
		from = new String[] { "id", "name" };
		to = new int[] { R.id.bm, R.id.name };
		final Map<String, Object> itemmap = ServiceReportCache.getObjectdata()
				.get(ServiceReportCache.getIndex());

		zbh = (String) itemmap.get("zbh");
		lxdh = (String) itemmap.get("lxdh");
		cssj = (String) itemmap.get("cssj");
		ywlx = (String) itemmap.get("ywlx");
		ddh = (String) itemmap.get("ddh");
		((TextView) findViewById(R.id.tv_1)).setText(zbh);
		((TextView) findViewById(R.id.tv_2)).setText((String)itemmap.get("axdh"));
		((TextView) findViewById(R.id.tv_3)).setText((String)itemmap.get("xqmc"));
		//((TextView) findViewById(R.id.tv_4)).setText((String)itemmap.get("yqsx"));
		((TextView) findViewById(R.id.tv_5)).setText((String)itemmap.get("lxdh"));
		((TextView) findViewById(R.id.tv_6)).setText((String)itemmap.get("gzxx"));
		((TextView) findViewById(R.id.tv_7)).setText((String)itemmap.get("bz"));

		((TextView) findViewById(R.id.tv_jddz)).setText((String)itemmap.get("jddz"));

		tv_jssx = (TextView) findViewById(R.id.tv_4);
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				showDjs();
			}
		});
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
								type = "1";
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

		spinner_pq.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				pqid = data_pq.get(position).get("id");
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {
						getWebService("getry");
					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spinner_jdry.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				tv_phone.setText(data_ry.get(position).get("lxdh"));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		iv_telphone1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Call(tv_phone.getText().toString());
			}
		});

		iv_telphone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Call(lxdh);
			}
		});
	}

	/**
	 * 倒计时
	 */
	private void showDjs(){
		try {

			final Date csDate = DateUtil.StringToDate(cssj);
			timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					djsStr = "";
					Date now = new Date();
					long last = csDate.getTime()-now.getTime();
					if(last<0){
						iscs = true;
					}
					String dayStr = "",hoursStr = "",minuteStr = "",secondStr = "";
					int day = (int) (last/(24*3600*1000));
					long leave1=last%(24*3600*1000);    //计算天数后剩余的毫秒数
					int hours=(int) (leave1/(3600*1000));
					//计算相差分钟数
					long leave2=leave1%(3600*1000);        //计算小时数后剩余的毫秒数
					int minutes=(int) (leave2/(60*1000));
					//计算相差秒数
					long leave3=leave2%(60*1000);      //计算分钟数后剩余的毫秒数
					int seconds=Math.round(leave3/1000);

					dayStr = ""+day;
					hoursStr = ""+hours;
					minuteStr = ""+minutes;
					secondStr = ""+seconds;
					djsStr = dayStr + "天" + hoursStr + "小时" + minuteStr+ "分钟" + secondStr + "秒";

					Message msg = new Message();
					msg.what = Constant.NUM_9;
					handler.sendMessage(msg);
				}
			}, 0,1000);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected void getWebService(String s) {

		if (s.equals("getpq")) {
			try {
				data_pq = new ArrayList<Map<String, String>>();
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_KDG_ZCZP_PQ", DataCache.getinition().getUserId()+"*"+DataCache.getinition().getUserId(), "uf_json_getdata", this);
				flag = jsonObject.getString("flag");

				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("id", temp.getString("pqbm"));
						item.put("name", temp.getString("pqmc"));
						data_pq.add(item);
					}
					Message msg = new Message();
					msg.what = Constant.NUM_6;
					handler.sendMessage(msg);

				} else {
					flag = "查询失败，没有片区数据";
					Message msg = new Message();
					msg.what = Constant.FAIL;// 失败
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}


		if (s.equals("getry")) {
			try {
				data_ry = new ArrayList<Map<String, String>>();
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_KDG_ZCZP_RY", pqid+"*"+pqid, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");

				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("id", temp.getString("userid"));
						item.put("name", temp.getString("username"));
						item.put("lxdh", temp.getString("sjhm"));
						data_ry.add(item);
					}

					Message msg = new Message();
					msg.what = Constant.NUM_8;
					handler.sendMessage(msg);

				} else {
					Map<String, String> item = new HashMap<String, String>();
					item.put("id", "");
					item.put("name", "");
					item.put("lxdh", "");
					data_ry.add(item);
					Message msg = new Message();
					msg.what = Constant.NUM_8;// 失败
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
				String sqlid = "";
				if("巡检".equals(ywlx)){
					sqlid = "c#_PAD_KDG_XJ_ALL";
				}else{
					sqlid = "c#_PAD_KDG_ALL";
				}
				String typeStr = "zzzp";
				msgStr = "转派成功";
				String str = zbh + "*PAM*" + DataCache.getinition().getUserId()+"*PAM*" +data_ry.get(spinner_jdry.getSelectedItemPosition()).get("id");
				JSONObject json = this.callWebserviceImp.getWebServerInfo(
						sqlid, str, typeStr, typeStr,
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
					if (progressDialog != null) {
						progressDialog.dismiss();
					}
					dialogShowMessage_P(flag, null);
					break;
				case Constant.SUCCESS:
					if (progressDialog != null) {
						progressDialog.dismiss();
					}
					dialogShowMessage_P(msgStr,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
													int paramAnonymous2Int) {
									Intent intent  = getIntent();
									setResult(-1, intent);
									finish();
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
					SimpleAdapter adapter1 = new SimpleAdapter(ZzzpKdgWx.this, data_pq,
							R.layout.spinner_item, from, to);
					spinner_pq.setAdapter(adapter1);
					break;
				case Constant.NUM_8:
					if (progressDialog != null) {
						progressDialog.dismiss();
					}
					SimpleAdapter adapter3 = new SimpleAdapter(ZzzpKdgWx.this, data_ry,
							R.layout.spinner_item, from, to);
					spinner_jdry.setAdapter(adapter3);
					break;
				case Constant.NUM_9:
					tv_jssx.setText(djsStr);
					if(iscs){
						tv_jssx.setTextColor(getResources().getColor(R.color.red));
					}
					break;
			}
		}
	};

//	@Override
//	public void onBackPressed() {
//		Intent intent = new Intent(this, MainActivity.class);
//		intent.putExtra("currType", 1);
//		startActivity(intent);
//		finish();
//	}

}