package com.jg.common.objects;

import com.jg.common.persistence.AbstractPersistenceFactory;
import com.jg.identification.User;

/**
 * 数据字典的工厂类
 */
public final class DictFactory extends AbstractPersistenceFactory<Dict> {
	public static final DictFactory DICT_FACTORY = new DictFactory();

	private DictFactory() {
		this.tableName = "data_dictionary";
		this.sequenceField = addField("id", Integer.class, "nextval('seq_data_dictionary')", true, true);
		sequenceField.setSerial("seq_data_dictionary");

		addField("company_id", Integer.class, null, false, false);
		addField("dict_name", String.class, null, false, false);
		addField("label", String.class, null, false, false);
		addField("value", String.class, null, false, false);
		addField("flag", Integer.class, 1, true, false);

		init();
	}

	public static DictFactory getInstance() {
		return DICT_FACTORY;
	}

	@Override
	protected Dict createObject(User user) {
		return new Dict(this, user);
	}
}
