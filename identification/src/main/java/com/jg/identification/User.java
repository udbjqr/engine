package com.jg.identification;

import java.util.List;

/**
 * 用户权限部分接口，此接口指定当前所对应的操作用户.
 *
 * @author yimin
 */
@SuppressWarnings("unused")
public interface User extends PermissionControl, OrganizationStructure<User> {
	/**
	 * 用户的登录操作
	 *
	 * @param passWord 用户密码
	 * @return true 成功
	 */
	boolean login(String passWord);

	/**
	 * 返回登录名称
	 *
	 * @return 登录名称
	 */
	String getLoginName();

	/**
	 * 返回此人员对应所有的岗位列表
	 * <p>
	 * 返回同${getRoles()}
	 *
	 * @return 岗位列表
	 */
	default List<Role> getAllRoles() {
		return getRoles();
	}

	/**
	 * 得到此人员所在部门列表
	 *
	 * @return 返回同${getDepartments()}
	 */
	default List<Department> getAllDepartments() {
		return getDepartments();
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

	/**
	 * 设置人员的对口上级人员
	 *
	 * @param user 上级人员
	 */
	void setParent(User user);
}
