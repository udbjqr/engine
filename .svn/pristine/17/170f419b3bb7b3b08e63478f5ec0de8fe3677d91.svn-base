package com.jg.common.sql;

import com.jg.common.InternalConstant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * 实际的mysql操作对象.
 * <p>
 * <p>
 * 目前此对象每一次操作都新建一个连接对象进行操作
 *
 * @author yimin
 */
final class MysqlHandle extends AbstractDataBaseHandle {
	private static final String DB_SERVER_ADD = "DBServerAdd";
	private static final String DB_SERVER_PORT = "DBServerPort";
	private static final String DB_DATA_BASE_NAME = "DBDataBaseName";
	private static final String DB_PASSWORD = "DBPassword";
	private static final String DB_USER_NAME = "DBUserName";
	private static final String CONNECTION_TIMEOUT = "ConnectionTimeout";
	private static final String DB_POOL_NUM = "DBPoolNum";

	private String userName = null;
	private String password = null;
	private String connectionURL = null;

	MysqlHandle() {
		this("");
	}

	MysqlHandle(String suffix) {

		try {
			logger.debug("初始化mysql" + suffix + "数据操作对象开始。");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Properties properties = DBHelperFactory.getProperties();

			userName = properties.getProperty(DB_USER_NAME + suffix);
			if (userName == null || userName.equals("")) {
				throw new NullPointerException("配置值：DBUserName" + suffix + " is NULL");
			}
			password = properties.getProperty(DB_PASSWORD + suffix, "");
			connectionURL = String.format("jdbc:mysql://%s:%s/%s",
							properties.getProperty(DB_SERVER_ADD + suffix, ""), properties.getProperty(DB_SERVER_PORT + suffix, ""),
							properties.getProperty(DB_DATA_BASE_NAME + suffix, ""));

			if (properties.getProperty(DB_SERVER_ADD + suffix, "").equals("")) {
				throw new NullPointerException("配置值：DBServerAdd " + suffix + " is NULL");
			}

			int connectionTimeOut = Integer.parseInt(properties.getProperty(CONNECTION_TIMEOUT + suffix, "10"));
			int maxPoolNumber = Integer.parseInt(properties.getProperty(DB_POOL_NUM + suffix, "10"));
			pool = new ConnectionPool(maxPoolNumber, connectionTimeOut, this);

			logger.trace("成功初始化mysql数据库操作对象。");
		} catch (Exception e) {
			logger.error("初始化mysqlJDBC出现异常:", e);
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
			SimpleDateFormat format = new SimpleDateFormat(InternalConstant.DATEFORMAT);
			return "'" + format.format((Date) obj) + "'";
		}
		if (obj instanceof String) {
			return "'" + (String) obj + "'";
		}

		return String.valueOf(obj);
	}
}
