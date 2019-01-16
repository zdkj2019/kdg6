package com.kdg6.activity.register;

import java.util.Vector;

import org.json.JSONObject;

import com.kdg6.R;
import com.kdg6.activity.FrameActivity;
import com.kdg6.utils.Config;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterAgreeActivity extends FrameActivity {

	private TextView tv_agreement;
	private EditText et_agreement;
	private CheckBox checkbox_agreement;
	private Button btn_next;
	private int type = 0;
	private int time = 15;
	private Intent intent;

	private String name = "";
	private String qyname = "";
	private String selfnum = "";
	private String address = "";
	private String phonenum = "";
	private String phonenum2 = "";
	private String province_id = "";
	private String city_id = "";
	private String area_id = "";
	private String ids = "";
	private byte[] photo_file2 = null;
	private byte[] photo_file1 = null;

	private String zbh_return = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_agree);
		initView();
		initListener();
	}

	protected void initView() {
		intent = getIntent();
		type = intent.getIntExtra("type", 0);

		tv_agreement = (TextView) findViewById(R.id.tv_agreement);
		et_agreement = (EditText) findViewById(R.id.et_agreement);
		checkbox_agreement = (CheckBox) findViewById(R.id.checkbox_agreement);
		btn_next = (Button) findViewById(R.id.btn_next);

		String xy = "<div>"
				+ "<br />"

				+ "<br />" + "</div>";
		
		if (type == 1) {
			tv_agreement.setText("");
			et_agreement.setText(Html.fromHtml(xy));
		} else if (type == 2) {
			tv_agreement.setText("");
			et_agreement.setText(Html.fromHtml(xy));
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (time > 0) {
						time = time - 1;
						Message msg = new Message();
						msg.what = 1;
						handler.sendMessage(msg);
						Thread.sleep(1000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void initListener() {
	}

	private void submitData() {
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				try {
					selfnum = selfnum.toLowerCase();
					String sql = "insert into USER_PASSWORD_2 (userid,username,whcd,sfzh,jzdz,sjid,sjhm,id,rylb,gzjl,password,zyshgx,lzsj,byyx,zzmm,wydj,jsjdj) "
							+ "values ("
							+ "'%s',"
							+ "'"
							+ name
							+ "','"
							+ qyname
							+ "','"
							+ selfnum
							+ "','"
							+ address
							+ "',"
							+ "to_number('0')"
							+ ",'"
							+ phonenum
							+ "',to_number('%s'),'"
							+ type
							+ "','1','"
							+ selfnum.substring(selfnum.length() - 6,
									selfnum.length())
							+ "','"
							+ ids
							+ "',sysdate,'"+phonenum2+"','"+province_id+"','"+city_id+"','"+area_id+"')";

					JSONObject object = callWebserviceImp.getWebServerInfo(
							"c#_PAD_ESP_ZC", sql, "0000", "1",
							"uf_json_setdata2", getApplicationContext());
					String flag = object.getString("flag");
					zbh_return = object.getString("zbh");

					if (Integer.parseInt(flag) > 0) {
						Message msg = new Message();
						msg.what = 2;// ���
						handler.sendMessage(msg);

					} else {
						Message msg = new Message();
						msg.what = 4;//
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 4;//
					handler.sendMessage(msg);
				}

			}
		});

	}

	private void returnData() {
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				try {
					String sql = "delete from  USER_PASSWORD_2 where userid = '"
							+ zbh_return + "'";
					JSONObject object = callWebserviceImp.getWebServerInfo(
							"c#_PAD_ESP_ZC", sql, "0000", "1",
							"uf_json_setdata2", getApplicationContext());
					Message msg = new Message();
					msg.what = 5;//
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 5;//
					handler.sendMessage(msg);
				}

			}
		});
	}

	private void upload() {
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				try {
					if (photo_file1 != null) {
						boolean flag = uploadPic("", photo_file1,
								"uf_json_setdata");
						if (flag) {
							if (photo_file2 != null) {
								flag = uploadPic("", photo_file2,
										"uf_json_setdata");
								if (flag) {
									Message msg = new Message();
									msg.what = 3;
									handler.sendMessage(msg);
								} else {
									Message msg = new Message();
									msg.what = 0;
									handler.sendMessage(msg);
								}
							} else {
								Message msg = new Message();
								msg.what = 3;
								handler.sendMessage(msg);
							}
						} else {
							Message msg = new Message();
							msg.what = 0;
							handler.sendMessage(msg);
						}
					} else {
						Message msg = new Message();
						msg.what = 0;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 0;
					handler.sendMessage(msg);
				}

			}

		});

	}

	private boolean uploadPic(final String orderNumbers, final byte[] data1,
			final String methed) throws Exception {

		if (data1 != null && orderNumbers != null) {
			JSONObject json = callWebserviceImp.getWebServerInfo2_pic(
					"c#_PAD_ESP_ZCMX", "0001", zbh_return + "*1", "0001",
					data1, "uf_json_setdata2_p11", getApplicationContext());
			String flag = json.getString("flag");
			if ("1".equals(flag)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	private Handler handler = new Handler() {
		@SuppressLint("ResourceAsColor")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				returnData();
				break;
			case 1:
				if (time == 0) {
					btn_next.setText("");
					btn_next.setBackgroundResource(R.drawable.btn_normal);
					btn_next.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (checkbox_agreement.isChecked()) {
								qyname = intent.getStringExtra("qyname");
								name = intent.getStringExtra("name");
								selfnum = intent.getStringExtra("selfnum");
								ids = intent.getStringExtra("ids");
								address = intent.getStringExtra("address");
								phonenum = intent.getStringExtra("phonenum");
								phonenum2 = intent.getStringExtra("phonenum2");
								province_id = intent.getStringExtra("province_id");
								city_id = intent.getStringExtra("city_id");
								area_id = intent.getStringExtra("area_id");
								photo_file2 = intent
										.getByteArrayExtra("photo_file2");
								photo_file1 = intent
										.getByteArrayExtra("photo_file1");
								submitData();
							} else {
								Toast.makeText(getApplicationContext(),
										"", 1).show();
							}

						}
					});
				} else {
					btn_next.setText(time + "");
				}

				break;
			case 2:
				upload();
				break;
			case 3:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				Intent intent = new Intent(getApplicationContext(),
						RegisterCompleteActivity.class);
				intent.putExtra("zbh", zbh_return);
				startActivity(intent);
				break;
			case 4:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("", null);
				break;
			case 5:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("", null);
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void initVariable() {
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
}
