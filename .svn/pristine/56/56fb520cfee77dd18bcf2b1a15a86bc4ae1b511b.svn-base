package com.jg.workflow.process;


import com.alibaba.fastjson.JSONObject;
import com.jg.common.InternalConstant;
import com.jg.common.persistence.AbstractPersistence;
import com.jg.common.persistence.Field;
import com.jg.common.persistence.WriteValueException;
import com.jg.identification.Company;
import com.jg.identification.Context;
import com.jg.identification.User;
import com.jg.workflow.event.PorcessEndEvent;
import com.jg.workflow.event.ProcessStartEvent;
import com.jg.workflow.exception.ImpossibleJumpToTask;
import com.jg.workflow.exception.RunScriptException;
import com.jg.workflow.expression.ExpressionControl;
import com.jg.workflow.process.definition.*;
import com.jg.workflow.task.Task;
import com.jg.workflow.task.TaskImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.script.ScriptException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.jg.common.sql.DBHelperFactory.DB_HELPER;
import static com.jg.workflow.event.EventMangerImpl.EVENT_MANGER;
import static com.jg.workflow.expression.ExpressionControl.SCRIPT_ENGINE;
import static com.jg.workflow.task.TaskFactory.TASK_FACTORY;

/**
 * 流程实例类.
 * create by 17-3-29.
 *
 * @author yimin
 */

public class ProcessImpl extends AbstractPersistence implements Process {
	private final static Logger log = LogManager.getLogger(ProcessImpl.class.getName());
	private final static ProcessDetailFactory DETAIL_FACTORY = ProcessDetailFactory.getInstance();

	private int id;
	private List<User> attentionUsers = new ArrayList<>();
	private List<ProcessDetail> details;
	private ProcessDefinitionImpl definition;
	//流程当中当前活跃的任务
	private List<TaskImpl> activityTasks = new ArrayList<>();
	private boolean isStart = false;
	private ExpressionControl expressionControl;

	ProcessImpl(ProcessFactory factory) {
		super(factory);
	}

	@Override
	public synchronized void set(Field field, Object value) throws WriteValueException {
		if (field.name.equals("id")) {
			this.id = (int) value;

			details = DETAIL_FACTORY.getMultipleObjects("execution_id", value, null);
			createAttentionUsers();
			createActivityTasks();

		} else if (field.name.equals("definition_id")) {
			this.definition = ProcessDefnitionFactory.getInstance().getObject("id", value);
		}

		super.set(field, value);
	}

	private void createActivityTasks() {
		this.activityTasks = TASK_FACTORY.getObjectsForString(" where t.flag <> 10 and t.execution_id = " + id, Context.getCurrentOperatorUser());

		for (TaskImpl task : this.activityTasks) {
			task.init(this, definition.getTaskDefinition(task.get("defition_id")));
		}
	}

	private void createAttentionUsers() {
		DB_HELPER.selectWithRow("select user_id from process_attention_users where execution_id = " + DB_HELPER.getString(getId()), set ->
			attentionUsers.add(com.jg.identification.Context.getUserById(set.getInt(1)))
		);
	}

	public ExpressionControl getExpressionControl() {
		if (expressionControl == null) {
			expressionControl = new ExpressionControl(this);
		}

		return expressionControl;
	}

	@Override
	public String getStartUser() {
		return get("sponsor");
	}

	@Override
	public List<User> getAttentionUsers() {
		return attentionUsers;
	}

	@Override
	public void addAttentionUsers(User user) {
		attentionUsers.add(user);
		DB_HELPER.update(String.format("INSERT INTO process_attention_users (execution_id, user_id) VALUES (%d,%d);", id, user.getId()));
	}

	@Override
	public void removeAttentionUsers(User user) {
		attentionUsers.remove(user);
		DB_HELPER.update(String.format("DELETE FROM process_attention_users where execution_id = %s and user_id = %d;", DB_HELPER.getString(get("execution_id").toString()), user.getId()));
	}

	@Override
	public String getSVGXML() {
		//TODO 将图形化的流程定义图以SVG形式返回.
		return null;
	}


	@Override
	public ProcessDefinition getDefinition() {
		return definition;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public boolean isSuspended() {
		return (Integer) get("flag") == 5;
	}

	@Override
	public boolean isEnded() {
		return (Integer) get("flag") == 10;
	}


	@Override
	public Map<String, Object> getProcessVariables() {
		return (JSONObject) get("variable");
	}

	@Override
	public String getName() {
		return get("name");
	}


	@Override
	public Company getCompany() {
		return Context.getCompanyById(get("company_id"));
	}

	@Override
	public void setVariable(String name, Object value) {
		JSONObject variables = get("variable");

		if (value == null) {
			variables.remove(name);
			flush();
		} else if (!variables.get(name).equals(value)) {
			variables.put(name, value);
			flush();
		}
	}


	@Override
	public Object getVariable(String name) {
		return ((JSONObject) get("variable")).get(name);
	}

	private ProcessDetail createDetail(Integer taskId) {
		ProcessDetail detail = DETAIL_FACTORY.getNewObject(null);

		detail.set("execution_id", id);
		detail.set("task_id", taskId);
		detail.set("flag", 0);

		if (details == null) {
			details = new ArrayList<>();
		}
		details.add(detail);

		return detail;
	}

	/**
	 * 流程开始工作.
	 * <p>
	 * 到这一步流程才将自身存储至持久层内.
	 */
	synchronized void start(ProcessDefinitionImpl definition) {
		if (isStart) {
			log.error("流程已经被启动过，无法再进行启动.");
			return;
		}

		this.definition = definition;

		//noinspection ConstantConditions
		set("sponsor", Context.getCurrentOperatorUser().getId());
		set("definition_id", definition.getId());
		set("name", definition.get("name"));
		set("company_id", definition.get("company_id"));
		set("variable", new JSONObject());
		set("data", new JSONObject());
		set("flag", 1);
		flush();

		setIdBySequence();

		EVENT_MANGER.triggerEvent(new ProcessStartEvent(this));
		isStart = true;

		TaskDefinition taskDefinition = definition.getStartNode();

		activateTask(taskDefinition, null);
	}

	/**
	 * 获得需要的任务对象.
	 *
	 * @return 任务对象
	 */
	private TaskImpl getTask(TaskDefinition taskDefinition) {
		int taskId = DB_HELPER.selectOneValues(String.format("select COALESCE(max(id),0) from task where execution_id = %d and defition_id = %d ", id, taskDefinition.getId()));

		if (taskId > 0) {
			return TASK_FACTORY.getObject("id", taskId).init(this, taskDefinition);
		} else {
			TaskImpl task = TASK_FACTORY.getNewObject(null);
			task.init(this, taskDefinition);
			task.flush();

			return task;
		}
	}

	public List<Task> getActivityTasks() {
		return new ArrayList<>(activityTasks);
	}

	@Override
	public void setModuleValueId(Integer moduleId, Integer id) {
		((JSONObject) get("data")).put(InternalConstant.MODULE_PREFIX + moduleId, id);

		flush();
	}

	@Override
	public Integer getModuleInstanceId(Integer moduleId) {
		return (Integer) ((JSONObject) get("data")).get(InternalConstant.MODULE_PREFIX + moduleId);
	}


	/**
	 * 流程操作当前任务的结束动作.
	 *
	 * @param task 结束的任务
	 */
	public synchronized void closeTask(TaskImpl task) {
		activityTasks.remove(task);
	}

	/**
	 * 流程完全结束动作.
	 * <p>
	 * 调用此方式仅在结束流程时被调用 。
	 *
	 * @param task 结束的任务
	 */
	public synchronized void finish(TaskImpl task) {
		set("flag", 10);
		flush();

		EVENT_MANGER.triggerEvent(new PorcessEndEvent(this, task));
		clearData();
	}

	/**
	 * 流程结束后清除所有任务
	 */
	private void clearData() {
		String[] sqls = new String[]{
			"delete from process_set_operator where execution_id = " + id,
			"delete from task where execution_id = " + id,
			"delete from process_insert_audit where execution_id = " + id,
			"delete from process_run_control where execution_id = " + id,
		};

		DB_HELPER.execBatchSql(sqls);
	}

	public void activateTask(TaskDefinition taskDefinition, Link link) {
		TaskImpl task = getTask(taskDefinition);

		activityTasks.add(task);

		task.turnIn(link);

		if (task.isImmediateOver()) {
			task.complete();
		}
	}

	/**
	 * 保存明细的数据
	 *
	 * @param detail 需要保存的明细数据
	 */
	public void saveDetail(ProcessDetail detail) {
		detail.flush();
	}

	/**
	 * 在本流程范围内,计算表达式值.
	 *
	 * @param condition 需要计算的表达式，目前使用js代码.
	 * @return 返回的值
	 */
	public Object calculateExpression(String condition) {
		synchronized (SCRIPT_ENGINE) {
			if (expressionControl == null) {
				expressionControl = new ExpressionControl(this);
			}

			try {
				SCRIPT_ENGINE.eval(condition, expressionControl);
			} catch (ScriptException e) {
				log.error("发生异常：", e);
				throw new RunScriptException(condition, e);
			}

			return expressionControl.getResult();
		}
	}

	/**
	 * 将流程跳至某一个任务节点.
	 *
	 * @param fromId             从哪一个节点开始跳，可为空
	 * @param toTaskDefinitionId 需要跳到的节点Id。必须非空，而且是本流程节点
	 * @param clearOperator      是否清空操作人,true 清空。
	 */
	public void jumpToTask(int fromId, int toTaskDefinitionId, boolean clearOperator) {
		jumpToTask(fromId, definition.getTaskDefinition(toTaskDefinitionId), clearOperator);
	}

	private void jumpToTask(int fromId, TaskDefinition toTaskDefinition, boolean clearOperator) {
		StringBuilder builder = new StringBuilder();
		String sql;
		List<String> sqls = new ArrayList<>();


		if (toTaskDefinition == null) {
			throw new ImpossibleJumpToTask("无法进行跳转，目标任务非本流程内节点或目标指定错误,ID:");
		}

		if (clearOperator) {
			for (Task task : activityTasks) {
				builder.append(builder.length() == 0 ? task.getId() : "," + task.getId());
			}
			sql = "delete from process_run_control where task_id in (" + builder.toString() + ");";
			sqls.add(sql);
		}

		sqls.add(" update task set flag = 10 where execution_id = " + id + ";");
		sqls.add(" update task set flag = 1 where task_id = " + toTaskDefinition.getId() + ";");
		sqls.add(" update process_insert_audit set flag = 0 where task_id = " + fromId + ";");

		activityTasks.clear();
		activateTask(toTaskDefinition, null);

		try {
			DB_HELPER.execBatchSql(sqls);
		} catch (SQLException e) {
			log.error("执行sql语句出现异常:", e);
		}
	}

	public TaskImpl getActivitiTaskById(int taskId) {
		for (TaskImpl task : activityTasks) {
			if (task.getId().equals(taskId)) {
				return task;
			}
		}
		return null;
	}

	/**
	 * 返回当前任务对应的明细数据.
	 *
	 * @param taskId 任务的Id
	 * @return 当前任务对应的明细数据对象
	 */
	public ProcessDetail getDetail(Integer taskId) {
		for (ProcessDetail detail : details) {
			if (detail.get("flag").equals(0) && detail.get("task_id").equals(taskId)) {
				return detail;
			}
		}

		return createDetail(taskId);
	}

	/**
	 * 返回是否被挂起.
	 *
	 * @return true 是被挂起
	 */
	public boolean isSupspend() {
		return get("flag").equals(5);
	}

	/**
	 * 返回流程是否正常进行当中.
	 *
	 * @return true 正常
	 */
	public boolean IsNormal() {
		return get("flag").equals(1);
	}


	/**
	 * 返回流程是否已经结束
	 *
	 * @return true 已经结束
	 */
	public boolean IsOver() {
		return get("flag").equals(10);
	}

	/**
	 * 返回流程是否被取消.
	 *
	 * @return true 已经被取消
	 */
	public boolean IsCancel() {
		return get("flag").equals(9);
	}
}




































