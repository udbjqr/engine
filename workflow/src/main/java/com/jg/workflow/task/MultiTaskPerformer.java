package com.jg.workflow.task;

/**
 * create by 2017/4/6.
 *
 * @author yimin
 */

class MultiTaskPerformer extends AbstractPerformer {
	MultiTaskPerformer(TaskImpl task) {
		super(task);
	}

	//TODO 多用户任务需要根据设置调整是否可以结束，还是更换操作人员继续
	@Override
	protected void complete() {
	}
}
