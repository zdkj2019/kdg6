package com.kdg6.receiver;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.kdg6.activity.notify.QpActivity;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

public class MessageReceiver extends XGPushBaseReceiver {

	@Override
	public void onNotifactionClickedResult(Context context,
			XGPushClickedResult arg1) {
	}

	@Override
	public void onDeleteTagResult(Context arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNotifactionShowedResult(Context context,
			XGPushShowedResult arg1) {
		

	}

	@Override
	public void onRegisterResult(Context arg0, int arg1,
			XGPushRegisterResult arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSetTagResult(Context arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextMessage(Context context, XGPushTextMessage message) {
		// TODO Auto-generated method stub
		Intent it = new Intent(context, QpActivity.class);
		it.putExtra("message", message.toString());
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(it);
	}

	@Override
	public void onUnregisterResult(Context arg0, int arg1) {
		// TODO Auto-generated method stub

	}

}
