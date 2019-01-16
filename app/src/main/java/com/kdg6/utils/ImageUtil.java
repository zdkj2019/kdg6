package com.kdg6.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.kdg6.common.Constant;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * 鍥剧墖宸ュ叿锟?
 *
 * @author Jangs
 *
 */
public class ImageUtil {

	/**
	 * 鏍规嵁鎸囧畾鐨勮矾寰勮幏寰桞itmap
	 *
	 * @param imgPath
	 * @return
	 */
	public static Bitmap getBitmap(String imgPath) {
		// Get bitmap through image path
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = false;
		newOpts.inPurgeable = true;
		newOpts.inInputShareable = true;
		// Do not compress
		newOpts.inSampleSize = 1;
		newOpts.inPreferredConfig = Config.RGB_565;
		return BitmapFactory.decodeFile(imgPath, newOpts);
	}

	/**
	 * 鎶夿itmap淇濆瓨鍒版寚瀹氳矾锟?
	 *
	 * @param bitmap
	 * @param outPath
	 * @throws FileNotFoundException
	 */
	public static void storeImage(Bitmap bitmap, String outPath)
			throws FileNotFoundException {
		FileOutputStream os = new FileOutputStream(outPath);
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);// 30
		// 鏄帇缂╃巼锛岃〃绀哄帇锟?0%;
		// 濡傛灉涓嶅帇缂╂槸100锛岃〃绀哄帇缂╃巼锟?
	}

	/**
	 * 鏍规嵁鎸囧畾鐨勯珮瀹界缉鏀綛itmap
	 *
	 * @param imgPath
	 *            image path
	 * @param pixelW
	 *            target pixel of width
	 * @param pixelH
	 *            target pixel of height
	 * @return
	 */
	public static Bitmap ratio(String imgPath, float pixelW, float pixelH) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 锟?锟斤拷璇诲叆鍥剧墖锛屾鏃舵妸options.inJustDecodeBounds 璁惧洖true锛屽嵆鍙杈逛笉璇诲唴锟?
		newOpts.inJustDecodeBounds = true;
		newOpts.inPreferredConfig = Config.RGB_565;
		// Get bitmap info, but notice that bitmap is null now
		Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 鎯宠缂╂斁鐨勭洰鏍囧昂锟?
		float hh = pixelH;// 璁剧疆楂樺害锟?40f鏃讹紝鍙互鏄庢樉鐪嬪埌鍥剧墖缂╁皬锟?
		float ww = pixelW;// 璁剧疆瀹藉害锟?20f锛屽彲浠ユ槑鏄剧湅鍒板浘鐗囩缉灏忎簡
		// 缂╂斁姣旓拷?鐢变簬鏄浐瀹氭瘮渚嬬缉鏀撅紝鍙敤楂樻垨鑰呭鍏朵腑锟?锟斤拷鏁版嵁杩涜璁＄畻鍗冲彲
		int be = 1;// be=1琛ㄧず涓嶇缉锟?
		if (w > h && w > ww) {// 濡傛灉瀹藉害澶х殑璇濇牴鎹搴﹀浐瀹氬ぇ灏忕缉锟?
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 濡傛灉楂樺害楂樼殑璇濇牴鎹搴﹀浐瀹氬ぇ灏忕缉锟?
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 璁剧疆缂╂斁姣斾緥
		// 锟?锟斤拷鍘嬬缉鍥剧墖锛屾敞鎰忔鏃跺凡缁忔妸options.inJustDecodeBounds 璁惧洖false锟?
		bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
		// 鍘嬬缉濂芥瘮渚嬪ぇ灏忓悗鍐嶈繘琛岃川閲忓帇锟?
		// return compress(bitmap, maxSize); //
		// 杩欓噷鍐嶈繘琛岃川閲忓帇缂╃殑鎰忎箟涓嶅ぇ锛屽弽鑰岋拷?璧勬簮锛屽垹锟?
		return bitmap;
	}

	/**
	 * 鏍规嵁鎸囧畾鐨勯珮瀹界缉鏀綛itmap
	 *
	 * @param image
	 * @param pixelW
	 *            target pixel of width
	 * @param pixelH
	 *            target pixel of height
	 * @return
	 */
	public Bitmap ratio(Bitmap image, float pixelW, float pixelH) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, os);
		if (os.toByteArray().length / 1024 > 1024) {// 鍒ゆ柇濡傛灉鍥剧墖澶т簬1M,杩涜鍘嬬缉閬垮厤鍦ㄧ敓鎴愬浘鐗囷紙BitmapFactory.decodeStream锛夋椂婧㈠嚭
			os.reset();// 閲嶇疆baos鍗虫竻绌篵aos
			image.compress(Bitmap.CompressFormat.JPEG, 50, os);// 杩欓噷鍘嬬缉50%锛屾妸鍘嬬缉鍚庣殑鏁版嵁瀛樻斁鍒癰aos锟?
		}
		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 锟?锟斤拷璇诲叆鍥剧墖锛屾鏃舵妸options.inJustDecodeBounds 璁惧洖true锟?
		newOpts.inJustDecodeBounds = true;
		newOpts.inPreferredConfig = Config.RGB_565;
		Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = pixelH;// 璁剧疆楂樺害锟?40f鏃讹紝鍙互鏄庢樉鐪嬪埌鍥剧墖缂╁皬锟?
		float ww = pixelW;// 璁剧疆瀹藉害锟?20f锛屽彲浠ユ槑鏄剧湅鍒板浘鐗囩缉灏忎簡
		// 缂╂斁姣旓拷?鐢变簬鏄浐瀹氭瘮渚嬬缉鏀撅紝鍙敤楂樻垨鑰呭鍏朵腑锟?锟斤拷鏁版嵁杩涜璁＄畻鍗冲彲
		int be = 1;// be=1琛ㄧず涓嶇缉锟?
		if (w > h && w > ww) {// 濡傛灉瀹藉害澶х殑璇濇牴鎹搴﹀浐瀹氬ぇ灏忕缉锟?
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 濡傛灉楂樺害楂樼殑璇濇牴鎹搴﹀浐瀹氬ぇ灏忕缉锟?
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 璁剧疆缂╂斁姣斾緥
		// 閲嶆柊璇诲叆鍥剧墖锛屾敞鎰忔鏃跺凡缁忔妸options.inJustDecodeBounds 璁惧洖false锟?
		is = new ByteArrayInputStream(os.toByteArray());
		bitmap = BitmapFactory.decodeStream(is, null, newOpts);
		// 鍘嬬缉濂芥瘮渚嬪ぇ灏忓悗鍐嶈繘琛岃川閲忓帇锟?
		// return compress(bitmap, maxSize); //
		// 杩欓噷鍐嶈繘琛岃川閲忓帇缂╃殑鎰忎箟涓嶅ぇ锛屽弽鑰岋拷?璧勬簮锛屽垹锟?
		return bitmap;
	}

	/**
	 * 鍘嬬缉鍥剧墖鐨勮川閲忥紝骞剁敓鎴愬浘鍍忓埌鎸囧畾鐨勮矾锟?
	 *
	 * @param image
	 * @param outPath
	 * @param maxSize
	 *            target will be compressed to be smaller than this size.(kb)
	 * @throws IOException
	 */
	public static void compressAndGenImage(Bitmap image, String outPath,
										   int maxSize) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		// scale
		int options = 100;
		// Store the bitmap into output stream(no compress)
		image.compress(Bitmap.CompressFormat.JPEG, options, os);
		// Compress by loop
		while (os.toByteArray().length / 1024 > maxSize) {
			// Clean up os
			os.reset();
			// interval 10
			options -= 10;
			image.compress(Bitmap.CompressFormat.JPEG, options, os);
		}

		// Generate compressed image file
		FileOutputStream fos = new FileOutputStream(outPath);
		fos.write(os.toByteArray());
		fos.flush();
		fos.close();
	}

	/**
	 * 压缩图片到100k以内
	 *
	 * @param image
	 * @param outPath
	 * @param maxSize
	 * @throws IOException
	 */
	public static String compressAndGenImage(Bitmap image, int maxSize,
											 String type) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		// scale
		int options = 100;
		// Store the bitmap into output stream(no compress)
		image.compress(Bitmap.CompressFormat.JPEG, options, os);
		// Compress by loop
		while (os.toByteArray().length / 1024 > maxSize) {
			// Clean up os
			os.reset();
			// interval 10
			options -= 10;
			image.compress(Bitmap.CompressFormat.JPEG, options, os);
		}
		String outPath = Constant.SAVE_PIC_PATH
				+ "/"
				+ String.valueOf(System.currentTimeMillis()).trim()
				.substring(4) + "." + type;
		// Generate compressed image file
		FileOutputStream fos = new FileOutputStream(outPath);
		fos.write(os.toByteArray());
		fos.flush();
		fos.close();
		return outPath;
	}

	/**
	 * 鍘嬬缉鍥剧墖鐨勮川閲忥紝骞剁敓鎴愬浘鍍忓埌鎸囧畾鐨勮矾锟?
	 *
	 * @param imgPath
	 * @param outPath
	 * @param maxSize
	 *            target will be compressed to be smaller than this size.(kb)
	 * @param needsDelete
	 *            Whether delete original file after compress
	 * @throws IOException
	 */
	public static void compressAndGenImage(String imgPath, String outPath,
										   int maxSize, boolean needsDelete) throws IOException {
		compressAndGenImage(getBitmap(imgPath), outPath, maxSize);

		// Delete original file
		if (needsDelete) {
			File file = new File(imgPath);
			if (file.exists()) {
				file.delete();
			}
		}
	}

	/**
	 * 缂╂斁鍚庝繚瀛樺埌鎸囧畾璺緞
	 *
	 * @param image
	 * @param outPath
	 * @param pixelW
	 *            target pixel of width
	 * @param pixelH
	 *            target pixel of height
	 * @throws FileNotFoundException
	 */
	public void ratioAndGenThumb(Bitmap image, String outPath, float pixelW,
								 float pixelH) throws FileNotFoundException {
		Bitmap bitmap = ratio(image, pixelW, pixelH);
		storeImage(bitmap, outPath);
	}

	/**
	 * 缂╂斁鍚庝繚瀛樺埌鎸囧畾璺緞
	 *
	 * @param image
	 * @param outPath
	 * @param pixelW
	 *            target pixel of width
	 * @param pixelH
	 *            target pixel of height
	 * @param needsDelete
	 *            Whether delete original file after compress
	 * @throws FileNotFoundException
	 */
	public void ratioAndGenThumb(String imgPath, String outPath, float pixelW,
								 float pixelH, boolean needsDelete) throws FileNotFoundException {
		Bitmap bitmap = ratio(imgPath, pixelW, pixelH);
		storeImage(bitmap, outPath);

		// Delete original file
		if (needsDelete) {
			File file = new File(imgPath);
			if (file.exists()) {
				file.delete();
			}
		}
	}
}
