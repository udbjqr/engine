package com.jg.identification.implement.test;

import com.jg.identification.*;

import java.util.ArrayList;
import java.util.List;

/**
 * create by 2017/4/17.
 *
 * @author yimin
 */

public class UserImpl implements User {
	private final int id;
	private final String name;

	private List<Role> roles = new ArrayList<>();
	private List<Department> departments = new ArrayList<>();
	private List<User> users = new ArrayList<>();
	private Company company;
	private User parent;
	private String loginName;

	public UserImpl(int id, String name, String loginName, Company company, User parent) {
		this.company = company;
		this.id = id;
		this.name = name;
		this.parent = parent;
		this.loginName = loginName;
		this.company.addBelongUser(this);
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
	public List<Role> getRoles() {
		return roles;
	}

	@Override
	public List<Department> getDepartments() {
		return departments;
	}

	@Override
	public List<User> getAllUsers() {
		throw new NotSupportOperator("get");
	}

	@Override
	public List<User> getUsers() {
		throw new NotSupportOperator("getUsers");
	}

	@Override
	public User getParent() {
		return parent;
	}

	@Override
	public void addBelongUser(User user) {
		if (!users.contains(user)) {
			users.add(user);

			user.setParent(this);
		}
	}

	@Override
	public void addBelongRole(Role role) {
		role.addBelongUser(this);
	}

	@Override
	public void addBelongDepartment(Department department) {
		department.addBelongUser(this);
	}

	@Override
	public boolean isBelongDepartment(Department department) {
		return department.isBelongUser(this);
	}

	@Override
	public boolean isBelongRole(Role role) {
		return role.isBelongUser(this);
	}

	@Override
	public boolean isBelongUser(User user) {
		return users.contains(user);
	}

	@Override
	public void removeBelongDepartment(Department department) {
		department.removeBelongUser(this);
	}

	@Override
	public void removeBelongRole(Role role) {
		role.removeBelongUser(this);
	}

	@Override
	public void removeBelongUser(User user) {
		users.remove(user);

		if (this.equals(user.getParent())) {
			user.setParent(null);
		}
	}

	@Override
	public void safeRemoveBelongUser(User user) {
		removeBelongUser(user);
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
	public LookOverPermission getLookOverPermission() {
		return new LookOverPermission();
	}

	@Override
	public boolean hasPermission(Permission permission) {
		return true;
	}

	@Override
	public boolean login(String passWord) {
		return true;
	}

	@Override
	public String getLoginName() {
		return loginName;
	}

	@Override
	public void setParent(User user) {
		parent = user;
	}
}
