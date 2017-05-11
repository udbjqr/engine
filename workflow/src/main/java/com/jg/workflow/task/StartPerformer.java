package com.jg.workflow.task;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.result.HttpResult;
import com.jg.identification.Context;
import com.jg.workflow.process.definition.Link;
import com.jg.workflow.process.definition.TaskDefinition;
import com.jg.workflow.process.handle.Handle;
import com.jg.workflow.process.module.Module;

/**
 * create by 2017/4/6.
 *
 * @author yimin
 */

public class StartPerformer extends AbstractPerformer {

	StartPerformer(TaskImpl task) {
		super(task);
	}

	@Override
	protected void turnInTask(Link link) {
		setProcessDetail();

		task.isImmediateOver = true;
	}

	@Override
	protected synchronized HttpResult execute(JSONObject variables) {
		TaskDefinition definition = task.getDefinition();
		Module module = definition.getModule();
		Handle handle = module.getAddHandle();

		HttpResult httpResult = handle.run(Context.getCurrentOperatorUser(), task, module, process, variables);

		processDetail.set("result_data", ((JSONObject) process.get("variable")).clone());
		process.saveDetail(processDetail);

		return httpResult;
	}
}























