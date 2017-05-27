package com.jg.common.persistence;


import com.jg.identification.User;

/**
 * 实现此接口的对象，表明可以直接对持久层做读写操作.
 * <p>
 * 每一个实现此接口的对象代表一行记录。
 * <p>
 * 此接口的实现类可自行处理对应的持久层保存/读取/更新。保证与持久层的数据记录一致。
 * <p>
 * 实例对象已经实例化，因此新增加对象的方法采用对象自身的构造方法获得。
 */
public interface Persistence {
	/**
	 * 得到指定名称的数据的值.
	 *
	 * @param name 指定返回值的名称。
	 * @param <T>  指定返回值的类型
	 * @return 如果名称未指定，返回null
	 */
	<T> T get(String name);

	/**
	 * 得到指定名称的数据的值.
	 *
	 * @param name         指定返回值的名称。
	 * @param defaultValue 如果得到的值为NULL.返回默认值。
	 * @param <T>          指定返回值的类型
	 * @return 如果名称未指定，返回null
	 */
	<T> T get(String name, T defaultValue);

	/**
	 * 将一个数据写入指定名称的值当中
	 *
	 * @param name  指定的列名称
	 * @param value 要更新的值
	 * @param <T>   值的类型。
	 */
	<T> Persistence set(String name, T value) throws WriteValueException;

	/**
	 * 将一个数据写入指定名称的值当中
	 *
	 * @param field  指定的列对象
	 * @param value 要更新的值
	 * @param <T>   值的类型。
	 */
	<T> Persistence set(Field field, T value) throws WriteValueException;
	/**
	 * 刷新数据至持久层.
	 * <p>
	 * 此方法为底层方法。强制刷新持久层数据
	 *
	 * @return true 成功，false 失败。
	 */
	default boolean flush() {
		return false;
	}

	/**
	 * 将此接口代表的数据删除.
	 * <p>
	 * 具体的删除实现由实现类自己实现。
	 *
	 * @return 成功：true，失败，false
	 */
	boolean delete();

	/**
	 * 返回指定的列名是否在此用户可用.
	 * <p>
	 * 在这里得到false的值，使用get方法将始终返回null
	 *
	 * @param user      用户对象
	 * @param fieldName 指定的列名
	 * @return true 可以显示 false 不能显示
	 */
	boolean canViewField(User user, String fieldName);

	/**
	 * 设置序列号的值
	 */
	void setIdBySequence();
}
