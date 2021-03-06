package com.kdg6.activity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.KeyEvent;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class CheckPermissionsActivity extends Activity {
	/**
	 */
	protected String[] needPermissions = {
			Manifest.permission.INTERNET,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.ACCESS_NETWORK_STATE,
			Manifest.permission.ACCESS_WIFI_STATE,
			Manifest.permission.READ_PHONE_STATE,
			Manifest.permission.ACCESS_COARSE_LOCATION,
			Manifest.permission.CAMERA
	};

	private static final int PERMISSON_REQUESTCODE = 0;

	/**
	 */
	private boolean isNeedCheck = true;

	@Override
	protected void onResume() {
		super.onResume();
		if (Build.VERSION.SDK_INT >= 23 && getApplicationInfo().targetSdkVersion >= 23) {
			if (isNeedCheck) {
				checkPermissions(needPermissions);
			}
		}
	}

	/**
	 *
	 * @param permissions
	 * @since 2.5.0
	 *
	 */
	private void checkPermissions(String... permissions) {
		try {
			if (Build.VERSION.SDK_INT >= 23 && getApplicationInfo().targetSdkVersion >= 23) {
				List<String> needRequestPermissonList = findDeniedPermissions(permissions);
				if (null != needRequestPermissonList && needRequestPermissonList.size() > 0) {
					String[] array = needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]);
					Method method = getClass().getMethod("requestPermissions", new Class[]{String[].class, int.class});
					method.invoke(this, array, PERMISSON_REQUESTCODE);
				}
			}
		} catch (Throwable e) {
		}
	}

	/**
	 *
	 * @param permissions
	 * @return
	 * @since 2.5.0
	 *
	 */
	private List<String> findDeniedPermissions(String[] permissions) {
		List<String> needRequestPermissonList = new ArrayList<String>();
		if (Build.VERSION.SDK_INT >= 23 && getApplicationInfo().targetSdkVersion >= 23){
			try {
				for (String perm : permissions) {
					Method checkSelfMethod = getClass().getMethod("checkSelfPermission", String.class);
					Method shouldShowRequestPermissionRationaleMethod = getClass().getMethod("shouldShowRequestPermissionRationale", String.class);
					if ((Integer)checkSelfMethod.invoke(this, perm) != PackageManager.PERMISSION_GRANTED || (Boolean)shouldShowRequestPermissionRationaleMethod.invoke(this, perm)) {
						needRequestPermissonList.add(perm);
					}
				}
			} catch (Throwable e) {

			}
		}
		return needRequestPermissonList;
	}

	/**
	 * @param grantResults
	 * @return
	 * @since 2.5.0
	 *
	 */
	private boolean verifyPermissions(int[] grantResults) {
		for (int result : grantResults) {
			if (result != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}

	@SuppressLint("Override")
	@TargetApi(23)
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] paramArrayOfInt) {
		if (requestCode == PERMISSON_REQUESTCODE) {
			if (!verifyPermissions(paramArrayOfInt)) {
				showMissingPermissionDialog();
				isNeedCheck = false;
			}
		}
	}

	/**
	 *
	 * @since 2.5.0
	 *
	 */
	private void showMissingPermissionDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提示");
		builder.setMessage("相关权限被禁用，请设置允许");

		builder.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});

		builder.setPositiveButton("设置",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startAppSettings();
					}
				});

		builder.setCancelable(false);

		builder.show();
	}

	/**
	 *
	 * @since 2.5.0
	 *
	 */
	private void startAppSettings() {
		Intent intent = new Intent(
				Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		intent.setData(Uri.parse("package:" + getPackageName()));
		startActivity(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
