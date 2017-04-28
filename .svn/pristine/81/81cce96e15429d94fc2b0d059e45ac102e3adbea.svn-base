package com.jg.workflow.process.Model;

import com.jg.identification.Company;
import com.jg.workflow.Context;
import com.jg.workflow.util.TestIdentificationUtil;
import com.jg.workflow.process.model.Model;
import com.jg.workflow.process.model.ModelManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

/**
 * create by 2017/4/18.
 *
 * @author yimin
 */

public class TestModel {
	private static Logger log = LogManager.getLogger(TestModel.class.getName());
	private static Company company;


	@BeforeClass
	public static void beforeClass() {
		TestIdentificationUtil.setEmployers1();

		company = com.jg.identification.Context.getCompanyById(1);

		com.jg.identification.Context.setCurrentOperatorUser(com.jg.identification.Context.getUserById(1,3));
	}

	@Test
	public void testModelManager() {
		ModelManager modelManager = Context.getModelManager(company);

		Model model = modelManager.createModel();

		model.set("name","test1");
		model.set("category","1");
		model.set("company_id",company.getId());

		model.flush();
	}

	@Test
	public void testModelModify() throws IOException {
		ModelManager modelManager = Context.getModelManager(company);

		Model model = modelManager.getModel(3);


		model.set("name","修改");
		model.set("category","1");
		model.set("content", TestIdentificationUtil.readJsonFormFile("/testmodel.json"));
		model.set("update_time",new Date(System.currentTimeMillis()));
		model.set("company_id",company.getId());

		model.flush();
	}
}
