package com.jg.common.util;


import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public final class MapUtil {
	/**
	 * 从{@link JSONObject}类得到指定名称的对象，并且转换成{@link Map}对象.
	 * <p>
	 * 此方法始终返回一个{@link Map}对象。即返回值不是会一个NULL值。
	 * <p>
	 * 如name指向的是不是一个map,则返回一个key是name参数的值。
	 * <p>
	 * 如name指向的是一个{@link Map}对象，将此{@link Map}对象分解后返回。
	 *
	 * @param object 一个{@link JSONObject}类的对象。
	 * @param name   需要转换成{@link Map}对象的名称。
	 * @return 始终指定名称的{@link Map}对象。
	 */
	public static Map<String, Object> getParameter(JSONObject object, String name) {
		Map<String, Object> map = new HashMap<>();

		Object obj = object.get(name);

		if (obj instanceof JSONObject) {
			for (String string : ((JSONObject) obj).keySet()) {
				map.put(string,((JSONObject) obj).get(string));
			}
		} else {
			map.put(name, obj);
		}

		return map;
	}
}
