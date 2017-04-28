package com.jg.identification;

/**
 * 权限控制接口.
 *
 * @author yimin
 */
@SuppressWarnings("unused")
public interface PermissionControl {
	/**
	 * 返回所对应的查看权限.
	 * <p>
	 * 当需要一个权限的列表时，使用getLookOverPermission().get({Name}).的方式去获得。
	 * 除有特别需求外，为空或size为0,则具有所有权限。
	 */
	LookOverPermission getLookOverPermission();

	/**
	 * 返回此用户是否有查看此字段权限
	 * <p>
	 * 默认返回true
	 *
	 * @param fieldName 字段名
	 * @return true 有相看权限
	 */
	default boolean canViewField(String fieldName) {
		return true;
	}

	/**
	 * 返回此用户是否能操作对应权限对象。
	 *
	 * @param permission 需要判断的权限对象
	 * @return true 具有权限
	 */
	boolean hasPermission(Permission permission);
}
