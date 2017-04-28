package com.jg.workflow.event;

import com.jg.workflow.process.Process;

/**
 * 流程启动事件.
 * <p>
 * 当一个流程被发起时,将抛出此事件.
 *
 * @author yimin
 */

public class ProcessStartEvent implements ProcessEvent {
	private final Process process;

	public ProcessStartEvent(Process process) {
		this.process = process;
	}

	public Process getProcess() {
		return process;
	}

	@Override
	public ProcessEventType getType() {
		return ProcessEventType.ProcessStart;
	}

}
