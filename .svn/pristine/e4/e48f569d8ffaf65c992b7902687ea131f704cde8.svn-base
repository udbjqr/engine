package com.jg.identification.implement.test;

import com.jg.identification.*;

import java.util.ArrayList;
import java.util.List;

/**
 * create by 2017/4/17.
 *
 * @author yimin
 */

public class RoleImpl implements Role {
	private final int id;
	private final String name;

	private List<User> users = new ArrayList<>();
	private Department parent = null;
	private Company company;

	public RoleImpl(int id, String name, Company company) {
		this.company = company;
		this.id = id;
		this.name = name;
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
	public List<Department> getDepartments() {
		return null;
	}

	@Override
	public List<Department> getAllDepartments() {
		return null;
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
	public void addBelongUser(User user) {
		if (!users.contains(user)) {
			users.add(user);

			user.addBelongRole(this);
		}
	}

	@Override
	public void addBelongRole(Role role) {
		throw new NotSupportOperator("addBelongRole");
	}

	@Override
	public void addBelongDepartment(Department department) {
		throw new NotSupportOperator("addBelongDepartment");
	}

	@Override
	public boolean isBelongDepartment(Department department) {
		throw new NotSupportOperator("isBelongDepartment");
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
	public boolean isBelongUser(User user) {
		return users.contains(user);
	}

	@Override
	public void removeBelongDepartment(Department department) {
		throw new NotSupportOperator("removeBelongDepartment");
	}

	@Override
	public void removeBelongUser(User user) {
		if(users.contains(user)){
			users.remove(user);

			user.removeBelongRole(this);
		}
	}

	@Override
	public void safeRemoveBelongUser(User user) {
		removeBelongUser(user);
	}

	@Override
	public boolean isTop() {
		return false;
	}

	@Override
	public void setTop(boolean top) {
		throw new NotSupportOperator("setTop");
	}

	@Override
	public Company getCompany() {
		return company;
	}

	@Override
	public Company getTopCompany() {
		return company;
	}
}
