package com.jg.workflow;

import com.jg.identification.Company;
import com.jg.identification.User;
import com.jg.workflow.process.ProcessManager;
import com.jg.workflow.process.ProcessManagerImpl;
import com.jg.workflow.process.model.ModelManager;
import com.jg.workflow.process.model.ModelManagerImpl;
import com.jg.workflow.task.TaskManager;
import com.jg.workflow.task.TaskManagerImpl;

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
	/**
	 * 根擗用户对象, 获得流程管理对象.
	 *
	 * @param user 用户对象
	 * @return 流程管理对象.
	 */
	public static ProcessManager getProcessManager(User user) {
		return ProcessManagerImpl.getInstance(user.getTopCompany());
	}

	/**
	 * 根据企业对象, 获得流程管理对象.
	 *
	 * @param company 企业对象
	 * @return 流程管理对象.
	 */
	public static ProcessManager getProcessManager(Company company) {
		return ProcessManagerImpl.getInstance(company);
	}


	/**
	 * 根据企业对象, 获得任务管理对象.
	 *
	 * @param company 企业对象
	 * @return 任务管理对象.
	 */
	public static TaskManager getTaskManager(Company company) {
		return TaskManagerImpl.getInstance(company);
	}

	public static ModelManager getModelManager(Company company){
		return ModelManagerImpl.getInstance(company);
	}
}
