package com.jg.workflow.process.definition;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.persistence.AbstractPersistenceFactory;
import com.jg.identification.User;

/**
 * 流程定义工厂类.
 *
 * @author yimin
 */

public class ProcessDefinitionFactory extends AbstractPersistenceFactory<ProcessDefinitionImpl> {
	public static final ProcessDefinitionFactory PROCESS_DEFINITION_FACTORY = new ProcessDefinitionFactory();

	private ProcessDefinitionFactory() {
		this.tableName = "process_definition";
		this.sequenceField = addField("id", Integer.class, "nextval('seq_process_definition')", true, true);
		sequenceField.setSerial("seq_process_definition");

		addField("company_id", Integer.class, null, true, false);
		addField("name", String.class, null, true, false);
		addField("version", Integer.class, null, true, false);
		addField("content", JSONObject.class, null, false, false);
		addField("model_content", JSONObject.class, null, false, false);
		addField("view_information", String.class, null, false, false);
		addField("category", String.class, null, false, false);
		addField("description", String.class, null, false, false);
		addField("flag", Integer.class, 0, false, false);
		addField("model_id", Integer.class, null, false, false);

		init();
	}

	public static ProcessDefinitionFactory getInstance() {
		return PROCESS_DEFINITION_FACTORY;
	}


	@Override
	protected ProcessDefinitionImpl createObject(User user) {
		return new ProcessDefinitionImpl(this);
	}

	/**
	 * 得到流程定义最新对象.
	 * <p>
	 * 使用此方法必然得到流程定义最新的数据对象。
	 */
	@Override
	public synchronized ProcessDefinitionImpl getObject(String key, Object value) {
		Integer id_value = dbHelper.selectOneValues("select id from process_definition p where exists(select 1 from process_definition d where d." + key + " = " + dbHelper.getString(value) + " and p.name = d.name) order by version desc limit 1");

		return (id_value == null) ? null : super.getObject("id", id_value);
	}

	/**
	 * 得到流程定义绝对的对象.
	 * <p>
	 * 此方法仅使用Id做为查找标准。
	 * <p>
	 * 使用此方法得到指定流程对象的绝对对象。
	 */
	public synchronized ProcessDefinitionImpl getAbsolutelyObjectById(Object value) {
		return super.getObject("id", value);
	}
}
