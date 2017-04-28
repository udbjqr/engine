package com.jg.workflow.event;

/**
 * 事件管理器接口.
 * <p>
 * 注册了的事件操作类会被事件处理器长期持有。需要注意的是事件处理操作需要维护线程的安全。
 * <p>
 * 同一个操作类的实例，多次注册将仅保留一次注册信息。同时一次移除将被移除掉。
 *
 * @author yimin
 */
public interface EventManager {
	/**
	 * 注册一个事件处理操作.
	 *
	 * @param eventType   对应的事件类型
	 * @param eventHandle 事件处理对象
	 */
	void registerEventHandle(ProcessEventType eventType, EventHandle eventHandle);

	/**
	 * 从事件处理器当中移除一个处理对象.
	 *
	 * @param eventType   事件类型
	 * @param eventHandle 需要移除的事件处理对象
	 */
	void removeEventHandle(ProcessEventType eventType, EventHandle eventHandle);
}
