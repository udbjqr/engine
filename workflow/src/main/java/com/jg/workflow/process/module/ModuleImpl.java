package com.jg.workflow.process.module;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.persistence.AbstractPersistence;
import com.jg.common.persistence.AbstractPersistenceFactory;
import com.jg.common.persistence.Field;
import com.jg.common.persistence.WriteValueException;
import com.jg.common.sql.DBHelperFactory;
import com.jg.workflow.process.handle.Handle;
import com.jg.workflow.process.handle.HandleFactory;
import com.jg.workflow.process.handle.HandleType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class ModuleImpl extends AbstractPersistence implements Module {
	private Handle[] handles;
	private List<JSONObject> formStructures;

	ModuleImpl(AbstractPersistenceFactory factory) {
		super(factory);
	}

	@Override
	public synchronized void set(Field field, Object value) throws WriteValueException {
		super.set(field, value);

		if (field.name.equals("id")) {
			refreshHandles();
		} else if (field.name.equals("form_structure")) {
			refreshFormStructure();
		}
	}

	private void refreshFormStructure() {
		Object o = get("form_structure");
		if (o != null && o instanceof JSONObject) {
			//noinspection unchecked
			formStructures = (List<JSONObject>) ((JSONObject) o).get("content");
		}
	}


	public synchronized void refreshHandles() {
		try {
			int id = get("id");
			int count = DBHelperFactory.getDBHelper().selectOneValues("select count(*)::int from link_module_handle where module_id =" + id);
			if (count > 0) {
				ResultSet set = DBHelperFactory.getDBHelper().select(
					"select * from link_module_handle where module_id =" + id);
				handles = new Handle[count];
				for (int i = 0; set.next(); i++) {
					handles[i] = HandleFactory.getHandle(set.getInt("handle_id"));
				}
			} else {
				logger.error("功能模块{}未指定操作。", id);
			}
		} catch (SQLException e) {
			logger.error("读取功能模块与操作对应关系出现异常：", e);
		}
	}

	public int getId() {
		if (get("id") == null) {
			setIdBySequence();
		}

		return get("id");
	}

	@Override
	public boolean containHandle(Handle handle) {
		for (Handle h : handles) {
			if (h.equals(handle)) {
				return true;
			}
		}

		return false;
	}

	public JSONObject getFormStructure() {
		return get("form_structure");
	}

	public void steam(FormStructureNameFunction function) {
		formStructures.forEach(function::setDataByField);
	}


	public Handle getAddHandle() {
		return getHandle(HandleType.Add);
	}

	public Handle getQueryHandle() {
		return getHandle(HandleType.List);
	}

	private Handle getHandle(HandleType type) {
		if (handles == null) {
			return null;
		}

		for (Handle handle : handles) {
			if (handle.getType().equals(type)) {
				return handle;
			}
		}
		return null;
	}
}
