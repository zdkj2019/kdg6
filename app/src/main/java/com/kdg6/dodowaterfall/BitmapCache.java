package com.kdg6.dodowaterfall;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Hashtable;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapCache {
	static private BitmapCache cache;
	private Hashtable<String, BtimapRef> bitmapRefs;
	private ReferenceQueue<Bitmap> q;

	/**
	 */
	private class BtimapRef extends SoftReference<Bitmap> {
		private String _key = "";

		public BtimapRef(Bitmap bmp, ReferenceQueue<Bitmap> q, String key) {
			super(bmp, q);
			_key = key;
		}
	}

	private BitmapCache() {
		bitmapRefs = new Hashtable<String, BtimapRef>();
		q = new ReferenceQueue<Bitmap>();

	}

	public static BitmapCache getInstance() {
		if (cache == null) {
			cache = new BitmapCache();
		}
		return cache;

	}

	/**
	 */
	private void addCacheBitmap(Bitmap bmp, String key) {
		cleanCache();// 清除垃圾引用
		BtimapRef ref = new BtimapRef(bmp, q, key);
		bitmapRefs.put(key, ref);
	}

	/**
	 */
	public Bitmap getBitmap(String filename, AssetManager assetManager) {

		Bitmap bitmapImage = null;
		if (bitmapRefs.containsKey(filename)) {
			BtimapRef ref = (BtimapRef) bitmapRefs.get(filename);
			bitmapImage = (Bitmap) ref.get();
		}
		// 并保存对这个新建实例的软引用
		if (bitmapImage == null) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inTempStorage = new byte[16 * 1024];

			// bitmapImage = BitmapFactory.decodeFile(filename, options);
			BufferedInputStream buf;
			try {
				buf = new BufferedInputStream(assetManager.open(filename));
				bitmapImage = BitmapFactory.decodeStream(buf);
				this.addCacheBitmap(bitmapImage, filename);
			} catch (IOException e) {

				e.printStackTrace();
			}

		}

		return bitmapImage;
	}

	private void cleanCache() {
		BtimapRef ref = null;
		while ((ref = (BtimapRef) q.poll()) != null) {
			bitmapRefs.remove(ref._key);
		}
	}

	// 清除Cache内的全部内容
	public void clearCache() {
		cleanCache();
		bitmapRefs.clear();
		System.gc();
		System.runFinalization();
	}

}
