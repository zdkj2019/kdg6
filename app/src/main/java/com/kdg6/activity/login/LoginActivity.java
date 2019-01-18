package com.kdg6.activity.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.kdg6.R;
import com.kdg6.activity.FrameActivity;
import com.kdg6.activity.main.MainActivity;
import com.kdg6.activity.register.RegisterActivity;
import com.kdg6.activity.w.SetParams;
import com.kdg6.cache.DataCache;
import com.kdg6.common.Constant;
import com.kdg6.utils.Config;

/**
 * 登录页面
 *
 * @author
 */
@SuppressLint("HandlerLeak")
public class LoginActivity extends FrameActivity {

	private SharedPreferences spf;
	private SharedPreferences.Editor spfe;
	private EditText et_userid, et_password;
	private Button login;
	private String nameStr, passStr,userid, flag, flag_DL;
	private CheckBox saveuser, saveps, autologin;

	private JSONObject jsonObject;
	private TextView tv_register, tv_sz;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();// 是否自动登录
		initVariable();
		initListeners();

	}

	@Override
	protected void initVariable() {
		et_userid = (EditText) findViewById(R.id.userId);
		et_password = (EditText) findViewById(R.id.userPs);
		login = (Button) findViewById(R.id.login);

		saveuser = (CheckBox) findViewById(R.id.saveuser);
		saveps = (CheckBox) findViewById(R.id.saveps);
		autologin = (CheckBox) findViewById(R.id.autologin);

		tv_register = (TextView) findViewById(R.id.tv_register);
		tv_register.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		tv_sz = (TextView) findViewById(R.id.tv_sz);
		tv_sz.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		et_userid.setText(nameStr);
		et_password.setText(passStr);


		PackageInfo packageInfo = null;
		try {
			packageInfo = getPackageManager().getPackageInfo(getPackageName(),0);

			TextView v = (TextView) findViewById(R.id.vers);
			v.setText("V: " + packageInfo.versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 是否自动登录
	 */
	@Override
	protected void initView() {

		spf = getSharedPreferences("loginsp", LoginActivity.MODE_PRIVATE);
		boolean autologin = spf.getBoolean("autologin", false);
		userid = spf.getString("userId", "");
		nameStr = spf.getString("nameStr", "");
		passStr = spf.getString("userPs", "");
		spfe = spf.edit();

		DataCache.getinition().setUserId(userid);

	}

	@Override
	protected void initListeners() {

		// 登录
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nameStr = et_userid.getText().toString().trim();
				passStr = et_password.getText().toString().trim();
				if (!nameStr.equals("") && !passStr.equals("")) {
					showProgressDialog();
					Config.getExecutorService().execute(new Runnable() {
						@Override
						public void run() {
							getWebService("Login");
						}
					});
				} else {
					Message msg = new Message();
					msg.what = Constant.NUM_6;
					handler.sendMessage(msg);
				}
			}
		});

		tv_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(intent);
			}
		});

		tv_sz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, SetParams.class);
				startActivity(intent);
			}
		});

	}

	@Override
	protected void getWebService(String s) {

		if ("Login".equals(s)) {

			try {
				jsonObject = callWebserviceImp.getWebServerInfo("_DL_2", nameStr + "*" + nameStr + "*" + passStr, "uf_json_getdata", this);
				flag_DL = jsonObject.getString("flag");
				if (Integer.parseInt(flag_DL) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					JSONObject temp = jsonArray.getJSONObject(0);
					userid = temp.getString("userid");
					DataCache.getinition().setUserId(userid);
					Config.getExecutorService().execute(new Runnable() {
						@Override
						public void run() {
							getWebService("getmenu");
						}
					});

					DataCache.getinition().setUsername(
							temp.getString("username"));

					// 是否保存用户名和密码
					if (saveuser.isChecked()) {
						spfe.putString("userId", userid);
						spfe.putString("nameStr", nameStr);
					} else {
						spfe.putString("userId", "");
						spfe.putString("nameStr", "");
						spfe.putString("Idbm", "");
					}
					if (saveps.isChecked()) {
						spfe.putString("userPs", passStr);
					} else {
						spfe.putString("userPs", "");
					}
					if (autologin.isChecked()) {
						spfe.putBoolean("autologin", true);
					} else {
						spfe.putBoolean("autologin", false);
					}

					spfe.commit();

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

		if (s.equals("getmenu")) {
			JSONObject jsonObject;
			try {

				jsonObject = callWebserviceImp.getWebServerInfo("_PAD_GNQX", userid
						+ "*" + userid, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {

					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					List<String> menu_name = new ArrayList<String>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject object = jsonArray.getJSONObject(i);

						menu_name.add(object.getString("menu_name"));
					}
					DataCache.getinition().setMenu(menu_name);
					Message msg = new Message();
					msg.what = Constant.SUCCESS;
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = Constant.NUM_7;
					handler.sendMessage(msg);
				}

			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}

		}

		if ("getTsxx".equals(s)) {
			try {
				try {
					// 查询登陆后 弹出框显示信息
					jsonObject = callWebserviceImp.getWebServerInfo(
							"_PAD_YGXX_XXNR1", spf.getString("userId", ""),
							"uf_json_getdata", this);
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					JSONObject temp = jsonArray.getJSONObject(0);
					Map<String, String> item = new HashMap<String, String>();
					item.put("zdrq", temp.getString("zdrq"));
					item.put("var_kzzd1", temp.getString("var_kzzd1"));
					item.put("ryid", temp.getString("ryid"));
					item.put("xxzt", temp.getString("xxzt"));
					item.put("zbh", temp.getString("zbh"));
					item.put("bz", temp.getString("bz"));
					DataCache.getinition().setLogin_show_map(item);
				} catch (Exception e) {

				}

				jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SJXX_ZDKJ", "", "uf_json_getdata", this);
				String flag2 = jsonObject.getString("flag");
				if (Integer.parseInt(flag2) > 0) {
					JSONArray array = jsonObject.getJSONArray("tableA");
					JSONObject temp = array.getJSONObject(0);
					DataCache.getinition().setTsxx(temp.getString("ts_msg"));
					Config.getExecutorService().execute(new Runnable() {
						@Override
						public void run() {
							getWebService("getYHKxx");
						}
					});
				} else {
					Config.getExecutorService().execute(new Runnable() {
						@Override
						public void run() {
							getWebService("getYHKxx");
						}
					});
				}

			} catch (Exception e) {
				Message msg = Message.obtain();
				msg.what = NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

		if ("getYHKxx".equals(s)) {
			try {
				// 查询银行卡信息是否完善
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_YHK_CX", DataCache.getinition().getUserId(),
						"uf_json_getdata", this);
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				jsonObject = (JSONObject) jsonArray.get(0);
				if (!"1".equals(jsonObject.getString("yhk"))) {
					DataCache.getinition().setHasYHK(true);
				} else {
					DataCache.getinition().setHasYHK(false);
				}
				Message msg = Message.obtain();
				msg.what = SUCCESSFUL;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = Message.obtain();
				msg.what = NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}

		if ("fb".equals(s)) {

			try {
				jsonObject = callWebserviceImp.getWebServerInfo("_PAD_QX_FBF",
						nameStr, "uf_json_getdata", this);

				flag = jsonObject.getString("flag");

				if (Integer.parseInt(flag) > 0) {

					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {

							Config.writeFile("_PAD_QX_FBF",
									jsonObject.toString(),
									getApplicationContext());
						}
					});

					getWebService("xq");

				} else {
					Message msg = new Message();
					msg.what = 3;// 失败
					handler.sendMessage(msg);
				}

			} catch (Exception e) {
				// Toast.makeText(this, jsonObject.toString(), 1).show();
				Message msg = new Message();
				msg.what = NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

		if ("xq".equals(s)) {

			try {
				jsonObject = callWebserviceImp.getWebServerInfo("_PAD_PQSZ",
						"", "uf_json_getdata", this);

				flag = jsonObject.getString("flag");

				if (Integer.parseInt(flag) > 0) {

					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {

							Config.writeFile("_PAD_PQSZ",
									jsonObject.toString(),
									getApplicationContext());
						}
					});

					Message msg = new Message();
					msg.what = SUCCESSFUL;// 成功
					handler.sendMessage(msg);
					// getWebService("fy");

				} else {
					Message msg = new Message();
					msg.what = 3;// 失败
					handler.sendMessage(msg);
				}

			} catch (Exception e) {
				// Toast.makeText(this, jsonObject.toString(), 1).show();
				Message msg = new Message();
				msg.what = NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

		if ("fy".equals(s)) {

			try {
				jsonObject = callWebserviceImp.getWebServerInfo("_PAD_FYXMLX",
						"", "uf_json_getdata", this);

				flag = jsonObject.getString("flag");

				if (Integer.parseInt(flag) > 0) {

					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {

							Config.writeFile("_PAD_FYXMLX",
									jsonObject.toString(),
									getApplicationContext());
						}
					});

					getWebService("wd");

				} else {
					Message msg = new Message();
					msg.what = 3;// 失败
					handler.sendMessage(msg);
				}

			} catch (Exception e) {
				// Toast.makeText(this, jsonObject.toString(), 1).show();
				Message msg = new Message();
				msg.what = NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

		if ("wd".equals(s)) {

			try {
				// jsonObject = callWebserviceImp.getWebServerInfo("_PAD_WD",
				// "","uf_json_getdata", this);
				//
				// Config.getExecutorService().execute(new Runnable() {
				//
				// @Override
				// public void run() {
				//
				// Config.writeFile("_PAD_WD",
				// jsonObject.toString(), getApplicationContext());
				// }
				// });

				Message msg = new Message();
				msg.what = SUCCESSFUL;// 成功
				handler.sendMessage(msg);

			} catch (Exception e) {
				// Toast.makeText(this, jsonObject.toString(), 1).show();
				Message msg = new Message();
				msg.what = NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

	}

	/**
	 * 重写回退键
	 */
	@Override
	public void onBackPressed() {
		dialogShowMessage("确定退出?", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
				System.exit(0);
				// AppManager.getAppManager().AppExit(getApplicationContext());
			}
		}, null);

	}

	public boolean isrepeatlogin = false;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case Constant.NETWORK_ERROR:
					dialogShowMessage_P("网络连接失败，请检查网络连接是否正常！", null);
					break;
				case Constant.SUCCESS:
					skipActivity(MainActivity.class);
					break;
				case Constant.FAIL:
					if (flag_DL.equals("4")) {
						dialogShowMessage_P("你密码输入错误3次,帐号已锁定,请联系管理员解锁!", null);
					} else {
						dialogShowMessage_P("请输入正确的密码!输入密码错误超过3次,系统将锁定帐号", null);
					}
					break;
				case Constant.NUM_7:
					dialogShowMessage_P("没有菜单权限，请联系管理员！", null);
					break;
				case Constant.NUM_6:
					dialogShowMessage_P("账号或密码不为空！", null);
					break;
			}
			if(progressDialog!=null){
				progressDialog.dismiss();
			}
		}

	};
}
