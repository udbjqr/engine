package com.jg.workflow.process;

import com.jg.identification.Company;
import com.jg.workflow.Context;
import com.jg.workflow.process.Model.TestModel;
import com.jg.workflow.task.Task;
import com.jg.workflow.task.TaskManager;
import com.jg.workflow.util.TestIdentificationUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * create by 17/4/24.
 *
 * @author yimin
 */

public class TestTaskManager {
	private static Logger log = LogManager.getLogger(TestModel.class.getName());
	private static Company company;
	private static TaskManager taskManager;

	@BeforeClass
	public static void beforeClass() {
		TestIdentificationUtil.setEmployers1();

		company = com.jg.identification.Context.getCompanyById(1);

		com.jg.identification.Context.setCurrentOperatorUser(com.jg.identification.Context.getUserById(1, 3));

		taskManager = Context.getTaskManager(company);
	}

	@Test
	public void testGetMyTasks(){
		for (Task task : taskManager.getMyTasks()) {
			log.info(task.getId() + " \t "  + task.get("name"));
		}
	}

	@Test
	public void tastHandle(){
		taskManager.handle(250,null);
	}

	@Test
	public void tastComplate(){
		taskManager.complete(250);
	}
}
