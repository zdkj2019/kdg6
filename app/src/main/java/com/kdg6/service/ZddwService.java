package com.kdg6.service;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.view.WindowManager;
import android.widget.RemoteViews;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.kdg6.R;
import com.kdg6.activity.login.LoginActivity;
import com.kdg6.activity.main.MainActivity;
import com.kdg6.activity.notify.QpActivity;
import com.kdg6.cache.DataCache;
import com.kdg6.common.Constant;
import com.kdg6.utils.Config;
import com.kdg6.utils.DateUtil;
import com.kdg6.utils.GPS;
import com.kdg6.webservice.CallWebserviceImp;

@SuppressLint("HandlerLeak")
public class ZddwService extends Service{

	private CallWebserviceImp callWebserviceImp;
	private String flag,msgStr;
	private boolean isFirst = true;
	private int num=0;
	private JSONArray array;
	private SharedPreferences spf,spf_zddw;
	private SharedPreferences.Editor spfe_zddw;
	private WakeLock wakeLock = null;
	private BDLocation location;
	private LocationClient mLocClient;
	private BDLocationListener myListener = new MyLocationListener();

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,getClass().getCanonicalName());
		wakeLock.acquire();

		callWebserviceImp = new CallWebserviceImp();
		spf = getSharedPreferences("loginsp", LoginActivity.MODE_WORLD_READABLE);
		spf_zddw = getSharedPreferences("zddw", ZddwService.MODE_WORLD_READABLE);
		mLocClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocClient.registerLocationListener(myListener); // 注册监听函数
		setLocationClientOption();

		return super.onStartCommand(intent, flags, startId);
	}

	private void queryZddw() {
		try {
			if(isFirst){
				String dataStr = spf_zddw.getString("data", "[]");
				array = new JSONArray(dataStr);
				String userid = spf.getString("userId", "");
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_YWGL_KSDW", userid, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					for(int i=0;i<jsonArray.length();i++){
						JSONObject json = jsonArray.getJSONObject(i);
						boolean iscz = false;
						for(int j=0;j<array.length();j++){
							JSONObject obj = array.getJSONObject(j);
							if(obj.getString("zbh").equals(json.getString("zbh"))){
								iscz = true;
							}
						}
						if(!iscz){
							json.put("oldlat", 0);
							json.put("oldlng", 0);
							json.put("address", 0);
							json.put("hasdw", 0);
							array.put(json);
						}
					}
				}
				isFirst = false;
			}else{
				String dataStr = spf_zddw.getString("data", "[]");
				array = new JSONArray(dataStr);
			}

			if(array.length()>0){
				ppdw();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = Constant.NETWORK_ERROR;
			handler.sendMessage(msg);
		}

	}

	/**
	 * 定位匹配
	 */
	private void ppdw(){
		try {
			for(int i=0;i<array.length();i++){
				JSONObject json = (JSONObject) array.get(i);
				if(json.getInt("hasdw")==0){
					json.put("oldlat", location.getLatitude());
					json.put("oldlng", location.getLongitude());
					json.put("address", location.getAddrStr());
					double jlfw = Double.parseDouble((String) json.get("jlfw"));
					double jd = Double.parseDouble((String) json.get("jd"));
					double wd = Double.parseDouble((String) json.get("wd"));
					double jl = GPS.getDistance(jd, wd,location.getLongitude() , location.getLatitude());
					if(jl<=jlfw){
						json.put("hasdw", 1);
						final JSONObject obj = json;
						Config.getExecutorService().execute(new Runnable() {

							@Override
							public void run() {
								submit(obj);
							}
						});

					}
				}else{
					final JSONObject obj = json;
					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {
							submit(obj);
						}
					});
				}

			}
			spfe_zddw = spf_zddw.edit();
			spfe_zddw.putString("data", array.toString());
			spfe_zddw.commit();
			Message msg = new Message();
			msg.what = Constant.NUM_8;
			handler.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = Constant.NETWORK_ERROR;
			handler.sendMessage(msg);
		}
	}

	private void saveData(String jsonStr){
		try {
			String zbhs = "";
			JSONObject obj = new JSONObject(jsonStr);
			String dataStr = spf_zddw.getString("data", "[]");
			JSONArray jsonarray = new JSONArray(dataStr);
			JSONArray array_ = new JSONArray();
			for(int i=0;i<jsonarray.length();i++){
				JSONObject json = (JSONObject) jsonarray.get(i);
				if(!json.getString("zbh").equals(obj.getString("zbh"))){
					array_.put(json);
				}else{
					zbhs+=obj.getString("zbh")+",";
				}
			}
			spfe_zddw = spf_zddw.edit();
			spfe_zddw.putString("data", array_.toString());
			spfe_zddw.commit();
			Bundle bundle = new Bundle();
			bundle.putString("zbhs", zbhs);
			Message msg = new Message();
			msg.what = Constant.NUM_6;
			msg.setData(bundle);
			handler.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = Constant.NETWORK_ERROR;
			handler.sendMessage(msg);
		}
	}

	private void submit(JSONObject object){
		try {

			String typeStr = "smdy";
			String zbh = (String) object.get("zbh");

			String str = zbh + "*PAM*" + DataCache.getinition().getUserId();
			str += "*PAM*";
			str += object.getString("oldlat");
			str += "*PAM*";
			str += object.getString("oldlng");
			str += "*PAM*";
			str += object.getString("address");

			JSONObject json = this.callWebserviceImp.getWebServerInfo(
					"c#_PAD_KDG_ALL", str, typeStr, typeStr,
					"uf_json_setdata2", this);
			flag = json.getString("flag");
			if (Integer.parseInt(flag) > 0) {
				Bundle bundle = new Bundle();
				bundle.putString("json", json.toString());
				Message msg = new Message();
				msg.what = Constant.SUCCESS;
				msg.setData(bundle);
				handler.sendMessage(msg);
			}else{
				Message msg = new Message();
				msg.what = Constant.NUM_8;
				handler.sendMessage(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = Constant.NETWORK_ERROR;
			handler.sendMessage(msg);
		}

	}

	private void setLocationClientOption() {

		final LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(0);// 设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);// 禁止启用缓存定位
		option.setPriority(LocationClientOption.GpsFirst);
		option.setAddrType("all");
		mLocClient.setLocOption(option);
		mLocClient.start();

	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation locations) {
			if (locations == null) {
				return;
			} else {
				location = locations;
				Message msg = new Message();
				msg.what = Constant.NUM_7;// 成功
				handler.sendMessage(msg);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {

		}

		@Override
		public void onConnectHotSpotMessage(String arg0, int arg1) {
			// TODO Auto-generated method stub

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
		notification.tickerText = msgStr;
		notification.defaults = Notification.DEFAULT_ALL;
		long[] vibrate = { 0, 100, 200, 300 };
		notification.vibrate = vibrate;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		// audioStreamType的值必须AudioManager中的值，代表着响铃的模式
		AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mAudioManager.setStreamVolume(android.media.AudioManager.STREAM_ALARM,
				7, 0); // tempVolume:音量绝对值
		notification.audioStreamType = AudioManager.ADJUST_RAISE;

		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		RemoteViews remoteViews = new RemoteViews(getPackageName(),
				R.layout.notification_remont);
		remoteViews.setImageViewResource(R.id.imageView1, R.drawable.logo);
		remoteViews.setTextViewText(R.id.txtnotification, msgStr);
		notification.contentView = remoteViews;
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_ONE_SHOT);
		notification.contentIntent = pendingIntent;
		manager.notify(num, notification);

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case Constant.NETWORK_ERROR:
					break;

				case Constant.SUCCESS:
					String jsonStr = msg.getData().getString("json");
					saveData(jsonStr);
					break;
				case Constant.NUM_6:
					String zbhs = msg.getData().getString("zbhs");
					if(!"".equals(zbhs)){
						num=0;
						zbhs = zbhs.substring(0, zbhs.length()-1);
						String[] zbhArr = zbhs.split(",");
						for(int i=0;i<zbhArr.length;i++){
							String zbh = zbhArr[i];
							msgStr = "工单"+zbh+"自动定位成功！";
							num++;
							addNotificaction();
						}
					}
					break;
				case Constant.NUM_7:
					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {
							queryZddw();
						}
					});
					break;
				case Constant.NUM_8:
					break;
			}
		}

	};

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		if(wakeLock!= null){
			wakeLock.release();
			wakeLock = null;
		}
		if(mLocClient!=null){
			mLocClient.stop();
		}
		super.onDestroy();
	}

}
