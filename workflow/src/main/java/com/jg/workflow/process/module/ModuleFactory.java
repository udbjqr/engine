package com.jg.workflow.process.module;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.persistence.AbstractPersistenceFactory;
import com.jg.identification.User;

import java.util.Date;

/**
 */
public class ModuleFactory extends AbstractPersistenceFactory<ModuleImpl> {
	public static final ModuleFactory MODULE_FACTORY = new ModuleFactory();

	private ModuleFactory() {
		this.tableName = "module";
		this.sequenceField = addField("id", Integer.class, "nextval('seq_module')", true, true);
		sequenceField.setSerial("seq_module");

		addField("module_name", String.class, null, true, false);
		addField("company_id", Integer.class, null, false, false);
		addField("remark", String.class, null, false, false);
		addField("flag", Integer.class, 0, true, false);
		addField("create_time", Date.class, null, false, false);
		addField("create_user", Integer.class, null, false, false);
		addField("update_time", Date.class, null, false, false);
		addField("update_user", Integer.class, null, false, false);
		addField("form_structure", JSONObject.class, null, false, false);
		init();
	}

	public static ModuleFactory getModuleFactory() {
		return MODULE_FACTORY;
	}

	@Override
	protected ModuleImpl createObject(User user) {
		return new ModuleImpl(this);
	}
}