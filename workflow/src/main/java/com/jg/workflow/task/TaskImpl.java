package com.jg.workflow.task;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.persistence.AbstractPersistence;
import com.jg.common.persistence.AbstractPersistenceFactory;
import com.jg.common.persistence.Field;
import com.jg.common.persistence.WriteValueException;
import com.jg.common.result.HttpResult;
import com.jg.common.util.DateUtil;
import com.jg.identification.User;
import com.jg.workflow.process.Process;
import com.jg.workflow.process.definition.Link;
import com.jg.workflow.process.definition.TaskDefinition;

import java.util.Date;
import java.util.Set;

/**
 * 任务对象的实现类.
 *
 * @author yimin
 */

public class TaskImpl extends AbstractPersistence implements Task {
//	private static final Logger log = LogManager.getLogger(TaskImpl.class.getName());
	private TaskDefinition taskDefinition;
	private Integer id;
	private Process process;
	private AbstractPerformer performer;
	boolean isImmediateOver = false;

	TaskImpl(AbstractPersistenceFactory factory, User user) {
		super(factory);
	}

	/**
	 * 任务的初始化动作。
	 */
	public TaskImpl init(Process process, TaskDefinition taskDefinition) {
		this.process = process;
		this.taskDefinition = taskDefinition;

		if(isNewCreate) {
			this.setIdBySequence();
			set("execution_id", process.getId());
			set("definition_id", taskDefinition.getId());
			set("create_time", new Date(System.currentTimeMillis()));
			set("due_time", DateUtil.toDateNowAddInterval(taskDefinition.getDurationTime()));
			if (taskDefinition.getModule() != null) {
				set("module_id", taskDefinition.getModule().getId());
			}
			if (taskDefinition.getHandle() != null) {
				set("handle_id", taskDefinition.getHandle().getId());
			}
			set("flag", 0);
			set("can_view", taskDefinition.getCanSeeColumn());
			set("can_modify", taskDefinition.getCanModifyColumn());
		}

		this.performer = AbstractPerformer.getPerformer(this);
		return this;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public String getName() {
		return get("name");
	}

	@Override
	public Set<String> getCanSeeColumn() {
		return taskDefinition.getCanSeeColumn();
	}

	@Override
	public Set<String> getCanModifyColumn() {
		return taskDefinition.getCanModifyColumn();
	}

	@Override
	public Process getProcess() {
		return process;
	}

	@Override
	public HttpResult run(JSONObject variables) {
		return performer.run(variables);
	}

	@Override
	public void complete() {
		performer.complete();
	}

	public void turnIn(Link link) {
		//设置任务为激活状态
		set("flag", 0);
		flush();

		performer.turnIn(link);
	}

	@Override
	public synchronized void set(Field field, Object value) throws WriteValueException {
		if (field.name.equals("id")) {
			this.id = (Integer) value;
		}

		super.set(field, value);
	}

	public TaskDefinition getDefinition() {
		return taskDefinition;
	}

	/**
	 * 是否立即执行此任务的结束操作.
	 *
	 * @return true 是
	 */
	public boolean isImmediateOver() {
		return isImmediateOver;
	}

	public boolean isNomal() {
		Integer flag = get("flag");
		return flag == 0 || flag == 2 || flag == 3;
	}

	public boolean isOver() {
		return get("flag").equals(10);
	}

	public void setOperators(String operators) {
		performer.setOperators(operators);
	}

	public boolean isInsertAudit() {
		return get("flag").equals(2);
	}
}