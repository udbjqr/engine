package com.jg.workflow.util;

import com.jg.identification.Company;
import com.jg.identification.Department;
import com.jg.identification.Role;
import com.jg.identification.User;
import com.jg.identification.implement.test.CompanyImpl;
import com.jg.identification.implement.test.DepartmentImpl;
import com.jg.identification.implement.test.RoleImpl;
import com.jg.identification.implement.test.UserImpl;
import org.apache.logging.log4j.core.util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * create by 2017/4/18.
 *
 * @author yimin
 */

public class TestIdentificationUtil {
	/**
	 * 测试的设置用户对象.
	 * <p>
	 * 此方法设置两个企业。第二企业为空。
	 * <p>
	 * 第一个企业内三个部门，二个岗位，4个人员。
	 * <p>
	 * 所有岗位属于所有部门内。
	 * <p>
	 * 第一个部门只有一个1号员工。
	 * 第二个部门有2，3号员工
	 * 第三个部门有2,3,4号员工
	 * <p>
	 * 第一个岗位有1，2，3 3个员工
	 * 第二个岗位有 1,2,3,4 4个员工
	 * <p>
	 * 1号员工为顶级，2号上级是1号，3，4号上级是2号
	 */
	public static void setEmployers1() {
		Company oneCompnay = new CompanyImpl(1, "第一家企业");
		Company twoCompnay = new CompanyImpl(2, "第二家企业");
		com.jg.identification.Context.addCompany(oneCompnay);
		com.jg.identification.Context.addCompany(twoCompnay);

		Department oneDepartment1 = new DepartmentImpl(1, "总经办1", oneCompnay, null);
		Department twoDepartment1 = new DepartmentImpl(2, "技术部1", oneCompnay, null);
		Department threeDepartment1 = new DepartmentImpl(3, "客服部1", oneCompnay, null);

		Role oneRole = new RoleImpl(1, "主管", oneCompnay);
		Role twoRole = new RoleImpl(2, "员工", oneCompnay);

		oneDepartment1.addBelongRole(oneRole);
		oneDepartment1.addBelongRole(twoRole);
		twoDepartment1.addBelongRole(oneRole);
		twoDepartment1.addBelongRole(twoRole);
		threeDepartment1.addBelongRole(oneRole);
		threeDepartment1.addBelongRole(twoRole);


		User oneUser = new UserImpl(1, "第一人", "first", oneCompnay, null);
		User twoUser = new UserImpl(2, "第二人", "second", oneCompnay, null);
		User threeUser = new UserImpl(3, "第三人", "third", oneCompnay, null);
		User fourUser = new UserImpl(4, "第四人", "fourth", oneCompnay, null);

		oneDepartment1.addBelongUser(oneUser);
		twoDepartment1.addBelongUser(twoUser);
		twoDepartment1.addBelongUser(threeUser);
		threeDepartment1.addBelongUser(twoUser);
		threeDepartment1.addBelongUser(threeUser);
		threeDepartment1.addBelongUser(fourUser);

		oneRole.addBelongUser(oneUser);
		oneRole.addBelongUser(twoUser);
		oneRole.addBelongUser(threeUser);
		twoRole.addBelongUser(oneUser);
		twoRole.addBelongUser(twoUser);
		twoRole.addBelongUser(threeUser);
		twoRole.addBelongUser(fourUser);

		twoUser.setParent(oneUser);
		threeUser.setParent(twoUser);
		fourUser.setParent(twoUser);

		Department oneDepartment2 = new DepartmentImpl(1, "总经办2", twoCompnay, null);
		Department twoDepartment2 = new DepartmentImpl(2, "技术部2", twoCompnay, null);
		Department threeDepartment2 = new DepartmentImpl(3, "客服部2", twoCompnay, null);
	}

	public static String readJsonFormFile(String fileName) throws IOException {
		InputStream inStream = TestIdentificationUtil.class.getResourceAsStream(fileName);

		BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));

		return IOUtils.toString(reader);
	}
}
