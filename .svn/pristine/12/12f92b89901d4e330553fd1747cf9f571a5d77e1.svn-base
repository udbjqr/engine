package com.jg.workflow.event;

import com.jg.workflow.task.Task;
import com.jg.workflow.process.Process;

/**
 * 任务完成事件.
 *
 * @author yimin
 */

public class TaskCompleteEvent implements ProcessEvent {
	private Task task;
	private Process process;

	public TaskCompleteEvent(Task task) {
		this.task = task;
		this.process = task.getProcess();
	}

	@Override
	public ProcessEventType getType() {
		return ProcessEventType.TaskComplete;
	}
}
