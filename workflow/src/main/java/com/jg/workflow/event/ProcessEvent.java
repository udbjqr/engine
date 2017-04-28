package com.jg.workflow.event;

/**
 * 流程事件接口.
 *
 * @author yimin
 */
public interface ProcessEvent {
	/**
	 * 得到事件的类型
	 * @return 事件类型
	 */
	ProcessEventType getType();
}
