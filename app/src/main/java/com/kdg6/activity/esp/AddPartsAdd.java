package com.kdg6.activity.esp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
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
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kdg6.R;
import com.kdg6.Parser.JSONObjectParser;
import com.kdg6.activity.FrameActivity;
import com.kdg6.cache.DataCache;
import com.kdg6.utils.Config;

public class AddPartsAdd extends FrameActivity{

	private TextView part_etx_pjdl,part_etx_pjzl,part_etx_pjxl;
	private EditText part_etx_sl,part_etx_bz;
	private RadioGroup rg_sfhs;
	private String flag;
	private ArrayList<Map<String, String>> data,dl_data,zl_data,xl_data;
	private Button confirm, cancel;
	private JSONObject jsonObject;
	private JSONObjectParser jsonObjectParser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_addpartsadd);
		initVariable();
		initView();
		initListeners();
	}

	@Override
	protected void initVariable() {
		data = new ArrayList<Map<String, String>>();
		dl_data = new ArrayList<Map<String, String>>();
		zl_data = new ArrayList<Map<String, String>>();
		xl_data = new ArrayList<Map<String, String>>();
		jsonObjectParser = new JSONObjectParser(this);

		try {
//			//大类
//			String dl_str = Config.readFile("_PAD_SBPJDL", this);
//			JSONObject jsonObject_dl = new JSONObject(dl_str);
//			dl_data = jsonObjectParser.PjlxParser(jsonObject_dl,"dl");
//			//中类
//			String zl_str = Config.readFile("_PAD_SBPJZL", this);
//			JSONObject jsonObject_zl = new JSONObject(zl_str);
//			zl_data = jsonObjectParser.PjlxParser(jsonObject_zl,"zl");
			//小类
			String xl_str = Config.readFile("_PAD_SBPJXL", this);
			JSONObject jsonObject_xl = new JSONObject(xl_str);
			xl_data = jsonObjectParser.PjlxParser(jsonObject_xl,"xl");
		} catch (Exception e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = FAIL;// 失败
			handler.sendMessage(msg);
		}
	}

	@Override
	protected void initView() {
		part_etx_pjdl = (TextView) findViewById(R.id.part_etx_pjdl);
		part_etx_pjzl = (TextView) findViewById(R.id.part_etx_pjzl);
		part_etx_pjxl = (TextView) findViewById(R.id.part_etx_pjxl);

		rg_sfhs = (RadioGroup) findViewById(R.id.rg_sfhs);

		part_etx_sl = (EditText) findViewById(R.id.part_etx_sl);
		part_etx_bz = (EditText) findViewById(R.id.part_etx_bz);
		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);

		title.setText("新增配件");
	}

	@Override
	protected void initListeners() {
		OnClickListener backonClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);

		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(part_etx_pjxl.getTag()==null||"".equals(part_etx_pjxl.getTag())){
					dialogShowMessage_P("请选择配件小类", null);
				}else if("".equals(part_etx_sl.getText().toString())||"0".equals(part_etx_sl.getText().toString())){
					dialogShowMessage_P("请录入配件数量", null);
				}else{
					String name = part_etx_pjxl.getText().toString();
					String id = part_etx_pjxl.getTag().toString();
					String num = part_etx_sl.getText().toString();
					String bz = part_etx_bz.getText().toString();
					String sfhs = "否";
					if(rg_sfhs.getCheckedRadioButtonId()==R.id.rb_s){
						sfhs = "是";
					}
					Intent intent = getIntent();

					intent.putExtra("id", id);
					intent.putExtra("name", name);
					intent.putExtra("num", num);
					intent.putExtra("bz", bz);
					intent.putExtra("sfhs", sfhs);
					setResult(21, intent);
					finish();
				}
			}
		});

		part_etx_pjxl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AddPartsAdd.this,
						ChoosePjlx.class);
				intent.putExtra("type", "xl");
				intent.putExtra("data", xl_data);
				startActivityForResult(intent, 13);

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 11 && resultCode == 11 && data != null) {
			part_etx_pjdl.setText(data.getStringExtra("name"));
			part_etx_pjdl.setTag(data.getStringExtra("id"));

			part_etx_pjzl.setText("");
			part_etx_pjzl.setTag("");
			part_etx_pjxl.setText("");
			part_etx_pjxl.setTag("");
		}else if (requestCode == 12 && resultCode == 12 && data != null) {
			part_etx_pjzl.setText(data.getStringExtra("name"));
			part_etx_pjzl.setTag(data.getStringExtra("id"));

			part_etx_pjxl.setText("");
			part_etx_pjxl.setTag("");
		}else if(requestCode == 13 && resultCode == 13 && data != null) {
			part_etx_pjxl.setText(data.getStringExtra("name"));
			part_etx_pjxl.setTag(data.getStringExtra("id"));
			if("1".equals(data.getStringExtra("sfhs"))){
				rg_sfhs.check(R.id.rb_s);
			}else{
				rg_sfhs.check(R.id.rb_f);
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void getWebService(String s) {

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			switch (msg.what) {
				case NETWORK_ERROR:
					dialogShowMessage_P("网络连接出错，请检查你的网络设置", null);
					break;

				case SUCCESSFUL:
					dialogShowMessage_P("设备信息添加成功", null);
					break;
				case 2:

					break;

				case FAIL:
					dialogShowMessage_P("设备信息添加失败，请检查网络后重试", null);
					break;
				case -2:
					dialogShowMessage_P("信息查询失败，请检查网络后重试...", null);
					break;

			}
		}

	};
}
