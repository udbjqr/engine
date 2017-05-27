package com.jg.common.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * http协议相关的数据获取方法
 * <p>
 * create by 17/5/25.
 *
 * @author yimin
 */

public class HttpClientUtil {
	private final static Logger log = LogManager.getLogger(HttpClientUtil.class.getName());

	/**
	 * 发起一个post请求，并且以application/json 的方式向上发送.
	 *
	 * @param URLString 访问的完整地址
	 * @param para      访问的一个json格式的参数
	 * @return 出现异常返回null, 如果结果非200或304，返回头信息，并以error:做为前缀。正常情况直接返回结果
	 */
	public static String post(String URLString, String para) {
		try {
			URL url = new URL(URLString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// 设置连接输出流为true,默认false (post 请求是以流的方式隐式的传递参数)
			connection.setDoOutput(true);
			// 设置连接输入流为true
			connection.setDoInput(true);
			// 设置请求方式为post
			connection.setRequestMethod("POST");
			// post请求缓存设为false
			connection.setUseCaches(false);
			// 设置该HttpURLConnection实例是否自动执行重定向
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type", "application/json");
			// 建立连接 (请求未开始,直到connection.getInputStream()方法调用时才发起,以上各个参数设置需在此方法之前进行)
			connection.connect();
			// 创建输入输出流,用于往连接里面输出携带的参数,(输出内容为?后面的内容)
			DataOutputStream dataOut = new DataOutputStream(connection.getOutputStream());
			//URLEncoder.encode()方法 为字符串进行编码(具体编码格式依据项目文档而定)
			String parameter = URLEncoder.encode(para, "utf-8");
			// 将参数输出到连接
			dataOut.writeBytes(parameter);

			// 输出完成后刷新并关闭流
			dataOut.flush();
			dataOut.close();

			int responseCode = connection.getResponseCode();
			if (responseCode != 200 && responseCode != 304) {
				String mes = connection.getResponseMessage();
				log.debug(mes);
				return "error:" + mes;
			}

			// 连接发起请求,处理服务器响应  (从连接获取到输入流并包装为bufferedReader)
			BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuilder sb = new StringBuilder(); // 用来存储响应数据
			while ((line = bf.readLine()) != null) {
				sb.append(bf.readLine());
			}

			bf.close();    // 重要且易忽略步骤 (关闭流,切记!)
			connection.disconnect(); // 销毁连接

			return sb.toString();
		} catch (Exception e) {
			log.error("请示地址{},参数{}出现异常。", URLString, para, e);
		}

		return null;
	}
}
