package com.jg.workflow.process;


import com.alibaba.fastjson.JSONObject;
import com.jg.common.persistence.AbstractPersistenceFactory;
import com.jg.identification.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public final class ProcessFactory extends AbstractPersistenceFactory<ProcessImpl> {
	private static final Logger log = LogManager.getLogger(ProcessFactory.class.getName());
	public static final ProcessFactory PROCESS_FACTORY = new ProcessFactory();

	private ProcessFactory() {
		this.tableName = "process_data";

		addField("company_id", Integer.class, null, false, true);
		addField("definition_id", Integer.class, null, false, true);
		addField("name", String.class, null, false, true);
		addField("sponsor", Integer.class, null, false, false);
		addField("variable", JSONObject.class, null, false, false);
		addField("data", JSONObject.class, null, false, false);
		addField("flag", Integer.class, 0, false, false);
		addField("create_time", Date.class, "now()", true, false);
		addField("last_operat", Integer.class, null, false, false);

		this.sequenceField = addField("id", Integer.class, "nextval('seq_process_data')", true, true);
		sequenceField.setSerial("seq_process_data");

		setIsCheck(false);
		init();
	}

	public static ProcessFactory getInstance() {
		return PROCESS_FACTORY;
	}

	@Override
	protected ProcessImpl createObject(User user) {
		return new ProcessImpl(this);
	}

	public ProcessImpl getProcessByTaskId(int taskId) {
		ProcessImpl process = createObject(null);

		if (readFromDB(process, "select * from " + tableName + " where id = (select execution_id from task where id = " + taskId + ")")) {
			process.init();

			return addObject(process);
		} else {
			log.warn("没有找到指定值的对象.taskid:" + taskId);
			return null;
		}
	}
}
