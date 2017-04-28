package com.jg.common.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据操作对象，此对象直接对相关数据库进行操作.
 */
public interface DBHelper {
	/**
	 * 执行一个更新或者删除操作.
	 *
	 * @param sql 要执行的sql语句.
	 * @return 成功影响的条数
	 */
	int update(String sql);


	/**
	 * 执行一个只返回单个值的select语句,此操作不需要关闭动作.
	 *
	 * @param sql 需要执行的sql语句
	 * @param <T> 需要返回的结果集数据。
	 * @return 执行结果的一个单独的值
	 */
	<T> T selectOneValues(String sql);

	/**
	 * 执行一个查询过程,使用完毕必须使用close()来关闭.
	 * <p>
	 * 必须使用{@link #close(ResultSet)}方法将得到的数据集关闭。未关闭将导致数据连接一直存在而无法提供相应的服务。
	 *
	 * @param sql 需要执行的查询语句.
	 * @return 一个结果集.用完此结果集需要调用close()过程进行关闭操作.
	 */
	ResultSet select(String sql) throws SQLException;

	/**
	 * 执行一个查询过程,使用完毕必须使用close()来关闭.
	 * <p>
	 * 必须使用{@link #close(ResultSet)}方法将得到的数据集关闭。未关闭将导致数据连接一直存在而无法提供相应的服务。
	 *
	 * @param sql    需要执行的查询语句.
	 * @param limit  需要返回的行数
	 * @param offset 从第offset行开始返回
	 * @return 一个结果集.用完此结果集需要调用close()过程进行关闭操作.
	 */
	ResultSet select(String sql, int limit, int offset) throws SQLException;

	/**
	 * 关闭一个结果集对应的所有对象.
	 * <p>
	 * 见{@link #select(String)}
	 *
	 * @param set 本对象返回的结果集.
	 */
	void close(ResultSet set);

	/**
	 * 执行一系列sql语句.
	 * <p>
	 * 此方法将采用事务保证数据的一致性。
	 *
	 * @param sqlStrings 需要执行的sql语句集合，数组每一个元素存储一条语句。
	 */
	void execBatchSql(Iterable<String> sqlStrings) throws SQLException;

	/**
	 * 执行一系列sql语句.
	 * <p>
	 * 此方法将采用事务保证数据的一致性。
	 *
	 * @param sqlStrings 需要执行的sql语句集合，数组每一个元素存储一条语句。
	 */
	void execBatchSql(String[] sqlStrings);


	/**
	 * 执行一系列sql语句,并且返回一个单一的值.
	 * <p>
	 * 此方法将采用事务保证数据的一致性。
	 * <p>
	 * 调用此方法需要保证传入的sql语句大于等于2条。
	 * <p>
	 * 调用此方法需要保证传入的sql语句前几句都不返回值，最后一条返回一个int值。
	 *
	 * @param sqlStrings 需要执行的sql语句集合，数组每一个元素存储一条语句。
	 */
	<T> T execBatchSqlWithOneValue(String[] sqlStrings) throws SQLException;

	/**
	 * 将对象转换成sql语句支持的格式
	 *
	 * @param obj 需要转换的对象
	 * @return 符合数据库格式的字符串
	 */
	String getString(Object obj);

	/**
	 * 根据指定的名称，从数据库获得一个唯一的ID值
	 *
	 * @param name 唯一值的名称，根据不同的数据库唯一
	 * @return 一个唯一的值, 此值永远大于0
	 */
	Integer getNextID(String name);


	/**
	 * 执行一个查询过程.
	 * <p>
	 * 此查询过程将回调set的操作。对应于每一行调用回调一次，set.next()方法调用 。
	 *
	 * @param sql      需要执行的查询语句.
	 * @param function 需要回调的操作类
	 * @return 一个结果集.用完此结果集需要调用close()过程进行关闭操作.
	 */
	void selectWithRow(String sql, SQLSelectFunction function);

	/**
	 * 执行一个查询过程,使用完毕必须使用close()来关闭.
	 * <p>
	 * 建议使用此方法使用lamb表达示来处理相应的数据.
	 * 此查询过程将回调set的操作。对应于每一行调用回调一次，set.next()方法调用 。
	 *
	 * @param sql      需要执行的查询语句.
	 * @param limit    控制返回的条数
	 * @param offset   从第几条开始返回
	 * @param function 对数据的处理方法
	 */
	void selectWithRow(String sql, int limit, int offset, SQLSelectFunction function);

	/**
	 * 执行一个查询过程.
	 * <p>
	 * 此查询过程将回调set的操作。对于set。而无set.next()方法调用 。
	 *
	 * @param sql      需要执行的查询语句.
	 * @param function 需要回调的操作类
	 * @return 一个结果集.用完此结果集需要调用close()过程进行关闭操作.
	 */
	void selectWithSet(String sql, SQLSelectFunction function);

	/**
	 * 执行一个查询过程,使用完毕必须使用close()来关闭.
	 * <p>
	 * 建议使用此方法使用lamb表达示来处理相应的数据.
	 * * 此查询过程将回调set的操作。对于set。而无set.next()方法调用 。
	 *
	 * @param sql      需要执行的查询语句.
	 * @param limit    控制返回的条数
	 * @param offset   从第几条开始返回
	 * @param function 对数据的处理方法
	 */
	void selectWithSet(String sql, int limit, int offset, SQLSelectFunction function);

}
