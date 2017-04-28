package com.jg.workflow.event;

import java.util.List;

/**
 * 用户驱办事件.
 *
 * @author yimin
 */

public class UserUrgeEvent implements ProcessEvent {
	private final List<String> users;
	private final int processInstanceId;

	public UserUrgeEvent(List<String> users, int processInstanceId) {
		this.users = users;
		this.processInstanceId = processInstanceId;
	}

	@Override
	public ProcessEventType getType() {
		return ProcessEventType.UserUrge;
	}

	public List<String> getUsers() {
		return users;
	}

	public int getProcessInstanceId() {
		return processInstanceId;
	}
}
