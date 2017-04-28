package com.jg.common.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FileUtil {
	private static final Logger logger = LogManager.getLogger(FileUtil.class.getName());

	/**
	 * 根据给定的编码，从文件中读取字符串
	 *
	 * @param configURL URL地址
	 * @param code      指定文件的编码格式
	 * @return 读取到的字符串
	 */
	public static String getProperties(URL configURL, String code) {
		logger.debug("Reading configuration from URL {}", configURL);
		InputStream stream;
		URLConnection uConn;
		BufferedReader reader;
		StringBuilder builder = new StringBuilder();

		try {
			uConn = configURL.openConnection();
			uConn.setUseCaches(false);
			stream = uConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(stream, code);
			reader = new BufferedReader(inputStreamReader);
			String tempString;
			while ((tempString = reader.readLine()) != null) {
				builder.append(tempString).append(StringUtil.NEWLINE);
			}
			reader.close();

			return builder.toString();
		} catch (Exception e) {
			logger.error("{} error", configURL, e);
			return null;
		}
	}


	public static Properties getPropertiesFromString(String config){
		Properties properties = new Properties();
		try{
			properties.load(new StringReader(config));
		}catch (Exception e){
			logger.error("配置转换出错：",e);
		}
		return properties;
	}
	

	/**
	 * 获得可上传的文件类型
	 *
	 * @return
	 */
	public static List<String> getFileTypeList() {
		List<String> fileTypeList = new ArrayList<String>();
		fileTypeList.add("jpg");
		fileTypeList.add("jpeg");
		fileTypeList.add("bmp");
		fileTypeList.add("png");
		fileTypeList.add("gif");
		return fileTypeList;
	}
}