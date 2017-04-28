package com.jg.workflow.event;

import com.jg.workflow.task.Task;
import com.jg.workflow.task.TaskImpl;
import com.jg.workflow.process.Process;

/**
 * create by 2017/4/11.
 *
 * @author yimin
 */

public class TaskInsertAuditEvent implements ProcessEvent {
	private Process process;
	private Task task;
	private int sponsor;

	public TaskInsertAuditEvent(TaskImpl task, int sponsor) {
		this.sponsor = sponsor;
		this.task = task;
		this.process = task.getProcess();
	}


	public Process getProcess() {
		return process;
	}

	public Task getTask() {
		return task;
	}

	public int getSponsor() {
		return sponsor;
	}

	@Override
	public ProcessEventType getType() {
		return ProcessEventType.TaskInsertAudit;
	}
}
