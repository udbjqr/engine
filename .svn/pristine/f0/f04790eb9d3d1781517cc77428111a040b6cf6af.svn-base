package com.jg.workflow.process.definition;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.persistence.AbstractPersistenceFactory;
import com.jg.identification.User;

/**
 * 流程定义工厂类.
 *
 * @author yimin
 */

public class ProcessDefnitionFactory extends AbstractPersistenceFactory<ProcessDefinitionImpl> {
	public static final ProcessDefnitionFactory PROCESS_DEFNITION_FACTORY = new ProcessDefnitionFactory();

	private ProcessDefnitionFactory() {
		this.tableName = "process_definition";
		this.sequenceField = addField("id", Integer.class, "nextval('seq_process_definition')", true, true);
		sequenceField.setSerial("seq_process_definition");

		addField("company_id", Integer.class, null, true, false);
		addField("name", String.class, null, true, false);
		addField("version", Integer.class, null, true, false);
		addField("content", JSONObject.class, null, false, false);
		addField("view_infomation", String.class, null, false, false);
		addField("category", String.class, null, false, false);
		addField("description", String.class, null, false, false);
		addField("flag", Integer.class, 0, false, false);

		init();
	}

	public static ProcessDefnitionFactory getInstance() {
		return PROCESS_DEFNITION_FACTORY;
	}


	@Override
	protected ProcessDefinitionImpl createObject(User user) {
		return new ProcessDefinitionImpl(this);
	}
}
