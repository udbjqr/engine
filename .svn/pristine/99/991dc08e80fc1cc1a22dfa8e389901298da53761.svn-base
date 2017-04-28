package com.jg.identification;

import java.util.Hashtable;
import java.util.Map;

/**
 * 此类定义上下文对象,需要对象可从此类获得.
 * <p>
 * 此类为静态类,无需要获得实例.
 * <p>
 * 从此类获得的对象将隐藏实际的操作类,建议从此类获得对象.
 * <p>
 *
 * @author yimin
 */

public final class Context {
	private static User currentOperatorUser;
	private static Map<Integer, Company> companyMap = new Hashtable<>();

	/**
	 * 根据用户登录名获得用户对象.
	 *
	 * @param loginName 用户登录名
	 * @return 用户对象
	 */
	public static User getUserByLoginName(String loginName) {
		Company company = getCurrentCompany();

		if (company != null) {
			for (User user : company.getAllUsers()) {
				if (user.getLoginName().equals(loginName)) {
					return user;
				}
			}
		}

		return null;
	}

	/**
	 * 根据用户登录名获得用户对象.
	 *
	 * @param userId 用户Id
	 * @return 用户对象
	 */
	public static User getUserById(int userId) {
		Company company = getCurrentCompany();

		if (company != null) {
			for (User user : company.getAllUsers()) {
				if (user.getId() == userId) {
					return user;
				}
			}
		}

		return null;
	}

	/**
	 * 根据用户登录名获得用户对象.
	 *
	 * @param userId 用户Id
	 * @return 用户对象
	 */
	public static User getUserById(int companyId, int userId) {
		Company company = companyMap.get(companyId);

		if (company != null) {
			for (User user : company.getAllUsers()) {
				if (user.getId() == userId) {
					return user;
				}
			}
		}

		return null;
	}

	/**
	 * 根据用户登录名获得用户对象.
	 *
	 * @param loginName 用户登录名
	 * @return 用户对象
	 */
	public static User getUserByLoginName(int companyId, String loginName) {
		Company company = companyMap.get(companyId);

		if (company != null) {
			for (User user : company.getAllUsers()) {
				if (user.getLoginName().equals(loginName)) {
					return user;
				}
			}
		}

		return null;
	}


	private static Company getCurrentCompany() {
		return currentOperatorUser.getCompany();
	}

	/**
	 * 获得企业对象
	 *
	 * @param companyId 企业Id
	 * @return 企业对象
	 */
	public static Company getCompanyById(int companyId) {
		return companyMap.get(companyId);
	}

	/**
	 * 返回当前操作人对象.
	 * <p>
	 * 此对象必须指定为当前的操作人.后续很多操作由此部分获得当前操作人参与操作。
	 *
	 * @return 当前操作人对象。
	 */
	public static User getCurrentOperatorUser() {
		return currentOperatorUser;
	}

	public static void setCurrentOperatorUser(User user) {
		currentOperatorUser = user;
	}

	public static void addCompany(Company company) {
		companyMap.put(company.getId(), company);
	}
}
