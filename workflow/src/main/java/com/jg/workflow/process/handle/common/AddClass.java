package com.jg.workflow.process.handle.common;


import com.alibaba.fastjson.JSONObject;
import com.jg.common.persistence.WriteValueException;
import com.jg.common.result.HttpResult;
import com.jg.identification.User;
import com.jg.workflow.process.Process;
import com.jg.workflow.process.handle.AbstractHandle;
import com.jg.workflow.process.handle.HandleType;
import com.jg.workflow.process.module.Module;
import com.jg.workflow.task.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

/**
 * 默认的新增或修改操作.
 */
public class AddClass extends AbstractHandle {
	private static final FormDataFactory formDataFactory = FormDataFactory.getInstance();
	protected static Logger logger = LogManager.getLogger(AddClass.class.getName());

	public AddClass() {
		super(null);
		this.type = HandleType.Add;
	}

	@Override
	protected HttpResult execute(User user, Task task, Module module, Process process, JSONObject jsonData) {
		//表单数据
		FormData form = null;

		//找到表单数据ID
		Integer formData_Id = process.getModuleInstanceId(module.getId());

		if (null != formData_Id) {
			form = formDataFactory.getObject("id", formData_Id);
		}

		JSONObject formData = new JSONObject();

		module.steam(object -> {
			String id = (String) object.get("id");
			formData.put(id, jsonData.get(id));
		});

		try {
			if (null != form) {//若为修改
				form.set("update_user", user.get("id"));
				form.set("update_time", new Date(System.currentTimeMillis()));
			} else {//新增
				form = FormDataFactory.getInstance().getNewObject(user);
				if (user != null) {
					form.set("create_user", user.getId());
				}

				form.setIdBySequence();
				//虽否被锁，0：未被锁，1：被锁
				form.set("lock", 0);
				form.set("flag", 1);
				form.set("create_time", new Date(System.currentTimeMillis()));
				form.set("module_id", module.getId());

				//把表单id更新进流程数据
				process.setModuleValueId(module.getId(), form.get("id"));
			}
			form.set("form_data", formData);
		} catch (WriteValueException e) {
			logger.error("写数据出现异常。", e);
		}

		//noinspection ConstantConditions
		form.flush();

		return HttpResult.NORMAL;
	}

}
