package com.kdg6.service;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.widget.RemoteViews;

import com.kdg6.R;
import com.kdg6.activity.kdg.Zzpqgdcx;
import com.kdg6.activity.login.LoginActivity;
import com.kdg6.definition.Sign;
import com.kdg6.webservice.CallWebserviceImp;

@SuppressLint("HandlerLeak")
public class CsyyService extends Service implements Sign {

	private CallWebserviceImp callWebserviceImp;
	private String flag;
	private String txzd = "";
	private SharedPreferences spf;
	private Calendar c = Calendar.getInstance();
	private WakeLock wakeLock = null;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,getClass().getCanonicalName());
		wakeLock.acquire();

		callWebserviceImp = new CallWebserviceImp();
		spf = getSharedPreferences("loginsp", LoginActivity.MODE_PRIVATE);
		getWebService("query");
		return super.onStartCommand(intent, flags, startId);
	}

	private void getWebService(String s) {
		try {
			new Thread() {

				@Override
				public void run() {

					int currentTime = c.get(Calendar.HOUR_OF_DAY);

					if (currentTime >= 8 && currentTime <= 22) {
						queryCs();
					}

				}

			}.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 超时原因未录推送
	private void queryCs() {
		try {
			String userid = spf.getString("userId", "");
			JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
					"_PAD_KDG_TS_CSYYWL", userid, "uf_json_getdata", this);
			flag = jsonObject.getString("flag");
			JSONArray jsonArray = jsonObject.getJSONArray("tableA");
			if (Integer.parseInt(flag) > 0) {
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject temp = jsonArray.getJSONObject(i);
					txzd += temp.getString("nr") + "。";
				}
				Message msg = new Message();
				msg.what = SUCCESSFUL;// 成功
				handler.sendMessage(msg);
			} else {
				Message msg = new Message();
				msg.what = NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = NETWORK_ERROR;
			handler.sendMessage(msg);
		}

	}

	private void addNotificaction() {
		NotificationManager manager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// 创建一个Notification
		Notification notification = new Notification();
		// 设置显示在手机最上边的状态栏的图标
		notification.icon = R.drawable.logo;
		// 当当前的notification被放到状态栏上的时候，提示内容
		notification.tickerText = txzd;
		notification.defaults = Notification.DEFAULT_ALL;
		long[] vibrate = { 0, 100, 200, 300 };
		notification.vibrate = vibrate;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		// audioStreamType的值必须AudioManager中的值，代表着响铃的模式
		AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mAudioManager.setStreamVolume(android.media.AudioManager.STREAM_ALARM,
				7, 0); // tempVolume:音量绝对值
		notification.audioStreamType = AudioManager.ADJUST_RAISE;

		Intent intent = new Intent(this, Zzpqgdcx.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		RemoteViews remoteViews = new RemoteViews(getPackageName(),
				R.layout.notification_remont);
		remoteViews.setImageViewResource(R.id.imageView1, R.drawable.logo);
		remoteViews.setTextViewText(R.id.txtnotification, txzd);
		notification.contentView = remoteViews;
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_ONE_SHOT);
		notification.contentIntent = pendingIntent;
		manager.notify(1, notification);

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case NETWORK_ERROR:
					break;

				case SUCCESSFUL:
					addNotificaction();
					break;
			}
			txzd = "";
			//stopSelf();
		}

	};

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
//		Intent intent = new Intent(this,MyBroadcastReceiver.class);
//		sendBroadcast(intent);
		if (wakeLock!= null){
			wakeLock.release();
			wakeLock = null;
		}
		super.onDestroy();
	}

}
