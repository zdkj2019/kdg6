package com.kdg6.activity.kdg;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.kdg6.R;
import com.kdg6.activity.FrameActivity;
import com.kdg6.activity.main.MainActivity;
import com.kdg6.cache.DataCache;
/**
 * 组长片区工单查询
 * @author zdkj
 *
 */
public class Zzpqgdcx extends FrameActivity {

	private Button confirm,cancel;
	private RadioGroup rg_1,rg_2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_zzpqgdcx);
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
		confirm.setText("确定");
		cancel.setText("取消");

	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		rg_1 = (RadioGroup) findViewById(R.id.rg_1);
		rg_2 = (RadioGroup) findViewById(R.id.rg_2);
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
						DataCache.getinition().setQueryType(2501);
						Intent intent = new Intent(Zzpqgdcx.this, ListKdg.class);
						intent.putExtra("status", rg_1.getCheckedRadioButtonId()==R.id.rb_1_s?1:2);
						intent.putExtra("sj", rg_2.getCheckedRadioButtonId()==R.id.rb_2_s?1:2);
						startActivity(intent);
						break;
					default:
						break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);

		rg_1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == R.id.rb_1_s){
					findViewById(R.id.ll_gdsj).setVisibility(View.VISIBLE);
					findViewById(R.id.ll_gdsj_line).setVisibility(View.VISIBLE);
				}else{
					findViewById(R.id.ll_gdsj).setVisibility(View.GONE);
					findViewById(R.id.ll_gdsj_line).setVisibility(View.GONE);
				}
			}
		});
	}

	@Override
	protected void getWebService(String s) {


	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			}
			if(progressDialog!=null){
				progressDialog.dismiss();
			}
		}
	};

//	@Override
//	public void onBackPressed() {
//		Intent intent = new Intent(this, MainActivity.class);
//		intent.putExtra("currType", 5);
//		startActivity(intent);
//		finish();
//	}

}
