package com.jg.workflow.task;

import com.jg.workflow.process.definition.Link;
import com.jg.workflow.process.definition.TaskDefinition;

/**
 * create by 2017/4/6.
 *
 * @author yimin
 */

class SerialGatewayPerformer extends AbstractPerformer {
	SerialGatewayPerformer(TaskImpl task) {
		super(task);
	}

	@Override
	protected void turnInTask(Link link) {
		int count = ((String) task.get("already_enter")).split(",").length;

		if (count >= task.getDefinition().getFromLinks().size() - 1) {
			setProcessDetail();
			task.isImmediateOver = true;
		}

		setAlreadyEnter(link);
	}

	@Override
	protected void transfer() {
		TaskDefinition definition = task.getDefinition();
		for (Link link : definition.getToLinks()) {
			if (link.isThrough(process)) {
				process.activateTask(link.getTo(), link);
				return;
			}
		}
	}
}
