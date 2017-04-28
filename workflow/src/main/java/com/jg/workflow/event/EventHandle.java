package com.jg.workflow.event;

/**
 * 事件操作对象接口.
 * <p>
 * 一个流程在同一时间只会执行一个事件处理，此接口的实现类需要保证自身在同时处理多个流程的事件时的线程安全。
 * 同一流程的事件由流程控制器保证是串行执行。
 *
 * @author yimin
 */
public interface EventHandle {
	/**
	 * 当事件触发时被调用。
	 *
	 * @param event 触发的事件
	 */
	void onEvent(ProcessEvent event);
}
