package com.kdg6.activity.w;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kdg6.R;
import com.kdg6.activity.FrameActivity;
import com.kdg6.common.Constant;
import com.kdg6.utils.Config;

public class GlzdShowActivity extends FrameActivity {

	private String zlbm,flag;
	private List<Map<String, String>> data_zsk;
	private WebView webview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_xxglshow);
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

	@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
	@Override
	protected void initVariable() {


		webview = (WebView) findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setUseWideViewPort(true);
		webview.getSettings().setSupportZoom(true);//是否可以缩放，默认true
		webview.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
		webview.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
		webview.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
		webview.getSettings().setAppCacheEnabled(true);//是否使用缓存
		webview.getSettings().setDomStorageEnabled(true);

		webview.setLayerType(View.LAYER_TYPE_HARDWARE,null);//开启硬件加速

		if (Build.VERSION.SDK_INT >= 21) {
			webview.getSettings().setMixedContentMode( WebSettings.MIXED_CONTENT_ALWAYS_ALLOW );
		}


		webview.setWebViewClient(new WebViewClient(){

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				showProgressDialog();
			}
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if (!backboolean) {
					progressDialog.dismiss();
				}
			}
		});

		zlbm = getIntent().getStringExtra("zlbm");

	}

	@Override
	protected void initView() {
		title.setText("知识库");
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
		if(s.equals("query")){
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_YWCX_GZZSD", zlbm, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				data_zsk = new ArrayList<Map<String, String>>();
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("id", temp.getString("wjpath"));
						item.put("name", temp.getString("wjname"));
						data_zsk.add(item);
					}
					Message msg = new Message();
					msg.what = Constant.SUCCESS;
					handler.sendMessage(msg);
				}else{
					flag="没有数据";
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

	@Override
	public void onBackPressed() {
		finish();
	}


	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case Constant.NETWORK_ERROR:
					dialogShowMessage_P("网络连接出错，请检查你的网络设置", null);
					break;

				case Constant.SUCCESS:
					if(data_zsk.size()>0){
						String url = "";
						Map<String, String> map = data_zsk.get(0);
						url = Constant.ImgPath+"/"+map.get("id")+"/"+map.get("name");
						url = "https://view.officeapps.live.com/op/view.aspx?src="+url;
						webview.loadUrl(url);
					}else{
						dialogShowMessage_P("没有数据", null);
					}
					break;

				case Constant.FAIL:
					dialogShowMessage_P("没有数据", null);
					break;

			}
			if (!backboolean) {
				progressDialog.dismiss();
			}
		}

	};

}
