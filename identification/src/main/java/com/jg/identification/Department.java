package com.jg.identification;

/**
 * 部门接口.
 *
 * @author yimin
 */
@SuppressWarnings("unused")
public interface Department extends OrganizationStructure<Department> {

	/**
	 * 返回此部门的管理员对象.
	 * <p>
	 * 当未设置管理员时，此对象返回为null
	 *
	 * @return 部门的管理员对象。
	 */
	User getAdministrator();


	/**
	 * 设置本部门的管理员对象。
	 *
	 * @param user 需要设置的管理员对象。
	 * @return 0:正常，1:人员对象为null,2:人员非本部门人员。
	 */
	int setAdministrator(User user);
}
