package com.jg.workflow.process;


import com.alibaba.fastjson.JSONObject;
import com.jg.common.persistence.AbstractPersistenceFactory;
import com.jg.identification.User;

import java.util.Date;

public final class ProcessDetailFactory extends AbstractPersistenceFactory<ProcessDetail> {
	private static final ProcessDetailFactory instance = new ProcessDetailFactory();

	private ProcessDetailFactory() {
		this.tableName = "process_detail_data";
		this.sequenceField = addField("id", Integer.class, "nextval('seq_process_data')", true, true);
		sequenceField.setSerial("seq_process_data");

		addField("execution_id", String.class, null, true, false);
		addField("task_id", String.class, null, true, false);
		addField("now_data", JSONObject.class, null, false, false);
		addField("result_data", JSONObject.class, null, false, false);
		addField("employee_id", String.class, null, false, false);
		addField("create_time", Date.class, "now()", true, false);
		addField("flag", Integer.class, "0", true, false);

		setIsCheck(false);
		init();
	}

	public static ProcessDetailFactory getInstance() {
		return instance;
	}

	@Override
	protected ProcessDetail createObject(User user) {
		return new ProcessDetail(this, user);
	}
}
