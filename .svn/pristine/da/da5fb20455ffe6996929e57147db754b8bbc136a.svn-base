package com.jg.workflow.process.handle.common;


import com.alibaba.fastjson.JSONObject;
import com.jg.common.result.ResultCode;
import com.jg.identification.User;
import com.jg.workflow.process.Process;
import com.jg.workflow.process.handle.AbstractHandle;
import com.jg.workflow.process.handle.HandleType;
import com.jg.workflow.process.module.Module;
import com.jg.workflow.task.Task;

/**
 * 默认的审核操作.
 */
public class AuditClass extends AbstractHandle {

	public AuditClass() {
		super("reviewInput");
		this.type = HandleType.Audit;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	protected ResultCode execute(User user, Task task, Module module, Process processData, JSONObject jsonData) {
		FormData formData = FormDataFactory.getInstance().getNewObject(user);
		JSONObject formStructure = module.getFormStructure();
		JSONObject data = new JSONObject();

		//TODO 这边根据审核的类型去操作流程数据

	/*	for (Entry<String, Object> entry : formStructure.entrySet()) {
			data.put(entry.getKey(), jsonData.get(entry.getKey()));
		}

		try {
			formData.set("module_id", com.jg.module.get("id"));
			formData.set("form_data", data);
			if (user != null) {
				formData.set("create_user", user.get("id"));
			}
		} catch (WriteValueException e) {
			logger.error("写数据出现异常。", e);
			return ResultCode.ADD_ERROR;
		}

		formData.flush();*/
		return ResultCode.NORMAL;
	}
}
