package com.kdg6.activity.kdg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.CharacterStyle;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.kdg6.R;
import com.kdg6.activity.BaseActivity;
import com.kdg6.activity.FrameActivity;
import com.kdg6.activity.main.MainActivity;
import com.kdg6.cache.DataCache;
import com.kdg6.cache.ServiceReportCache;
import com.kdg6.utils.Config;
import com.kdg6.utils.DateUtil;
/**
 * 快递柜-拒绝接单
 * @author zdkj
 *
 */
public class JjjdKdg extends FrameActivity {

	private Button confirm,cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_jdxy);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				getWebService("querygzlx");

			}
		});
	}

	@Override
	protected void initVariable() {

		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("接单");
		cancel.setText("拒绝");

	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());

		Map<String, Object> itemmap = ServiceReportCache.getObjectdata().get(
				ServiceReportCache.getIndex());

		((TextView) findViewById(R.id.tv_suxq)).setText(itemmap.get("khbmmc")
				.toString());
		((TextView) findViewById(R.id.tv_smsf)).setText(itemmap.get("smsf")
				.toString());
		((TextView) findViewById(R.id.tv_kfyq)).setText(itemmap.get("kzzd18")
				.toString());
		((TextView) findViewById(R.id.tv_supq)).setText(itemmap.get("ds")
				.toString());
		((TextView) findViewById(R.id.tv_xxdz)).setText(itemmap.get("khxxdz")
				.toString());
		((TextView) findViewById(R.id.tv_bz)).setText(itemmap.get("bz")
				.toString());
		((TextView) findViewById(R.id.tv_kfxm)).setText(itemmap.get("kfxm")
				.toString());
		((TextView) findViewById(R.id.tv_jsdw)).setText(itemmap.get("cx")
				.toString());// 结算位置
		((TextView) findViewById(R.id.tv_clfs)).setText("1".equals(itemmap
				.get("clfs")) ? "人工上门" : "电话完工");// 结算位置
		String gdfy = itemmap.get("gdfy").toString();
		if (gdfy == null || "".equals(gdfy)) {
			((TextView) findViewById(R.id.tv_gdfy)).setText(gdfy);// 工单费用
		} else {
			((TextView) findViewById(R.id.tv_gdfy))
					.setText(Float.parseFloat(itemmap.get("gdfy").toString()
							.length() > 7 ? itemmap.get("gdfy").toString()
							.substring(0, 7) : itemmap.get("gdfy").toString())
							+ "");// 工单费用
		}
		// 电话
		String usertel = itemmap.get("usertel").toString();

		if (usertel != null) {

			usertel = usertel.replaceAll("[\\D]+", ",");
			String[] telarry = usertel.split(",");

			for (String tel : telarry) {

				addpohoneview(tel);
			}
		}

	}

	private void addpohoneview(String phone) {

		TextView textView = new TextView(getApplicationContext());

		textView.setTextSize(18l);
		textView.setTextColor(Color.BLACK);
		textView.setGravity(Gravity.CENTER_VERTICAL);
		textView.setBackgroundResource(R.drawable.edittext_bg);
		textView.setPadding(5, 0, 0, 0);

		setunderline(phone, textView);

		//phone_lin.addView(textView);

	}

	private void setunderline(String tel, TextView view) {

		CharacterStyle span = new UnderlineSpan();
		SpannableString telss = new SpannableString(tel);
		telss.setSpan(span, 0, telss.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		view.setText(telss);

		telonclick(view, tel);
	}

	private void telonclick(View v, final String tel) {

		v.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent phoneIntent = new Intent("android.intent.action.CALL",
						Uri.parse("tel:" + tel));

				startActivity(phoneIntent);

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

						break;
					default:
						break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);

		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

	}

	String flag;

	@Override
	protected void getWebService(String s) {

		if (s.equals("submit")) {// 提交
			try {

				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_JDCF_CX", "", "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray array = jsonObject.getJSONArray("tableA");
					jsonObject = (JSONObject) array.get(0);
					if("2".equals(jsonObject.get("djzt"))){
						String czrz3 = "'0'||chr(42)||'"
								+ DataCache.getinition().getUserId() + "'||chr(42)||'"
								+ new DateUtil().toDataString("yyyy-MM-dd HH:mm:ss")
								+ "'";
						Log.e("dd", czrz3);
						String djzt = "";
						String sql = "";
						if ("5".equals(djzt)) {
							sql = "";
						} else {
							sql = "";
						}
						jsonObject = callWebserviceImp.getWebServerInfo(
								"_RZ", sql, DataCache.getinition().getUserId(),
								"uf_json_setdata", this);
						flag = jsonObject.getString("flag");

						if (Integer.parseInt(flag) > 0) {
							Message msg = new Message();
							msg.what = SUCCESSFUL;
							handler.sendMessage(msg);
						} else {
							flag = jsonObject.getString("msg");
							Message msg = new Message();
							msg.what = FAIL;// 失败
							handler.sendMessage(msg);
						}
					}else{
						Message msg = new Message();
						msg.what = 4;
						handler.sendMessage(msg);
					}

				}else{
					flag = jsonObject.getString("msg");
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

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case -1:
					dialogShowMessage_P("获取基础数据失败", null);
					break;
				case 1:
					String message = "接收成功!";
					dialogShowMessage_P(message,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
													int paramAnonymous2Int) {
									onBackPressed();
								}
							});

					break;
				case 0:
					dialogShowMessage_P("失败，请检查后重试...错误标识：" + flag, null);
					break;

				case 2:

					break;
				case 3:
					dialogShowMessage_P("拒绝成功！", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface face,
											int paramAnonymous2Int) {
							onBackPressed();
						}
					});
					break;
				case 4:
					dialogShowMessage_P("工单已接收！", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface face,
											int paramAnonymous2Int) {
							onBackPressed();
						}
					});
					break;
			}
			if(progressDialog!=null){
				progressDialog.dismiss();
			}

		}
	};

//	@Override
//	public void onBackPressed() {
//		skipActivity2(MainActivity.class);
//	}

}