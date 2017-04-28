package com.jg.workflow.task;

import com.jg.workflow.process.definition.Link;

/**
 * create by 2017/4/6.
 *
 * @author yimin
 */

public class EndPerformer extends AbstractPerformer {

	EndPerformer(TaskImpl task) {
		super(task);
	}


	protected void turnInTask(Link link) {
		setProcessDetail();
		task.isImmediateOver = true;
	}

	@Override
	protected void completeSelf() {
		super.completeSelf();
		process.finish(task);
	}
}
