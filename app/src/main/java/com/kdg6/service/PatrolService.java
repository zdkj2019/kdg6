package com.kdg6.service;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.kdg6.cache.DataCache;
import com.kdg6.definition.Sign;
import com.kdg6.utils.AppUtils;
import com.kdg6.webservice.CallWebserviceImp;

@SuppressLint("HandlerLeak")
public class PatrolService extends Service implements Sign{

	private CallWebserviceImp callWebserviceImp;
	private String flag;
	String txzd = "";
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	@Override
	public void onCreate() {
		callWebserviceImp = new CallWebserviceImp();
		super.onCreate();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		getWebService("query");
		return super.onStartCommand(intent, flags, startId);
	}

	Calendar c = Calendar.getInstance();

	private void getWebService(String s) {

		try {
			new Thread() {

				@Override
				public void run() {

					int currentTime = c.get(Calendar.HOUR_OF_DAY);

					if (currentTime >= 8 && currentTime <= 22) {
						query();
					}

				}

			}.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private void query() {
		try {
			String userid = DataCache.getinition().getUserId();
			JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
					"_PAD_SHGL_KDG_WWGDS", userid + "*" + userid,
					"uf_json_getdata", this);
			flag = jsonObject.getString("flag");
			if (Integer.parseInt(flag) > 0) {
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject temp = jsonArray.getJSONObject(i);
					txzd = txzd + temp.getString("wwgd");
				}
				if (!"".equals(txzd)) {
					
					Message msg = new Message();
					msg.what = SUCCESSFUL;// �ɹ�
					handler.sendMessage(msg);
				}else{
					AppUtils.sendBadgeNumber(this,"");//���
				}
			} else {
				Message msg = new Message();
				msg.what = FAIL;// ʧ��
				handler.sendMessage(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = NETWORK_ERROR;// ���粻ͨ
			handler.sendMessage(msg);
		}

	}

	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case NETWORK_ERROR:
				break;

			case SUCCESSFUL:
				break;
			}
			txzd = "";
			stopSelf();
		}

	};

}
