package com.kdg6.activity.register;

import com.kdg6.R;
import com.kdg6.activity.BaseActivity;
import com.kdg6.activity.login.LoginActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RegisterCompleteActivity extends BaseActivity {

	private Button btn_next;
	private TextView tv_loginid, tv_loginpwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_complete);
		initView();
		initListner();
	}

	private void initView() {
		btn_next = (Button) findViewById(R.id.btn_next);
		tv_loginid = (TextView) findViewById(R.id.tv_loginid);
		tv_loginpwd = (TextView) findViewById(R.id.tv_loginpwd);
		Intent intent = getIntent();
		tv_loginid.setText("");
		tv_loginpwd.setText("");
	}

	private void initListner() {
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialogShowMessage("",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(
										getApplicationContext(),
										LoginActivity.class);
								startActivity(intent);
								finish();
							}
						}, null);
			}
		});
	}
}
