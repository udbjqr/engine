package com.jg.workflow.event;

import com.jg.workflow.task.Task;
import com.jg.workflow.process.Process;
/**
 * create by 17/4/6.
 *
 * @author zhengyimin
 */

public class TaskTurnIn implements ProcessEvent {
	private final Task task;
	private final Process process;

	public TaskTurnIn(Task task) {
		this.task = task;
		this.process = task.getProcess();
	}

	@Override
	public ProcessEventType getType() {
		return ProcessEventType.TurnIn;
	}
}
