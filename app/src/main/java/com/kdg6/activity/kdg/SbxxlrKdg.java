package com.kdg6.activity.kdg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.kdg6.R;
import com.kdg6.Parser.JSONObjectParser;
import com.kdg6.activity.FrameActivity;
import com.kdg6.activity.main.MainActivity;
import com.kdg6.cache.DataCache;
import com.kdg6.utils.Config;
import com.kdg6.utils.GPS;
import com.kdg6.zxing.CaptureActivity;

/**
 * 快递柜-服务报告-扫描二维码
 *
 * @author zdkj
 *
 */
public class SbxxlrKdg extends FrameActivity {

	@Override
	protected void initVariable() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void getWebService(String s) {
		// TODO Auto-generated method stub

	}
//
//	private TextView tv_yytmc, tv_jd, tv_wd, tv_sbbm, tv_sblx, tv_khsspq,
//			tv_sssqx;
//	private EditText tv_yytdz, tv_pp, tv_xh, tv_sbgh;
//	private Button bt_pdsm, bt_pdtj;
//	private RadioGroup rg_cqxz;
//	private String sbbm;
//	private ArrayList<Map<String, String>> data_khsspq; // 所属片区缓存数据
//	private ArrayList<Map<String, String>> data_khwd; // 所属网点缓存数据
//	private ArrayList<Map<String, String>> data_sblx; // 设备类型
//	private JSONObject jsonObject_khwd;
//	private JSONObject jsonObject_sblx;
//	private JSONObjectParser jsonObjectParser;
//	private String sspqid_str = "";
//	private String fbfbm = "";
//
//	private LocationClient mLocClient;
//	private BDLocationListener myListener = new MyLocationListener();
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		// 默认焦点不进入输入框，避免显示输入法
//		getWindow().setSoftInputMode(
//				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//		appendMainBody(R.layout.activity_shebeipd);
//		initVariable();
//		initView();
//		initListeners();
////		Config.getExecutorService().execute(new Runnable() {
////
////			@Override
////			public void run() {
////
////				getWebService("xq");
////			}
////		});
//
////		mLocClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
////		mLocClient.registerLocationListener(myListener); // 注册监听函数
////		setLocationClientOption();
//		startSm();
//	}
//
//	@Override
//	protected void initVariable() {
//
//		tv_yytmc = (TextView) findViewById(R.id.tv_yytmc);
//		tv_yytdz = (EditText) findViewById(R.id.tv_yytdz);
//		tv_jd = (TextView) findViewById(R.id.tv_jd);
//		tv_wd = (TextView) findViewById(R.id.tv_wd);
//		tv_sbbm = (TextView) findViewById(R.id.tv_sbbm);
//		tv_sblx = (TextView) findViewById(R.id.tv_sblx);
//		tv_pp = (EditText) findViewById(R.id.tv_pp);
//		tv_xh = (EditText) findViewById(R.id.tv_xh);
//		tv_sbgh = (EditText) findViewById(R.id.tv_sbgh);
//		tv_khsspq = (TextView) findViewById(R.id.tv_khsspq);
//		tv_sssqx = (TextView) findViewById(R.id.tv_sssqx);
//
//		rg_cqxz = (RadioGroup) findViewById(R.id.rg_cqxz);
//
//		bt_pdsm = (Button) findViewById(R.id.bt_pdsm);
//		bt_pdtj = (Button) findViewById(R.id.bt_pdtj);
//
//		jsonObjectParser = new JSONObjectParser(this);
//	}
//
//	@Override
//	protected void initView() {
//
//		title.setText("设备信息录入");
//	}
//
//	@Override
//	protected void initListeners() {
//		OnClickListener onClickListener = new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				switch (v.getId()) {
//				case R.id.bt_topback:
//					onBackPressed();
//					break;
//				}
//			}
//		};
//
//		topBack.setOnClickListener(onClickListener);
//
//		rg_cqxz.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				switch (checkedId) {
//				case R.id.tb_cq:
//					((RadioButton) group.findViewById(R.id.tb_cq))
//							.setChecked(true);
//					((RadioButton) group.findViewById(R.id.tb_xz))
//							.setChecked(false);
//					break;
//				case R.id.tb_xz:
//					((RadioButton) group.findViewById(R.id.tb_cq))
//							.setChecked(false);
//					((RadioButton) group.findViewById(R.id.tb_xz))
//							.setChecked(true);
//					break;
//				}
//
//			}
//		});
//
//		bt_pdsm.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				startSm();
//			}
//		});
//
//		bt_pdtj.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				String yytmc = tv_yytmc.getText().toString();
//				String sblx = tv_sblx.getText().toString();
//
//				if (isNotNull(tv_yytdz) && isNotNull(tv_pp) && isNotNull(tv_xh)
//						&& isNotNull(tv_sbgh) && !"".equals(yytmc)
//						&& !"".equals(sblx)) {
//					if (!backboolean) {
//						showProgressDialog();
//					}
//					Config.getExecutorService().execute(new Runnable() {
//
//						@Override
//						public void run() {
//
//							getWebService("submit");
//						}
//					});
//
//				} else {
//					dialogShowMessage("信息不完整，请重新录入！", null, null);
//				}
//
//			}
//		});
//
//		// 所属市区县
//		tv_sssqx.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//
//				Intent intent = new Intent(SbxxlrKdg.this, ChooseSssqx.class);
//				intent.putExtra("data_khsspq", data_khsspq);
//				startActivityForResult(intent, 11);
//			}
//		});
//
//		// 所属片区
//		tv_khsspq.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				Intent intent = new Intent(SbxxlrKdg.this, ChooseXQ.class);
//				String sssqxid = tv_sssqx.getTag() == null ? "" : tv_sssqx
//						.getTag().toString().trim();
//				intent.putExtra("data_khsspq", data_khsspq);
//				intent.putExtra("sssqxid", sssqxid);
//				startActivityForResult(intent, 12);
//			}
//		});
//
//		// 客户网点
//		tv_yytmc.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				try {
//
//					String sspqid = (String) tv_khsspq.getTag();
//					sspqid_str = sspqid;
//					if ("".equals(sspqid) || sspqid == null) {
//						dialogShowMessage_P("请选择所属片区", null);
//					} else {
//						data_khwd = new ArrayList<Map<String, String>>();
//
//						showProgressDialog();
//						Config.getExecutorService().execute(new Runnable() {
//							@Override
//							public void run() {
//								getWebService("_PAD_WD");
//							}
//						});
//					}
//
//				} catch (Exception e) {
//					Message msg = new Message();
//					msg.what = NETWORK_ERROR;// 网络不通
//					handler.sendMessage(msg);
//				}
//
//			}
//		});
//
//		tv_sblx.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (!backboolean) {
//					showProgressDialog();
//				}
//				Config.getExecutorService().execute(new Runnable() {
//					@Override
//					public void run() {
//						getWebService("_PAD_JDGS");
//					}
//				});
//			}
//		});
//	}
//
//	private void startSm(){
//		// 二维码
//		Intent intent = new Intent(getApplicationContext(),
//				CaptureActivity.class);
//		startActivityForResult(intent, 2);
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//		super.onActivityResult(requestCode, resultCode, data);
//		if (requestCode == 0 && resultCode == 0) {
//			Intent intent = new Intent(getApplicationContext(),
//					CaptureActivity.class);
//			startActivityForResult(intent, 2);
//		} else if (requestCode == 2 && resultCode == 2) {
//			// 二维码
//			sbbm = data.getStringExtra("result");
//			showProgressDialog();
//			Config.getExecutorService().execute(new Runnable() {
//
//				@Override
//				public void run() {
//					getWebService("_PAD_KDG_SBSFLR");
//				}
//			});
//		} else if (requestCode == 11 && resultCode == 11 && data != null) {
//			tv_sssqx.setText(data.getStringExtra("qymc"));
//			tv_sssqx.setTag(data.getStringExtra("qybm"));
//
//			tv_khsspq.setText("");
//			tv_khsspq.setTag("");
//			// 客户网点
//			tv_yytmc.setText("");
//			tv_yytmc.setTag("");
//		} else if (requestCode == 12 && resultCode == 12 && data != null) {
//			tv_khsspq.setText(data.getStringExtra("name"));
//			tv_khsspq.setTag(data.getStringExtra("id"));
//
//			// 客户网点
//			tv_yytmc.setText("");
//			tv_yytmc.setTag("");
//		} else if (requestCode == 15 && resultCode == 15 && data != null) {
//			tv_yytmc.setText(data.getStringExtra("name"));
//			tv_yytmc.setTag(data.getStringExtra("id"));
//		} else if (requestCode == 13 && resultCode == 13 && data != null) {
//			tv_sblx.setText(data.getStringExtra("name"));
//			tv_sblx.setTag(data.getStringExtra("id"));
//		}
//
//	}
//
//	@Override
//	protected void getWebService(String s) {
//
//		if ("xq".equals(s)) {
//
//			data_khsspq = new ArrayList<Map<String, String>>();
//			// data_khwd = new ArrayList<Map<String, String>>();
//
//			final JSONObject jsonObject_sspq;
//
//			String readFile_sspq = null;
//			try {
//				readFile_sspq = Config.readFile("_PAD_PQSZ", this);
//				if (readFile_sspq == null) {// _PAD_XQ_KD_LC
//					jsonObject_sspq = callWebserviceImp.getWebServerInfo(
//							"_PAD_PQSZ", "1", "uf_json_getdata", this);
//
//					Config.getExecutorService().execute(new Runnable() {
//
//						@Override
//						public void run() {
//
//							Config.writeFile("_PAD_PQSZ",
//									jsonObject_sspq.toString(),
//									getApplicationContext());
//						}
//					});
//				} else {
//					jsonObject_sspq = new JSONObject(readFile_sspq);
//				}
//				try {
//					data_khsspq = jsonObjectParser.xqParser(jsonObject_sspq);
//					Message msg = new Message();
//					msg.what = -2;// 成功
//					handler.sendMessage(msg);
//				} catch (JSONException e) {
//					e.printStackTrace();
//					Message msg = new Message();
//					msg.what = -3;// 解析出错
//					handler.sendMessage(msg);
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//				Message msg = new Message();
//				msg.what = NETWORK_ERROR;// 网络不通
//				handler.sendMessage(msg);
//			}
//		}
//
//		if ("_PAD_SBWD".equals(s)) {
//			try {
//				JSONObject object = callWebserviceImp.getWebServerInfo(
//						"_PAD_SBWD", sbbm, "uf_json_getdata", SbxxlrKdg.this);
//				JSONArray jsonArray = object.getJSONArray("tableA");
//				String flag = object.getString("flag");
//				if (Integer.parseInt(flag) > 0) {
//					object = (JSONObject) jsonArray.get(0);
//					fbfbm = object.getString("dyyyt");
//					Message msg = new Message();
//					msg.what = -2;// 成功
//					handler.sendMessage(msg);
//				} else {
//					Message msg = new Message();
//					msg.what = -3;// 解析出错
//					handler.sendMessage(msg);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				Message msg = new Message();
//				msg.what = NETWORK_ERROR;// 网络不通
//				handler.sendMessage(msg);
//			}
//
//		}
//
//		if ("_PAD_WD".equals(s)) {
//			try {
//				jsonObject_khwd = callWebserviceImp.getWebServerInfo(
//						"_PAD_WD_SB", fbfbm + "*" + sspqid_str,
//						"uf_json_getdata", SbxxlrKdg.this);
//				data_khwd = jsonObjectParser.KhwdParser(jsonObject_khwd);
//				if (data_khwd == null) {
//					Message localMessage1 = new Message();
//					localMessage1.what = -3;// 获取失败
//					handler.sendMessage(localMessage1);
//				} else {
//					Intent intent = new Intent(SbxxlrKdg.this, ChooseKhwd.class);
//					String pqid = tv_khsspq.getTag() == null ? ""
//							: tv_khsspq.getTag().toString();
//					intent.putExtra("data_khwd", data_khwd);
//					intent.putExtra("pqid", pqid);
//					progressDialog.dismiss();
//					startActivityForResult(intent, 15);
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//				Message localMessage1 = new Message();
//				localMessage1.what = -3;// 获取失败
//				handler.sendMessage(localMessage1);
//			}
//
//		}
//
//		if ("_PAD_JDGS".equals(s)) {
//			try {
//				jsonObject_sblx = callWebserviceImp.getWebServerInfo(
//						"_PAD_JDGS", fbfbm, "uf_json_getdata", SbxxlrKdg.this);
//				JSONArray jsonArray = jsonObject_sblx.getJSONArray("tableA");
//				String flag = jsonObject_sblx.getString("flag");
//				if (Integer.parseInt(flag) > 0) {
//					Intent intent = new Intent(SbxxlrKdg.this, ChooseSb.class);
//					data_sblx = new ArrayList<Map<String, String>>();
//					for (int i = 0; i < jsonArray.length(); i++) {
//						JSONObject object = (JSONObject) jsonArray.get(i);
//						Map<String, String> map = new HashMap<String, String>();
//						map.put("hplbmc",
//								object.getString("hpgl_jcsj_hplbb_hplbmc"));
//						map.put("hplbbm",
//								object.getString("hpgl_jcsj_hplbb_hplbbm"));
//						data_sblx.add(map);
//					}
//					intent.putExtra("data_fbdw", data_sblx);
//					progressDialog.dismiss();
//					startActivityForResult(intent, 13);
//				} else {
//					Message localMessage1 = new Message();
//					localMessage1.what = -3;// 获取失败
//					handler.sendMessage(localMessage1);
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//				Message localMessage1 = new Message();
//				localMessage1.what = -3;// 获取失败
//				handler.sendMessage(localMessage1);
//			}
//		}
//
//		Map<String, String> map;
//		if ("sbbm".equals(s)) {
//
//			JSONObject jsonObject = null;
//			try {
//				jsonObject = callWebserviceImp.getWebServerInfo("_PAD_SBS",
//						sbbm, "uf_json_getdata", this);
//				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
//				jsonObject = (JSONObject) jsonArray.get(0);
//				String jg = jsonObject.getString("sbs");
//				if ("0".equals(jg)) {
//					Config.getExecutorService().execute(new Runnable() {
//						@Override
//						public void run() {
//							getWebService("_PAD_SBWD");
//						}
//					});
//					Message msg = new Message();
//					msg.what = SUCCESSFUL;
//					handler.sendMessage(msg);
//				} else {
//					Message msg = new Message();
//					msg.what = 2;
//					handler.sendMessage(msg);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				Message localMessage1 = new Message();
//				localMessage1.what = NETWORK_ERROR;// 网络连接出错，你检查你的网络设置
//				handler.sendMessage(localMessage1);
//			}
//		}
//
//		if ("_PAD_KDG_SBSFLR".equals(s)) {
//
//			JSONObject jsonObject = null;
//			try {
//				jsonObject = callWebserviceImp.getWebServerInfo("_PAD_KDG_SBSFLR",
//						sbbm, "uf_json_getdata", this);
//				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
//				jsonObject = (JSONObject) jsonArray.get(0);
//				String jg = jsonObject.getString("ssl");
//				if ("0".equals(jg)) {
//					Message msg = new Message();
//					msg.what = 7;
//					handler.sendMessage(msg);
//				} else {
//					Message msg = new Message();
//					msg.what = 6;
//					handler.sendMessage(msg);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				Message localMessage1 = new Message();
//				localMessage1.what = NETWORK_ERROR;// 网络连接出错，你检查你的网络设置
//				handler.sendMessage(localMessage1);
//			}
//		}
//
//		if ("submit".equals(s)) {
//
//			try {
//				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
//						"_PAD_WD_XXYY", fbfbm + "*"
//								+ tv_yytmc.getTag().toString(),
//						"uf_json_getdata", this);
//				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
//				jsonObject = (JSONObject) jsonArray.get(0);
//				String xxx = jsonObject.getString("xxx");
//				String yyy = jsonObject.getString("yyy");
//				double ym = GPS.getDistance(Double.parseDouble(xxx),
//						Double.parseDouble(yyy),
//						Double.parseDouble(tv_jd.getText().toString()),
//						Double.parseDouble(tv_wd.getText().toString()));
//				if ("0".equals(xxx)) {
//					ym = -1;
//				}
//				String glip = rg_cqxz.getCheckedRadioButtonId() == R.id.tb_xz ? "002"
//						: "001";
//
//				String sql = "update MAIN_JCSJ_SBB2 set cjbm = '"
//						+ tv_yytmc.getTag() + "'," + "sblx = '"
//						+ tv_sblx.getTag() + "'," + "pch = '"
//						+ tv_pp.getText() + "'," + "sbxh = '" + tv_xh.getText()
//						+ "'," + "yytlx = '" + tv_sbgh.getText() + "',xxx = '"
//						+ tv_jd.getText() + "'," + "yyy = '" + tv_wd.getText()
//						+ "'," + "xxdz = '" + tv_yytdz.getText() + "',ym='"
//						+ ym + "',jkip='" + DataCache.getinition().getUserId()
//						+ "',djzt = '1',azsj=sysdate,glip='" + glip
//						+ "' where ddbh = '" + sbbm + "'";
//
//				jsonObject = callWebserviceImp.getWebServerInfo("_RZ", sql,
//						DataCache.getinition().getUserId(), "uf_json_setdata",
//						this);
//				String f = jsonObject.getString("flag");
//				if (Integer.parseInt(f) > 0) {
//
//					Message msg = new Message();
//					msg.what = 4;
//					handler.sendMessage(msg);
//				} else {
//					Message msg = new Message();
//					msg.what = 5;// 失败
//					handler.sendMessage(msg);
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//				Message localMessage1 = new Message();
//				localMessage1.what = NETWORK_ERROR;// 网络连接出错，你检查你的网络设置
//				handler.sendMessage(localMessage1);
//			}
//		}
//
//	}
////
////	@Override
////	public void onBackPressed() {
////		Intent intent = getIntent();
////		intent.putExtra("sbbm",sbbm);
////		setResult(3, intent);
////		finish();
////	}
//
//	private Handler handler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			if (progressDialog != null) {
//				progressDialog.dismiss();
//			}
//			switch (msg.what) {
//			case -3:
//				dialogShowMessage_P("基础信息解析失败", null);
//				break;
//			case -2:
//				break;
//			case NETWORK_ERROR:
//				dialogShowMessage_P("网络连接出错，请检查你的网络设置", null);
//				break;
//
//			case SUCCESSFUL:
//				tv_sbbm.setText(sbbm);
//				break;
//			case 2:
//				dialogShowMessage_P("当前设备已录入或不存在！",
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface face,
//									int paramAnonymous2Int) {
//								Intent intent = getIntent();
//								intent.putExtra("sbbm",sbbm);
//								setResult(3, intent);
//								finish();
//							}
//						});
//				break;
//			case 3:
//				dialogShowMessage_P("该设备不在选定营业厅内",
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface face,
//									int paramAnonymous2Int) {
//								face.dismiss();
//							}
//						});
//				break;
//			case 4:
//				dialogShowMessage_P("保存成功！",
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface face,
//									int paramAnonymous2Int) {
//								Intent intent = getIntent();
//								intent.putExtra("sbbm",sbbm);
//								setResult(3, intent);
//								finish();
//							}
//						});
//				break;
//			case 5:
//				dialogShowMessage_P("保存失败！",
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface face,
//									int paramAnonymous2Int) {
//								face.dismiss();
//							}
//						});
//				break;
//			case 6:
//				Intent intent = getIntent();
//				intent.putExtra("sbbm",sbbm);
//				setResult(3, intent);
//				finish();
//				break;
//			case 7:
//				Intent intent1 = getIntent();
//				intent1.putExtra("sbbm","-"+sbbm);
//				setResult(3, intent1);
//				finish();
//				break;
//			}
//
//		}
//
//	};
//
//	/*
//	 * BDLocationListener接口有2个方法需要实现： 1.接收异步返回的定位结果，参数是BDLocation类型参数。
//	 * 2.接收异步返回的POI查询结果，参数是BDLocation类型参数。
//	 */
//	public class MyLocationListener implements BDLocationListener {
//
//		@Override
//		public void onReceiveLocation(BDLocation locations) {
//			if (locations == null) {
//				return;
//			} else {
//				tv_jd.setText("" + locations.getLongitude());
//				tv_wd.setText("" + locations.getLatitude());
//				tv_yytdz.setText("" + locations.getAddrStr());
//				mLocClient.stop();
//			}
//		}
//
//		public void onReceivePoi(BDLocation poiLocation) {
//
//		}
//
//		@Override
//		public void onConnectHotSpotMessage(String arg0, int arg1) {
//			// TODO Auto-generated method stub
//
//		}
//
//	};
//
//	/**
//	 * 设置定位参数包括：定位模式（单次定位，定时定位），返回坐标类型，是否打开GPS等等。
//	 */
//	private void setLocationClientOption() {
//
//		final LocationClientOption option = new LocationClientOption();
//		option.setOpenGps(true);
//		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
//		option.setScanSpan(1000);// 设置发起定位请求的间隔时间为5000ms
//		option.disableCache(true);// 禁止启用缓存定位
//		option.setPriority(LocationClientOption.GpsFirst);
//		option.setAddrType("all");
//		mLocClient.setLocOption(option);
//		mLocClient.start();
//
//	}
//
}
