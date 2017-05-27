package com.jg.common.util;

import com.jg.common.InternalConstant;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	/**
	 * 转换String至Date.
	 * <p>
	 * 此方法不产生任何异常，只要无法转换均返回null.
	 */
	public static Date toDateNoError(String dateStr) {
		try {
			return new SimpleDateFormat(InternalConstant.DATEFORMAT).parse(dateStr);
		} catch (Exception e) {
			return null;
		}
	}

	public static Date toDateNowAddInterval(Long durationTime) {
		if (durationTime == null) {
			return null;
		}

		return new Date(System.currentTimeMillis() + durationTime);
	}

	public static String convertNowToString() {
		final SimpleDateFormat format = new SimpleDateFormat(InternalConstant.DATEFORMAT);
		return format.format(new Date(System.currentTimeMillis()));
	}
}
