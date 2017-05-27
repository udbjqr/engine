package com.jg.common.result;


import com.alibaba.fastjson.JSONObject;
import com.jg.common.persistence.Persistence;
import com.jg.common.util.StringUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 返回结果
 */
public final class HttpResult {
	public static final HttpResult NORMAL = new HttpResult(0, "正常结束", true);
	public static final HttpResult ISNULL = new HttpResult(1, "返回结果为空");
	public static final HttpResult NO_PARAMETER = new HttpResult(2, "未指定参数，请检查");
	public static final HttpResult NO_SET_REQUEST_TYPE = new HttpResult(3, "未写请求参数，请检查");
	public static final HttpResult UNKNOWN = new HttpResult(-1, "未知错误");
	public static final HttpResult NOT_LOGIN = new HttpResult(4, "用户未登录");
	public static final HttpResult NOT_FOUND_OBJECT = new HttpResult(101, "未找到指定对象。");
	public static final HttpResult LACK_VARIABLE = new HttpResult(102, "缺少必须的参数");
	public static final HttpResult ALREADY_EXISTS = new HttpResult(103, "已经存在，不允许再次增加");
	public static final HttpResult TASK_UNAVAILABLE = new HttpResult(104, "任务不存在或当前不可用.");
	public static final HttpResult HANDLE_NOT_ASSIGN = new HttpResult(105, "需要所操作未被指定.");
	public static final HttpResult OBJECT_USED_NOT_DELETE = new HttpResult(106, "对象已经被使用，不能删除");
	public static final HttpResult SUCCESS = new HttpResult(0, "成功", true);


	private static final String format = "{\"code\":%1$d,\"data\":%2$s,\"message\":\"%3$s\"}";
	private JSONObject data = null;
	private final int code;
	private String message;
	private final boolean success;
	private StringBuilder builder;

	public HttpResult() {
		this(0, "OK", true);
	}

	public HttpResult(int code, String message) {
		this(code, message, false);
	}

	public HttpResult(int code, String message, Boolean success) {
		this.code = code;
		this.message = message;
		this.success = success;
	}

	public JSONObject getData() {
		return data;
	}

	/**
	 * 构造一个返回对象.
	 * <p>
	 * 此方法所指定的data 参数将直接赋值。
	 * 如果此对象可能在其他地方被调用，使用new JSONObject(data)方式传入参数。
	 *
	 * @param code    返回码
	 * @param message 返回消息
	 * @param success 是否成功
	 * @param data    返回的数据
	 */
	public HttpResult(int code, String message, Boolean success, JSONObject data) {
		this(code, message, success);

		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public HttpResult put(String name, Object value) {
		if (data == null) {
			data = new JSONObject();
		}

		data.put(name, value);
		return this;
	}

	public HttpResult setData(JSONObject data) {
		this.data = data;
		return this;
	}

	/**
	 * 需要双引号时，自己增加.
	 *
	 * @param value 传入的值。可以使用String的json格式。
	 * @return 对象本身
	 */
	public HttpResult setValue(String value) {
		this.builder = new StringBuilder(value);
		return this;
	}

	/**
	 * 用以将数据库列表字段转成可以向前面发送的数据格式.
	 *
	 * @param name    需要向前台显示的名称
	 * @param set     需要转换的列表
	 * @param columns 需要转换的列表当中某些列
	 * @return 返回自身
	 */
	public HttpResult setListToData(String name, ResultSet set, String... columns) {
		builder = new StringBuilder();

		try {
			while (set.next()) {
				builder.append("{");
				for (int i = 0; i < columns.length; i++) {
					if (i > 0) {
						builder.append(",");
					}

					builder.append("\"").append(columns[i]).append("\":")
						.append(StringUtil.converToJSONString(set.getObject(columns[i])));
				}

				builder.append("},");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (builder.length() > 0) {
			builder.delete(builder.length() - 1, builder.length());
		}

		builder.insert(0, "\":[").insert(0, name).insert(0, "{\"").append("]}");

		return this;
	}

	/**
	 * 用以将数据库列表字段转成可以向前面发送的数据格式.
	 *
	 * @param name    需要向前台显示的名称
	 * @param lists   需要转换的列表
	 * @param columns 需要转换的列表当中某些列
	 * @return 返回自身
	 */
	public HttpResult setListToData(String name, List<? extends Persistence> lists, String... columns) {
		builder = new StringBuilder();

		for (Persistence item : lists) {
			addJsonToBuilder(item, columns);
			builder.append(",");
		}

		if (builder.length() > 0) {
			builder.delete(builder.length() - 1, builder.length());
		}

		builder.insert(0, "\":[").insert(0, name).insert(0, "{\"").append("]}");

		return this;
	}

	private void addJsonToBuilder(Persistence item, String... columns) {
		builder.append("{");
		for (int i = 0; i < columns.length; i++) {
			if (i > 0) {
				builder.append(",");
			}

			builder.append("\"").append(columns[i]).append("\":")
				.append(StringUtil.converToJSONString(item.get(columns[i])));
		}

		builder.append("}");
	}

	/**
	 * 用以将数据库列表字段转成可以向前面发送的数据格式.
	 *
	 * @param name        需要向前台显示的名称
	 * @param persistence 数据对象。
	 * @param columns     需要转换的列表当中某些列
	 * @return 返回自身
	 */
	public HttpResult addInfoToValue(String name, Persistence persistence, String... columns) {
		if (builder == null) {
			builder = new StringBuilder();
			builder.append("{");
		} else {
			builder.replace(builder.length() - 1, builder.length(), ",");
		}

		builder.append("\"").append(name).append("\"").append(":");
		addJsonToBuilder(persistence, columns);

		builder.append("}");
		return this;
	}

	/**
	 * 用以将数据库列表字段转成可以向前面发送的数据格式.
	 *
	 * @param name  需要向前台显示的名称
	 * @param value 值
	 * @return 返回自身
	 */
	public HttpResult addInfoToValue(String name, Object value) {
		if (builder == null) {
			builder = new StringBuilder();
			builder.append("{");
		} else {
			builder.replace(builder.length() - 1, builder.length(), ",");
		}

		builder.append("\"").append(name).append("\"").append(":").append(StringUtil.converToJSONString(value));

		builder.append("}");
		return this;
	}


	@Override
	public String toString() {
		if (builder != null) {
			return String.format(format, code, builder.toString(), message);
		}

		if (data != null) {
			return String.format(format, code, data.toJSONString(), message);
		}

		return String.format(format, code, "{}", message);
	}

	public HttpResult clone() {
		JSONObject jsonData = data == null ? null : new JSONObject(data);
		return new HttpResult(code, message, success, jsonData);
	}

	public HttpResult setMessage(String message) {
		this.message = message;
		return this;
	}

}
