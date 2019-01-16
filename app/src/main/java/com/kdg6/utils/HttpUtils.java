package com.kdg6.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import android.util.Log;

/**
 * http杩炴帴宸ュ叿锟?
 *
 * @author Jangs
 *
 */
public class HttpUtils {

	private static final String TAG = "HttpUtils";
	/**
	 * post鏂瑰紡
	 *
	 * @param path
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String sendPostMethod(String path,
										Map<String, Object> params, String encode) throws Exception {

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(path);
		// 璇锋眰瓒呮椂
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
		// 璇诲彇瓒呮椂
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				10000);

		String result = "";
		List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		if (params != null && !params.isEmpty()) {
			String name = "";
			String value = "";
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				name = entry.getKey();
				value = entry.getValue().toString();
				BasicNameValuePair valuePair = new BasicNameValuePair(name,
						value);
				parameters.add(valuePair);
			}
		}

		UrlEncodedFormEntity uFormEntity = new UrlEncodedFormEntity(parameters,
				encode);
		httpPost.setEntity(uFormEntity);
		HttpResponse httpResponse = httpClient.execute(httpPost);
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			result = toString(httpResponse.getEntity(), encode);
		} else {
			result = "鎿嶄綔瓒呮椂";
		}
		return result;
	}

	public static String toString(HttpEntity entity, String encode)
			throws IllegalStateException, IOException {
		String result = null;
		StringBuffer sb = new StringBuffer();
		InputStream is = entity.getContent();
		BufferedReader br = new BufferedReader(
				new InputStreamReader(is, encode));
		String data = "";
		while ((data = br.readLine()) != null) {
			sb.append(data);
		}
		result = sb.toString();
		return result;
	}

}
