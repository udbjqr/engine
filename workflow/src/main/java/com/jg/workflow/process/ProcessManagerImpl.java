package com.jg.workflow.process;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jg.common.util.StringUtil;
import com.jg.identification.Company;
import com.jg.identification.Context;
import com.jg.identification.User;
import com.jg.workflow.event.UserUrgeEvent;
import com.jg.workflow.exception.*;
import com.jg.workflow.process.definition.ProcessDefinition;
import com.jg.workflow.process.definition.ProcessDefinitionImpl;
import com.jg.workflow.process.model.Model;
import com.jg.workflow.process.model.ModelManagerImpl;
import com.jg.workflow.task.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jg.common.sql.DBHelperFactory.DB_HELPER;
import static com.jg.workflow.event.EventMangerImpl.EVENT_MANGER;
import static com.jg.workflow.process.ProcessFactory.PROCESS_FACTORY;
import static com.jg.workflow.process.definition.ProcessDefnitionFactory.PROCESS_DEFNITION_FACTORY;

/**
 * 流程管理器.
 *
 * @author yimin
 */

public class ProcessManagerImpl implements ProcessManager {
	private static final Logger log = LogManager.getLogger(ProcessManagerImpl.class.getName());
	private static final Map<Company, ProcessManagerImpl> MANAGERS = new HashMap<>();

	private final Company company;

	private ProcessManagerImpl(Company company) {
		this.company = company;

		init();
	}

	private void init() {
		if (company == null) {
			throw new CompanyIsNull();
		}
	}

	/**
	 * 获得企业对应的流程管理器
	 *
	 * @param company 企业对象
	 * @return 对应的模型管理器对象
	 */
	public synchronized static ProcessManagerImpl getInstance(Company company) {
		if (company == null || !company.isTop()) {
			log.warn("企业为空或者企业非顶级企业。");
			return null;
		}

		if (!MANAGERS.containsKey(company)) {
			MANAGERS.put(company, new ProcessManagerImpl(company));
		}

		return MANAGERS.get(company);
	}

	@Override
	public Company getCompany() {
		return company;
	}

	@Override
	public List<Process> getMyInProcesses(User user, String condition, int offset, int limit) {
		return new ArrayList<>(PROCESS_FACTORY.getObjectsForStringByPage(" where flag = 1 and sponsor = " + user.getId() + condition, limit, offset, Context.getCurrentOperatorUser()));
	}

	@Override
	public List<Process> getMyFinishedProcesses(User user, String condition, int offset, int limit) {
		return new ArrayList<>(PROCESS_FACTORY.getObjectsForStringByPage(" where flag = 10 and sponsor = " + user.getId() + condition, limit, offset, Context.getCurrentOperatorUser()));
	}

	@Override
	public List<ProcessDefinition> getAllProcessDefinitions(String condition, int companyId, int offset, int limit) {
		return new ArrayList<>(PROCESS_DEFNITION_FACTORY.getObjectsForStringByPage(" where flag in (1,2) and company_id = " + companyId + (StringUtil.isEmpty(condition) ? "" : " and ( 1=1 and " + condition + ")"), limit, offset, Context.getCurrentOperatorUser()));
	}

	@Override
	public ProcessDefinition deploymentProcess(int modelId) throws ModelHasbeenDeployed {
		@SuppressWarnings("ConstantConditions")
		Model model = ModelManagerImpl.getInstance(company).getModel(modelId);

		ProcessDefinitionImpl definition = PROCESS_DEFNITION_FACTORY.getNewObject(Context.getCurrentOperatorUser());

		definition.deploy(model, company.getId(), getContentFromGOJS(model.get("content"))).flush();

		return definition;
	}

	/**
	 * 将GoJS生成的json格式转换成流程必须的JSON格式。
	 *
	 * @param content GoJS生成的json格式
	 * @return 流程需要的Json格式
	 */
	private JSONObject getContentFromGOJS(JSONObject content) {
		JSONObject newC = new JSONObject();

		newC.put("name", content.get("name"));

		int i = 2001;
		JSONArray array = new JSONArray();
		for (Object object : ((JSONArray) content.get("linkDataArray"))) {
			JSONObject link = (JSONObject) object;
			JSONObject newLink = new JSONObject();

			newLink.put("id", i++);
			newLink.put("to", link.get("to"));
			newLink.put("from", link.get("from"));
			array.add(newLink);
		}
		newC.put("links", array);

		array = new JSONArray();
		for (Object object : ((JSONArray) content.get("nodeDataArray"))) {
			JSONObject data = (JSONObject) object;
			JSONObject newData = new JSONObject();

			newData.put("id", data.get("key"));
			newData.put("name", data.get("text"));
			newData.put("type", ((String) data.get("category")).toUpperCase());
			newData.put("handle", data.get("module"));
			newData.put("assignees", data.get("assignees"));
			newData.put("module", data.get("module"));
			array.add(newData);
		}
		newC.put("nodes", array);

		return newC;
	}


	@Override
	public void suspendProcessDefinition(int deploymentId) {
		checkProcessDefinitionExists(deploymentId);

		DB_HELPER.update("update process_definition set flag = 2 where id = " + deploymentId);
	}

	private void checkProcessDefinitionExists(int deploymentId) {
		int count = DB_HELPER.selectOneValues("select count(*)::int from process_definition where id = " + deploymentId);

		if (count <= 0) {
			throw new ProcessDefinitionNotFount("指定的Id未找到对应值.id:" + deploymentId);
		}
	}

	@Override
	public void activateProcessDefinition(int deploymentId) {
		checkProcessDefinitionExists(deploymentId);

		DB_HELPER.update("update process_definition set flag = 1 where id = " + deploymentId);
	}

	@Override
	public void removeProcessDefinition(int deploymentId) {
		checkProcessDefinitionExists(deploymentId);

		DB_HELPER.update("delete process_definition where id = " + deploymentId);
	}

	private void checkProcessExists(int processId) {
		int count = DB_HELPER.selectOneValues("select count(*) from process_data where id = " + processId);

		if (count <= 0) {
			throw new ProcessInstanceIsNULL("指定的Id未找到对应值.id:" + processId);
		}
	}

	@Override
	public void cancelProcess(int processId) {
		checkProcessExists(processId);

		DB_HELPER.update("update process_data set flag = 9 where id = " + processId);
	}


	@Override
	public void urgeProcessing(int processInstanceId) {
		checkProcessExists(processInstanceId);

		List<String> lists = new ArrayList<>();

		DB_HELPER.selectWithRow("SELECT operator_id from process_run_data where execution_id =" + DB_HELPER.getString(processInstanceId) + " and flag = 0", set -> lists.add(set.getString(1)));

		EVENT_MANGER.triggerEvent(new UserUrgeEvent(lists, processInstanceId));
	}

	@Override
	public void suspendProcess(int processId) {
		checkProcessExists(processId);

		DB_HELPER.update("update process_data set flag = 5 where id = " + processId);
	}

	@Override
	public void activateProcess(int processId) {
		checkProcessExists(processId);

		DB_HELPER.update("update process_data set flag = 1 where id = " + processId);
	}

	@Override
	public void startProcessByName(String processDefinitionName) {
		List<ProcessDefinitionImpl> defs;

		defs = PROCESS_DEFNITION_FACTORY.getObjectsForString(" where t.name = " + DB_HELPER.getString(processDefinitionName) + " order by version desc limit 1;", null);

		startProcess(defs.size() > 0 ? defs.get(0) : null, processDefinitionName);
	}

	@Override
	public Process getProcessByTaskId(int taskId) {
		return PROCESS_FACTORY.getProcessByTaskId(taskId);
	}

	private void startProcess(ProcessDefinitionImpl definition, String var) {
		if (definition == null) {
			throw new ProcessDefinitionNotFount("流程定义未找到" + var);
		}

		if (definition.get("flag").equals(2)) {
			throw new ProcessDefinitionIsSuspend("流程定义已经被挂起，无法启动流程.id:" + var);
		}

		ProcessImpl process = ProcessFactory.getInstance().createObject(Context.getCurrentOperatorUser());

		process.start(definition);
	}

	@Override
	public void startProcess(int processDefinitionId) {
		ProcessDefinitionImpl definition = PROCESS_DEFNITION_FACTORY.getObject("id", processDefinitionId);

		if (definition == null) {
			throw new ProcessDefinitionNotFount("流程定义未找到,流程Id：" + processDefinitionId);
		}

		startProcess(definition, definition.get("name"));
	}

	@Override
	public List<Task> getNeedSetOperatorsTask() {
		List<Task> tasks = new ArrayList<>();
		DB_HELPER.selectWithRow("select * from process_set_operator where flag = 0 and sponsor = " + Context.getCurrentOperatorUser().getId(), set -> {
			ProcessImpl process = PROCESS_FACTORY.getObject("id", set.getInt("execution_id"));
			tasks.add(process.getActivitiTaskById(set.getInt("task_id")));
		});

		return tasks;
	}
}






























