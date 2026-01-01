package com.wwfinance.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

/**
 *
 */
@Slf4j
public final class HttpUtils {

	static final String POST = "POST";
	static final String GET = "GET";
	static final int CONN_TIMEOUT = 300000;// ms
	static final int READ_TIMEOUT = 300000;// ms

	/**
	 * post 方式发送http请求.
	 *
	 * @param strUrl
	 * @param reqData
	 * @return
	 */
	public static byte[] doPost(String strUrl, byte[] reqData) {
		return send(strUrl, POST, reqData);
	}

	/**
	 * get方式发送http请求.
	 *
	 * @param strUrl
	 * @return
	 */
	public static byte[] doGet(String strUrl) {
		return send(strUrl, GET, null);
	}

	/**
	 * @param strUrl
	 * @param reqmethod
	 * @param reqData
	 * @return
	 */
	public static byte[] send(String strUrl, String reqmethod, byte[] reqData) {
		try {
			// 创建URL对象 - 使用URI.create().toURL()替代直接使用URL构造函数，以兼容Java 21
			URL url = URI.create(strUrl).toURL();
			// 打开与指定URL的连接
			HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
			// 设置连接属性
			httpcon.setDoOutput(true);  // 允许向连接输出数据(用于POST请求)
			httpcon.setDoInput(true);  // 允许从连接读取数据
			httpcon.setUseCaches(false);  // 不使用缓存
			httpcon.setInstanceFollowRedirects(true); // 自动跟随重定向
			httpcon.setConnectTimeout(CONN_TIMEOUT); // 设置连接超时时间
			httpcon.setReadTimeout(READ_TIMEOUT); // 设置读取超时时间
			httpcon.setRequestMethod(reqmethod); // 设置请求方法(GET,POST等)
			httpcon.connect(); // 建立连接
			// 如果请求方法是POST, 则写入请求数据
			if (reqmethod.equalsIgnoreCase(POST)) {
				OutputStream os = httpcon.getOutputStream(); // 获取输入流
				os.write(reqData); // 写入数据
				os.flush(); // 刷新输出流
				os.close(); // 关闭输出流
			}
			// 从连接中读取响应数据
			BufferedReader in = new BufferedReader(new InputStreamReader(httpcon.getInputStream(),"utf-8"));
			String inputLine;
			StringBuilder bankXmlBuffer = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				bankXmlBuffer.append(inputLine);
			}
			in.close();
			httpcon.disconnect(); // 断开连接
			return bankXmlBuffer.toString().getBytes(); // 返回响应数据的字节数组
		} catch (Exception ex) {
			log.error(ex.toString(), ex);
			return null;
		}
	}

	/**
	 * 从输入流中读取数据
	 *
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();// 网页的二进制数据
		outStream.close();
		inStream.close();
		return data;
	}
}