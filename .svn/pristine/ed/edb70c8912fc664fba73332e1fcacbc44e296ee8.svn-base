package com.jg.common.sql;

import com.jg.common.InternalConstant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

class MSSQLSERVERHandle extends AbstractDataBaseHandle {
	private static final String DB_SERVER_ADD = "DBServerAdd";
	private static final String DB_SERVER_PORT = "DBServerPort";
	private static final String DB_DATA_BASE_NAME = "DBDataBaseName";
	private static final String DB_PASSWORD = "DBPassword";
	private static final String DB_USER_NAME = "DBUserName";
	private static final String CONNECTION_TIMEOUT = "ConnectionTimeout";
	private static final String DB_POOL_NUM = "DBPoolNum";
	private static final String STR_INCLUDE = "'";

	private String userName = null;
	private String password = null;
	private String connectionURL = null;
	private final SimpleDateFormat format = new SimpleDateFormat(InternalConstant.DATEFORMAT);

	MSSQLSERVERHandle() {
		this("");
	}

	MSSQLSERVERHandle(String suffix) {
		try {
			logger.debug("初始化MSSqlserver" + suffix + "数据操作对象开始。");
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Properties properties = DBHelperFactory.getProperties();

			userName = properties.getProperty(DB_USER_NAME + suffix);
			if (userName == null || userName.equals("")) {
				throw new NullPointerException("配置值：DBUserName" + suffix + " is NULL");
			}
			password = properties.getProperty(DB_PASSWORD + suffix, "");
			connectionURL = String.format("jdbc:sqlserver://%s:%s;DatabaseName=%s",
							properties.getProperty(DB_SERVER_ADD + suffix, ""), properties.getProperty(DB_SERVER_PORT + suffix, ""),
							properties.getProperty(DB_DATA_BASE_NAME + suffix, ""));

			if (properties.getProperty(DB_SERVER_ADD + suffix, "").equals("")) {
				throw new NullPointerException("配置值：DBServerAdd" + suffix + " is NULL");
			}

			int connectionTimeOut = Integer.parseInt(properties.getProperty(CONNECTION_TIMEOUT + suffix, "10"));
			int maxPoolNumber = Integer.parseInt(properties.getProperty(DB_POOL_NUM + suffix, "10"));
			pool = new ConnectionPool(maxPoolNumber, connectionTimeOut, this);

			logger.trace("成功初始化sqlserver数据库操作对象。");
		} catch (Exception e) {
			logger.error("初始化sqlserverJDBC出现异常:", e);
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
	public synchronized String getString(Object obj) {
		if (obj instanceof Date) {
			synchronized (this) {
				return STR_INCLUDE + format.format((Date) obj) + STR_INCLUDE;
			}
		}

		if (obj instanceof String) {
			return STR_INCLUDE + ((String) obj).replace("'", "''") + STR_INCLUDE;
		}

		return String.valueOf(obj);
	}

	//@Override
	//protected Connection getConnection() {
	//	return getNewJDBCConnection();
	//}
	//
	//@Override
	//public void close(ResultSet set) {
	//	if (set == null) {
	//		logger.error("传入的数据为空");
	//		return;
	//	}
	//
	//	try {
	//		logger.trace("关闭一个连接.");
	//		Statement st = set.getStatement();
	//		Connection con = st.getConnection();
	//		set.close();
	//		st.close();
	//		con.close();
	//	} catch (SQLException e) {
	//		logger.error("关闭JDBC相应对象出现异常:", e);
	//	}
	//}
}
