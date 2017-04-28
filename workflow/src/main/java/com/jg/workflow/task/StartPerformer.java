package com.jg.workflow.task;

import com.jg.workflow.process.definition.Link;

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
}
