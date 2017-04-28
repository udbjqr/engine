package com.jg.workflow.event;

import com.alibaba.fastjson.JSONObject;
import com.jg.workflow.process.Process;
import com.jg.workflow.task.Task;

/**
 * create by 2017/4/6.
 *
 * @author yimin
 */

public class TaskBeforeRun implements ProcessEvent {
	private Task task;
	private Process process;
	private JSONObject variables;

	public TaskBeforeRun(Task task, JSONObject variables) {
		this.task = task;
		this.process = task.getProcess();
		this.variables = variables;
	}

	public Task getTask() {
		return task;
	}

	public Process getProcess() {
		return process;
	}

	public JSONObject getVariables() {
		return variables;
	}

	@Override
	public ProcessEventType getType() {
		return ProcessEventType.TaskBeforeRun;
	}
}
