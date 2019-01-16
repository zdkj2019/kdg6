package com.kdg6.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.w3c.dom.Text;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kdg6.R;
import com.kdg6.activity.login.LoginActivity;
import com.kdg6.activity.util.ImgChoose;
import com.kdg6.activity.util.ImgGridViewUtil;
import com.kdg6.activity.util.ImgPz;
import com.kdg6.cache.DataCache;
import com.kdg6.definition.Sign;
import com.kdg6.utils.ImageUtil;
import com.kdg6.webservice.CallWebserviceImp;

@SuppressLint("SimpleDateFormat")
public abstract class FrameActivity extends BaseActivity implements Sign {

	protected CallWebserviceImp callWebserviceImp = new CallWebserviceImp();
	protected TextView title, showUserId;
	protected ImageView topBack;
	protected Calendar c = Calendar.getInstance();
	protected int m_year, m_month, m_day;
	protected List<String> wxaz;

	// protected DataCache dataCache = DataCache.getinition();

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// if (null != savedInstanceState)

		// {
		// Log.e("ddd","null != savedInstanceState");
		// Object saved = savedInstanceState.getSerializable("datacache");
		// if (saved instanceof Stack) {
		// @SuppressWarnings("unused")
		// Stack<DataCache> mLocationInfoVector = (Stack<DataCache>) saved;
		// } else if (saved != null) {
		// Log.e("RestoreBug", "Restored object class: "
		// + saved.getClass().getName());
		// }
		// // System.err.println("savedInstanceState="
		// // + savedInstanceState.getSerializable("datacache"));
		// // DataCache
		// // .setDataCache((com.kdg6.cache.DataCache) savedInstanceState
		// // .getSerializable("datacache"));
		//
		// List<String> menu = DataCache.getinition().getMenu();
		// String string = menu.get(0);
		// Log.e("dd", "menu.get(0)=" + string);
		//
		// String StrTest = savedInstanceState.getString("StrTest");
		//
		// Log.e("dd", "onCreate get the savedInstanceState+IntTest="
		// + "+StrTest=" + StrTest);
		//
		// }
		setContentView(R.layout.activity_frame);
		topBack = (ImageView) findViewById(R.id.bt_topback);
		title = (TextView) findViewById(R.id.interfacename);
		m_year = c.get(Calendar.YEAR);
		m_month = c.get(Calendar.MONTH) + 1;
		m_day = c.get(Calendar.DAY_OF_MONTH);
		wxaz = new LinkedList<String>();
		wxaz.add("巡检");// 1
		wxaz.add("维修");// 2
		wxaz.add("安装");// 3
		wxaz.add("其他");// 4
		wxaz.add("撤机");// 5
		wxaz.add("换密键");// 6
		wxaz.add("升级");// 7
	}

	/**
	 * 获取屏幕宽度
	 *
	 * @return
	 */
	protected int getScreenWidth() {
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;// 宽度
		return width;
	}

	/**
	 * 获取屏幕高度
	 *
	 * @return
	 */
	protected int getScreenHeight() {
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int height = dm.heightPixels;// 高度
		return height;
	}

	// @Override
	// public void onSaveInstanceState(Bundle savedInstanceState) {
	//
	// // Save away the original text, so we still have it if the activity
	//
	// // needs to be killed while paused.
	//
	// savedInstanceState.putSerializable("datacache", DataCache.class);
	//
	// super.onSaveInstanceState(savedInstanceState);
	//
	// Log.e("dd", "onSaveInstanceState");
	//
	// }
	//
	// @Override
	// public void onRestoreInstanceState(Bundle savedInstanceState) {
	//
	// super.onRestoreInstanceState(savedInstanceState);
	//
	// DataCache.setDataCache((DataCache) savedInstanceState
	// .getSerializable("datacache"));
	// List<String> menu = DataCache.getinition().getMenu();
	// String string = menu.get(0);
	// Log.e("ss", "menu.get(0)=" + string);
	//
	// Log.e("dd", "onRestoreInstanceState");
	//
	// }

	protected void appendMainBody(int resId) {
		LinearLayout mainBody = (LinearLayout) findViewById(R.id.layMainBody);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(resId, null);
		LinearLayout.LayoutParams layoutParams = new LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		mainBody.addView(view, layoutParams);
	}

	protected void myDialog(String title, View view,
							OnClickListener confirmListener, OnClickListener cancelListener) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setCancelable(false);
		builder.setTitle(title);
		builder.setView(view);
		builder.setPositiveButton("确认", confirmListener);
		builder.setNegativeButton("取消", cancelListener);
		builder.create().show();
	}

	protected void Call(String tel) {
		Intent phoneIntent = new Intent("android.intent.action.CALL",
				Uri.parse("tel:" + tel));
		startActivity(phoneIntent);
	}

	protected String getUserId(){
		String userid = DataCache.getinition().getUserId();
		if(userid==null){
			SharedPreferences spf = getSharedPreferences("loginsp", LoginActivity.MODE_WORLD_READABLE);
			userid = spf.getString("userId", "");
		}
		return userid;
	}

	// protected void showDateDialog(String title, final Class<?> cls) {
	// final TextView start, end;
	// View view = LayoutInflater.from(this).inflate(R.layout.custom_dialog,
	// null);
	//
	// start = (TextView) view.findViewById(R.id.starttime);
	// end = (TextView) view.findViewById(R.id.endtime);
	//
	// start.setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// new DatePickerDialog(FrameActivity.this,
	// new DatePickerDialog.OnDateSetListener() {
	//
	// @Override
	// public void onDateSet(DatePicker view, int year,
	// int monthOfYear, int dayOfMonth) {
	// // TODO Auto-generated method stub
	// c.set(Calendar.YEAR, year);
	// c.set(Calendar.MONTH, monthOfYear);
	// c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
	// DataCache.setStart(formatTime());
	// // System.out.println("开始时间1-------"
	// // + formatTime());
	// start.setText(formatTime());
	// }
	// }, m_year, m_month, m_day).show();
	// }
	//
	// });
	//
	// end.setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// new DatePickerDialog(FrameActivity.this,
	// new DatePickerDialog.OnDateSetListener() {
	//
	// @Override
	// public void onDateSet(DatePicker view, int year,
	// int monthOfYear, int dayOfMonth) {
	// // TODO Auto-generated method stub
	// c.set(Calendar.YEAR, year);
	// c.set(Calendar.MONTH, monthOfYear);
	// c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
	// DataCache.setEnd(formatTime());
	// // System.out.println("结束时间2-------"
	// // + formatTime());
	// end.setText(formatTime());
	// }
	// }, m_year, m_month, m_day).show();
	// }
	// });
	//
	// myDialog(title, view, new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// // TODO Auto-generated method stub
	// if (DataCache.getStart() != null
	// && !"".equals(DataCache.getStart())
	// && DataCache.getEnd() != null
	// && !"".equals(DataCache.getEnd())) {
	// skipActivity(cls);
	// } else {
	// dialogShowMessage("请设置查询的开始和结束时间", null,null);
	// }
	// }
	// }, null);
	// }

	protected String formatTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String s = df.format(c.getTime());
		return s;
	}

	protected String formatTime2() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String s = df.format(c.getTime());
		return s;
	}

	protected String formatTime3() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		String s = df.format(c.getTime());
		return s;
	}

	protected void dateDialog(TextView textView) {
		final TextView time;
		time = textView;
		new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
								  int dayOfMonth) {
				// TODO Auto-generated method stub
				c.set(Calendar.YEAR, year);
				c.set(Calendar.MONTH, monthOfYear);
				c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				// DataCache.setStart(formatTime2());
				time.setText(formatTime2());
			}
		}, m_year, m_month, m_day).show();
	}

	protected void dateDialog(final EditText tv) {
		new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
								  int dayOfMonth) {
				// TODO Auto-generated method stub
				c.set(Calendar.YEAR, year);
				c.set(Calendar.MONTH, monthOfYear);
				c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				// DataCache.setStart(formatTime2());
				tv.setText(formatTime2());
			}
		}, m_year, m_month, m_day).show();
	}

	protected void MonthDialog(final EditText tv) {
		DatePickerDialog dp = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
										  int monthOfYear, int dayOfMonth) {
						// TODO Auto-generated method stub
						c.set(Calendar.YEAR, year);
						c.set(Calendar.MONTH, monthOfYear);
						c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						// DataCache.setStart(formatTime2());
						tv.setText(formatTime3());
					}
				}, m_year, m_month, m_day);
		dp.show();
	}

	/**
	 * 显示日期控件
	 *
	 * @param v
	 */
	@SuppressLint("NewApi")
	public void showDateSelector(final View v) {
		final Calendar calendar = Calendar.getInstance();
		final EditText et = (EditText) v;
		/**
		 * @description 日期设置匿名类
		 */
		DatePickerDialog.OnDateSetListener DateSet = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
								  int dayOfMonth) {
				// 每次保存设置的日期
				calendar.set(Calendar.YEAR, year);
				calendar.set(Calendar.MONTH, monthOfYear);
				calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				monthOfYear = monthOfYear + 1;
				String month = "";
				String day = "";
				if (monthOfYear < 10) {
					month = "0" + monthOfYear;
				} else {
					month = "" + monthOfYear;
				}
				if (dayOfMonth < 10) {
					day = "0" + dayOfMonth;
				} else {
					day = "" + dayOfMonth;
				}
				String str = year + "-" + month + "-" + day;
				// String str = year + month;
				et.setText(str);
			}
		};
		DatePickerDialog datePickerDialog = new DatePickerDialog(this, DateSet,
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		datePickerDialog.getDatePicker().setCalendarViewShown(false);
		datePickerDialog.show();
	}

	protected String mdateformat(int format, String time) {

		String[] times = time.split(" ");
		String t = times[format];

		if (time.indexOf("1990") != -1) {
			t = "";
		}
		if (format == 1) {
			String[] strings = t.split(":");
			t = strings[0] + ":" + strings[1];
		}
		return t;
	}

	protected int addone(int index2) {
		if (index2 >= 2) {
			index2 = index2 + 1;
		}
		return ++index2;
	}

	/**
	 * 根据data里面的arg字段的值s 返回 arg所在的index
	 *
	 * @param data
	 * @param s
	 * @param arg
	 * @return
	 */
	protected int setChooseOptional(List<Map<String, String>> data, String s,
									String arg) {
		if (s != null && !"".equals(s)) {
			for (int i = 0; i < data.size(); i++) {
				if (s.equals(data.get(i).get(arg))) {
					return i;
				}
			}
		}
		return 0;
	}

	/**
	 * EditText的text是否为空
	 *
	 * @param v
	 *            EditText
	 * @return boolean 非空 -》true
	 */
	protected boolean isNotNull(View v) {
		String temp = null;
		if (v instanceof EditText) {
			temp = ((EditText) v).getText().toString();
		} else if (v instanceof TextView) {
			temp = ((TextView) v).getText().toString();
		}
		if (temp != null && !"".equals(temp.trim())) {
			return true;
		} else {
			if (v != null && v.getTag() != null) {
				String left = v.getTag() + "";
				// Toast.makeText(this, "请填写完"+left, Toast.LENGTH_SHORT).show();
			}
			return false;
		}
	}

	/**
	 * spinner 选中的不是第一个（id！=0）
	 *
	 * @param v
	 *            Spinner
	 * @return boolean
	 */
	protected boolean isNotOne(View v) {
		long i = ((Spinner) v).getSelectedItemId();
		if (i > 0) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 *
	 * @param s
	 *            一个map的tostring
	 *            重多少位开始取
	 * @return 取得的string
	 */
	protected String getId(String s) {
		if (!"".equals(s) && s != null) {
			String[] s1 = s.split(",");
			String temp = s1[0];

			return temp.substring(4, temp.length());
		}
		return "";
	}

	/**
	 *
	 * 获取bitmap尺寸缩放解决OOM问题
	 *
	 * @throws IOException
	 */
	protected Bitmap convertBitmap(File file, int REQUIRED_SIZE)
			throws Exception {
		Bitmap bitmap = null;
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		FileInputStream fis = new FileInputStream(file.getAbsolutePath());
		BitmapFactory.decodeStream(fis, null, o);
		fis.close();
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
				break;
			} else {
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}
		}
		BitmapFactory.Options op = new BitmapFactory.Options();
		op.inSampleSize = scale;
		fis = new FileInputStream(file.getAbsolutePath());
		o.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeStream(fis, null, op);
		fis.close();
		return bitmap;
	}

	protected void camera(int requestCode, ArrayList<String> list) {
		Intent intent = new Intent(getApplicationContext(), ImgPz.class);
		intent.putStringArrayListExtra("imglist", list);
		startActivityForResult(intent, requestCode);
	}

	// protected void loadGridView(final GridView gridview,
	// final List<Map<String, Object>> data_list,
	// final ArrayList<String> list) {
	// String[] from = new String[] { "img", "filepath" };
	// int[] to = new int[] { R.id.child_image, R.id.child_text };
	//
	// final SimpleAdapter adapter = new SimpleAdapter(
	// getApplicationContext(), data_list,
	// R.layout.item_grid_image_pz, from, to);
	//
	// adapter.setViewBinder(new ViewBinder() {
	//
	// public boolean setViewValue(View view, Object data,
	// String textRepresentation) {
	// // 判断是否为我们要处理的对象
	// if (view.getId() == R.id.child_image) {
	// Bitmap bitmap = ImageUtil.ratio(data.toString(), 150, 150);
	// ImageView iv = (ImageView) view;
	// iv.setImageBitmap(bitmap);
	// return true;
	// } else {
	// return false;
	// }
	// }
	//
	// });
	//
	// // 配置适配器
	// gridview.setAdapter(adapter);
	// // gridview.setOnItemClickListener(new OnItemClickListener() {
	// //
	// // @Override
	// // public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	// // long arg3) {
	// // Intent intent = new Intent(getApplicationContext(),
	// // ImagePagerActivity.class);
	// // intent.putExtra("imageUrls",
	// // list.toArray(new String[list.size()]));
	// // intent.putExtra("position", 0);
	// // startActivity(intent);
	// // }
	// // });
	//
	// gridview.setOnItemLongClickListener(new OnItemLongClickListener() {
	//
	// @Override
	// public boolean onItemLongClick(AdapterView<?> parent, View view,
	// final int position, long id) {
	// dialogShowMessage("是否删除选中图片?",
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface face,
	// int paramAnonymous2Int) {
	// data_list.remove(position);
	// ImgGridViewUtil
	// .setListViewHeightBasedOnChildren(gridview);
	// adapter.notifyDataSetChanged();
	// }
	// }, null);
	// return false;
	// }
	// });
	// ImgGridViewUtil.setListViewHeightBasedOnChildren(gridview);
	// adapter.notifyDataSetChanged();
	// gridview.setVisibility(View.VISIBLE);
	// }

	protected boolean uploadPic(String num, String mxh, final byte[] data1,
								final String methed, String zbh, String sqlid) throws Exception {

		if (data1 != null && mxh != null) {
			JSONObject json = callWebserviceImp.getWebServerInfo2_pic(sqlid,
					num, zbh + "*" + mxh, "0001", data1,
					"uf_json_setdata2_p11", getApplicationContext());
			String flag = json.getString("flag");
			if ("1".equals(flag)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	protected byte[] readJpeg(File filename) {
		if (filename == null) {
			return null;
		}

		ByteArrayOutputStream baos = null;
		byte[] filebyteArray = null;
		try {
			@SuppressWarnings("resource")
			FileInputStream fis = new FileInputStream(filename);
			baos = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = fis.read(buffer)) >= 0) {
				baos.write(buffer, 0, count);
			}

			filebyteArray = baos.toByteArray();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return filebyteArray;
	}

	protected int getListItemIcon1(String djzt) {
		if ("3".equals(djzt)) {
			return R.drawable.list_item_green;
		} else if ("4".equals(djzt)) {
			return R.drawable.list_item_blue;
		} else if ("4.5".equals(djzt)) {
			return R.drawable.list_item_brown;
		} else if ("5".equals(djzt)) {
			return R.drawable.list_item_red;
		} else if ("6".equals(djzt)) {
			return R.drawable.list_item_pink;
		} else if ("7".equals(djzt)) {
			return R.drawable.list_item_purple;
		}
		return R.drawable.list_item_yellow;
	}

	protected int getListItemIcon(int num) {
		if (num % 4 == 0) {
			return R.drawable.list_item_yellow;
		} else if (num % 4 == 1) {
			return R.drawable.list_item_red;
		} else if (num % 4 == 2) {
			return R.drawable.list_item_blue;
		} else if (num % 4 == 3) {
			return R.drawable.list_item_green;
		}
		return R.drawable.list_item_yellow;
	}

	protected abstract void initVariable();

	protected abstract void initView();

	protected abstract void initListeners();

	protected abstract void getWebService(String s);
}
