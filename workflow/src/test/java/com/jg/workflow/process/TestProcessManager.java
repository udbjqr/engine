package com.jg.workflow.process;

import com.jg.identification.Company;
import com.jg.workflow.Context;
import com.jg.workflow.util.TestIdentificationUtil;
import com.jg.workflow.exception.ModelHasbeenDeployed;
import com.jg.workflow.process.Model.TestModel;
import com.jg.workflow.process.definition.ProcessDefinition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

/**
 * create by 2017/4/19.
 *
 * @author yimin
 */

public class TestProcessManager {
	private static Logger log = LogManager.getLogger(TestModel.class.getName());
	private static Company company;
	private static ProcessManager processManager;

	@BeforeClass
	public static void beforeClass() {
		TestIdentificationUtil.setEmployers1();

		company = com.jg.identification.Context.getCompanyById(1);

		com.jg.identification.Context.setCurrentOperatorUser(com.jg.identification.Context.getUserById(1, 3));

		processManager = Context.getProcessManager(company);
	}

	@Test
	public void testDeploymentProcess() throws ModelHasbeenDeployed {
		processManager.deploymentProcess(3);
	}

	@Test
	public void testStrartProcess() {
		processManager.startProcess(208, null);
	}


	@Test
	public void testListProcessDifinition() {
		List<ProcessDefinition> processDefinitions = processManager.getAllProcessDefinitions("", company.getId());
		processDefinitions.forEach(processDefinition -> {
			log.info(processDefinition.getId());
		});
	}

	@Test
	public void testSupProcessDefinition() {
		processManager.suspendProcessDefinition(208);
	}


	@Test
	public void testActivateProcessDefinition() {
		processManager.activateProcessDefinition(208);
	}


}
