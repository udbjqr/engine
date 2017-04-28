package com.jg.workflow.process.handle.common;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.persistence.AbstractPersistenceFactory;
import com.jg.identification.User;

import java.util.Date;

public class FormDataFactory extends AbstractPersistenceFactory<FormData> {
	private static final FormDataFactory instance = new FormDataFactory();

	private FormDataFactory() {
		this.tableName = "form_data";
		this.sequenceField = addField("id", Integer.class, "nextval('seq_form_data')", true, true);
		sequenceField.setSerial("seq_form_data");

		addField("module_id", Integer.class, null, true, false);
		addField("form_data", JSONObject.class, null, true, false);
		addField("lock", Integer.class, 0, true, false);
		addField("create_time", Date.class, "now()", true, false);
		addField("create_user", Integer.class, null, false, false);
		addField("update_time", Date.class, null, true, false);
		addField("update_user", Integer.class, null, false, false);
		addField("flag", Integer.class, 0, false, false);

		init();
	}

	public static FormDataFactory getInstance() {
		return instance;
	}

	@Override
	protected FormData createObject(User user) {
		return new FormData(this, user);
	}

}
