package com.kdg6.zxing.core;

public interface Delegate {

	/**
	 * 处理扫描结果
	 *
	 * @param result
	 *            摄像头扫码时只要回调了该方法 result 就一定有值，不会为 null。解析本地图片或 Bitmap 时 result
	 *            可能为 null
	 */
	void onScanQRCodeSuccess(String result);

	/**
	 * 摄像头环境亮度发生变化
	 *
	 * @param isDark
	 *            是否变暗
	 */
	void onCameraAmbientBrightnessChanged(boolean isDark);

	/**
	 * 处理打开相机出错
	 */
	void onScanQRCodeOpenCameraError();

}
