package com.kdg6.activity.kdg;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kdg6.R;
import com.kdg6.activity.FrameActivity;
import com.kdg6.activity.main.MainActivity;
import com.kdg6.cache.DataCache;
import com.kdg6.utils.DateTimePickerDialog;
/**
 * 快递柜-近期工单查询-筛选条件
 * @author zdkj
 *
 */
public class JqgdcxKdg extends FrameActivity {

	private Button confirm,cancel;
	private TextView tv_start,tv_end;
	private String type = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_jqgdcx);
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
		confirm.setText("查询");
		cancel.setText("取消");

	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		tv_start = (TextView) findViewById(R.id.tv_start);
		tv_end = (TextView) findViewById(R.id.tv_end);

		type = getIntent().getStringExtra("type");

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		tv_start.setText(sdf.format(date));
		tv_end.setText(sdf.format(date));
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
						if("m_pad_kdg_jqgdxz".equals(type)){
							DataCache.getinition().setQueryType(205);
							Intent intent = new Intent(JqgdcxKdg.this, ListKdg.class);
							intent.putExtra("status", tv_start.getText().toString()+"*"+tv_end.getText().toString());
							startActivity(intent);
						}else{
							DataCache.getinition().setQueryType(2901);
							Intent intent = new Intent(JqgdcxKdg.this, ListKdg.class);
							intent.putExtra("status", tv_start.getText().toString()+"*"+tv_end.getText().toString());
							startActivity(intent);
						}

						break;
					default:
						break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);

		tv_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DateTimePickerDialog dateTimePicKDialog = new DateTimePickerDialog(JqgdcxKdg.this);
				dateTimePicKDialog.dateTimePicKDialog(tv_start, 1);

			}
		});

		tv_end.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DateTimePickerDialog dateTimePicKDialog = new DateTimePickerDialog(JqgdcxKdg.this);
				dateTimePicKDialog.dateTimePicKDialog(tv_end, 1);

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
