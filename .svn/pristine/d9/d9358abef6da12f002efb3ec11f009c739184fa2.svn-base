package com.jg.common.sql;

import com.jg.common.InternalConstant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Oracle对象的处理类.
 */
public class OracleHandle extends AbstractDataBaseHandle {
	private static final String DB_SERVER_ADD = "DBServerAdd";
	private static final String DB_SERVER_PORT = "DBServerPort";
	private static final String DB_DATA_BASE_NAME = "DBDataBaseName";
	private static final String DB_PASSWORD = "DBPassword";
	private static final String DB_USER_NAME = "DBUserName";
	private static final String CONNECTION_TIMEOUT = "ConnectionTimeout";
	private static final String DB_POOL_NUM = "DBPoolNum";
	private static final String STR_INCLUDE = "'";
	private static final Properties properties = DBHelperFactory.getProperties();

	private String userName = null;
	private String password = null;
	private String connectionURL = null;

	private final SimpleDateFormat format = new SimpleDateFormat(InternalConstant.DATEFORMAT);

	OracleHandle() {
		this("");
	}


	OracleHandle(String suffix) {
		try {
			logger.debug("初始化Oracle数据操作对象开始。");
			Class.forName("oracle.jdbc.OracleDriver");

			userName = properties.getProperty(DB_USER_NAME + suffix);
			if (userName == null || userName.equals("")) {
				throw new NullPointerException("配置值：DBUserName" + suffix + " is NULL");
			}
			password = properties.getProperty(DB_PASSWORD + suffix, "");

			if (properties.getProperty(DB_SERVER_ADD + suffix, "").equals("")) {
				throw new NullPointerException("配置值：DBServerAdd" + suffix + " is NULL");
			}

			connectionURL = String.format("jdbc:oracle:thin:@//%s:%s/%s",
							properties.getProperty(DB_SERVER_ADD + suffix, ""), properties.getProperty(DB_SERVER_PORT + suffix, ""),
							properties.getProperty(DB_DATA_BASE_NAME + suffix, ""));


			int connectionTimeOut = Integer.parseInt(properties.getProperty(CONNECTION_TIMEOUT + suffix, "10"));
			int maxPoolNumber = Integer.parseInt(properties.getProperty(DB_POOL_NUM + suffix, "10"));
			pool = new ConnectionPool(maxPoolNumber, connectionTimeOut, this);

			logger.trace("成功初始化oracle数据库操作对象。");
		} catch (Exception e) {
			logger.error("初始化oracleJDBC出现异常:", e);
		}
	}

	@Override
	Connection getNewJDBCConnection() {
		try {
			return DriverManager.getConnection(connectionURL, userName, password);
		} catch (SQLException e) {
			logger.error("{}得到数据库实际连接异常。连接字串：{} 用户名：{} 密码：{}",
							this.getClass().getName(), connectionURL, userName, password, e);
			return null;
		}
	}

	@Override
	public String getString(Object obj) {
		if (obj instanceof Date) {
			synchronized (this) {
				return "to_date('" + format.format((Date) obj) + "' , 'yyyy-mm-dd hh24:mi:ss')";
			}
		}

		if (obj instanceof String) {
			return STR_INCLUDE + obj + STR_INCLUDE;
		}

		return String.valueOf(obj);
	}
}