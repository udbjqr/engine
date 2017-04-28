package com.jg.identification;

import java.util.List;

/**
 * 岗位接口.
 * <p>
 * 此接口的实现实表示一个岗位。
 *
 * @author yimin
 */
@SuppressWarnings("unused")
public interface Role extends OrganizationStructure<Role>, PermissionControl {

	/**
	 * 得到所有下级岗位列表
	 * <p>
	 * 当前岗位无对应吏属关系。
	 *
	 * @return 总是返回null
	 */
	default List<Role> getAllRoles() {
		return getRoles();
	}

	/**
	 * 得到岗位列表
	 * <p>
	 * 当前岗位无对应吏属关系。
	 *
	 * @return 总是返回null
	 */
	default List<Role> getRoles() {
		return null;
	}

	/**
	 * 得到上级部门对象.
	 * <p>
	 * 当前岗位无对应吏属关系。
	 *
	 * @return 总是返回null
	 */
	default Role getParent() {
		return null;
	}

	/**
	 * 判断指定岗位是否所属岗位
	 * <p>
	 * 当前岗位无对应吏属关系。
	 *
	 * @return 总是返回false
	 */
	default boolean isBelongRole(Role role) {
		return false;
	}

	/**
	 * 判断指定人员是否所属人员
	 *
	 * @param user 需要检测的人员对象
	 * @return true 是
	 */
	boolean isBelongUser(User user);

	/**
	 * 将指定岗位从所属子岗位列表当中移除
	 * <p>
	 * <p>
	 * 当前岗位无对应吏属关系。
	 * <p>
	 * 无任何操作返回
	 */
	default void removeBelongRole(Role role) {
	}

	default Department getSubDepartment(int departmentId) {
		return null;
	}

	default User getSubUser(int userId) {
		return null;
	}

	default Role getSubRole(int roleId) {
		return null;
	}
}
