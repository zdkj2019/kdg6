package com.kdg6.activity.esp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.kdg6.activity.FrameActivity;
import com.kdg6.utils.Config;
import com.kdg6.R;

public class ChoosePjlx extends FrameActivity {

	private ListView listView1;
	private EditText et_search;
	private List<Map<String, String>> mdata;
	private ArrayList<Map<String, String>> data;
	private String type = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_yytbz);

		initVariable();
		initView();
		initListeners();
		getWebService("");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initVariable() {

		listView1 = (ListView) findViewById(R.id.listView1);
		et_search = (EditText) findViewById(R.id.et_search);
		mdata = new ArrayList<Map<String, String>>();

		Intent intent = getIntent();
		type = intent.getStringExtra("type");
		data = (ArrayList<Map<String, String>>) intent
				.getSerializableExtra("data");
	}

	@Override
	protected void initView() {

		textchange("", "name");
	}

	@Override
	protected void initListeners() {

		listView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// String string = mdata.get(position).get("name");
				//
				// Cursor cursor = query(string, "id");
				// int index = cursor.getColumnIndex("id");
				// String _id;
				// // do {
				// // Map<String, String> mMap = new HashMap<String, String>();
				//
				// _id = cursor.getString(index);
				// Log.e("dd", _id + " : " + string);
				// // } while (cursor.moveToNext());
				//
				// cursor.close();

				if (position >= 0) {

					Intent intent = getIntent();

					intent.putExtra("id", mdata.get(position).get("id"));
					intent.putExtra("name", mdata.get(position).get("name"));
					intent.putExtra("type", mdata.get(position).get("type"));
					intent.putExtra("sfhs", mdata.get(position).get("sfhs"));
					if("dl".equals(type)){
						setResult(11, intent);
					}else if("zl".equals(type)){
						setResult(12, intent);
					}else{
						setResult(13, intent);
					}


					finish();
				}
			}
		});

		et_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(final CharSequence s, int start,
									  int before, int count) {
				textchange(s.toString(), "name");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		topBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}

	@Override
	protected void getWebService(String s) {

	}

	// synchronized
	// private Cursor query(final CharSequence s, String select) {
	//
	// ContentResolver cr = getContentResolver();
	// String likes = "%" + s + "%";
	// // Log.e("dd", likes);
	// // 查找name like %s%的数据
	// Cursor c = cr.query(uri, new String[] { select },
	// "name like ? limit 500", new String[] { likes }, null);
	// // 这里必须要调用 c.moveToFirst将游标移动到第一条数据,不然会出现index -1
	// // requested , with a size of 1错误；cr.query返回的是一个结果集。
	// if (c.moveToFirst() == false) {
	// // 为空的Cursor
	// return null;
	// }
	//
	// return c;
	// }

	/**
	 * 搜索
	 *
	 * @param pipei
	 *            匹配map的哪个字段
	 * @param str
	 *            要匹配的字段
	 *            匹配的数据
	 * @return 匹配的date
	 */
	public List<Map<String, String>> querycache(String pipei, String str,
												List<Map<String, String>> data2) {
		List<Map<String, String>> matchered;
		if (str == null || "".equals(str)) {
			return data2;
		}
		String regex = ".*" + str + ".*";
		Pattern pattern = Pattern.compile(regex);
		matchered = new LinkedList<Map<String, String>>();

		// Map<String, String> map = new HashMap<String, String>();
		// map.put("id", "计算机码");
		// map.put("name", "设备唯一编码");
		// matchered.add(map);
		for (int i = 0; i < data2.size(); i++) {
			Map<String, String> map2 = data2.get(i);
			Matcher matcher = pattern.matcher(map2.get(pipei));
			if (matcher.find()) {

				matchered.add(map2);
			}
		}
		return matchered;
	}

	/**
	 *
	 * @param s
	 *            模糊查询的字段
	 * @param string2
	 *            查询数据库返回的字段
	 */
	private void textchange(final String s, final String string2) {

		// mdata.clear();
		//
		// Cursor c = query(s, string2);
		// if(c == null){
		// return;
		// }
		// int name = c.getColumnIndex("name");
		// do {
		// Map<String, String> mMap = new HashMap<String, String>();
		// mMap.put("name", c.getString(name));
		// mdata.add(mMap);
		//
		// // System.out.println(c.getString(name));
		//
		// } while (c.moveToNext());
		//
		// c.close();
		// SimpleAdapter adapter2 = new SimpleAdapter(getApplicationContext(),
		// mdata, R.layout.item_yytbz, new String[] { "name" },
		// new int[] { R.id.tv_name });
		// listView1.setAdapter(adapter2);

		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				mdata = querycache(string2, s, data);

				Message message = new Message();
				message.what = 1;
				hander.sendMessage(message);
			}
		});
	}

	private Handler hander = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			SimpleAdapter adapter2 = new SimpleAdapter(getApplicationContext(),
					mdata, R.layout.listview_item_filterdata1, new String[] {
					"id", "name" }, new int[] { R.id.tv_id,
					R.id.tv_name });
			listView1.setAdapter(adapter2);

		}

	};

	@Override
	public void onBackPressed() {
		finish();
	}
}
