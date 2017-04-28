package com.jg.workflow.event;

import com.jg.workflow.process.Process;
import com.jg.workflow.task.Task;

/**
 * create by 2017/4/6.
 *
 * @author yimin
 */

public class TaskEndRun implements ProcessEvent {
	private Task task;
	private Process process;

	public TaskEndRun(Task task) {
		this.task = task;
		this.process = task.getProcess();
	}

	@Override
	public ProcessEventType getType() {
		return ProcessEventType.TaskAfterRun;
	}
}
