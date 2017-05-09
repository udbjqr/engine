package com.jg.workflow.util;

import com.jg.identification.Company;
import com.jg.identification.Context;
import com.jg.identification.User;
import com.jg.workflow.exception.CompanyIsNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * 提供转换的对象.
 */
public final class UserExpressionUtil {
	private final static Logger logger = LogManager.getLogger(UserExpressionUtil.class.getName());

	private final static String NULLString = "";

	/**
	 * 根据传入的名称，返回用“，”分隔的用户的名称.
	 * <p>
	 * 根据当前数据库内人员与部门、岗位的关系，转换出对应人员的对象。
	 * <p>
	 * 此对象提供用户、部门、岗位之间的关系，并且以一个字段来获得所需要的用户身份。
	 * <p>
	 * 使用","来分隔，前缀大写字母为指明类型。
	 * 例：“E101,E23,D29,R2,SD20R44”
	 * E:用户对象，后续数字为用户对象ID
	 * D：部门，后续数字为部门ID
	 * R：岗位，后续数据为岗位ID
	 * S：部门与岗位，后续必须也只能带一个部门和一个岗位。
	 * 每一个分隔为指定单一ID，需要多个可以多写
	 * <p>
	 * 提供的数据将会根据当时数据库内数据，给出对应的用户名称。
	 * <p>
	 * 多个用户可以重复，转换后仅提供单一个用户名。
	 *
	 * @param names 需要转换的名称
	 * @return 如果未找到或者没有对象时返回为“”,如果参数格式错误，将返回null
	 */
	public static String transformUser(String names, Integer companyId) {
		logger.trace("匹配用户，开始查找指定用户。");


		if (names == null || names.equals("")) {
			logger.trace("未传入值，返回空值");
			return NULLString;
		}


		StringBuilder builder = new StringBuilder();
		for (User employee : getLists(names, companyId)) {
			builder.append(employee.get("login_name").toString()).append(",");
		}
		if (builder.length() > 1) {
			builder.delete(builder.length() - 1, builder.length());
		}

		return builder.toString();
	}

	public static List<User> getLists(String names, Integer companyId) {
		ArrayList<User> tempUsers = new ArrayList<>();
		Company company = Context.getCompanyById(companyId);
		com.jg.identification.Department department;
		com.jg.identification.Role role;
		if (company == null) {
			throw new CompanyIsNull("未定义的ID:" + companyId);
		}

		for (String name : names.split(",")) {
			if (name.startsWith("S")) {//部门+岗位的处理方式
				String[] drs = name.substring(1).split("R"); //SD20R44 格式
				if (drs.length == 2) {
					drs[0] = drs[0].substring(1);
					department = company.getSubDepartment(Integer.parseInt(drs[0]));
					if (department != null) {
						role = department.getSubRole(Integer.parseInt(drs[1]));
						if (role != null) {
							return role.getAllUsers();
						}
					}
				}
			} else if (name.startsWith("E")) {  //人员的处理方式
				tempUsers.add(company.getSubUser(Integer.parseInt(name.substring(1))));
			} else if (name.startsWith("D")) {  //部门的处理方式
				department = company.getSubDepartment(Integer.parseInt(name.substring(1)));
				if (department != null) {
					return department.getUsers();
				}
			} else if (name.startsWith("R")) {//岗位的处理方式
				role = company.getSubRole(Integer.parseInt(name.substring(1)));
				if (role != null) {
					return role.getUsers();
				}
			} else {
				logger.error("传入的参数格式错误，请检查，{}，参数开头只能是'E、D、R、S'，并以_ 分隔。", name);
			}
		}

		return tempUsers;
	}

}