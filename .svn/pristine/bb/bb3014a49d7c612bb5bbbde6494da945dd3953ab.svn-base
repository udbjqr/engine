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

public class DepartmentImpl implements Department {
	private final int id;
	private final String name;

	private List<Role> roles = new ArrayList<>();
	private List<Department> departments = new ArrayList<>();
	private List<User> users = new ArrayList<>();
	private Department parent = null;
	private Company company;
	private User administrator = null;

	public DepartmentImpl(int id, String name, Company company, Department parent) {
		this.company = company;
		this.id = id;
		this.name = name;
		this.parent = parent;

		company.addBelongDepartment(this);
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
		throw new NotSupportOperator("get");
	}

	@Override
	public void set(String name, Object value) {
		throw new NotSupportOperator("set");
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
	public Department getParent() {
		return parent;
	}

	@Override
	public void addBelongUser(User user) {
		users.add(user);
		company.addBelongUser(user);
	}

	@Override
	public void addBelongRole(Role role) {
		roles.add(role);
		company.addBelongRole(role);
	}

	@Override
	public void addBelongDepartment(Department department) {
		departments.add(department);
		company.addBelongDepartment(department);
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

		for (Role role : roles) {
			if (role.isBelongUser(user)) {
				role.removeBelongUser(user);
			}
		}
	}

	@Override
	public boolean isTop() {
		return parent == null;
	}

	@Override
	public void setTop(boolean top) {

	}

	@Override
	public Company getCompany() {
		return company;
	}

	@Override
	public Company getTopCompany() {
		return company;
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

	@Override
	public User getAdministrator() {
		return administrator;
	}

	@Override
	public int setAdministrator(User user) {
		if (user == null) {
			return 2;
		}

		if (users.contains(user)) {
			return 1;
		}

		this.administrator = user;
		return 0;
	}
}
