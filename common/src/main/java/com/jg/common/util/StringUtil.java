package com.jg.common.util;

import com.jg.common.InternalConstant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;

/**
 * String utility class.
 */
public final class StringUtil {
	public static final Logger logger = LogManager.getLogger(StringUtil.class.getName());
	public static final String NEWLINE;
	private static final char PACKAGE_SEPARATOR_CHAR = '.';


	static {
		// Determine the newline character of the current platform.
		String newLine;

		Formatter formatter = new Formatter();
		try {
			newLine = formatter.format("%n").toString();
		} catch (Exception e) {
			// Should not reach here, but just in case.
			newLine = "\n";
		} finally {
			formatter.close();
		}

		NEWLINE = newLine;
	}

	/**
	 * The shortcut to {@link #simpleClassName(Class) simpleClassName(o.getClass())}.
	 */
	public static String simpleClassName(Object o) {
		if (o == null) {
			return "null_object";
		} else {
			return simpleClassName(o.getClass());
		}
	}

	/**
	 * Generates a simplified name from a {@link Class}.  Similar to {@link Class#getSimpleName()}, but it works fine
	 * with anonymous classes.
	 */
	public static String simpleClassName(Class<?> clazz) {
		String className = ObjectUtil.checkNotNull(clazz, "clazz").getName();
		final int lastDotIdx = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
		if (lastDotIdx > -1) {
			return className.substring(lastDotIdx + 1);
		}
		return className;
	}

	private StringUtil() {
		// Unused.
	}

	/**
	 * 判断明文加密之后与密码是否匹配,
	 *
	 * @param plaintext 明文
	 * @param passwd    密码
	 * @return 匹配返回true, 不匹配返回false
	 */
	public static boolean matchPasswd(String plaintext, String passwd) {
		return encrypt(plaintext).equals(passwd);
	}

	/**
	 * 加密码一个明文,此过程返回对应的密码,如出现错误,返回NULL
	 *
	 * @param plaintext 需要加密的文本
	 * @return
	 */
	public static String encrypt(String plaintext) {
		MessageDigest en;
		try {
			en = MessageDigest.getInstance("SHA");
			en.update(plaintext.getBytes());
			String pass = (new BASE64Encoder()).encodeBuffer(en.digest());
			return pass.trim();
		} catch (NoSuchAlgorithmException e) {
			logger.error("加密出错.", e);
		}
		return null;
	}

	/**
	 * 转换一个对象至JSON的值显示.
	 *
	 * @param object 要转换的对象
	 * @return 对象为空，返回""
	 */
	public static String converToJSONString(Object object) {
		final SimpleDateFormat format = new SimpleDateFormat(InternalConstant.DATEFORMAT);

		if (object != null) {
			if (object instanceof String) {
				return "\"" + object + "\"";
			} else if (object instanceof Date) {
				synchronized (format) {
					return "\"" + format.format(object) + "\"";
				}
			} else {
				return object.toString();
			}
		}

		return "\"\"";
	}

	public static boolean isEmpty(String value) {
		return value == null || value.isEmpty();
	}

}
