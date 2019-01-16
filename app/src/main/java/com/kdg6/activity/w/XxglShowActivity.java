package com.kdg6.activity.w;

import java.util.Map;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.TextView;

import com.kdg6.R;
import com.kdg6.activity.FrameActivity;
import com.kdg6.activity.main.MainActivity;
import com.kdg6.cache.DataCache;
import com.kdg6.cache.ServiceReportCache;
import com.kdg6.utils.Config;

/**
 * 消息展示
 *
 * @author
 */
@SuppressLint("HandlerLeak")
public class XxglShowActivity extends FrameActivity {

	private WebView webview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_xxglshow);
		initVariable();
		initView();
		initListeners();
	}

	@Override
	protected void initVariable() {
		final int index = ServiceReportCache.getIndex();
		final Map<String, String> itemmap = ServiceReportCache.getData().get(index);
		webview = (WebView) findViewById(R.id.webview);
		webview.loadUrl(itemmap.get("bz"));
		if("0".equals(itemmap.get("xxzt"))){
			Config.getExecutorService().execute(new Runnable() {

				@Override
				public void run() {
					try {
						String sql = "insert into esp_sjxx_zjb (rybm,xxbm,lrsj,xxzt) values ("
								+ "'"+DataCache.getinition().getUserId()+"',"
								+ "'"+itemmap.get("zbh")+"',"
								+ "sysdate,"
								+ "'1')";
						JSONObject object = callWebserviceImp.getWebServerInfo(
								"_RZ", sql, DataCache.getinition().getUserId(),
								"uf_json_setdata", getApplicationContext());
						ServiceReportCache.getData().get(index).put("xxzt", "1");
					} catch (Exception e) {
						e.printStackTrace();
					}


				}
			});
		}

	}

	@Override
	protected void initView() {
		title.setText("消息展示");
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
	}

	@Override
	protected void getWebService(String s) {

	}

	@Override
	public void onBackPressed() {
		finish();
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

					break;

				case FAIL:

					break;

			}
			if (!backboolean) {
				progressDialog.dismiss();
			}
		}

	};

}
