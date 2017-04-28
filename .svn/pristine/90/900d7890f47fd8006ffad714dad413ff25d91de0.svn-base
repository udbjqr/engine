package com.jg.common.sql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;

/**
 * 数据库连接池对象.
 * <p>
 * 此对象为单一对象
 * <p>
 * 此对象支持JDBC的连接池,同时保持一个ResultSet和本对象的映射,以便于在关闭resultSet的时候能找到相对应的connect.
 *
 * @author yimin
 */
class ConnectionPool {
	protected static final Logger logger = LogManager.getLogger(ConnectionPool.class.getName());
	final Map<Connection, MyConnection> rc = new Hashtable<>();

	private final LinkedList<MyConnection> myConnections = new LinkedList<>();
	private MyConnection nowFree = null;
	private int size = 0;

	private final int maxPoolNumber;
	private final int connectionTimeOut;
	private final AbstractDataBaseHandle handler;

	ConnectionPool(int maxPoolNumber, int connectionTimeOut, AbstractDataBaseHandle handler) {
		this.handler = handler;
		this.connectionTimeOut = connectionTimeOut * 1000 * 60;
		this.maxPoolNumber = maxPoolNumber;

		while (size < maxPoolNumber) {
			Connection connection = handler.getNewJDBCConnection();
			add(connection, new MyConnection(this, connection));
		}
	}

	synchronized Connection getFreeConn() {
		MyConnection now = nowFree;
		// 找到一个空闲的连接.
		for (int i = 1; now.busy; i++) {
			if (i % size == 0) {
				// 如果所有都忙,等待.
				try {
					logger.trace("大家都特别忙,在这里等一下.");
					wait();
				} catch (InterruptedException e) {
					logger.error("出现异常", e);
				}
			}

			now = now.next;
		}

		nowFree = now.next;
		return handleIdle(now).setBusy(true);
	}

	private MyConnection handleIdle(MyConnection connection) {
		if (System.currentTimeMillis() - connection.beginUserTime > connectionTimeOut) {
			logger.trace("连接空闲超过指定时间，更换连接。");
			connection.release(handler.getNewJDBCConnection());
		}

		return connection;
	}

	private synchronized void add(Connection connection, MyConnection myConnection) {
		rc.put(connection, myConnection);

		size++;
		if (myConnections.isEmpty()) {
			myConnections.add(myConnection);
			nowFree = myConnection;
			myConnection.next = myConnection;
		} else {
			myConnections.getLast().next = myConnection;
			myConnection.next = myConnections.getFirst();
			myConnections.offer(myConnection);
		}

		logger.trace("增加一个连接对象，Conn:{},myConn:{},size:{}", connection, myConnection, size);
		notify();
	}

	void closeConnection(Connection con) {
		try {
			rc.get(con).close();
		} catch (SQLException e) {
			logger.error("关闭连接出现异常", e);
		}
	}
}
