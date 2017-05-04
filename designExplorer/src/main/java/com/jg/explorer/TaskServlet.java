package com.jg.explorer;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.result.HttpResult;
import com.jg.identification.Company;
import com.jg.workflow.Context;
import com.jg.workflow.exception.TaskIsNull;
import com.jg.workflow.task.Task;
import com.jg.workflow.task.TaskManagerImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import java.util.List;

import static com.jg.common.result.HttpResult.*;

/**
 * 任务管理的Servlet.
 * <p>
 * create by 17/5/4.
 *
 * @author yimin
 */
@WebServlet(name = "task", urlPatterns = "/task")
public class TaskServlet extends BaseServlet {
	private final static Logger log = LogManager.getLogger(TaskServlet.class.getName());

	@Override
	protected HttpResult execute(HttpRequestType type, ServletData servletData) {
		Company company = (Company) servletData.get(COMPANY);
		JSONObject jsonData = (JSONObject) servletData.get(JSON_DATA);
		TaskManagerImpl taskManager = (TaskManagerImpl) Context.getTaskManager(company);

		switch (type) {
			case loadmytasks:
				return loadMyTasks(taskManager);
			case handle:
				return handleTask(jsonData, taskManager);
			default:
				log.error("未找到此类型定义的操作。type:" + type.name());
				return UNKNOWN;
		}
	}

	private HttpResult handleTask(JSONObject jsonData, TaskManagerImpl taskManager) {
		Integer taskId = (Integer) jsonData.get("taskId");

		if (taskId == null) {
			return NO_SET_REQUEST_TYPE.clone().setMessage("未找到需要的参数：taskId");
		}
		JSONObject variable = (JSONObject) jsonData.get("variable");

		if (variable == null) {
			return NO_SET_REQUEST_TYPE.clone().setMessage("未找到需要的参数：variable");
		}

		HttpResult result;
		try {
			result = taskManager.handle(taskId, variable);
		} catch (TaskIsNull e) {
			return TASK_UNAVAILABLE.clone().setMessage("指定的任务当前不可用或者Id错误。id:" + taskId);
		}

		if (result.isSuccess()) {
			taskManager.complete(taskId);
		}

		return result;
	}

	private HttpResult loadMyTasks(TaskManagerImpl taskManager) {
		List<Task> tasks = taskManager.getMyTasks();

		if (tasks != null) {
			return SUCCESS.clone().setListToData(RESULT_LIST, tasks, "id", "execution_id", "name", "module_id", "handle_id");
		}

		return UNKNOWN;
	}


}
