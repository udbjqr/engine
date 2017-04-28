package com.jg.workflow.task;

import com.jg.workflow.process.definition.Link;

/**
 * create by 2017/4/6.
 *
 * @author yimin
 */

class ParallelGatewayPerformer extends AbstractPerformer {
	ParallelGatewayPerformer(TaskImpl task) {
		super(task);
	}

	@Override
	protected void turnInTask(Link link) {
		setProcessDetail();

		setAlreadyEnter(link);

		task.isImmediateOver = true;
	}
}
