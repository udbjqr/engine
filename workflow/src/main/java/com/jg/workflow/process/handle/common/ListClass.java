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

	public ResultCode run(User user, Task task, Module module, Process processData, JSONObject jsonData) {
		JSONObject resultJson = new JSONObject();
		//表单数据
		FormData formData = null;
		//表单ID
		Integer formData_Id = null;
		//获得功能对应的空表单
		JSONObject formStructure = module.getFormStructure();
		resultJson.put("formStructure", formStructure);

		//查询有没有已存在的表单数据
		JSONObject dataFromProcess = processData.get("data");
		//找到表单数据ID
		if (null != dataFromProcess) {
			formData_Id = processData.getModuleInstanceId(module.getId());
		}
		if (null != formData_Id) {
			formData = FormDataFactory.getInstance().getObject("id", formData_Id);
		}
		if (null != formData) {
			JSONObject formDataJson = formData.get("form_data");
			resultJson.put("formData", formDataJson);
		}
		resultJson.put("task_id", task.getId());
		resultJson.put("processData", processData);
		resultJson.put("jsonData", jsonData);
		return new ResultCode(0, "", true, resultJson);
	}

	@Override
	public Object getFormValue(String name, int formId) {
		FormData data = FormDataFactory.getInstance().getObject("id", formId);
		if (data != null) {
			return ((JSONObject) data.get("form_data")).get(name);
		}

		return null;
	}
}
