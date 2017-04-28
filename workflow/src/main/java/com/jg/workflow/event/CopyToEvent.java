package com.jg.workflow.event;

import com.jg.workflow.task.Task;

import java.util.List;

/**
 * 抄送事件.
 *
 * @author yimin
 */

public class CopyToEvent implements ProcessEvent {
	private final Task task;
	private final int userId;
	private final List<Integer> copyToUsers;

	public CopyToEvent(Task task, int userId, List<Integer> copyToUsers) {
		this.task = task;
		this.userId = userId;
		this.copyToUsers = copyToUsers;
	}

	/**
	 * 抄送至人Id列表
	 *
	 * @return 抄送人Id列表
	 */
	public List<Integer> getCopyToUsers() {
		return copyToUsers;
	}

	/**
	 * 发送消息的用户任务.
	 *
	 * @return 任务id
	 */
	public Task getTask() {
		return task;
	}

	/**
	 * 发送消息的人.
	 *
	 * @return 发送消息的用户Id
	 */
	public int getUserId() {
		return userId;
	}

	@Override
	public ProcessEventType getType() {
		return ProcessEventType.CopyTo;
	}
}
