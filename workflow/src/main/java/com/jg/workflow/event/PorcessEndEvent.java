package com.jg.workflow.event;

import com.jg.workflow.process.Process;
import com.jg.workflow.task.Task;

/**
 * 流程结束事件.
 *
 * @author yimin
 */

public class PorcessEndEvent implements ProcessEvent {
	private final Process process;
	private final Task task;

	public Task getTask() {
		return task;
	}

	public PorcessEndEvent(Process process, Task task) {
		this.task = task;
		this.process = process;
	}

	public Process getProcess() {
		return process;
	}

	@Override
	public ProcessEventType getType() {
		return ProcessEventType.ProcessEnd;
	}
}
