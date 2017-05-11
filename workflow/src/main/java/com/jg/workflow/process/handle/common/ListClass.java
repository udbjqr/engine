package com.jg.workflow.process.handle.common;


import com.alibaba.fastjson.JSONObject;
import com.jg.common.result.HttpResult;
import com.jg.identification.User;
import com.jg.workflow.process.Process;
import com.jg.workflow.process.handle.AbstractHandle;
import com.jg.workflow.process.handle.HandleType;
import com.jg.workflow.process.module.Module;
import com.jg.workflow.task.Task;

import static com.jg.workflow.process.handle.common.FormDataFactory.FORM_DATA_FACTORY;


/**
 * 通用的查询操作方式
 */
public class ListClass extends AbstractHandle {

	public ListClass() {
		super("reviewInput");
		this.type = HandleType.List;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public HttpResult run(User user, Task task, Module module, Process process, JSONObject jsonData) {
		JSONObject resultJson = null;
		//表单数据
		FormData formData;
		//表单ID
		Integer formData_Id;

		//查询有没有已存在的表单数据
		JSONObject dataFromProcess = process.get("data");
		//找到表单数据ID
		if (null != dataFromProcess) {
			formData_Id = process.getModuleInstanceId(module.getId());

			if (null != formData_Id) {
				formData = FORM_DATA_FACTORY.getObject("id", formData_Id);

				if (null != formData) {
					resultJson = formData.get("form_data");
				}
			}
		}

		return new HttpResult(0, "", true, resultJson);
	}

	@Override
	public Object getFormValue(String name, int formId) {
		FormData data = FORM_DATA_FACTORY.getObject("id", formId);
		if (data != null) {
			return ((JSONObject) data.get("form_data")).get(name);
		}

		return null;
	}
}
