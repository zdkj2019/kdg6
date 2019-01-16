package com.kdg6.activity.kdg;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kdg6.R;
import com.kdg6.activity.FrameActivity;
import com.kdg6.activity.main.MainActivity;
import com.kdg6.cache.DataCache;
import com.kdg6.cache.ServiceReportCache;
import com.kdg6.common.Constant;
import com.kdg6.utils.Config;
import com.kdg6.utils.ImageUtil;
import com.kdg6.activity.util.BaiduMapActivity;
/**
 * 快递柜-近期工单查询-数据展示
 * @author zdkj
 *
 */
public class JqgdcxShowKdg extends FrameActivity {

	private Button confirm,cancel;
	private String flag,zbh,type="1",msgStr,lxdh,sfkxg,keyStr;
	private ImageView iv_telphone;
	private TextView tv_curr;
	private List<Map<String, Object>> datalist;
	private ArrayList<Map<String, String>> data_zp;
	private Map<String, ArrayList<String>> filemap;
	private LinearLayout ll_show;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_jqgdcxshow);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				getWebService("query");
			}
		});
	}

	@Override
	protected void initVariable() {

		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("确定");
		cancel.setText("取消");
		ll_show = (LinearLayout) findViewById(R.id.ll_show);
		iv_telphone = (ImageView) findViewById(R.id.iv_telphone);
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());

		Map<String, Object> itemmap = ServiceReportCache.getObjectdata().get(ServiceReportCache.getIndex());

		zbh = itemmap.get("zbh").toString();

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
						if("y".equals(sfkxg)){
							showProgressDialog();
							Config.getExecutorService().execute(new Runnable() {

								@Override
								public void run() {
									getWebService("submit");
								}
							});
						}else{
							onBackPressed();
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

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			ArrayList<String> list = data.getStringArrayListExtra("imglist");
			loadImg(list);
		}
	}

	@Override
	protected void getWebService(String s) {
		if (s.equals("query")) {
			try {
				datalist = new ArrayList<Map<String, Object>>();
				JSONObject jsonObject = null;
				jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_KDG_YWCX_JQGDCX2", zbh, "uf_json_getdata",
						this);
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, Object> item = new HashMap<String, Object>();
						String timeff = "";
						item.put("textView1", getListItemIcon(i));
						timeff = temp.getString("bzsj");
						item.put("bzsj", timeff);
						item.put("faultuser", temp.getString("xqmc") + "   "
								+ temp.getString("xxdz"));
						item.put("zbh", temp.getString("zbh"));
						item.put("sx", temp.getString("sx"));
						item.put("qy", temp.getString("qy"));
						item.put("djzt", temp.getString("djzt"));
						item.put("xqmc", temp.getString("xqmc"));
						item.put("xxdz", temp.getString("xxdz"));
						item.put("gzxx", temp.getString("gzxx"));
						item.put("ywlx", temp.getString("ywlx"));
						item.put("ywlx2", temp.getString("ywlx2"));
						item.put("bz", temp.getString("bz"));
						item.put("ds", temp.getString("ds"));
						item.put("fwnr", temp.getString("fwnr"));
						item.put("kzzf1", temp.getString("kzzf1"));

						item.put("dygdh1_mc", temp.getString("dygdh1_mc"));
						item.put("dygdh2_mc", temp.getString("dygdh2_mc"));
						item.put("axdh", temp.getString("axdh"));
						item.put("ddh", temp.getString("ddh"));
						item.put("ddh_mc", temp.getString("ddh_mc"));
						item.put("yqsx", temp.getString("yqsx"));
						item.put("lxdh", temp.getString("lxdh"));

						item.put("kzsz4", temp.getString("kzsz4"));
						item.put("jsxh", temp.getString("jsxh"));
						item.put("jddz", temp.getString("jddz"));
						item.put("sfcs", temp.getString("sfcs"));
						item.put("wcsj", temp.getString("wcsj"));
						item.put("djzt2", temp.getString("djzt2"));
						item.put("sfkxg", temp.getString("sfkxg"));

						item.put("kzzf3_bm", temp.getString("kzzf3_bm"));
						item.put("kzzf4_bm", temp.getString("kzzf4_bm"));
						item.put("kzzf5_bm", temp.getString("kzzf5_bm"));

						item.put("timemy", temp.getString("ddh_mc"));
						item.put("datemy", temp.getString("djzt2"));
						datalist.add(item);
					}

					jsonObject = callWebserviceImp.getWebServerInfo(
							"_PAD_KDG_YWCX_WXTZL", zbh + "*" + zbh + "*"+ zbh, "uf_json_getdata", this);
					flag = jsonObject.getString("flag");
					data_zp = new ArrayList<Map<String, String>>();
					if (Integer.parseInt(flag) > 0) {
						jsonArray = jsonObject.getJSONArray("tableA");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject temp = jsonArray.getJSONObject(i);
							Map<String, String> item = new HashMap<String, String>();
							item.put("tzlmc", temp.getString("tzlmc"));
							item.put("kzzf1", temp.getString("kzzf1"));
							item.put("kzsz1", temp.getString("kzsz1"));
							item.put("str", temp.getString("str"));
							item.put("mxh", temp.getString("mxh"));
							item.put("tzz", temp.getString("tzz"));
							item.put("path", temp.getString("path"));
							data_zp.add(item);
						}
					}


					Message msg = new Message();
					msg.what = Constant.NUM_6;
					handler.sendMessage(msg);
				}else{
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

		if (s.equals("submit")) {// 提交
			try {
				String cs = "";
				for (int i = 0; i < ll_show.getChildCount(); i++) {
					LinearLayout ll = (LinearLayout) ll_show.getChildAt(i);
					if (ll.getChildAt(1) instanceof LinearLayout) {
						ll = (LinearLayout) ll.getChildAt(1);
						if (ll.getChildAt(0) instanceof RadioGroup) {
							RadioGroup rg = (RadioGroup) ll.getChildAt(0);
							RadioButton rb = (RadioButton) rg.findViewById(rg
									.getCheckedRadioButtonId());
							if (rb == null) {
								cs += "#@##@#0";
							} else {
								String che = rb.getId() == R.id.rb_1 ? "1"
										: "2";
								cs += rb.getTag().toString() +"#@#" + rb.getText().toString();
							}
							cs += "#^#";
						} else if (ll.getChildAt(0) instanceof EditText) {
							EditText et = (EditText) ll.getChildAt(0);
							CheckBox cb = (CheckBox) ll.getChildAt(1);
							String che = cb.isChecked() ? "1" : "2";
							cs += et.getTag().toString()+ "#@#" + et.getText().toString();
							cs += "#^#";
						}
					}

				}
				if (!"".equals(cs)) {
					cs = cs.substring(0, cs.length() - 3);
				}

				// 再提交服务报告
				String str = "";
				String typeStr = "az_fwbg_xg";

				str = zbh + "*PAM*" + DataCache.getinition().getUserId();
				str += "*PAM*";
				str += cs;


				JSONObject json = this.callWebserviceImp.getWebServerInfo(
						"c#_PAD_KDG_ALL", str,
						DataCache.getinition().getUserId(), typeStr,
						"uf_json_setdata2", this);
				flag = json.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					upload();
					// Message msg = new Message();
					// msg.what = Constant.SUCCESS;
					// handler.sendMessage(msg);
				} else {
					msgStr = json.getString("msg");
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

	private void upload() {
		try {
			boolean flag = true;
			List<Map<String, String>> filelist = new ArrayList<Map<String, String>>();
			for (String mxh : filemap.keySet()) {
				List<String> filepathlist = filemap.get(mxh);
				for (int j = 0; j < filepathlist.size(); j++) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("mxh", mxh);
					map.put("num", j + "");
					String path = filepathlist.get(j);
					map.put("filepath", path);
					if(path.indexOf(Constant.ImgPath)==-1){
						filelist.add(map);
					}
				}
			}
			int filenum = filelist.size();
			for (int i = 0; i < filenum; i++) {
				Map<String, String> map = filelist.get(i);
				if (flag) {
					String mxh = map.get("mxh");
					String filepath = map.get("filepath");
					String num = map.get("num");
					filepath = filepath.substring(7, filepath.length());
					// 压缩图片到100K
					filepath = ImageUtil
							.compressAndGenImage(
									convertBitmap(new File(filepath),
											getScreenWidth()), 200, "jpg");
					File file = new File(filepath);
					// toastShowMessage("开始上传第" + (i + 1) + "/" + filenum +
					// "张图片");
					flag = uploadPic(num, mxh, readJpeg(file),
							"uf_json_setdata", zbh, "c#_PAD_KDGAZ_FWBG_ZPXG");
					file.delete();
				} else {
					flag = false;
					break;
				}
			}
			if (flag) {
				Message msg = new Message();
				msg.what = Constant.NUM_12;
				handler.sendMessage(msg);
			} else {
				Message msg = new Message();
				msg.what = Constant.NUM_12;
				handler.sendMessage(msg);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = Constant.NUM_12;
			handler.sendMessage(msg);
		}

	}

	@SuppressLint("ResourceAsColor")
	private void loadData(){
		Map<String, Object> itemmap = datalist.get(0);
		lxdh = itemmap.get("lxdh").toString();
		sfkxg = itemmap.get("sfkxg").toString();
		keyStr = itemmap.get("xqmc").toString();
		((TextView) findViewById(R.id.tv_1)).setText(zbh);
		((TextView) findViewById(R.id.tv_2)).setText(itemmap.get("axdh").toString());
		((TextView) findViewById(R.id.tv_3)).setText(itemmap.get("sx").toString());
		((TextView) findViewById(R.id.tv_4)).setText(itemmap.get("qy").toString());
		((TextView) findViewById(R.id.tv_5)).setText(itemmap.get("xqmc").toString());
		((TextView) findViewById(R.id.tv_6)).setText(itemmap.get("xxdz").toString());
		((TextView) findViewById(R.id.tv_8)).setText(itemmap.get("bzsj").toString());
		((TextView) findViewById(R.id.tv_9)).setText(itemmap.get("yqsx").toString());
		((TextView) findViewById(R.id.tv_10)).setText(itemmap.get("lxdh").toString());
		((TextView) findViewById(R.id.tv_13)).setText(itemmap.get("gzxx").toString());
		((TextView) findViewById(R.id.tv_14)).setText(itemmap.get("sfcs").toString());
		((TextView) findViewById(R.id.tv_15)).setText(itemmap.get("wcsj").toString());
		((TextView) findViewById(R.id.tv_16)).setText(itemmap.get("djzt2").toString());
		((TextView) findViewById(R.id.tv_17)).setText(itemmap.get("bz").toString());
		((TextView) findViewById(R.id.tv_jddz)).setText(itemmap.get("jddz").toString());

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


	}

	private void loadImg(final ArrayList<String> list) {
		try {
			String mxh = tv_curr.getTag().toString();
			if (list.size() > 0) {
				tv_curr.setText("继续选择");
				tv_curr.setBackgroundResource(R.drawable.btn_normal_yellow);
			} else {
				tv_curr.setText("选择图片");
				tv_curr.setBackgroundResource(R.drawable.btn_normal);
			}
			filemap.put(mxh, list);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressLint("ResourceAsColor")
	protected void loadSbxx(ArrayList<Map<String, String>> data, LinearLayout ll) {
		String errormsg = "";
		try {
			filemap = new HashMap<String, ArrayList<String>>();
			ll.removeAllViews();
			for (int i = 0; i < data.size(); i++) {
				Map<String, String> map = data.get(i);
				String title = map.get("tzlmc");
				errormsg = title;
				String type = map.get("kzzf1");
				String content = map.get("str");
				String tzz = map.get("tzz") == null ? "" : map.get("tzz").toString();
				View view = null;
				if ("2".equals(type)) {// 输入
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.include_xj_text, null);
					TextView tv_name = (TextView) view
							.findViewById(R.id.tv_name);
					EditText et_val = (EditText) view.findViewById(R.id.et_val);
					et_val.setText(tzz);
					et_val.setTag(map.get("mxh"));
					tv_name.setText(title);

				} else if ("1".equals(type)) {// 选择
					String[] contents = content.split(",");
					if (contents.length == 2) {
						view = LayoutInflater.from(getApplicationContext())
								.inflate(R.layout.include_xj_aqfx, null);
						TextView tv_name = (TextView) view
								.findViewById(R.id.tv_name);
						RadioButton rb_1 = (RadioButton) view
								.findViewById(R.id.rb_1);
						RadioButton rb_2 = (RadioButton) view
								.findViewById(R.id.rb_2);
						rb_1.setText(contents[0]);
						rb_2.setText(contents[1]);
						rb_1.setTag(map.get("mxh"));
						rb_2.setTag(map.get("mxh"));
						if (tzz.equals(contents[0])) {
							rb_1.setChecked(true);
						} else if (tzz.equals(contents[1])) {
							rb_2.setChecked(true);
						}
						tv_name.setText(title);

					} else if (contents.length == 3) {
						view = LayoutInflater.from(getApplicationContext())
								.inflate(R.layout.include_xj_type_2, null);
						TextView tv_name = (TextView) view
								.findViewById(R.id.tv_name);
						RadioButton rb_1 = (RadioButton) view
								.findViewById(R.id.rb_1);
						RadioButton rb_2 = (RadioButton) view
								.findViewById(R.id.rb_2);
						RadioButton rb_3 = (RadioButton) view
								.findViewById(R.id.rb_3);
						rb_1.setText(contents[0]);
						rb_2.setText(contents[1]);
						rb_3.setText(contents[2]);
						rb_1.setTag(map.get("mxh"));
						rb_2.setTag(map.get("mxh"));
						rb_3.setTag(map.get("mxh"));
						if (tzz.equals(contents[0])) {
							rb_1.setChecked(true);
						} else if (tzz.equals(contents[1])) {
							rb_2.setChecked(true);
						} else if (tzz.equals(contents[2])) {
							rb_3.setChecked(true);
						}
						tv_name.setText(title);

					}

				} else if ("3".equals(type)) {// 图片
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.include_xj_pz, null);
					TextView tv_name = (TextView) view
							.findViewById(R.id.tv_name);
					tv_name.setText(title);
					TextView tv_1 = (TextView) view.findViewById(R.id.tv_1);
					final String mxh = map.get("mxh");
					tv_1.setTag(mxh);
					String path = map.get("path");
					try {
						if(!"".equals(path)){
							String[] paths = path.split(",");
							ArrayList<String> arraylist = new ArrayList<String>();
							for(int n=0;n<paths.length;n++){
								arraylist.add(Constant.ImgPath+"/"+tzz+"/"+paths[n]);
							}
							filemap.put(mxh, arraylist);
							tv_1.setText("继续选择");
							tv_1.setBackgroundResource(R.drawable.btn_normal_yellow);
						}
					} catch (Exception e) {

					}
					tv_1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							tv_curr = (TextView) v;
							ArrayList<String> list = filemap.get(mxh);
							camera(1, list);
						}
					});

				} else if ("4".equals(type)) { // 日期
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.include_xj_text, null);
					TextView tv_name = (TextView) view
							.findViewById(R.id.tv_name);
					final EditText et_val = (EditText) view
							.findViewById(R.id.et_val);
					et_val.setFocusable(false);
					et_val.setTag(map.get("mxh"));
					et_val.setText(tzz);
					et_val.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dateDialog(et_val);
						}
					});
					tv_name.setText(title);

				} else if ("5".equals(type)) { // 数值
					view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.include_xj_text, null);
					TextView tv_name = (TextView) view
							.findViewById(R.id.tv_name);
					EditText et_val = (EditText) view.findViewById(R.id.et_val);
					et_val.setInputType(InputType.TYPE_CLASS_NUMBER);
					et_val.setTag(map.get("mxh"));
					et_val.setText(tzz);
					tv_name.setText(title);

				}

				ll.addView(view);
			}
		} catch (Exception e) {
			dialogShowMessage_P("数据错误:" + errormsg + ",选项数据类型不匹配,请联系管理员修改",
					null);
			e.printStackTrace();
		}
		if(data.size()==0){
			findViewById(R.id.ll_show_title).setVisibility(View.GONE);
			findViewById(R.id.ll_show).setVisibility(View.GONE);
		}
		loadOther();
	}

	@SuppressLint("ResourceAsColor")
	private void loadOther(){
		if("y".equals(sfkxg)){
			confirm.setText("修改");
		}else{
			for (int i = 0; i < ll_show.getChildCount(); i++) {
				LinearLayout ll = (LinearLayout) ll_show.getChildAt(i);
				if (ll.getChildAt(1) instanceof LinearLayout) {
					ll = (LinearLayout) ll.getChildAt(1);
					if (ll.getChildAt(0) instanceof RadioGroup) {
						RadioGroup rg = (RadioGroup) ll.getChildAt(0);
						rg.setEnabled(false);
					} else if (ll.getChildAt(0) instanceof EditText) {
						EditText et = (EditText) ll.getChildAt(0);
						et.setTextColor(R.color.gray);
						et.setEnabled(false);
					}
				}

			}
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case Constant.FAIL:
					dialogShowMessage_P("失败，错误标识：" + flag, null);
					break;
				case Constant.SUCCESS:
					break;
				case Constant.NETWORK_ERROR:

					dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
					break;
				case Constant.NUM_6:
					loadData();
					loadSbxx(data_zp, ll_show);
					break;
				case Constant.NUM_12:
					dialogShowMessage_P("提交成功",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface face,
											int paramAnonymous2Int) {
							onBackPressed();
						}
					});
					break;
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	};


}
