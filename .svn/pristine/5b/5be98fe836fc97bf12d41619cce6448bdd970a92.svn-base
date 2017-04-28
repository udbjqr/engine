package com.jg.workflow.process.handle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.TreeMap;

/**
 * 操作的工厂类.
 */
public final class HandleFactory {
	private static final Logger logger = LogManager.getLogger(AbstractHandle.class.getPackage().getName());
	private static final Map<Integer, Handle> handles = new TreeMap<>();
	private static final HandleDataFactory HANDLE_DATA_FACTORY;

	static {
		HANDLE_DATA_FACTORY = new HandleDataFactory();
		logger.info("开始实例化操作对象");

		Handle handle;
		for (HandleData data : HANDLE_DATA_FACTORY.getAllObjects(null)) {
			handle = createHandle(data);
			if (handle == null) {
				continue;
			}

			data.get("class_name");
			handles.put(data.get("id"), handle);
		}
	}

	private HandleFactory() {
	}


	private static Handle createHandle(HandleData data) {
		String className = data.get("class_name");
		if (className == null || className.equals("")) {
			logger.error("操作" + data.get("id") + "的class_name数据为空,跳过此操作");
			return null;
		}

		try {
			return ((AbstractHandle) Class.forName(className).newInstance()).initValue(data);
		} catch (ClassNotFoundException e) {
			logger.error("未找到对应的类，请检查。", e);
		} catch (IllegalAccessException | InstantiationException e) {
			logger.error("出现异常。", e);
		}
		return null;
	}

	public static Handle getHandle(final Integer id) {
		return (id == null) ? null : handles.get(id);
	}


	public static void addHandle(HandleData data) {
		Handle handle = createHandle(data);

		if (handle != null) {
			handles.put(handle.getId(), handle);
		}
	}
}

