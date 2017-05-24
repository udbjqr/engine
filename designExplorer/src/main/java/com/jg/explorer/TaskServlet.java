package com.jg.explorer;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.result.HttpResult;
import com.jg.identification.Company;
import com.jg.workflow.Context;
import com.jg.workflow.exception.TaskIsNull;
import com.jg.workflow.process.handle.Handle;
import com.jg.workflow.process.handle.HandleFactory;
import com.jg.workflow.process.module.ModuleImpl;
import com.jg.workflow.task.Task;
import com.jg.workflow.task.TaskImpl;
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
			case load:
				return load(taskManager, jsonData);
			case loadmytasks:
				return loadMyTasks(taskManager);
			case handle:
				return handleTask(jsonData, taskManager);
			default:
				log.error("未找到此类型定义的操作。type:" + type.name());
				return UNKNOWN;
		}
	}

	private HttpResult load(TaskManagerImpl taskManager, JSONObject jsonData) {
		Integer taskId = jsonData.getInteger("taskId");
		if (taskId == null) {
			return NO_SET_REQUEST_TYPE.clone().setMessage("未找到需要的参数：taskId");
		}
		TaskImpl task;
		try {
			task = (TaskImpl) taskManager.getTask(taskId);
		} catch (TaskIsNull taskIsNull) {
			return NO_SET_REQUEST_TYPE.clone().setMessage("指定的Id无法找到对应的任务,或任务当前非激活状态，id:" + taskId);
		}

		HttpResult result = SUCCESS.clone().addInfoToValue("task", task, "id", "execution_id", "module_id", "handle_id", "due_time", "flag", "definition_id");


		ModuleImpl module = (ModuleImpl) task.getDefinition().getModule();
		Handle queryHandle = module.getQueryHandle();
		if (queryHandle == null) {
			return HANDLE_NOT_ASSIGN.clone().addInfoToValue("module", module.getId());
		}

		HttpResult query = queryHandle.run(null, task, module, task.getProcess(), jsonData);

		JSONObject isShows = (JSONObject) task.getDefinition().getCanSeeColumn();
		JSONObject modifications = (JSONObject) task.getDefinition().getCanSeeColumn();
		JSONObject values = query.getData();

		@SuppressWarnings("unchecked")
		List<JSONObject> formContent = (List<JSONObject>) module.getFormStructure().get("content");

		String key;
		for (JSONObject item : formContent) {
			key = item.getString("id");

			if (isShows != null && isShows.containsKey(key)) {
				item.put("isShow", isShows.get(key));
			}
			if (values != null && values.containsKey(key)) {
				item.put("value", values.get(key));
			}
			if (modifications != null && modifications.containsKey(key)) {
				item.put("isModify", modifications.get(key));
			}
		}

		result.addInfoToValue("taskStructure", formContent);

		//增加操作对应的按钮
		Handle handle = HandleFactory.getHandle(task.get("handle_id"));
		result.addInfoToValue("Additional", handle.getAdditionalHandle());

		return result;
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
			return SUCCESS.clone().setListToData(LIST, tasks, "id", "execution_id", "name", "module_id", "handle_id");
		}

		return UNKNOWN;
	}


}
