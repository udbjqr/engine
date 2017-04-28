package com.jg.workflow.process.handle;

import com.jg.common.persistence.AbstractPersistenceFactory;
import com.jg.identification.User;

public class ModuleLinkHandleFactory extends AbstractPersistenceFactory<ModuleLinkHandle> {
	private static final ModuleLinkHandleFactory instance = new ModuleLinkHandleFactory();

	private ModuleLinkHandleFactory() {
		this.tableName = "link_module_handle";

		addField("module_id", Integer.class, null, true, false);
		addField("handle_id", Integer.class, null, true, false);
		init();
	}

	public static ModuleLinkHandleFactory getInstance() {
		return instance;
	}

	@Override
	protected ModuleLinkHandle createObject(User user) {
		return new ModuleLinkHandle(this, user);
	}

}
