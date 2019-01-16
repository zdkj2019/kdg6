package com.kdg6.activity.kdg;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONObject;
import android.annotation.SuppressLint;
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
import com.kdg6.utils.DateUtil;
/**
 * 快递柜-接单响应（维修）
 * @author zdkj
 *
 */
public class JdxyKdgWx extends FrameActivity {

	private Button confirm,cancel;
	private String flag,zbh,type="1",msgStr,lxdh,keyStr,cssj,djsStr,gjbm;
	private boolean iscs = false;
	private TextView tv_jssx;
	private ImageView iv_telphone,iv_search;
	private Timer  timer;

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
	}

	@Override
	protected void initVariable() {

		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		iv_telphone = (ImageView) findViewById(R.id.iv_telphone);
		tv_jssx = (TextView) findViewById(R.id.tv_jssx);
		confirm.setText("接单");
		cancel.setText("拒绝");
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());

		final Map<String, Object> itemmap = ServiceReportCache.getObjectdata().get(ServiceReportCache.getIndex());

		zbh = (String)itemmap.get("zbh");
		lxdh = (String)itemmap.get("lxdh");
		keyStr = (String)itemmap.get("xqmc");
		cssj = (String)itemmap.get("cssj");
		gjbm = (String)itemmap.get("axdh");
		((TextView) findViewById(R.id.tv_1)).setText(zbh);
		((TextView) findViewById(R.id.tv_2)).setText((String)itemmap.get("axdh"));
		((TextView) findViewById(R.id.tv_3)).setText((String)itemmap.get("xqmc"));
		((TextView) findViewById(R.id.tv_4)).setText((String)itemmap.get("lxdh"));
		((TextView) findViewById(R.id.tv_5)).setText((String)itemmap.get("gzxx"));
		((TextView) findViewById(R.id.tv_6)).setText((String)itemmap.get("bz"));

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
						dialogShowMessage("是否确认拒绝接单？",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,int paramAnonymous2Int) {
								showProgressDialog();
								Config.getExecutorService().execute(new Runnable() {

									@Override
									public void run() {
										type = "2";
										getWebService("submit");
									}
								});
							}
						} ,null);
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

		iv_telphone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("".equals(lxdh)) {
					toastShowMessage("请选择联系电话！");
					return;
				}
				Call(lxdh);
			}
		});

		findViewById(R.id.iv_baidumap).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						BaiduMapActivity.class);
				intent.putExtra("keyStr", keyStr);
				startActivity(intent);
			}
		});

		findViewById(R.id.iv_search).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						GjxxActivity.class);
				intent.putExtra("gjbm", gjbm);
				startActivity(intent);
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
					msg.what = Constant.NUM_6;
					handler.sendMessage(msg);
				}
			}, 0,1000);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected void getWebService(String s) {

		if (s.equals("submit")) {// 提交
			try {
				String typeStr = "1".equals(type)?"jdxy":"jjgd";
				msgStr = "1".equals(type)?"接单成功！":"拒单成功！";
				String str = zbh+"*PAM*"+DataCache.getinition().getUserId();
				JSONObject json =  this.callWebserviceImp.getWebServerInfo(
						"c#_PAD_KDG_ALL",
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
		@SuppressLint("ResourceAsColor")
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case Constant.FAIL:
					dialogShowMessage_P("失败，请检查后重试...错误标识：" + flag, null);
					break;
				case Constant.SUCCESS:
					dialogShowMessage_P(msgStr,new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface face,int paramAnonymous2Int) {
							Intent intent  = getIntent();
							setResult(-1, intent);
							finish();
						}
					});
					break;
				case Constant.NETWORK_ERROR:
					dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
					break;
				case Constant.NUM_6:
					tv_jssx.setText(djsStr);
					if(iscs){
						tv_jssx.setTextColor(getResources().getColor(R.color.red));
					}
					break;
			}
			if(progressDialog!=null){
				progressDialog.dismiss();
			}
		}
	};

	@Override
	public void onBackPressed() {
		if(timer != null){
			timer.cancel();
		}
		super.onBackPressed();
	}

}