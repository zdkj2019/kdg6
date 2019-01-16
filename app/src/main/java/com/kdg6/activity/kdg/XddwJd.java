package com.kdg6.activity.kdg;

import java.util.Map;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.kdg6.R;
import com.kdg6.activity.FrameActivity;
import com.kdg6.activity.main.MainActivity;
import com.kdg6.activity.util.BaiduMapActivity;
import com.kdg6.cache.DataCache;
import com.kdg6.cache.ServiceReportCache;
import com.kdg6.common.Constant;
import com.kdg6.utils.Config;
/**
 * 选单定位 接单
 * @author zdkj
 *
 */
public class XddwJd extends FrameActivity {

	private Button confirm,cancel;
	private String flag,zbh,type="1",msgStr,keyStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_xddw_jdxy);
		initVariable();
		initView();
		initListeners();
	}

	@Override
	protected void initVariable() {

		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("接单");
		cancel.setText("返回");

	}

	@Override
	protected void initView() {

		title.setText("接单响应（巡检）");

		final Map<String, Object> itemmap = ServiceReportCache.getObjectdata().get(ServiceReportCache.getIndex());

		zbh = itemmap.get("zbh").toString();
		keyStr = itemmap.get("xqmc").toString();
		((TextView) findViewById(R.id.tv_1)).setText(zbh);
		((TextView) findViewById(R.id.tv_2)).setText(itemmap.get("sbbm").toString());
		((TextView) findViewById(R.id.tv_3)).setText(itemmap.get("ssqx").toString());
		((TextView) findViewById(R.id.tv_4)).setText(itemmap.get("xqmc").toString());
		((TextView) findViewById(R.id.tv_5)).setText(itemmap.get("xxdz").toString());
		//((TextView) findViewById(R.id.tv_6)).setText(itemmap.get("bz").toString());
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

		findViewById(R.id.iv_baidumap).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						BaiduMapActivity.class);
				intent.putExtra("keyStr", keyStr);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void getWebService(String s) {

		if (s.equals("submit")) {// 提交
			try {
				String typeStr = "1".equals(type)?"jdxy":"jjgd";
				msgStr = "1".equals(type)?"接单成功！":"拒单成功！";
				String str = zbh+"*PAM*"+DataCache.getinition().getUserId();
				JSONObject json =  this.callWebserviceImp.getWebServerInfo(
						"c#_PAD_KDG_XJ_ALL",
						str,
						typeStr,
						typeStr,
						"uf_json_setdata2", this);
				flag =json.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					Message msg = new Message();
					msg.what = Constant.SUCCESS;
					handler.sendMessage(msg);
				}else{
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
					dialogShowMessage_P("失败，请检查后重试...错误标识：" + flag, null);
					break;
				case Constant.SUCCESS:
					dialogShowMessage_P(msgStr,new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface face,int paramAnonymous2Int) {
							onBackPressed();
						}
					});
					break;
				case Constant.NETWORK_ERROR:
					dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
					break;
			}
			if(progressDialog!=null){
				progressDialog.dismiss();
			}
		}
	};

}