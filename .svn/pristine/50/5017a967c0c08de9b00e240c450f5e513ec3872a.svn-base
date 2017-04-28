package com.jg.identification.implement.test;

import com.jg.identification.Company;
import com.jg.identification.Department;
import com.jg.identification.Role;
import com.jg.identification.User;

import java.util.ArrayList;
import java.util.List;

/**
 * create by 2017/4/17.
 *
 * @author yimin
 */

public class CompanyImpl implements Company {
	private final int id;
	private final String name;

	private List<Role> roles = new ArrayList<>();
	private List<Department> departments = new ArrayList<>();
	private List<User> users = new ArrayList<>();

	public CompanyImpl(int id, String name) {
		this.id = id;
		this.name = name;

	}

	@Override
	public void addSubCompany(Company company) {
		throw new NotSupportOperator("addSubCompany");
	}

	@Override
	public void removeSubCompany(Company company) {
		throw new NotSupportOperator("addSubCompany");
	}

	@Override
	public List<Company> getCompanies() {
		return null;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public <T> T get(String name) {
		return null;
	}

	@Override
	public void set(String name, Object value) {
		throw new NotSupportOperator("addSubCompany");
	}

	@Override
	public boolean flush() {
		return true;
	}

	@Override
	public List<Role> getAllRoles() {
		return roles;
	}

	@Override
	public List<Role> getRoles() {
		return roles;
	}

	@Override
	public List<Department> getDepartments() {
		return departments;
	}

	@Override
	public List<Department> getAllDepartments() {
		return departments;
	}

	@Override
	public List<User> getAllUsers() {
		return users;
	}

	@Override
	public List<User> getUsers() {
		return users;
	}

	@Override
	public Company getParent() {
		return null;
	}

	@Override
	public void addBelongUser(User user) {
		if (!isBelongUser(user))
			users.add(user);
	}

	@Override
	public void addBelongRole(Role role) {
		if (!isBelongRole(role))
			roles.add(role);
	}

	@Override
	public void addBelongDepartment(Department department) {
		if (!isBelongDepartment(department))
			departments.add(department);
	}

	@Override
	public boolean isBelongDepartment(Department department) {
		return departments.contains(department);
	}

	@Override
	public boolean isBelongRole(Role role) {
		return roles.contains(role);
	}

	@Override
	public boolean isBelongUser(User user) {
		return users.contains(user);
	}

	@Override
	public void removeBelongDepartment(Department department) {
		departments.remove(department);
	}

	@Override
	public void removeBelongRole(Role role) {
		roles.remove(role);
	}

	@Override
	public void removeBelongUser(User user) {
		users.remove(user);
	}

	@Override
	public void safeRemoveBelongUser(User user) {
		users.remove(user);

		for (Department department : departments) {
			if (department.isBelongUser(user)) {
				department.removeBelongUser(user);
			}
		}

		for (Role role : roles) {
			if (role.isBelongUser(user)) {
				role.removeBelongUser(user);
			}
		}
	}

	@Override
	public boolean isTop() {
		return true;
	}

	@Override
	public void setTop(boolean top) {

	}

	@Override
	public Company getCompany() {
		return this;
	}

	@Override
	public Company getTopCompany() {
		return this;
	}

	@Override
	public Department getSubDepartment(int departmentId) {
		for (Department department : departments) {
			if (department.getId() == departmentId) {
				return department;
			}
		}

		return null;
	}

	@Override
	public User getSubUser(int userId) {
		for (User user : users) {
			if (user.getId() == userId) {
				return user;
			}
		}

		return null;
	}

	@Override
	public Role getSubRole(int roleId) {
		for (Role role : roles) {
			if (role.getId() == roleId) {
				return role;
			}
		}

		return null;
	}
}
