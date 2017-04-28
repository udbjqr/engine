package com.jg.identification;

import java.util.List;

/**
 * 此接口为所有认证对象接口超类.
 *
 * @author yimin
 */
@SuppressWarnings("unused")
public interface OrganizationStructure<K extends OrganizationStructure> {

	/**
	 * 返回对象唯一编码
	 *
	 * @return 对象唯一编码
	 */
	int getId();

	/**
	 * 返回指定的名称
	 *
	 * @return 名称
	 */
	String getName();

	/**
	 * 得到指定名称的值.
	 *
	 * @param name 名称
	 * @return 根据请求的类型给出对应的值，可能产生类型转换异常。
	 */
	<T> T get(String name);

	/**
	 * 为此对象设置对应名称的值.
	 * <p>
	 * 有可能出现异常
	 *
	 * @param name  属性的名称
	 * @param value 需要设置的值
	 */
	void set(String name, Object value);

	/**
	 * 刷新数据至持久层.
	 * <p>
	 * 此方法为底层方法。强制刷新持久层数据
	 *
	 * @return true 成功，false 失败。
	 */
	boolean flush();

	/**
	 * 得到所有下级岗位列表
	 *
	 * @return 岗位列表
	 */
	List<Role> getAllRoles();

	/**
	 * 得到岗位列表
	 *
	 * @return 岗位列表
	 */
	List<Role> getRoles();

	/**
	 * 得到部门列表
	 *
	 * @return 部门列表
	 */
	List<Department> getDepartments();

	/**
	 * 得到所有部门列表
	 *
	 * @return 包括所有深度的部门列表
	 */
	List<Department> getAllDepartments();

	/**
	 * 得到所有下属人员
	 * <p>
	 * 包括下属的下属
	 *
	 * @return 人员的列表
	 */
	List<User> getAllUsers();

	/**
	 * 得到下属人员
	 *
	 * @return 人员的列表
	 */
	List<User> getUsers();

	/**
	 * 得到上级部门对象.
	 * <p>
	 * 如果无上级部门，返回null.
	 *
	 * @return 上级部门对象
	 */
	K getParent();


	/**
	 * 将一个人员加入到人员所属列表.
	 * <p>
	 * 可能抛出各种异常
	 *
	 * @param user 所属人员对象。
	 */
	void addBelongUser(User user);

	/**
	 * 将一个岗位加入到岗位所属列表
	 *
	 * @param role 需要加入的岗位
	 */
	void addBelongRole(Role role);

	/**
	 * 将一个部门加入所属部门列表当中.
	 *
	 * @param department 要加入的部门
	 */
	void addBelongDepartment(Department department);


	/**
	 * 判断指定部门是否所属部门.
	 * <p>
	 * 对于部门与企业来说，判断是否下级所属部门，
	 * 对于岗位与人员来说，判断是否自身所属部门
	 *
	 * @param department 需要检测的部门
	 * @return true 是所属部门
	 */
	boolean isBelongDepartment(Department department);

	/**
	 * 判断指定岗位是否所属岗位
	 *
	 * @param role 需要检测的岗位
	 * @return true 是
	 */
	boolean isBelongRole(Role role);

	/**
	 * 判断指定人员是否所属人员
	 *
	 * @param user 需要检测的人员对象
	 * @return true 是
	 */
	boolean isBelongUser(User user);

	/**
	 * 将指定部门从所属部门列表当中移除
	 *
	 * @param department 需要移除的部门
	 */
	void removeBelongDepartment(Department department);

	/**
	 * 将指定岗位从所属子岗位列表当中移除
	 *
	 * @param role 需要移除的岗位
	 */
	void removeBelongRole(Role role);

	/**
	 * 将指定人员从所属人员列表移除.
	 * <p>
	 * 当需要同时移除其他对应关系时，使用${safeRemoveBelongUser(User user)}方法
	 *
	 * @param user 需要移除的人员对象
	 */
	void removeBelongUser(User user);

	/**
	 * 安全的移除指定人员.
	 * <p>
	 * 当人员从列表当中移除时，同时将此人员从对应的其他关系内移除。
	 * <p>
	 * 此方法总是成功移除。
	 *
	 * @param user 需要移除的人员。
	 */
	void safeRemoveBelongUser(User user);


	/**
	 * 返回是否为最高级.
	 * <p>
	 * 同时允许多个istop为true.即允许多个最高级。
	 *
	 * @return true 是
	 */
	boolean isTop();

	/**
	 * 设置是否为顶级。
	 *
	 * @param top true:设置为顶级。
	 */
	void setTop(boolean top);

	/**
	 * 返回当前对象所属的企业对象.
	 *
	 * @return 企业对象
	 */
	Company getCompany();

	/**
	 * 返回当前对象所属顶级企业对象.
	 * <p>
	 * 如果有多层企业,总是返回顶层企业对象
	 *
	 * @return 企业对象
	 */
	Company getTopCompany();

	/**
	 * 得到下属部门对象.
	 *
	 * @param departmentId 部门的Id
	 * @return 部门的对象
	 */
	Department getSubDepartment(int departmentId);

	/**
	 * 得到下属人员对象.
	 *
	 * @param userId 人员的Id
	 * @return 人员的对象
	 */
	User getSubUser(int userId);

	/**
	 * 得到下属角色对象.
	 *
	 * @param roleId 角色Id
	 * @return 角色对象
	 */
	Role getSubRole(int roleId);
}
