package com.jg.workflow.event;

import com.jg.identification.Company;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 事件管理类.
 *
 * @author yimin
 */

public class EventMangerImpl implements EventManager {
	private static final Logger log = LogManager.getLogger(EventMangerImpl.class.getName());
	private static final Map<Company, EventMangerImpl> mangerMap = new HashMap<>();
	public static final EventMangerImpl EVENT_MANGER = new EventMangerImpl();


	private final Map<ProcessEventType, List<EventHandle>> eventMap = new HashMap<>();

	private EventMangerImpl() {

	}

	/**
	 * 返回企业对应的事件操作对象.
	 *
	 * @return 事件对象
	 */
	public static synchronized EventMangerImpl getInstance() {
		return EVENT_MANGER;
	}

	public synchronized void registerEventHandle(ProcessEventType eventType, EventHandle eventHandle) {
		if (!eventMap.containsKey(eventType)) {
			eventMap.put(eventType, new ArrayList<>());
		}

		log.trace("增加事件{}的处理操作:{}", eventType, eventHandle);

		List<EventHandle> handles = eventMap.get(eventType);
		if (!handles.contains(eventHandle)) {
			handles.add(eventHandle);
		}
	}


	/**
	 * 当一个事件发生时，触发此方法执行.
	 *
	 * @param event 触发的事件
	 */
	public void triggerEvent(ProcessEvent event) {
		ProcessEventType type = event.getType();

		log.debug("触发事件:" + type);
		if (eventMap.containsKey(type)) {
			eventMap.get(type).forEach(eventHandle -> {
				log.trace("执行触发流程操作:" + eventHandle);
				eventHandle.onEvent(event);
			});
		}
	}


	public synchronized void removeEventHandle(ProcessEventType eventType, EventHandle eventHandle) {
		if (eventMap.containsKey(eventType)) {
			eventMap.get(eventType).remove(eventHandle);
			log.debug("为事件:{} 移除操作:{}", eventType, eventHandle);
		}
	}
}
