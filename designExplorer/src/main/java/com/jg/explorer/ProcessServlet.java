package com.jg.explorer;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.result.HttpResult;
import com.jg.identification.Company;
import com.jg.identification.User;
import com.jg.workflow.Context;
import com.jg.workflow.exception.ProcessDefinitionNotFount;
import com.jg.workflow.process.Process;
import com.jg.workflow.process.ProcessDetail;
import com.jg.workflow.process.ProcessImpl;
import com.jg.workflow.process.ProcessManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import java.util.List;

import static com.jg.common.result.HttpResult.*;

/**
 * 流程控制servlet.
 *
 * @author yimin
 */

@WebServlet(name = "process", urlPatterns = "/process")
public class ProcessServlet extends BaseServlet {
	private static final Logger log = LogManager.getLogger(ProcessServlet.class.getName());

	@Override
	protected HttpResult execute(HttpRequestType type, ServletData servletData) {
		Company company = (Company) servletData.get(COMPANY);
		JSONObject jsonData = (JSONObject) servletData.get(JSON_DATA);
		ProcessManager processManager = Context.getProcessManager(company);
		User user = com.jg.identification.Context.getCurrentOperatorUser();

		switch (type) {
			case start:
				return startProcess(processManager, jsonData);
			case load:
				return loadProcessDetail(processManager, jsonData);
			case myfinishprocesslist:
				return getMyFinishProcessList(processManager, user);
			case mylaunchprocesslist:
				return getMyLaunchProcessList(processManager, user);
			default:
				log.error("未找到此类型定义的操作。type:" + type.name());
				return UNKNOWN;
		}
	}

	private HttpResult getMyLaunchProcessList(ProcessManager processManager, User user) {
		List<Process> processes = processManager.getMyInProcesses(user, "");
		if (processes == null) {
			return UNKNOWN;
		}

		return SUCCESS.clone().setListToData(RESULT_LIST, processes, "id", "name");
	}

	private HttpResult getMyFinishProcessList(ProcessManager processManager, User user) {
		List<Process> processes = processManager.getMyFinishedProcesses(user, "");
		if (processes == null) {
			return UNKNOWN;
		}

		return SUCCESS.clone().setListToData(RESULT_LIST, processes, "id", "name");
	}

	private HttpResult loadProcessDetail(ProcessManager processManager, JSONObject jsonData) {
		Integer processId = jsonData.getInteger("processId");
		if (processId == null) {
			return NO_SET_REQUEST_TYPE.clone().setMessage("未找到需要的参数：processId");
		}
		ProcessImpl process = (ProcessImpl) processManager.getProcessById(processId);
		List<ProcessDetail> list = process.getDetails();
		if (list != null) {
			HttpResult result = SUCCESS.clone().setListToData("detail", list, "id", "task_id", "employee_id", "flag");
			result.addInfoToValue("process", process, "id", "name", "sponsor", "flag");
			result.addInfoToValue("process_definition", process.getDefinition(), "id", "model_content", "category", "view_information");

			return result;
		}

		return UNKNOWN;
	}

	private HttpResult startProcess(ProcessManager processManager, JSONObject jsonData) {
		Integer definitionId = jsonData.getInteger("definitionId");
		if (definitionId == null) {
			return NO_SET_REQUEST_TYPE.clone().setMessage("未找到需要的参数：definitionId");
		}

		JSONObject variable = (JSONObject) jsonData.get("variable");
		if (variable == null) {
			return NO_SET_REQUEST_TYPE.clone().setMessage("未找到需要的参数：variable");
		}

		Process process;
		try {
			process = processManager.startProcess(definitionId, variable);
		} catch (ProcessDefinitionNotFount e) {
			return NO_SET_REQUEST_TYPE.clone().setMessage("指定的流程定义Id不匹配，id：" + definitionId);
		}

		if (process != null) {
			log.trace("成功生成流程：" + process);
			return SUCCESS.clone().put("processId", process.getId());
		}

		return UNKNOWN;
	}
}
