package com.jg.identification;

/**
 * 权限对象的接口.
 *
 * 此接口定义权限对象需要提供的方法。
 *
 * 每一个实现此接口的对象将表示一个权限对象。
 * @author yimin
 */
public interface Permission {


	/**
	 * 给出权限对象Id值，每一个权限将不同。
	 * @return int值指定的ID值。
	 */
	int getId();

	/**
	 * 返回此对象是否与另一对象相等.
	 *
	 * 默认方法通过判断id是否相等。
	 *
	 * @param other 另一个对象
	 * @return true 相等，false 不想等
	 */
	default boolean equals(Permission other){
		return this.getId() == other.getId();
	}

	/**
	 * 返回为此权限定义的名称
	 * @return 一个string格式的名称
	 */
	String getName();
}
