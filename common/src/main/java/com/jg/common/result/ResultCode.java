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
public final class ResultCode {
	public static final ResultCode NORMAL = new ResultCode(0, "正常结束");
	public static final ResultCode ISNULL = new ResultCode(1, "返回结果为空");
	public static final ResultCode NO_PARAMETER = new ResultCode(2, "未指定参数，请检查", false);
	public static final ResultCode NO_SET_REQUEST_TYPE = new ResultCode(3, "未写请求参数，请检查", false);
	public static final ResultCode UNKNOWN = new ResultCode(-1, "未知错误", false);
	public static final ResultCode NOT_LOGIN = new ResultCode(4, "用户未登录", false);
	public static final ResultCode NOT_FOUND_OBJECT = new ResultCode(101, "未找到指定对象。", false);
	public static final ResultCode LACK_VARIABLE = new ResultCode(102, "缺少必须的参数");
	public static final ResultCode ALREADY_EXISTS = new ResultCode(103, "已经存在，不允许再次增加");
	public static final ResultCode SUCCESS = new ResultCode(0, "成功");


	private static final String format = "{\"code\":%1$d,\"data\":%2$s}";
	private JSONObject data = null;
	private final int code;
	private final String message;
	private final boolean success;
	private String value = null;

	public ResultCode() {
		this(0, "OK");
	}

	public ResultCode(int code, String message) {
		this(code, message, true);
	}

	public ResultCode(int code, String message, Boolean success) {
		this.code = code;
		this.message = message;
		this.success = success;
	}

	public ResultCode(int code, String message, Boolean success, JSONObject data) {
		this(code, message, success);

		if (data == null) {
			this.data = null;
		} else {
			this.data = new JSONObject(data);
		}
	}

	public boolean isSuccess() {
		return success;
	}

	public ResultCode put(String name, Object value) {
		if (data == null) {
			data = new JSONObject();
		}

		data.put(name, value);
		return this;
	}

	/**
	 * 需要双引号时，自己增加.
	 *
	 * @param value 传入的值。可以使用String的json格式。
	 * @return 对象本身
	 */
	public ResultCode setValue(String value) {
		this.value = value;
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
	public ResultCode setListToData(String name, ResultSet set, String... columns) {
		StringBuilder builder = new StringBuilder();

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

		value = builder.toString();
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
	public ResultCode setListToData(String name, List<? extends Persistence> lists, String... columns) {
		StringBuilder builder = new StringBuilder();

		for (Persistence item : lists) {
			builder.append("{");
			for (int i = 0; i < columns.length; i++) {
				if (i > 0) {
					builder.append(",");
				}

				builder.append("\"").append(columns[i]).append("\":")
					.append(StringUtil.converToJSONString(item.get(columns[i])));
			}

			builder.append("},");
		}

		if (builder.length() > 0) {
			builder.delete(builder.length() - 1, builder.length());
		}

		builder.insert(0, "\":[").insert(0, name).insert(0, "{\"").append("]}");

		value = builder.toString();
		return this;
	}


	@Override
	public String toString() {
		if (value != null) {
			return String.format(format, code, value);
		}

		if (data != null) {
			return String.format(format, code, data.toJSONString());
		}

		return String.format(format, code, "{}");
	}

	public int getCode() {
		return code;
	}

	public ResultCode clone() {
		return new ResultCode(code, message, success, data);
	}

}
