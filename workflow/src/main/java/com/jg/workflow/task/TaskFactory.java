package com.jg.workflow.task;

import com.jg.common.persistence.AbstractPersistenceFactory;
import com.jg.identification.User;

import java.util.Date;

/**
 * 任务对象工厂类.
 *
 * @author yimin
 */

public final class TaskFactory extends AbstractPersistenceFactory<TaskImpl> {
	public static final TaskFactory TASK_FACTORY = new TaskFactory();

	private TaskFactory() {
		this.tableName = "task";
		this.sequenceField = addField("id", Integer.class, "nextval('seq_task')", true, true);
		sequenceField.setSerial("seq_task");

		addField("execution_id", Integer.class, null, false, false);
		addField("defition_id", Integer.class, null, false, false);
		addField("create_time", Date.class, "now()", false, false);
		addField("due_time", Date.class, "now()", false, false);
		addField("module_id", Integer.class, null, false, false);
		addField("handle_id", Integer.class, null, false, false);
		addField("flag", Integer.class, null, false, false);
		addField("can_view", String.class, null, false, false);
		addField("can_modify", String.class, null, false, false);
		addField("already_enter", String.class, null, false, false);
		addField("name", String.class, null, false, false, true);

		setIsCheck(false);
		init();
	}

	public static TaskFactory getInstance() {
		return TASK_FACTORY;
	}

	@Override
	protected TaskImpl createObject(User user) {
		return new TaskImpl(this, user);
	}

	@Override
	protected String setSelectStr() {
		selectStr = " select * from (select t.*,getTaskDefition(d.definition_id,t.defition_id)->>'name' as name from task t inner join process_data d on t.execution_id = d.id) t ";

		return selectStr;
	}

}
