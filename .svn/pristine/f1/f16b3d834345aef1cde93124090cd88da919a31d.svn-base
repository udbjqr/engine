package com.jg.common.sql;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.InternalConstant;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

class PostgresqlHandle extends AbstractDataBaseHandle {
	private static final String DB_SERVER_ADD = "DBServerAdd";
	private static final String DB_SERVER_PORT = "DBServerPort";
	private static final String DB_DATA_BASE_NAME = "DBDataBaseName";
	private static final String DB_PASSWORD = "DBPassword";
	private static final String DB_USER_NAME = "DBUserName";
	private static final String CONNECTION_TIMEOUT = "ConnectionTimeout";
	private static final String DB_POOL_NUM = "DBPoolNum";
	private static final String STR_INCLUDE = "$yq$";

	private String userName = null;
	private String password = null;
	private String connectionURL = null;

	private final SimpleDateFormat format = new SimpleDateFormat(InternalConstant.DATEFORMAT);

	PostgresqlHandle() {
		this("");
	}

	PostgresqlHandle(String suffix) {
		try {
			logger.debug("初始化Postgresql" + suffix + "数据操作对象开始。");
			Class.forName("org.postgresql.Driver");
			Properties properties = DBHelperFactory.getProperties();

			userName = properties.getProperty(DB_USER_NAME + suffix);
			if (userName == null || userName.equals("")) {
				throw new NullPointerException("配置值：DBUserName " + suffix + "is NULL");
			}
			password = properties.getProperty(DB_PASSWORD + suffix, "");

			if (properties.getProperty(DB_SERVER_ADD + suffix, "").equals("")) {
				throw new NullPointerException("配置值：DBServerAdd" + suffix + " is NULL");
			}

			connectionURL = String.format("jdbc:postgresql://%s:%s/%s",
				properties.getProperty(DB_SERVER_ADD + suffix, ""), properties.getProperty(DB_SERVER_PORT + suffix, ""),
				properties.getProperty(DB_DATA_BASE_NAME + suffix, ""));


			int connectionTimeOut = Integer.parseInt(properties.getProperty(CONNECTION_TIMEOUT + suffix, "10"));
			int maxPoolNumber = Integer.parseInt(properties.getProperty(DB_POOL_NUM + suffix, "10"));
			pool = new ConnectionPool(maxPoolNumber, connectionTimeOut, this);

			logger.trace("成功初始化postgresql数据库操作对象。");
		} catch (Exception e) {
			logger.error("初始化postgresJDBC出现异常:", e);
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
	public ResultSet select(String sql, int limit, int offset) throws SQLException {
		if (limit <= 0 || offset < 1) {
			logger.error("页数和每页行数必须大于0。limit:{},pageCount:{}", limit, offset);
			return null;
		}

		if (!sql.contains(" order ")) {
			logger.warn("查询语句未带order子句，查询结果可能无效.sql:" + sql);
		} else {
			logger.debug("执行sql语句:" + sql);
		}

		Connection con = getConnection();
		Statement st = null;

		try {
			st = con.createStatement();

			return st.executeQuery(sql + " limit " + limit + " offset " + offset);
		} catch (SQLException e) {
			logger.error("执行sql语句发生异常,关闭数据连接。 sql:" + sql, e);
			if (st != null) {
				st.close();
			}
			con.close();

			throw e;
		}
	}

	@Override
	public String getString(Object obj) {
		if (obj instanceof Date) {
			synchronized (this) {
				return STR_INCLUDE + format.format((Date) obj) + STR_INCLUDE;
			}
		}

		if (obj instanceof String) {
			return STR_INCLUDE + obj + STR_INCLUDE;
		}

		if (obj instanceof JSONObject) {
			return STR_INCLUDE + obj.toString() + STR_INCLUDE + "::jsonb";
		}

		if (obj instanceof String[]) {
			StringBuilder builder = new StringBuilder();
			for (String str : (String[]) obj) {
				builder.append("'").append(str).append("',");
			}

			if (builder.length() > 0) {
				builder.delete(builder.length() - 1, builder.length());
			}
			builder.insert(0, "ARRAY[").append("]::varchar[]");

			return builder.toString();
		}

		if (obj instanceof Integer[]) {
			StringBuilder builder = new StringBuilder();
			for (Integer str : (Integer[]) obj) {
				builder.append(str).append(",");
			}

			if (builder.length() > 0) {
				builder.delete(builder.length() - 1, builder.length());
			}
			builder.insert(0, "ARRAY[").append("]::int[]");

			return builder.toString();
		}

		return String.valueOf(obj);
	}

	@Override
	public Integer getNextID(String name) {
		return ((Long) selectOneValues("select nextval(" + getString(name) + ")")).intValue();
	}
}
