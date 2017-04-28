package com.jg.common.sql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 数据库操作层面接口.此对象负责操作数据库
 *
 * @author yimin
 */
abstract class AbstractDataBaseHandle implements DBHelper {
	static final Logger logger = LogManager.getLogger(AbstractDataBaseHandle.class.getName());

	ConnectionPool pool;

	/**
	 * 返回一个连接对象.
	 *
	 * @return 针对当前数据库的连接对象
	 */
	Connection getConnection() {
		return pool.getFreeConn();
	}

	/**
	 * 对数据库操作对象进行初始化操作。此方法保留.
	 */
	AbstractDataBaseHandle init() {
		return this;
	}

	/**
	 * 返回一个实际的连接对象。
	 */
	abstract Connection getNewJDBCConnection();

	/**
	 * 执行一个更新或者删除操作.
	 *
	 * @param sql 要执行的sql语句.
	 * @return 成功影响的条数
	 */
	public int update(String sql) {
		int res = -1;
		try (Connection con = getConnection()) {
			Statement st = con.createStatement();
			res = st.executeUpdate(sql);

			logger.debug("执行sql语句:" + sql + ",影响的行数:" + res);

			st.close();
		} catch (SQLException e) {
			logger.error("执行无返回sql语句发生异常,sql:" + sql, e);
		}

		return res;
	}

	/**
	 * 执行一个只返回单个值的select语句,此操作不需要关闭动作.
	 * <p>如果未取得值,返回 Integer.MIN_VALUE
	 *
	 * @param sql 需要执行的sql语句
	 * @return 执行结果的一个值.
	 */
	public <T> T selectOneValues(String sql) {
		Object res = null;
		ResultSet set = null;

		try {
			set = select(sql);

			if (set == null) {
				return null;
			}

			if (set.next()) {
				res = set.getObject(1);
			} else {
				return null;
			}
		} catch (SQLException e) {
			logger.error("获得ResultSet数据异常:", e);
		} finally {
			close(set);
		}

		//noinspection unchecked
		return (T) res;
	}

	/**
	 * 执行一个查询过程,使用完毕必须使用close()来关闭.
	 *
	 * @param sql 需要执行的查询语句.
	 * @return 一个结果集.用完此结果集需要调用close()过程进行关闭操作.
	 */
	public ResultSet select(String sql) throws SQLException {
		Connection con = getConnection();
		Statement st = null;

		logger.debug("执行sql语句:" + sql);
		try {
			st = con.createStatement();

			return st.executeQuery(sql);
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
	public void selectWithSet(String sql, SQLSelectFunction function) {
		ResultSet set = null;
		try {
			set = select(sql);
			function.callBack(set);
		} catch (SQLException ignored) {
		} finally {
			close(set);
		}
	}

	@Override
	public void selectWithSet(String sql, int limit, int offset, SQLSelectFunction function) {
		ResultSet set = null;
		try {
			set = select(sql, limit, offset);
			function.callBack(set);
		} catch (SQLException ignored) {
		} finally {
			close(set);
		}
	}

	@Override
	public void selectWithRow(String sql, SQLSelectFunction function) {
		ResultSet set = null;
		try {
			set = select(sql);
			while (set.next()) {
				function.callBack(set);
			}
		} catch (SQLException ignored) {
		} finally {
			close(set);
		}
	}

	@Override
	public void selectWithRow(String sql, int limit, int offset, SQLSelectFunction function) {
		ResultSet set = null;
		try {
			set = select(sql, limit, offset);
			while (set.next()) {
				function.callBack(set);
			}
		} catch (SQLException ignored) {
		} finally {
			close(set);
		}
	}

	@Override
	public ResultSet select(String sql, int limit, int offset) throws SQLException {
		logger.error("此方法需要继承类实现。");
		return null;
	}

	@Override
	public Integer getNextID(String name) {
		logger.error("此方法需要继承类实现。");
		return -1;
	}

	/**
	 * 关闭一个结果集对应的所有对象.
	 *
	 * @param set 本对象返回的结果集.
	 */
	public void close(ResultSet set) {
		if (set == null) {
			logger.error("传入的数据为空");
			return;
		}
		Connection con = null;

		try {
//			logger.trace(this.getClass().getSimpleName() + " 关闭一个连接.");
			Statement st = set.getStatement();
			con = st.getConnection();
			set.close();
			st.close();
		} catch (SQLException e) {
			logger.error("关闭JDBC相应对象出现异常:", e);
		} finally {
			pool.closeConnection(con);
		}
	}

	@Override
	public <T> T execBatchSqlWithOneValue(String[] sqlStrings) throws SQLException {
		if (sqlStrings.length < 2) {
			logger.error("传入的sql语句需要大于等于2条。");
			return null;
		}

		Object res = null;
		Connection con = getConnection();
		try {
			con.setAutoCommit(false);
			logger.trace("开始事务，执行批量sql语句");

			for (int i = 0; i < sqlStrings.length - 1; i++) {
				logger.trace("执行sql语句:" + sqlStrings[i]);
				Statement st = con.createStatement();
				st.executeUpdate(sqlStrings[i]);
				st.close();
			}

			Statement st = con.createStatement();
			ResultSet set = st.executeQuery(sqlStrings[sqlStrings.length - 1]);

			logger.debug("执行sql语句:" + sqlStrings[sqlStrings.length - 1]);


			while (set.next()) {
				res = set.getObject(1);
			}

			con.commit();
			st.close();
		} catch (SQLException e) {
			logger.error("执行sql语句发生异常.sqls:[{}]", sqlStrings, e);
			con.rollback();
			throw e;
		} finally {
			try {
				con.close();
			} catch (SQLException e1) {
				logger.error("执行关闭发生异常.", e1);
			}
		}

		@SuppressWarnings("unchecked")
		T t = (T) res;
		return t;
	}

	public void execBatchSql(String[] sqlStrings) {
		execBatchSqlImpl((sb, st) -> {
			for (String sql : sqlStrings) {
				st.addBatch(sql);
				sb.append(sql).append("\n");
			}
		});
	}

	public void execBatchSql(Iterable<String> sqlStrings) {
		execBatchSqlImpl((sb, st) -> {
			for (String sql : sqlStrings) {
				st.addBatch(sql);
				sb.append(sql).append("\n");
			}
		});
	}

	private void execBatchSqlImpl(SetStatementFunction exec) {
		Connection con = null;
		StringBuilder sb = new StringBuilder();
		try {
			con = getConnection();
			Statement st = con.createStatement();

			exec.setSqls(sb, st);

			con.setAutoCommit(false);
			st.executeBatch();

			con.commit();
			st.close();
		} catch (SQLException e) {
			logger.error("执行sql语句发生异常.sqls:" + sb.toString(), e);

			try {
				con.rollback();
				con.close();
			} catch (SQLException e1) {
				logger.error("执行sql关闭连接发生异常.", e1);
			}
		} finally {
			try {
				assert con != null;
				con.close();
			} catch (SQLException e2) {
				logger.error("执行关闭发生异常.", e2);
			}
		}
	}

	@FunctionalInterface
	interface SetStatementFunction {
		void setSqls(StringBuilder sb, Statement st) throws SQLException;
	}

}

