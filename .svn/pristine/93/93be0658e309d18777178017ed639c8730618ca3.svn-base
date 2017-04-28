package com.jg.common.persistence;

/**
 * 写入数据值异常.
 * <p>
 * 通过调用{@link #getName()}和{@link #getValue()}得到出现错误的名称和值。
 */
public class WriteValueException extends RuntimeException {
	private final String name;
	private final Object value;

	public WriteValueException(String name, Object value, String message) {
		super(message);
		this.name = name;
		this.value = value;
	}

	@Override
	public String getMessage() {
		return super.getMessage() + "name:" + name + " value:" + value;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}
}
