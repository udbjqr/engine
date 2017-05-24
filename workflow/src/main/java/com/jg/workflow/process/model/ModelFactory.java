package com.jg.workflow.process.model;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.persistence.AbstractPersistenceFactory;
import com.jg.identification.User;

import java.util.Date;

/**
 * @author yimin
 */

public class ModelFactory extends AbstractPersistenceFactory<ModeImpl> {
	public static final ModelFactory MODEL_FACTORY = new ModelFactory();

	private ModelFactory() {
		this.tableName = "model";
		this.sequenceField = addField("id", Integer.class, "nextval('seq_model')", true, true);
		sequenceField.setSerial("seq_model");

		addField("name", String.class, null, true, false);
		addField("category", String.class, null, false, false);
		addField("create_time", Date.class, " now()", false, false);
		addField("update_time", Date.class, " now()", false, false);
		addField("content", JSONObject.class, null, false, false);
		addField("view_information", String.class, null, false, false);
		addField("company_id", Integer.class, null, true, false);
		addField("description", String.class, null, false, false);
		addField("flag", Integer.class, 0, false, false);
		addField("module_id", Integer.class, null, false, false);

		setIsCheck(false);
		init();
	}

	public static ModelFactory getModelFactory() {
		return MODEL_FACTORY;
	}

	@Override
	protected ModeImpl createObject(User user) {
		return new ModeImpl(this, user);
	}
}
